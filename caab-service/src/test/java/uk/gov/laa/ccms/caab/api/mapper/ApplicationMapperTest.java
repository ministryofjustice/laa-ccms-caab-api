package uk.gov.laa.ccms.caab.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.laa.ccms.caab.api.entity.Address;
import uk.gov.laa.ccms.caab.api.entity.Application;
import uk.gov.laa.ccms.caab.api.entity.AuditTrail;
import uk.gov.laa.ccms.caab.api.entity.CostStructure;
import uk.gov.laa.ccms.caab.api.entity.LinkedCase;
import uk.gov.laa.ccms.caab.api.entity.Opponent;
import uk.gov.laa.ccms.caab.api.entity.PriorAuthority;
import uk.gov.laa.ccms.caab.api.entity.Proceeding;
import uk.gov.laa.ccms.caab.api.entity.CostEntry;
import uk.gov.laa.ccms.caab.api.entity.ReferenceDataItem;
import uk.gov.laa.ccms.caab.api.entity.ScopeLimitation;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.ApplicationProviderDetails;
import uk.gov.laa.ccms.caab.model.ApplicationType;
import uk.gov.laa.ccms.caab.model.AuditDetail;
import uk.gov.laa.ccms.caab.model.BooleanDisplayValue;
import uk.gov.laa.ccms.caab.model.Client;
import uk.gov.laa.ccms.caab.model.CostLimit;
import uk.gov.laa.ccms.caab.model.DevolvedPowers;
import uk.gov.laa.ccms.caab.model.IntDisplayValue;
import uk.gov.laa.ccms.caab.model.StringDisplayValue;

public class ApplicationMapperTest {

    private final ApplicationMapper mapper = new ApplicationMapperImpl();

    private static final String CAAB_USER_LOGIN_ID = "testUser";

    private Date createdAt;
    private Date updatedAt;

