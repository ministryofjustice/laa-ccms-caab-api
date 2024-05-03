package uk.gov.laa.ccms.caab.api.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import uk.gov.laa.ccms.caab.api.entity.Address;
import uk.gov.laa.ccms.caab.api.entity.Application;
import uk.gov.laa.ccms.caab.api.entity.AuditTrail;
import uk.gov.laa.ccms.caab.api.entity.BaseAward;
import uk.gov.laa.ccms.caab.api.entity.CaseOutcome;
import uk.gov.laa.ccms.caab.api.entity.CostAward;
import uk.gov.laa.ccms.caab.api.entity.EvidenceDocument;
import uk.gov.laa.ccms.caab.api.entity.FinancialAward;
import uk.gov.laa.ccms.caab.api.entity.LandAward;
import uk.gov.laa.ccms.caab.api.entity.LiableParty;
import uk.gov.laa.ccms.caab.api.entity.Opponent;
import uk.gov.laa.ccms.caab.api.entity.OtherAssetAward;
import uk.gov.laa.ccms.caab.api.entity.ProceedingOutcome;
import uk.gov.laa.ccms.caab.api.entity.Recovery;
import uk.gov.laa.ccms.caab.api.entity.TimeRecovery;
import uk.gov.laa.ccms.caab.model.AddressDetail;
import uk.gov.laa.ccms.caab.model.AuditDetail;
import uk.gov.laa.ccms.caab.model.CaseOutcomeDetail;
import uk.gov.laa.ccms.caab.model.CostAwardDetail;
import uk.gov.laa.ccms.caab.model.EvidenceDocumentDetail;
import uk.gov.laa.ccms.caab.model.FinancialAwardDetail;
import uk.gov.laa.ccms.caab.model.LandAwardDetail;
import uk.gov.laa.ccms.caab.model.LiablePartyDetail;
import uk.gov.laa.ccms.caab.model.OpponentDetail;
import uk.gov.laa.ccms.caab.model.OtherAssetAwardDetail;
import uk.gov.laa.ccms.caab.model.ProceedingOutcomeDetail;
import uk.gov.laa.ccms.caab.model.RecoveryDetail;
import uk.gov.laa.ccms.caab.model.StringDisplayValue;
import uk.gov.laa.ccms.caab.model.TimeRecoveryDetail;

public class ModelUtils {

  public static Address buildAddress() {
    Address address = new Address();
    address.setPostCode("InitialPostcode");
    address.setHouseNameNumber("InitialHouseNameNumber");
    address.setNoFixedAbode(true);
    address.setAddressLine1("InitialAddressLine1");
    address.setAddressLine2("InitialAddressLine2");
    address.setCity("InitialCity");
    address.setCounty("InitialCounty");
    address.setCountry("InitialCountry");
    address.setCareOf("InitialCareOf");
    address.setPreferredAddress("InitialPreferredAddress");
    return address;
  }

  public static Opponent buildOpponent(Long id, Date date) {
    Opponent opponent = new Opponent();
    opponent.setId(id);
    opponent.setAmendment(true);
    opponent.setApplication(new Application());
    opponent.setAppMode(false);
    opponent.setAssessedAssets(BigDecimal.TEN);
    opponent.setAssessedIncome(BigDecimal.ONE);
    opponent.setAssessedIncomeFrequency("freq");
    opponent.setAssessmentDate(date);
    opponent.setAuditTrail(new AuditTrail());
    opponent.setAward(true);
    opponent.setCertificateNumber("certnum");
    opponent.setConfirmed(false);
    opponent.setContactNameRole("namerole");
    opponent.setCourtOrderedMeansAssessment(true);
    opponent.setCurrentlyTrading(false);
    opponent.setDateOfBirth(date);
    opponent.setDeleteInd(true);
    opponent.setEbsId("ebs1");
    opponent.setEmailAddress("emailadd");
    opponent.setEmployerAddress("empaddr");
    opponent.setEmployerName("name");
    opponent.setEmploymentStatus("empStat");
    opponent.setFaxNumber("faxnum");
    opponent.setFirstName("first");
    opponent.setLegalAided(true);
    opponent.setMiddleNames("middle");
    opponent.setNationalInsuranceNumber("nino");
    opponent.setOrganisationName("org");
    opponent.setOrganisationType("orgtype");
    opponent.setOtherInformation("otherinf");
    opponent.setCaseOutcome(new CaseOutcome());
    opponent.setPublicFundingApplied(false);
    opponent.setRelationshipToCase("rel2case");
    opponent.setRelationshipToClient("rel2client");
    opponent.setSharedInd(true);
    opponent.setSurname("sur");
    opponent.setTelephoneHome("telhome");
    opponent.setTelephoneMobile("telmob");
    opponent.setTelephoneWork("telwork");
    opponent.setTitle("ttl");
    opponent.setType("thetype");

    return opponent;
  }

