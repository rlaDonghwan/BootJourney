package com.BootJourney.Service;

import com.BootJourney.Entity.Answer;
import com.BootJourney.Entity.Question;
import com.BootJourney.Entity.User;
import com.BootJourney.Exception.DataNotFoundException;
import com.BootJourney.Repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public void create(Question question, String content, User author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);

    }

    public Answer getAnswer(Integer id) throws DataNotFoundException {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        }else{
            throw new DataNotFoundException("댓글을 찾지 못했습니다.");
        }
    }

    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }



    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }

}
