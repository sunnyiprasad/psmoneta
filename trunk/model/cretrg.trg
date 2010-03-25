/*==============================================================*/
/* DBMS name:      ORACLE Version 10g                           */
/* Created on:     17.07.2008 23:34:56                          */
/*==============================================================*/



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


--  Before insert trigger "TRIGGER_7" for table "T_ABONENTPAYMENT"
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


--  Before insert trigger ""Trigger_5"" for table "T_DEALER"
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


--  Before insert trigger ""Trigger_6"" for table "T_DEALERPAYMENT"
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


--  Before insert trigger ""Trigger_9"" for table "T_DEALERUSER"
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


--  Before insert trigger ""Trigger_8"" for table "T_KEY"
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


--  Before insert trigger ""Trigger_4"" for table "T_POINT"
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


--  Before insert trigger ""Trigger_1"" for table "T_POINTPAYMENT"
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


--  Before insert trigger ""Trigger_3"" for table "T_POINTUSER"
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


--  Before insert trigger ""Trigger_2"" for table "T_PROVIDER"
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

