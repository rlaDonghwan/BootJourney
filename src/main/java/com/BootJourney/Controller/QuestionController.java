package com.BootJourney.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.BootJourney.Entity.Question;
import com.BootJourney.Repository.QuestionRepository;
import com.BootJourney.Service.QuestionService;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Controller
public class QuestionController {
	
	private final QuestionService questionService;
	
//	public QuestionController(QuestionRepository questionRepository) { 위에 DI를 하지 않으면 이렇게 생성자를 써줘야함
//		this.questionRepository = questionRepository;
//	}
	
	@GetMapping("/question/list")
	public String list(Model model){
		List<Question> questionList = this.questionService.getList();
		model.addAttribute("questionList",questionList);
		return "question_list";
	}
	
	@GetMapping(value = "/question/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id) {
		return "question_detail";
	}

	
}
