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

CREATE UNIQUE INDEX "SYS_C0010984" ON "XXCCMS_APPLICATION" ("ID");
CREATE UNIQUE INDEX "SYS_C0010985" ON "XXCCMS_APPLICATION" ("LSC_CASE_REFERENCE", "PROVIDER_ID");
CREATE INDEX "XXCCMS_APPLICATION_I3" ON "XXCCMS_APPLICATION" ("FK_CORRESPONDENCE_ADDRESS");
CREATE INDEX "XXCCMS_APPLICATION_I2" ON "XXCCMS_APPLICATION" ("FK_COST_STRUCTURE");






