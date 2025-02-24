package com.BootJourney.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.BootJourney.Entity.Answer;
import com.BootJourney.Entity.User;
import com.BootJourney.Exception.DataNotFoundException;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.BootJourney.Entity.Question;
import com.BootJourney.Repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> getList() {
        return this.questionRepository.findAll();
    } // 질문 디비에 있는 모든값을 리스트 형식으로 가져옴

    public Question getQuestion(Integer id) throws DataNotFoundException {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not fonnd");
        }
    }

    public void create(String subject, String content, User author) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(author);
        this.questionRepository.save(q);

    }

    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.DESC, "createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
//        Specification<Question> spec = search(kw);
//        return this.questionRepository.findAll(spec, pageable);
        return this.questionRepository.findAllByKeyword(kw, pageable);
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);

    }

    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    public void vote(Question question, User user) {
        question.getVoter().add(user);
        this.questionRepository.save(question);
    }

    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                Join<Question, User> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, User> u2 = a.join("author", JoinType.LEFT);

                return cb.or(cb.like(q.get("subject"), "%d" + "kw" + "%"), //제목
                        cb.like(q.get("content"), "%" + kw + "%"), //내용
                        cb.like(u1.get("username"), "%" + kw + "%"),// 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"), //답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%")); //답변 작성자
            }
        };
    }


}
