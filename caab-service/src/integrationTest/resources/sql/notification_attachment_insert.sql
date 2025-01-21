INSERT INTO XXCCMS_PUI.XXCCMS_NOTIFICATION_ATTACHMENT (
  ID,
  NOTIFICATION_REFERENCE,
  PROVIDER_ID,
  NOTIFICATION_NUMBER,
  "TYPE",
  TYPE_DISPLAY_VALUE,
  SEND_BY,
  DESCRIPTION,
  STATUS,
  FILE_NAME,
  FILE_BYTES,
  CREATED,
  CREATED_BY,
  MODIFIED,
  MODIFIED_BY
)
VALUES (
  282,
  '123451',
  '123452',
  1,
  '123453',
  'Document Type',
  'Electronic',
  'Description',
  'Status',
  'File Name',
  TO_BLOB('626C6120626C61'),
  SYSDATE,
  'SOMEUSER@COMPANY.CO.UK',
  null,
  null
);

INSERT INTO XXCCMS_PUI.XXCCMS_NOTIFICATION_ATTACHMENT (
  ID,
  NOTIFICATION_REFERENCE,
  PROVIDER_ID,
  NOTIFICATION_NUMBER,
  "TYPE",
  TYPE_DISPLAY_VALUE,
  SEND_BY,
  DESCRIPTION,
  STATUS,
  FILE_NAME,
  FILE_BYTES,
  CREATED,
  CREATED_BY,
  MODIFIED,
  MODIFIED_BY
)
VALUES (
  283,
  '123454',
  '123455',
  1,
  '123456',
  'Document Type',
  'Electronic',
  'Description',
  'Status',
  'File Name',
  TO_BLOB('626C6120626C61'),
  SYSDATE,
  'SOMEOTHERUSER@COMPANY.CO.UK',
  null,
  null
);

INSERT INTO XXCCMS_PUI.XXCCMS_NOTIFICATION_ATTACHMENT (
  ID,
  NOTIFICATION_REFERENCE,
  PROVIDER_ID,
  NOTIFICATION_NUMBER,
  "TYPE",
  TYPE_DISPLAY_VALUE,
  SEND_BY,
  DESCRIPTION,
  STATUS,
  FILE_NAME,
  FILE_BYTES,
  CREATED,
  CREATED_BY,
  MODIFIED,
  MODIFIED_BY
)
VALUES (
  284,
  '123457',
  '123458',
  1,
  '123459',
  'Document Type',
  'Post',
  'Description',
  'Status',
  'File Name',
  TO_BLOB('626C6120626C61'),
  SYSDATE,
  'SOMEOTHEROTHERUSER@COMPANY.CO.UK',
  null,
  null
);