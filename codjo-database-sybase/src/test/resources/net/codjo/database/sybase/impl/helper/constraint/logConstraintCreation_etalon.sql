if exists (select 1 from sysobjects
           where name = 'FK_MERETOTO_TOTO'
           and type = 'RI')
    print 'Foreign key AP_TOTO.FK_MERETOTO_TOTO created'
go
