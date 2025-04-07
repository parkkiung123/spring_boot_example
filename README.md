## 그냥 참고용
혹시 나중에 spring boot를 다룰 수도 있으니 기본적인 것 정리해 놓음.<br>
https://github.com/parkkiung123/legacy_spring_mvc_example<br>
를 약간의 기능을 추가 해 spring boot로 해 놓은 것.<br>
thymeleaf는 잘 안쓰고 spring boot + react 같은 걸로 하는 게 대세인듯<br>

## 기능
로그인 페이지에서 로그인 하면 학생 성적을 보여줌.<br>
학생으로 로그인 하면 자기 성적만 나오고, 선생으로 로그인 하면 모든 학생 성적이 나옴.<br>
과목별 1등 학생이 누구인지 테이블로 표시하는 기능 추가<br>
Spring Security가 로그인 없이 성적화면에 못 들어오게 막음<br>
404등 에러가 발생하면 에러페이지로 이동.<br>

## 로그인 기능에 대해
Spring Security의 formLogin을 쓰지 않고 별도의 LoginController가 로그인을 수행함.<br>
logout은 Spring Security가 해주고 logout후 /login?logout으로 이동하는 게 기본설정임.<br>
DB방식 로그인으로 아이디(학번)과 이름으로 로그인함. 이름이 패스워드 역할을 하는데 암호화 없음.<br>
실제로는 암호화가 필요하다고 함.<br>

## 그밖에 정보
spring-boot-starter-data-jpa와 spring-boot-starter-data-rest는 같이 쓴다.<br>
@RepositoryRestResource(path = "students")는 data-rest에서 왔고 자동으로 rest api를 만들어 준다.<br>
JpaRepository는 data-jpa에서 왔고 상속받으면 기본 crud등을 따로 안만들어도 쓸 수 있다.<br>
Lombok의 @Getter,@Setter 등을 쓰면 반복적으로 쓰이는 메소드들을 자동 생성 할 수 있다.<br>
spring-boot-devtools를 쓰면 view 변경 후 새로고침해서 바로 확인 가능하다.<br>
@ExceptionHandler(NoResourceFoundException.class)로 404에러를 잡을 수 있는데,<br>
favicon.ico가 없을 때도 예외가 발생하므로 favicon.ico를 추가하자.<br>
DBService가 UserDetailsService를 구현하면 loadUserByUsername가 authenticationManager.authenticate(...) 를 호출할 때 내부적으로 호출된다고 한다.<br>

## 버그 인가?
thymeleaf뷰에서 map으로 넘어온 것의 keySet()을 취하여 th:each로 돌릴 때 key에 불필요한 공백이 추가되는 오류가 있다.<br>
key을 key.trim()해서 쓰지 않으면 그냥 null을 출력하므로 주의해야 한다.<br>
thymeleaf는 왠만하면 안쓰는 게 좋을 것 같다.<br>

## application.properties
```
spring.application.name=demo
### ========== ✅ Oracle 데이터소스 설정 ==========
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=admin
spring.datasource.password=12345
### ========== ✅ JPA 설정 ==========
# 테이블 자동 생성 X (DB에서 이미 생성돼 있음)
spring.jpa.hibernate.ddl-auto=none
# 실행되는 SQL 출력 (선택)
spring.jpa.show-sql=true
# 콘솔 SQL 보기 좋게 (선택)
spring.jpa.properties.hibernate.format_sql=true
# 방언 수동 지정 (Oracle 사용 시 명시 권장)
spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
### ========== ✅ AOP 관련 ==========
# 별도 설정 없이 @Aspect 어노테이션과 @EnableAspectJAutoProxy 사용하면 동작
### ========== ✅ 기타 설정 ==========
# 타임리프 설정
spring.thymeleaf.prefix=classpath:/templates/views/
spring.thymeleaf.suffix=.html
spring.thymeleaf.cache=false
spring.web.resources.static-locations=classpath:/static/
# 에러 메시지를 classpath:/templates/views/error/error.html 에서만 확인하도록
server.error.whitelabel.enabled=false
# view의 라이브 확인용
spring.devtools.restart.enabled=true
spring.devtools.livereload.enabled=true
```

## 실행
gradle bootRun

## 테스트
gradle test

## DB
oracle xe DB를 docker에서 돌려서 테스트 함. 아래 테이블 정의.  
dorker 이미지는 docker hub의 gvenzl/oracle-xe  
```sql
CREATE TABLE "ADMIN"."STUDENT" (
    "ID"      NUMBER,
    "NAME"    VARCHAR2(100 BYTE),
    "CLASS"   NUMBER,
    "TEACHER" VARCHAR2(100 BYTE),
    "KOREAN"  NUMBER,
    "ENGLISH" NUMBER,
    "MATH"    NUMBER,
    "SCIENCE" NUMBER,
    "HISTORY" NUMBER,
    PRIMARY KEY ("ID")
);
```
```sql
CREATE TABLE "ADMIN"."TEACHER" (
    "ID"    NUMBER,
    "NAME"  VARCHAR2(100 BYTE),
    "CLASS" NUMBER,
    PRIMARY KEY ("ID")
);
```
