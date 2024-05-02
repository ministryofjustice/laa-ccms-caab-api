package uk.gov.laa.ccms.caab.api.mapper;


import java.util.List;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.gov.laa.ccms.caab.api.entity.CaseOutcome;
import uk.gov.laa.ccms.caab.api.entity.CostAward;
import uk.gov.laa.ccms.caab.api.entity.FinancialAward;
import uk.gov.laa.ccms.caab.api.entity.LandAward;
import uk.gov.laa.ccms.caab.api.entity.LiableParty;
import uk.gov.laa.ccms.caab.api.entity.OtherAssetAward;
import uk.gov.laa.ccms.caab.api.entity.ProceedingOutcome;
import uk.gov.laa.ccms.caab.api.entity.Recovery;
import uk.gov.laa.ccms.caab.api.entity.TimeRecovery;
import uk.gov.laa.ccms.caab.model.CaseOutcomeDetail;
import uk.gov.laa.ccms.caab.model.CaseOutcomeDetails;
import uk.gov.laa.ccms.caab.model.CostAwardDetail;
import uk.gov.laa.ccms.caab.model.FinancialAwardDetail;
import uk.gov.laa.ccms.caab.model.LandAwardDetail;
import uk.gov.laa.ccms.caab.model.LiablePartyDetail;
import uk.gov.laa.ccms.caab.model.OtherAssetAwardDetail;
import uk.gov.laa.ccms.caab.model.ProceedingOutcomeDetail;
import uk.gov.laa.ccms.caab.model.RecoveryDetail;
import uk.gov.laa.ccms.caab.model.TimeRecoveryDetail;

/**
 * Interface responsible for mapping and transforming objects related
 * to the case outcome domain. It bridges the gap between the data model
 * and the service or API layers, ensuring consistent object translation.
 */

@Mapper(componentModel = "spring",
    uses = {CommonMapper.class, ApplicationMapper.class})
public interface CaseOutcomeMapper {

  /**
   * Maps a list of {@link CaseOutcome} entities to an {@link CaseOutcomeDetails} model.
   *
   * @param caseOutcomes the list of {@link CaseOutcome} entities to map
   * @return the mapped {@link CaseOutcomeDetails} model
   */
  default CaseOutcomeDetails toCaseOutcomeDetails(final List<CaseOutcome> caseOutcomes) {
    CaseOutcomeDetails caseOutcomeDetails = new CaseOutcomeDetails();
    if (caseOutcomes != null) {
      caseOutcomeDetails.setContent(caseOutcomes.stream()
          .map(this::toCaseOutcomeDetail)
          .toList());
    }
    return caseOutcomeDetails;
  }

  @Mapping(target = "caseReferenceNumber", source = "lscCaseReference")
  CaseOutcomeDetail toCaseOutcomeDetail(final CaseOutcome caseOutcome);

  @Mapping(target = "stageEnd.id", source = "stageEnd")
  @Mapping(target = "stageEnd.displayValue", source = "stageEndDisplayValue")
  @Mapping(target = "result.id", source = "result")
  @Mapping(target = "result.displayValue", source = "resultDisplayValue")
  @Mapping(target = "matterType", ignore = true)
  @Mapping(target = "proceedingType", ignore = true)
  ProceedingOutcomeDetail toProceedingOutcomeDetail(final ProceedingOutcome proceedingOutcome);

  LiablePartyDetail toLiablePartyDetail(final LiableParty liableParty);

  @Mapping(target = "conditionsOfOffer", ignore = true)
  RecoveryDetail toRecoveryDetail(final Recovery recovery);

  @Mapping(target = "costOrFinancial", ignore = true)
  @Mapping(target = "effectiveDate", ignore = true)
  @Mapping(target = "opponentsToSelect", ignore = true)
  @Mapping(target = "totalCertCostsAwarded", ignore = true)
  CostAwardDetail toCostAwardDetail(final CostAward costAward);

  @Mapping(target = "amount", ignore = true)
  @Mapping(target = "costOrFinancial", ignore = true)
  @Mapping(target = "effectiveDate", ignore = true)
  @Mapping(target = "opponentsToSelect", ignore = true)
  FinancialAwardDetail toFinancialAwardDetail(final FinancialAward financialAward);

  @Mapping(target = "costOrFinancial", ignore = true)
  @Mapping(target = "effectiveDate", ignore = true)
  @Mapping(target = "opponentsToSelect", ignore = true)
  LandAwardDetail toLandAwardDetail(final LandAward landAward);

  @Mapping(target = "costOrFinancial", ignore = true)
  @Mapping(target = "effectiveDate", ignore = true)
  @Mapping(target = "opponentsToSelect", ignore = true)
  OtherAssetAwardDetail toOtherAssetAwardDetail(final OtherAssetAward otherAssetAward);

  @InheritInverseConfiguration
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  CaseOutcome toCaseOutcome(final CaseOutcomeDetail caseOutcomeDetail);

  @InheritInverseConfiguration
  @Mapping(target = "caseOutcome", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  CostAward toCostAward(final CostAwardDetail costAwardDetail);

  @InheritInverseConfiguration
  @Mapping(target = "caseOutcome", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  FinancialAward toFinancialAward(final FinancialAwardDetail financialAwardDetail);

  @InheritInverseConfiguration
  @Mapping(target = "caseOutcome", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  LandAward toLandAward(final LandAwardDetail landAwardDetail);

  @InheritInverseConfiguration
  @Mapping(target = "caseOutcome", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  OtherAssetAward toOtherAssetAward(final OtherAssetAwardDetail otherAssetAwardDetail);

  @InheritInverseConfiguration
  @Mapping(target = "caseOutcome", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  ProceedingOutcome toProceedingOutcome(final ProceedingOutcomeDetail proceedingOutcomeDetail);

  @InheritInverseConfiguration
  @Mapping(target = "costAward", ignore = true)
  @Mapping(target = "financialAward", ignore = true)
  @Mapping(target = "landAward", ignore = true)
  @Mapping(target = "otherAssetAward", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  LiableParty toLiableParty(final LiablePartyDetail liablePartyDetail);

  @InheritInverseConfiguration
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  Recovery toRecovery(final RecoveryDetail recoverDetail);

  @InheritInverseConfiguration
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  TimeRecovery toTimeRecovery(final TimeRecoveryDetail timeRecoverDetail);
}
