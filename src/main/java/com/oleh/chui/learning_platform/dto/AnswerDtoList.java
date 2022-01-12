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
public class AnswerDtoList {

    @NotEmpty
    private List<AnswerDTO> answerDTOList;

    public AnswerDtoList() {
        answerDTOList = new ArrayList<>();
    }

    public void addAnswerDTO(AnswerDTO answerDTO) {
        this.answerDTOList.add(answerDTO);
    }

}
