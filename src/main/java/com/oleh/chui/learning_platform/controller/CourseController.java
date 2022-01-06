package com.oleh.chui.learning_platform.controller;

import com.oleh.chui.learning_platform.dto.CourseDTO;
import com.oleh.chui.learning_platform.dto.PersonDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/course")
public class CourseController {

    @GetMapping("new")
    public String create(Model model) {
        model.addAttribute("course", new CourseDTO());

        return "course/create";
    }

    @PostMapping()
    public String addNewPerson(Model model, @ModelAttribute("course") @Valid CourseDTO courseDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "course/create";
        }

        return "redirect:/course";
    }

}
