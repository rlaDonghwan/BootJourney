package com.BootJourney.Controller;

import com.BootJourney.Entity.Question;
import com.BootJourney.Exception.DataNotFoundException;
import com.BootJourney.Service.AnswerService;
import com.BootJourney.Service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    @PostMapping("/create/{id}") // 프론트에서 답변 등록 버튼을 누르게 되면 이 메서드 호출 됨
    public String createAnswer(Model model, @PathVariable("id") Integer id, @RequestParam(value = "content") String content) throws DataNotFoundException {
        Question question = this.questionService.getQuestion(id); //버튼 클릭시 받아오는 id 값을 통해 외래키 등록
        this.answerService.create(question, content);
        return String.format("redirect:/question/detail/%s",id);

    }
}


