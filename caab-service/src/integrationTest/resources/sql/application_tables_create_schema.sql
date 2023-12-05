CREATE SEQUENCE XXCCMS_GENERATED_ID_S INCREMENT BY 1 MINVALUE 1 MAXVALUE 9999999999999999999999999999 NOCYCLE CACHE 20 NOORDER;

CREATE TABLE XXCCMS_ADDRESS (
        "ID" NUMBER(19,0) NOT NULL ENABLE,
        "NO_FIXED_ABODE" NUMBER(1,0),
        "POSTCODE" VARCHAR2(15 CHAR),
        "HOUSE_NAME_NUMBER" VARCHAR2(50 CHAR),
        "ADDRESS_LINE1" VARCHAR2(35 CHAR),
        "ADDRESS_LINE2" VARCHAR2(35 CHAR),
        "CITY" VARCHAR2(35 CHAR),
        "COUNTY" VARCHAR2(35 CHAR),
        "COUNTRY" VARCHAR2(3 CHAR),
        "CARE_OF" VARCHAR2(35 CHAR),
        "PREFERRED_ADDRESS" VARCHAR2(50 CHAR),
        "CREATED" TIMESTAMP (6),
        "CREATED_BY" VARCHAR2(50 CHAR),
        "MODIFIED" TIMESTAMP (6),
        "MODIFIED_BY" VARCHAR2(50 CHAR)
);

CREATE UNIQUE INDEX "SYS_C0010978" ON "XXCCMS_ADDRESS" ("ID");

create table XXCCMS_COST_ENTRY (
       ID number(19,0) not null,
       EBS_ID varchar2(15 char),
       RESOURCE_NAME varchar2(300 char),
       LSC_RESOURCE_ID varchar2(15 char),
       COST_CATEGORY varchar2(50 char),
       REQUESTED_COSTS number(10,2),
       FK_COST_STRUCTURE number(19,0) not null,
       CREATED timestamp,
       CREATED_BY varchar2(50 char),
       MODIFIED timestamp,
       MODIFIED_BY varchar2(50 char),
       NEW_ENTRY NUMBER(1),
       primary key (ID)
);



CREATE TABLE XXCCMS_COST_STRUCTURE (
       "ID" NUMBER(19,0) NOT NULL ENABLE,
       "DEFAULT_COST_LIMITATION" NUMBER(10,2),
       "GRANTED_COST_LIMITATION" NUMBER(10,2),
       "REQUESTED_COST_LIMITATION" NUMBER(10,2),
       "CREATED" TIMESTAMP (6),
       "CREATED_BY" VARCHAR2(50 CHAR),
       "MODIFIED" TIMESTAMP (6),
       "MODIFIED_BY" VARCHAR2(50 CHAR)
);

CREATE UNIQUE INDEX "SYS_C0011003" ON "XXCCMS_COST_STRUCTURE" ("ID");

CREATE INDEX XXCCMS_COST_ENTRY_I1 on XXCCMS_COST_ENTRY (FK_COST_STRUCTURE);

CREATE TABLE XXCCMS_LINKED_CASE (
        ID number(19,0) not null,
        LSC_CASE_REFERENCE varchar2(35 char),
        PROVIDER_CASE_REFERENCE varchar2(35 char),
        FEE_EARNER varchar2(300 char),
        CLIENT_FIRST_NAME varchar2(35 char),
        CLIENT_SURNAME varchar2(35 char),
        CLIENT_REFERENCE varchar2(30 char),
        CATEGORY_OF_LAW varchar2(50 char) not null,
        RELATION_TO_LINKED_CASE varchar2(50 char),
        STATUS varchar2(50 char),
        FK_APPLICATION number(19,0) not null,
        CREATED timestamp,
        CREATED_BY varchar2(50 char),
        MODIFIED timestamp,
        MODIFIED_BY varchar2(50 char),
        primary key (ID)
);

