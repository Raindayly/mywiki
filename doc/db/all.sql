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

insert into wikidev.`doc` (`id`,`ebook_id`, `parent`, `name`,`sort`,`view_count`, `vote_count`) values ('1','1','0', 'Assembly',1, 602442845, 32797968);
insert into wikidev.`doc` (`id`,`ebook_id`, `parent`, `name`,`sort`,`view_count`, `vote_count`) values ('2', '1','1','Lisp', 1,44, 6731794);
insert into wikidev.`doc` (`id`,`ebook_id`, `parent`, `name`,`sort`,`view_count`, `vote_count`) values ('3','1','1', 'WebAssembly',1, 883, 700310);
insert into wikidev.`doc` (`id`,`ebook_id`, `parent`, `name`,`sort`,`view_count`, `vote_count`) values ('4','1','1', 'Hack',1, 593876661, 309);
insert into wikidev.`doc` (`id`,`ebook_id`, `parent`, `name`,`sort`,`view_count`, `vote_count`) values ('5','1','1', 'NATURAL', 1,758210704, 4608924);
insert into wikidev.`doc` (`id`,`ebook_id`, `parent`, `name`,`sort`,`view_count`, `vote_count`) values ('6','1','1', 'Ada',1, 983018, 4);
insert into wikidev.`doc` (`id`,`ebook_id`, `parent`, `name`,`sort`,`view_count`, `vote_count`) values ('7','1','1', 'Tcl', 1,2, 531);
insert into wikidev.`doc` (`id`,`ebook_id`, `parent`, `name`,`sort`,`view_count`, `vote_count`) values ('8', '1','1','COBOL',1, 34011360, 7470560);
insert into wikidev.`doc` (`id`,`ebook_id`, `parent`, `name`,`sort`,`view_count`, `vote_count`) values ('9','1','1', 'ABAP', 1,5199437, 603);
insert into wikidev.`doc` (`id`,`ebook_id`, `parent`, `name`,`sort`,`view_count`, `vote_count`) values ('10','1','0', 'SPARK', 1,33, 95393250);
insert into wikidev.`doc` (`id`,`ebook_id`, `parent`, `name`,`sort`,`view_count`, `vote_count`) values ('11','1','10', 'SPARK', 1,79145, 5503174);
insert into wikidev.`doc` (`id`,`ebook_id`, `parent`, `name`,`sort`,`view_count`, `vote_count`) values ('12','1','10', 'Perl',1, 4090, 609870217);
insert into wikidev.`doc` (`id`,`ebook_id`, `parent`, `name`,`sort`,`view_count`, `vote_count`) values ('13','1','10', 'Io', 1,858066146, 6729);
insert into wikidev.`doc` (`id`,`ebook_id`, `parent`, `name`,`sort`,`view_count`, `vote_count`) values ('14','1','10', 'Small Basic',1, 7, 1509760);
insert into wikidev.`doc` (`id`,`ebook_id`, `parent`, `name`,`sort`,`view_count`, `vote_count`) values ('15','1','10', 'NATURAL',1, 56531962, 455);
insert into wikidev.`doc` (`id`,`ebook_id`, `parent`, `name`,`sort`,`view_count`, `vote_count`) values ('16','1','10', 'Hack',1, 76370, 240);
insert into wikidev.`doc` (`id`,`ebook_id`, `parent`, `name`,`sort`,`view_count`, `vote_count`) values ('17','1','10', 'Emacs Lisp',1, 65, 61);
insert into wikidev.`doc` (`id`,`ebook_id`, `parent`, `name`,`sort`,`view_count`, `vote_count`) values ('18','1','10', 'bc', 1,11882180, 59981);
insert into wikidev.`doc` (`id`,`ebook_id`, `parent`, `name`,`sort`,`view_count`, `vote_count`) values ('19','1','10', 'Tcl', 1,188267, 4663);
insert into wikidev.`doc` (`id`,`ebook_id`, `parent`, `name`,`sort`,`view_count`, `vote_count`) values ('20','1','10', 'RPG', 1,43556, 369);