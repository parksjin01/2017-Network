create table user(
     user_ID varchar(30),
     password varchar(30) not null,
     name varchar(20) not null,
     phone_number varchar(20),
     address varchar(30),
     e_mail varchar(30),
     sex varchar(10),
     total_storage float(1),
     usage_storage float(1),
     primary key (user_ID, phone_number));

 create table file(
     user_ID varchar(30),
     file_ID varchar(80),
     file_name varchar(80),
     type varchar(10) not null,
     category varchar(20) not null,
     directory varchar(100) not null,
     size float(1),
     date varchar(50),
     share_offset varchar(80),
     download int,
     backup int,
     primary key(file_ID),
     foreign key(user_ID) references user(user_ID));

create table share_group(
     group_ID varchar(80),
     group_name varchar(30) not null,
     headcount int,
     primary key(group_ID));

create table group_member(
    group_ID varchar(80),
    user_ID varchar(30),
    primary key(group_ID, user_ID),
    foreign key(group_ID) references share_group(group_ID),
    foreign key(user_ID) references user(user_ID));
