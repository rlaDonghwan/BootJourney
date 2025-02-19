# Spring Boot Board Project

이 문서는 **Spring Boot를 이용한 게시판 프로젝트**에 대한 개요와 함께,  
`Question`, `Answer` 엔티티에 대한 **CRUD(생성, 조회, 수정, 삭제)** 및 **쿼리** 예시를 정리한 문서입니다.

---

## 1. 전반적인 개념 정리

### 1.1 Java 기반 웹 애플리케이션의 흐름
Java ==> Servlet/JSP ==> Struts Framework + EJB ==> Spring Framework ==> Spring Boot

- **Servlet/JSP** 시절: 웹 서버 개발 시 서블릿, JSP 파일 사용  
- **Struts/EJB**: 과거 자바 웹 프레임워크(엔터프라이즈급)  
- **Spring Framework**: 객체 지향 설계를 바탕으로 한 경량 프레임워크  
- **Spring Boot**: 스프링을 더 쉽게 설정할 수 있도록 지원 (Starter, Auto-configuration 등)

---

### 1.2 `@GetMapping`, `@ResponseBody`
- **`@GetMapping`**: 클라이언트가 GET 요청을 보냈을 때, 해당 메서드를 매핑(호출)하겠다는 의미  
- **`@ResponseBody`**: 컨트롤러 메서드의 반환값을 **HTTP 응답**의 **Body**로 직접 전달한다는 의미

---

### 1.3 ORM (Object Relational Mapping)
- **MyBatis**: SQL 매퍼 기반 ORM 프레임워크  
- **JPA**: 자바 표준 ORM 인터페이스  
- **Hibernate**: JPA 구현체 중 하나 (스프링 JPA에 주로 사용)

---

### 1.4 Hibernate `ddl-auto` 규칙
- **none**: 엔티티가 변경되어도 DB 스키마에 영향을 주지 않음  
- **update**: 엔티티의 변경분만 DB에 반영  
- **validate**: 엔티티와 테이블 간에 차이가 있는지만 검사  
- **create**: 서버 시작 시 테이블을 모두 삭제한 후 재생성  
- **create-drop**: `create`와 동일, 서버 종료 시 테이블도 삭제

> **주의**: 운영환경에서는 보통 `none` 또는 `validate`를 사용하고, 개발환경에서는 `update`나 `create`를 사용할 수 있습니다.

---

### 1.5 엔티티 설계 시 주의 사항
- 엔티티 클래스에서 **Setter 메서드를 지양**하고, **생성자**를 통해서만 값 세팅  
- **데이터 변경**이 필요한 경우, 해당 로직을 담은 별도 메서드를 작성 (불필요한 Setter 남발 방지)

---

### 1.6 Row & Column
- **row (행)**: 가로로 이어지는 데이터를 한 줄로 표현  
- **column (열)**: 세로로 이어지는 필드 하나하나

---

### 1.7 `@Column(columnDefinition = "TEXT")`
- 컬럼 타입을 DB에서 `TEXT`로 지정
- **`TEXT`**는 길이 제한이 없는 대신, 일반적으로 **검색 성능이 `VARCHAR`보다 떨어질 수 있음**  
- 제목처럼 검색이 잦은 필드는 `VARCHAR`, 본문처럼 길이가 긴 필드는 `TEXT` 등 선택적으로 사용

---

### 1.8 `CascadeType.REMOVE`
- 예: **Question**(질문)과 **Answer**(답변)의 관계에서,  
  질문 하나에 여러 답변이 달릴 수 있음  
- 질문을 삭제하면 연관된 답변도 모두 삭제하고 싶다면 **`cascade = CascadeType.REMOVE`** 사용

---

### 1.9 Repository
- **`@Autowired`**: 스프링의 **의존성 주입(DI)** 기능으로, 해당 인터페이스(리포지토리)를 구현한 Bean을 주입  
- **`assertEquals(기댓값, 실제값)`**: JUnit 단언(Assertion). 기대값과 실제값이 일치하지 않으면 테스트 실패