  public static OpponentDetail buildOpponentModel(java.util.Date date) {
    return new OpponentDetail()
        .address(new AddressDetail().addressLine1("add1"))
        .amendment(Boolean.TRUE)
        .appMode(Boolean.FALSE)
        .assessedAssets(BigDecimal.TEN)
        .assessedIncome(BigDecimal.ONE)
        .assessedIncomeFrequency("freq")
        .assessmentDate(date)
        .auditTrail(new AuditDetail())
        .award(Boolean.TRUE)
        .certificateNumber("cert")
        .confirmed(Boolean.FALSE)
        .contactNameRole("conNameRole")
        .courtOrderedMeansAssessment(Boolean.TRUE)
        .currentlyTrading(Boolean.TRUE)
        .dateOfBirth(date)
        .deleteInd(Boolean.FALSE)
        .displayAddress("address 1")
        .displayName("disp name")
        .ebsId("ebsid")
        .emailAddress("emailAdd")
        .employerName("empName")
        .employerAddress("empAddr")
        .employmentStatus("empSt")
        .faxNumber("fax")
        .firstName("firstname")
        .id(2222)
        .legalAided(Boolean.TRUE)
        .middleNames("midnames")
        .nationalInsuranceNumber("nino")
        .organisationName("orgName")
        .organisationType("orgid")
        .otherInformation("otherInf")
        .partyId("party")
        .publicFundingApplied(Boolean.TRUE)
        .relationshipToCase("relToCase")
        .relationshipToClient("relToClient")
        .sharedInd(Boolean.TRUE)
        .surname("surname")
        .telephoneHome("telHome")
        .telephoneMobile("telMob")
        .telephoneWork("telWork")
        .title("thetitle")
        .type("thetype");
  }

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

  public static CaseOutcome buildCaseOutcome() {
    CaseOutcome caseOutcome = new CaseOutcome();
    caseOutcome.setAuditTrail(buildAuditTrail());
    caseOutcome.setClientContinueInd(Boolean.TRUE);
    caseOutcome.setCostAwards(List.of(buildCostAward()));
    caseOutcome.setDischargeCaseInd(Boolean.FALSE);
    caseOutcome.setDischargeReason("reason");
    caseOutcome.setFinancialAwards(List.of(buildFinancialAward()));
    caseOutcome.setId(937L);
    caseOutcome.setLandAwards(List.of(buildLandAward()));
    caseOutcome.setLegalCosts(BigDecimal.TEN);
    caseOutcome.setLscCaseReference("caseref");
    caseOutcome.setOfficeCode("offcode");
    caseOutcome.setOpponents(List.of(new Opponent()));
    caseOutcome.setOtherAssetAwards(List.of(buildOtherAssetAward()));
    caseOutcome.setOtherDetails("otherdets");
    caseOutcome.setPreCertificateCosts(BigDecimal.TEN);
    caseOutcome.setProceedingOutcomes(List.of(buildProceedingOutcome()));
    caseOutcome.setProviderId("provId");
    caseOutcome.setUniqueFileNo("fileno");

    return caseOutcome;
  }

