
    alter table NWAP_CITY 
        drop constraint FK76B31992959BBC6C;

    alter table NWAP_CITY 
        drop constraint FK76B31992F1A7FC2D;

    drop table MP_USER if exists;

    drop table NWAP_CITY if exists;

    drop table NWAP_JOS_NESTO if exists;

    drop table NWAP_STATE if exists;

    create table MP_USER (
        ID bigint generated by default as identity unique,
        password varchar(255) not null,
        username varchar(255) not null,
        primary key (ID)
    );

    create table NWAP_CITY (
        ID bigint generated by default as identity unique,
        CIT_NAME varchar(255) not null,
        city_mesto bigint,
        city_state bigint,
        primary key (ID)
    );

    create table NWAP_JOS_NESTO (
        ID bigint generated by default as identity unique,
        JOS_IMA varchar(255) not null,
        primary key (ID)
    );

    create table NWAP_STATE (
        ID bigint generated by default as identity unique,
        STA_NAME varchar(255) not null,
        primary key (ID)
    );

    alter table NWAP_CITY 
        add constraint FK76B31992959BBC6C 
        foreign key (city_mesto) 
        references NWAP_JOS_NESTO;

    alter table NWAP_CITY 
        add constraint FK76B31992F1A7FC2D 
        foreign key (city_state) 
        references NWAP_STATE;