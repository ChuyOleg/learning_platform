package com.oleh.chui.learning_platform.service;

import com.oleh.chui.learning_platform.dto.PersonDTO;
import com.oleh.chui.learning_platform.entity.Person;
import com.oleh.chui.learning_platform.entity.PersonDetails;
import com.oleh.chui.learning_platform.entity.Role;
import com.oleh.chui.learning_platform.repository.PersonRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService implements UserDetailsService {

    private final PersonRepository personRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public PersonService(PersonRepository personRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public void createUser(PersonDTO personDTO) {
        Person person = new Person(personDTO);
        Role role = roleService.getRoleAndCreateIfNeed(Role.RoleEnum.USER);
        PersonDetails personDetails = PersonDetails.builder()
                .money(BigDecimal.valueOf(0)).taxNumber(personDTO.getTaxNumber()).build();
        person.setRole(role);
        person.setPersonDetails(personDetails);
        person.setPassword(passwordEncoder.encode(person.getPassword()));

        personRepository.save(person);
    }

    public boolean isPersonAlreadyExistByUsername(String username) {
        Optional<Person> person = personRepository.findPersonByUsername(username);
        return person.isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findPersonByUsername(username);

        return person.orElseThrow(() -> new UsernameNotFoundException("Not found " + username));
    }
}
