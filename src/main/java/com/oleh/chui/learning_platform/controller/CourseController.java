package com.oleh.chui.learning_platform.controller;

import com.oleh.chui.learning_platform.dto.*;
import com.oleh.chui.learning_platform.entity.Course;
import com.oleh.chui.learning_platform.entity.Person;
import com.oleh.chui.learning_platform.service.CourseService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

// TODO
// add logger
// add tests
// think front validation
// catalog shows all excluded if user is creator or course is paid
// div size in header
// course description set textarea
// decompose this controller
// update activeUserInfo in SecuritySession after every update
// check SecurityConfig permissions for all URLs
// add free course and implement this logic

@Controller
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/createdCourses")
    public String getCreatedCoursesPage(Model model) {
        Person activeUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        get created courses by userId

        model.addAttribute("courseList", activeUser.getCourseSet());

        return "course/createdCourses";
    }

    @GetMapping("/purchased")
    public String getPurchasedCoursesPage(Model model) {
        Person activeUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//          get selected courses by userId

        model.addAttribute("courseList", activeUser.getSelectedCourses());

        return "course/purchasedCourses";
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
    public String addCourseInfoToSession(HttpSession session, @ModelAttribute("course") @Valid CourseDTO courseDTO,
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
    public String saveCourse(HttpSession session, Model model, WebRequest webRequest, RedirectAttributes redirectAttributes,
                             @ModelAttribute("questionDtoList") @Valid QuestionDtoList questionDtoList,
                             BindingResult result) {

        for (QuestionDTO questionDTO : questionDtoList.getQuestionDTOList()) {
            if (questionDTO.getQuestion().isEmpty())
                return getQuestionPageWithQuestionIsEmptyErrorMessage(model);
            else if (questionDTO.getAnswer1().isEmpty() || questionDTO.getAnswer2().isEmpty() || questionDTO.getAnswer3().isEmpty())
                return getQuestionPageWithAnswerTextIsEmptyErrorMessage(model);
            else if (questionDTO.getCorrectAnswer() == null)
                return getQuestionPageWithAnswerRadioIsEmptyErrorMessage(model);
        }

        if (result.hasErrors())
            return getQuestionPageWithZeroQuestionsErrorMessage(model);

        session.setAttribute("questionDtoList", questionDtoList);
        CourseDTO courseDTO = (CourseDTO) session.getAttribute("courseDTO");
        MaterialDtoList materialDtoList = (MaterialDtoList) session.getAttribute("materialDtoList");
        Person activeUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String validateResult = validateCourseInfoAndMaterials(courseDTO, materialDtoList);
        if (validateResult.equals("courseBaseInfoIsEmptyError")) {
            return getRedirectedCourseBaseInfoPageWithErrorMessage(redirectAttributes);
        } else if (validateResult.equals("materialIsEmptyError")) {
            return getRedirectedMaterialPageWithErrorMessage(redirectAttributes);
        } else {
            courseService.saveCourse(courseDTO, materialDtoList, questionDtoList, activeUser);
            clearCourseInfoFromSession(webRequest);
        }

        return "redirect:/course/created";
    }

    @GetMapping("/created")
    public String getSuccessfullyCreationCoursePage() {
        return "course/successfulCreationPage";
    }

    @GetMapping("/all")
    public String getCatalog(Model model) {
        List<Course> courseList = courseService.getAll();
        model.addAttribute("courseList", courseList);

        return "course/catalogPage";
    }

    private void clearCourseInfoFromSession(WebRequest webRequest) {
        webRequest.removeAttribute("courseDTO", WebRequest.SCOPE_SESSION);
        webRequest.removeAttribute("materialDtoList", WebRequest.SCOPE_SESSION);
        webRequest.removeAttribute("questionDtoList", WebRequest.SCOPE_SESSION);
    }

    private String getQuestionPageWithZeroQuestionsErrorMessage(Model model) {
        QuestionDtoList defaultQuestionDtoList = new QuestionDtoList();
        defaultQuestionDtoList.addQuestionDTO(new QuestionDTO("", "", "", "", ""));
        model.addAttribute("zeroQuestionsError", true);
        model.addAttribute("questionDtoList", defaultQuestionDtoList);
        return "course/createQuestions";
    }

    private String getQuestionPageWithQuestionIsEmptyErrorMessage(Model model) {
        model.addAttribute("questionIsEmptyError", true);
        return "course/createQuestions";
    }

    private String getQuestionPageWithAnswerTextIsEmptyErrorMessage(Model model) {
        model.addAttribute("answerTextIsEmptyError", true);
        return "course/createQuestions";
    }

    private String getQuestionPageWithAnswerRadioIsEmptyErrorMessage(Model model) {
        model.addAttribute("answerRadioIsEmptyError", true);
        return "course/createQuestions";
    }

    private String getRedirectedCourseBaseInfoPageWithErrorMessage(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("courseBaseInfoIsEmptyError", true);
        return "redirect:/course/new";
    }

    private String getRedirectedMaterialPageWithErrorMessage(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("materialIsEmptyError", true);
        return "redirect:/course/material/new";
    }

    private String validateCourseInfoAndMaterials(CourseDTO courseDTO, MaterialDtoList materialDtoList) {
        if (courseDTO == null)
            return "courseBaseInfoIsEmptyError";
        else if (materialDtoList == null)
            return "materialIsEmptyError";
        else
            return "No errors";
    }

}
