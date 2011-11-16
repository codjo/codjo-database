if exists (select 1 from sysobjects
           where id = object_id('AP_GROUP_PERSON') and type = 'U')
begin
    drop table AP_GROUP_PERSON
    print 'Table AP_GROUP_PERSON dropped'
end
go
