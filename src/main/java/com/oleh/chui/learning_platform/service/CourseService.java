package com.oleh.chui.learning_platform.service;

import com.oleh.chui.learning_platform.dto.AnswerDtoList;
import com.oleh.chui.learning_platform.dto.CourseDTO;
import com.oleh.chui.learning_platform.dto.MaterialDtoList;
import com.oleh.chui.learning_platform.dto.QuestionDtoList;
import com.oleh.chui.learning_platform.entity.*;
import com.oleh.chui.learning_platform.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final PersonService personService;
    private final MaterialService materialService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final PersonCourseService personCourseService;

    public CourseService(CourseRepository courseRepository, PersonService personService, MaterialService materialService, QuestionService questionService, AnswerService answerService, PersonCourseService personCourseService) {
        this.courseRepository = courseRepository;
        this.personService = personService;
        this.materialService = materialService;
        this.questionService = questionService;
        this.answerService = answerService;
        this.personCourseService = personCourseService;
    }

    public void saveCourse(CourseDTO courseDTO,
                           MaterialDtoList materialDtoList,
                           QuestionDtoList questionDtoList,
                           Person creator) {

        Set<Material> materialSet = new HashSet<>();
        Set<Question> questionSet = new HashSet<>();

        materialDtoList.getMaterialDTOList().forEach(material -> materialSet.add(new Material(material.getMaterial())));

        questionDtoList.getQuestionDTOList().forEach(question -> {
            Set<Answer> answerSet = new HashSet<>(Arrays.asList(
                    new Answer(question.getAnswer1(), question.getCorrectAnswer().equals("answer1")),
                    new Answer(question.getAnswer2(), question.getCorrectAnswer().equals("answer2")),
                    new Answer(question.getAnswer3(), question.getCorrectAnswer().equals("answer3"))
            ));

            questionSet.add(new Question(question.getQuestion(), answerSet));
        });

        Course course = new Course(courseDTO, materialSet, questionSet, creator);
        Course createdCourse = courseRepository.save(course);

        materialSet.forEach(mat -> {
           mat.setCourse(createdCourse);
           materialService.save(mat);
        });

        questionSet.forEach(question -> {
            question.setCourse(createdCourse);
            Question createdQuestion = questionService.save(question);
            question.getAnswerSet().forEach(answer -> {
                answer.setQuestion(createdQuestion);
                answerService.save(answer);
            });
        });

    }

    public void userBuyCourse(Person activeUser, Long courseId) {
        Person person = personService.getPersonById(activeUser.getId());
        Course course = courseRepository.getById(courseId);

        person.getPerson_course_Set().add(new Person_Course(person, course));

        personService.changeBalance(activeUser, course.getPrice().negate());
        activeUser.setPerson_course_Set(person.getPerson_course_Set());

        personService.save(person);
    }

    public int finishCourseAndGetMark(Person activeUser, Course course, AnswerDtoList answerDtoList) {
        int correctAnswerCounter = 0;
        int counter = 0;
        for (Question question : course.getQuestionSet()) {
            for (Answer answer : question.getAnswerSet()) {
                if (answer.isCorrect() && answer.getAnswer().equals(answerDtoList.getAnswerDTOList().get(counter).getAnswer())) {
                    correctAnswerCounter++;
                }
            }
            counter++;
        }

        int mark = correctAnswerCounter * 100 / course.getQuestionSet().size();

        course.getPerson_courseSet().forEach(person_course -> {
            if (Objects.equals(person_course.getStudent().getId(), activeUser.getId())) {
                person_course.setFinished(true);
                person_course.setMark(mark);
                personCourseService.save(person_course);
                Person person = personService.getPersonById(activeUser.getId());
                activeUser.setPerson_course_Set(person.getPerson_course_Set());
            }
        });

        return mark;
    }

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    public Course getById(Long id) {
        return courseRepository.getById(id);
    }

    public Set<Course> getAllByFilters(String category, String language, BigDecimal minPrice, BigDecimal maxPrice) {
        Set<Course> courseSet = new HashSet<>(getAll());
        return filterByPrice(filterByLanguage(filterByCategory(courseSet, category), language), minPrice, maxPrice);
    }

    public Set<Course> getCoursesForCatalog() {
        return new HashSet<>(getAll());
    }

    public Set<Course> getCoursesForCatalog(Long personId) {
        Person person = personService.getPersonById(personId);
        List<Course> allCourses = courseRepository.findAll();
        Set<Course> createdCourses = person.getCourseSet();
        Set<Course> selectedCourses = getPurchasedCourses(personId);

        return allCourses.stream()
                .filter(course -> !createdCourses.contains(course) && !selectedCourses.contains(course))
                .collect(Collectors.toSet());
    }

    public Set<Course> getCoursesForCatalogByFilters(Long personId, String category, String language, BigDecimal minPrice, BigDecimal maxPrice) {
        Set<Course> courseSet = getCoursesForCatalog(personId);
        return filterByPrice(filterByLanguage(filterByCategory(courseSet, category), language), minPrice, maxPrice);
    }

    public Set<Course> getPurchasedCourses(Long personId) {
        Person person = personService.getPersonById(personId);
        Set<Course> courseSet = new HashSet<>();
        person.getPerson_course_Set().forEach(data -> courseSet.add(data.getCourse()));
        return courseSet;
    }

    public Set<Course> getCreatedCourses(Long personId) {
        Person person = personService.getPersonById(personId);
        return person.getCourseSet();
    }

    private Set<Course> filterByCategory(Set<Course> courseSet, String category) {
        if (!category.isEmpty()) {
            return courseSet.stream().
                    filter(course -> course.getCategory().equals(category)).collect(Collectors.toSet());
        } else {
            return courseSet;
        }
    }

    private Set<Course> filterByLanguage(Set<Course> courseSet, String language) {
        if (!language.isEmpty()) {
            return courseSet.stream().
                    filter(course -> course.getLanguage().equals(language)).collect(Collectors.toSet());
        } else {
            return courseSet;
        }
    }

    private Set<Course> filterByPrice(Set<Course> courseSet, BigDecimal minPrice, BigDecimal maxPrice) {
        return courseSet.stream().filter(course -> (course.getPrice().compareTo(minPrice) >= 0) &&
                (course.getPrice().compareTo(maxPrice) <= 0)).collect(Collectors.toSet());
    }

}
