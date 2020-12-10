drop database green_market;
create database green_market;
use green_market;

CREATE TABLE `product_article` (
	`id`	int	NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '게시글 아이디' ,
	`title`	varchar(100)	NOT NULL	COMMENT '게시글 제목',
	`content`	text	NULL	COMMENT '게시글 내용',
	`write_date`	datetime	NOT NULL	DEFAULT now()	COMMENT '게시글 작성시각',
	`update_date`	datetime	NULL	COMMENT '게시글 수정시각',
	`price`	int	NOT NULL	COMMENT '상품가격',
	`interest_count`	int	NULL	DEFAULT 0	COMMENT '상품 관심수 (cf 좋아요 갯수와 비슷한 개념)',
	`user_id`	int	NOT NULL	COMMENT '게시글작성자 ID (user ID)',
	`category_id`	int	NOT NULL	COMMENT '카테고리 아이디',
	`status`	bit	NOT NULL DEFAULT 0	COMMENT '상품거래 상태(거래진행중:0, :거래완료:1)'
);

CREATE TABLE `user` (
	`id`	int	NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '사용자 id',
	`email`	varchar(50)	NOT NULL UNIQUE	COMMENT 'OAuth 용 이메일 (카카오,구글,네이버)',
	`password`	varchar(100)	NOT NULL	COMMENT '비밀번호',
	`address1`	varchar(30)	NOT NULL	COMMENT '주소1',
	`address2`	varchar(30)	NOT NULL	COMMENT '주소2',
	`nickname`	varchar(30)	NOT NULL	COMMENT '사용자 별명',
	`profile_image_url`	varchar(255)	NULL	COMMENT '프로필 이미지 파일 url',
	`kakao_id`	int	NULL,
	`naver_id`	int	NULL
);

CREATE TABLE `product_review` (
	`article_id`	int	NOT NULL	COMMENT '게시글 아이디',
	`content`	text	NOT NULL	COMMENT '상품거래후기 내용',
	`date`	datetime	NOT NULL	DEFAULT now()	COMMENT '삼품거래후기 작성시각',
	`user_id`	int	NOT NULL	COMMENT '상품거래후기 작성자 ID (user  ID)'
);

CREATE TABLE `product_interest` (
	`article_id`	int	NOT NULL	COMMENT '게시글 아이디',
	`user_id`	int	NOT NULL	COMMENT '사용자 id',
	`status`	bit(1)	NOT NULL	DEFAULT 0	COMMENT '좋아요여부(0:좋아요비활성화, 1:좋아요활성화)'
);

CREATE TABLE `category` (
	`id`	int	NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '카테고리 아이디' ,
	`name`	varchar(30)	NOT NULL	COMMENT '카테고리  이름'
);

CREATE TABLE `product_image` (
	`list_num`	int	NOT NULL	COMMENT '게시글 이미지 리스트 번호' ,
	`article_id`	int	NOT NULL	COMMENT '게시글 아이디',
	`file_url`	varchar(255)	NOT NULL	COMMENT '이미지 파일 url'
);

CREATE TABLE `chat_room` (
	`id`	int	NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '체팅방아이디',
	`article_id`	int	NOT NULL	COMMENT '게시글 아이디',
	`user_id_buyer`	int	NOT NULL	COMMENT '사용자 id',
    CONSTRAINT UQ_CHAT_ROOM UNIQUE (article_id, user_id_buyer)
);

CREATE TABLE `chat_message` (
	`id`	int	NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`room_id`	int	NOT NULL	COMMENT '체팅방아이디',
	`user_id`	int	NOT NULL	COMMENT '메세지를 보낸 사용자 아이디',
	`message`	text NOT NULL	COMMENT '채팅메세지',
	`create_date`	datetime NOT NULL	DEFAULT now()	COMMENT '채팅을 보낸날짜'
);


ALTER TABLE `product_review` ADD CONSTRAINT `PK_PRODUCT_REVIEW` PRIMARY KEY (
	`article_id`
);

ALTER TABLE `product_image` ADD CONSTRAINT `PK_PRODUCT_IMAGE` PRIMARY KEY (
	`list_num`,
	`article_id`
);

ALTER TABLE `product_interest` ADD CONSTRAINT `PK_PRODUCT_INTEREST` PRIMARY KEY (
	`article_id`,
	`user_id`
);

ALTER TABLE `product_review` ADD CONSTRAINT `FK_product_article_TO_product_review_1` FOREIGN KEY (
	`article_id`
)
REFERENCES `product_article` (
	`id`
);

ALTER TABLE `product_image` ADD CONSTRAINT `FK_product_article_TO_product_image_1` FOREIGN KEY (
	`article_id`
)
REFERENCES `product_article` (
	`id`
);

ALTER TABLE `product_interest` ADD CONSTRAINT `FK_product_article_TO_product_interest_1` FOREIGN KEY (
	`article_id`
)
REFERENCES `product_article` (
	`id`
);

ALTER TABLE `product_interest` ADD CONSTRAINT `FK_user_TO_product_interest_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `user` (
	`id`
);