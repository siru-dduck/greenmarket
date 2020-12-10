-- category 
INSERT INTO category(name) values("디지털/가전");
INSERT INTO category(name) values("유아동/유아도서");
INSERT INTO category(name) values("생활/가공식품");
INSERT INTO category(name) values("스포츠/레저");
INSERT INTO category(name) values("여성잡화");
INSERT INTO category(name) values("여성의류");
INSERT INTO category(name) values("남성패션/잡화");
INSERT INTO category(name) values("게임/취미");
INSERT INTO category(name) values("뷰티/내용");
INSERT INTO category(name) values("반려동물용품");
INSERT INTO category(name) values("도서티켓/음반");
INSERT INTO category(name) values("기타 중고물품");

-- user
insert into user(email,password,address1,address2,nickname) values('test@email.com', '1234','인천광역시','계양구','시루떡');
insert into user(email,password,address1,address2,nickname) values('test2@email.com', '1234','인천광역시','계양구','유진떡');
insert into user(email,password,address1,address2,nickname) values('test3@email.com', '1234','인천광역시','계양구','슬비즈');

-- product_article
insert into product_article(title, content, price, user_id, category_id ) values('에어팟 팔아요','사용한지 거의 안된 에어팟입니다. 싸게 내놓습니다.', 230000, 1, 1);
insert into product_article(title, content, price, user_id, category_id ) values('가습기 팔아요','요즘 잘 안사용하는데 사용하는데 문제없습니다.', 70000, 1, 1);
insert into product_article(title, content, write_date, price, user_id, category_id) values('아이패드 팜','아이패드 프로 1세대 입니다.', now(), 200000, 2, 1);
    
-- product_image
insert into product_image(article_id, list_num, file_url)
    values(4,1,'/static/images/img_1.jpg'),
		      (4,2,'/static/images/img_2.jpg'),
          (4,3,'/static/images/img_3.jpg');
        
-- product_interest
insert into product_interest(article_id, user_id, status) values(1,3,1);

-- chat_room
INSERT INTO chat_room(article_id, user_id_seller, user_id_buyer) values(1,1,2);


-- chat_message
INSERT INTO chat_message(room_id, sender_type, message) values(1, 0, '혹시 구매 가능할까요?');
INSERT INTO chat_message(room_id, sender_type, message) values(1, 1, '아직 판매안됐습니다 ^^ 구매가능해요!');