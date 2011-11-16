create or replace trigger TR_AP_TOTO_IU
    after insert or update on AP_TOTO for each row

declare
    parentCount number := 0;

begin

    select count(1) into parentCount
    from   AP_PARENT_TOTO t1
    where  t1.PARENT_ID = :new.ID;

  if (parentCount = 0) then
    raise_application_error(-20000,
           'Parent does not exist in "AP_PARENT_TOTO". Cannot create child in "AP_TOTO".');
  end if;

end TR_AP_TOTO_IU;
/

prompt Trigger TR_AP_TOTO_IU created

sho err