drop trigger TR_AP_TOTO_IU_INSERT if exists;
/*  Insert trigger "TR_AP_TOTO_IU_INSERT" for table "AP_TOTO"  */
create trigger TR_AP_TOTO_IU_INSERT after insert on AP_TOTO referencing new row as newrow for each row
begin atomic

    /*  Parent "AP_PARENT_TOTO" must exist when inserting a child in "AP_TOTO"  */
       if not exists(select 1
           from   AP_PARENT_TOTO t1
           where  t1.PARENT_ID = newrow.ID) then
             signal SQLSTATE '30002' set MESSAGE_TEXT='Parent does not exist in "AP_PARENT_TOTO". Cannot create child in "AP_TOTO".';
    end if;

end;
drop trigger TR_AP_TOTO_IU_UPDATE if exists;
/*  Update trigger "TR_AP_TOTO_IU_UPDATE" for table "AP_TOTO"  */
create trigger TR_AP_TOTO_IU_UPDATE after update on AP_TOTO referencing old row as oldrow new row as newrow for each row
begin atomic

    /*  Parent "AP_PARENT_TOTO" must exist when inserting a child in "AP_TOTO"  */
       if not exists(select 1
           from   AP_PARENT_TOTO t1
           where  t1.PARENT_ID = newrow.ID) then
             signal SQLSTATE '30002' set MESSAGE_TEXT='Parent does not exist in "AP_PARENT_TOTO". Cannot create child in "AP_TOTO".';
    end if;

end;
