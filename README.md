# 초록장터

2020년 한이음 ICT 멘토링 프로젝트 '쿠버네티스기반 MSA 어플리케이션'에서 진행한 프로젝트로 중고거래사이트를 각각 독립적으로 배포가 가능한 Micro Service로 개발후 쿠버네티스에 배포하는것을 목표로 진행된 프로젝트입니다. 해당 프로젝트는 중고거래사이트 프로젝트이며 인증과 사용자 정보를 담당하는 user 서비스, 상품을 담당하는 product 서비스, 채팅을 담당하는 chat service로 나누어져 있어 각각 독립적으로 배포 및 개발이 가능 합니다. 각각의 서비스는 도커이미지로 만든 후에 쿠버네티스상에 배포됩니다. 각각의 서비스는 느슨한 결합도를 위해 서비스간에 http통신을 통해 느슨한 결합을 지향했습니다. 
http로 인해 발생하는 오버헤드를 줄이기 위해 추후 gRPC그리고 메세지큐(Kafka)를 도입할 예정입니다.<br><br>
**프로젝트기간** : 2020년 5월 ~ 2020년 11월 
(💻현재 개인으로 리팩토링 중)

---

## 💻 서비스별 요약
### Product Service
**사용기술** : Spring Boot, Spring Web MVC, Mybatis
* 중고상품등록, 조회
* 관심상품 등록
* 상품검색
### User Service
**사용기술** :  express, sequelize, jwt
* 회원가입 로그인
* 사용자 조회
### Chat Service
**사용기술** :  express, sequelize, socket.io
* 판매자와 구매자간의 채팅
  