  public static ProceedingOutcome buildProceedingOutcome() {
    ProceedingOutcome proceedingOutcome = new ProceedingOutcome();
    proceedingOutcome.setAdrInfo("adrInf");
    proceedingOutcome.setAlternativeResolution("altres");
    proceedingOutcome.setAuditTrail(buildAuditTrail());
    proceedingOutcome.setCourtCode("code");
    proceedingOutcome.setCourtName("name");
    proceedingOutcome.setDateOfFinalWork(new Date());
    proceedingOutcome.setDateOfIssue(new Date());
    proceedingOutcome.setDescription("descr");
    proceedingOutcome.setId(8276L);
    proceedingOutcome.setOutcomeCourtCaseNo("num");
    proceedingOutcome.setProceedingCaseId("caseid");
    proceedingOutcome.setResolutionMethod("resmeth");
    proceedingOutcome.setResult("res");
    proceedingOutcome.setResultDisplayValue("result");
    proceedingOutcome.setResultInfo("info");
    proceedingOutcome.setStageEnd("stageend");
    proceedingOutcome.setStageEndDisplayValue("stage end");
    proceedingOutcome.setWiderBenefits("wider");

    return proceedingOutcome;
  }

  public static Recovery buildRecovery() {
    Recovery recovery = new Recovery();
    recovery.setAuditTrail(buildAuditTrail());
    recovery.setAwardAmount(BigDecimal.TEN);
    recovery.setAwardType("type");
    recovery.setClientAmountPaidToLsc(BigDecimal.ONE);
    recovery.setClientRecoveryAmount(BigDecimal.TEN);
    recovery.setClientRecoveryDate(new Date());
    recovery.setCourtAmountPaidToLsc(BigDecimal.ZERO);
    recovery.setCourtRecoveryAmount(BigDecimal.ONE);
    recovery.setCourtRecoveryDate(new Date());
    recovery.setDescription("descr");
    recovery.setId(953L);
    recovery.setLeaveOfCourtRequiredInd(Boolean.FALSE);
    recovery.setOfferDetails("detail");
    recovery.setOfferedAmount(BigDecimal.TEN);
    recovery.setRecoveredAmount(BigDecimal.ONE);
    recovery.setSolicitorAmountPaidToLsc(BigDecimal.TEN);
    recovery.setSolicitorRecoveryAmount(BigDecimal.ONE);
    recovery.setSolicitorRecoveryDate(new Date());
    recovery.setUnrecoveredAmount(BigDecimal.TEN);

    return recovery;
  }

  public static TimeRecovery buildTimeRecovery() {
    TimeRecovery timeRecovery = new TimeRecovery();
    timeRecovery.setAuditTrail(buildAuditTrail());
    timeRecovery.setAwardAmount(BigDecimal.TEN);
    timeRecovery.setAwardType("type");
    timeRecovery.setDescription("descr");
    timeRecovery.setEffectiveDate(new Date());
    timeRecovery.setId(937L);
    timeRecovery.setTimeRelatedRecoveryDetails("time rel details");
    timeRecovery.setTriggeringEvent("event");

    return timeRecovery;
  }

  public static <T extends BaseAward> void populateBaseAward(T award) {
    award.setAuditTrail(buildAuditTrail());
    award.setAwardAmount(BigDecimal.TEN);
    award.setAwardCode("awcode");
    award.setAwardedBy("awBy");
    award.setAwardType("type");
    award.setDateOfOrder(new Date());
    award.setDeleteAllowed(Boolean.TRUE);
    award.setDescription("descr");
    award.setEbsId("ebsid");
    award.setUpdateAllowed(Boolean.TRUE);
  }

  public static CostAward buildCostAward() {
    CostAward award = new CostAward();
    populateBaseAward(award);

    award.setAddressLine1("add1");
    award.setAddressLine2("add2");
    award.setAddressLine3("add3");
    award.setCertificateCostLsc(BigDecimal.ONE);
    award.setCertificateCostMarket(BigDecimal.ZERO);
    award.setCourtAssessmentStatus("stat");
    award.setId(456L);
    award.setInterestAwardedRate(BigDecimal.TEN);
    award.setInterestStartDate(new Date());
    award.setLiableParties(List.of(buildLiableParty(award, null, null, null)));
    award.setOrderServedDate(new Date());
    award.setOtherDetails("other dets");
    award.setPreCertificateLscCost(BigDecimal.TEN);
    award.setPreCertificateOtherCost(BigDecimal.ONE);
    award.setRecovery(buildRecovery());

    return award;
  }

