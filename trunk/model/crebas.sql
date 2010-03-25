/*==============================================================*/
/* DBMS name:      ORACLE Version 10g                           */
/* Created on:     23.05.2009 18:27:40                          */
/*==============================================================*/



-- Type package declaration
create or replace package PDTypes  
as
    TYPE ref_cursor IS REF CURSOR;
end;

-- Integrity package declaration
create or replace package IntegrityPackage AS
 procedure InitNestLevel;
 function GetNestLevel return number;
 procedure NextNestLevel;
 procedure PreviousNestLevel;
 end IntegrityPackage;
/

-- Integrity package definition
create or replace package body IntegrityPackage AS
 NestLevel number;

-- Procedure to initialize the trigger nest level
 procedure InitNestLevel is
 begin
 NestLevel := 0;
 end;


-- Function to return the trigger nest level
 function GetNestLevel return number is
 begin
 if NestLevel is null then
     NestLevel := 0;
 end if;
 return(NestLevel);
 end;

-- Procedure to increase the trigger nest level
 procedure NextNestLevel is
 begin
 if NestLevel is null then
     NestLevel := 0;
 end if;
 NestLevel := NestLevel + 1;
 end;

-- Procedure to decrease the trigger nest level
 procedure PreviousNestLevel is
 begin
 NestLevel := NestLevel - 1;
 end;

 end IntegrityPackage;
/


drop trigger TRIGGER_7
/

drop trigger "Trigger_5"
/

drop trigger "Trigger_6"
/

drop trigger "Trigger_9"
/

drop trigger "Trigger_8"
/

drop trigger "Trigger_4"
/

drop trigger "Trigger_1"
/

drop trigger "Trigger_3"
/

drop trigger "Trigger_2"
/

alter table T_ABONENTPAYMENT
   drop constraint FK_T_ABONEN_REFERENCE_T_PROVID
/

alter table T_ABONENTPAYMENT
   drop constraint FK_T_ABONEN_REFERENCE_T_POINTU
/

alter table T_ABONENTPAYMENT
   drop constraint FK_T_ABONEN_REFERENCE_T_DEALER
/

alter table T_ABONENTPAYMENT
   drop constraint FK_T_ABONEN_REFERENCE_T_POINT
/

alter table T_DEALERPAYMENT
   drop constraint FK_DEALERPA_REFERENCE_DEALER1
/

alter table T_DEALERPAYMENT
   drop constraint FK_DEALERPA_REFERENCE_DEALER2
/

alter table T_DEALERUSER
   drop constraint FK_T_DEALER_REFERENCE_T_DEALER
/

alter table T_DEALERUSER
   drop constraint FK_T_DEALER_REFERENCE_T_KEY
/

alter table T_POINT
   drop constraint FK_T_POINT_REFERENCE_T_KEY
/

alter table T_POINT
   drop constraint FK_T_POINT_REFERENCE_T_DEALER
/

alter table T_POINTPAYMENT
   drop constraint FK_T_POINTP_REFERENCE_T_DEALER
/

alter table T_POINTPAYMENT
   drop constraint FK_T_POINTP_REFERENCE_T_POINT
/

alter table T_POINTUSER
   drop constraint FK_T_POINTU_REFERENCE_T_KEY
/

alter table T_POINTUSER
   drop constraint FK_T_POINTU_REFERENCE_T_POINT
/

drop table T_ABONENTPAYMENT cascade constraints
/

drop table T_DEALER cascade constraints
/

drop table T_DEALERPAYMENT cascade constraints
/

drop table T_DEALERUSER cascade constraints
/

drop table T_KEY cascade constraints
/

drop table T_POINT cascade constraints
/

drop table T_POINTPAYMENT cascade constraints
/

drop index "Index_1"
/

drop table T_POINTUSER cascade constraints
/

drop table T_PROVIDER cascade constraints
/

drop sequence APSEQ
/

drop sequence DEALSEQ
/

drop sequence DPSEQ
/

drop sequence DUSEQ
/

drop sequence KEYSEQ
/

drop sequence POINTSEQ
/

drop sequence PPSEQ
/

drop sequence PROVSEQ
/

drop sequence USSEQ
/

create sequence APSEQ
/

create sequence DEALSEQ
/

create sequence DPSEQ
/

create sequence DUSEQ
/

create sequence KEYSEQ
/

create sequence POINTSEQ
/

create sequence PPSEQ
/

create sequence PROVSEQ
/

create sequence USSEQ
/

