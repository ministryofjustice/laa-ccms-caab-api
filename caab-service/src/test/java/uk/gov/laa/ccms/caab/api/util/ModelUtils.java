package uk.gov.laa.ccms.caab.api.util;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import uk.gov.laa.ccms.caab.api.entity.AuditTrail;
import uk.gov.laa.ccms.caab.api.entity.EvidenceDocument;
import uk.gov.laa.ccms.caab.model.AuditDetail;
import uk.gov.laa.ccms.caab.model.EvidenceDocumentDetail;
import uk.gov.laa.ccms.caab.model.StringDisplayValue;

public class ModelUtils {
  public static EvidenceDocument buildEvidenceDocument() {
    EvidenceDocument evidenceDocument = new EvidenceDocument();
    evidenceDocument.setApplicationOrOutcomeId("appOutId");
    evidenceDocument.setAuditTrail(buildAuditTrail());
    evidenceDocument.setCaseReferenceNumber("caseref");
    evidenceDocument.setCcmsModule("module");
    evidenceDocument.setDescription("descr");
    evidenceDocument.setDocumentSender("docsend");
    evidenceDocument.setDocumentType("doctype");
    evidenceDocument.setDocumentTypeDisplayValue("doc type");
    evidenceDocument.setEvidenceDescriptions("evidence descr");
    evidenceDocument.setFileBytes("the file data".getBytes());
    evidenceDocument.setFileExtension("ext");
    evidenceDocument.setFileName("name");
    evidenceDocument.setId(123L);
    evidenceDocument.setNotificationReference("notref");
    evidenceDocument.setProviderId("provId");
    evidenceDocument.setRegisteredDocumentId("regDocId");
    evidenceDocument.setTransferResponseCode("code");
    evidenceDocument.setTransferResponseDescription("the code");
    evidenceDocument.setTransferRetryCount(10);
    evidenceDocument.setTransferStatus("stat");

    return evidenceDocument;
  }

  public static EvidenceDocumentDetail buildEvidenceDocumentDetail() {
    return new EvidenceDocumentDetail()
        .applicationOrOutcomeId("appOutId")
        .auditTrail(buildAuditDetail())
        .caseReferenceNumber("caseref")
        .ccmsModule("module")
        .description("descr")
        .documentSender("docsend")
        .documentType(new StringDisplayValue()
            .id("doctype")
            .displayValue("doc type"))
        .evidenceDescriptions("evidence descr")
        .fileData(Base64.getEncoder().encodeToString("the file data".getBytes()))
        .fileExtension("ext")
        .fileName("name")
        .id(123)
        .notificationReference("notref")
        .providerId("provId")
        .registeredDocumentId("regDocId")
        .transferResponseCode("code")
        .transferResponseDescription("the code")
        .transferRetryCount(10)
        .transferStatus("stat");
  }

  public static AuditDetail buildAuditDetail() {
    AuditDetail auditDetail = new AuditDetail();
    // Ensure a difference between created and last saved dates
    auditDetail.setCreated(
        Date.from(LocalDate.of(2000, 1, 1).atStartOfDay()
            .toInstant(ZoneOffset.UTC)));
    auditDetail.setCreatedBy("CreatedBy");
    auditDetail.setLastSaved(new Date());
    auditDetail.setLastSavedBy("LastSavedBy");
    return auditDetail;
  }

  public static AuditTrail buildAuditTrail() {
    AuditTrail auditTrail = new AuditTrail();
    auditTrail.setCreated(Date.from(LocalDate.of(2000, 1, 1).atStartOfDay()
        .toInstant(ZoneOffset.UTC)));
    auditTrail.setCreatedBy("CreatedBy");
    auditTrail.setLastSaved(new Date());
    auditTrail.setLastSavedBy("LastSavedBy");
    return auditTrail;
  }

}
