create database green_market;
use green_market;

create table category
(
	id int auto_increment comment '카테고리 아이디'
		primary key,
	name varchar(30) not null comment '카테고리  이름'
);

create table chat_room
(
	id int auto_increment comment '체팅방아이디'
		primary key,
	article_id int not null comment '게시글 아이디',
	user_id_buyer int not null comment '사용자 id',
	user_id_seller int not null,
	constraint UQ_CHAT_ROOM
		unique (article_id, user_id_buyer)
);

create table chat_message
(
	id int auto_increment
		primary key,
	room_id int not null comment '체팅방아이디',
	user_id int not null comment '메세지를 보낸 사용자 아이디',
	message text not null comment '채팅메세지',
	create_date datetime default CURRENT_TIMESTAMP not null comment '채팅을 보낸날짜',
	constraint chat_message_chat_room_id_fk
		foreign key (room_id) references chat_room (id)
			on delete cascade
);

create table product_article
(
	id int auto_increment comment '게시글 아이디'
		primary key,
	title varchar(100) not null comment '게시글 제목',
	content text null comment '게시글 내용',
	write_date datetime default CURRENT_TIMESTAMP not null comment '게시글 작성시각',
	update_date datetime null comment '게시글 수정시각',
	price int not null comment '상품가격',
	interest_count int default 0 null comment '상품 관심수 (cf 좋아요 갯수와 비슷한 개념)',
	user_id int not null comment '게시글작성자 ID (user ID)',
	category_id int not null comment '카테고리 아이디',
	status bit default b'0' not null comment '상품거래 상태(거래진행중:0, :거래완료:1)',
	address1 varchar(255) not null comment '주소(시,군)',
	address2 varchar(255) not null comment '주소(구,읍,면)' 
);

create table product_image
(
	list_num int not null comment '게시글 이미지 리스트 번호',
	article_id int not null comment '게시글 아이디',
	file_url varchar(255) not null comment '이미지 파일 url',
	primary key (list_num, article_id)
);

create table product_review
(
	article_id int not null comment '게시글 아이디'
		primary key,
	content text not null comment '상품거래후기 내용',
	date datetime default CURRENT_TIMESTAMP not null comment '삼품거래후기 작성시각',
	user_id int not null comment '상품거래후기 작성자 ID (user  ID)'
);

create table user
(
	id int auto_increment comment '사용자 id'
		primary key,
	email varchar(50) not null comment 'OAuth 용 이메일 (카카오,구글,네이버)',
	password varchar(100) not null comment '비밀번호',
	address1 varchar(30) not null comment '주소1',
	address2 varchar(30) not null comment '주소2',
	nickname varchar(30) not null comment '사용자 별명',
	profile_image_url varchar(255) null comment '프로필 이미지 파일 url',
	kakao_id int null,
	naver_id int null,
	constraint email
		unique (email)
);

create table product_interest
(
	article_id int not null comment '상품게시글 아이디', 
	user_id int not null comment '사용자 아이디',
	constraint product_interest_pk
		unique (article_id, user_id)
)