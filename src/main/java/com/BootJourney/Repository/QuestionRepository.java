package com.BootJourney.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.BootJourney.Entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer>{

    Question findBySubject(String subject); 


}