CREATE TABLE XXCCMS_APPLICATION (
        "ID" NUMBER(19,0) NOT NULL ENABLE,
        "LSC_CASE_REFERENCE" VARCHAR2(35 CHAR) NOT NULL ENABLE,
        "PROVIDER_ID" VARCHAR2(19 CHAR) NOT NULL ENABLE,
        "PROVIDER_CASE_REFERENCE" VARCHAR2(35 CHAR),
        "PROVIDER_DISPLAY_VALUE" VARCHAR2(300 CHAR),
        "OFFICE_ID" NUMBER(19,0),
        "OFFICE_DISPLAY_VALUE" VARCHAR2(360 CHAR),
        "SUPERVISOR" VARCHAR2(19 CHAR),
        "SUPERVISOR_DISPLAY_VALUE" VARCHAR2(300 CHAR),
        "FEE_EARNER" VARCHAR2(19 CHAR),
        "FEE_EARNER_DISPLAY_VALUE" VARCHAR2(300 CHAR),
        "PROVIDER_CONTACT" VARCHAR2(19 CHAR),
        "PROVIDER_CONTACT_DISPLAY_VALUE" VARCHAR2(300 CHAR),
        "CATEGORY_OF_LAW" VARCHAR2(50 CHAR) NOT NULL ENABLE,
        "CATEGORY_OF_LAW_DISPLAY_VALUE" VARCHAR2(50 CHAR),
        "RELATION_TO_LINKED_CASE" VARCHAR2(50 CHAR),
        "OPPONENT_APPLIED_FOR_FUNDING" NUMBER(1,0),
        "DISPLAY_STATUS" VARCHAR2(50 CHAR),
        "ACTUAL_STATUS" VARCHAR2(50 CHAR),
        "CLIENT_FIRSTNAME" VARCHAR2(50 CHAR),
        "CLIENT_SURNAME" VARCHAR2(50 CHAR),
        "CLIENT_REFERENCE" VARCHAR2(30 CHAR) NOT NULL ENABLE,
        "AMENDMENT" NUMBER(1,0),
        "MEANS_ASSESSMENT_AMENDED" NUMBER(1,0),
        "MERITS_ASSESSMENT_AMENDED" NUMBER(1,0),
        "COST_LIMIT_CHANGED" NUMBER(1,0),
        "COST_LIMIT_AT_TIME_OF_MERITS" NUMBER(10,2),
        "SUBMISSION_STATUS" VARCHAR2(30 CHAR),
        "POLL_TRANSACTION_ID" VARCHAR2(30 CHAR),
        "APPLICATION_TYPE" VARCHAR2(30 CHAR),
        "APPLICATION_TYPE_DISPLAY_VALUE" VARCHAR2(35 CHAR),
        "DEVOLVED_POWERS_USED" VARCHAR2(5 CHAR),
        "DATE_DEVOLVED_POWERS_USED" DATE,
        "DEVOLVED_POWERS_CONTRACT_FLAG" VARCHAR2(30 CHAR),
        "MERITS_REASSESSMENT_REQD_IND" NUMBER(1,0),
        "LAR_SCOPE_FLAG" VARCHAR2(5 CHAR),
        "LEAD_PROCEEDING_CHANGED" NUMBER(1,0),
        "FK_COST_STRUCTURE" NUMBER(19,0),
        "FK_CORRESPONDENCE_ADDRESS" NUMBER(19,0),
        "CREATED" TIMESTAMP (6),
        "CREATED_BY" VARCHAR2(50 CHAR),
        "MODIFIED" TIMESTAMP (6),
        "MODIFIED_BY" VARCHAR2(50 CHAR)
);

create table XXCCMS_PRIOR_AUTHORITY (
        "ID" NUMBER(19,0) NOT NULL ENABLE,
        EBS_ID varchar(50),
        TYPE varchar(50),
        TYPE_DISPLAY_VALUE varchar(50),
        SUMMARY varchar(35),
        JUSTIFICATION varchar(200),
        VALUE_REQUIRED_FLAG varchar(5),
        AMOUNT_REQUESTED numeric(10,2),
        STATUS varchar(50),
        FK_APPLICATION NUMBER(19,0) not null,
        CREATED timestamp,
        CREATED_BY varchar(50),
        MODIFIED timestamp,
        MODIFIED_BY varchar(50),
        primary key (ID)
);

create table XXCCMS_PROCEEDING (
       "ID" NUMBER(19,0) NOT NULL ENABLE,
       EBS_ID varchar(50),
       MATTER_TYPE varchar(50),
       MATTER_TYPE_DISPLAY_VALUE varchar(50),
       PROCEEDING_TYPE varchar(50),
       PROCEEDING_TYPE_DISPLAY_VALUE varchar(542),
       DESCRIPTION varchar(542),
       LEVEL_OF_SERVICE varchar(50),
       LEVEL_OF_SERVICE_DISPLAY_VALUE varchar(50),
       CLIENT_INVOLVEMENT varchar(50),
       CLIENT_DISPLAY_VALUE varchar(50),
       COST_LIMITATION numeric(10,2),
       STATUS varchar(50),
       DISPLAY_STATUS varchar(50),
       TYPE_OF_ORDER varchar(50),
       DATE_GRANTED date,
       EDITED NUMBER(1,0),
       DEFAULT_SCOPE_LIMITATION varchar(50),
       STAGE varchar(10),
       DATE_COSTS_VALID date,
       LEAD_PROCEEDING_IND NUMBER(1,0),
       LAR_SCOPE varchar(50),
       FK_APPLICATION NUMBER(19,0) not null,
       CREATED timestamp,
       CREATED_BY varchar(50),
       MODIFIED timestamp,
       MODIFIED_BY varchar(50),
       primary key (ID)
);

