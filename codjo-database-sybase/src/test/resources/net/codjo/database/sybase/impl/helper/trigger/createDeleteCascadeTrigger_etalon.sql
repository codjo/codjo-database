if exists (select 1 from sysobjects
           where id = object_id('TR_AP_TOTO_D')
           and type = 'TR')
begin
    drop trigger TR_AP_TOTO_D
    print 'Trigger TR_AP_TOTO_D dropped'
end
go

/*  Delete trigger "TR_AP_TOTO_D" for table "AP_TOTO"  */
create trigger TR_AP_TOTO_D on AP_TOTO for delete as
begin
    declare
       @numrows  int,
       @errno    int,
       @errmsg   varchar(255)

    select  @numrows = @@rowcount
    if @numrows = 0
       return

    /*  Delete all children in "AP_CHILD1_TOTO"  */
    delete AP_CHILD1_TOTO
    from   AP_CHILD1_TOTO t2, deleted t1
    where  t2.CHILD_ID = t1.ID

    /*  Delete all children in "AP_CHILD2_TOTO"  */
    delete AP_CHILD2_TOTO
    from   AP_CHILD2_TOTO t2, deleted t1
    where  t2.CHILD_ID1 = t1.ID1
           and t2.CHILD_ID2 = t1.ID2

// CUSTOM SQL CODE

    return

/*  Errors handling  */
error:
    raiserror @errno @errmsg
    rollback  transaction
end
go

if exists (select 1 from sysobjects
           where id = object_id('TR_AP_TOTO_D')
           and type = 'TR')
    print 'Trigger TR_AP_TOTO_D created'
go