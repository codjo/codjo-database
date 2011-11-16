alter table AP_MERETOTO add (constraint pk_AP_MERETOTO primary key (ISIN_CODE, AUTOMATIC_UPDATE));

alter table AP_TOTO add constraint FK_MERETOTO_TOTO foreign key (PORTFOLIO_CODE, AUTOMATIC_UPDATE) references AP_MERETOTO (ISIN_CODE, AUTOMATIC_UPDATE);
