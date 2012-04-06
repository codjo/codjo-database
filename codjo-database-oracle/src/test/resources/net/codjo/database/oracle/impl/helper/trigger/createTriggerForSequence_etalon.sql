create or replace trigger TR_AP_TOTO_SEQ
    before insert on AP_TOTO for each row
declare
    v_errno    NUMBER(12);
    v_errmsg   VARCHAR2(255);

begin
if :new.QUARANTINE_ID is null then
    select SEQ_AP_TOTO.nextval into :new.QUARANTINE_ID from dual;
end if;
return;
   /*  Errors handling  */
   <<error>>
   raise_application_error( -20002, v_errno|| ':' ||v_errmsg );
   ROLLBACK;
end;
/

prompt Trigger TR_AP_TOTO_SEQ created

sho err