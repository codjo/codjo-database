create table AP_GROUP_PERSON
(
    MY_ID      integer  not null,
    MY_VARCHAR      varchar(15)  null
 constraint CKC_MY_VARCHAR check (MY_VARCHAR in ('A','B')),
    MY_TIMESTAMP      timestamp default systimestamp  null
);
