package uk.gov.laa.ccms.caab.api.mapper;


import java.util.List;
import java.util.Optional;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uk.gov.laa.ccms.caab.api.entity.CaseOutcome;
import uk.gov.laa.ccms.caab.api.entity.CostAward;
import uk.gov.laa.ccms.caab.api.entity.FinancialAward;
import uk.gov.laa.ccms.caab.api.entity.LandAward;
import uk.gov.laa.ccms.caab.api.entity.LiableParty;
import uk.gov.laa.ccms.caab.api.entity.Opponent;
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
  @Mapping(target = "opponentIds", source = "opponents")
  CaseOutcomeDetail toCaseOutcomeDetail(final CaseOutcome caseOutcome);

  /**
   * Map a List of Opponent entities to a List of opponent ids.
   *
   * @param opponents - the opponent entities
   * @return List of Integer opponent ids.
   */
  default List<Integer> toOpponentIds(final List<Opponent> opponents) {
    return Optional.ofNullable(opponents)
        .map(o -> o.stream()
            .map(opponent -> Optional.ofNullable(opponent.getId())
                .map(Long::intValue)
                .orElse(null)).toList())
        .orElse(null);
  }

  @Mapping(target = "stageEnd.id", source = "stageEnd")
  @Mapping(target = "stageEnd.displayValue", source = "stageEndDisplayValue")
  @Mapping(target = "result.id", source = "result")
  @Mapping(target = "result.displayValue", source = "resultDisplayValue")
  @Mapping(target = "matterType", ignore = true)
  @Mapping(target = "proceedingType", ignore = true)
  ProceedingOutcomeDetail toProceedingOutcomeDetail(final ProceedingOutcome proceedingOutcome);

  @Mapping(target = "opponentId", source = "opponent.id")
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
  @Mapping(target = "opponents", source = "opponentIds")
  CaseOutcome toCaseOutcome(final CaseOutcomeDetail caseOutcomeDetail,
      @Context final List<Opponent> allOpponents);

  /**
   * Finalise the CaseOutcome entity and its children with all required
   * relationships.
   *
   * @param caseOutcome - the case outcome.
   */
  @AfterMapping
  default void finaliseCaseOutcome(@MappingTarget final CaseOutcome caseOutcome) {
    Optional.ofNullable(caseOutcome.getOpponents())
        .ifPresent(opponents -> opponents.forEach(
            opponent -> opponent.setCaseOutcome(caseOutcome)));

    Optional.ofNullable(caseOutcome.getCostAwards())
        .ifPresent(awards -> awards.forEach(
            award -> {
              award.setCaseOutcome(caseOutcome);

              Optional.ofNullable(award.getLiableParties())
                  .ifPresent(liableParties -> liableParties.forEach(
                      liableParty -> liableParty.setCostAward(award)));
            }));

    Optional.ofNullable(caseOutcome.getFinancialAwards())
        .ifPresent(awards -> awards.forEach(
            award -> {
              award.setCaseOutcome(caseOutcome);

              Optional.ofNullable(award.getLiableParties())
                  .ifPresent(liableParties -> liableParties.forEach(
                      liableParty -> liableParty.setFinancialAward(award)));
            }));

    Optional.ofNullable(caseOutcome.getLandAwards())
        .ifPresent(awards -> awards.forEach(
            award -> {
              award.setCaseOutcome(caseOutcome);

              Optional.ofNullable(award.getLiableParties())
                  .ifPresent(liableParties -> liableParties.forEach(
                      liableParty -> liableParty.setLandAward(award)));
            }));

    Optional.ofNullable(caseOutcome.getOtherAssetAwards())
        .ifPresent(awards -> awards.forEach(
            award -> {
              award.setCaseOutcome(caseOutcome);

              Optional.ofNullable(award.getLiableParties())
                  .ifPresent(liableParties -> liableParties.forEach(
                      liableParty -> liableParty.setOtherAssetAward(award)));
            }));

    Optional.ofNullable(caseOutcome.getProceedingOutcomes())
        .ifPresent(proceedingOutcomes -> proceedingOutcomes.forEach(
            proceedingOutcome -> proceedingOutcome.setCaseOutcome(caseOutcome)));
  }

  @InheritInverseConfiguration
  @Mapping(target = "caseOutcome", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  CostAward toCostAward(final CostAwardDetail costAwardDetail,
      @Context final List<Opponent> allOpponents);

  @InheritInverseConfiguration
  @Mapping(target = "caseOutcome", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  FinancialAward toFinancialAward(final FinancialAwardDetail financialAwardDetail,
      @Context final List<Opponent> allOpponents);

  @InheritInverseConfiguration
  @Mapping(target = "caseOutcome", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  LandAward toLandAward(final LandAwardDetail landAwardDetail,
      @Context final List<Opponent> allOpponents);

  @InheritInverseConfiguration
  @Mapping(target = "caseOutcome", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  OtherAssetAward toOtherAssetAward(final OtherAssetAwardDetail otherAssetAwardDetail,
      @Context final List<Opponent> allOpponents);

  @InheritInverseConfiguration
  @Mapping(target = "caseOutcome", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  ProceedingOutcome toProceedingOutcome(final ProceedingOutcomeDetail proceedingOutcomeDetail);

  @InheritInverseConfiguration
  @Mapping(target = "opponent", source = "opponentId")
  @Mapping(target = "costAward", ignore = true)
  @Mapping(target = "financialAward", ignore = true)
  @Mapping(target = "landAward", ignore = true)
  @Mapping(target = "otherAssetAward", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  LiableParty toLiableParty(final LiablePartyDetail liablePartyDetail,
      @Context final List<Opponent> allOpponents);

  @InheritInverseConfiguration
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  Recovery toRecovery(final RecoveryDetail recoverDetail);

  @InheritInverseConfiguration
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  TimeRecovery toTimeRecovery(final TimeRecoveryDetail timeRecoverDetail);

  /**
   * Find the Opponent entity with the specified id.
   *
   * @param opponentId - the id to find.
   * @param allOpponents - the list of all Opponents.
   * @return the matching Opponent or null.
   */
  default Opponent findOpponent(final Long opponentId,
      @Context final List<Opponent> allOpponents) {
    return allOpponents.stream()
        .filter(opponent -> opponent.getId().equals(opponentId))
        .findFirst()
        .orElse(null);
  }
}
