drop procedure if exists foo_trigger_deleter;

delimiter $$
create procedure foo_trigger_deleter()
begin

    declare l_count integer;

    select 1 into l_count
    from information_schema.TRIGGERS
    where TRIGGER_NAME = 'TR_AP_TOTO_D';

    if (l_count > 0) then
        drop trigger TR_AP_TOTO_D;
        select 'Trigger TR_AP_TOTO_D dropped' as '';
    end if;

end $$
delimiter ;

call foo_trigger_deleter();
drop procedure foo_trigger_deleter;


delimiter $$
create trigger TR_AP_TOTO_D
    after delete on AP_TOTO for each row
begin

    /*  Delete all children in "AP_CHILD1_TOTO"  */
    delete
    from   AP_CHILD1_TOTO
    where  CHILD_ID = OLD.ID;

    /*  Delete all children in "AP_CHILD2_TOTO"  */
    delete
    from   AP_CHILD2_TOTO
    where  CHILD_ID1 = OLD.ID1
           and CHILD_ID2 = OLD.ID2;

// CUSTOM SQL CODE

end $$
delimiter ;


select 'Trigger TR_AP_TOTO_D created' as ''
from information_schema.TRIGGERS
where TRIGGER_NAME = 'TR_AP_TOTO_D';