#### JPA Repository 메서드 관용
- `And`: 여러 컬럼이 동시에 일치하는 데이터 조회
- `Or`: 여러 컬럼 중 하나라도 일치하는 데이터 조회
- `Between`: 특정 범위 내의 값을 조회 (예: 날짜 범위)
- `LessThan`, `GreaterThanEqual`: 부등호 조건
- `Like`: 특정 문자열 패턴을 검색
- `In`: 특정 배열(컬렉션) 내에 포함되는지 검사
- `OrderBy`: 조회된 데이터를 정렬하여 반환

---

### 1.10 Optional
- `java.util.Optional`은 **Null-safe**를 위해 사용되는 래퍼 클래스  
- `isPresent()`로 값의 존재 여부 확인 → `get()`으로 값 추출  
- 예: `Optional<Question> oq = questionRepository.findById(1);`  
  - 존재하면 `oq.isPresent()` → `true`, `oq.get()` → `Question` 인스턴스

---

## 2. Question & Answer: CRUD & Query 예시

### 2.1 Question 엔티티

```java
(1) 생성 (Create)

Question q1 = new Question();
q1.setSubject("sbb가 무엇인가요?");
q1.setContent("sbb에 대해서 알고 싶습니다.");
q1.setCreateDate(LocalDateTime.now());
this.questionRepository.save(q1);

Question q2 = new Question();
q2.setSubject("스프링 부트 모델 질문입니다.");
q2.setContent("id는 자동으로 생성되나요?");
q2.setCreateDate(LocalDateTime.now());
this.questionRepository.save(q2);

(2) 조회 (Read)
// 모든 Question 조회
List<Question> all = this.questionRepository.findAll();
assertEquals(2, all.size());

// 특정 ID 조회
Optional<Question> oq = this.questionRepository.findById(1);
assertTrue(oq.isPresent());
Question q = oq.get();
assertEquals("sbb가 무엇인가요?", q.getSubject());

// 특정 필드값으로 조회
Question q22 = this.questionRepository.findBySubject("sbb가 무엇인가요?");
assertEquals(1, q22.getId());

// 여러 필드로 조회
Question q3 = this.questionRepository.findBySubjectAndContent(
    "sbb가 무엇인가요?", 
    "sbb에 대해서 알고 싶습니다."
);
assertNotNull(q3);

(3) 수정 (Update)
Optional<Question> oq = this.questionRepository.findById(28);
assertTrue(oq.isPresent());

Question q2 = oq.get();
q2.setSubject("수정된 제목");
this.questionRepository.save(q2);


(4) 삭제 (Delete)
assertEquals(2, this.questionRepository.count());

Optional<Question> oq = this.questionRepository.findById(29);
assertTrue(oq.isPresent());
Question q = oq.get();
this.questionRepository.delete(q);

assertEquals(1, this.questionRepository.count());

(5) Like 조건 조회
// subject가 "sbb"로 시작하는 Question 찾기
List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
assertFalse(qList.isEmpty());
Question firstQ = qList.get(0);
assertEquals("sbb가 무엇인가요?", firstQ.getSubject());
<hr>

2.2 Answer 엔티티
(1) 생성 (Create)
Optional<Answer> oa = this.answerRepository.findById(1);
assertTrue(oa.isPresent());
Answer a = oa.get();
assertEquals(30, a.getQuestion().getId());

(2) 조회 (Read)  

Optional<Answer> oa = this.answerRepository.findById(1);
assertTrue(oa.isPresent());
Answer a = oa.get();
assertEquals(30, a.getQuestion().getId());
```
<hr>
다음은 요청한 내용을 정리한 Markdown 문서입니다.
이제 그대로 복사해서 GitHub, Notion 등에 붙여넣으면 깔끔한 문서가 될 거야.

# Spring Boot 게시판 프로젝트

이 문서는 **Spring Boot를 이용한 게시판 프로젝트**에 대한 개요와 함께,  
`Question`, `Answer` 엔티티의 **CRUD(생성, 조회, 수정, 삭제)** 기능과 **데이터 흐름**을 정리한 문서이다.

---

## 1. Spring Boot + Thymeleaf 데이터 바인딩 흐름

### 1.1 컨트롤러 코드 (`/question/list` 요청 처리)

