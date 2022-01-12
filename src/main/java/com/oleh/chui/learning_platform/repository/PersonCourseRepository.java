package com.oleh.chui.learning_platform.repository;

import com.oleh.chui.learning_platform.entity.Person_Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonCourseRepository extends JpaRepository<Person_Course, Long> {

}
