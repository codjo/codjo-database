create table AP_GROUP_PERSON
(
    MY_ID      int auto_increment   not null,
    MY_VARCHAR      varchar(15)  null check (MY_VARCHAR in ('A','B')),
    MY_TIMESTAMP      timestamp default CURRENT_TIMESTAMP()  null,
    primary key (MY_ID, MY_VARCHAR)
) ENGINE=InnoDB;
