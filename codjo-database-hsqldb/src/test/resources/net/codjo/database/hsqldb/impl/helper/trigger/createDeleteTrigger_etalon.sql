drop trigger TR_AP_TOTO_D if exists;
/*  Delete trigger "TR_AP_TOTO_D" for table "AP_TOTO"  */
create trigger TR_AP_TOTO_D after delete on AP_TOTO referencing old row as oldrow for each row
begin atomic
DECLARE V CHAR(10);
set V = 'V';
end;
