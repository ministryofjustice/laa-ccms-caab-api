INSERT INTO XXCCMS_PUI.XXCCMS_ADDRESS
(ID, NO_FIXED_ABODE, POSTCODE, HOUSE_NAME_NUMBER, ADDRESS_LINE1, ADDRESS_LINE2, CITY, COUNTY, COUNTRY, CARE_OF, PREFERRED_ADDRESS, CREATED, CREATED_BY, MODIFIED, MODIFIED_BY)
VALUES(42, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, TIMESTAMP '2023-10-04 10:23:17.672000', 'PENNY.WALL@SWITALSKIS.COM', TIMESTAMP '2023-10-04 10:23:17.672000', 'PENNY.WALL@SWITALSKIS.COM');
INSERT INTO XXCCMS_PUI.XXCCMS_ADDRESS
(ID, NO_FIXED_ABODE, POSTCODE, HOUSE_NAME_NUMBER, ADDRESS_LINE1, ADDRESS_LINE2, CITY, COUNTY, COUNTRY, CARE_OF, PREFERRED_ADDRESS, CREATED, CREATED_BY, MODIFIED, MODIFIED_BY)
VALUES(22, 0, 'string', 'string', 'string', 'string', 'string', 'string', 'GBR', 'string', 'string', TIMESTAMP '2023-10-03 08:27:05.266000', 'PENNY.WALL@SWITALSKIS.COM', TIMESTAMP '2023-10-03 08:27:05.266000', 'PENNY.WALL@SWITALSKIS.COM');
INSERT INTO XXCCMS_PUI.XXCCMS_ADDRESS
(ID, NO_FIXED_ABODE, POSTCODE, HOUSE_NAME_NUMBER, ADDRESS_LINE1, ADDRESS_LINE2, CITY, COUNTY, COUNTRY, CARE_OF, PREFERRED_ADDRESS, CREATED, CREATED_BY, MODIFIED, MODIFIED_BY)
VALUES(25, 0, 'string', 'string', 'string', 'string', 'string', 'string', 'GBR', 'string', 'string', TIMESTAMP '2023-10-03 08:49:50.262000', 'PENNY.WALL@SWITALSKIS.COM', TIMESTAMP '2023-10-03 08:49:50.262000', 'PENNY.WALL@SWITALSKIS.COM');

INSERT INTO XXCCMS_PUI.XXCCMS_COST_STRUCTURE
(ID, DEFAULT_COST_LIMITATION, GRANTED_COST_LIMITATION, REQUESTED_COST_LIMITATION, CREATED, CREATED_BY, MODIFIED, MODIFIED_BY)
VALUES(43, 0, 0, 0, TIMESTAMP '2023-10-04 10:23:17.927000', 'PENNY.WALL@SWITALSKIS.COM', TIMESTAMP '2023-10-04 10:23:17.927000', 'PENNY.WALL@SWITALSKIS.COM');
INSERT INTO XXCCMS_PUI.XXCCMS_COST_STRUCTURE
(ID, DEFAULT_COST_LIMITATION, GRANTED_COST_LIMITATION, REQUESTED_COST_LIMITATION, CREATED, CREATED_BY, MODIFIED, MODIFIED_BY)
VALUES(23, 0, 0, 0, TIMESTAMP '2023-10-03 08:27:05.592000', 'PENNY.WALL@SWITALSKIS.COM', TIMESTAMP '2023-10-03 08:27:05.592000', 'PENNY.WALL@SWITALSKIS.COM');
INSERT INTO XXCCMS_PUI.XXCCMS_COST_STRUCTURE
(ID, DEFAULT_COST_LIMITATION, GRANTED_COST_LIMITATION, REQUESTED_COST_LIMITATION, CREATED, CREATED_BY, MODIFIED, MODIFIED_BY)
VALUES(26, 0, 0, 0, TIMESTAMP '2023-10-03 08:49:50.389000', 'PENNY.WALL@SWITALSKIS.COM', TIMESTAMP '2023-10-03 08:49:50.389000', 'PENNY.WALL@SWITALSKIS.COM');

