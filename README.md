# Touch-My-Body

최신 버전 : ver 0.0.5

## 프로젝트 주 기능

- 웨이트 트레이닝 개인 운동 기록 

- 웨이트 트레이닝 관련 정보 제공

- 웨이트 트레이닝 관련 커뮤니티 제공

---

## 스키마 

- 프로젝트명: Touch My Body

- 프로젝트 참여 인원: 1명 (김건영)

- 개발 기간: 2021.06.21 ~ 2021.07.11

- 사용기술 및 개발 환경: Spring Boot, Spring JPA, Spring MVC, Spring Security, JUnit5, Maven, 

- 사용 데이터베이스

   - Prod: Mysql 8.0

   - Session Storage: Redis

   - Cache Storage: Redis

   - Test: H2

--- 

## 코드 배포 구조

![tmb_deploy_stc1](https://user-images.githubusercontent.com/60494603/125197187-a7e9f980-e297-11eb-8e5f-242cd46a5d3d.png)

---

## DB 테이블 설계 

- 테이블 설명 : [테이블 설명 보러가기](https://github.com/Gunyoung-Kim/TouchMyBody_ABOUT/blob/master/DB_Info.md)

![TouchMyBodyTable](https://user-images.githubusercontent.com/60494603/125125625-cc799080-e134-11eb-8868-5ade689ad6db.png)

---

## 성능 개선 

- JPQL 사용에 따른 N+1 문제 발생에 대한 경각심 고취

   1. 모든 연관 엔티티 로딩 -> 지연 로딩으로 설정 

   2. 엔티티 로딩 시 연관 엔티티 로딩도 필요시 크게 2가지 방식 사용

        2-1. 페치 조인 활용 

        2-2. 쿼리 결과를 양쪽 엔티티에서 필요한 필드들로만 구성된 DTO 객체에 매핑

- Cache의 활용

   1. Cache Storage로 REDIS 활용

   2. 사용자의 요청이 잦고 내용 변동이 그나마 적은 항목에 대해 캐싱

- Session Storage와 Cache Storage의 분리 

   1. REDIS의 인메모리 방식의 이점 -> 두 항목의 저장소로 활용

   2. 두 항목 같은 REDIS 저장소 사용 시 메모리 공유 -> 사용자 증가 시 잦은 Swap -> Redis의 디스크 접근 횟수 증가 

      -> Redis의 장점 퇴색 -> 세션과 캐시 서로 다른 Redis 저장소 방식 채택!

---

### 테스트 방식 

1. RestController, Controller 클래스 

   - 통합 테스트 : 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 

2. Service 클래스 

   - 통합 테스트 : 서비스 계층 - 영속성 계층 

---

## 코드 패키지 

- 패키지 설명: [패키지 설명 보러가기](https://github.com/Gunyoung-Kim/TouchMyBody_ABOUT/blob/master/Package_Info.md)

--- 

## Git 브랜치 전략 

### Git-Flow 전략 사용

- master: 실제 배포가 진행되는 브랜치

- develop: 리팩토링, 버그 수정, 테스트 코드 추가가 진행되는 브랜치 

- feature: 기능 개발이 진행되는 브랜치

- release: 주석 작성, README 수정 등 배포 준비 진행되는 브랜치 

- hotfix: 배포시 문제가 발생했을때 빠른 조치를 위해 사용하는 브랜치

-> master, develop 브랜치만이 계속 유지 나머지는 생성 삭제 반복


---

## 개발 일지 

- 개발 일지 링크 : [개발일지 보러가기](https://github.com/Gunyoung-Kim/TouchMyBody_ABOUT/blob/master/Development_Log.md)

--- 

## 버전 기록 

- 버전 기록 링크 : [버전기록 보러가기](https://github.com/Gunyoung-Kim/TouchMyBody_ABOUT/blob/master/Version_Note.md)

---

TouchMyBody 2021-

