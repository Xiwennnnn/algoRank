create database if not exists algo_rating;

drop table if exists algo_user;
drop table if exists lc_rating;
drop table if exists cf_rating;

create table if not exists lc_rating (
    lc_id bigint primary key auto_increment,
    user_name varchar(100) comment '用户名',
    rating int comment '分数',
    grading varchar(100) comment '段位',
    nation_rank int comment '全国排名',
    top_percentage double comment '排名百分比',
    index idx_rating(rating)
);

create table if not exists cf_rating (
    cf_id bigint primary key auto_increment,
    # 加上描述
    user_name varchar(100) comment '用户名',
    rating int comment '分数',
    cf_rank varchar(100) comment '段位',
    max_rating int comment '历史最高分',
    index idx_rating(rating)
);

create table if not exists algo_user(
    user_id   bigint primary key auto_increment,
    real_name varchar(100) comment '真实姓名',
    grade     varchar(100) comment '年级',
    major     varchar(100) comment '专业',
    lc_id     bigint comment 'lc_rating表的id',
    cf_id     bigint comment 'cf_rating表的id',
    foreign key (lc_id) references lc_rating (lc_id) on delete set null,
    foreign key (cf_id) references cf_rating (cf_id) on delete set null,
    index idx_real_name(real_name),
    index idx_grade(grade),
    index idx_major(major)
);

create table if not exists contest(
    contest_id bigint primary key auto_increment,
    oj varchar(100) comment 'oj名称',
    name varchar(100) comment '比赛名称',
    start_time datetime comment '开始时间',
    end_time datetime comment '结束时间',
    status varchar(100) comment '比赛状态',
    oi_contest boolean comment '是否为OI赛制',
    link varchar(100) unique comment '比赛链接',
    index idx_oj(oj),
    index idx_name(name),
    index idx_oi_contest(oi_contest),
    index idx_link(link)
);

create table if not exists user(
    user_id bigint primary key auto_increment,
    username varchar(100) unique comment '用户名',
    password varchar(100) comment '密码',
    is_captain boolean comment '是否是队长',
    index idx_username(username),
    index idx_is_captain(is_captain)
);