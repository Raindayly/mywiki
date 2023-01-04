drop table if exists `test`;
create table `test` (
    `id` bigint not null comment 'id',
    `name` varchar(50) comment '名称',
    `password` varchar(50) comment '密码',
    primary key (`id`)
) engine = innodb default charset = utf8mb4 comment='测试';
insert into `test` (`id`, `name`, `password`) values (1 , 'test' , '123');


drop table if exists `demo`;
create table `demo` (
                        `id` bigint not null comment 'id',
                        `name` varchar(50) comment '名称',
                        primary key (`id`)
) engine = innodb default charset = utf8mb4 comment='demo';
insert into `demo` (`id`, `name`) values (1 , 'demo');


# 添加ebook这张表
drop table if exists `ebook`;

create table `ebook`
(

    `id`           bigint not null comment 'id',
    `name`         varchar(50) comment '名称',
    `category1_id` bigint comment '分类1',
    `category2_id` bigint comment '分类2',
    `description`  varchar(200) comment '描述',
    `cover`        varchar(200) comment '封面',
    `doc_count`    int comment '文档数',
    `view_count`   int comment '阅读数',
    `vote_count`   int comment '点赞数',
    primary key (`id`)

) engine=innodb default charset=utf8mb4 comment='电子书';


insert into ebook (id, name, description) values (1, 'Spring Boot 入门教程', '零基础入门 Java 开发，企业级应用开发最佳首选框架');

insert into ebook (id, name, description) values (2,'Vue 入门教程', '零基础入门 Vue 开发，企业级应用开发最佳首选框架');

insert into ebook (id, name, description) values (3, 'python 入门教程','零基础入门 Python 开发，企业级应用开发最佳首选框架');

insert into ebook (id, name, description) values (4 , 'Mysql 入门教程', '零基础入门 Mysql 开发，企业级应用开发最佳首选框架');

insert into ebook (id, name, description) values (5, 'Oracle 入门教程', '零基础入门 Oracle 开发，企业级应用开发最佳首选框架');


#分类表设计

drop table if exists `category`;
create table `category` (
    `id` varchar(20) not null comment 'id',
    `parent` varchar(20) not null default '0' comment '父id',
    `name` varchar(50) not null comment '名称',
    `sort` int comment '顺序',
    primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='分类';

insert into `category` (id, parent, name, sort) values ('100','000','前端开发',100);
insert into `category` (id, parent, name, sort) values ('101','100','vue',101);
insert into `category` (id, parent, name, sort) values ('102','100','html & css',102);
insert into `category` (id, parent, name, sort) values ('200','000','Java',200);
insert into `category` (id, parent, name, sort) values ('201','200','基础',201);
insert into `category` (id, parent, name, sort) values ('202','200','框架',202);


#文档表设计
-- 文档

create table if not exists wikidev.`doc`
(
    `id` varchar(20) not null comment 'id' primary key,
    `ebook_id` varchar(20) default '0' not null comment '电子书id',
    `parent` varchar(20) default '0' not null comment '父id',
    `name` varchar(50) not null comment '名称',
    `sort` int default 0 not null comment '顺序',
    `view_count` int default 0 not null comment '阅读数',
    `vote_count` int default 0 not null comment '点赞数'
) comment '文档';

insert into wikidev.`doc` (`id`, `name`, `view_count`, `vote_count`) values ('9', 'Assembly', 602442845, 327979688);
insert into wikidev.`doc` (`id`, `name`, `view_count`, `vote_count`) values ('26144680', 'Lisp', 44, 6731794);
insert into wikidev.`doc` (`id`, `name`, `view_count`, `vote_count`) values ('99274111', 'WebAssembly', 883, 700310);
insert into wikidev.`doc` (`id`, `name`, `view_count`, `vote_count`) values ('65557565', 'Hack', 593876661, 309);
insert into wikidev.`doc` (`id`, `name`, `view_count`, `vote_count`) values ('56453324', 'NATURAL', 758210704, 4608924);
insert into wikidev.`doc` (`id`, `name`, `view_count`, `vote_count`) values ('8273901481', 'Ada', 983018, 4);
insert into wikidev.`doc` (`id`, `name`, `view_count`, `vote_count`) values ('82', 'Tcl', 2, 531);
insert into wikidev.`doc` (`id`, `name`, `view_count`, `vote_count`) values ('99428', 'COBOL', 34011360, 7470560);
insert into wikidev.`doc` (`id`, `name`, `view_count`, `vote_count`) values ('31587203', 'ABAP', 5199437, 603);
insert into wikidev.`doc` (`id`, `name`, `view_count`, `vote_count`) values ('470', 'SPARK', 33, 95393250);
insert into wikidev.`doc` (`id`, `name`, `view_count`, `vote_count`) values ('39', 'SPARK', 79145, 5503174);
insert into wikidev.`doc` (`id`, `name`, `view_count`, `vote_count`) values ('371', 'Perl', 4090, 609870217);
insert into wikidev.`doc` (`id`, `name`, `view_count`, `vote_count`) values ('514638', 'Io', 858066146, 6729);
insert into wikidev.`doc` (`id`, `name`, `view_count`, `vote_count`) values ('59', 'Small Basic', 7, 1509760879);
insert into wikidev.`doc` (`id`, `name`, `view_count`, `vote_count`) values ('153275', 'NATURAL', 5653196542, 455);
insert into wikidev.`doc` (`id`, `name`, `view_count`, `vote_count`) values ('35450', 'Hack', 76370, 240);
insert into wikidev.`doc` (`id`, `name`, `view_count`, `vote_count`) values ('449', 'Emacs Lisp', 65, 61);
insert into wikidev.`doc` (`id`, `name`, `view_count`, `vote_count`) values ('4136', 'bc', 11882180, 59981);
insert into wikidev.`doc` (`id`, `name`, `view_count`, `vote_count`) values ('1837', 'Tcl', 188267, 4663);
insert into wikidev.`doc` (`id`, `name`, `view_count`, `vote_count`) values ('38867', 'RPG', 43556, 369);