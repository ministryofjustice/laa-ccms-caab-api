package uk.gov.laa.ccms.caab.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildCaseOutcome;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildCaseOutcomeDetail;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildCostAward;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildCostAwardDetail;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildFinancialAward;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildFinancialAwardDetail;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildLandAward;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildLandAwardDetail;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildLiableParty;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildLiablePartyDetail;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildOtherAssetAward;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildOtherAssetAwardDetail;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildProceedingOutcome;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildProceedingOutcomeDetail;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildRecovery;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildRecoveryDetail;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildTimeRecoveryDetail;

import java.text.ParseException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.laa.ccms.caab.api.entity.AuditTrail;
import uk.gov.laa.ccms.caab.api.entity.BaseAward;
import uk.gov.laa.ccms.caab.api.entity.CaseOutcome;
import uk.gov.laa.ccms.caab.api.entity.CostAward;
import uk.gov.laa.ccms.caab.api.entity.FinancialAward;
import uk.gov.laa.ccms.caab.api.entity.LandAward;
import uk.gov.laa.ccms.caab.api.entity.LiableParty;
import uk.gov.laa.ccms.caab.api.entity.OtherAssetAward;
import uk.gov.laa.ccms.caab.api.entity.ProceedingOutcome;
import uk.gov.laa.ccms.caab.api.entity.Recovery;
import uk.gov.laa.ccms.caab.api.entity.TimeRecovery;
import uk.gov.laa.ccms.caab.model.AuditDetail;
import uk.gov.laa.ccms.caab.model.BaseAwardDetail;
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

@ExtendWith(MockitoExtension.class)
public class CaseOutcomeMapperTest {
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private CommonMapper commonMapper;

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private ApplicationMapper applicationMapper;

    @InjectMocks
    private final CaseOutcomeMapper mapper = new CaseOutcomeMapperImpl();

    @BeforeEach
    void setup() {
    }

    @Test
    void testToCaseOutcomeDetails_null() {
        CaseOutcomeDetails caseOutcomeDetails = mapper.toCaseOutcomeDetails(null);
        assertNotNull(caseOutcomeDetails);
        assertTrue(caseOutcomeDetails.getContent().isEmpty());
    }

