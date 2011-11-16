if exists (select 1 from sysobjects o, sysindexes i
           where o.id = i.id and i.name = 'X1_AP_TOTO'
           and o.name = 'AP_TOTO' and o.type = 'U')
begin
    drop index AP_TOTO.X1_AP_TOTO
    print 'Index AP_TOTO.X1_AP_TOTO dropped'
end
go
