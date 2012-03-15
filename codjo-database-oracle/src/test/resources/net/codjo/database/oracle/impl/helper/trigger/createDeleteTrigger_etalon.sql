create or replace trigger TR_AP_TOTO_D
    after delete on AP_TOTO for each row
declare
    v_errno    NUMBER(12);
    v_errmsg   VARCHAR2(255);

begin

// CUSTOM SQL CODE

return;
   /*  Errors handling  */
   <<error>>
   raise_application_error( -20002, v_errno|| ':' ||v_errmsg );
   ROLLBACK;
end;
/

prompt Trigger TR_AP_TOTO_D created

sho err