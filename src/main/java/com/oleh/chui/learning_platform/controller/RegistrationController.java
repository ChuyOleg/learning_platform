package com.oleh.chui.learning_platform.controller;

import com.oleh.chui.learning_platform.dto.PersonDTO;
import com.oleh.chui.learning_platform.entity.Person;
import com.oleh.chui.learning_platform.service.PersonDetailsService;
import com.oleh.chui.learning_platform.service.PersonService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final PersonService personService;
    private final PersonDetailsService personDetailsService;

    public RegistrationController(PersonService personService, PersonDetailsService personDetailsService) {
        this.personService = personService;
        this.personDetailsService = personDetailsService;
    }

    @GetMapping()
    public String getRegistrationPage(Model model) {
        model.addAttribute("person", new PersonDTO());

        return "registration";
    }

    @PostMapping()
    public String addNewPerson(Model model, @ModelAttribute("person") @Valid PersonDTO personDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "registration";
        }

        if (!personDTO.getPassword().equals(personDTO.getPasswordCopy())) {
            model.addAttribute("passwordsDontMatch", true);
            return "registration";
        }

        if (LocalDate.now().getYear() - personDTO.getBirthday().getYear() > 150) {
            model.addAttribute("tooOldError", true);
            return "registration";
        }

        if (personService.isPersonAlreadyExistByUsername(personDTO.getUsername())) {
            model.addAttribute("userAlreadyExistsError", true);
            return "registration";
        }

        if (personDetailsService.isTaxNumberAlreadyExist(personDTO.getTaxNumber())) {
            model.addAttribute("taxNumberAlreadyExistsError", true);
            return "registration";
        }

        Person user = personService.createAndGetUser(personDTO);
        autoAuth(user);

        return "redirect:/course/all";
    }

    private void autoAuth(Person user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
