if exists (select 1 from sysobjects o, sysindexes i
           where o.id = i.id and i.name = 'X1_INDEX_WITH_VERY_VERY_LONG_N'
           and o.name = 'AP_TOTO' and o.type = 'U')
begin
    drop index AP_TOTO.X1_INDEX_WITH_VERY_VERY_LONG_N
    print 'Index AP_TOTO.X1_INDEX_WITH_VERY_VERY_LONG_N dropped'
end
go
