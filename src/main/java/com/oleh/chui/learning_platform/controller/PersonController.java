package com.oleh.chui.learning_platform.controller;

import com.oleh.chui.learning_platform.entity.Person;
import com.oleh.chui.learning_platform.service.PersonService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/replenishBalance")
    public String getPersons(HttpServletRequest request, @RequestParam("replenishment") BigDecimal replenishment) {
        Person activeUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        personService.changeBalance(activeUser, replenishment);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/users")
    public String getUsersPage(Model model) {
        List<Person> users = personService.getAllUsers();

        model.addAttribute("userList", users);

        return "/person/all";
    }

    @PostMapping("/users/{id}")
    public String blockOrUnblockUser(@PathVariable("id") Long id) {
        personService.blockOrUnblockUser(id);

        return "redirect:/users";
    }

}
