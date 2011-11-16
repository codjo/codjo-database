if exists (select 1 from sysindexes
           where id = object_id('AP_TOTO')
           and identitygap = 10000)
    print 'Identity Gap = 10000 created'
go