  public static FinancialAward buildFinancialAward() {
    FinancialAward award = new FinancialAward();
    populateBaseAward(award);

    award.setAddressLine1("add1");
    award.setAddressLine2("add2");
    award.setAddressLine3("add3");
    award.setAwardJustifications("just");
    award.setId(754L);
    award.setInterimAward("interim");
    award.setLiableParties(List.of(buildLiableParty(null, award, null, null)));
    award.setOrderServedDate(new Date());
    award.setOtherDetails("dets");
    award.setRecovery(buildRecovery());
    award.setStatutoryChargeExemptReason("reason");

    return award;
  }

  public static LandAward buildLandAward() {
    LandAward award = new LandAward();
    populateBaseAward(award);

    award.setAddressLine1("add1");
    award.setAddressLine2("add2");
    award.setAddressLine3("add3");
    award.setDisputedPercentage(BigDecimal.TEN);
    award.setEquity(BigDecimal.ONE);
    award.setId(235L);
    award.setLandChargeRegistration("reg");
    award.setLiableParties(List.of(buildLiableParty(null, null, award, null)));
    award.setMortgageAmountDue(BigDecimal.ZERO);
    award.setNoRecoveryDetails("norecover");
    award.setRecovery("recovery");
    award.setRecoveryOfAwardTimeRelated(Boolean.FALSE);
    award.setRegistrationReference("regref");
    award.setStatutoryChargeExemptReason("reason");
    award.setTimeRecovery(buildTimeRecovery());
    award.setTitleNumber("titlenum");
    award.setValuationAmount(BigDecimal.TEN);
    award.setValuationCriteria("crit");
    award.setValuationDate(new Date());

    return award;
  }


  public static OtherAssetAward buildOtherAssetAward() {
    OtherAssetAward award = new OtherAssetAward();
    populateBaseAward(award);

    award.setAwardedPercentage(BigDecimal.TEN);
    award.setDisputedAmount(BigDecimal.ONE);
    award.setDisputedPercentage(BigDecimal.TEN);
    award.setId(235L);
    award.setLiableParties(List.of(buildLiableParty(null, null, null, award)));
    award.setNoRecoveryDetails("norecover");
    award.setRecovery("recovery");
    award.setRecoveryOfAwardTimeRelated(Boolean.FALSE);
    award.setRecoveredAmount(BigDecimal.ZERO);
    award.setRecoveredPercentage(BigDecimal.ONE);
    award.setStatutoryChargeExemptReason("reason");
    award.setTimeRecovery(buildTimeRecovery());
    award.setValuationAmount(BigDecimal.TEN);
    award.setValuationCriteria("crit");
    award.setValuationDate(new Date());

    return award;
  }

  public static LiableParty buildLiableParty(
      final CostAward costAward,
      final FinancialAward financialAward,
      final LandAward landAward,
      final OtherAssetAward otherAssetAward) {
    LiableParty liableParty = new LiableParty();
    liableParty.setAuditTrail(buildAuditTrail());
    liableParty.setAwardType("type");
    liableParty.setId(987L);

    liableParty.setCostAward(costAward);
    liableParty.setFinancialAward(financialAward);
    liableParty.setLandAward(landAward);
    liableParty.setOtherAssetAward(otherAssetAward);

    liableParty.setOpponent(new Opponent());

    return liableParty;
  }

  public static CaseOutcomeDetail buildCaseOutcomeDetail() {
    return new CaseOutcomeDetail()
        .auditTrail(buildAuditDetail())
        .caseReferenceNumber("caseRef")
        .clientContinueInd(Boolean.TRUE)
        .addCostAwardsItem(buildCostAwardDetail())
        .dischargeCaseInd(Boolean.FALSE)
        .dischargeReason("reason")
        .addFinancialAwardsItem(buildFinancialAwardDetail())
        .id(987)
        .addLandAwardsItem(buildLandAwardDetail())
        .legalCosts(BigDecimal.TEN)
        .officeCode("off")
        .opponentIds(List.of(1, 2))
        .addOtherAssetAwardsItem(buildOtherAssetAwardDetail())
        .otherDetails("other dets")
        .preCertificateCosts(BigDecimal.TEN)
        .addProceedingOutcomesItem(buildProceedingOutcomeDetail())
        .providerId("provId")
        .uniqueFileNo("fileno");
  }