## 💾 프로젝트 디렉토리 구조
|Directory|Description|
|------|------|
|[frontend](https://github.com/sinwoo1225/greenmarket/tree/master/frontend)|어플리케이션의 화면을 담당하는 React기반의 프로젝트 입니다. 웹페이지에 필요한 컴포넌트와 API간의 통신을 담당하고 있습니다.|
|[product](https://github.com/sinwoo1225/greenmarket/tree/master/product)|`product`는 상품에 대한 전반적인 기능을 담당하는 프로젝트입니다. 중고거래를 이용하는 사용자가 등록된 상품을 읽고 검색할 수 있으며 중고거래를 위해 상품을 등록,수정,삭제가 가능합니다. 상품에 대한 관심을 추가할 수 있습니다.|
|[user](https://github.com/sinwoo1225/greenmarket/tree/master/user)|`user`는 사용자의 인증과 조회, 회원가입, 프로필 수정에 대한 부분을 다루는 프로젝트입니다. 인증은 Jwt 발급을 통해 다른 각각의 서비스에서 인증을 할 수 있도록 했습니다.|
|[chat](https://github.com/sinwoo1225/greenmarket/tree/master/chat)|`chat`은 사용자간의 채팅을 담당하는 프로젝트입니다. `socket-io`를 통해 양방향 통신 채팅을 구현했습니다. 사용자간의 채팅을 통해 거래가 이루어집니다.|
|[deployments](https://github.com/sinwoo1225/greenmarket/tree/master/deployments)|`deployments`는 각강의 서비스를 쿠버네티스에 배포하는데 필요한 `yaml`파일들을 정의했습니다. 어플리케이션 배포에 필요한 ingress, deployment, dashboard, persistent-volume을 생성할 수 있습니다.
---
## 설계
### 시스템 구성도
![시스템 구성도](/images/screenshot/system_design_diagram.png)
### [ER 다이어그램](https://www.erdcloud.com/d/BqDdP5eA6TcskXWvn)
![ER 다이어그램](/images/screenshot/erd.PNG)

---
## 실행화면
![실행화면 - 상품조회](/images/screenshot/screenshot1.PNG)
![실행화면 - 상품상세조회](/images/screenshot/screenshot2.PNG)
![실행화면 - 유저간 채팅](/images/screenshot/screenshot3.PNG)

---
## 어려웠던 점 & 극복과정

### 마이크로 서비스간의 통신
마이크로 서비스 설계시 서비스간의 주고받는 데이터와 데이터베이스의 논리적인 분리가 처음 설계하는 부분이어서 어려웠지만 서비스간의 의존성을 분석하고 정리하여 chat, user, product서비스로 분리하여 설계하고 개발할 수 있었다. 현재는 트랜잭션이 없지만 앞으로 Image File Service, Commuity Service(게시판 기반의 커뮤니티)로 확장할때 논리적으로 분리된 서비스간에 어떻게 트랜잭션을 할지에 대한 고민이 있었다. 현재 생각하고 있는 방법은 Message Queue기반의 Saga패턴을 이용해 서비스간의 트랜잭션을 구현할 예정이다.

### socket-io.redis 적용시 ```await```이후 ```socket.emit``` 호출시 발생하는 에러
```javascript
io.adapter(redis({ host: REDIS_MASTER_HOST, port: Number(REDIS_MASTER_PORT) })); // socket.io-redis 적용
// 생략 ...
socket.on("sendMessage", async ({ message, userId, roomId }) => {
		try {
			// 생략 ...
			socket
				.to(`room_${roomId}`)
				.emit("sendMessage", { ...response.dataValues }); // ❗ 에러발생
		} catch (error) {
			console.log("❌", error);
		}
	});
```
socket.io-redis를 적용했을때 `await` 이후에 socket.io의 `emit`함수에서 에러가 발생하여 개발자 커뮤니티를 통해 문제를 찾으려 했지만 해당문제에 관한 논의나 이슈가 없어 문제점을 찾지 못했는데 코드내부를 살펴본 결과 최신버전의 `socket.io.-redis`와 이전 버전의 동작이 다름을 알았고 버전을 낮춰 버전호환성의 문제임을 깨달았다. 나와 같은 문제를 겪은 사람을 위해 `socket.io-redis` github 이슈사항에 등록했다.

### 여러개로 분리된 Pod에서의 `websocket`통신
쿠버네티스 상에 어플리케이션을 서비스별로 배포하면 각 서비스는 여러 Pod이 배포된다. 이 Pod의 단위는 논리적으로 독립되어있기 때문에 Pod간의 데이터 공유가 되지않는다. 그렇기 때문에 웹소켓 통신을 위한 데이터도 분리되어있기 때문에 어려움을 겪었다. 클라이언트별로 일정한 Pod에 요청을 고정시키는 sticky session을 도입했지만 데이터를 전송할때는 문제가 없으나 데이터를 받고 다른 소켓을 찾아 응답을 보낼때는 소켓정보가 부재한 경우가 있어 양방향 데이터 전송이 불가능했다. 이를 위해 pub/sub기능을 제공하는 redis를 도입해 이벤트 전파방식을 이용하는 pub/sub기능으로 Pod간에 통신을 통해 원하는 소켓을 통해 데이터를 전송함으로써 문제를 해결했다. 

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


### 2021년 1월24일
* 상품등록, 수정시 유효성검사에서 발생한 버그 수정
* 채팅방 조회 API Promise.all기반으로 리팩토링 

### 2021년 1월26일
* 카테고리 항목추가(가구/인테리어, 식물)
* 상품설명에 있는 개행문자('\n')를 ```<br>```태그로 변환

---

# ✅ TODO

* [x] ~~chatservice에 redis adapter 적용시 socket.emit애서 발생하는 에러해결~~
* [x] ~~회원정보수정 기능~~
* [x] ~~product service Request Param DTO 생성 및 Validation 적용~~
* [x] ~~product service Interceptor 적용~~
* [x] ~~product service 상품등록시에 트랜잭션 롤백상황시 모든 파일이 일괄삭제되지않는 버그 수정 => 파일은 Spring Batch로 처리할 예정~~
* [x] ~~product 수정, 삭제 기능 추가~~
* [ ] 구매후기 기능 
* [ ] 구매내역 조회
* [ ] 상품등록내역 조회
* [ ] gRPC 또는 메세지 큐(Kafka)도입
* [ ] DB를 Product, User, Chat Service별로 분리 (느슨한 결합도)
* [ ] Image File Service 개발 (Image Crop 기능포함)
* [ ] CI/CD 도입
* [ ] 카카오톡, 네이버 로그인기능