    @BeforeEach
    public void setup() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        createdAt = dateFormat.parse("01/01/2000");
        updatedAt = dateFormat.parse("01/01/2001");
    }

    @Test
    public void testApplicationMapping_null() {
        Application application = mapper.toApplication(null);
        assertNull(application);
    }

    @Test
    public void testApplicationMapping() {
        // Construct ApplicationDetail
        ApplicationDetail detail = new ApplicationDetail(null,null,null,null);
        detail.setCaseReferenceNumber("caseRef123");
        detail.setProviderCaseReference("providerCase");
        detail.setProvider(new IntDisplayValue().id(1234).displayValue("providerDisp"));
        detail.setOffice(new IntDisplayValue().id(1).displayValue("officeDisp"));
        detail.setSupervisor(new StringDisplayValue().id("supervisorId").displayValue("supervisorDisp"));
        detail.setFeeEarner(new StringDisplayValue().id("feeEarnerId").displayValue("feeEarnerDisp"));
        detail.setProviderContact(new StringDisplayValue().id("providerContactId").displayValue("providerContactDisp"));
        detail.setCategoryOfLaw(new StringDisplayValue().id("categoryLawId").displayValue("categoryLawDisp"));
        detail.setStatus(new StringDisplayValue().id("statusId").displayValue("statusDisp"));
        detail.setClient(new Client().firstName("clientFirst").surname("clientSurname").reference("clientRef"));
        detail.setApplicationType(new ApplicationType().id("appTypeId").displayValue("appTypeDisp"));
        detail.setMeritsReassessmentRequired(true);
        detail.setLeadProceedingChanged(true);
        detail.setMeansAssessmentAmended(true);
        detail.setMeritsAssessmentAmended(true);
        detail.setAmendment(true);

        CostLimit costLimit = new CostLimit()
            .changed(true)
            .limitAtTimeOfMerits(BigDecimal.valueOf(123));
        detail.setCostLimit(costLimit);

        // Convert ApplicationDetail to Application
        Application application = mapper.toApplication(detail);

        assertNull(application.getId()); // ID should be ignored and not set
        assertEquals("caseRef123", application.getLscCaseReference());
        assertEquals(1234, Integer.parseInt(application.getProviderId()));
        assertEquals("providerDisp", application.getProviderDisplayValue());
        assertEquals("providerCase", application.getProviderCaseReference());
        assertEquals(1, application.getOfficeId());
        assertEquals("officeDisp", application.getOfficeDisplayValue());
        assertEquals("supervisorId", application.getSupervisor());
        assertEquals("supervisorDisp", application.getSupervisorDisplayValue());
        assertEquals("feeEarnerId", application.getFeeEarner());
        assertEquals("feeEarnerDisp", application.getFeeEarnerDisplayValue());
        assertEquals("providerContactId", application.getProviderContact());
        assertEquals("providerContactDisp", application.getProviderContactDisplayValue());
        assertEquals("categoryLawId", application.getCategoryOfLaw());
        assertEquals("categoryLawDisp", application.getCategoryOfLawDisplayValue());
        assertEquals("statusDisp", application.getDisplayStatus());
        assertEquals("statusId", application.getActualStatus());
        assertEquals("clientFirst", application.getClientFirstName());
        assertEquals("clientSurname", application.getClientSurname());
        assertEquals("clientRef", application.getClientReference());
        assertEquals("appTypeId", application.getApplicationType());
        assertEquals("appTypeDisp", application.getApplicationTypeDisplayValue());
        assertTrue(application.isCostLimitChanged());
        assertEquals(BigDecimal.valueOf(123), application.getCostLimitAtTimeOfMerits());
        assertTrue(application.isAmendment());
        assertTrue(application.isMeansAssessmentAmended());
        assertTrue(application.isMeritsAssessmentAmended());

    }

    @Test
    public void testApplicationMapping_unsetBooleansFalse() {
        ApplicationDetail detail = new ApplicationDetail(null,null,null,null);

        Application application = mapper.toApplication(detail);

        assertFalse(application.isCostLimitChanged());
        assertFalse(application.isAmendment());
        assertFalse(application.isMeansAssessmentAmended());
        assertFalse(application.isMeritsAssessmentAmended());
    }

    @Test
    public void testToApplication_costStructure_costEntry() {
        // Construct an ApplicationDetail instance with costEntry
        ApplicationDetail applicationDetail = new ApplicationDetail(null, null,null,null);

        uk.gov.laa.ccms.caab.model.CostEntry costEntryModel = new uk.gov.laa.ccms.caab.model.CostEntry();
        costEntryModel.setRequestedCosts(BigDecimal.valueOf(1000));
        costEntryModel.setCostCategory("Legal Fees");

        uk.gov.laa.ccms.caab.model.CostStructure costStructureModel = new uk.gov.laa.ccms.caab.model.CostStructure();
        costStructureModel.setCostEntries(Collections.singletonList(costEntryModel));
        applicationDetail.setCosts(costStructureModel);

        // Convert ApplicationDetail to Application with costEntry
        Application application = mapper.toApplication(applicationDetail);

        // Assert values in the mapped result
        assertNotNull(application.getCosts()); // Ensure that CostStructure is not null
        assertEquals(1, application.getCosts().getCostEntries().size()); // Ensure that there is one CostEntry
        assertEquals(BigDecimal.valueOf(1000), application.getCosts().getCostEntries().get(0).getRequestedCosts());
        assertEquals("Legal Fees", application.getCosts().getCostEntries().get(0).getCostCategory());
    }

    @Test
    public void testToApplicationDetail_costStructure_costEntry() {
        Application application = new Application();
        application.setId(1L);
        application.setLscCaseReference("caseRef123");

        CostEntry costEntry = new CostEntry();
        costEntry.setRequestedCosts(BigDecimal.valueOf(1000));
        costEntry.setCostCategory("Legal Fees");

        CostStructure costStructure = new CostStructure();
        costStructure.setCostEntries(Collections.singletonList(costEntry));
        application.setCosts(costStructure);

        ApplicationDetail applicationDetail = mapper.toApplicationDetail(application);

        assertEquals("caseRef123", applicationDetail.getCaseReferenceNumber());
        assertNotNull(applicationDetail.getCosts());
        assertEquals(1, applicationDetail.getCosts().getCostEntries().size());
        assertEquals(BigDecimal.valueOf(1000), applicationDetail.getCosts().getCostEntries().get(0).getRequestedCosts());
        assertEquals("Legal Fees", applicationDetail.getCosts().getCostEntries().get(0).getCostCategory());
    }


    @Test
    public void testProceedingMapping_null() {
        assertNull(mapper.toProceeding(null));
    }

    @Test
    public void testProceedingMapping() {
        uk.gov.laa.ccms.caab.model.Proceeding proceedingModel = new uk.gov.laa.ccms.caab.model.Proceeding();
        StringDisplayValue matterType = new StringDisplayValue()
            .id("MT")
            .displayValue("MatterType");
        proceedingModel.setMatterType(matterType);

        StringDisplayValue proceedingType = new StringDisplayValue()
            .id("PT")
            .displayValue("ProceedingType");
        proceedingModel.setProceedingType(proceedingType);

        StringDisplayValue levelOfService = new StringDisplayValue()
            .id("LOS")
            .displayValue("LevelOfService");
        proceedingModel.setLevelOfService(levelOfService);

        StringDisplayValue clientInvolvement = new StringDisplayValue()
            .id("CI")
            .displayValue("ClientInvolvement");
        proceedingModel.setClientInvolvement(clientInvolvement);

        StringDisplayValue status = new StringDisplayValue()
            .id("S")
            .displayValue("Status");
        proceedingModel.setStatus(status);

        StringDisplayValue typeOfOrder = new StringDisplayValue()
            .id("TOO")
            .displayValue("TypeOfOrder");
        proceedingModel.setTypeOfOrder(typeOfOrder);

        proceedingModel.setAuditTrail(buildAuditDetail());

        proceedingModel.setEbsId("EBSID");
        proceedingModel.setCostLimitation(BigDecimal.valueOf(111112));


        proceedingModel.setScopeLimitations(new ArrayList<>());
        proceedingModel.setEdited(Boolean.TRUE);
        proceedingModel.setLeadProceedingInd(Boolean.TRUE);

        Proceeding proceeding = mapper.toProceeding(proceedingModel);

        assertNotNull(proceeding);
        assertEquals("MT", proceeding.getMatterType());
        assertEquals("MatterType", proceeding.getMatterTypeDisplayValue());
        assertEquals("PT", proceeding.getProceedingType());
        assertEquals("ProceedingType", proceeding.getProceedingTypeDisplayValue());
        assertEquals("LOS", proceeding.getLevelOfService());
        assertEquals("LevelOfService", proceeding.getLevelOfServiceDisplayValue());
        assertEquals("CI", proceeding.getClientInvolvement());
        assertEquals("ClientInvolvement", proceeding.getClientInvolvementDisplayValue());
        assertEquals("S", proceeding.getStatus());
        assertEquals("Status", proceeding.getDisplayStatus());
        assertEquals("TOO", proceeding.getTypeOfOrder());

        assertEquals("EBSID", proceeding.getEbsId());
        assertEquals(BigDecimal.valueOf(111112), proceeding.getCostLimitation());

        assertEquals(proceedingModel.getDescription(), proceeding.getDescription());
        assertEquals(proceedingModel.getDateGranted(), proceeding.getDateGranted());
        assertEquals(proceedingModel.getDateCostsValid(), proceeding.getDateCostsValid());
        assertTrue(proceeding.isEdited());
        // For collections like scopeLimitations, you might need to assert both the size and the contents
        assertEquals(0, proceeding.getScopeLimitations().size());
        // Add more detailed checks for elements in scopeLimitations if necessary
        assertEquals(proceedingModel.getDefaultScopeLimitation(), proceeding.getDefaultScopeLimitation());
        assertEquals(proceedingModel.getStage(), proceeding.getStage());
        assertTrue(proceeding.isLeadProceedingInd());
        assertEquals(proceedingModel.getLarScope(), proceeding.getLarScope());

    }

    @Test
    public void testProceedingMapping_unsetBooleansFalse() {
        uk.gov.laa.ccms.caab.model.Proceeding proceedingModel = new uk.gov.laa.ccms.caab.model.Proceeding();
        proceedingModel.setLeadProceedingInd(null);

        Proceeding proceeding = mapper.toProceeding(proceedingModel);
        assertFalse(proceeding.isLeadProceedingInd());
        assertFalse(proceeding.isEdited());
    }

    @Test
    public void testToProceedingModel() {
        // Construct Proceeding
        Proceeding proceeding = new Proceeding();
        AuditTrail auditTrail = new AuditTrail();
        // Set fields on auditTrail as needed
        proceeding.setAuditTrail(auditTrail);
        proceeding.setStage("Stage1");

        // Convert Proceeding to ProceedingDetail
        uk.gov.laa.ccms.caab.model.Proceeding proceedingDetail = mapper.toProceedingModel(proceeding);

        // Assertions
        assertEquals("Stage1", proceedingDetail.getStage());
    }

    @Test
    public void testToProceedingModel_null() {
        assertNull(mapper.toProceedingModel(null));
    }



    @Test
    public void testToPriorAuthority(){
        uk.gov.laa.ccms.caab.model.PriorAuthority priorAuthorityModel = new uk.gov.laa.ccms.caab.model.PriorAuthority();
        priorAuthorityModel.setEbsId("EBSID");
        priorAuthorityModel.setStatus("Status");
        priorAuthorityModel.setType(new StringDisplayValue().id("T").displayValue("Type"));
        priorAuthorityModel.setSummary("Summary");
        priorAuthorityModel.setJustification("Justification");
        priorAuthorityModel.setValueRequired(true);
        priorAuthorityModel.setAmountRequested(new BigDecimal("0.123456"));
        priorAuthorityModel.setItems(new ArrayList<>());
        priorAuthorityModel.setAuditTrail(buildAuditDetail());

        PriorAuthority priorAuthority = mapper.toPriorAuthority(priorAuthorityModel);

        assertEquals("EBSID", priorAuthority.getEbsId());
        assertEquals("Status", priorAuthority.getStatus());
        assertEquals("T", priorAuthority.getType());
        assertEquals("Type", priorAuthority.getTypeDisplayValue());
        assertEquals("Summary", priorAuthority.getSummary());
        assertEquals("Justification", priorAuthority.getJustification());
        assertTrue(priorAuthority.getValueRequired());
        assertEquals(new BigDecimal("0.123456"), priorAuthority.getAmountRequested());
        assertEquals(0, priorAuthority.getItems().size()); // Assuming you expect an empty list
    }

    @Test
    public void testToPriorAuthority_null(){
        assertNull(mapper.toPriorAuthority(null));
    }


    @Test
    public void testToPriorAuthorityModel() {
        PriorAuthority priorAuthority = new PriorAuthority();
        AuditTrail auditTrail = buildAuditTrail();

        priorAuthority.setAuditTrail(auditTrail);

        uk.gov.laa.ccms.caab.model.PriorAuthority priorAuthorityDetail
            = mapper.toPriorAuthorityModel(priorAuthority);

        //TODO
    }

    @Test
    public void testToPriorAuthorityModel_null(){
        assertNull(mapper.toPriorAuthorityModel(null));
    }

    @Test
    public void testToReferenceDataItem(){

        uk.gov.laa.ccms.caab.model.ReferenceDataItem referenceDataItemModel =
            new uk.gov.laa.ccms.caab.model.ReferenceDataItem();

        referenceDataItemModel.setCode(new StringDisplayValue()
            .id("C")
            .displayValue("Code"));
        referenceDataItemModel.setType("Type");
        referenceDataItemModel.setMandatory(true);
        referenceDataItemModel.setLovLookUp("Lookup");
        referenceDataItemModel.setValue(new StringDisplayValue()
            .id("V")
            .displayValue("Value"));

        ReferenceDataItem referenceDataItem = mapper.toReferenceDataItem(referenceDataItemModel);

        assertEquals("C", referenceDataItem.getCode());
        assertEquals("Code", referenceDataItem.getLabel());
        assertEquals("V", referenceDataItem.getValue());
        assertEquals("Value", referenceDataItem.getDisplayValue());
        assertEquals("Type", referenceDataItem.getType());
        assertTrue(referenceDataItem.getMandatory());
        assertEquals("Lookup", referenceDataItem.getLovLookUp());
    }

    @Test
    public void testToReferenceDataItem_null(){
        assertNull(mapper.toReferenceDataItem(null));
    }

    @Test
    public void testToReferenceDataItemModel() {
        ReferenceDataItem referenceDataItem = new ReferenceDataItem();
        referenceDataItem.setCode("C");
        referenceDataItem.setValue("V");
        referenceDataItem.setType("Type");
        referenceDataItem.setMandatory(true);
        referenceDataItem.setLovLookUp("Lookup");

        uk.gov.laa.ccms.caab.model.ReferenceDataItem referenceDataItemModel = mapper.toReferenceDataItemModel(referenceDataItem);

        assertEquals("C", referenceDataItemModel.getCode().getId());
        assertEquals("V", referenceDataItemModel.getValue().getId());
        assertEquals("Type", referenceDataItemModel.getType());
        assertTrue(referenceDataItemModel.getMandatory());
        assertEquals("Lookup", referenceDataItemModel.getLovLookUp());
    }


    @Test
    public void testToReferenceDataItemModel_null(){
        assertNull(mapper.toReferenceDataItemModel(null));
    }

    @Test
    public void testToScopeLimitation() {
        uk.gov.laa.ccms.caab.model.ScopeLimitation scopeLimitationModel = new uk.gov.laa.ccms.caab.model.ScopeLimitation();
        scopeLimitationModel.setScopeLimitation(new StringDisplayValue().id("SL").displayValue("ScopeLimitation"));
        scopeLimitationModel.setScopeLimitationWording("Wording");
        scopeLimitationModel.setDelegatedFuncApplyInd(new BooleanDisplayValue().flag(true).displayValue(""));
        scopeLimitationModel.setAuditTrail(buildAuditDetail());
        scopeLimitationModel.setEbsId("EBSID");
        scopeLimitationModel.setDefaultInd(true);

        ScopeLimitation scopeLimitation = mapper.toScopeLimitation(scopeLimitationModel);

        assertEquals("SL", scopeLimitation.getScopeLimitation());
        assertEquals("ScopeLimitation", scopeLimitation.getScopeLimitationDisplayValue());
        assertTrue(scopeLimitation.isDelegatedFuncApplyInd());
        assertEquals("EBSID", scopeLimitation.getEbsId());
        assertEquals("Wording", scopeLimitation.getScopeLimitationWording());
        assertTrue(scopeLimitation.isDefaultInd());
    }

    @Test
    public void testToScopeLimitation_unsetBooleansFalse() {
        uk.gov.laa.ccms.caab.model.ScopeLimitation scopeLimitationModel = new uk.gov.laa.ccms.caab.model.ScopeLimitation();

        ScopeLimitation scopeLimitation = mapper.toScopeLimitation(scopeLimitationModel);

        assertFalse(scopeLimitation.isDelegatedFuncApplyInd());
        assertFalse(scopeLimitation.isDefaultInd());
    }

    @Test
    public void testToScopeLimitation_null() {
        assertNull(mapper.toScopeLimitation(null));
    }

    @Test
    public void testToScopeLimitationModel() {
        ScopeLimitation scopeLimitationEntity = new ScopeLimitation();
        scopeLimitationEntity.setScopeLimitation("SL");
        scopeLimitationEntity.setScopeLimitationDisplayValue("ScopeLimitation");
        scopeLimitationEntity.setDelegatedFuncApplyInd(true);
        scopeLimitationEntity.setAuditTrail(buildAuditTrail());
        scopeLimitationEntity.setEbsId("EBSID");
        scopeLimitationEntity.setScopeLimitationWording("Wording");
        scopeLimitationEntity.setDefaultInd(true);

        uk.gov.laa.ccms.caab.model.ScopeLimitation scopeLimitation = mapper.toScopeLimitationModel(scopeLimitationEntity);

        assertEquals("SL", scopeLimitation.getScopeLimitation().getId());
        assertEquals("ScopeLimitation", scopeLimitation.getScopeLimitation().getDisplayValue());
        assertTrue(scopeLimitation.getDelegatedFuncApplyInd().getFlag());
        assertEquals("EBSID", scopeLimitation.getEbsId());
        assertEquals("Wording", scopeLimitation.getScopeLimitationWording());
        assertTrue(scopeLimitation.getDefaultInd());
    }

    @Test
    public void testToScopeLimitationModel_unsetBooleansFalse() {
        ScopeLimitation scopeLimitationEntity = new ScopeLimitation();

        uk.gov.laa.ccms.caab.model.ScopeLimitation scopeLimitation = mapper.toScopeLimitationModel(scopeLimitationEntity);

        assertFalse(scopeLimitation.getDelegatedFuncApplyInd().getFlag());
        assertFalse(scopeLimitation.getDefaultInd());
    }

    @Test
    public void testToScopeLimitationModel_null() {
        assertNull(mapper.toScopeLimitationModel(null));
    }

    @Test
    public void testToOpponent() {
        uk.gov.laa.ccms.caab.model.Opponent opponentDetail = new uk.gov.laa.ccms.caab.model.Opponent();
        opponentDetail.setOrganisationType(new StringDisplayValue().id("OT").displayValue("Organisation Type"));
        opponentDetail.setAuditTrail(buildAuditDetail());
        opponentDetail.setEbsId("EBSID");
        opponentDetail.setType("Type");
        opponentDetail.setTitle("Title");
        opponentDetail.setFirstName("John");
        opponentDetail.setMiddleNames("Doe");
        opponentDetail.setSurname("Smith");
        opponentDetail.setDateOfBirth(createdAt);
        opponentDetail.setNationalInsuranceNumber("AB123456C");
        opponentDetail.setRelationshipToCase("Relation1");
        opponentDetail.setRelationshipToClient("Client1");
        opponentDetail.setTelephoneHome("123456789");
        opponentDetail.setTelephoneWork("987654321");
        opponentDetail.setTelephoneMobile("456789123");
        opponentDetail.setFaxNumber("555555555");
        opponentDetail.setEmailAddress("john@example.com");
        opponentDetail.setOtherInformation("Other Info");
        opponentDetail.setEmploymentStatus("Employed");
        opponentDetail.setEmployerName("ABC Ltd");
        opponentDetail.setLegalAided(true);
        opponentDetail.setCertificateNumber("CERT123");
        opponentDetail.setCourtOrderedMeansAssessment(true);
        opponentDetail.setAssessedIncome(BigDecimal.valueOf(50000.00));
        opponentDetail.setAssessedIncomeFrequency("Annually");
        opponentDetail.setAssessedAssets(BigDecimal.valueOf(100000.00));
        opponentDetail.setAssessmentDate(createdAt);
        opponentDetail.setOrganisationName("Org Name");
        opponentDetail.setCurrentlyTrading(true);
        opponentDetail.setContactNameRole("Contact");
        opponentDetail.setConfirmed(true);
        opponentDetail.setAppMode(false);
        opponentDetail.setAmendment(true);
        opponentDetail.setAward(true);
        opponentDetail.setPublicFundingApplied(true);
        opponentDetail.setSharedInd(true);
        opponentDetail.setDeleteInd(false);

        Opponent opponent = mapper.toOpponent(opponentDetail);

        assertEquals("OT", opponent.getOrganisationType());
        assertEquals("EBSID", opponent.getEbsId());
        assertEquals("Type", opponent.getType());
        assertEquals("Title", opponent.getTitle());
        assertEquals("John", opponent.getFirstName());
        assertEquals("Doe", opponent.getMiddleNames());
        assertEquals("Smith", opponent.getSurname());
        assertEquals(createdAt, opponent.getDateOfBirth());
        assertEquals("AB123456C", opponent.getNationalInsuranceNumber());
        assertEquals("Relation1", opponent.getRelationshipToCase());
        assertEquals("Client1", opponent.getRelationshipToClient());
        assertEquals("123456789", opponent.getTelephoneHome());
        assertEquals("987654321", opponent.getTelephoneWork());
        assertEquals("456789123", opponent.getTelephoneMobile());
        assertEquals("555555555", opponent.getFaxNumber());
        assertEquals("john@example.com", opponent.getEmailAddress());
        assertEquals("Other Info", opponent.getOtherInformation());
        assertEquals("Employed", opponent.getEmploymentStatus());
        assertEquals("ABC Ltd", opponent.getEmployerName());
        assertTrue(opponent.getLegalAided());
        assertEquals("CERT123", opponent.getCertificateNumber());
        assertTrue(opponent.getCourtOrderedMeansAssessment());
        assertEquals(BigDecimal.valueOf(50000.00), opponent.getAssessedIncome());
        assertEquals("Annually", opponent.getAssessedIncomeFrequency());
        assertEquals(BigDecimal.valueOf(100000.00), opponent.getAssessedAssets());
        assertEquals(createdAt, opponent.getAssessmentDate());
        assertEquals("Org Name", opponent.getOrganisationName());
        assertTrue(opponent.getCurrentlyTrading());
        assertEquals("Contact", opponent.getContactNameRole());
        assertTrue(opponent.isConfirmed());
        assertFalse(opponent.isAppMode());
        assertTrue(opponent.isAmendment());
        assertTrue(opponent.isAward());
        assertTrue(opponent.isPublicFundingApplied());
        assertTrue(opponent.isSharedInd());

        assertFalse(opponent.isAppMode());
        assertFalse(opponent.isDeleteInd());
    }

    @Test
    public void testToOpponent_unsetBooleans() {
        uk.gov.laa.ccms.caab.model.Opponent opponentDetail =
            new uk.gov.laa.ccms.caab.model.Opponent();
        opponentDetail.setAppMode(null);

        Opponent opponent = mapper.toOpponent(opponentDetail);

        assertFalse(opponent.isConfirmed());
        assertFalse(opponent.isAmendment());
        assertFalse(opponent.isAward());
        assertFalse(opponent.isPublicFundingApplied());
        assertFalse(opponent.isSharedInd());

        assertTrue(opponent.isAppMode());
        assertTrue(opponent.isDeleteInd());
    }

    @Test
    public void testToOpponent_null() {
        assertNull(mapper.toOpponent(null));
    }

    @Test
    public void testToOpponentModel() {
        Opponent opponent = new Opponent();
        opponent.setAuditTrail(buildAuditTrail());
        opponent.setRelationshipToCase("Relation1");
        opponent.setType("Type1");
        opponent.setFirstName("John");
        opponent.setMiddleNames("Doe");
        opponent.setSurname("Smith");
        opponent.setDateOfBirth(createdAt);
        opponent.setNationalInsuranceNumber("AB123456C");
        opponent.setCourtOrderedMeansAssessment(true);

        uk.gov.laa.ccms.caab.model.Opponent opponentDetail = mapper.toOpponentModel(opponent);

        assertEquals("Relation1", opponentDetail.getRelationshipToCase());
        assertEquals("Type1", opponentDetail.getType());
        assertEquals("John", opponentDetail.getFirstName());
        assertEquals("Doe", opponentDetail.getMiddleNames());
        assertEquals("Smith", opponentDetail.getSurname());
        assertEquals(createdAt, opponentDetail.getDateOfBirth());
        assertEquals("AB123456C", opponentDetail.getNationalInsuranceNumber());
        assertTrue(opponentDetail.getCourtOrderedMeansAssessment());
    }

    @Test
    public void testToOpponentModel_null() {
        assertNull(mapper.toOpponentModel(null));
    }

    @Test
    public void testToLinkedCase() {
        uk.gov.laa.ccms.caab.model.LinkedCase linkedCaseModel = new uk.gov.laa.ccms.caab.model.LinkedCase();
        linkedCaseModel.setAuditTrail(buildAuditDetail());
        linkedCaseModel.setLscCaseReference("LSC123");
        linkedCaseModel.setRelationToCase("Relation1");
        linkedCaseModel.setProviderCaseReference("Provider123");
        linkedCaseModel.setFeeEarner("FeeEarner1");
        linkedCaseModel.setStatus("Active");
        Client linkedCaseClient = new Client();
        linkedCaseClient.setReference("Client123");
        linkedCaseClient.setFirstName("John");
        linkedCaseClient.setSurname("Doe");
        linkedCaseModel.setClient(linkedCaseClient);

        LinkedCase linkedCase = mapper.toLinkedCase(linkedCaseModel);

        assertEquals("LSC123", linkedCase.getLscCaseReference());
        assertEquals("Relation1", linkedCase.getRelationToCase());
        assertEquals("Provider123", linkedCase.getProviderCaseReference());
        assertEquals("FeeEarner1", linkedCase.getFeeEarner());
        assertEquals("Active", linkedCase.getStatus());
        assertEquals("Client123", linkedCase.getClientReference());
        assertEquals("John", linkedCase.getClientFirstName());
        assertEquals("Doe", linkedCase.getClientSurname());
    }

    @Test
    public void testToLinkedCase_null() {
        assertNull(mapper.toLinkedCase(null));
    }


    @Test
    public void testToLinkedCaseModel() {
        LinkedCase linkedCase = new LinkedCase();
        linkedCase.setAuditTrail(buildAuditTrail());
        linkedCase.setLscCaseReference("LSC123");
        linkedCase.setRelationToCase("Relation1");
        linkedCase.setProviderCaseReference("Provider123");
        linkedCase.setFeeEarner("FeeEarner1");
        linkedCase.setStatus("Active");
        linkedCase.setClientReference("Client123");
        linkedCase.setClientFirstName("John");
        linkedCase.setClientSurname("Doe");

        // Perform the mapping
        uk.gov.laa.ccms.caab.model.LinkedCase linkedCaseModel = mapper.toLinkedCaseModel(linkedCase);

        // Assert the values in the mapped result
        assertEquals("LSC123", linkedCaseModel.getLscCaseReference());
        assertEquals("Relation1", linkedCaseModel.getRelationToCase());
        assertEquals("Provider123", linkedCaseModel.getProviderCaseReference());
        assertEquals("FeeEarner1", linkedCaseModel.getFeeEarner());
        assertEquals("Active", linkedCaseModel.getStatus());
        assertEquals("Client123", linkedCaseModel.getClient().getReference());
        assertEquals("John", linkedCaseModel.getClient().getFirstName());
        assertEquals("Doe", linkedCaseModel.getClient().getSurname());
    }

    @Test
    public void testToLinkedCaseModel_null() {
        assertNull(mapper.toLinkedCaseModel(null));
    }

    @Test
    public void testAddressMapping() {
        uk.gov.laa.ccms.caab.model.Address detailAddress = new uk.gov.laa.ccms.caab.model.Address();
        detailAddress.setNoFixedAbode(true);
        detailAddress.setPostcode("12345");
        detailAddress.setHouseNameOrNumber("House 123");
        detailAddress.setAddressLine1("Address line 1");
        detailAddress.setAddressLine2("Address line 2");
        detailAddress.setCity("City");
        detailAddress.setCounty("County");
        detailAddress.setCountry("Country");
        detailAddress.setCareOf("Care of");
        detailAddress.setPreferredAddress("Preferred Address");

        // Convert ApplicationDetailCorrespondenceAddress to Address
        Address address = mapper.toAddress(detailAddress);

        // Assertions
        assertNull(address.getId()); // ID should be ignored and not set
        assertTrue(address.getNoFixedAbode());
        assertEquals("12345", address.getPostCode());
        assertEquals("House 123", address.getHouseNameNumber());
        assertEquals("Address line 1", address.getAddressLine1());
        assertEquals("Address line 2", address.getAddressLine2());
        assertEquals("City", address.getCity());
        assertEquals("County", address.getCounty());
        assertEquals("Country", address.getCountry());
        assertEquals("Care of", address.getCareOf());
        assertEquals("Preferred Address", address.getPreferredAddress());
    }

    @Test
    public void testCostStructureMapping() {
        // Construct ApplicationDetailCosts
        uk.gov.laa.ccms.caab.model.CostStructure detailCosts =
            new uk.gov.laa.ccms.caab.model.CostStructure();
        detailCosts.setDefaultCostLimitation(new BigDecimal("100.00"));
        detailCosts.setGrantedCostLimitation(new BigDecimal("200.00"));
        detailCosts.setRequestedCostLimitation(new BigDecimal("300.00"));

        // Convert ApplicationDetailCosts to CostStructure
        CostStructure costStructure = mapper.toCostStructure(detailCosts);

        // Assertions
        assertNull(costStructure.getId()); // ID should be ignored and not set
        assertEquals(new BigDecimal("100.00"), costStructure.getDefaultCostLimitation());
        assertEquals(new BigDecimal("200.00"), costStructure.getGrantedCostLimitation());
        assertEquals(new BigDecimal("300.00"), costStructure.getRequestedCostLimitation());
    }

    @Test
    public void testToApplicationMapping() {
        // Construct ApplicationDetail
        ApplicationDetail detail = new ApplicationDetail(null,null,null,null);;
        detail.setCaseReferenceNumber("CASE-001");
        detail.setProviderCaseReference("CASE-001");
        detail.setProvider(new IntDisplayValue().id(1234).displayValue("Provider Display"));
        detail.setOffice(new IntDisplayValue().id(1).displayValue("Office 1"));
        detail.setSupervisor(new StringDisplayValue().id("Supervisor").displayValue("Supervisor Display"));
        detail.setFeeEarner(new StringDisplayValue().id("Fee Earner").displayValue("Fee Earner Display"));

        // Convert ApplicationDetail to Application
        Application application = mapper.toApplication(detail);

        // Assertions
        assertEquals("CASE-001", application.getLscCaseReference());
        assertEquals(1234, Integer.parseInt(application.getProviderId()));
        assertEquals("Provider Display", application.getProviderDisplayValue());
        assertEquals("CASE-001", application.getProviderCaseReference());
        assertEquals(1L, application.getOfficeId());
        assertEquals("Office 1", application.getOfficeDisplayValue());
    }

    @Test
    public void testToAddressWithNullValues() {
        // Construct ApplicationDetailCorrespondenceAddress with null values
        uk.gov.laa.ccms.caab.model.Address detailAddress =
            new uk.gov.laa.ccms.caab.model.Address();

        // Convert ApplicationDetailCorrespondenceAddress to Address
        Address address = mapper.toAddress(detailAddress);

        // Assertions
        assertNull(address.getPostCode());
        assertNull(address.getHouseNameNumber());
    }

    @Test
    public void testToCostStructureWithZeroValues() {
        // Construct ApplicationDetailCosts with zero values
        uk.gov.laa.ccms.caab.model.CostStructure detailCosts =
            new uk.gov.laa.ccms.caab.model.CostStructure();
        detailCosts.setDefaultCostLimitation(new BigDecimal("0.00"));
        detailCosts.setGrantedCostLimitation(new BigDecimal("0.00"));
        detailCosts.setRequestedCostLimitation(new BigDecimal("0.00"));

        // Convert ApplicationDetailCosts to CostStructure
        CostStructure costStructure = mapper.toCostStructure(detailCosts);

        // Assertions
        assertEquals(new BigDecimal("0.00"), costStructure.getDefaultCostLimitation());
        assertEquals(new BigDecimal("0.00"), costStructure.getGrantedCostLimitation());
        assertEquals(new BigDecimal("0.00"), costStructure.getRequestedCostLimitation());
    }

    @Test
    public void testToApplicationDetailCorrespondenceAddressMapping() {
        // Construct Address
        Address address = new Address();
        address.setNoFixedAbode(true);
        address.setPostCode("12345");
        address.setHouseNameNumber("House 123");
        address.setAddressLine1("Address line 1");
        address.setAddressLine2("Address line 2");
        address.setCity("City");
        address.setCounty("County");
        address.setCountry("Country");
        address.setCareOf("Care of");
        address.setPreferredAddress("Preferred Address");

        // Convert Address to ApplicationDetailCorrespondenceAddress
        uk.gov.laa.ccms.caab.model.Address detailAddress = mapper.toAddressModel(address);

        // Assertions
        assertTrue(detailAddress.getNoFixedAbode());
        assertEquals("12345", detailAddress.getPostcode());
        assertEquals("House 123", detailAddress.getHouseNameOrNumber());
        assertEquals("Address line 1", detailAddress.getAddressLine1());
        assertEquals("Address line 2", detailAddress.getAddressLine2());
        assertEquals("City", detailAddress.getCity());
        assertEquals("County", detailAddress.getCounty());
        assertEquals("Country", detailAddress.getCountry());
        assertEquals("Care of", detailAddress.getCareOf());
        assertEquals("Preferred Address", detailAddress.getPreferredAddress());
    }

    @Test
    public void testToApplicationDetailCostsMapping() {
        CostStructure costStructure = new CostStructure();
        costStructure.setDefaultCostLimitation(new BigDecimal("100.00"));
        costStructure.setGrantedCostLimitation(new BigDecimal("200.00"));
        costStructure.setRequestedCostLimitation(new BigDecimal("300.00"));

        uk.gov.laa.ccms.caab.model.CostStructure detailCosts = mapper.toCostStructureModel(costStructure);

        assertEquals(new BigDecimal("100.00"), detailCosts.getDefaultCostLimitation());
        assertEquals(new BigDecimal("200.00"), detailCosts.getGrantedCostLimitation());
        assertEquals(new BigDecimal("300.00"), detailCosts.getRequestedCostLimitation());
    }


    @Test
    void testToApplicationDetailWithNullInput() {
        assertNull(mapper.toApplicationDetail(null));
    }

    @Test
    void testToApplicationDetailWithValidInput() {
        Application application = getApplication();

        // Call toApplicationDetail(Application)
        ApplicationDetail applicationDetail = mapper.toApplicationDetail(application);

        // Verify the resulting ApplicationDetail instance
        assertNotNull(applicationDetail);
        assertEquals(Integer.valueOf(12345), applicationDetail.getProvider().getId());
        assertEquals("Provider Display Value", applicationDetail.getProvider().getDisplayValue());
        assertEquals("Provider Case Reference", applicationDetail.getProviderCaseReference());
        assertEquals(Integer.valueOf(67890), applicationDetail.getOffice().getId());
        assertEquals("Office Display Value", applicationDetail.getOffice().getDisplayValue());
        assertEquals("John", applicationDetail.getClient().getFirstName());
        assertEquals("Doe", applicationDetail.getClient().getSurname());
        assertEquals("Client Reference", applicationDetail.getClient().getReference());
        assertEquals("Supervisor ID", applicationDetail.getSupervisor().getId());
        assertEquals("Supervisor Display Value", applicationDetail.getSupervisor().getDisplayValue());
        assertEquals("Fee Earner ID", applicationDetail.getFeeEarner().getId());
        assertEquals("Fee Earner Display Value", applicationDetail.getFeeEarner().getDisplayValue());
        assertEquals("Provider Contact ID", applicationDetail.getProviderContact().getId());
        assertEquals("Provider Contact Display Value", applicationDetail.getProviderContact().getDisplayValue());
        assertEquals("Category of Law", applicationDetail.getCategoryOfLaw().getId());
        assertEquals("Category of Law Display Value", applicationDetail.getCategoryOfLaw().getDisplayValue());
        assertEquals("Relation to Linked Case", applicationDetail.getRelationToLinkedCase());
    }

    private static Application getApplication() {
        Application application = new Application();
        application.setProviderId("12345");
        application.setProviderDisplayValue("Provider Display Value");
        application.setProviderCaseReference("Provider Case Reference");
        application.setOfficeId(67890L);
        application.setOfficeDisplayValue("Office Display Value");
        application.setClientFirstName("John");
        application.setClientSurname("Doe");
        application.setClientReference("Client Reference");
        application.setSupervisor("Supervisor ID");
        application.setSupervisorDisplayValue("Supervisor Display Value");
        application.setFeeEarner("Fee Earner ID");
        application.setFeeEarnerDisplayValue("Fee Earner Display Value");
        application.setProviderContact("Provider Contact ID");
        application.setProviderContactDisplayValue("Provider Contact Display Value");
        application.setCategoryOfLaw("Category of Law");
        application.setCategoryOfLawDisplayValue("Category of Law Display Value");
        application.setRelationToLinkedCase("Relation to Linked Case");
        application.setOpponentAppliedForFunding(true);
        application.setDisplayStatus("Display Status");
        application.setActualStatus("Actual Status");
        application.setAmendment(true);
        application.setApplicationType("Application Type");
        application.setApplicationTypeDisplayValue("Application Type Display Value");
        application.setDevolvedPowersUsed(true);
        application.setDateDevolvedPowersUsed(new Date());
        application.setDevolvedPowersContractFlag("Contract Flag");
        application.setLarScopeFlag(true);
        application.setMeansAssessmentAmended(true);
        application.setMeritsAssessmentAmended(true);
        application.setCostLimitChanged(true);
        application.setCostLimitAtTimeOfMerits(new BigDecimal("10000.00"));
        application.setMeritsReassessmentReqdInd(true);
        application.setLeadProceedingChangedOpaInput(true);
        return application;
    }

    @Test
    void testToApplicationDetailWithValidInput_opponents() {
        Application application = getApplicationWithOpponents();

        // Call toApplicationDetail(Application)
        ApplicationDetail applicationDetail = mapper.toApplicationDetail(application);

        // Verify the resulting ApplicationDetail instance
        assertNotNull(applicationDetail);

        // Assuming there's a method getOpponentDetails in ApplicationDetail class that returns a List of OpponentDetail
        List<uk.gov.laa.ccms.caab.model.Opponent> opponentDetails = applicationDetail.getOpponents();
        assertNotNull(opponentDetails);
        assertEquals(2, opponentDetails.size());

        uk.gov.laa.ccms.caab.model.Opponent opponentDetail1 = opponentDetails.get(0);
        assertEquals("Relative", opponentDetail1.getRelationshipToCase());
        assertEquals("Type1", opponentDetail1.getType());
        assertNotNull(opponentDetail1.getAuditTrail());  // assuming AuditTrail maps to AuditDetail with a straightforward mapping

        uk.gov.laa.ccms.caab.model.Opponent opponentDetail2 = opponentDetails.get(1);
        assertEquals("Friend", opponentDetail2.getRelationshipToCase());
        assertEquals("Type2", opponentDetail2.getType());
        assertNotNull(opponentDetail2.getAuditTrail());  // assuming AuditTrail maps to AuditDetail with a straightforward mapping
    }

    private Application getApplicationWithOpponents() {
        Opponent opponent1 = new Opponent();
        opponent1.setRelationshipToCase("Relative");
        opponent1.setType("Type1");
        AuditTrail auditTrail1 = new AuditTrail();  // assuming AuditTrail class has default constructor
        opponent1.setAuditTrail(auditTrail1);

        Opponent opponent2 = new Opponent();
        opponent2.setRelationshipToCase("Friend");
        opponent2.setType("Type2");
        AuditTrail auditTrail2 = new AuditTrail();  // assuming AuditTrail class has default constructor
        opponent2.setAuditTrail(auditTrail2);

        List<Opponent> opponents = Arrays.asList(opponent1, opponent2);

        // Create an Application instance and set its properties
        Application application = new Application();
        application.setOpponents(opponents);  // assuming setOpponents is a setter method in Application class
        return application;
    }

    @Test
    void testToApplicationDetailWithValidInput_proceedings() {
        // Create a list of Proceeding instances and set their properties
        Application application = getApplicationWithProceedings();

        // Call toApplicationDetail(Application)
        ApplicationDetail applicationDetail = mapper.toApplicationDetail(application);

        // Verify the resulting ApplicationDetail instance
        assertNotNull(applicationDetail);

        // Assuming there's a method getProceedingDetails in ApplicationDetail class that returns a List of ProceedingDetail
        List<uk.gov.laa.ccms.caab.model.Proceeding> proceedingDetails = applicationDetail.getProceedings();
        assertNotNull(proceedingDetails);
        assertEquals(2, proceedingDetails.size());

        uk.gov.laa.ccms.caab.model.Proceeding proceedingDetail1 = proceedingDetails.get(0);
        assertEquals("Stage1", proceedingDetail1.getStage());
        assertNotNull(proceedingDetail1.getAuditTrail());  // assuming AuditTrail maps to AuditDetail with a straightforward mapping

        uk.gov.laa.ccms.caab.model.Proceeding proceedingDetail2 = proceedingDetails.get(1);
        assertEquals("Stage2", proceedingDetail2.getStage());
        assertNotNull(proceedingDetail2.getAuditTrail());  // assuming AuditTrail maps to AuditDetail with a straightforward mapping
    }

    @Test
    public void testAddApplicationTypeWithNullType() {
        Application application = new Application();
        String caabUserLoginId = "user123";

        mapper.addApplicationType(application, null);

        assertNull(application.getApplicationType());
        assertNull(application.getApplicationTypeDisplayValue());
        assertNull(application.getDevolvedPowersUsed());
        assertNull(application.getDateDevolvedPowersUsed());
        assertNull(application.getDevolvedPowersContractFlag());
    }

    @Test
    public void testAddApplicationType() {
        Application application = new Application();
        ApplicationType applicationType = new ApplicationType();
        applicationType.setId("TEST");
        applicationType.setDisplayValue("TEST123");
        DevolvedPowers devolvedPowers = new DevolvedPowers();
        devolvedPowers.setContractFlag("Y");
        devolvedPowers.setUsed(false);
        applicationType.setDevolvedPowers(devolvedPowers);
        String caabUserLoginId = "user123";

        mapper.addApplicationType(application, applicationType);

        assertEquals("TEST",application.getApplicationType());
        assertEquals("TEST123",application.getApplicationTypeDisplayValue());
        assertFalse(application.getDevolvedPowersUsed());
        assertNull(application.getDateDevolvedPowersUsed());
        assertEquals("Y",application.getDevolvedPowersContractFlag());
    }

    private Application getApplicationWithProceedings() {
        Proceeding proceeding1 = new Proceeding();
        proceeding1.setStage("Stage1");
        AuditTrail auditTrail1 = new AuditTrail();
        proceeding1.setAuditTrail(auditTrail1);

        Proceeding proceeding2 = new Proceeding();
        proceeding2.setStage("Stage2");
        AuditTrail auditTrail2 = new AuditTrail();
        proceeding2.setAuditTrail(auditTrail2);

        List<Proceeding> proceedings = Arrays.asList(proceeding1, proceeding2);

        Application application = new Application();
        application.setProceedings(proceedings);
        return application;
    }

    @Test
    void testToApplicationDetailWithValidInput_priorAuthorities() {
        // Create a list of PriorAuthority instances and set their properties
        Application application = getApplicationWithPriorAuthorities();

        // Call toApplicationDetail(Application)
        ApplicationDetail applicationDetail = mapper.toApplicationDetail(application);

        // Verify the resulting ApplicationDetail instance
        assertNotNull(applicationDetail);

        // Assuming there's a method getPriorAuthorityDetails in ApplicationDetail class that returns a List of PriorAuthorityDetail
        List<uk.gov.laa.ccms.caab.model.PriorAuthority> priorAuthorityDetails = applicationDetail.getPriorAuthorities();
        assertNotNull(priorAuthorityDetails);
        assertEquals(2, priorAuthorityDetails.size());

        uk.gov.laa.ccms.caab.model.PriorAuthority priorAuthorityDetail1 = priorAuthorityDetails.get(0);
        assertNotNull(priorAuthorityDetail1.getAuditTrail());  // assuming AuditTrail maps to AuditDetail with a straightforward mapping

        uk.gov.laa.ccms.caab.model.PriorAuthority priorAuthorityDetail2 = priorAuthorityDetails.get(1);
        assertNotNull(priorAuthorityDetail2.getAuditTrail());  // assuming AuditTrail maps to AuditDetail with a straightforward mapping
    }

    private Application getApplicationWithPriorAuthorities() {
        PriorAuthority priorAuthority1 = new PriorAuthority();
        AuditTrail auditTrail1 = new AuditTrail();  // assuming AuditTrail class has default constructor
        priorAuthority1.setAuditTrail(auditTrail1);

        PriorAuthority priorAuthority2 = new PriorAuthority();
        AuditTrail auditTrail2 = new AuditTrail();  // assuming AuditTrail class has default constructor
        priorAuthority2.setAuditTrail(auditTrail2);

        List<PriorAuthority> priorAuthorities = Arrays.asList(priorAuthority1, priorAuthority2);

        // Create an Application instance and set its properties
        Application application = new Application();
        application.setPriorAuthorities(priorAuthorities);  // assuming setPriorAuthorities is a setter method in Application class
        return application;
    }

    @Test
    public void testToApplicationType() {
        Application application = new Application();
        application.setApplicationType("AppType123");
        application.setApplicationTypeDisplayValue("Application Type Display");

        application.setDevolvedPowersUsed(true);
        application.setDateDevolvedPowersUsed(new Date());
        application.setDevolvedPowersContractFlag("ContractFlag123");

        ApplicationType result = mapper.toApplicationType(application);

        assertNotNull(result);
        assertEquals("AppType123", result.getId());
        assertEquals("Application Type Display", result.getDisplayValue());

        assertNotNull(result.getDevolvedPowers());
        assertEquals(true, result.getDevolvedPowers().getUsed());
        assertNotNull(result.getDevolvedPowers().getDateUsed());
        assertEquals("ContractFlag123", result.getDevolvedPowers().getContractFlag());

    }

    @Test
    public void testAddProviderDetailsWithNullDetails() {
        Application application = new Application();
        String caabUserLoginId = "user123";

        mapper.addProviderDetails(application, null);

        assertNull(application.getProviderId());
        assertNull(application.getProviderDisplayValue());
        assertNull(application.getOfficeId());
        assertNull(application.getOfficeDisplayValue());
        assertNull(application.getSupervisor());
        assertNull(application.getSupervisorDisplayValue());
        assertNull(application.getFeeEarner());
        assertNull(application.getFeeEarnerDisplayValue());
        assertNull(application.getProviderContact());
        assertNull(application.getProviderContactDisplayValue());
    }

    @Test
    public void testAddProviderDetails() {
        Application application = new Application();
        ApplicationProviderDetails providerDetails = new ApplicationProviderDetails();
        providerDetails.setProvider(new IntDisplayValue().id(123).displayValue("Provider Display"));
        providerDetails.setOffice(new IntDisplayValue().id(1).displayValue("Office 1"));
        providerDetails.setSupervisor(new StringDisplayValue().id("Supervisor").displayValue("Supervisor Display Value"));
        providerDetails.setFeeEarner(new StringDisplayValue().id("FeeEarner").displayValue("Fee Earner Display Value"));
        providerDetails.setProviderContact(new StringDisplayValue().id("ProviderContact").displayValue("Provider Contact Display Value"));
        String caabUserLoginId = "user123";

        mapper.addProviderDetails(application, providerDetails);

        assertEquals("123", application.getProviderId());
        assertEquals("Provider Display", application.getProviderDisplayValue());
        assertEquals(1, application.getOfficeId().intValue());
        assertEquals("Office 1", application.getOfficeDisplayValue());
        assertEquals("Supervisor", application.getSupervisor());
        assertEquals("Supervisor Display Value", application.getSupervisorDisplayValue());
        assertEquals("FeeEarner", application.getFeeEarner());
        assertEquals("Fee Earner Display Value", application.getFeeEarnerDisplayValue());
        assertEquals("ProviderContact", application.getProviderContact());
        assertEquals("Provider Contact Display Value", application.getProviderContactDisplayValue());
    }

    @Test
    public void testToProviderDetails() {
        Application application = new Application();
        application.setProviderId("123");
        application.setProviderDisplayValue("Provider Display Value");
        application.setOfficeId(1L);
        application.setOfficeDisplayValue("Office 1");
        application.setSupervisorDisplayValue("Supervisor Display Value");
        application.setFeeEarnerDisplayValue("Fee Earner Display Value");
        application.setProviderContactDisplayValue("Provider Contact Display Value");
        application.setProviderCaseReference("ProviderCase123");

        ApplicationProviderDetails result = mapper.toProviderDetails(application);

        assertNotNull(result);
        assertEquals(123, result.getProvider().getId());
        assertEquals("Provider Display Value", result.getProvider().getDisplayValue());
        assertEquals(1, result.getOffice().getId());
        assertEquals("Office 1", result.getOffice().getDisplayValue());
        assertEquals("Supervisor Display Value", result.getSupervisor().getDisplayValue());
        assertEquals("Fee Earner Display Value", result.getFeeEarner().getDisplayValue());
        assertEquals("Provider Contact Display Value", result.getProviderContact().getDisplayValue());
        assertEquals("ProviderCase123", result.getProviderCaseReference());
    }

    @Test
    void updateAddress_updatesFieldsWhenModelIsNotNull() {
        Address address = buildAddress();
        uk.gov.laa.ccms.caab.model.Address addressModel = new uk.gov.laa.ccms.caab.model.Address();

        addressModel.setPostcode("TestPostcode");
        addressModel.setHouseNameOrNumber("123");

        mapper.updateAddress(address, addressModel);

        assertEquals("TestPostcode", address.getPostCode());
        assertEquals("123", address.getHouseNameNumber());
    }

    @Test
    void updateAddress_doesNothingWhenModelIsNull() {
        ApplicationMapper mapper = new ApplicationMapperImpl(); // Replace with your actual mapper instantiation

        Address address = buildAddress();

        mapper.updateAddress(address, null);

        // Assert that all fields remain unchanged
        assertEquals("InitialPostcode", address.getPostCode());
        assertEquals("InitialHouseNameNumber", address.getHouseNameNumber());
        assertTrue(address.getNoFixedAbode());
        assertEquals("InitialAddressLine1", address.getAddressLine1());
        assertEquals("InitialAddressLine2", address.getAddressLine2());
        assertEquals("InitialCity", address.getCity());
        assertEquals("InitialCounty", address.getCounty());
        assertEquals("InitialCountry", address.getCountry());
        assertEquals("InitialCareOf", address.getCareOf());
        assertEquals("InitialPreferredAddress", address.getPreferredAddress());
    }

    @NotNull
    private static Address buildAddress() {
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


    private AuditDetail buildAuditDetail() {
        AuditDetail auditDetail = new AuditDetail();
        auditDetail.setCreated(createdAt);
        auditDetail.setLastSaved(updatedAt);
        auditDetail.setCreatedBy("CreatedBy");
        auditDetail.setLastSavedBy("LastSavedBy");
        return auditDetail;
    }

    private AuditTrail buildAuditTrail() {
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setCreated(createdAt);
        auditTrail.setLastSaved(updatedAt);
        auditTrail.setCreatedBy("CreatedBy");
        auditTrail.setLastSavedBy("LastSavedBy");
        return auditTrail;
    }
}
