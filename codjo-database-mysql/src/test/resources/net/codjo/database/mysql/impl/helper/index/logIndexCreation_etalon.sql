select 'Index AP_TOTO.X1_AP_TOTO created' as ''
from information_schema.STATISTICS
where INDEX_NAME='X1_AP_TOTO' and TABLE_NAME='AP_TOTO'
group by INDEX_NAME;