create table XXCCMS_PROCEEDING_OUTCOME (
       "ID" NUMBER(19,0) NOT NULL ENABLE,
       DATE_OF_ISSUE date,
       DESCRIPTION varchar(542),
       STAGE_END varchar(70),
       RESOLUTION_METHOD varchar(70),
       DATE_OF_FINAL_WORK date,
       RESULT varchar(70),
       RESULT_INFO varchar(1000),
       ALTERNATIVE_RESOLUTION varchar(70),
       ADR_INFO varchar(1000),
       WIDER_BENEFITS varchar(70),
       COURT_CODE varchar(70),
       COURT_NAME varchar(70),
       PROCEEDING_CASE_ID varchar(50),
       STAGE_END_DISPLAY_VALUE varchar(150),
       RESULT_DISPLAY_VALUE varchar(150),
       OUTCOME_COURT_CASE_NO varchar(30),
       FK_PROCEEDING_CASE_OUTCOME NUMBER(19,0) not null,
       CREATED timestamp,
       CREATED_BY varchar(50),
       MODIFIED timestamp,
       MODIFIED_BY varchar(50),
       primary key (ID)
);

create table XXCCMS_OPPONENT (
         "ID" NUMBER(19,0) NOT NULL ENABLE,
         EBS_ID varchar(50),
         TYPE varchar(50),
         TITLE varchar(50),
         FIRST_NAME varchar(35),
         MIDDLE_NAMES varchar(35),
         SURNAME varchar(35),
         DATE_OF_BIRTH date,
         NATIONAL_INSURANCE_NUMBER varchar(9),
         RELATIONSHIP_TO_CASE varchar(50),
         RELATIONSHIP_TO_CLIENT varchar(50),
         TELEPHONE_HOME varchar(15),
         TELEPHONE_WORK varchar(15),
         TELEPHONE_MOBILE varchar(15),
         FAX_NUMBER varchar(15),
         EMAIL_ADDRESS varchar(254),
         OTHER_INFORMATION varchar(2000),
         EMPLOYMENT_STATUS varchar(25),
         EMPLOYER_NAME varchar(35),
         EMPLOYER_ADDRESS varchar(200),
         LEGAL_AIDED NUMBER(1,0),
         CERTIFICATE_NUMBER varchar(35),
         COURT_ORDERDED_MEANS_ASSMT varchar(10),
         ASSESSED_INCOME numeric(10,2),
         ASSESSED_INCOME_FREQUENCY varchar(25),
         ASSESSED_ASSETS numeric(10,2),
         ASSESSMENT_DATE date,
         ORGANISATION_NAME varchar(360),
         ORGANISATION_TYPE varchar(50),
         CONTACT_NAME_ROLE varchar(35),
         CURRENTLY_TRADING varchar(10),
         CONFIRMED NUMBER(1,0),
         APPMODE NUMBER(1,0),
         AMENDMENT NUMBER(1,0),
         AWARD NUMBER(1,0),
         PUBLIC_FUNDING_APPLIED NUMBER(1,0),
         SHARED_IND NUMBER(1,0),
         DELETE_IND NUMBER(1,0),
         FK_APPLICATION NUMBER(19,0),
         FK_OUTCOME NUMBER(19,0),
         FK_ADDRESS NUMBER(19,0),
         CREATED timestamp,
         CREATED_BY varchar(50),
         MODIFIED timestamp,
         MODIFIED_BY varchar(50),
         primary key (ID)
);

create table XXCCMS_CASE_OUTCOME (
         "ID" NUMBER(19,0) NOT NULL ENABLE,
         LSC_CASE_REFERENCE varchar(50) not null,
         PROVIDER_ID varchar(19) not null,
         PRE_CERTIFICATE_COSTS numeric(10,2),
         LEGAL_COSTS numeric(10,2),
         DISCHARGE_REASON varchar(50),
         DISCHARGE_CASE_IND varchar(10),
         CLIENT_CONTINUE_IND varchar(10),
         OTHER_DETAILS varchar(1000),
         SUBMISSION_STATUS varchar(30),
         POLL_TRANSACTION_ID varchar(30),
         OFFICE_CODE varchar(30),
         UNIQUE_FILE_NO varchar(30),
         CREATED timestamp,
         CREATED_BY varchar(50),
         MODIFIED timestamp,
         MODIFIED_BY varchar(50),
         primary key (ID),
         unique (LSC_CASE_REFERENCE, PROVIDER_ID)
);

CREATE UNIQUE INDEX "SYS_C0010984" ON "XXCCMS_APPLICATION" ("ID");
CREATE UNIQUE INDEX "SYS_C0010985" ON "XXCCMS_APPLICATION" ("LSC_CASE_REFERENCE", "PROVIDER_ID");
CREATE INDEX "XXCCMS_APPLICATION_I3" ON "XXCCMS_APPLICATION" ("FK_CORRESPONDENCE_ADDRESS");
CREATE INDEX "XXCCMS_APPLICATION_I2" ON "XXCCMS_APPLICATION" ("FK_COST_STRUCTURE");