/*==============================================================*/
/* Table: T_ABONENTPAYMENT                                      */
/*==============================================================*/
create table T_ABONENTPAYMENT  (
   ID                   NUMBER                          not null,
   ACCOUNT              VARCHAR2(255),
   NUMBER1              VARCHAR2(255),
   AMOUNT               NUMBER(36,2),
   AMOUNT_ALL           NUMBER(36,2),
   COMMENT1             VARCHAR2(255),
   DATE1                TIMESTAMP,
   DEALERID             NUMBER,
   ENDDATE              TIMESTAMP,
   STARTDATE            TIMESTAMP,
   POINTID              NUMBER,
   PROVIDERID           NUMBER,
   SESSION1             VARCHAR2(255),
   USERID               NUMBER,
   STATUS               NUMBER(2),
   ERRMSG               VARCHAR2(255),
   AUTHCODE             VARCHAR2(50),
   TRANSID              VARCHAR2(50),
   constraint PK_T_ABONENTPAYMENT primary key (ID)
)
/

/*==============================================================*/
/* Table: T_DEALER                                              */
/*==============================================================*/
create table T_DEALER  (
   ID                   NUMBER                          not null,
   NAME                 VARCHAR2(255),
   EMAIL                VARCHAR2(255),
   PHONE                VARCHAR2(255),
   OWNERDEALERID        NUMBER,
   BALANCE              NUMBER(36,2)                   default 0 not null,
   constraint PK_T_DEALER primary key (ID)
)
/

/*==============================================================*/
/* Table: T_DEALERPAYMENT                                       */
/*==============================================================*/
create table T_DEALERPAYMENT  (
   ID                   NUMBER                          not null,
   FROMDEALERID         NUMBER,
   TODEALERID           NUMBER,
   DATE1                TIMESTAMP,
   AMOUNT               NUMBER(36,2),
   constraint PK_T_DEALERPAYMENT primary key (ID)
)
/

/*==============================================================*/
/* Table: T_DEALERUSER                                          */
/*==============================================================*/
create table T_DEALERUSER  (
   ID                   NUMBER                          not null,
   NAME                 VARCHAR2(255),
   PUBKEYID             NUMBER,
   DEALERID             NUMBER,
   constraint PK_T_DEALERUSER primary key (ID)
)
/

/*==============================================================*/
/* Table: T_KEY                                                 */
/*==============================================================*/
create table T_KEY  (
   ID                   NUMBER                          not null,
   DATA                 BLOB                            not null,
   constraint PK_T_KEY primary key (ID)
)
/

/*==============================================================*/
/* Table: T_POINT                                               */
/*==============================================================*/
create table T_POINT  (
   ID                   NUMBER                          not null,
   DEALERID             NUMBER                          not null,
   BALANCE              NUMBER(36,2),
   PUBKEYID             NUMBER,
   constraint PK_T_POINT primary key (ID)
)
/

/*==============================================================*/
/* Table: T_POINTPAYMENT                                        */
/*==============================================================*/
create table T_POINTPAYMENT  (
   ID                   NUMBER                          not null,
   DATE1                TIMESTAMP,
   DEALERID             NUMBER,
   POINTID              NUMBER,
   SUMMA                NUMBER(36,2),
   constraint PK_T_POINTPAYMENT primary key (ID)
)
/

/*==============================================================*/
/* Table: T_POINTUSER                                           */
/*==============================================================*/
create table T_POINTUSER  (
   ID                   NUMBER                          not null,
   NAME                 VARCHAR2(255)                   not null,
   POINTID              NUMBER                          not null,
   BALANCE              NUMBER(36,2),
   PUBKEYID             NUMBER,
   constraint PK_T_POINTUSER primary key (ID)
)
/

/*==============================================================*/
/* Index: "Index_1"                                             */
/*==============================================================*/
create index "Index_1" on T_POINTUSER (
   POINTID ASC
)
/

/*==============================================================*/
/* Table: T_PROVIDER                                            */
/*==============================================================*/
create table T_PROVIDER  (
   ID                   NUMBER                          not null,
   NAME                 VARCHAR2(50)                    not null,
   CHECKURL             VARCHAR2(255)                   not null,
   PAYMENTURL           VARCHAR2(255)                   not null,
   GETSTATUSURL         VARCHAR2(255)                   not null,
   CLASSNAME            VARCHAR2(255)                   not null,
   constraint PK_T_PROVIDER primary key (ID)
)
/

alter table T_ABONENTPAYMENT
   add constraint FK_T_ABONEN_REFERENCE_T_PROVID foreign key (PROVIDERID)
      references T_PROVIDER (ID)
/

