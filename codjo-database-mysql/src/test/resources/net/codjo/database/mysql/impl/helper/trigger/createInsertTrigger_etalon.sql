drop procedure if exists foo_trigger_deleter;

delimiter $$
create procedure foo_trigger_deleter()
begin

    declare l_count integer;

    select 1 into l_count
    from information_schema.TRIGGERS
    where TRIGGER_NAME = 'TR_AP_TOTO_I';

    if (l_count > 0) then
        drop trigger TR_AP_TOTO_I;
        select 'Trigger TR_AP_TOTO_I dropped' as '';
    end if;

end $$
delimiter ;

call foo_trigger_deleter();
drop procedure foo_trigger_deleter;


delimiter $$
create trigger TR_AP_TOTO_I
    after insert on AP_TOTO for each row
begin

// CUSTOM SQL CODE

end $$
delimiter ;


select 'Trigger TR_AP_TOTO_I created' as ''
from information_schema.TRIGGERS
where TRIGGER_NAME = 'TR_AP_TOTO_I';
