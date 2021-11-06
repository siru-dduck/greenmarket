create table category
(
    id   int auto_increment,
    name varchar(30) not null,
    primary key (id)
);

create table product_image
(
    id         bigint auto_increment,
    file_id    bigint  not null,
    list_num   integer not null,
    product_id bigint  not null,
    primary key (id),
    key `product_image_product_id_idx` (`product_id`, `list_num`) using btree
);

create table product_interest
(
    id          bigint auto_increment,
    create_date datetime not null,
    user_id     bigint   not null,
    product_id  bigint   not null,
    primary key (id),
    unique key `product_interest_product_id_idx` (`product_id`, `user_id`) using btree
);

create table product_article
(
    id             bigint auto_increment,
    address1       varchar(100) not null,
    address2       varchar(100) not null,
    content        TEXT         not null,
    create_date    datetime     not null,
    interest_count integer      not null,
    price          integer      not null,
    status         varchar(255),
    title          varchar(255) not null,
    update_date    datetime     not null,
    user_id        bigint       not null,
    category_id    integer      not null,
    review_id      bigint,
    primary key (id),
    key `product_article_user_id_idx` (`user_id`) using btree,
    fulltext key `product_article_title_idx` (`title`, `content`)
);

create table product_review
(
    id          bigint auto_increment,
    content     TEXT     not null,
    create_date datetime not null,
    user_id     bigint   not null,
    primary key (id)
);