```java
package com.BootJourney.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.BootJourney.Service.QuestionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class QuestionController {
    
    private final QuestionService questionService;
    
    @GetMapping("/question/list")
    public String list(Model model) {
        List<Question> questionList = this.questionService.getList();
        model.addAttribute("questionList", questionList);
        return "question_list";
    }
}

1.2 데이터 흐름

1. 데이터 조회 (questionService.getList())
	•	questionService.getList()를 호출하여 데이터베이스에서 모든 Question 데이터를 가져옴.
	•	questionService는 questionRepository.findAll()을 실행하여 JPA를 통해 DB에서 데이터를 가져오는 역할을 수행한다.

package com.BootJourney.Service;

import java.util.List;

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
    }
}

2. 데이터 모델에 추가 (model.addAttribute)

model.addAttribute("questionList", questionList);

	•	"questionList"라는 이름으로 데이터를 Thymeleaf 템플릿에 전달.
	•	Thymeleaf에서는 ${questionList} 로 데이터를 접근할 수 있음.

3. Thymeleaf 템플릿 호출 (return "question_list")

return "question_list";

	•	question_list.html이라는 Thymeleaf 템플릿을 렌더링하여 클라이언트에게 HTML을 반환.

2. HTML (question_list.html)

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>질문 목록</title>
</head>
<body>
    <table>
        <thead>
        <tr>
            <th>제목</th>
            <th>작성일시</th>
        </tr>
        </thead>
        <tbody>
            <tr th:each="question : ${questionList}">
                <td th:text="${question.subject}"></td>
                <td th:text="${question.createDate}"></td>
            </tr>
        </tbody>
    </table>
</body>
</html>

3. HTML 코드 설명

3.1 반복문 (th:each)

<tr th:each="question : ${questionList}">

	•	questionList의 모든 요소를 하나씩 꺼내서 question 변수에 저장.
	•	리스트의 개수만큼 <tr> 태그가 반복 생성됨.

3.2 값 출력 (th:text)

<td th:text="${question.subject}"></td>

	•	question.subject 값을 <td> 태그 안에 출력.

<td th:text="${question.createDate}"></td>

	•	question.createDate 값을 <td> 태그 안에 출력.

4. 최종 데이터 흐름 정리
	1.	사용자가 /question/list URL을 요청
	2.	QuestionController의 list() 메서드가 실행됨
	3.	questionService.getList()가 호출되어 데이터베이스에서 데이터를 조회
	4.	조회된 데이터를 model.addAttribute("questionList", questionList);를 통해 Thymeleaf에 전달
	5.	question_list.html에서 th:each를 사용해 데이터를 반복 출력
	6.	최종 HTML이 브라우저에서 렌더링됨

5. Question & Answer: CRUD & Query 예시

5.1 Question 엔티티

1. 생성 (Create)

Question q1 = new Question();
q1.setSubject("sbb가 무엇인가요?");
q1.setContent("sbb에 대해서 알고 싶습니다.");
q1.setCreateDate(LocalDateTime.now());
this.questionRepository.save(q1);

Question q2 = new Question();
q2.setSubject("스프링 부트 모델 질문입니다.");
q2.setContent("id는 자동으로 생성되나요?");
q2.setCreateDate(LocalDateTime.now());
this.questionRepository.save(q2);

2. 조회 (Read)

// 모든 Question 조회
List<Question> all = this.questionRepository.findAll();
assertEquals(2, all.size());

// 특정 ID 조회
Optional<Question> oq = this.questionRepository.findById(1);
assertTrue(oq.isPresent());
Question q = oq.get();
assertEquals("sbb가 무엇인가요?", q.getSubject());

// 특정 필드값으로 조회
Question q2 = this.questionRepository.findBySubject("sbb가 무엇인가요?");
assertEquals(1, q2.getId());

// 여러 필드로 조회
Question q3 = this.questionRepository.findBySubjectAndContent(
    "sbb가 무엇인가요?", 
    "sbb에 대해서 알고 싶습니다."
);
assertNotNull(q3);

3. 수정 (Update)

Optional<Question> oq = this.questionRepository.findById(28);
assertTrue(oq.isPresent());

Question q2 = oq.get();
q2.setSubject("수정된 제목");
this.questionRepository.save(q2);

4. 삭제 (Delete)

assertEquals(2, this.questionRepository.count());

Optional<Question> oq = this.questionRepository.findById(29);
assertTrue(oq.isPresent());
Question q = oq.get();
this.questionRepository.delete(q);

assertEquals(1, this.questionRepository.count());

5. Like 조건 조회

// subject가 "sbb"로 시작하는 Question 찾기
List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
assertFalse(qList.isEmpty());
Question firstQ = qList.get(0);
assertEquals("sbb가 무엇인가요?", firstQ.getSubject());

6. Answer 엔티티

1. 생성 (Create)

Optional<Answer> oa = this.answerRepository.findById(1);
assertTrue(oa.isPresent());
Answer a = oa.get();
assertEquals(30, a.getQuestion().getId());

2. 조회 (Read)

Optional<Answer> oa = this.answerRepository.findById(1);
assertTrue(oa.isPresent());
Answer a = oa.get();
assertEquals(30, a.getQuestion().getId());

7. 정리

이 문서는 Spring Boot를 활용한 게시판 프로젝트의 개요와,
Question 및 Answer 엔티티의 CRUD 및 데이터 바인딩 흐름을 정리한 자료이다.


DTO와 VO의 차이점 이해하기

소프트웨어 개발에서 DTO(Data Transfer Object)와 VO(Value Object)는 중요한 개념입니다. 그러나 많은 개발자들이 이 두 용어를 혼동하곤 합니다. 이 글에서는 DTO와 VO의 차이점을 명확히 이해하고, 실제 예시를 통해 그 차이를 설명하고자 합니다.

DTO와 VO의 정의 및 차이점
	•	DTO: 데이터 전송 객체로, 주로 데이터베이스와 애플리케이션 간의 데이터 전송을 위해 사용됩니다. 데이터베이스의 엔터티와 유사한 구조를 가지며, 데이터 전송을 목적으로 합니다.
	•	VO: 값 객체로, 불변성을 가지며 주로 비즈니스 로직에서 사용됩니다. 값이 변경되지 않으며, 비즈니스 로직 내에서 의미 있는 값을 표현합니다.

DTO의 예시: UserDTO

DTO는 데이터 전송을 목적으로 하며, 데이터베이스의 엔터티와 유사한 구조를 가집니다. 예를 들어, UserDTO는 사용자 정보를 전송하기 위한 DTO입니다. 이 DTO는 사용자 이름, 이메일, 비밀번호 등의 정보를 포함할 수 있습니다.

public class UserDTO {
    private String name;
    private String email;
    private String password;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

VO의 예시: Money

VO는 값 객체로, 불변성을 가지며 주로 비즈니스 로직에서 사용됩니다. 예를 들어, Money는 금액을 나타내는 VO입니다. 이 VO는 금액과 통화를 포함하며, 생성 시에만 값이 설정되고 이후에는 변경되지 않습니다.

public class Money {
    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }
}

DTO와 VO의 통합 사용

DTO와 VO는 종종 함께 사용됩니다. 예를 들어, 애플리케이션에서 사용자 정보를 전송할 때 UserDTO를 사용하고, 금액을 나타낼 때 Money VO를 사용할 수 있습니다. 이 경우, UserDTO는 데이터 전송을 목적으로 하며, Money VO는 비즈니스 로직에서 사용됩니다.

public class UserService {
    public void createUser(UserDTO userDTO) {
        // UserDTO를 사용하여 사용자 생성
    }

    public Money calculateTotalAmount(List<Money> amounts) {
        BigDecimal total = BigDecimal.ZERO;
        for (Money amount : amounts) {
            total = total.add(amount.getAmount());
        }
        return new Money(total, Currency.getInstance("USD"));
    }
}

결론

DTO와 VO는 소프트웨어 개발에서 매우 중요한 개념입니다. DTO는 데이터 전송을 목적으로 하며, 데이터베이스의 엔터티와 유사한 구조를 가지는 반면, VO는 불변성을 가지며 주로 비즈니스 로직에서 사용됩니다. 이 두 개념을 명확히 이해하면 소프트웨어 개발이 더 효율적이고 간편해집니다.
