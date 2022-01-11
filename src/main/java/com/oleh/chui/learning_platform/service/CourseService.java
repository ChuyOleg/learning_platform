package com.oleh.chui.learning_platform.service;

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

    public CourseService(CourseRepository courseRepository, PersonService personService) {
        this.courseRepository = courseRepository;
        this.personService = personService;
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

        courseRepository.save(course);
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
        Set<Course> selectedCourses = person.getSelectedCourses();

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
        return person.getSelectedCourses();
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
