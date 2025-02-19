# Spring Boot borad project


Java Based Web Application
<hr>
java ==> Servlet/JSP ==> Struts Framework + EJB ==> Spring Framework ==> Spring Boot
<hr>
@GetMapping 클라이언트 요청으로 hello 메서드가 실행됨을 알려줌
@ResponseBody 헬로 메서드의 출력값 그래도 리턴할 것임을 알려준다.
<hr>
ORM
- Mybatis
- JPA 
<hr>
hibernate ddl-auto규칙

- none: 엔티티가 변경되더라도 디비를 변경하지 않음
- update: 엔티티의 변경된 부분만 디비에 적용
- validate: 엔티티와 테이블 간에 차이점이 있는지 검사만 함 
- create: 서버를 시작할 때 테이블을 모두 삭제한 후 다시 생성
- create-drop : create와 동일하지만 스프링부트 서버를 종료할 때에도 테이블을 모두 삭제한다.
- 엔티티를 만들때 Setter 메서드는 저장하지 않는다
- 엔티티는 생성자에 의해서만 엔티티의 값을 저장할 수 있게 하고, 데이터를 변경해야 할 경우에는 메서드를 추가로 작성하면 된다.

<hr>

- row:  행 , 좌 우
- column:  열,  위 아래

<hr>

#### @Column(columnDefinition = "TEXT") 
- 타입을 지정하여 디비 속성 생성
Text는 길이 제한은 없지만 검색 성능이 중요한 경우에는 varchar를 사용하는 것이 좋음

<hr>

#### CascadeType.REMOVE
- 게시판 서비스에서는 질문 하나에 답변이 여러 개 작성될 수 있다. 그런데 보통 게시판 서비스에서는 질문을 삭제하면 그에 달린 답변들도 함께 삭제된다. SBB도 질문을 삭제하면 그에 달린 답변들도 모두 삭제되도록 cascade = CascadeType.REMOVE를 사용

<hr>

#### Repository
- @Autowired는 스프링의 의존성 주입(DI)이라는 기능을 사용하여 객체를 주입
- assertEquals(기댓값, 실제값)와 같이 작성하고 기댓값과 실제값이 동일한지를 조사한다. 만약 기댓값과 실제값이 동일하지 않다면 테스트는 실패로 처리된다.
