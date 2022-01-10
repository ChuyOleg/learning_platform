package com.oleh.chui.learning_platform.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDTO {

    private String question;

    private String answer1;

    private String answer2;

    private String answer3;

    private String correctAnswer;

}
