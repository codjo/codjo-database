if exists (select 1 from sysobjects
           where id = object_id('TR_AP_TOTO_IU')
           and type = 'TR')
begin
    drop trigger TR_AP_TOTO_IU
    print 'Trigger TR_AP_TOTO_IU dropped'
end
go

/*  Insert, Update trigger "TR_AP_TOTO_IU" for table "AP_TOTO"  */
create trigger TR_AP_TOTO_IU on AP_TOTO for insert, update as
begin
    declare
       @numrows  int,
       @errno    int,
       @errmsg   varchar(255)

    select  @numrows = @@rowcount
    if @numrows = 0
       return

    /*  Parent "AP_PARENT_TOTO" must exist when inserting a child in "AP_TOTO"  */
    if update(ID)
    begin
       if (select count(*)
           from   AP_PARENT_TOTO t1, inserted t2
           where  t1.PARENT_ID = t2.ID) != @numrows
          begin
             select @errno  = 30002,
                    @errmsg = 'Parent does not exist in "AP_PARENT_TOTO". Cannot create child in "AP_TOTO".'
             goto error
          end
    end

    return

/*  Errors handling  */
error:
    raiserror @errno @errmsg
    rollback  transaction
end
go

if exists (select 1 from sysobjects
           where id = object_id('TR_AP_TOTO_IU')
           and type = 'TR')
    print 'Trigger TR_AP_TOTO_IU created'
go