INSERT INTO XXCCMS_PUI.XXCCMS_APPLICATION
(ID, LSC_CASE_REFERENCE, PROVIDER_ID, PROVIDER_CASE_REFERENCE, PROVIDER_DISPLAY_VALUE, OFFICE_ID, OFFICE_DISPLAY_VALUE, SUPERVISOR, SUPERVISOR_DISPLAY_VALUE, FEE_EARNER, FEE_EARNER_DISPLAY_VALUE, PROVIDER_CONTACT, PROVIDER_CONTACT_DISPLAY_VALUE, CATEGORY_OF_LAW, CATEGORY_OF_LAW_DISPLAY_VALUE, RELATION_TO_LINKED_CASE, OPPONENT_APPLIED_FOR_FUNDING, DISPLAY_STATUS, ACTUAL_STATUS, CLIENT_FIRSTNAME, CLIENT_SURNAME, CLIENT_REFERENCE, AMENDMENT, MEANS_ASSESSMENT_AMENDED, MERITS_ASSESSMENT_AMENDED, COST_LIMIT_CHANGED, COST_LIMIT_AT_TIME_OF_MERITS, SUBMISSION_STATUS, POLL_TRANSACTION_ID, APPLICATION_TYPE, APPLICATION_TYPE_DISPLAY_VALUE, DEVOLVED_POWERS_USED, DATE_DEVOLVED_POWERS_USED, DEVOLVED_POWERS_CONTRACT_FLAG, MERITS_REASSESSMENT_REQD_IND, LAR_SCOPE_FLAG, LEAD_PROCEEDING_CHANGED, FK_COST_STRUCTURE, FK_CORRESPONDENCE_ADDRESS, CREATED, CREATED_BY, MODIFIED, MODIFIED_BY)
VALUES(41, '300001644533', '26517', NULL, 'SWITALSKI''S SOLICITORS LTD', 85391, 'SWITALSKI''S SOLICITORS LTD-0P322F', NULL, NULL, NULL, NULL, NULL, NULL, 'AAP', 'Claim Against Public Authority', NULL, NULL, 'Unsubmitted', 'UNSUBMITTED', 'PTEST', 'PP', '62595640', 0, 0, 0, 0, NULL, NULL, NULL, 'ECF', 'Exceptional Case Funding', 'N', NULL, 'Yes - Excluding JR Proceedings', 0, 'Y', 0, 43, 42, TIMESTAMP '2023-10-04 10:23:17.995000', 'PENNY.WALL@SWITALSKIS.COM', TIMESTAMP '2023-10-04 10:23:17.995000', 'PENNY.WALL@SWITALSKIS.COM');
INSERT INTO XXCCMS_PUI.XXCCMS_APPLICATION
(ID, LSC_CASE_REFERENCE, PROVIDER_ID, PROVIDER_CASE_REFERENCE, PROVIDER_DISPLAY_VALUE, OFFICE_ID, OFFICE_DISPLAY_VALUE, SUPERVISOR, SUPERVISOR_DISPLAY_VALUE, FEE_EARNER, FEE_EARNER_DISPLAY_VALUE, PROVIDER_CONTACT, PROVIDER_CONTACT_DISPLAY_VALUE, CATEGORY_OF_LAW, CATEGORY_OF_LAW_DISPLAY_VALUE, RELATION_TO_LINKED_CASE, OPPONENT_APPLIED_FOR_FUNDING, DISPLAY_STATUS, ACTUAL_STATUS, CLIENT_FIRSTNAME, CLIENT_SURNAME, CLIENT_REFERENCE, AMENDMENT, MEANS_ASSESSMENT_AMENDED, MERITS_ASSESSMENT_AMENDED, COST_LIMIT_CHANGED, COST_LIMIT_AT_TIME_OF_MERITS, SUBMISSION_STATUS, POLL_TRANSACTION_ID, APPLICATION_TYPE, APPLICATION_TYPE_DISPLAY_VALUE, DEVOLVED_POWERS_USED, DATE_DEVOLVED_POWERS_USED, DEVOLVED_POWERS_CONTRACT_FLAG, MERITS_REASSESSMENT_REQD_IND, LAR_SCOPE_FLAG, LEAD_PROCEEDING_CHANGED, FK_COST_STRUCTURE, FK_CORRESPONDENCE_ADDRESS, CREATED, CREATED_BY, MODIFIED, MODIFIED_BY)
VALUES(21, '300001644516', '26517', '329635', 'SWITALSKI''S SOLICITORS LTD', 145512, 'SWITALSKI''S SOLICITORS LTD-2L847Q', '2854148', 'David Greenwood', '2027148', 'Carole Spencer', '2027079', 'CAROLE.SPENCER@SWITALSKIS.COM', 'AAP', 'Claim Against Public Authority', NULL, NULL, 'Unsubmitted', 'UNSUBMITTED', 'Phil', 'Payne', 'PhilTest', 0, 0, 0, 0, 1350, NULL, NULL, 'SUB', 'Substantive', 'N', NULL, 'Yes - Excluding JR Proceedings', 0, 'Y', 0, 23, 22, TIMESTAMP '2023-10-03 08:27:05.652000', 'PENNY.WALL@SWITALSKIS.COM', TIMESTAMP '2023-10-03 08:27:05.652000', 'PENNY.WALL@SWITALSKIS.COM');
INSERT INTO XXCCMS_PUI.XXCCMS_APPLICATION
(ID, LSC_CASE_REFERENCE, PROVIDER_ID, PROVIDER_CASE_REFERENCE, PROVIDER_DISPLAY_VALUE, OFFICE_ID, OFFICE_DISPLAY_VALUE, SUPERVISOR, SUPERVISOR_DISPLAY_VALUE, FEE_EARNER, FEE_EARNER_DISPLAY_VALUE, PROVIDER_CONTACT, PROVIDER_CONTACT_DISPLAY_VALUE, CATEGORY_OF_LAW, CATEGORY_OF_LAW_DISPLAY_VALUE, RELATION_TO_LINKED_CASE, OPPONENT_APPLIED_FOR_FUNDING, DISPLAY_STATUS, ACTUAL_STATUS, CLIENT_FIRSTNAME, CLIENT_SURNAME, CLIENT_REFERENCE, AMENDMENT, MEANS_ASSESSMENT_AMENDED, MERITS_ASSESSMENT_AMENDED, COST_LIMIT_CHANGED, COST_LIMIT_AT_TIME_OF_MERITS, SUBMISSION_STATUS, POLL_TRANSACTION_ID, APPLICATION_TYPE, APPLICATION_TYPE_DISPLAY_VALUE, DEVOLVED_POWERS_USED, DATE_DEVOLVED_POWERS_USED, DEVOLVED_POWERS_CONTRACT_FLAG, MERITS_REASSESSMENT_REQD_IND, LAR_SCOPE_FLAG, LEAD_PROCEEDING_CHANGED, FK_COST_STRUCTURE, FK_CORRESPONDENCE_ADDRESS, CREATED, CREATED_BY, MODIFIED, MODIFIED_BY)
VALUES(24, '300001644517', '26517', '329635', 'SWITALSKI''S SOLICITORS LTD', 145512, 'SWITALSKI''S SOLICITORS LTD-2L847Q', '2854148', 'David Greenwood', '2027148', 'Carole Spencer', '2027079', 'CAROLE.SPENCER@SWITALSKIS.COM', 'AAP', 'Claim Against Public Authority', NULL, NULL, 'Unsubmitted', 'UNSUBMITTED', 'Phil', 'Payne', 'PhilTest', 0, 0, 0, 0, 1350, NULL, NULL, 'ECF', 'Exceptional Case Funding', 'N', NULL, 'Yes - Excluding JR Proceedings', 0, 'Y', 0, 26, 25, TIMESTAMP '2023-10-03 08:49:50.403000', 'PENNY.WALL@SWITALSKIS.COM', TIMESTAMP '2023-10-03 08:49:50.410000', 'PENNY.WALL@SWITALSKIS.COM');

