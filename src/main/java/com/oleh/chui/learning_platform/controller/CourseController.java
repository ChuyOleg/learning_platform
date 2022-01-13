package com.oleh.chui.learning_platform.controller;

import com.oleh.chui.learning_platform.dto.AnswerDTO;
import com.oleh.chui.learning_platform.dto.AnswerDtoList;
import com.oleh.chui.learning_platform.entity.Course;
import com.oleh.chui.learning_platform.entity.Person;
import com.oleh.chui.learning_platform.entity.Person_Course;
import com.oleh.chui.learning_platform.service.CourseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Set;


@Controller
@Log4j2
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/createdCourses")
    public String getCreatedCoursesPage(Model model) {
        Person activeUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Set<Course> courseList = courseService.getCreatedCourses(activeUser.getId());

        model.addAttribute("courseList", courseList);

        return "course/createdCourses";
    }

    @GetMapping("/purchased")
    public String getPurchasedCoursesPage(Model model) {
        Person activeUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Set<Course> courseSet = courseService.getPurchasedCourses(activeUser.getId());

        model.addAttribute("courseList", courseSet);

        return "course/purchasedCourses";
    }

    @GetMapping("/all")
    public String getCatalogPage(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Set<Course> courseSet = courseService.getCoursesForCatalog();
            model.addAttribute("courseList", courseSet);
        } else {
            Person activeUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Set<Course> courseSet = courseService.getCoursesForCatalog(activeUser.getId());
            model.addAttribute("courseList", courseSet);
        }

        return "course/catalogPage";
    }

    @GetMapping("/{id}")
    public String getCourseDetailsPage(@PathVariable Long id, Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Course course = courseService.getById(id);
            model.addAttribute("course", course);
        } else {
            Person activeUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Course course = courseService.getById(id);

            if (activeUser.getPerson_course_Set() == null ||
                    activeUser.getPerson_course_Set().stream().noneMatch(data -> data.getCourse().equals(course))) {
                model.addAttribute("canBuy", true);
            } else {
                model.addAttribute("canBuy", false);
            }

            model.addAttribute("course", course);

        }

        return "course/detailsPage";
    }

    @PostMapping("/buy/{id}")
    public String buyCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Person activeUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Course course = courseService.getById(id);

        if (activeUser.getPersonDetails().getMoney().compareTo(course.getPrice()) < 0) {
            redirectAttributes.addFlashAttribute("notEnoughMoneyError", true);
            return "redirect:/course/" + id;
        }

        courseService.userBuyCourse(activeUser, id);
        log.debug(String.format("User (name = %s, id = %d) buy course (id = %d)", activeUser.getUsername(), activeUser.getId(), course.getId()));

        return "redirect:/course/purchased";
    }

    @GetMapping("/all/filter")
    public String applyFiltersForCatalogPage(@RequestParam(name = "category", defaultValue = "") String category,
                                             @RequestParam(name = "language", defaultValue = "") String language,
                                             @RequestParam(name = "minPrice", defaultValue = "0") BigDecimal minPrice,
                                             @RequestParam(name = "maxPrice", defaultValue = "9999999999") BigDecimal maxPrice,
                                             Model model) {

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Set<Course> courseSet = courseService.getAllByFilters(category, language, minPrice, maxPrice);
            model.addAttribute("courseList", courseSet);
        } else {
            Person activeUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Set<Course> courseSet = courseService.getCoursesForCatalogByFilters(activeUser.getId(), category, language, minPrice, maxPrice);
            model.addAttribute("courseList", courseSet);
        }

        return "course/catalogPage";
    }

    @GetMapping("/learning/{id}")
    public String getLearningPage(@PathVariable("id") Long id, Model model) {
        Person activeUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Course course = courseService.getById(id);

        if (activeUser.getPerson_course_Set().stream().noneMatch(data -> data.getCourse().equals(course))) {
            return "redirect:/course/all";
        }

        Person_Course personCourseInfo = course.getPerson_courseSet().stream()
                .filter(person_course -> person_course.getStudent().getId().equals(activeUser.getId())).findFirst().get();

        AnswerDtoList answerDtoList = new AnswerDtoList();
        for (int i = 0; i < course.getQuestionSet().size(); i++) {
            answerDtoList.addAnswerDTO(new AnswerDTO(""));
        }

        model.addAttribute("answerDtoList", answerDtoList);

        model.addAttribute("isFinished", personCourseInfo.isFinished());
        model.addAttribute("mark", personCourseInfo.getMark());
        model.addAttribute("userAnswers", new String[course.getQuestionSet().size()]);
        model.addAttribute("course", course);

        return "/course/learningPage";
    }

    @PostMapping("/learning/{id}")
    public String finishCourse(@ModelAttribute("answerDtoList") AnswerDtoList answerDtoList,
                               @PathVariable("id") Long id,
                               HttpServletRequest httpServletRequest,
                               RedirectAttributes redirectAttributes) {

        Person activeUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Course course = courseService.getById(id);

        if (course.getQuestionSet().size() != answerDtoList.getAnswerDTOList().size()) {
            redirectAttributes.addFlashAttribute("emptyAnswersError", true);
            String referer = httpServletRequest.getHeader("Referer");
            return "redirect:" + referer;
        }

        for (AnswerDTO answerDTO : answerDtoList.getAnswerDTOList()) {
            if (answerDTO.getAnswer() == null) {
                redirectAttributes.addFlashAttribute("emptyAnswersError", true);
                String referer = httpServletRequest.getHeader("Referer");
                return "redirect:" + referer;
            }
        }

        courseService.finishCourseAndGetMark(activeUser, course, answerDtoList);
        log.debug(String.format("User (name = %s, id = %d) has finished course (id = %d)", activeUser.getUsername(), activeUser.getId(), course.getId()));

        String referer = httpServletRequest.getHeader("Referer");
        return "redirect:" + referer;
    }

}
