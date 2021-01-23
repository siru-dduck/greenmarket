# 초록장터

2020년 한이음 ICT 멘토링 프로젝트 '쿠버네티스기반 MSA 어플리케이션'에서 진행한 프로젝트로 중고거래사이트를 각각 독립적으로 배포가 가능 Micro Service로 개발후 쿠버네티스에 배포하는것을 목표로 진행된 프로젝트입니다. 해당 프로젝트는 중고거래사이트 프로젝트이며 인증과 사용자 정보를 담당하는 user 서비스, 상품을 담당하는 product 서비스, 채팅을 담당하는 chat service로 나누어져 있어 각각 독립적으로 배포 및 개발이 가능 합니다. 각각의 서비스는 도커이미지로 만든 후에 쿠버네티스상에 배포됩니다. 각각의 서비스는 느슨한 결합도를 위해 서비스간에 http통신을 통해 느슨한 결합을 지향했습니다. 
http로 인해 발생하는 오버헤드를 줄이기 위해 추후 gRPC또는 메세지큐(Kafka)를 도입할 예정입니다.
프로젝트기간 : 2020년 5월 ~ 2020년 11월 
(💻현재 개인으로 리팩토링 중)

---

## 💻 기능
### Product Service
* Spring Boot, Spring Web MVC
* 중고상품등록, 조회
* 관심상품 등록
* 상품검색
### User Service
* express, jwt
* 회원가입 로그인
* 사용자 조회
### Chat Service
* express, socket.io
* 판매자와 구매자간의 채팅
  
---

# Link
###  ERD: https://www.erdcloud.com/d/BqDdP5eA6TcskXWvn   

---

# 📕 Daily Report 
###  2020년 12월19일
* product_article 조회시 집계함수 대신 interest_count필드로 조회하도록 기능 추가
* 상품조회 업데이트에서 Dependant Subquery로 인한 성능저하요소를 집계필드(interest_count)를 조회함으로써 성능개선
* 관심추가,제거시 중복검사와 interest_count update하는 기능을 하나의 트랜잭션으로 묶음
* 상품조회 파라미터 order에 따른 동작개선
* 관심기능 프론트엔드 UI 구현 및 API 연동
* README 추가

### 2020년 12월20일
* user auth api에서 쿠키에 jwt토큰을 보내지 않을때 400(Bad Request)응답에서 200번 응답으로 변경후 응답메세지에 { ...isAuth: false }를 추가
* chatservice에 redis adapter 적용시 socket.emit애서 발생하는 에러해결 => socket.io-redis의 버전을 6.0.1.에서 5.2.0으로 다운그래이드함으로써 해결

### 2020년 12월30일
* 상품삭제기능(백앤드) 추가
* 상품삭제시 chat 서비스의 chat_room 테이블 중 삭제된 게시글을 참조하는 행에 대한 처리를 cascade로 할지는 추후 결정 

### 2021년 1월4일
* 상품수정 기능 백앤드 개발진행중 게시글 로직 작성

### 2021년 1월5일
* 상품수정 기능 백앤드 구현(게시글 및 파일첨부 수정)

### 2021년 1월7일
* 상품수정페이지 구현
* 상품상세페이지에서 거래완료된 상품의 경우 거래완료여부를 게시글제목옆에 표시
* 상품 수정,등록페이지에서 첨부파일 삭제기능 구현
  
### 2021년 1월8일
* 유저수정기능(백앤드) 구현
* 유저프로필 사진 업로드 기능 구현

### 2021년 1월13일
* 유저프로필 페이지 추가

### 2021년 1월15일
* Product Service Bean Validation 추가
  
### 2021년 1월18일
* jwt인증 인터셉터 추가
* jwt토큰 아규먼트리졸버 추가
* 상품수정,삭제와 관심상품추가,삭제에 인가기능 추가

### 2021년 1월19일
* Product Service 리팩토링
* 페이징 방식을 페이지방식에서 More 버튼을 이용하는 UX를 고려해 변경
* Product Service에서 user 테이블에 직접 접근하지않고 User Service를 통해 데이터 접근
* product article 테이블에 주소(address1, address2)팔드추가


### 2021 1월24일
* 상품등록, 수정시 유효성검사에서 발생한 버그 수정
* 채팅방 조회 API Promise.all기반으로 리팩토링 
---

# ✅ TODO

* [x] chatservice에 redis adapter 적용시 socket.emit애서 발생하는 에러해결
* [x] 회원정보수정 기능
* [ ] 구매후기 기능 
* [ ] 구매내역 조회
* [ ] 상품등록내역 조회
* [x] product service Request Param DTO 생성 및 Validation 적용
* [x] product service Interceptor 적용
* [x] product service 상품등록시에 트랜잭션 롤백상황시 모든 파일이 일괄삭제되지않는 버그 수정 => 파일은 Spring Batch로 처리할 예정 
* [x] product 수정, 삭제 기능 추가
* [ ] gRPC 또는 메세지 큐(Kafka)도입
* [ ] DB를 Product, User, Chat Service별로 분리 (느슨한 결합도)
* [ ] Image File Service 개발 (Image Crop 기능포함)
* [ ] CI/CD 도입
* [ ] 카카오톡, 네이버 로그인기능