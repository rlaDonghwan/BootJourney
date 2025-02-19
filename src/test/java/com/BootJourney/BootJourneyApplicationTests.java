package com.BootJourney;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.BootJourney.Entity.Question;
import com.BootJourney.Repository.QuestionRepository;

@SpringBootTest
class BootJourneyApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;
	
	@Test 
	void testJpa() { 
	    Question q1 = new Question();              // Question 엔티티 객체(레코드용) q1 생성
	    q1.setSubject("sbb가 무엇인가요?");       // q1의 'subject' 필드에 "sbb가 무엇인가요?" 라는 값을 설정
	    q1.setContent("sbb에 대해서 알고 싶습니다."); // q1의 'content' 필드에 "sbb에 대해서 알고 싶습니다." 라는 값을 설정
	    q1.setCreateDate(LocalDateTime.now());     // q1의 'createDate' 필드를 현재 시각으로 설정
	    this.questionRepository.save(q1);          // q1 객체를 DB에 저장
	    
	    Question q2 = new Question();              // 또 다른 Question 엔티티 객체 q2 생성
	    q2.setSubject("스프링 부트 모델 질문입니다.");  // q2의 'subject' 필드에 값 설정
	    q2.setContent("id는 자동으로 생성되나요?");   // q2의 'content' 필드에 값 설정
	    q2.setCreateDate(LocalDateTime.now());     // q2의 'createDate'를 현재 시각으로 설정
	    this.questionRepository.save(q2);          // q2 객체를 DB에 저장
	    
	    List<Question> all = this.questionRepository.findAll();  // 모든 Question 레코드를 조회하여 리스트로 받음
	    assertEquals(2, all.size());               // 저장된 Question 객체가 2개가 맞는지 확인
	    
	    Question q = all.get(0);                   // 조회 결과 리스트에서 첫 번째 Question 객체를 가져옴
	    assertEquals("sbb가 무엇인가요?", q.getSubject()); // 해당 객체의 subject가 "sbb가 무엇인가요?"인지 검증
	    
	    Optional<Question> oq = this.questionRepository.findById(1); // ID가 1인 Question을 DB에서 조회(결과는 Optional)
	    if(oq.isPresent()) {                      // Optional이 비어있지 않다면(해당 ID 레코드가 존재한다면)
	        Question q21 = oq.get();              // 실제 Question 객체를 가져옴
	        assertEquals("sbb가 무엇인가요?", q21.getSubject()); // q21의 subject가 "sbb가 무엇인가요?"인지 검증
	    }
	    
	    Question q22 = this.questionRepository.findBySubject("sbb가 무엇인가요?"); // subject가 "sbb가 무엇인가요?"인 레코드 조회
	    assertEquals(1, q.getId());               // 이전에 가져온 q(리스트 첫 번째)의 id가 1번인지 확인
	}

}
