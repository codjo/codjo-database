drop trigger TR_AP_TOTO_I if exists;
/*  Insert trigger "TR_AP_TOTO_I" for table "AP_TOTO"  */
create trigger TR_AP_TOTO_I after insert on AP_TOTO referencing new row as newrow for each row
begin atomic
DECLARE V CHAR(10);
set V = 'V';
end;