  public static OtherAssetAwardDetail buildOtherAssetAwardDetail() {
    return new OtherAssetAwardDetail()
        .auditTrail(buildAuditDetail())
        .awardAmount(BigDecimal.ONE)
        .awardCode("code")
        .awardedAmount(BigDecimal.TEN)
        .awardedBy("person")
        .awardedPercentage(BigDecimal.ONE)
        .awardType("type")
        .costOrFinancial(Boolean.FALSE)
        .dateOfOrder(new Date())
        .deleteAllowed(Boolean.TRUE)
        .description("descr")
        .disputedAmount(BigDecimal.ZERO)
        .disputedPercentage(BigDecimal.TEN)
        .ebsId("ebsid")
        .effectiveDate(new Date())
        .id(536)
        .addLiablePartiesItem(buildLiablePartyDetail(4))
        .noRecoveryDetails("norecov")
        .opponentsToSelect(Boolean.FALSE)
        .recoveredAmount(BigDecimal.TEN)
        .recoveredPercentage(BigDecimal.ONE)
        .recovery("recov")
        .recoveryOfAwardTimeRelated(Boolean.TRUE)
        .statutoryChargeExemptReason("stat")
        .timeRecovery(buildTimeRecoveryDetail())
        .updateAllowed(Boolean.TRUE)
        .valuationAmount(BigDecimal.ONE)
        .valuationCriteria("crit")
        .valuationDate(new Date());
  }

  public static LandAwardDetail buildLandAwardDetail() {
    return new LandAwardDetail()
        .addressLine1("add1")
        .addressLine2("add2")
        .addressLine3("add3")
        .auditTrail(buildAuditDetail())
        .awardAmount(BigDecimal.ONE)
        .awardCode("code")
        .awardedBy("person")
        .awardedPercentage(BigDecimal.TEN)
        .awardType("type")
        .costOrFinancial(Boolean.TRUE)
        .dateOfOrder(new Date())
        .deleteAllowed(Boolean.TRUE)
        .description("descr")
        .disputedPercentage(BigDecimal.ONE)
        .ebsId("ebsid")
        .effectiveDate(new Date())
        .equity(BigDecimal.ZERO)
        .id(456)
        .landChargeRegistration("reg")
        .addLiablePartiesItem(
            buildLiablePartyDetail(3))
        .mortgageAmountDue(BigDecimal.ONE)
        .noRecoveryDetails("norecov")
        .opponentsToSelect(Boolean.TRUE)
        .recovery("recov")
        .recoveryOfAwardTimeRelated(Boolean.TRUE)
        .registrationReference("regref")
        .statutoryChargeExemptReason("stat")
        .timeRecovery(buildTimeRecoveryDetail())
        .titleNumber("title")
        .updateAllowed(Boolean.TRUE)
        .valuationAmount(BigDecimal.ONE)
        .valuationCriteria("crit")
        .valuationDate(new Date());
  }

  public static FinancialAwardDetail buildFinancialAwardDetail() {
    return new FinancialAwardDetail()
        .addressLine1("add1")
        .addressLine2("add2")
        .addressLine3("add3")
        .amount(BigDecimal.TEN)
        .auditTrail(buildAuditDetail())
        .awardAmount(BigDecimal.ONE)
        .awardCode("code")
        .awardedBy("person")
        .awardJustifications("just")
        .awardType("type")
        .costOrFinancial(Boolean.TRUE)
        .dateOfOrder(new Date())
        .deleteAllowed(Boolean.TRUE)
        .description("descr")
        .ebsId("ebsid")
        .effectiveDate(new Date())
        .id(456)
        .interimAward("interim")
        .addLiablePartiesItem(
            buildLiablePartyDetail(2))
        .opponentsToSelect(Boolean.FALSE)
        .orderServedDate(new Date())
        .otherDetails("other dets")
        .recovery(buildRecoveryDetail())
        .updateAllowed(Boolean.FALSE);
  }

