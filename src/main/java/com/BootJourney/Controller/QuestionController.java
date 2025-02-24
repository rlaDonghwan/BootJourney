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
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.BootJourney.Entity.Question;
import com.BootJourney.Service.QuestionService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor  // Lombok을 사용하여 생성자를 자동으로 생성
@Controller
@RequestMapping("/question") // '/question' 경로로 들어오는 요청을 처리하는 컨트롤러
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;

    // 질문 목록을 조회하는 메서드
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Question> paging = this.questionService.getList(page); // 페이지네이션된 질문 목록 조회
        model.addAttribute("paging", paging); // 모델에 데이터 추가
        return "question_list"; // question_list.html 뷰 반환
    }

    // 특정 질문 상세 정보를 조회하는 메서드
    @GetMapping("/detail/{id}")
    public String questionDetail(@PathVariable("id") Integer id, Model model) {
        try {
            Question question = questionService.getQuestion(id); // ID로 질문 조회
            model.addAttribute("question", question);
            model.addAttribute("answerForm", new AnswerForm()); // 답변 폼 추가
            return "question_detail"; // question_detail.html 뷰 반환
        } catch (DataNotFoundException e) {
            return "redirect:/"; // 질문이 존재하지 않으면 홈으로 리디렉트
        }
    }

    // 질문 생성 폼을 표시하는 메서드 (로그인한 사용자만 접근 가능)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(Model model) {
        model.addAttribute("questionForm", new QuestionForm()); // 빈 폼 객체 생성 후 전달
        return "question_form"; // question_form.html 뷰 반환
    }

    // 질문을 생성하는 메서드 (로그인한 사용자만 접근 가능)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) throws DataNotFoundException {
        if (bindingResult.hasErrors()) { // 폼 검증 실패 시 다시 폼으로 이동
            return "question_form";
        }
        User user = this.userService.getUser(principal.getName()); // 현재 로그인한 사용자 정보 가져오기
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), user); // 질문 생성
        return "redirect:/question/list"; // 질문 목록 페이지로 리디렉트
    }

    // 질문 수정 폼을 표시하는 메서드 (로그인한 사용자만 접근 가능)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) throws DataNotFoundException {
        Question question = this.questionService.getQuestion(id); // ID로 질문 조회
        if (!question.getAuthor().getUsername().equals(principal.getName())) { // 작성자와 현재 로그인한 사용자 비교
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다."); // 권한이 없으면 예외 발생
        }
        questionForm.setSubject(question.getSubject()); // 기존 제목을 폼에 설정
        questionForm.setContent(question.getContent()); // 기존 내용을 폼에 설정
        return "question_form"; // 수정 폼 뷰 반환
    }

    // 질문을 수정하는 메서드 (로그인한 사용자만 접근 가능)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) throws DataNotFoundException {
        if (bindingResult.hasErrors()) {
            return "question_form"; // 유효성 검사 실패 시 다시 폼을 보여줌
        }

        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        // ✅ 기존 데이터 수정 (새 글 생성이 아니라 기존 객체 업데이트)
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());

        return String.format("redirect:/question/detail/%s", id); // 수정 후 상세 페이지로 이동
    }

    // 질문을 삭제하는 메서드 (로그인한 사용자만 접근 가능)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) throws DataNotFoundException {
        Question question = this.questionService.getQuestion(id); // ID로 질문 조회
        if (!question.getAuthor().getUsername().equals(principal.getName())) { // 작성자와 현재 로그인한 사용자 비교
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다."); // 권한이 없으면 예외 발생
        }
        this.questionService.delete(question); // 질문 삭제
        return "redirect:/"; // 삭제 후 홈으로 리디렉트
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) throws DataNotFoundException {
        Question question = this.questionService.getQuestion(id);
        User user = this.userService.getUser(principal.getName());
        this.questionService.vote(question, user);
        return String.format("redirect:/question/detail/%s", id);

    }

}