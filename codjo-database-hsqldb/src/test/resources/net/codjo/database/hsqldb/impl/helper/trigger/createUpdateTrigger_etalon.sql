drop trigger TR_AP_TOTO_U if exists;
/*  Update trigger "TR_AP_TOTO_U" for table "AP_TOTO"  */
create trigger TR_AP_TOTO_U after update on AP_TOTO referencing old row as oldrow new row as newrow for each row
begin atomic
DECLARE V CHAR(10);
set V = 'V';
end;