alter table T_ABONENTPAYMENT
   add constraint FK_T_ABONEN_REFERENCE_T_POINTU foreign key (USERID)
      references T_POINTUSER (ID)
/

alter table T_ABONENTPAYMENT
   add constraint FK_T_ABONEN_REFERENCE_T_DEALER foreign key (DEALERID)
      references T_DEALER (ID)
/

alter table T_ABONENTPAYMENT
   add constraint FK_T_ABONEN_REFERENCE_T_POINT foreign key (POINTID)
      references T_POINT (ID)
/

alter table T_DEALERPAYMENT
   add constraint FK_DEALERPA_REFERENCE_DEALER1 foreign key (FROMDEALERID)
      references T_DEALER (ID)
/

alter table T_DEALERPAYMENT
   add constraint FK_DEALERPA_REFERENCE_DEALER2 foreign key (TODEALERID)
      references T_DEALER (ID)
/

alter table T_DEALERUSER
   add constraint FK_T_DEALER_REFERENCE_T_DEALER foreign key (DEALERID)
      references T_DEALER (ID)
/

alter table T_DEALERUSER
   add constraint FK_T_DEALER_REFERENCE_T_KEY foreign key (PUBKEYID)
      references T_KEY (ID)
/

alter table T_POINT
   add constraint FK_T_POINT_REFERENCE_T_KEY foreign key (PUBKEYID)
      references T_KEY (ID)
/

alter table T_POINT
   add constraint FK_T_POINT_REFERENCE_T_DEALER foreign key (DEALERID)
      references T_DEALER (ID)
/

alter table T_POINTPAYMENT
   add constraint FK_T_POINTP_REFERENCE_T_DEALER foreign key (DEALERID)
      references T_DEALER (ID)
/

alter table T_POINTPAYMENT
   add constraint FK_T_POINTP_REFERENCE_T_POINT foreign key (POINTID)
      references T_POINT (ID)
/

alter table T_POINTUSER
   add constraint FK_T_POINTU_REFERENCE_T_KEY foreign key (PUBKEYID)
      references T_KEY (ID)
/

alter table T_POINTUSER
   add constraint FK_T_POINTU_REFERENCE_T_POINT foreign key (POINTID)
      references T_POINT (ID)
/


create trigger TRIGGER_7 before insert
on T_ABONENTPAYMENT for each row
declare
    integrity_error  exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
    --  Column "ID" uses sequence APSEQ
    select APSEQ.NEXTVAL INTO :new.ID from dual;

--  Errors handling
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/


create trigger "Trigger_5" before insert
on T_DEALER for each row
declare
    integrity_error  exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
    --  Column "ID" uses sequence DEALSEQ
    select DEALSEQ.NEXTVAL INTO :new.ID from dual;

--  Errors handling
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/


create trigger "Trigger_6" before insert
on T_DEALERPAYMENT for each row
declare
    integrity_error  exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
    --  Column "ID" uses sequence DPSEQ
    select DPSEQ.NEXTVAL INTO :new.ID from dual;

--  Errors handling
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/


create trigger "Trigger_9" before insert
on T_DEALERUSER for each row
declare
    integrity_error  exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
    --  Column "ID" uses sequence DUSEQ
    select DUSEQ.NEXTVAL INTO :new.ID from dual;

--  Errors handling
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/


create trigger "Trigger_8" before insert
on T_KEY for each row
declare
    integrity_error  exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
    --  Column "ID" uses sequence KEYSEQ
    select KEYSEQ.NEXTVAL INTO :new.ID from dual;

--  Errors handling
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/


create trigger "Trigger_4" before insert
on T_POINT for each row
declare
    integrity_error  exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
    --  Column "ID" uses sequence POINTSEQ
    select POINTSEQ.NEXTVAL INTO :new.ID from dual;

--  Errors handling
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/


create trigger "Trigger_1" before insert
on T_POINTPAYMENT for each row
declare
    integrity_error  exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
    --  Column "ID" uses sequence PPSEQ
    select PPSEQ.NEXTVAL INTO :new.ID from dual;

--  Errors handling
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/


create trigger "Trigger_3" before insert
on T_POINTUSER for each row
declare
    integrity_error  exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
    --  Column "ID" uses sequence USSEQ
    select USSEQ.NEXTVAL INTO :new.ID from dual;

--  Errors handling
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/


create trigger "Trigger_2" before insert
on T_PROVIDER for each row
declare
    integrity_error  exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
    --  Column "ID" uses sequence PROVSEQ
    select PROVSEQ.NEXTVAL INTO :new.ID from dual;

--  Errors handling
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/

