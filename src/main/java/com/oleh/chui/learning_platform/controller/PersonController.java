package com.oleh.chui.learning_platform.controller;

import com.oleh.chui.learning_platform.service.PersonService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Collection;


@Controller
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    public String getPersons(Model model) {
        model.addAttribute("personList", personService.getAllPersons());

        return "person/all";
    }

    @GetMapping("/current")
    public String getCurrent() {
        Collection<SimpleGrantedAuthority> authorities =
                (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        for (SimpleGrantedAuthority role : authorities) {
            System.out.println(role);
        }
        return "ROLES";
    }

}
