package com.BootJourney;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.BootJourney.Service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.BootJourney.Entity.Answer;
import com.BootJourney.Entity.Question;
import com.BootJourney.Repository.AnswerRepository;
import com.BootJourney.Repository.QuestionRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
class BootJourneyApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private QuestionService questionService;

	@Test 
	void testJpa() { 
		
//		Create 입력 테스트----------------------------------------------------------------------------------------------------------------
//	    Question q1 = new Question();              	// Question 엔티티 객체(레코드용) q1 생성
//	    q1.setSubject("sbb가 무엇인가요?");            	// q1의 'subject' 필드에 "sbb가 무엇인가요?" 라는 값을 설정
//	    q1.setContent("sbb에 대해서 알고 싶습니다.");     // q1의 'content' 필드에 "sbb에 대해서 알고 싶습니다." 라는 값을 설정
//	    q1.setCreateDate(LocalDateTime.now());      // q1의 'createDate' 필드를 현재 시각으로 설정
//	    this.questionRepository.save(q1);           // q1 객체를 DB에 저장

//	    Question q2 = new Question();              // 또 다른 Question 엔티티 객체 q2 생성
//	    q2.setSubject("스프링 부트 모델 질문입니다.");    // q2의 'subject' 필드에 값 설정
//	    q2.setContent("id는 자동으로 생성되나요?");      // q2의 'content' 필드에 값 설정
//	    q2.setCreateDate(LocalDateTime.now());     // q2의 'createDate'를 현재 시각으로 설정
//	    this.questionRepository.save(q2);          // q2 객체를 DB에 저장
	    
//		Read 조회 테스트----------------------------------------------------------------------------------------------------------------
//	    List<Question> all = this.questionRepository.findAll();  // 모든 Question 레코드를 조회하여 리스트로 받음
//	    assertEquals(2, all.size());              			    // 저장된 Question 객체가 2개가 맞는지 확인
//	    
//	    Question q = all.get(0);                   		// 조회 결과 리스트에서 첫 번째 Question 객체를 가져옴
//	    assertEquals("sbb가 무엇인가요?", q.getSubject()); // 해당 객체의 subject가 "sbb가 무엇인가요?"인지 검증
//	    
//	    Optional<Question> oq = this.questionRepository.findById(1); // ID가 1인 Question을 DB에서 조회(결과는 Optional)
//	    if(oq.isPresent()) {                     					 // Optional이 비어있지 않다면(해당 ID 레코드가 존재한다면)
//	        Question q21 = oq.get();             					 // 실제 Question 객체를 가져옴
//	        assertEquals("sbb가 무엇인가요?", q21.getSubject()); 	     // q21의 subject가 "sbb가 무엇인가요?"인지 검증
//	    }
//	    
//	    Question q22 = this.questionRepository.findBySubject("sbb가 무엇인가요?"); // subject가 "sbb가 무엇인가요?"인 레코드 조회
//	    assertEquals(1, q.getId());               								// 이전에 가져온 q(리스트 첫 번째)의 id가 1번인지 확인
		

//	    Question q = this.questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");  //실제 DB에 있는 레코드 중 subject, content가 일치하는 것을 조회
//
//	    List<Question> qList = this.questionRepository.findBySubjectLike("sbb%"); // "sbb"가 들어간 subject 찾기
//	    // 주의: MySQL에서는 '%sbb%' 가 "sbb"가 포함된 문자열을 찾는 문법
//	    Question q = qList.get(0);
//	    
//		Uddate 수정 테스트----------------------------------------------------------------------------------------------------------------
//	    Optional<Question> oq = this.questionRepository.findById(28);
//	    assertTrue(oq.isPresent());
//	    Question q2 = oq.get();
//	    q2.setSubject("수정된 제목");
//	    this.questionRepository.save(q2);

//		Delete 삭제 테스트----------------------------------------------------------------------------------------------------------------
//		assertEquals(2,this.questionRepository.count());
//		Optional<Question> oq = this.questionRepository.findById(29);
//		assertTrue(oq.isPresent());
//		Question q = oq.get();
//		this.questionRepository.delete(q);
//		assertEquals(1, this.questionRepository.count());
		
		//Answer------------------------------------------------------------------------------------------------------------------------------
		//Create 데이터 생성
//		Optional<Question> oq = this.questionRepository.findById(1);
//		assertTrue(oq.isPresent());
//		Question q = oq.get();
//
//		Answer a = new Answer();
//		a.setContent("네 자동으로 생성됩니다.");
//		a.setQuestion(q);
//		a.setCreateDate(LocalDateTime.now());
//		this.answerRepository.save(a);
		
//		조회 Read
//		Optional<Answer> oa = this.answerRepository.findById(1);
//		assertTrue(oa.isPresent());
//		Answer a = oa.get();
//		assertEquals(30, a.getQuestion().getId());
		
		
//		Optional<Question> oq = this.questionRepository.findById(30);
//		assertTrue(oq.isPresent());
//		Question q = oq.get();
//
//		List<Answer> answerList = q.getAnswerList();
//
//		assertEquals(1, answerList.size());
//		assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());

//		for(int i=1;i<=300; i++){
//			String subject = String.format("테스트 데이터 입니다:[%03d]",i);
//			String content = "내용 없음";
//			this.questionService.create(subject,content);
//		}

	}

}











