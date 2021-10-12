create table 应用
(
    id       bigint auto_increment
        primary key,
    app_name varchar(50) not null,
    app_id   varchar(50) not null,
    constraint app_id
        unique (app_id)
);

create table 任务
(
    id           bigint auto_increment
        primary key,
    status       varchar(10)          null,
    trigger_time datetime             not null,
    remove       tinyint(1) default 0 not null,
    call_name    varchar(100)         not null,
    call_data    text                 null,
    call_type    varchar(10)          not null,
    call_host    varchar(20)          null,
    cron         varchar(20)          not null,
    create_time  datetime             not null,
    modify_time  datetime             null,
    app          bigint               not null,
    constraint 任务_应用_fk
        foreign key (app) references 应用 (id)
);

create table 用户
(
    id       bigint auto_increment
        primary key,
    username varchar(100)         not null,
    password varchar(20)          not null,
    prohibit tinyint(1) default 0 not null
);

create table 用户_应用关系表
(
    id      bigint auto_increment
        primary key,
    user_id bigint not null,
    app_id  bigint not null
);


