package com.oleh.chui.learning_platform.service;

import com.oleh.chui.learning_platform.entity.Person_Course;
import com.oleh.chui.learning_platform.repository.PersonCourseRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonCourseService {

    private final PersonCourseRepository personCourseRepository;

    public PersonCourseService(PersonCourseRepository personCourseRepository) {
        this.personCourseRepository = personCourseRepository;
    }

    public void save(Person_Course person_course) {
        personCourseRepository.save(person_course);
    }
}
