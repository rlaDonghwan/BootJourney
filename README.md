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

---

##  `@PathVariable` vs `@RequestParam`

###  `@PathVariable`
- **URL 경로에서 값을 추출할 때 사용**
- **RESTful API에서 리소스를 식별하는 경우 적합**
- **주로 `GET`, `PUT`, `DELETE` 요청에서 사용**

```java
@GetMapping("/users/{id}")
public String getUser(@PathVariable("id") Long userId) {
    return "User ID: " + userId;
}
```
### 요청 예시: GET /users/10 
결과: "User ID: 10"
- @RequestParam
	•	쿼리 스트링(Query String) 방식으로 데이터를 전달
	•	필터링, 정렬 등의 추가 정보 전달 시 사용
	•	주로 GET 요청에서 사용, POST에서는 비효율적
```java
@GetMapping("/users")
public String getUser(@RequestParam("id") Long userId) {
    return "User ID: " + userId;
}
```
- 요청 예시: GET /users?id=10
- 결과: "User ID: 10"

```java
@RequestBody (POST 요청)
	•	요청 Body에서 JSON 데이터를 직접 매핑
	•	POST, PUT, PATCH 요청에서 사용
	•	@PathVariable, @RequestParam보다 더 많이 사용됨
```

```java
@PostMapping("/users")
public String createUser(@RequestBody UserDTO user) {
    return "Created User: " + user.getName();
}
```

###  스프링 시큐리티
- 스프링 시큐리티는 스프링 기반 웹 애플리케이션의 인증과 권한을 담당하는 스프링의 하위 프레임워크이다. 여기서 인증은 로그인과 같은 사용자의 신원을 확인하는 프로세스를, 권한은 인증된 사용자가 어떤 일을 할 수 있는지(어떤 접근 권한이 있는지) 관리하는 것을 의미한다.
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests.
                        requestMatchers(new AntPathRequestMatcher("/**")).permitAll());
        return http.build();
    }

}
```
@Configuration은 이 파일이 스프링 환경 설정 파일임을 의미하는 어노테이션이다.
@EnableWebSecurity는 모든 요청 URL이 스프링 시큐리티를 활성화하는 역할을 한다. 내부적으로 SecurityFilterChain 클래스가 동작하여 모든 요청 URL에 이 클래스가 필터로 적용되어 URL 별로 특별한 설정을 할 수 있게 된다.
@Bean을 통해 SecurityFilterChain을 세부 설정 할 수 있다.

#### 빈?
- 빈은 스프링에 의해 생성 또는 관리되는 객체를 의미함. 우리가 지금껏 만들어왔던 컨트로러,서비스, 레파지토리 등도 모두 빈에 해당한다. @Bean 어노테이션을 통해 자바 코드 내에서 별도로 빈을 정의하고 등록할 수도 있다.

#### CSRF
- 웹 보안 공격중 하나로 조작된 정보로 웹 사이트가 실행되도록 속이는 공격 기술. 스프링 시큐리티는 이러한 공격을 방지하기 위해 CSRF 토큰을 새션을 통해 발행하고, 웹 페이지에서는 폼 전송 시에 해당 토큰을 함께 전송하여 실제 웹 페이지에서 작성한 데이터가 전달 되는지를 점증한다.

---
```java
@Column(unique = true)
```
유일한 값만 저장할 수 있다는 의미


#### BCryptPasswordEncoder
- BCryptPasswordEncoder는 비크립트 해시 함수를 사용하는데, 비크립트는 해시 함수의 하나로 주로 비밀번호와 같은 보안정보를 안전하 게 저장하고 검증할 때 사용하는 암호화 기술
- 비크립트는 객체를 직접 new로 생성하는 방식보다는 PasswordEncoder 객체를 빈으로 등록하여 사용하는 것이 좋다. 왜냐하면 암호화 방식을 변경하면 비크립트패스워드인코더를 사용한 모든 프로그램을 일일이 찾아 다니며 수정해야하기 때문이다.


### Spring Security 세션과 Principal의 관계 및 역할 정리

1️⃣ Spring Security에서 세션(Session)이란?
•	Spring Security는 로그인한 사용자의 인증 정보를 세션에 저장하여 관리함.
•	사용자가 로그인하면 Spring Security가 자동으로 세션을 생성하고, 사용자 인증 정보를 유지함.
•	기본적으로 세션을 통해 로그인 상태를 유지하고, 요청마다 사용자의 인증 상태를 확인함.

2️⃣ SecurityContext와 Authentication의 역할
•	**Spring Security의 핵심은 SecurityContext**이며, 현재 로그인한 사용자의 정보를 저장하고 있음.
•	SecurityContext 내부에는 Authentication 객체가 존재함.
•	Authentication 객체는 로그인한 사용자 정보를 담고 있으며, 사용자 인증 여부를 관리함.

#### SecurityContext의 흐름
1.	사용자가 로그인하면 Spring Security가 SecurityContext를 생성.
2.	SecurityContext 내부에는 현재 로그인한 사용자의 Authentication 객체가 저장됨.
3.	이후 사용자가 요청할 때마다 Spring Security가 SecurityContext를 참조하여 로그인 상태를 유지.

3️⃣ Principal과 SecurityContext의 관계
•	Principal은 SecurityContext 내부의 Authentication 객체에서 사용자 정보를 가져오는 역할을 함.
•	즉, Principal은 로그인한 사용자의 정보를 쉽게 가져올 수 있도록 해주는 편리한 인터페이스.

#### Principal의 역할

역할	설명
Principal.getName()	로그인한 사용자의 username(아이디) 반환
Principal	현재 로그인한 사용자의 정보를 제공
SecurityContext	현재 사용자의 인증(Authentication) 정보를 저장하는 컨텍스트
Authentication	현재 로그인한 사용자의 정보(Principal), 권한(Role) 등을 포함

4️⃣ Spring Security의 세션과 Principal의 동작 과정
1.	사용자가 로그인 요청을 보냄.
2.	Spring Security가 사용자를 인증(Authentication) 후, SecurityContext를 생성함.
3.	SecurityContext 내부에 현재 로그인한 사용자의 Authentication 객체가 저장됨.
4.	Authentication 객체 안에는 로그인한 사용자의 정보(Principal), 권한(Role) 등이 포함됨.
5.	이후 요청이 들어오면, Spring Security가 SecurityContext를 참조하여 사용자의 로그인 상태를 확인함.
6.	컨트롤러에서 Principal을 사용하면, SecurityContext에서 로그인된 사용자 정보를 쉽게 가져올 수 있음.

5️⃣ Principal을 사용할 때의 장점

-  SecurityContext를 직접 사용하지 않고 더 간단하게 로그인한 사용자 정보를 가져올 수 있음.
-  컨트롤러에서 쉽게 현재 로그인한 사용자 정보를 확인할 수 있음.
-  Spring Security와 자동 연동되므로 별도의 설정 없이 사용 가능.

#### 최종 요약
•	Spring Security는 로그인한 사용자의 정보를 SecurityContext에 저장하여 세션을 관리함.
•	SecurityContext 내부에는 Authentication 객체가 존재하며, 로그인된 사용자 정보를 담고 있음.
•	Principal은 SecurityContext에서 로그인한 사용자 정보를 쉽게 가져오는 역할을 함.
•	컨트롤러에서 Principal을 사용하면, 로그인한 사용자의 username 등을 편리하게 가져올 수 있음.

