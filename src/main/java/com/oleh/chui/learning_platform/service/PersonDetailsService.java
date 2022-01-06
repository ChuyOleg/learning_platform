package com.oleh.chui.learning_platform.service;

import com.oleh.chui.learning_platform.entity.PersonDetails;
import com.oleh.chui.learning_platform.repository.PersonDetailsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService {

    private final PersonDetailsRepository personDetailsRepository;

    public PersonDetailsService(PersonDetailsRepository personDetailsRepository) {
        this.personDetailsRepository = personDetailsRepository;
    }

    public boolean isTaxNumberAlreadyExist(String taxNumber) {
        Optional<PersonDetails> personDetails = personDetailsRepository.findPersonDetailsByTaxNumber(taxNumber);

        return personDetails.isPresent();
    }
}
