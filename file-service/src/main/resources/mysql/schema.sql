create table file
(
    id            bigint auto_increment,
    create_time   datetime default CURRENT_TIMESTAMP not null,
    extension     varchar(10)                        not null,
    file_crop_url varchar(255),
    file_type     varchar(255),
    file_url      varchar(255)                       not null,
    mime_type     varchar(100)                       not null,
    size          bigint                             not null,
    status        varchar(255),
    update_time   datetime                           not null,
    user_id       bigint                             not null,
    primary key (id)
)