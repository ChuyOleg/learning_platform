package com.oleh.chui.learning_platform.controller;

import com.oleh.chui.learning_platform.dto.CourseDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/course")
public class CourseController {

    @GetMapping("/new")
    public String getCourseCreatingBaseInfoPage(HttpSession session, Model model) {
        CourseDTO courseDTO = (CourseDTO) session.getAttribute("courseDTO");

        if (courseDTO == null) {
            courseDTO = new CourseDTO();
        }

        model.addAttribute("course", courseDTO);

        return "course/createCourseBaseInfo";
    }


    @PostMapping("/new")
    public String addCourseInfoToSession(HttpSession session, Model model,
                                                @ModelAttribute("course") @Valid CourseDTO courseDTO,
                                                BindingResult result) {

        if (result.hasErrors()) {
            return "course/createCourseBaseInfo";
        } else {
            session.setAttribute("courseDTO", courseDTO);
        }

        return "redirect:/course/material/new";

    }

    @GetMapping("/material/new")
    public String getMaterialPage(HttpSession session, Model model) {
        String[] materialList = (String[]) session.getAttribute("materialList");

        if (materialList == null) {
            materialList = new String[1];
            materialList[0] = "";
        }

        model.addAttribute("materialList", materialList);

        return "course/createMaterial";
    }

    @PostMapping("/material/new")
    public String addMaterialInfoToSession(HttpSession session, @RequestParam("material") Optional<String[]> materialListOptional,
                                           Model model) {

        if (!materialListOptional.isPresent()) {
            model.addAttribute("materialIsEmptyError", true);
            String[] materialList = {""};
            model.addAttribute("materialList", materialList);
            return "course/createMaterial";
        }

        String[] materialList = materialListOptional.get();

        for (String material : materialList) {
            if (material.isEmpty()) {
                model.addAttribute("materialIsEmptyError", true);
                model.addAttribute("materialList", materialList);
                return "course/createMaterial";
            }
        }

        session.setAttribute("materialList", materialList);
        return "redirect:/course/question/new";
    }

    @GetMapping("/question/new")
    public String getQuestionsPage() {
        return "course/createQuestions";
    }

    @PostMapping("/question/new")
    public String saveCourse(HttpSession session, @RequestParam("question") String[] questionList) {
        return "person/all";
    }


}
