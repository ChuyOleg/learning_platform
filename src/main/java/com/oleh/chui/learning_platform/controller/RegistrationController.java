package com.oleh.chui.learning_platform.controller;

import com.oleh.chui.learning_platform.dto.PersonDTO;
import com.oleh.chui.learning_platform.service.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final PersonService personService;

    public RegistrationController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    public String newPerson(Model model) {
        model.addAttribute("person", new PersonDTO());

        return "registration";
    }

    @PostMapping()
    public String addNewPerson(@ModelAttribute("person") @Valid PersonDTO person,
                               BindingResult result) {
        if (result.hasErrors())
            return "registration";

//        personService.createUser(person);

        return "redirect:/person";
    }

}
