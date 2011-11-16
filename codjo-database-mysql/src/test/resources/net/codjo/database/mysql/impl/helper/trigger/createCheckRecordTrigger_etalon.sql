drop procedure if exists foo_trigger_deleter;

delimiter $$
create procedure foo_trigger_deleter()
begin

    declare l_count integer;

    select 1 into l_count
    from information_schema.TRIGGERS
    where TRIGGER_NAME = 'TR_AP_TOTO_IU_I';

    if (l_count > 0) then
        drop trigger TR_AP_TOTO_IU_I;
        select 'Trigger TR_AP_TOTO_IU_I dropped' as '';
    end if;

end $$
delimiter ;

call foo_trigger_deleter();
drop procedure foo_trigger_deleter;


delimiter $$
create trigger TR_AP_TOTO_IU_I
    after insert on AP_TOTO for each row
begin

    declare parentCount integer;

    select count(1) into parentCount
    from   AP_PARENT_TOTO t1
    where  t1.PARENT_ID = new.ID;

    if (parentCount = 0) then
        call raise_error('Parent does not exist in "AP_PARENT_TOTO". Cannot create child in "AP_TOTO".');
    end if;

end $$
delimiter ;


select 'Trigger TR_AP_TOTO_IU_I created' as ''
from information_schema.TRIGGERS
where TRIGGER_NAME = 'TR_AP_TOTO_IU_I';


drop procedure if exists foo_trigger_deleter;

delimiter $$
create procedure foo_trigger_deleter()
begin

    declare l_count integer;

    select 1 into l_count
    from information_schema.TRIGGERS
    where TRIGGER_NAME = 'TR_AP_TOTO_IU_U';

    if (l_count > 0) then
        drop trigger TR_AP_TOTO_IU_U;
        select 'Trigger TR_AP_TOTO_IU_U dropped' as '';
    end if;

end $$
delimiter ;

call foo_trigger_deleter();
drop procedure foo_trigger_deleter;


delimiter $$
create trigger TR_AP_TOTO_IU_U
    after update on AP_TOTO for each row
begin

    declare parentCount integer;

    select count(1) into parentCount
    from   AP_PARENT_TOTO t1
    where  t1.PARENT_ID = new.ID;

    if (parentCount = 0) then
        call raise_error('Parent does not exist in "AP_PARENT_TOTO". Cannot create child in "AP_TOTO".');
    end if;

end $$
delimiter ;


select 'Trigger TR_AP_TOTO_IU_U created' as ''
from information_schema.TRIGGERS
where TRIGGER_NAME = 'TR_AP_TOTO_IU_U';
