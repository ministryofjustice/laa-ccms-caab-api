insert into XXCCMS_PUI.XXCCMS_OPPONENT (ID, EBS_ID, TYPE, TITLE, FIRST_NAME, MIDDLE_NAMES, SURNAME,
                                        DATE_OF_BIRTH, NATIONAL_INSURANCE_NUMBER,
                                        RELATIONSHIP_TO_CASE, RELATIONSHIP_TO_CLIENT,
                                        TELEPHONE_HOME, TELEPHONE_WORK, TELEPHONE_MOBILE,
                                        FAX_NUMBER, EMAIL_ADDRESS, OTHER_INFORMATION,
                                        EMPLOYMENT_STATUS, EMPLOYER_NAME, EMPLOYER_ADDRESS,
                                        LEGAL_AIDED, CERTIFICATE_NUMBER, COURT_ORDERDED_MEANS_ASSMT,
                                        ASSESSED_INCOME, ASSESSED_INCOME_FREQUENCY, ASSESSED_ASSETS,
                                        ASSESSMENT_DATE, ORGANISATION_NAME, ORGANISATION_TYPE,
                                        CONTACT_NAME_ROLE, CURRENTLY_TRADING, CONFIRMED, APPMODE,
                                        AMENDMENT, AWARD, PUBLIC_FUNDING_APPLIED, SHARED_IND,
                                        DELETE_IND, FK_APPLICATION, FK_OUTCOME, FK_ADDRESS, CREATED,
                                        CREATED_BY, MODIFIED, MODIFIED_BY)
values (3, NULL, 'type', 'title', 'firstname', 'middle', 'surname', SYSDATE, 'JC123456A',
        'rel2case', 'rel2client', '5555555', '4444444', '07777777777', '128375', 'email', 'other',
        'empstat', 'empname', 'empaddr', null, 'certnum', null, 10, 'monthly', 100, SYSDATE,
        'orgname', 'orgtype', 'contnamerole', null, null, null, null, null, null, null, null, 41, NULL, 42,
        SYSDATE, 'testuser', SYSDATE, 'testuser');