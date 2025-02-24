package com.BootJourney.Controller;

import com.BootJourney.Entity.Question;
import com.BootJourney.Entity.User;
import com.BootJourney.Exception.DataNotFoundException;
import com.BootJourney.Form.AnswerForm;
import com.BootJourney.Service.AnswerService;
import com.BootJourney.Service.QuestionService;

import com.BootJourney.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(
            Model model,
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("answerForm") AnswerForm answerForm,
            BindingResult bindingResult,
            Principal principal
    ) throws DataNotFoundException {

        Question question = this.questionService.getQuestion(id);
        User user = this.userService.getUser(principal.getName());
        model.addAttribute("question", question);

        // ✅ 유효성 검사 실패 시 다시 question_detail.html을 렌더링
        if (bindingResult.hasErrors()) {
            model.addAttribute("answerForm", answerForm); // ✅ 폼 데이터 유지
            return "question_detail";
        }

        this.answerService.create(question, answerForm.getContent(), user);
        return String.format("redirect:/question/detail/%s", id);
    }
}