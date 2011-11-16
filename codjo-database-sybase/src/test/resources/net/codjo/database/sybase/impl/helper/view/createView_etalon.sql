if exists (select 1 from sysobjects
           where type = 'V' and name = 'VU_AP_TOTO')
begin
    drop view VU_AP_TOTO
    print 'View VU_AP_TOTO dropped'
end
go

create view VU_AP_TOTO
    as
select * from AP_TOTO
go

if exists (select 1 from sysobjects
           where type = 'V' and name = 'VU_AP_TOTO')
    print 'View VU_AP_TOTO created'
go
