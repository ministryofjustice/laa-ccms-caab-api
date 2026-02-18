CREATE table XXCCMS_PUI.XXCCMS_ADDRESS (
                                ID number(19,0) not null,
                                NO_FIXED_ABODE number(1,0),
                                POSTCODE varchar2(15 char),
                                HOUSE_NAME_NUMBER varchar2(50 char),
                                ADDRESS_LINE1 varchar2(35 char),
                                ADDRESS_LINE2 varchar2(35 char),
                                CITY varchar2(35 char),
                                COUNTY varchar2(35 char),
                                COUNTRY varchar2(3 char),
                                CARE_OF varchar2(35 char),
                                PREFERRED_ADDRESS varchar2(50 char),
                                CREATED timestamp,
                                CREATED_BY varchar2(50 char),
                                MODIFIED timestamp,
                                MODIFIED_BY varchar2(50 char),
                                primary key (ID)
);

CREATE table XXCCMS_PUI.XXCCMS_APPLICATION (
                                    ID number(19,0) not null,
                                    LSC_CASE_REFERENCE varchar2(35 char) not null,
                                    PROVIDER_ID varchar2(19 char) not null,
                                    PROVIDER_CASE_REFERENCE varchar2(35 char),
                                    PROVIDER_DISPLAY_VALUE varchar2(300 char),
                                    OFFICE_ID number(19,0),
                                    OFFICE_DISPLAY_VALUE varchar2(360 char),
                                    SUPERVISOR varchar2(19 char),
                                    SUPERVISOR_DISPLAY_VALUE varchar2(300 char),
                                    FEE_EARNER varchar2(19 char),
                                    FEE_EARNER_DISPLAY_VALUE varchar2(300 char),
                                    PROVIDER_CONTACT varchar2(19 char),
                                    PROVIDER_CONTACT_DISPLAY_VALUE varchar2(300 char),
                                    CATEGORY_OF_LAW varchar2(50 char) not null,
                                    CATEGORY_OF_LAW_DISPLAY_VALUE varchar2(50 char),
                                    RELATION_TO_LINKED_CASE varchar2(50 char),
                                    OPPONENT_APPLIED_FOR_FUNDING number(1,0),
                                    DISPLAY_STATUS varchar2(50 char),
                                    ACTUAL_STATUS varchar2(50 char),
                                    CLIENT_FIRSTNAME varchar2(50 char),
                                    CLIENT_SURNAME varchar2(50 char),
                                    CLIENT_REFERENCE varchar2(30 char) not null,
                                    AMENDMENT number(1,0),
                                    MEANS_ASSESSMENT_AMENDED number(1,0),
                                    MERITS_ASSESSMENT_AMENDED number(1,0),
                                    COST_LIMIT_CHANGED number(1,0),
                                    COST_LIMIT_AT_TIME_OF_MERITS number(10,2),
                                    SUBMISSION_STATUS varchar2(30 char),
                                    POLL_TRANSACTION_ID varchar2(30 char),
                                    APPLICATION_TYPE varchar2(30 char),
                                    APPLICATION_TYPE_DISPLAY_VALUE varchar2(35 char),
                                    DEVOLVED_POWERS_USED varchar2(5 char),
                                    DATE_DEVOLVED_POWERS_USED date,
                                    DEVOLVED_POWERS_CONTRACT_FLAG varchar2(30 char),
                                    MERITS_REASSESSMENT_REQD_IND number(1,0),
                                    LAR_SCOPE_FLAG varchar2(5 char),
                                    LEAD_PROCEEDING_CHANGED number(1,0),
                                    FK_COST_STRUCTURE number(19,0),
                                    FK_CORRESPONDENCE_ADDRESS number(19,0),
                                    CREATED timestamp,
                                    CREATED_BY varchar2(50 char),
                                    MODIFIED timestamp,
                                    MODIFIED_BY varchar2(50 char),
                                    primary key (ID),
                                    unique (LSC_CASE_REFERENCE, PROVIDER_ID)
);

CREATE table XXCCMS_PUI.XXCCMS_BILL (
                             ID number(19,0) not null,
                             CASE_REFERENCE_NUMBER varchar2(50 char) not null,
                             PROVIDER_ID varchar2(19 char) not null,
                             TYPE_OF_BILL varchar2(50 char),
                             CLIENT_APPROVAL number(1,0),
                             COURT_CODE varchar2(70 char),
                             COURT_NAME varchar2(70 char),
                             COURT_ASSESSMENT number(1,0),
                             SUPPORTING_INFO varchar2(2000 char),
                             DATE_SEND_TO_CLIENT timestamp,
                             CLIENT_RESPONSE varchar2(50 char),
                             CLIENT_OBJECTION_REASON varchar2(2000 char),
                             COURT_ASSESSMENT_DATE timestamp,
                             AMOUNT number(10,2),
                             CREATED timestamp,
                             CREATED_BY varchar2(50 char),
                             MODIFIED timestamp,
                             MODIFIED_BY varchar2(50 char),
                             primary key (ID),
                             unique (CASE_REFERENCE_NUMBER, PROVIDER_ID)
);

