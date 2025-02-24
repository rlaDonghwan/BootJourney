package com.BootJourney.Controller;

import java.security.Principal;
import java.util.List;

import com.BootJourney.Entity.User;
import com.BootJourney.Exception.DataNotFoundException;
import com.BootJourney.Form.AnswerForm;
import com.BootJourney.Form.QuestionForm;
import com.BootJourney.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.BootJourney.Entity.Question;
import com.BootJourney.Repository.QuestionRepository;
import com.BootJourney.Service.QuestionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;

//	public QuestionController(QuestionRepository questionRepository) { 위에 DI를 하지 않으면 이렇게 생성자를 써줘야함
//		this.questionRepository = questionRepository;
//	}

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Question> paging = this.questionService.getList(page);
        model.addAttribute("paging", paging);
        return "question_list";
    }

    @GetMapping("/detail/{id}")
    public String questionDetail(@PathVariable("id") Integer id, Model model) {
        try {
            Question question = questionService.getQuestion(id);
            model.addAttribute("question", question);
            model.addAttribute("answerForm", new AnswerForm()); // ✅ answerForm 추가

            return "question_detail";
        } catch (DataNotFoundException e) {
            return "redirect:/"; //존재하지 않는 질문이면 홈으로 리디렉트
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(Model model) {
        model.addAttribute("questionForm", new QuestionForm());
        return "question_form";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) throws DataNotFoundException { //폼객체를 통해 값을 받고 bindingResult가 검증
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        User user = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), user);
        return "redirect:/question/list"; //질문 저장 후 질문 목록으로 이동
    }


}
