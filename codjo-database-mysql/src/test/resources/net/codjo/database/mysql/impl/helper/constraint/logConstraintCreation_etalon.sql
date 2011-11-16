select 'Foreign key AP_TOTO.FK_MERETOTO_TOTO created' as ''
from information_schema.TABLE_CONSTRAINTS
where CONSTRAINT_NAME='FK_MERETOTO_TOTO' and CONSTRAINT_TYPE='FOREIGN KEY';
