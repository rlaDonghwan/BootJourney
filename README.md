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