CREATE table XXCCMS_PUI.XXCCMS_CASE_OUTCOME (
                                     ID number(19,0) not null,
                                     LSC_CASE_REFERENCE varchar2(50 char) not null,
                                     PROVIDER_ID varchar2(19 char) not null,
                                     PRE_CERTIFICATE_COSTS number(10,2),
                                     LEGAL_COSTS number(10,2),
                                     DISCHARGE_REASON varchar2(50 char),
                                     DISCHARGE_CASE_IND varchar2(10 char),
                                     CLIENT_CONTINUE_IND varchar2(10 char),
                                     OTHER_DETAILS varchar2(1000 char),
                                     SUBMISSION_STATUS varchar2(30 char),
                                     POLL_TRANSACTION_ID varchar2(30 char),
                                     OFFICE_CODE varchar2(30 char),
                                     UNIQUE_FILE_NO varchar2(30 char),
                                     CREATED timestamp,
                                     CREATED_BY varchar2(50 char),
                                     MODIFIED timestamp,
                                     MODIFIED_BY varchar2(50 char),
                                     primary key (ID),
                                     unique (LSC_CASE_REFERENCE, PROVIDER_ID)
);

CREATE table XXCCMS_PUI.XXCCMS_COST_AWARD (
                                   ID number(19,0) not null,
                                   AWARD_TYPE varchar2(50 char),
                                   DESCRIPTION varchar2(50 char),
                                   AWARDED_BY varchar2(50 char),
                                   DATE_OF_ORDER date,
                                   AWARD_AMOUNT number(10,2),
                                   COURT_ASSESSMENT_STATUS varchar2(50 char),
                                   PRECERTIFICATE_LSC_COST number(10,2),
                                   PRECERTIFICATE_OTHER_COST number(10,2),
                                   CERTIFICATE_COST_LSC number(10,2),
                                   CERTIFICATE_COST_MARKET number(10,2),
                                   INTEREST_AWARDED_RATE number(10,2),
                                   ORDER_DATE_SERVED date,
                                   INTEREST_START_DATE date,
                                   OTHER_DETAILS varchar2(1000 char),
                                   ADDRESS_LINE1 varchar2(100 char),
                                   ADDRESS_LINE2 varchar2(100 char),
                                   ADDRESS_LINE3 varchar2(100 char),
                                   UPDATE_ALLOWED_IND number(1,0),
                                   DELETE_ALLOWED_IND number(1,0),
                                   EBS_ID varchar2(15 char),
                                   AWARD_CODE varchar2(30 char),
                                   FK_COST_CASE_OUTCOME number(19,0) not null,
                                   FK_COST_RECOVERY number(19,0),
                                   CREATED timestamp,
                                   CREATED_BY varchar2(50 char),
                                   MODIFIED timestamp,
                                   MODIFIED_BY varchar2(50 char),
                                   primary key (ID)
);

