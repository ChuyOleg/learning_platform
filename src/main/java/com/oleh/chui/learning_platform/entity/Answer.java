package com.oleh.chui.learning_platform.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "answers")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answer;

    @Column(name = "is_correct")
    private boolean isCorrect;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    public Answer(String answer, boolean isCorrect) {
        this.answer = answer;
        this.isCorrect = isCorrect;
    }

}
