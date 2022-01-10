package com.oleh.chui.learning_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class QuestionDtoList {

    @NotEmpty
    private List<QuestionDTO> questionDTOList;

    public QuestionDtoList() {
        questionDTOList = new ArrayList<>();
    }

    public void addQuestionDTO(QuestionDTO questionDTO) {
        this.questionDTOList.add(questionDTO);
    }

}
