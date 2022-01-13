package com.oleh.chui.learning_platform.service;

import com.oleh.chui.learning_platform.entity.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Test
    void getPersonById() {
        List<Person> personList = personService.getAllUsers();
        if (personList.size() > 0) {
            Long personId = personList.get(0).getId();
            Person foundPerson = personService.getPersonById(personId);

            assertEquals(personId, foundPerson.getId());
        } else {
            fail("person Table is empty");
        }
    }

    @Test
    void isPersonAlreadyExistByUsername() {
        List<Person> personList = personService.getAllUsers();
        if (personList.size() > 0) {
            String username = personList.get(0).getUsername();
            boolean isPersonExist = personService.isPersonAlreadyExistByUsername(username);

            assertTrue(isPersonExist);
        } else {
            fail("person Table is empty");
        }
    }

}