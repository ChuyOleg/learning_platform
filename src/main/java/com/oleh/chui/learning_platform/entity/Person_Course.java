package com.oleh.chui.learning_platform.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "person_learn_course")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Person_Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    Person student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    Course course;

    boolean finished;

    Integer mark;

    public Person_Course(Person student, Course course) {
        this.student = student;
        this.course = course;
    }

}
