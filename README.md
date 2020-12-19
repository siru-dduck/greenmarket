# Green Market
쿠버네티스 MSA기반의 중고거래사이트

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
---

# ✅ TODO

* [ ] chatservice에 redis adapter 적용시 socket.emit애서 발생하는 에러해결
* [ ] user profile 기능 (회원정보수정, 구매내역 조회, 상품등록내역 조회)
* [ ] product service Request Param Dto 생성 및 Validation 적용
* [ ] product service Spring Sequrity 적용
* [ ] product service 상품등록시에 트랜잭션 롤백상황시 모든 파일이 일괄삭제되지않는 버그 수정