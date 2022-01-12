package com.oleh.chui.learning_platform.service;

import com.oleh.chui.learning_platform.entity.Answer;
import com.oleh.chui.learning_platform.repository.AnswerRepository;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public void save(Answer answer) {
        answerRepository.save(answer);
    }
}
