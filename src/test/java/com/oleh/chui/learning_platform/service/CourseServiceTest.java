package com.oleh.chui.learning_platform.service;

import com.oleh.chui.learning_platform.entity.Course;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Test
    void getById() {
        List<Course> courseList = courseService.getAll();
        if (courseList.size() > 0) {
            Long courseId = courseList.get(0).getId();

            Course foundCourse = courseService.getById(courseId);

            assertEquals(courseId, foundCourse.getId());
        } else {
            fail("course table is empty!");
        }

    }

    @Test
    void getAllByFilters() {
        List<Course> courseList = courseService.getAll();

        if (courseList.size() > 0) {
            Course courseObj = courseList.get(0);
            String category = courseObj.getCategory();
            String language = courseObj.getLanguage();

            Set<Course> courseSet = courseService.getAllByFilters(category, language,
                    BigDecimal.valueOf(Long.MIN_VALUE), BigDecimal.valueOf(Long.MAX_VALUE));

            assertTrue(courseSet.stream().allMatch(course -> course.getCategory().equals(category) && course.getLanguage().equals(language)));
        } else {
            fail("course table is empty!");
        }

    }
}