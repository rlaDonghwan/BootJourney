package com.BootJourney.Controller;

import java.util.List;

import com.BootJourney.Exception.DataNotFoundException;
import com.BootJourney.Form.QuestionForm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

	@GetMapping("/create")
	public String questionCreate(Model model){
		model.addAttribute("questionForm",new QuestionForm());
		return "question_form";
	}

	@PostMapping("/create")
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult){ //폼객체를 통해 값을 받고 bindingResult가 검증
		if(bindingResult.hasErrors()){
			return "question_form";
		}
		this.questionService.create(questionForm.getSubject(),questionForm.getContent());
		return "redirect:/question/list"; //질문 저장 후 질문 목록으로 이동
	}

	
}
