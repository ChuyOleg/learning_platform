package com.oleh.chui.learning_platform.service;

import com.oleh.chui.learning_platform.entity.Question;
import com.oleh.chui.learning_platform.repository.QuestionRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question save(Question question) {
        return questionRepository.save(question);
    }
}
