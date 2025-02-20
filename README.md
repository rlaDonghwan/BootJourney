# Spring Boot 게시판 프로젝트

이 문서는 **Spring Boot를 이용한 게시판 프로젝트**에 대한 개요와 함께, `Question`, `Answer` 엔티티의 **CRUD(생성, 조회, 수정, 삭제)** 기능과 **데이터 흐름**을 정리한 문서입니다.

---

## 1. 전반적인 개념 정리

### 1.1 Java 기반 웹 애플리케이션의 흐름
Java ==> Servlet/JSP ==> Struts Framework + EJB ==> Spring Framework ==> Spring Boot

- **Servlet/JSP** 시절: 웹 서버 개발 시 서블릿, JSP 파일 사용
- **Struts/EJB**: 과거 자바 웹 프레임워크(엔터프라이즈급)
- **Spring Framework**: 객체 지향 설계를 바탕으로 한 경량 프레임워크
- **Spring Boot**: 스프링을 더 쉽게 설정할 수 있도록 지원 (Starter, Auto-configuration 등)

---

### 1.2 Spring MVC 주요 어노테이션

- **`@GetMapping`**: 클라이언트가 GET 요청을 보냈을 때, 해당 메서드를 매핑(호출)하겠다는 의미
- **`@ResponseBody`**: 컨트롤러 메서드의 반환값을 **HTTP 응답**의 **Body**로 직접 전달한다는 의미
- **`@PathVariable`**: URL 경로의 값을 메서드 파라미터로 매핑할 때 사용

```java
public String detail(Model model, @PathVariable("id") Integer id) {
    // id 값을 활용한 상세 조회 처리
}
```

---

### 1.3 ORM (Object Relational Mapping)
- **MyBatis**: SQL 매퍼 기반 ORM 프레임워크
- **JPA**: 자바 표준 ORM 인터페이스
- **Hibernate**: JPA 구현체 중 하나 (스프링 JPA에 주로 사용)

#### Hibernate `ddl-auto` 규칙

- **none**: 엔티티가 변경되어도 DB 스키마에 영향을 주지 않음
- **update**: 엔티티의 변경분만 DB에 반영
- **validate**: 엔티티와 테이블 간에 차이가 있는지만 검사
- **create**: 서버 시작 시 테이블을 모두 삭제한 후 재생성
- **create-drop**: `create`와 동일, 서버 종료 시 테이블도 삭제

> **운영환경에서는 보통 `none` 또는 `validate`를 사용하고, 개발환경에서는 `update`나 `create`를 사용할 수 있습니다.**

---

### 1.4 엔티티 설계 시 주의 사항
- **Setter 메서드를 지양**하고, **생성자**를 통해 값 설정
- 데이터 변경이 필요한 경우, 해당 로직을 담은 별도 메서드 작성 (불필요한 Setter 사용 방지)
- 컬럼 타입을 DB에서 `TEXT`로 지정할 경우 `@Column(columnDefinition = "TEXT")`를 사용
  - **TEXT**는 길이 제한이 없는 대신, 일반적으로 **검색 성능이 `VARCHAR`보다 떨어질 수 있음**

---

### 1.5 JPA Repository 주요 메서드
- **`@Autowired`**: 스프링의 **의존성 주입(DI)** 기능을 활용하여 Repository 주입
- **`Optional<T>` 활용**: `isPresent()`로 값의 존재 여부 확인 후 `get()`으로 값 추출

```java
Optional<Question> oq = questionRepository.findById(1);
if (oq.isPresent()) {
    Question q = oq.get();
    System.out.println(q.getSubject());
}
```

#### JPA Repository 메서드 관용
- `And`: 여러 컬럼이 동시에 일치하는 데이터 조회
- `Or`: 여러 컬럼 중 하나라도 일치하는 데이터 조회
- `Between`: 특정 범위 내의 값을 조회 (예: 날짜 범위)
- `LessThan`, `GreaterThanEqual`: 부등호 조건
- `Like`: 특정 문자열 패턴을 검색
- `In`: 특정 배열(컬렉션) 내에 포함되는지 검사
- `OrderBy`: 조회된 데이터를 정렬하여 반환

---

## 2. CRUD & Query 예시

### 2.1 Question 엔티티

#### **생성 (Create)**
```java
Question q1 = new Question();
q1.setSubject("sbb가 무엇인가요?");
q1.setContent("sbb에 대해서 알고 싶습니다.");
q1.setCreateDate(LocalDateTime.now());
this.questionRepository.save(q1);
```

#### **조회 (Read)**
```java
List<Question> all = this.questionRepository.findAll();
Optional<Question> oq = this.questionRepository.findById(1);
if (oq.isPresent()) {
    Question q = oq.get();
    System.out.println(q.getSubject());
}
```

#### **수정 (Update)**
```java
Optional<Question> oq = this.questionRepository.findById(28);
if (oq.isPresent()) {
    Question q2 = oq.get();
    q2.setSubject("수정된 제목");
    this.questionRepository.save(q2);
}
```

#### **삭제 (Delete)**
```java
Optional<Question> oq = this.questionRepository.findById(29);
if (oq.isPresent()) {
    this.questionRepository.delete(oq.get());
}
```

---

## 3. DTO와 VO의 차이

### **DTO (Data Transfer Object)**
- 데이터 전송을 목적으로 함
- 주로 Controller와 Service 사이에서 사용됨

```java
public class UserDTO {
    private String name;
    private String email;
    private String password;
}
```

### **VO (Value Object)**
- 불변성을 가지며, 비즈니스 로직에서 사용됨

```java
public class Money {
    private final BigDecimal amount;
    private final Currency currency;
    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }
}
```

---


