package com.oleh.chui.learning_platform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answer;

    @Column(name = "is_correct")
    private boolean isCorrect;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

}