    @Test
    void testToCaseOutcomeDetails() {
        List<CaseOutcome> caseOutcomes = List.of(new CaseOutcome());

        CaseOutcomeDetails result = mapper.toCaseOutcomeDetails(caseOutcomes);

        assertNotNull(result);
        assertNotNull(result.getContent());
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testToCaseOutcomeDetail() {
        // Construct CaseOutcome
        CaseOutcome caseOutcome = buildCaseOutcome();

        // Convert CaseOutcome to CaseOutcomeDetail
        CaseOutcomeDetail result = mapper.toCaseOutcomeDetail(caseOutcome);

        assertNotNull(result);
        checkAuditDetail(caseOutcome.getAuditTrail(), result.getAuditTrail());
        assertEquals(caseOutcome.getClientContinueInd(), result.getClientContinueInd());

        assertNotNull(caseOutcome.getCostAwards());
        assertEquals(1, caseOutcome.getCostAwards().size());
        assertNotNull(caseOutcome.getFinancialAwards());
        assertEquals(1, caseOutcome.getFinancialAwards().size());
        assertNotNull(caseOutcome.getLandAwards());
        assertEquals(1, caseOutcome.getLandAwards().size());
        assertNotNull(caseOutcome.getOtherAssetAwards());
        assertEquals(1, caseOutcome.getOtherAssetAwards().size());
        assertNotNull(caseOutcome.getOpponents());
        assertEquals(1, caseOutcome.getOpponents().size());
        assertNotNull(caseOutcome.getProceedingOutcomes());
        assertEquals(1, caseOutcome.getProceedingOutcomes().size());

        assertEquals(caseOutcome.getDischargeCaseInd(), result.getDischargeCaseInd());
        assertEquals(caseOutcome.getDischargeReason(), result.getDischargeReason());
        assertEquals(caseOutcome.getId().intValue(), result.getId());
        assertEquals(caseOutcome.getLegalCosts(), result.getLegalCosts());
        assertEquals(caseOutcome.getLscCaseReference(), result.getCaseReferenceNumber());
        assertEquals(caseOutcome.getOfficeCode(), result.getOfficeCode());
        assertEquals(caseOutcome.getOtherDetails(), result.getOtherDetails());
        assertEquals(caseOutcome.getPreCertificateCosts(), result.getPreCertificateCosts());
        assertEquals(caseOutcome.getProviderId(), result.getProviderId());
        assertEquals(caseOutcome.getUniqueFileNo(), result.getUniqueFileNo());
    }

    @Test
    void testToCostAwardDetail() {
        CostAward costAward = buildCostAward();

        CostAwardDetail result = mapper.toCostAwardDetail(costAward);

        assertNotNull(result);
        checkBaseAwardMapping(costAward, result);

        assertEquals(costAward.getId().intValue(), result.getId());
        assertEquals(costAward.getAddressLine1(), result.getAddressLine1());
        assertEquals(costAward.getAddressLine2(), result.getAddressLine2());
        assertEquals(costAward.getAddressLine3(), result.getAddressLine3());
        assertEquals(costAward.getCertificateCostLsc(), result.getCertificateCostLsc());
        assertEquals(costAward.getCertificateCostMarket(), result.getCertificateCostMarket());
        assertEquals(costAward.getCourtAssessmentStatus(), result.getCourtAssessmentStatus());
        assertEquals(costAward.getInterestAwardedRate(), result.getInterestAwardedRate());
        assertEquals(costAward.getInterestStartDate(), result.getInterestStartDate());
        assertEquals(costAward.getOrderServedDate(), result.getOrderServedDate());
        assertEquals(costAward.getOtherDetails(), result.getOtherDetails());
        assertEquals(costAward.getPreCertificateLscCost(), result.getPreCertificateLscCost());
        assertEquals(costAward.getPreCertificateOtherCost(), result.getPreCertificateOtherCost());
        assertNotNull(costAward.getRecovery());
    }

    @Test
    void testToFinancialAwardDetail() {
        FinancialAward financialAward = buildFinancialAward();

        FinancialAwardDetail result = mapper.toFinancialAwardDetail(financialAward);

        assertNotNull(result);
        checkBaseAwardMapping(financialAward, result);

        assertEquals(financialAward.getId().intValue(), result.getId());
        assertEquals(financialAward.getAddressLine1(), result.getAddressLine1());
        assertEquals(financialAward.getAddressLine2(), result.getAddressLine2());
        assertEquals(financialAward.getAddressLine3(), result.getAddressLine3());
        assertEquals(financialAward.getAwardJustifications(), result.getAwardJustifications());
        assertEquals(financialAward.getInterimAward(), result.getInterimAward());
        assertEquals(financialAward.getOtherDetails(), result.getOtherDetails());
        assertNotNull(financialAward.getRecovery());
        assertEquals(financialAward.getStatutoryChargeExemptReason(), result.getStatutoryChargeExemptReason());
    }

    @Test
    void testToLandAwardDetail() {
        LandAward landAward = buildLandAward();

        LandAwardDetail result = mapper.toLandAwardDetail(landAward);

        assertNotNull(result);
        checkBaseAwardMapping(landAward, result);

        assertEquals(landAward.getId().intValue(), result.getId());
        assertEquals(landAward.getAddressLine1(), result.getAddressLine1());
        assertEquals(landAward.getAddressLine2(), result.getAddressLine2());
        assertEquals(landAward.getAddressLine3(), result.getAddressLine3());
        assertEquals(landAward.getAwardedPercentage(), result.getAwardedPercentage());
        assertEquals(landAward.getDisputedPercentage(), result.getDisputedPercentage());
        assertEquals(landAward.getEquity(), result.getEquity());
        assertEquals(landAward.getLandChargeRegistration(), result.getLandChargeRegistration());
        assertEquals(landAward.getMortgageAmountDue(), result.getMortgageAmountDue());
        assertEquals(landAward.getNoRecoveryDetails(), result.getNoRecoveryDetails());
        assertEquals(landAward.getRecovery(), result.getRecovery());
        assertEquals(landAward.getRecoveryOfAwardTimeRelated(), result.getRecoveryOfAwardTimeRelated());
        assertEquals(landAward.getRegistrationReference(), result.getRegistrationReference());
        assertEquals(landAward.getStatutoryChargeExemptReason(), result.getStatutoryChargeExemptReason());

        checkTimeRecoveryMapping(landAward.getTimeRecovery(), result.getTimeRecovery());

        assertEquals(landAward.getTitleNumber(), result.getTitleNumber());
        assertEquals(landAward.getValuationAmount(), result.getValuationAmount());
        assertEquals(landAward.getValuationCriteria(), result.getValuationCriteria());
        assertEquals(landAward.getValuationDate(), result.getValuationDate());
    }

    @Test
    void testToOtherAssetAwardDetail() {
        OtherAssetAward otherAssetAward = buildOtherAssetAward();

        OtherAssetAwardDetail result = mapper.toOtherAssetAwardDetail(otherAssetAward);

        assertNotNull(result);
        checkBaseAwardMapping(otherAssetAward, result);

        assertEquals(otherAssetAward.getId().intValue(), result.getId());
        assertEquals(otherAssetAward.getAwardedPercentage(), result.getAwardedPercentage());
        assertEquals(otherAssetAward.getDisputedAmount(), result.getDisputedAmount());
        assertEquals(otherAssetAward.getAwardedAmount(), result.getAwardedAmount());
        assertEquals(otherAssetAward.getDisputedPercentage(), result.getDisputedPercentage());
        assertEquals(otherAssetAward.getNoRecoveryDetails(), result.getNoRecoveryDetails());
        assertEquals(otherAssetAward.getRecoveredAmount(), result.getRecoveredAmount());
        assertEquals(otherAssetAward.getRecoveredPercentage(), result.getRecoveredPercentage());
        assertEquals(otherAssetAward.getRecovery(), result.getRecovery());
        assertEquals(otherAssetAward.getRecoveryOfAwardTimeRelated(), result.getRecoveryOfAwardTimeRelated());
        assertEquals(otherAssetAward.getStatutoryChargeExemptReason(), result.getStatutoryChargeExemptReason());

        checkTimeRecoveryMapping(otherAssetAward.getTimeRecovery(), result.getTimeRecovery());

        assertEquals(otherAssetAward.getValuationAmount(), result.getValuationAmount());
        assertEquals(otherAssetAward.getValuationCriteria(), result.getValuationCriteria());
        assertEquals(otherAssetAward.getValuationDate(), result.getValuationDate());
    }

    @Test
    void testToLiablePartyDetail() {
        LiableParty liableParty = buildLiableParty(new CostAward(), new FinancialAward(), new LandAward(), new OtherAssetAward());

        LiablePartyDetail result = mapper.toLiablePartyDetail(liableParty);

        assertNotNull(result);
        checkAuditDetail(liableParty.getAuditTrail(), result.getAuditTrail());

        assertEquals(liableParty.getId().intValue(), result.getId());
        assertEquals(liableParty.getAwardType(), result.getAwardType());
        assertNotNull(liableParty.getCostAward());
        assertNotNull(liableParty.getFinancialAward());
        assertNotNull(liableParty.getLandAward());
        assertNotNull(liableParty.getOtherAssetAward());
        assertNotNull(liableParty.getOpponent());
    }

    @Test
    void testToRecoveryDetail() {
        Recovery recovery = buildRecovery();

        RecoveryDetail result = mapper.toRecoveryDetail(recovery);

        assertNotNull(result);
        checkAuditDetail(recovery.getAuditTrail(), result.getAuditTrail());
        assertEquals(recovery.getAwardType(), result.getAwardType());
        assertEquals(recovery.getAwardAmount(), result.getAwardAmount());
        assertEquals(recovery.getClientAmountPaidToLsc(), result.getClientAmountPaidToLsc());
        assertEquals(recovery.getClientRecoveryAmount(), result.getClientRecoveryAmount());
        assertEquals(recovery.getClientRecoveryDate(), result.getClientRecoveryDate());
        assertEquals(recovery.getCourtAmountPaidToLsc(), result.getCourtAmountPaidToLsc());
        assertEquals(recovery.getCourtRecoveryAmount(), result.getCourtRecoveryAmount());
        assertEquals(recovery.getCourtRecoveryDate(), result.getCourtRecoveryDate());
        assertEquals(recovery.getDescription(), result.getDescription());
        assertEquals(recovery.getId().intValue(), result.getId());
        assertEquals(recovery.getLeaveOfCourtRequiredInd(), result.getLeaveOfCourtRequiredInd());
        assertEquals(recovery.getOfferDetails(), result.getOfferDetails());
        assertEquals(recovery.getOfferedAmount(), result.getOfferedAmount());
        assertEquals(recovery.getRecoveredAmount(), result.getRecoveredAmount());
        assertEquals(recovery.getSolicitorAmountPaidToLsc(), result.getSolicitorAmountPaidToLsc());
        assertEquals(recovery.getSolicitorRecoveryAmount(), result.getSolicitorRecoveryAmount());
        assertEquals(recovery.getSolicitorRecoveryDate(), result.getSolicitorRecoveryDate());
        assertEquals(recovery.getUnrecoveredAmount(), result.getUnrecoveredAmount());
    }

    @Test
    void testToProceedingOutcomeDetail() {
        ProceedingOutcome proceedingOutcome = buildProceedingOutcome();

        ProceedingOutcomeDetail result = mapper.toProceedingOutcomeDetail(proceedingOutcome);

        assertNotNull(result);
        checkAuditDetail(proceedingOutcome.getAuditTrail(), result.getAuditTrail());

        assertEquals(proceedingOutcome.getAdrInfo(), result.getAdrInfo());
        assertEquals(proceedingOutcome.getAlternativeResolution(), result.getAlternativeResolution());
        assertEquals(proceedingOutcome.getCourtCode(), result.getCourtCode());
        assertEquals(proceedingOutcome.getCourtName(), result.getCourtName());
        assertEquals(proceedingOutcome.getDateOfFinalWork(), result.getDateOfFinalWork());
        assertEquals(proceedingOutcome.getDateOfIssue(), result.getDateOfIssue());
        assertEquals(proceedingOutcome.getDescription(), result.getDescription());
        assertEquals(proceedingOutcome.getId().intValue(), result.getId());
        assertEquals(proceedingOutcome.getOutcomeCourtCaseNo(), result.getOutcomeCourtCaseNo());
        assertEquals(proceedingOutcome.getProceedingCaseId(), result.getProceedingCaseId());
        assertEquals(proceedingOutcome.getResolutionMethod(), result.getResolutionMethod());
        assertEquals(proceedingOutcome.getResult(), result.getResult().getId());
        assertEquals(proceedingOutcome.getResultDisplayValue(), result.getResult().getDisplayValue());
        assertEquals(proceedingOutcome.getResultInfo(), result.getResultInfo());
        assertEquals(proceedingOutcome.getStageEnd(), result.getStageEnd().getId());
        assertEquals(proceedingOutcome.getStageEndDisplayValue(), result.getStageEnd().getDisplayValue());
        assertEquals(proceedingOutcome.getWiderBenefits(), result.getWiderBenefits());
    }


    void checkBaseAwardMapping(BaseAward baseAward, BaseAwardDetail result) {
        checkAuditDetail(baseAward.getAuditTrail(), result.getAuditTrail());
        assertEquals(baseAward.getAwardAmount(), result.getAwardAmount());
        assertEquals(baseAward.getAwardCode(), result.getAwardCode());
        assertEquals(baseAward.getAwardedBy(), result.getAwardedBy());
        assertEquals(baseAward.getAwardType(), result.getAwardType());
        assertEquals(baseAward.getDateOfOrder(), result.getDateOfOrder());
        assertEquals(baseAward.getDeleteAllowed(), result.getDeleteAllowed());
        assertEquals(baseAward.getDescription(), result.getDescription());
        assertEquals(baseAward.getEbsId(), result.getEbsId());
        assertEquals(baseAward.getUpdateAllowed(), result.getUpdateAllowed());
        assertNotNull(result.getLiableParties());
        assertEquals(1, result.getLiableParties().size());
    }

   void checkTimeRecoveryMapping(TimeRecovery timeRecovery,
        TimeRecoveryDetail result) {
        assertNotNull(timeRecovery);
        checkAuditDetail(timeRecovery.getAuditTrail(), result.getAuditTrail());
        assertEquals(timeRecovery.getAwardAmount(), result.getAwardAmount());
        assertEquals(timeRecovery.getAwardType(), result.getAwardType());
        assertEquals(timeRecovery.getDescription(), result.getDescription());
        assertEquals(timeRecovery.getEffectiveDate(), result.getEffectiveDate());
        assertEquals(timeRecovery.getId().intValue(), result.getId());
        assertEquals(timeRecovery.getTimeRelatedRecoveryDetails(), result.getTimeRelatedRecoveryDetails());
        assertEquals(timeRecovery.getTriggeringEvent(), result.getTriggeringEvent());
    }

    void checkAuditDetail(AuditTrail auditTrail, AuditDetail result) {
        assertNotNull(result);
        assertEquals(auditTrail.getCreated(), result.getCreated());
        assertEquals(auditTrail.getCreatedBy(), result.getCreatedBy());
        assertEquals(auditTrail.getLastSaved(), result.getLastSaved());
        assertEquals(auditTrail.getLastSavedBy(), result.getLastSavedBy());
    }

    void checkAuditTrail(AuditTrail result) {
        assertNotNull(result);
        assertNull(result.getCreated());
        assertNull(result.getCreatedBy());
        assertNull(result.getLastSaved());
        assertNull(result.getLastSavedBy());
    }

    @Test
    void testToCaseOutcome() {
        CaseOutcomeDetail caseOutcomeDetail = buildCaseOutcomeDetail();

        CaseOutcome result = mapper.toCaseOutcome(caseOutcomeDetail);

        assertNotNull(result);
        assertNull(result.getId());
        checkAuditTrail(result.getAuditTrail());

        assertEquals(caseOutcomeDetail.getCaseReferenceNumber(), result.getLscCaseReference());
        assertEquals(caseOutcomeDetail.getClientContinueInd(), result.getClientContinueInd());

        assertNotNull(caseOutcomeDetail.getCostAwards());
        assertEquals(1, result.getCostAwards().size());
        assertNotNull(caseOutcomeDetail.getFinancialAwards());
        assertEquals(1, result.getFinancialAwards().size());
        assertNotNull(caseOutcomeDetail.getLandAwards());
        assertEquals(1, result.getLandAwards().size());
        assertNotNull(caseOutcomeDetail.getOtherAssetAwards());
        assertEquals(1, result.getOtherAssetAwards().size());
        assertNotNull(caseOutcomeDetail.getOpponents());
        assertEquals(1, result.getOpponents().size());
        assertNotNull(caseOutcomeDetail.getProceedingOutcomes());
        assertEquals(1, result.getProceedingOutcomes().size());

        assertEquals(caseOutcomeDetail.getDischargeCaseInd(), result.getDischargeCaseInd());
        assertEquals(caseOutcomeDetail.getDischargeReason(), result.getDischargeReason());
        assertEquals(caseOutcomeDetail.getLegalCosts(), result.getLegalCosts());
        assertEquals(caseOutcomeDetail.getOfficeCode(), result.getOfficeCode());
        assertEquals(caseOutcomeDetail.getOtherDetails(), result.getOtherDetails());
        assertEquals(caseOutcomeDetail.getPreCertificateCosts(), result.getPreCertificateCosts());
        assertEquals(caseOutcomeDetail.getProviderId(), result.getProviderId());
        assertEquals(caseOutcomeDetail.getUniqueFileNo(), result.getUniqueFileNo());
    }

    @Test
    void testToCostAward() {
        CostAwardDetail award = buildCostAwardDetail();

        CostAward result = mapper.toCostAward(award);

        assertNotNull(result);
        assertNull(result.getId());
        checkAuditTrail(result.getAuditTrail());

        assertEquals(award.getAddressLine1(), result.getAddressLine1());
        assertEquals(award.getAddressLine2(), result.getAddressLine2());
        assertEquals(award.getAddressLine3(), result.getAddressLine3());
        assertEquals(award.getAwardAmount(), result.getAwardAmount());
        assertEquals(award.getAwardCode(), result.getAwardCode());
        assertEquals(award.getAwardedBy(), result.getAwardedBy());
        assertEquals(award.getAwardType(), result.getAwardType());
        assertEquals(award.getCertificateCostLsc(), result.getCertificateCostLsc());
        assertEquals(award.getCertificateCostMarket(), result.getCertificateCostMarket());
        assertEquals(award.getCourtAssessmentStatus(), result.getCourtAssessmentStatus());
        assertEquals(award.getDateOfOrder(), result.getDateOfOrder());
        assertEquals(award.getDeleteAllowed(), result.getDeleteAllowed());
        assertEquals(award.getDescription(), result.getDescription());
        assertEquals(award.getEbsId(), result.getEbsId());
        assertEquals(award.getInterestAwardedRate(), result.getInterestAwardedRate());
        assertEquals(award.getInterestStartDate(), result.getInterestStartDate());

        assertNotNull(result.getLiableParties());
        assertEquals(1, result.getLiableParties().size());

        assertEquals(award.getOrderServedDate(), result.getOrderServedDate());
        assertEquals(award.getOtherDetails(), result.getOtherDetails());
        assertEquals(award.getPreCertificateLscCost(), result.getPreCertificateLscCost());
        assertEquals(award.getPreCertificateOtherCost(), result.getPreCertificateOtherCost());

        assertNotNull(result.getRecovery());

        assertEquals(award.getUpdateAllowed(), result.getUpdateAllowed());
    }

    @Test
    void testToFinancialAward() {
        FinancialAwardDetail award = buildFinancialAwardDetail();

        FinancialAward result = mapper.toFinancialAward(award);

        assertNotNull(result);
        assertNull(result.getId());
        checkAuditTrail(result.getAuditTrail());

        assertEquals(award.getAddressLine1(), result.getAddressLine1());
        assertEquals(award.getAddressLine2(), result.getAddressLine2());
        assertEquals(award.getAddressLine3(), result.getAddressLine3());
        assertEquals(award.getAwardAmount(), result.getAwardAmount());
        assertEquals(award.getAwardCode(), result.getAwardCode());
        assertEquals(award.getAwardedBy(), result.getAwardedBy());
        assertEquals(award.getAwardType(), result.getAwardType());
        assertEquals(award.getAwardJustifications(), result.getAwardJustifications());
        assertEquals(award.getDateOfOrder(), result.getDateOfOrder());
        assertEquals(award.getDeleteAllowed(), result.getDeleteAllowed());
        assertEquals(award.getDescription(), result.getDescription());
        assertEquals(award.getEbsId(), result.getEbsId());
        assertEquals(award.getInterimAward(), result.getInterimAward());

        assertNotNull(result.getLiableParties());
        assertEquals(1, result.getLiableParties().size());

        assertEquals(award.getOrderServedDate(), result.getOrderServedDate());
        assertEquals(award.getOtherDetails(), result.getOtherDetails());

        assertNotNull(result.getRecovery());

        assertEquals(award.getStatutoryChargeExemptReason(), result.getStatutoryChargeExemptReason());

        assertEquals(award.getUpdateAllowed(), result.getUpdateAllowed());
    }

    @Test
    void testToLandAward() {
        LandAwardDetail award = buildLandAwardDetail();

        LandAward result = mapper.toLandAward(award);

        assertNotNull(result);
        assertNull(result.getId());
        checkAuditTrail(result.getAuditTrail());

        assertEquals(award.getAddressLine1(), result.getAddressLine1());
        assertEquals(award.getAddressLine2(), result.getAddressLine2());
        assertEquals(award.getAddressLine3(), result.getAddressLine3());
        assertEquals(award.getAwardAmount(), result.getAwardAmount());
        assertEquals(award.getAwardCode(), result.getAwardCode());
        assertEquals(award.getAwardedBy(), result.getAwardedBy());
        assertEquals(award.getAwardType(), result.getAwardType());
        assertEquals(award.getAwardedPercentage(), result.getAwardedPercentage());
        assertEquals(award.getDateOfOrder(), result.getDateOfOrder());
        assertEquals(award.getDeleteAllowed(), result.getDeleteAllowed());
        assertEquals(award.getDescription(), result.getDescription());
        assertEquals(award.getDisputedPercentage(), result.getDisputedPercentage());
        assertEquals(award.getEbsId(), result.getEbsId());
        assertEquals(award.getEquity(), result.getEquity());
        assertEquals(award.getLandChargeRegistration(), result.getLandChargeRegistration());

        assertNotNull(result.getLiableParties());
        assertEquals(1, result.getLiableParties().size());

        assertEquals(award.getMortgageAmountDue(), result.getMortgageAmountDue());
        assertEquals(award.getNoRecoveryDetails(), result.getNoRecoveryDetails());
        assertEquals(award.getRecovery(), result.getRecovery());
        assertEquals(award.getRecoveryOfAwardTimeRelated(), result.getRecoveryOfAwardTimeRelated());
        assertEquals(award.getRegistrationReference(), result.getRegistrationReference());
        assertEquals(award.getStatutoryChargeExemptReason(), result.getStatutoryChargeExemptReason());

        assertNotNull(result.getTimeRecovery());

        assertEquals(award.getTitleNumber(), result.getTitleNumber());
        assertEquals(award.getUpdateAllowed(), result.getUpdateAllowed());
        assertEquals(award.getValuationAmount(), result.getValuationAmount());
        assertEquals(award.getValuationCriteria(), result.getValuationCriteria());
        assertEquals(award.getValuationDate(), result.getValuationDate());
    }

    @Test
    void testToOtherAssetAward() {
        OtherAssetAwardDetail award = buildOtherAssetAwardDetail();

        OtherAssetAward result = mapper.toOtherAssetAward(award);

        assertNotNull(result);
        assertNull(result.getId());
        checkAuditTrail(result.getAuditTrail());

        assertEquals(award.getAwardAmount(), result.getAwardAmount());
        assertEquals(award.getAwardCode(), result.getAwardCode());
        assertEquals(award.getAwardedBy(), result.getAwardedBy());
        assertEquals(award.getAwardType(), result.getAwardType());
        assertEquals(award.getAwardedPercentage(), result.getAwardedPercentage());
        assertEquals(award.getDateOfOrder(), result.getDateOfOrder());
        assertEquals(award.getDeleteAllowed(), result.getDeleteAllowed());
        assertEquals(award.getDescription(), result.getDescription());
        assertEquals(award.getDisputedPercentage(), result.getDisputedPercentage());
        assertEquals(award.getEbsId(), result.getEbsId());
        assertEquals(award.getDisputedAmount(), result.getDisputedAmount());

        assertNotNull(result.getLiableParties());
        assertEquals(1, result.getLiableParties().size());

        assertEquals(award.getNoRecoveryDetails(), result.getNoRecoveryDetails());
        assertEquals(award.getRecovery(), result.getRecovery());
        assertEquals(award.getRecoveryOfAwardTimeRelated(), result.getRecoveryOfAwardTimeRelated());
        assertEquals(award.getStatutoryChargeExemptReason(), result.getStatutoryChargeExemptReason());

        assertNotNull(result.getTimeRecovery());

        assertEquals(award.getUpdateAllowed(), result.getUpdateAllowed());
        assertEquals(award.getValuationAmount(), result.getValuationAmount());
        assertEquals(award.getValuationCriteria(), result.getValuationCriteria());
        assertEquals(award.getValuationDate(), result.getValuationDate());
    }

    @Test
    void testToRecovery() {
        RecoveryDetail recoveryDetail = buildRecoveryDetail();

        Recovery result = mapper.toRecovery(recoveryDetail);

        assertNotNull(result);
        assertNull(result.getId());
        checkAuditTrail(result.getAuditTrail());

        assertEquals(recoveryDetail.getAwardAmount(), result.getAwardAmount());
        assertEquals(recoveryDetail.getAwardType(), result.getAwardType());
        assertEquals(recoveryDetail.getClientAmountPaidToLsc(), result.getClientAmountPaidToLsc());
        assertEquals(recoveryDetail.getClientRecoveryAmount(), result.getClientRecoveryAmount());
        assertEquals(recoveryDetail.getClientRecoveryDate(), result.getClientRecoveryDate());
        assertEquals(recoveryDetail.getCourtAmountPaidToLsc(), result.getCourtAmountPaidToLsc());
        assertEquals(recoveryDetail.getCourtRecoveryAmount(), result.getCourtRecoveryAmount());
        assertEquals(recoveryDetail.getCourtRecoveryDate(), result.getCourtRecoveryDate());
        assertEquals(recoveryDetail.getCourtAmountPaidToLsc(), result.getCourtAmountPaidToLsc());
        assertEquals(recoveryDetail.getCourtAmountPaidToLsc(), result.getCourtAmountPaidToLsc());
        assertEquals(recoveryDetail.getDescription(), result.getDescription());
        assertEquals(recoveryDetail.getLeaveOfCourtRequiredInd(), result.getLeaveOfCourtRequiredInd());
        assertEquals(recoveryDetail.getOfferDetails(), result.getOfferDetails());
        assertEquals(recoveryDetail.getOfferedAmount(), result.getOfferedAmount());
        assertEquals(recoveryDetail.getRecoveredAmount(), result.getRecoveredAmount());
        assertEquals(recoveryDetail.getSolicitorAmountPaidToLsc(), result.getSolicitorAmountPaidToLsc());
        assertEquals(recoveryDetail.getSolicitorRecoveryAmount(), result.getSolicitorRecoveryAmount());
        assertEquals(recoveryDetail.getSolicitorRecoveryDate(), result.getSolicitorRecoveryDate());
        assertEquals(recoveryDetail.getUnrecoveredAmount(), result.getUnrecoveredAmount());
    }

    @Test
    void testToTimeRecovery() {
        TimeRecoveryDetail timeRecoveryDetail = buildTimeRecoveryDetail();

        TimeRecovery result = mapper.toTimeRecovery(timeRecoveryDetail);

        assertNotNull(result);
        assertNull(result.getId());
        checkAuditTrail(result.getAuditTrail());

        assertEquals(timeRecoveryDetail.getAwardAmount(), result.getAwardAmount());
        assertEquals(timeRecoveryDetail.getAwardType(), result.getAwardType());
        assertEquals(timeRecoveryDetail.getDescription(), result.getDescription());
        assertEquals(timeRecoveryDetail.getEffectiveDate(), result.getEffectiveDate());
        assertEquals(timeRecoveryDetail.getTimeRelatedRecoveryDetails(), result.getTimeRelatedRecoveryDetails());
        assertEquals(timeRecoveryDetail.getTriggeringEvent(), result.getTriggeringEvent());
    }

    @Test
    void testToLiableParty() {
        LiablePartyDetail liablePartyDetail = buildLiablePartyDetail();

        LiableParty result = mapper.toLiableParty(liablePartyDetail);

        assertNotNull(result);
        assertNull(result.getId());
        checkAuditTrail(result.getAuditTrail());

        assertEquals(liablePartyDetail.getAwardType(), result.getAwardType());
        assertNotNull(liablePartyDetail.getOpponent());
    }

    @Test
    void testToProceedingOutcome() {
        ProceedingOutcomeDetail proceedingOutcomeDetail = buildProceedingOutcomeDetail();

        ProceedingOutcome result = mapper.toProceedingOutcome(proceedingOutcomeDetail);

        assertNotNull(result);
        assertNull(result.getId());
        checkAuditTrail(result.getAuditTrail());

        assertEquals(proceedingOutcomeDetail.getAdrInfo(), result.getAdrInfo());
        assertEquals(proceedingOutcomeDetail.getAlternativeResolution(), result.getAlternativeResolution());
        assertEquals(proceedingOutcomeDetail.getCourtCode(), result.getCourtCode());
        assertEquals(proceedingOutcomeDetail.getCourtName(), result.getCourtName());
        assertEquals(proceedingOutcomeDetail.getDateOfFinalWork(), result.getDateOfFinalWork());
        assertEquals(proceedingOutcomeDetail.getDateOfIssue(), result.getDateOfIssue());
        assertEquals(proceedingOutcomeDetail.getDescription(), result.getDescription());
        assertEquals(proceedingOutcomeDetail.getOutcomeCourtCaseNo(), result.getOutcomeCourtCaseNo());
        assertEquals(proceedingOutcomeDetail.getProceedingCaseId(), result.getProceedingCaseId());
        assertEquals(proceedingOutcomeDetail.getResolutionMethod(), result.getResolutionMethod());
        assertEquals(proceedingOutcomeDetail.getResult().getId(), result.getResult());
        assertEquals(proceedingOutcomeDetail.getResult().getDisplayValue(), result.getResultDisplayValue());
        assertEquals(proceedingOutcomeDetail.getResultInfo(), result.getResultInfo());
        assertEquals(proceedingOutcomeDetail.getStageEnd().getId(), result.getStageEnd());
        assertEquals(proceedingOutcomeDetail.getStageEnd().getDisplayValue(), result.getStageEndDisplayValue());
        assertEquals(proceedingOutcomeDetail.getWiderBenefits(), result.getWiderBenefits());
    }

}
