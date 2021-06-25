# Touch-My-Body

## 프로젝트 주 기능

- 웨이트 트레이닝 운동 기록 

- 웨이트 트레이닝 관련 정보 제공

- 웨이트 트레이닝 관련 커뮤니티 제공

---

## 스키마 

- 프로젝트명: Touch My Body

- 프로젝트 참여 인원: 1명 (김건영)

- 개발 기간: 2021.06.21 ~ 

- 사용기술 및 개발 환경: Spring Boot, Spring JPA, Spring Security, JUnit5, Maven, Thymeleaf

- 사용 데이터베이스

   - Prod: Mysql 8.0

   - Test: H2

--- 

## 코드 배포 구조

---

## DB 테이블 설계 

![touchmb](https://user-images.githubusercontent.com/60494603/122730217-f289de80-d2b4-11eb-8a08-91e95cf9b497.png)

---

## CONTROLLER 설계

---

## 테스트

- 테스트 작성 시기: 테스트 주도 개발 방식(TDD)을 준수하기 위해 테스트 선 작성 후 소스코드 작성했습니다.

---

## 화면 설계

---

## 개발일지

### 2021.6.21

1. 프로젝트 설계

2. 데이터베이스 테이블 설계

3. Entity 클래스들 구현

4. Entity 클래스 관련 Repository 구현

5. Entity 클래스 관련 서비스 테스트 코드 작성 -> 서비스 클래스들 코드 작성

### 2021.6.22

- Spring Security 관련 클래스 구현

    1. Security Configuration 구현

    2. UserDetailsVO 구현

    3. UserDetailsServiceImpl 테스트 코드 작성 -> UserDetailsServiceImpl 구현

    4. UserAuthenticationProvider 구현

- UserController

    1. UserController 로그인, 회원가입 테스트 코드 작성 -> UserController 해당 부분 구현

    2. 위 구현 파트 관련 Exception, ExceptionHandler 구현

    3. 로그인, 회원가입 화면 구현

### 2021.6.23

- Spring Security

    1. 커스텀 Login,Logout Success Handler 구현

- MangerUserController

    1. 매니저들의 유저 검색을 위한 페이지 구현

- UserExerciseController

    1. 유저의 운동 기록 추가 By Post 처리하는 메소드 구현 

- UserExerciseRestController 

    1. 유저의 특정 날짜에 한 운동 기록들 반환하는 메소드 구현 

    2. 신체 부위별 데이터 베이스의 모든 운동 종류 반환하는 메소드 구현

- User Domain
	
    1. date 자료형 Date -> Calendar (Date의 대부분의 메소드가 Deprecated, 시간은 필요 없다는 비즈니스 요구에 의해 Calendar 가 더 적합하다 판단)

- DTO

    1. AddUserExerciseDTO 구현 - 유저의 운동 기록 추가사항을 전달받기 위한 DTO

    2. DateDTO 구현 - 유저가 운동기록을 열람하고자하는 날짜를 전달받기 위한 DTO

    3. UserManageListDTO 구현 - 매니저의 유저 검색 페이지에 보여지는 정보 전달을 위한 DTO

    4. ExerciseSortDTO 구현 - DB에서 Exercise의 name, target 만 가져올때 사용

- Enum 

    1. RoleType에 koreanName, EnglishName 필드 추가 -> 클라이언트에게 보일 role 명칭
	
    2. TargetType에 koreanName, EnglishName 필드 추가 -> 클라이언트에게 보일 target 명칭

- ErrorController

    1. userNotFounded 메소드 추가 - 특정 조건으로 유저 찾은 결과 없을때 던져진 예외 처리하는 메소드 

- ErrorCode, Exceptions

    1. ExerciseErrorCode, UserErrorCode 추가 

- Repository

    1. ExerciseRepository에 이름으로 Exercise 찾는 메소드 추가 

    2. UserExerciseRepository에 유저의 특정 날짜 운동 기록들 가져오는 메소드 추가 

    3. UserRepository에 닉네임이나 이름 검색으로 만족하는 유저들 가져오는 메소드, 총 개수 가져오는 메소드 추가 

    4. ExerciseRepository에 Exercise의 Name Target만 가져오는 메소드 추가

- DomainService (하부 항목에 대한 테스트 코드 우선적 구현)

    1. ExerciseService에 이름으로 Exercise 가져오는 메소드 추가 -> Impl에 구현

    2. UserExerciseService에 유저의 특정 날짜 운동 기록들 가져오는 메소드 추가 -> Impl에 구현

    3. UserService 에 닉네임이나 이름 검색으로 만족하는 유저들 가져오는 메소드, 총 개수 가져오는 메소드 추가 -> Impl 구현 

    4. UserService에 유저 운동 기록 추가, 삭제하는 메소드 추가 -> Impl에 구현

    5. ExerciseService에 신체부위별 운동 종류들 모두 반환하는 메소드 추가 -> Impl에 구현

- Util

    1. PageUtil 추가 - Page size 모아놓는 클래스

    2. SessionUtil 추가 - 세션에 접속자의 user_id 추가, 삭제,반환 하는 메소드, 모든 세션 삭제하는 메소드 구현 

- Templates

    1. ExerciseCalendar (html,css,js파일 추가) - 유저의 운동 기록들 볼수 있는 화면

    2. UserManage (html 추가) - 매니저 이상의 권한자가 유저들 검색할 수 있는 화면

### 2021.6.24

- AOP

    1. LogAspect - 유저가 요청을 보냈을때 로깅 구현

- UserExerciseRestController

    1. 반환형 List<UserExercise> -> List<UserExerciseWithDateDTO> 로 교체 (UserExercise 그대로 하면 User와의 양방향 매핑때문에 순환 참조)

- Domain

    1. UserExercise date 자료형 : Date -> Calendar (Date는 Deprecated)


- DTO

    1. UserExerciseWithDateDTO 클래스 추가 -> 유저의 특정 날짜 운동 기록 검색 응답용 DTO

- SessionUtil

    1. 로그인 하기 전에 있던 주소로 리다이렉트 하기위해 전에 있던 주소 세션에 저장용 메소드 추가 

- Template 

    1. exerciseCalendar.js - 특정 날짜 운동 기록 가져오고 반영하는 Ajax 추가  


### 2021.6.25

- ExerciseManagerController

    1. 매니저가 운동 종류 추가 처리하는 메소드 구현

- ExerciseRestController

    1. 운동 정보 반환하는 메소드 구현

- DTO

    1. DTO 들 사용 목적별 패키지 분기 (Request Param 바인딩용, Reponse 객체용, DB 쿼리 결과 바인딩용)

    2. AddExerciseDTO 추가 - 매니저가 운동 종류 추가할떄 요청 파라미터 바인딩용

    3. ExerciseInfoDTO 추가 - 운동 정보 클라이언트에 보낼때 사용

- Error

    1. MuscleErrorCode, TargetTypeErrorCode, ExercisePostErrorCode,LikeErrorCode, 추가 

    2. MuscleNotFoundedException, TargetTypeNotFoundedException, ExercisePostNotFoundedException,LikeNotFoundedException 추가

- ExerciseService (하위 항목들은 Test 코드 선 작성 후 구현)

    1. saveWithAddExerciseDTO 매소드 추가 - DTO를 통해 운동 정보 추가하는 메소드, Impl에 구현

- ExercisePostRestController -> 하위 항목에 대한 테스트 코드 작성

    1. 운동 게시글에 좋아요 누르는 요청 처리하는 메소드 추가 

    2. 운동 게시글에 좋아요 취소하는 요청 처리하는 메소드 추가 

- PostLikeRepository

    1. User Id, ExercisePost Id로 PostLike 찾는 메소드 추가 

- PostLikeService -> Impl에 구현, 하위 항목에 대한 테스트 코드 작성

    1. User Id, ExercisePost Id로 PostLike 찾는 메소드 추가 
 
    2. User, ExercisePost로 PostLike 추가하는 메소드 추가 

    3. delete(PostLike postLike)에 User,ExercisePost의 postLikes.remove 로직 추가 

- ExerciseSpace Entity 및 관련 서비스 클래스들 삭제 (별다른 의미 없는 테이블이라 판단)

--- 

## To do List

- 비즈니스 예외 구조도 만들기 

