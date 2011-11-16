create or replace trigger TR_AP_TOTO_D
    after delete on AP_TOTO for each row
begin

    /*  Delete all children in "AP_CHILD1_TOTO"  */
    delete
    from   AP_CHILD1_TOTO
    where  CHILD_ID = :old.ID;

    /*  Delete all children in "AP_CHILD2_TOTO"  */
    delete
    from   AP_CHILD2_TOTO
    where  CHILD_ID1 = :old.ID1
           and CHILD_ID2 = :old.ID2;

// CUSTOM SQL CODE

end TR_AP_TOTO_D;
/

prompt Trigger TR_AP_TOTO_D created

sho err