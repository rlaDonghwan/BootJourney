package com.BootJourney.Service;

import java.util.List;
import java.util.Optional;

import com.BootJourney.Exception.DataNotFoundException;
import org.springframework.stereotype.Service;

import com.BootJourney.Entity.Question;
import com.BootJourney.Repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
	
	private final QuestionRepository questionRepository;
	
	public List<Question> getList(){
		return this.questionRepository.findAll();
	} // 질문 디비에 있는 모든값을 리스트 형식으로 가져옴
	
	public Question getQuestion(Integer id) throws DataNotFoundException {
		Optional<Question> question = this.questionRepository.findById(id);
		if(question.isPresent()) {
			return question.get();
		}else {
			throw new DataNotFoundException("question not fonnd");
		}
	}

}
