package com.oleh.chui.learning_platform.repository;

import com.oleh.chui.learning_platform.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findPersonByUsername(String username);

    Optional<Person> findPersonById(Long id);

}
