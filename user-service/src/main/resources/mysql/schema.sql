create table user
(
    id              bigint auto_increment,
    address1        varchar(30)  not null,
    address2        varchar(30)  not null,
    creat_date      datetime     not null,
    email           varchar(100) not null,
    nickname        varchar(30)  not null,
    password        varchar(120) not null,
    profile_file_id bigint,
    status          varchar(255) not null,
    update_date     timestamp    not null,
    primary key (id),
    key `user_profile_file_id_idx` (`profile_file_id`) using btree
);

create table refresh_token
(
    id          bigint auto_increment,
    create_date datetime      not null,
    expire_date datetime      not null,
    token       varchar(1500) not null,
    token_id    varchar(100)  not null,
    update_date datetime      not null,
    user_id     bigint        not null,
    primary key (id),
    KEY `refresh_token_user_id_IDX` (`user_id`) USING BTREE
);