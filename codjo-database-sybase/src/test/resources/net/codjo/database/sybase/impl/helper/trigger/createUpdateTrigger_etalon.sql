if exists (select 1 from sysobjects
           where id = object_id('TR_AP_TOTO_U')
           and type = 'TR')
begin
    drop trigger TR_AP_TOTO_U
    print 'Trigger TR_AP_TOTO_U dropped'
end
go

/*  Update trigger "TR_AP_TOTO_U" for table "AP_TOTO"  */
create trigger TR_AP_TOTO_U on AP_TOTO for update as
begin
    declare
       @numrows  int,
       @errno    int,
       @errmsg   varchar(255)

    select  @numrows = @@rowcount
    if @numrows = 0
       return

// CUSTOM SQL CODE

    return

/*  Errors handling  */
error:
    raiserror @errno @errmsg
    rollback  transaction
end
go

if exists (select 1 from sysobjects
           where id = object_id('TR_AP_TOTO_U')
           and type = 'TR')
    print 'Trigger TR_AP_TOTO_U created'
go