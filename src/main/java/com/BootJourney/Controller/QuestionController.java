package com.BootJourney.Controller;

import java.util.List;

import com.BootJourney.Exception.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.BootJourney.Entity.Question;
import com.BootJourney.Repository.QuestionRepository;
import com.BootJourney.Service.QuestionService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/question")
public class QuestionController {
	
	private final QuestionService questionService;
	
//	public QuestionController(QuestionRepository questionRepository) { 위에 DI를 하지 않으면 이렇게 생성자를 써줘야함
//		this.questionRepository = questionRepository;
//	}
	
	@GetMapping("/list")
	public String list(Model model){
		List<Question> questionList = this.questionService.getList(); //서비스에서 getList 메서드 호출
		model.addAttribute("questionList",questionList); //뷰에다가 질문목록 이라는 명으로 값을 던져줌
		return "question_list"; //호출
	}
	
	@GetMapping("/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id) throws DataNotFoundException {
		Question question = this.questionService.getQuestion(id);
		model.addAttribute("question",question);
		return "question_detail";
	}


	
}
