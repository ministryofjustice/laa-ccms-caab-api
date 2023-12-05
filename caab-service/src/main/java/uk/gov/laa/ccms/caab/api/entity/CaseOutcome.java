package uk.gov.laa.ccms.caab.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Represents a case outcome entity associated with the "XXCCMS_CASE_OUTCOME" table.
 *
 * <p>This entity is utilized to manage and persist case outcome data
 * within the CCMS system. It makes use of the "XXCCMS_GENERATED_ID_S"
 * sequence for generating unique identifiers.</p>
 */
@Entity
@Table(name = "XXCCMS_CASE_OUTCOME")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_CASE_OUTCOME_S")
@Getter
@Setter
@RequiredArgsConstructor
public class CaseOutcome {

  @Id
  @GeneratedValue(generator = "XXCCMS_CASE_OUTCOME_S")
  private Long id;
  /**
   * audit trail info.
   */
  @Embedded
  private AuditTrail auditTrail;

  @Column(name = "LSC_CASE_REFERENCE", length = 50, nullable = false)
  private String lscCaseReference;

  @Column(name = "PRE_CERTIFICATE_COSTS", precision = 10, scale = 2)
  private BigDecimal preCertificateCosts = new BigDecimal("0.00");
  @Column(name = "LEGAL_COSTS", precision = 10, scale = 2)
  private BigDecimal legalCosts = new BigDecimal("0.00");
  @Column(name = "DISCHARGE_REASON", length = 50)
  private String dischargeReason;
  @Column(name = "DISCHARGE_CASE_IND", length = 50)
  private String dischargeCaseInd;
  @Column(name = "CLIENT_CONTINUE_IND", length = 50)
  private String clientContinueInd;
  @Column(name = "OTHER_DETAILS", length = 1000)
  private String otherDetails;
  @Column(name = "OFFICE_CODE", length = 30)
  private String officeCode;
  @Column(name = "UNIQUE_FILE_NO", length = 30)
  private String uniqueFileNo;

  @Column(name = "PROVIDER_ID", length = 19, nullable = false)
  private String providerId;

}
