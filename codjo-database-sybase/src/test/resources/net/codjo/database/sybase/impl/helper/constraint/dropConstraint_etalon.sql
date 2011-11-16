if exists (select 1 from sysobjects
           where name = 'FK_MERETOTO_TOTO' and type = 'RI')
begin
    alter table AP_TOTO drop constraint FK_MERETOTO_TOTO
    print 'Foreign key AP_TOTO.FK_MERETOTO_TOTO dropped'
end
go
