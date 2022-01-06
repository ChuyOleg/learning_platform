package com.oleh.chui.learning_platform.repository;

import com.oleh.chui.learning_platform.entity.PersonDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonDetailsRepository extends JpaRepository<PersonDetails, Long> {

    Optional<PersonDetails> findPersonDetailsByTaxNumber(String taxNumber);

}
