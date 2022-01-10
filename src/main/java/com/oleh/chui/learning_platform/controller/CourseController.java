package com.oleh.chui.learning_platform.controller;

import com.oleh.chui.learning_platform.dto.*;
import com.oleh.chui.learning_platform.entity.Course;
import com.oleh.chui.learning_platform.entity.Person;
import com.oleh.chui.learning_platform.service.CourseService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

// TODO
// implement creating course
// clear session after creating course
// add (do login) user after registration
// do one general validation before creating course
// think about toString methods
// change language selection via creating course language
// add logger
// add tests
// think front validation
// create header

@Controller
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

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

        MaterialDtoList materialDtoList = (MaterialDtoList) session.getAttribute("materialDtoList");

        if (materialDtoList == null) {
            materialDtoList = new MaterialDtoList();
            MaterialDTO materialDTO = new MaterialDTO("");
            materialDtoList.addMaterialDTO(materialDTO);
        }

        model.addAttribute("materialDtoList", materialDtoList);

        return "course/createMaterial";
    }

    @PostMapping("/material/new")
    public String addMaterialInfoToSession(HttpSession session, Model model,
                                           @ModelAttribute("materialDtoList") @Valid MaterialDtoList materialDtoList,
                                           BindingResult result) {

        for (MaterialDTO materialDTO : materialDtoList.getMaterialDTOList()) {
            if (materialDTO.getMaterial().isEmpty()) {
                model.addAttribute("materialIsEmptyError", true);
                return "course/createMaterial";
            }
        }

        if (result.hasErrors()) {
            MaterialDtoList defaultMaterialDtoList = new MaterialDtoList();
            defaultMaterialDtoList.addMaterialDTO(new MaterialDTO(""));
            model.addAttribute("materialIsEmptyError", true);
            model.addAttribute("materialDtoList", defaultMaterialDtoList);
            return "course/createMaterial";
        } else {
            session.setAttribute("materialDtoList", materialDtoList);
        }

        return "redirect:/course/question/new";
    }

    @GetMapping("/question/new")
    public String getQuestionsPage(HttpSession session, Model model) {

        QuestionDtoList questionDtoList = (QuestionDtoList) session.getAttribute("questionDtoList");

        if (questionDtoList == null) {
            questionDtoList = new QuestionDtoList();
            QuestionDTO questionDTO = new QuestionDTO("", "", "", "", "");
            questionDtoList.addQuestionDTO(questionDTO);
        }

        model.addAttribute("questionDtoList", questionDtoList);

        return "course/createQuestions";
    }

    @PostMapping("/question/new")
    public String saveCourse(HttpSession session, Model model, WebRequest webRequest,
                             @ModelAttribute("questionDtoList") @Valid QuestionDtoList questionDtoList,
                             BindingResult result) {

        for (QuestionDTO questionDTO : questionDtoList.getQuestionDTOList()) {
            if (questionDTO.getQuestion().isEmpty()) {
                model.addAttribute("questionIsEmptyError", true);
                return "course/createQuestions";
            } else if (questionDTO.getAnswer1().isEmpty() || questionDTO.getAnswer2().isEmpty() || questionDTO.getAnswer3().isEmpty()) {
                model.addAttribute("answerTextIsEmptyError", true);
                return "course/createQuestions";
            } else if (questionDTO.getCorrectAnswer() == null) {
                model.addAttribute("answerRadioIsEmptyError", true);
                return "course/createQuestions";
            }
        }

        if (result.hasErrors()) {
            QuestionDtoList defaultQuestionDtoList = new QuestionDtoList();
            defaultQuestionDtoList.addQuestionDTO(new QuestionDTO("", "", "", "", ""));
            model.addAttribute("zeroQuestionsError", true);
            model.addAttribute("questionDtoList", defaultQuestionDtoList);
            return "course/createQuestions";
        } else {
            CourseDTO courseDTO = (CourseDTO) session.getAttribute("courseDTO");
            MaterialDtoList materialDtoList = (MaterialDtoList) session.getAttribute("materialDtoList");
            session.setAttribute("questionDtoList", questionDtoList);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Person customUser = (Person) authentication.getPrincipal();

            courseService.saveCourse(courseDTO, materialDtoList, questionDtoList, customUser);

            clearCourseCreatingInfoFromSession(webRequest);
        }

        return "person/all";
    }

    @GetMapping("/all")
    public String getCatalog(Model model) {
        List<Course> courseList = courseService.getAll();
        model.addAttribute("courseList", courseList);

        return "course/catalogPage";
    }

    private void clearCourseCreatingInfoFromSession(WebRequest webRequest) {
        webRequest.removeAttribute("courseDTO", WebRequest.SCOPE_SESSION);
        webRequest.removeAttribute("materialDtoList", WebRequest.SCOPE_SESSION);
        webRequest.removeAttribute("questionDtoList", WebRequest.SCOPE_SESSION);
    }

}
