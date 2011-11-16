if exists (select 1 from sysobjects
           where id = object_id('AP_GROUP_PERSON') and type = 'U')
    print 'Table AP_GROUP_PERSON created'
go