  public static CostAwardDetail buildCostAwardDetail() {
    return new CostAwardDetail()
        .addressLine1("add1")
        .addressLine2("add2")
        .addressLine3("add3")
        .auditTrail(buildAuditDetail())
        .awardAmount(BigDecimal.TEN)
        .awardCode("code")
        .awardedBy("person")
        .awardType("type")
        .certificateCostLsc(BigDecimal.ONE)
        .certificateCostMarket(BigDecimal.ZERO)
        .costOrFinancial(Boolean.FALSE)
        .courtAssessmentStatus("stat")
        .dateOfOrder(new Date())
        .deleteAllowed(Boolean.TRUE)
        .description("descr")
        .ebsId("ebsid")
        .effectiveDate(new Date())
        .id(123)
        .interestAwardedRate(BigDecimal.ONE)
        .interestStartDate(new Date())
        .addLiablePartiesItem(buildLiablePartyDetail(1))
        .opponentsToSelect(Boolean.TRUE)
        .orderServedDate(new Date())
        .otherDetails("other dets")
        .preCertificateLscCost(BigDecimal.ZERO)
        .preCertificateOtherCost(BigDecimal.TEN)
        .recovery(buildRecoveryDetail())
        .totalCertCostsAwarded(BigDecimal.ONE)
        .updateAllowed(Boolean.FALSE);
  }

  public static LiablePartyDetail buildLiablePartyDetail(final Integer opponentId) {
    return new LiablePartyDetail()
        .auditTrail(buildAuditDetail())
        .awardType("type")
        .id(234)
        .opponentId(opponentId);
  }

  public static ProceedingOutcomeDetail buildProceedingOutcomeDetail() {
    return new ProceedingOutcomeDetail()
        .adrInfo("adr")
        .alternativeResolution("alt")
        .auditTrail(buildAuditDetail())
        .courtCode("code")
        .courtName("name")
        .dateOfFinalWork(new Date())
        .dateOfIssue(new Date())
        .description("descr")
        .id(927)
        .matterType(new StringDisplayValue().id("m").displayValue("matter"))
        .outcomeCourtCaseNo("caseno")
        .proceedingCaseId("caseid")
        .proceedingType(new StringDisplayValue().id("p").displayValue("proc"))
        .resolutionMethod("res")
        .result(new StringDisplayValue().id("r").displayValue("result"))
        .resultInfo("info")
        .stageEnd(new StringDisplayValue().id("s").displayValue("stage"))
        .widerBenefits("wider");
  }

  public static TimeRecoveryDetail buildTimeRecoveryDetail() {
    return new TimeRecoveryDetail()
        .auditTrail(buildAuditDetail())
        .awardAmount(BigDecimal.TEN)
        .awardType("type")
        .description("descr")
        .effectiveDate(new Date())
        .id(352)
        .timeRelatedRecoveryDetails("dets")
        .triggeringEvent("event");
  }

  public static RecoveryDetail buildRecoveryDetail() {
    return new RecoveryDetail()
        .auditTrail(buildAuditDetail())
        .awardAmount(BigDecimal.ONE)
        .awardType("type")
        .clientAmountPaidToLsc(BigDecimal.TEN)
        .clientRecoveryAmount(BigDecimal.ZERO)
        .clientRecoveryDate(new Date())
        .conditionsOfOffer("cond")
        .courtAmountPaidToLsc(BigDecimal.TEN)
        .courtRecoveryAmount(BigDecimal.ONE)
        .courtRecoveryDate(new Date())
        .description("descr")
        .id(843)
        .leaveOfCourtRequiredInd(Boolean.FALSE)
        .offerDetails("dets")
        .offeredAmount(BigDecimal.TEN)
        .recoveredAmount(BigDecimal.ONE)
        .solicitorAmountPaidToLsc(BigDecimal.ZERO)
        .solicitorRecoveryAmount(BigDecimal.ONE)
        .solicitorRecoveryDate(new Date())
        .unrecoveredAmount(BigDecimal.ONE);
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
