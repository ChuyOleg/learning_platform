package com.oleh.chui.learning_platform.service;

import com.oleh.chui.learning_platform.dto.CourseDTO;
import com.oleh.chui.learning_platform.dto.MaterialDtoList;
import com.oleh.chui.learning_platform.dto.QuestionDtoList;
import com.oleh.chui.learning_platform.entity.*;
import com.oleh.chui.learning_platform.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
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
}