CREATE table XXCCMS_PUI.XXCCMS_COST_ENTRY (
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

CREATE table XXCCMS_PUI.XXCCMS_COST_STRUCTURE (
                                       ID number(19,0) not null,
                                       DEFAULT_COST_LIMITATION number(10,2),
                                       GRANTED_COST_LIMITATION number(10,2),
                                       REQUESTED_COST_LIMITATION number(10,2),
                                       CREATED timestamp,
                                       CREATED_BY varchar2(50 char),
                                       MODIFIED timestamp,
                                       MODIFIED_BY varchar2(50 char),
                                       primary key (ID)
);

CREATE table XXCCMS_PUI.XXCCMS_FINANCIAL_AWARD (
                                        ID number(19,0) not null,
                                        AWARD_TYPE varchar2(50 char),
                                        DESCRIPTION varchar2(50 char),
                                        AWARDED_BY varchar2(50 char),
                                        DATE_OF_ORDER date,
                                        AWARD_AMOUNT number(10,2),
                                        STATUTORY_CHARGE_EXMP_REASON varchar2(1000 char),
                                        AWARD_JUSTIFICATIONS varchar2(1000 char),
                                        INTERIM_AWARD varchar2(50 char),
                                        DATE_ORDER_SERVED date,
                                        OTHER_DETAILS varchar2(1000 char),
                                        ADDRESS_LINE1 varchar2(100 char),
                                        ADDRESS_LINE2 varchar2(100 char),
                                        ADDRESS_LINE3 varchar2(100 char),
                                        UPDATE_ALLOWED_IND number(1,0),
                                        DELETE_ALLOWED_IND number(1,0),
                                        EBS_ID varchar2(15 char),
                                        AWARD_CODE varchar2(30 char),
                                        FK_FINANCIAL_CASE_OUTCOME number(19,0) not null,
                                        FK_COST_RECOVERY number(19,0),
                                        CREATED timestamp,
                                        CREATED_BY varchar2(50 char),
                                        MODIFIED timestamp,
                                        MODIFIED_BY varchar2(50 char),
                                        primary key (ID)
);

CREATE table XXCCMS_PUI.XXCCMS_LAND_AWARD (
                                   ID number(19,0) not null,
                                   AWARD_TYPE varchar2(50 char),
                                   DESCRIPTION varchar2(50 char),
                                   AWARDED_BY varchar2(50 char),
                                   DATE_OF_ORDER date,
                                   EQUITY number(10,2),
                                   AWARD_AMOUNT number(10,2),
                                   TITLE_NO varchar2(50 char),
                                   VALUATION_AMT number(10,2),
                                   VALUATION_CRITERIA varchar2(50 char),
                                   VALUATION_DATE date,
                                   DISPUTED_PERCENTAGE number(10,2),
                                   AWARDED_PERCENTAGE number(10,2),
                                   MORTGAGE_AMOUNT_DUE number(10,2),
                                   RECOVERY varchar2(200 char),
                                   NO_RECOVERY_DETAILS varchar2(1000 char),
                                   STAT_CHARGE_EXEMPT_REASON varchar2(1000 char),
                                   LAND_CHARGE_EXEMPT_REASON varchar2(1000 char),
                                   REGISTRATION_REFERENCE varchar2(50 char),
                                   RECOVERY_TIME_RELATED_FLAG varchar2(10 char),
                                   ADDRESS_LINE1 varchar2(100 char),
                                   ADDRESS_LINE2 varchar2(100 char),
                                   ADDRESS_LINE3 varchar2(100 char),
                                   UPDATE_ALLOWED_IND number(1,0),
                                   DELETE_ALLOWED_IND number(1,0),
                                   EBS_ID varchar2(15 char),
                                   AWARD_CODE varchar2(30 char),
                                   FK_LAND_CASE_OUTCOME number(19,0) not null,
                                   FK_LAND_RECOVERY number(19,0),
                                   CREATED timestamp,
                                   CREATED_BY varchar2(50 char),
                                   MODIFIED timestamp,
                                   MODIFIED_BY varchar2(50 char),
                                   primary key (ID)
);

CREATE table XXCCMS_PUI.XXCCMS_LIABLE_PARTY (
                                     ID number(19,0) not null,
                                     AWARD_TYPE varchar2(50 char),
                                     FK_OPPONENT number(19,0) not null,
                                     FK_COST_AWARD number(19,0),
                                     FK_FINANCIAL_AWARD number(19,0),
                                     FK_LAND_AWARD number(19,0),
                                     FK_OTHER_ASSET_AWARD number(19,0),
                                     CREATED timestamp,
                                     CREATED_BY varchar2(50 char),
                                     MODIFIED timestamp,
                                     MODIFIED_BY varchar2(50 char),
                                     primary key (ID)
);

CREATE table XXCCMS_PUI.XXCCMS_LINKED_CASE (
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

CREATE table XXCCMS_PUI.XXCCMS_NOTIFICATION_ATTACHMENT (
                                                ID number(19,0) not null,
                                                NOTIFICATION_REFERENCE varchar2(50 char) not null,
                                                PROVIDER_ID varchar2(19 char) not null,
                                                NOTIFICATION_NUMBER number(19,0),
                                                TYPE varchar2(50 char),
                                                TYPE_DISPLAY_VALUE varchar2(50 char),
                                                SEND_BY varchar2(50 char),
                                                DESCRIPTION varchar2(50 char),
                                                STATUS varchar2(50 char),
                                                FILE_NAME varchar2(255 char),
                                                FILE_BYTES long raw,
                                                CREATED timestamp,
                                                CREATED_BY varchar2(50 char),
                                                MODIFIED timestamp,
                                                MODIFIED_BY varchar2(50 char),
                                                primary key (ID)
);

CREATE table XXCCMS_PUI.XXCCMS_OPPONENT (
                                 ID number(19,0) not null,
                                 EBS_ID varchar2(50 char),
                                 TYPE varchar2(50 char),
                                 TITLE varchar2(50 char),
                                 FIRST_NAME varchar2(35 char),
                                 MIDDLE_NAMES varchar2(35 char),
                                 SURNAME varchar2(35 char),
                                 DATE_OF_BIRTH date,
                                 NATIONAL_INSURANCE_NUMBER varchar2(9 char),
                                 RELATIONSHIP_TO_CASE varchar2(50 char),
                                 RELATIONSHIP_TO_CLIENT varchar2(50 char),
                                 TELEPHONE_HOME varchar2(15 char),
                                 TELEPHONE_WORK varchar2(15 char),
                                 TELEPHONE_MOBILE varchar2(15 char),
                                 FAX_NUMBER varchar2(15 char),
                                 EMAIL_ADDRESS varchar2(254 char),
                                 OTHER_INFORMATION varchar2(2000 char),
                                 EMPLOYMENT_STATUS varchar2(25 char),
                                 EMPLOYER_NAME varchar2(35 char),
                                 EMPLOYER_ADDRESS varchar2(200 char),
                                 LEGAL_AIDED number(1,0),
                                 CERTIFICATE_NUMBER varchar2(35 char),
                                 COURT_ORDERDED_MEANS_ASSMT varchar2(10 char),
                                 ASSESSED_INCOME number(10,2),
                                 ASSESSED_INCOME_FREQUENCY varchar2(25 char),
                                 ASSESSED_ASSETS number(10,2),
                                 ASSESSMENT_DATE date,
                                 ORGANISATION_NAME varchar2(360 char),
                                 ORGANISATION_TYPE varchar2(50 char),
                                 CONTACT_NAME_ROLE varchar2(35 char),
                                 CURRENTLY_TRADING varchar2(10 char),
                                 CONFIRMED number(1,0),
                                 APPMODE number(1,0),
                                 AMENDMENT number(1,0),
                                 AWARD number(1,0),
                                 PUBLIC_FUNDING_APPLIED number(1,0),
                                 SHARED_IND number(1,0),
                                 DELETE_IND number(1,0),
                                 FK_APPLICATION number(19,0),
                                 FK_OUTCOME number(19,0),
                                 FK_ADDRESS number(19,0),
                                 CREATED timestamp,
                                 CREATED_BY varchar2(50 char),
                                 MODIFIED timestamp,
                                 MODIFIED_BY varchar2(50 char),
                                 primary key (ID)
);

CREATE table XXCCMS_PUI.XXCCMS_OTHER_ASSET_AWARD (
                                          ID number(19,0) not null,
                                          AWARD_TYPE varchar2(50 char),
                                          DESCRIPTION varchar2(50 char),
                                          AWARDED_BY varchar2(50 char),
                                          DATE_OF_ORDER date,
                                          AWARD_AMOUNT number(10,2),
                                          VALUATION_AMT number(10,2),
                                          VALUATION_CRITERIA varchar2(50 char),
                                          VALUATION_DATE date,
                                          DISPUTED_PERCENTAGE number(10,2),
                                          AWARDED_PERCENTAGE number(10,2),
                                          RECOVERED_PERCENTAGE number(10,2),
                                          DISPUTED_AMOUNT number(10,2),
                                          AWARDED_AMOUNT number(10,2),
                                          RECOVERED_AMOUNT number(10,2),
                                          RECOVERY varchar2(200 char),
                                          NO_RECOVERY_DETAILS varchar2(1000 char),
                                          STAT_CHARGE_EXEMPT_REASON varchar2(1000 char),
                                          RECOVERY_TIME_RELATED_FLAG varchar2(10 char),
                                          UPDATE_ALLOWED_IND number(1,0),
                                          DELETE_ALLOWED_IND number(1,0),
                                          EBS_ID varchar2(15 char),
                                          AWARD_CODE varchar2(30 char),
                                          FK_OTHER_ASSET_CASE_OUTCOME number(19,0) not null,
                                          FK_OTHER_ASSET_RECOVERY number(19,0),
                                          CREATED timestamp,
                                          CREATED_BY varchar2(50 char),
                                          MODIFIED timestamp,
                                          MODIFIED_BY varchar2(50 char),
                                          primary key (ID)
);

CREATE table XXCCMS_PUI.XXCCMS_PAYMENT_ON_ACCOUNT (
                                           ID number(19,0) not null,
                                           LSC_CASE_REFERENCE varchar2(50 char) not null,
                                           PROVIDER_ID varchar2(19 char) not null,
                                           CERTIFICATE_START_DATE timestamp,
                                           REGION varchar2(50 char),
                                           REASON varchar2(50 char),
                                           COURT_TYPE varchar2(50 char),
                                           DATE_INCURRED timestamp,
                                           ACTUAL_NET_COST number(10,2),
                                           VAT_RATE varchar2(25 char),
                                           DTLD_ASSESSMENT_ORDER_DATE timestamp,
                                           NOTES varchar2(200 char),
                                           CREATED timestamp,
                                           CREATED_BY varchar2(50 char),
                                           MODIFIED timestamp,
                                           MODIFIED_BY varchar2(50 char),
                                           primary key (ID),
                                           unique (LSC_CASE_REFERENCE, PROVIDER_ID)
);

CREATE table XXCCMS_PUI.XXCCMS_PRIOR_AUTHORITY (
                                        ID number(19,0) not null,
                                        EBS_ID varchar2(50 char),
                                        TYPE varchar2(50 char),
                                        TYPE_DISPLAY_VALUE varchar2(50 char),
                                        SUMMARY varchar2(35 char),
                                        JUSTIFICATION varchar2(200 char),
                                        VALUE_REQUIRED_FLAG varchar2(5 char),
                                        AMOUNT_REQUESTED number(10,2),
                                        STATUS varchar2(50 char),
                                        FK_APPLICATION number(19,0) not null,
                                        CREATED timestamp,
                                        CREATED_BY varchar2(50 char),
                                        MODIFIED timestamp,
                                        MODIFIED_BY varchar2(50 char),
                                        primary key (ID)
);

CREATE table XXCCMS_PUI.XXCCMS_PROCEEDING (
                                   ID number(19,0) not null,
                                   EBS_ID varchar2(50 char),
                                   MATTER_TYPE varchar2(50 char),
                                   MATTER_TYPE_DISPLAY_VALUE varchar2(50 char),
                                   PROCEEDING_TYPE varchar2(50 char),
                                   PROCEEDING_TYPE_DISPLAY_VALUE varchar2(542 char),
                                   DESCRIPTION varchar2(542 char),
                                   LEVEL_OF_SERVICE varchar2(50 char),
                                   LEVEL_OF_SERVICE_DISPLAY_VALUE varchar2(50 char),
                                   CLIENT_INVOLVEMENT varchar2(50 char),
                                   CLIENT_DISPLAY_VALUE varchar2(50 char),
                                   COST_LIMITATION number(10,2),
                                   STATUS varchar2(50 char),
                                   DISPLAY_STATUS varchar2(50 char),
                                   TYPE_OF_ORDER varchar2(50 char),
                                   DATE_GRANTED date,
                                   EDITED number(1,0),
                                   DEFAULT_SCOPE_LIMITATION varchar2(50 char),
                                   STAGE varchar2(10 char),
                                   DATE_COSTS_VALID date,
                                   LEAD_PROCEEDING_IND number(1,0),
                                   LAR_SCOPE varchar2(50 char),
                                   FK_APPLICATION number(19,0) not null,
                                   CREATED timestamp,
                                   CREATED_BY varchar2(50 char),
                                   MODIFIED timestamp,
                                   MODIFIED_BY varchar2(50 char),
                                   primary key (ID)
);

CREATE table XXCCMS_PUI.XXCCMS_PROCEEDING_OUTCOME (
                                           ID number(19,0) not null,
                                           DATE_OF_ISSUE date,
                                           DESCRIPTION varchar2(542 char),
                                           STAGE_END varchar2(70 char),
                                           RESOLUTION_METHOD varchar2(70 char),
                                           DATE_OF_FINAL_WORK date,
                                           RESULT varchar2(70 char),
                                           RESULT_INFO varchar2(1000 char),
                                           ALTERNATIVE_RESOLUTION varchar2(70 char),
                                           ADR_INFO varchar2(1000 char),
                                           WIDER_BENEFITS varchar2(70 char),
                                           COURT_CODE varchar2(70 char),
                                           COURT_NAME varchar2(70 char),
                                           PROCEEDING_CASE_ID varchar2(50 char),
                                           STAGE_END_DISPLAY_VALUE varchar2(150 char),
                                           RESULT_DISPLAY_VALUE varchar2(150 char),
                                           OUTCOME_COURT_CASE_NO varchar2(30 char),
                                           FK_PROCEEDING_CASE_OUTCOME number(19,0) not null,
                                           CREATED timestamp,
                                           CREATED_BY varchar2(50 char),
                                           MODIFIED timestamp,
                                           MODIFIED_BY varchar2(50 char),
                                           primary key (ID)
);

CREATE table XXCCMS_PUI.XXCCMS_RECOVERY (
                                 ID number(19,0) not null,
                                 AWARD_TYPE varchar2(50 char),
                                 DESCRIPTION varchar2(50 char),
                                 AWARD_AMT number(10,2),
                                 SOLICITOR_RECOVERY_AMT number(10,2),
                                 SOLICITOR_RECOVERY_DATE date,
                                 SOLICITOR_AMT_PAID_TO_LSC number(10,2),
                                 COURT_RECOVERY_AMT number(10,2),
                                 COURT_RECOVERY_DATE date,
                                 COURT_AMT_PAID_TO_LSC number(10,2),
                                 RECOVERED_AMT number(10,2),
                                 CLIENT_RECOVERY_DATE date,
                                 CLIENT_AMT_PAID_TO_LSC number(10,2),
                                 CLIENT_RECOVERY_AMT number(10,2),
                                 UNRECOVERED_AMT number(10,2),
                                 LEAVE_OF_COURT_REQD_IND varchar2(10 char),
                                 OFFERED_AMT number(10,2),
                                 OFFER_DETAILS varchar2(1000 char),
                                 CREATED timestamp,
                                 CREATED_BY varchar2(50 char),
                                 MODIFIED timestamp,
                                 MODIFIED_BY varchar2(50 char),
                                 primary key (ID)
);

CREATE table XXCCMS_PUI.XXCCMS_REFERENCE_DATA_ITEM (
                                            ID number(19,0) not null,
                                            CODE varchar2(50 char),
                                            LABEL varchar2(150 char),
                                            TYPE varchar2(5 char),
                                            MANDATORY varchar2(5 char),
                                            LOV_LOOKUP varchar2(200 char),
                                            VALUE varchar2(200 char),
                                            DISPLAY_VALUE varchar2(200 char),
                                            FK_PRIOR_AUTHORITY number(19,0) not null,
                                            primary key (ID)
);

CREATE table XXCCMS_PUI.XXCCMS_SCOPE_LIMITATION (
                                         ID number(19,0) not null,
                                         EBS_ID varchar2(50 char),
                                         SCOPE_LIMITATION varchar2(50 char),
                                         SCOPE_LIMITATION_DISPLAY_VALUE varchar2(100 char),
                                         SCOPE_LIMITATION_WORDING varchar2(1000 char),
                                         DEFAULT_IND number(1,0),
                                         DELEGATED_FUNC_APPLY_IND number(1,0),
                                         FK_PROCEEDING number(19,0),
                                         CREATED timestamp,
                                         CREATED_BY varchar2(50 char),
                                         MODIFIED timestamp,
                                         MODIFIED_BY varchar2(50 char),
                                         primary key (ID)
);

CREATE table XXCCMS_PUI.XXCCMS_TIME_RECOVERY (
                                      ID number(19,0) not null,
                                      AWARD_TYPE varchar2(50 char),
                                      DESCRIPTION varchar2(50 char),
                                      TRIGGERING_EVENT varchar2(1000 char),
                                      EFFECTIVE_DATE date,
                                      TIME_RELATED_RECOVERY_DETAILS varchar2(1000 char),
                                      AWARD_AMT number(10,2),
                                      CREATED timestamp,
                                      CREATED_BY varchar2(50 char),
                                      MODIFIED timestamp,
                                      MODIFIED_BY varchar2(50 char),
                                      primary key (ID)
);

CREATE table XXCCMS_PUI.XXCCMS_UNDERTAKING (
                                    ID number(19,0) not null,
                                    LSC_CASE_REFERENCE varchar2(50 char) not null,
                                    PROVIDER_ID varchar2(19 char) not null,
                                    AMOUNT number(10,2),
                                    ACCEPTED_TERMS number(1,0),
                                    SUBMISSION_STATUS varchar2(30 char),
                                    POLL_TRANSACTION_ID varchar2(30 char),
                                    CREATED timestamp,
                                    CREATED_BY varchar2(50 char),
                                    MODIFIED timestamp,
                                    MODIFIED_BY varchar2(50 char),
                                    primary key (ID),
                                    unique (LSC_CASE_REFERENCE, PROVIDER_ID)
);

CREATE index XXCCMS_PUI.XXCCMS_APPLICATION_I3 on XXCCMS_PUI.XXCCMS_APPLICATION (FK_CORRESPONDENCE_ADDRESS);

CREATE index XXCCMS_PUI.XXCCMS_APPLICATION_I2 on XXCCMS_PUI.XXCCMS_APPLICATION (FK_COST_STRUCTURE);

alter table XXCCMS_PUI.XXCCMS_APPLICATION
    add constraint XXCCMS_APPLICATION_F1
        foreign key (FK_COST_STRUCTURE)
            references XXCCMS_PUI.XXCCMS_COST_STRUCTURE;

alter table XXCCMS_PUI.XXCCMS_APPLICATION
    add constraint XXCCMS_APPLICATION_F2
        foreign key (FK_CORRESPONDENCE_ADDRESS)
            references XXCCMS_PUI.XXCCMS_ADDRESS;

CREATE index XXCCMS_PUI.XXCCMS_COST_AWARD_I2 on XXCCMS_PUI.XXCCMS_COST_AWARD (FK_COST_RECOVERY);

CREATE index XXCCMS_PUI.XXCCMS_COST_AWARD_I1 on XXCCMS_PUI.XXCCMS_COST_AWARD (FK_COST_CASE_OUTCOME);

alter table XXCCMS_PUI.XXCCMS_COST_AWARD
    add constraint FKC29DD024FD68D189
        foreign key (FK_COST_CASE_OUTCOME)
            references XXCCMS_PUI.XXCCMS_CASE_OUTCOME;

alter table XXCCMS_PUI.XXCCMS_COST_AWARD
    add constraint FKC29DD0241FC208D6
        foreign key (FK_COST_RECOVERY)
            references XXCCMS_PUI.XXCCMS_RECOVERY;

CREATE index XXCCMS_PUI.XXCCMS_COST_ENTRY_I1 on XXCCMS_PUI.XXCCMS_COST_ENTRY (FK_COST_STRUCTURE);

alter table XXCCMS_PUI.XXCCMS_COST_ENTRY
    add constraint XXCCMS_COST_ENTRY_F1
        foreign key (FK_COST_STRUCTURE)
            references XXCCMS_PUI.XXCCMS_COST_STRUCTURE;

CREATE index XXCCMS_PUI.XXCCMS_FINANCIAL_AWARD_I2 on XXCCMS_PUI.XXCCMS_FINANCIAL_AWARD (FK_COST_RECOVERY);

CREATE index XXCCMS_PUI.XXCCMS_FINANCIAL_AWARD_I1 on XXCCMS_PUI.XXCCMS_FINANCIAL_AWARD (FK_FINANCIAL_CASE_OUTCOME);

alter table XXCCMS_PUI.XXCCMS_FINANCIAL_AWARD
    add constraint FKEAA2ADAE58E74C01
        foreign key (FK_FINANCIAL_CASE_OUTCOME)
            references XXCCMS_PUI.XXCCMS_CASE_OUTCOME;

alter table XXCCMS_PUI.XXCCMS_FINANCIAL_AWARD
    add constraint FKEAA2ADAE1FC208D6
        foreign key (FK_COST_RECOVERY)
            references XXCCMS_PUI.XXCCMS_RECOVERY;

CREATE index XXCCMS_PUI.XXCCMS_LAND_AWARD_I1 on XXCCMS_PUI.XXCCMS_LAND_AWARD (FK_LAND_CASE_OUTCOME);

CREATE index XXCCMS_PUI.XXCCMS_LAND_AWARD_I2 on XXCCMS_PUI.XXCCMS_LAND_AWARD (FK_LAND_RECOVERY);

alter table XXCCMS_PUI.XXCCMS_LAND_AWARD
    add constraint FKE7AB23C27C966865
        foreign key (FK_LAND_RECOVERY)
            references XXCCMS_PUI.XXCCMS_TIME_RECOVERY;

alter table XXCCMS_PUI.XXCCMS_LAND_AWARD
    add constraint FKE7AB23C29E41D02B
        foreign key (FK_LAND_CASE_OUTCOME)
            references XXCCMS_PUI.XXCCMS_CASE_OUTCOME;

CREATE index XXCCMS_PUI.XXCCMS_LIABLE_PARTY_I5 on XXCCMS_PUI.XXCCMS_LIABLE_PARTY (FK_OTHER_ASSET_AWARD);

CREATE index XXCCMS_PUI.XXCCMS_LIABLE_PARTY_I3 on XXCCMS_PUI.XXCCMS_LIABLE_PARTY (FK_FINANCIAL_AWARD);

CREATE index XXCCMS_PUI.XXCCMS_LIABLE_PARTY_I4 on XXCCMS_PUI.XXCCMS_LIABLE_PARTY (FK_LAND_AWARD);

CREATE index XXCCMS_PUI.XXCCMS_LIABLE_PARTY_I1 on XXCCMS_PUI.XXCCMS_LIABLE_PARTY (FK_OPPONENT);

CREATE index XXCCMS_PUI.XXCCMS_LIABLE_PARTY_I2 on XXCCMS_PUI.XXCCMS_LIABLE_PARTY (FK_COST_AWARD);

alter table XXCCMS_PUI.XXCCMS_LIABLE_PARTY
    add constraint FKECA8777712B5CB75
        foreign key (FK_FINANCIAL_AWARD)
            references XXCCMS_PUI.XXCCMS_FINANCIAL_AWARD;

alter table XXCCMS_PUI.XXCCMS_LIABLE_PARTY
    add constraint FKECA877774A6C1101
        foreign key (FK_LAND_AWARD)
            references XXCCMS_PUI.XXCCMS_LAND_AWARD;

alter table XXCCMS_PUI.XXCCMS_LIABLE_PARTY
    add constraint FKECA87777C113FCC1
        foreign key (FK_COST_AWARD)
            references XXCCMS_PUI.XXCCMS_COST_AWARD;

alter table XXCCMS_PUI.XXCCMS_LIABLE_PARTY
    add constraint FKECA877776F94E468
        foreign key (FK_OPPONENT)
            references XXCCMS_PUI.XXCCMS_OPPONENT;

alter table XXCCMS_PUI.XXCCMS_LIABLE_PARTY
    add constraint FKECA877775F538B2E
        foreign key (FK_OTHER_ASSET_AWARD)
            references XXCCMS_PUI.XXCCMS_OTHER_ASSET_AWARD;

CREATE index XXCCMS_PUI.XXCCMS_LINKED_CASE_I1 on XXCCMS_PUI.XXCCMS_LINKED_CASE (FK_APPLICATION);

alter table XXCCMS_PUI.XXCCMS_LINKED_CASE
    add constraint XXCCMS_LINKED_CASE_F1
        foreign key (FK_APPLICATION)
            references XXCCMS_PUI.XXCCMS_APPLICATION;

CREATE index XXCCMS_PUI.XXCCMS_OPPONENT_I4 on XXCCMS_PUI.XXCCMS_OPPONENT (FK_ADDRESS);

CREATE index XXCCMS_PUI.XXCCMS_OPPONENT_I3 on XXCCMS_PUI.XXCCMS_OPPONENT (FK_OUTCOME);

CREATE index XXCCMS_PUI.XXCCMS_OPPONENT_I1 on XXCCMS_PUI.XXCCMS_OPPONENT (FK_APPLICATION);

alter table XXCCMS_PUI.XXCCMS_OPPONENT
    add constraint FK1CE244763F793FE6
        foreign key (FK_OUTCOME)
            references XXCCMS_PUI.XXCCMS_CASE_OUTCOME;

alter table XXCCMS_PUI.XXCCMS_OPPONENT
    add constraint XXCCMS_OPPONENT_F1
        foreign key (FK_APPLICATION)
            references XXCCMS_PUI.XXCCMS_APPLICATION;

alter table XXCCMS_PUI.XXCCMS_OPPONENT
    add constraint XXCCMS_OPPONENT_F2
        foreign key (FK_ADDRESS)
            references XXCCMS_PUI.XXCCMS_ADDRESS;

CREATE index XXCCMS_PUI.XXCCMS_OTHER_ASSET_AWARD_I2 on XXCCMS_PUI.XXCCMS_OTHER_ASSET_AWARD (FK_OTHER_ASSET_RECOVERY);

CREATE index XXCCMS_PUI.XXCCMS_OTHER_ASSET_AWARD_I1 on XXCCMS_PUI.XXCCMS_OTHER_ASSET_AWARD (FK_OTHER_ASSET_CASE_OUTCOME);

alter table XXCCMS_PUI.XXCCMS_OTHER_ASSET_AWARD
    add constraint FK537C72467A92DB43
        foreign key (FK_OTHER_ASSET_RECOVERY)
            references XXCCMS_PUI.XXCCMS_TIME_RECOVERY;

alter table XXCCMS_PUI.XXCCMS_OTHER_ASSET_AWARD
    add constraint FK537C724690EDA409
        foreign key (FK_OTHER_ASSET_CASE_OUTCOME)
            references XXCCMS_PUI.XXCCMS_CASE_OUTCOME;

CREATE index XXCCMS_PUI.XXCCMS_PRIOR_AUTHORITY_I1 on XXCCMS_PUI.XXCCMS_PRIOR_AUTHORITY (FK_APPLICATION);

alter table XXCCMS_PUI.XXCCMS_PRIOR_AUTHORITY
    add constraint XXCCMS_PRIOR_AUTHORITY_F1
        foreign key (FK_APPLICATION)
            references XXCCMS_PUI.XXCCMS_APPLICATION;

CREATE index XXCCMS_PUI.XXCCMS_PROCEEDING_I1 on XXCCMS_PUI.XXCCMS_PROCEEDING (FK_APPLICATION);

alter table XXCCMS_PUI.XXCCMS_PROCEEDING
    add constraint XXCCMS_PROCEEDING_F1
        foreign key (FK_APPLICATION)
            references XXCCMS_PUI.XXCCMS_APPLICATION;

CREATE index XXCCMS_PUI.XXCCMS_PROCEEDING_OUTCOME_I1 on XXCCMS_PUI.XXCCMS_PROCEEDING_OUTCOME (FK_PROCEEDING_CASE_OUTCOME);

alter table XXCCMS_PUI.XXCCMS_PROCEEDING_OUTCOME
    add constraint FKF884AE40329097C2
        foreign key (FK_PROCEEDING_CASE_OUTCOME)
            references XXCCMS_PUI.XXCCMS_CASE_OUTCOME;

CREATE index XXCCMS_PUI.XXCCMS_DATA_ITEM_I1 on XXCCMS_PUI.XXCCMS_REFERENCE_DATA_ITEM (FK_PRIOR_AUTHORITY);

alter table XXCCMS_PUI.XXCCMS_REFERENCE_DATA_ITEM
    add constraint XXCCMS_DATA_ITEM_F1
        foreign key (FK_PRIOR_AUTHORITY)
            references XXCCMS_PUI.XXCCMS_PRIOR_AUTHORITY;

CREATE index XXCCMS_PUI.XXCCMS_SCOPE_LIMITATION_I1 on XXCCMS_PUI.XXCCMS_SCOPE_LIMITATION (FK_PROCEEDING);

alter table XXCCMS_PUI.XXCCMS_SCOPE_LIMITATION
    add constraint XXCCMS_SCOPE_LIMITATION_F1
        foreign key (FK_PROCEEDING)
            references XXCCMS_PUI.XXCCMS_PROCEEDING;

CREATE sequence XXCCMS_PUI.XXCCMS_GENERATED_ID_S;

CREATE TABLE XXCCMS_PUI.XXCCMS_EVIDENCE_DOCUMENTS
(
    ID                        NUMBER(19)          NOT NULL,
    NOTIFICATION_REFERENCE    VARCHAR2(50),
    PROVIDER_ID               VARCHAR2(19)   NOT NULL,
    CASE_ID                   VARCHAR2(20)   NOT NULL,
    DOCUMENT_TYPE             VARCHAR2(50)   NOT NULL,
    FILE_EXTENSION            VARCHAR2(20)   NOT NULL,
    TYPE_DISPLAY_VALUE        VARCHAR2(50)   NOT NULL,
    DOCUMENT_SENDER           VARCHAR2(50)   NOT NULL,
    USER_FREE_TEXT            VARCHAR2(2000),
    FILE_NAME                 VARCHAR2(255)  NOT NULL,
    DOCUMENT                  BLOB                NOT NULL,
    TRANSFER_STATUS           VARCHAR2(20),
    NO_OF_RETRIES_UNDERTAKEN  INTEGER             DEFAULT 0                     NOT NULL,
    WEBSERVICE_RESPONSE_CODE  VARCHAR2(500),
    WEBSERVICE_RESPONSE_DESC  VARCHAR2(4000),
    DATE_CREATED              DATE                DEFAULT SYSDATE               NOT NULL,
    USER_CREATED              VARCHAR2(50)   DEFAULT USER                  NOT NULL,
    DATE_MODIFIED             DATE,
    USER_MODIFIED             VARCHAR2(50),
    DOCHANDLER_SESSION_ID     VARCHAR2(100),
    CCMS_MODULE               CHAR(1)        DEFAULT 'A'                   NOT NULL,
    EVIDENCE_DOC_DESCS        VARCHAR2(4000),
    REGISTERED_DOCUMENT_ID    VARCHAR2(50)
);

CREATE TABLE FND_LOOKUP_VALUES
(
    LOOKUP_TYPE          VARCHAR2(30)        NOT NULL,
    LOOKUP_CODE          VARCHAR2(30)        NOT NULL,
    MEANING              VARCHAR2(80)        NOT NULL,
    DESCRIPTION          VARCHAR2(240)
);

ALTER TABLE XXCCMS_PUI.XXCCMS_notification_attachment modify  file_bytes blob;
