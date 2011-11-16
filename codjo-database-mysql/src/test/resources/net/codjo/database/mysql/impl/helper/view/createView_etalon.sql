drop view if exists VU_AP_TOTO;

create view VU_AP_TOTO
    as
select * from AP_TOTO;

select 'View VU_AP_TOTO created' as ''
from information_schema.TABLES
where TABLE_NAME='VU_AP_TOTO' and TABLE_TYPE='VIEW';
