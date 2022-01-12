package com.oleh.chui.learning_platform.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private Set<Answer> answerSet;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    public Question(String question, Set<Answer> answerSet) {
        this.question = question;
        this.answerSet = answerSet;
    }

}
