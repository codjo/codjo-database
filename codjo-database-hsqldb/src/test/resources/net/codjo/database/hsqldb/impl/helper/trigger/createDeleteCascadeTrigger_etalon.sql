drop trigger TR_AP_TOTO_D if exists;
/*  Delete trigger "TR_AP_TOTO_D" for table "AP_TOTO"  */
create trigger TR_AP_TOTO_D after delete on AP_TOTO referencing old row as oldrow for each row
begin atomic
    /*  Delete all children in "AP_CHILD1_TOTO"  */
    delete from   AP_CHILD1_TOTO t2    where  t2.CHILD_ID = oldrow.ID;

    /*  Delete all children in "AP_CHILD2_TOTO"  */
    delete from   AP_CHILD2_TOTO t2    where  t2.CHILD_ID1 = oldrow.ID1
           and t2.CHILD_ID2 = oldrow.ID2;




end;
