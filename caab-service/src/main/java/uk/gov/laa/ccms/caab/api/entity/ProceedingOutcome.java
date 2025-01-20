package uk.gov.laa.ccms.caab.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entity to represent a proceeding outcome entry in the XXCCMS_PROCEEDING_OUTCOME TDS table.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "XXCCMS_PROCEEDING_OUTCOME", schema = "XXCCMS_PUI")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_PROCEEDING_OUTCOME_S",
    schema = "XXCCMS_PUI")
@Getter
@Setter
@RequiredArgsConstructor
public class ProceedingOutcome {

  @Id
  @GeneratedValue(generator = "XXCCMS_PROCEEDING_OUTCOME_S")
  private Long id;

  /**
   * The date of issue.
   */
  @Column(name = "DATE_OF_ISSUE")
  private Date dateOfIssue;

  /**
   * The stage end value.
   */
  @Column(name = "STAGE_END", length = 70)
  private String stageEnd;

  /**
   * The stage end display value.
   */
  @Column(name = "STAGE_END_DISPLAY_VALUE", length = 150)
  private String stageEndDisplayValue;

  /**
   * The resolution method.
   */
  @Column(name = "RESOLUTION_METHOD", length = 70)
  private String resolutionMethod;

  /**
   * The date of final work.
   */
  @Column(name = "DATE_OF_FINAL_WORK")
  private Date dateOfFinalWork;

  /**
   * The outcome result.
   */
  @Column(name = "RESULT", length = 70)
  private String result;

  /**
   * The outcome result display value.
   */
  @Column(name = "RESULT_DISPLAY_VALUE", length = 150)
  private String resultDisplayValue;

  /**
   * The result info.
   */
  @Column(name = "RESULT_INFO", length = 1000)
  private String resultInfo;

  /**
   * The alternative resolution value.
   */
  @Column(name = "ALTERNATIVE_RESOLUTION", length = 70)
  private String alternativeResolution;

  /**
   * The adr info.
   */
  @Column(name = "ADR_INFO", length = 1000)
  private String adrInfo;

  /**
   * The wider benefits.
   */
  @Column(name = "WIDER_BENEFITS", length = 70)
  private String widerBenefits;

  /**
   * The court code.
   */
  @Column(name = "COURT_CODE", length = 70)
  private String courtCode;

  /**
   * The court name.
   */
  @Column(name = "COURT_NAME", length = 70)
  private String courtName;

  /**
   * The outcome description.
   */
  @Column(name = "DESCRIPTION", length = 542)
  private String description;

  /**
   * The proceeding case id.
   */
  @Column(name = "PROCEEDING_CASE_ID", length = 50)
  private String proceedingCaseId;

  /**
   * The outcome court case number.
   */
  @Column(name = "OUTCOME_COURT_CASE_NO", length = 30)
  private String outcomeCourtCaseNo;

  /**
   * The related case outcome.
   */
  @ManyToOne
  @JoinColumn(name = "FK_PROCEEDING_CASE_OUTCOME", nullable = false)
  private CaseOutcome caseOutcome;

  /**
   * audit trail info.
   */
  @Embedded
  private AuditTrail auditTrail = new AuditTrail();
}
