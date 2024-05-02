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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import uk.gov.laa.ccms.caab.api.entity.Address;
import uk.gov.laa.ccms.caab.api.entity.Application;
import uk.gov.laa.ccms.caab.api.entity.AuditTrail;
import uk.gov.laa.ccms.caab.api.entity.CaseOutcome;
import uk.gov.laa.ccms.caab.api.entity.CostEntry;
import uk.gov.laa.ccms.caab.api.entity.CostStructure;
import uk.gov.laa.ccms.caab.api.entity.LinkedCase;
import uk.gov.laa.ccms.caab.api.entity.Opponent;
import uk.gov.laa.ccms.caab.api.entity.PriorAuthority;
import uk.gov.laa.ccms.caab.api.entity.Proceeding;
import uk.gov.laa.ccms.caab.api.entity.ReferenceDataItem;
import uk.gov.laa.ccms.caab.api.entity.ScopeLimitation;
import uk.gov.laa.ccms.caab.model.AddressDetail;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.ApplicationDetails;
import uk.gov.laa.ccms.caab.model.ApplicationProviderDetails;
import uk.gov.laa.ccms.caab.model.ApplicationType;
import uk.gov.laa.ccms.caab.model.AuditDetail;
import uk.gov.laa.ccms.caab.model.BaseApplicationDetail;
import uk.gov.laa.ccms.caab.model.BooleanDisplayValue;
import uk.gov.laa.ccms.caab.model.ClientDetail;
import uk.gov.laa.ccms.caab.model.CostEntryDetail;
import uk.gov.laa.ccms.caab.model.CostLimitDetail;
import uk.gov.laa.ccms.caab.model.CostStructureDetail;
import uk.gov.laa.ccms.caab.model.DevolvedPowersDetail;
import uk.gov.laa.ccms.caab.model.IntDisplayValue;
import uk.gov.laa.ccms.caab.model.LinkedCaseDetail;
import uk.gov.laa.ccms.caab.model.OpponentDetail;
import uk.gov.laa.ccms.caab.model.PriorAuthorityDetail;
import uk.gov.laa.ccms.caab.model.ProceedingDetail;
import uk.gov.laa.ccms.caab.model.ReferenceDataItemDetail;
import uk.gov.laa.ccms.caab.model.ScopeLimitationDetail;
import uk.gov.laa.ccms.caab.model.StringDisplayValue;

@ExtendWith(MockitoExtension.class)
public class ApplicationMapperTest {
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private CommonMapper commonMapper;

    @InjectMocks
    private final ApplicationMapper mapper = new ApplicationMapperImpl();

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
        ApplicationDetail detail = new ApplicationDetail();
        detail.setCaseReferenceNumber("caseRef123");

        ApplicationProviderDetails providerDetails = new ApplicationProviderDetails();
        providerDetails.setProvider(new IntDisplayValue().id(1234).displayValue("providerDisp"));
        providerDetails.setOffice(new IntDisplayValue().id(1).displayValue("officeDisp"));
        providerDetails.setSupervisor(new StringDisplayValue().id("supervisorId").displayValue("supervisorDisp"));
        providerDetails.setFeeEarner(new StringDisplayValue().id("feeEarnerId").displayValue("feeEarnerDisp"));
        providerDetails.setProviderContact(new StringDisplayValue().id("providerContactId").displayValue("providerContactDisp"));
        providerDetails.setProviderCaseReference("providerCase");

        detail.setProviderDetails(providerDetails);
        detail.setCategoryOfLaw(new StringDisplayValue().id("categoryLawId").displayValue("categoryLawDisp"));
        detail.setStatus(new StringDisplayValue().id("statusId").displayValue("statusDisp"));
        detail.setClient(new ClientDetail().firstName("clientFirst").surname("clientSurname").reference("clientRef"));
        detail.setApplicationType(new ApplicationType().id("appTypeId").displayValue("appTypeDisp"));
        detail.setMeritsReassessmentRequired(true);
        detail.setLeadProceedingChanged(true);
        detail.setMeansAssessmentAmended(true);
        detail.setMeritsAssessmentAmended(true);
        detail.setAmendment(true);

        CostLimitDetail costLimit = new CostLimitDetail()
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
        assertTrue(application.getCostLimitChanged());
        assertEquals(BigDecimal.valueOf(123), application.getCostLimitAtTimeOfMerits());
        assertTrue(application.getAmendment());
        assertTrue(application.getMeansAssessmentAmended());
        assertTrue(application.getMeritsAssessmentAmended());

    }

    @Test
    void mapIntoApplication_updatesAllFields() {
        Application application = new Application();
        ApplicationDetail applicationDetail = new ApplicationDetail();

        applicationDetail.setCaseReferenceNumber("12345");
        ApplicationProviderDetails providerDetails = new ApplicationProviderDetails();
        providerDetails.setProvider(new IntDisplayValue().id(1).displayValue("Provider"));
        providerDetails.setOffice(new IntDisplayValue().id(2).displayValue("Office"));
        providerDetails.setSupervisor(new StringDisplayValue().id("S123").displayValue("Supervisor"));
        providerDetails.setFeeEarner(new StringDisplayValue().id("F123").displayValue("Fee Earner"));
        providerDetails.setProviderContact(new StringDisplayValue().id("P123").displayValue("Provider Contact"));
        applicationDetail.setProviderDetails(providerDetails);
        applicationDetail.setCategoryOfLaw(new StringDisplayValue().id("C123").displayValue("Category of Law"));
        applicationDetail.setStatus(new StringDisplayValue().id("ST123").displayValue("Status"));

        ClientDetail client = new ClientDetail();
        client.setFirstName("John");
        client.setSurname("Doe");
        client.setReference("Ref123");
        applicationDetail.setClient(client);

        mapper.mapIntoApplication(application, applicationDetail);

        assertEquals("12345", application.getLscCaseReference());
        assertEquals("1", application.getProviderId());
        assertEquals("Provider", application.getProviderDisplayValue());
        assertEquals("2", application.getOfficeId().toString());
        assertEquals("Office", application.getOfficeDisplayValue());
        assertEquals("S123", application.getSupervisor());
        assertEquals("Supervisor", application.getSupervisorDisplayValue());
        assertEquals("F123", application.getFeeEarner());
        assertEquals("Fee Earner", application.getFeeEarnerDisplayValue());
        assertEquals("P123", application.getProviderContact());
        assertEquals("Provider Contact", application.getProviderContactDisplayValue());
        assertEquals("C123", application.getCategoryOfLaw());
        assertEquals("Category of Law", application.getCategoryOfLawDisplayValue());
        assertEquals("ST123", application.getActualStatus());
        assertEquals("Status", application.getDisplayStatus());
        assertEquals("John", application.getClientFirstName());
        assertEquals("Doe", application.getClientSurname());
        assertEquals("Ref123", application.getClientReference());
    }

    @Test
    void mapIntoApplication_ignoresNullApplicationDetail() {
        Application application = new Application();
        application.setLscCaseReference("Original");

        mapper.mapIntoApplication(application, null);

        assertEquals("Original", application.getLscCaseReference());
    }


    @Test
    public void testApplicationMapping_nullBooleansDefaultCorrectly() {
        ApplicationDetail detail = new ApplicationDetail();

        Application application = mapper.toApplication(detail);

        assertFalse(application.getCostLimitChanged());
        assertFalse(application.getAmendment());
        assertFalse(application.getMeansAssessmentAmended());
        assertFalse(application.getMeritsAssessmentAmended());
    }

    @Test
    public void testToApplication_costStructure_costEntry() {
        // Construct an ApplicationDetail instance with costEntry
        ApplicationDetail applicationDetail = new ApplicationDetail();

        CostEntryDetail costEntryModel = new CostEntryDetail();
        costEntryModel.setRequestedCosts(BigDecimal.valueOf(1000));
        costEntryModel.setCostCategory("Legal Fees");

        CostStructureDetail costStructureModel = new CostStructureDetail();
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
        ProceedingDetail proceedingModel = new ProceedingDetail();
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
        assertTrue(proceeding.getEdited());
        // For collections like scopeLimitations, you might need to assert both the size and the contents
        assertEquals(0, proceeding.getScopeLimitations().size());
        // Add more detailed checks for elements in scopeLimitations if necessary
        assertEquals(proceedingModel.getDefaultScopeLimitation(), proceeding.getDefaultScopeLimitation());
        assertEquals(proceedingModel.getStage(), proceeding.getStage());
        assertTrue(proceeding.getLeadProceedingInd());
        assertEquals(proceedingModel.getLarScope(), proceeding.getLarScope());

    }

    @Test
    public void testProceedingMapping_checkBooleanDefaults() {
        ProceedingDetail proceedingModel =
            new ProceedingDetail();

        Proceeding proceeding = mapper.toProceeding(proceedingModel);
        assertFalse(proceeding.getLeadProceedingInd());
        assertFalse(proceeding.getEdited());
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
        ProceedingDetail proceedingDetail = mapper.toProceedingModel(proceeding);

        // Assertions
        assertEquals("Stage1", proceedingDetail.getStage());
    }

    @Test
    public void testToProceedingModel_null() {
        assertNull(mapper.toProceedingModel(null));
    }



    @Test
    public void testToPriorAuthority(){
        PriorAuthorityDetail priorAuthorityModel = new PriorAuthorityDetail();
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

        PriorAuthorityDetail priorAuthorityDetail
            = mapper.toPriorAuthorityModel(priorAuthority);

        //TODO
    }

    @Test
    public void testToPriorAuthorityModel_null(){
        assertNull(mapper.toPriorAuthorityModel(null));
    }

    @Test
    public void testToReferenceDataItem(){

        ReferenceDataItemDetail referenceDataItemModel =
            new ReferenceDataItemDetail();

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

        ReferenceDataItemDetail referenceDataItemModel = mapper.toReferenceDataItemModel(referenceDataItem);

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
        ScopeLimitationDetail scopeLimitationModel = new ScopeLimitationDetail();
        scopeLimitationModel.setScopeLimitation(new StringDisplayValue().id("SL").displayValue("ScopeLimitation"));
        scopeLimitationModel.setScopeLimitationWording("Wording");
        scopeLimitationModel.setDelegatedFuncApplyInd(new BooleanDisplayValue().flag(true).displayValue(""));
        scopeLimitationModel.setAuditTrail(buildAuditDetail());
        scopeLimitationModel.setEbsId("EBSID");
        scopeLimitationModel.setDefaultInd(true);

        ScopeLimitation scopeLimitation = mapper.toScopeLimitation(scopeLimitationModel);

        assertEquals("SL", scopeLimitation.getScopeLimitation());
        assertEquals("ScopeLimitation", scopeLimitation.getScopeLimitationDisplayValue());
        assertTrue(scopeLimitation.getDelegatedFuncApplyInd());
        assertEquals("EBSID", scopeLimitation.getEbsId());
        assertEquals("Wording", scopeLimitation.getScopeLimitationWording());
        assertTrue(scopeLimitation.getDefaultInd());
    }

    @Test
    public void testToScopeLimitation_checkBooleanDefaults() {
        ScopeLimitationDetail scopeLimitationModel =
            new ScopeLimitationDetail();

        ScopeLimitation scopeLimitation = mapper.toScopeLimitation(scopeLimitationModel);

        assertFalse(scopeLimitation.getDelegatedFuncApplyInd());
        assertFalse(scopeLimitation.getDefaultInd());
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

        ScopeLimitationDetail scopeLimitation = mapper.toScopeLimitationModel(scopeLimitationEntity);

        assertEquals("SL", scopeLimitation.getScopeLimitation().getId());
        assertEquals("ScopeLimitation", scopeLimitation.getScopeLimitation().getDisplayValue());
        assertTrue(scopeLimitation.getDelegatedFuncApplyInd().getFlag());
        assertEquals("EBSID", scopeLimitation.getEbsId());
        assertEquals("Wording", scopeLimitation.getScopeLimitationWording());
        assertTrue(scopeLimitation.getDefaultInd());
    }

    @Test
    public void testToScopeLimitationModel_null() {
        assertNull(mapper.toScopeLimitationModel(null));
    }

    @Test
    public void testToOpponent() {
        OpponentDetail opponentModel = buildOpponentModel(createdAt);

        Opponent opponent = mapper.toOpponent(opponentModel);

        assertNotNull(opponent);
        assertEquals(opponentModel.getOrganisationType(), opponent.getOrganisationType());
        assertEquals(opponentModel.getEbsId(), opponent.getEbsId());
        assertEquals(opponentModel.getType(), opponent.getType());
        assertEquals(opponentModel.getTitle(), opponent.getTitle());
        assertEquals(opponentModel.getFirstName(), opponent.getFirstName());
        assertEquals(opponentModel.getMiddleNames(), opponent.getMiddleNames());
        assertEquals(opponentModel.getSurname(), opponent.getSurname());
        assertEquals(opponentModel.getDateOfBirth(), opponent.getDateOfBirth());
        assertEquals(opponentModel.getNationalInsuranceNumber(), opponent.getNationalInsuranceNumber());
        assertEquals(opponentModel.getRelationshipToCase(), opponent.getRelationshipToCase());
        assertEquals(opponentModel.getRelationshipToClient(), opponent.getRelationshipToClient());
        assertEquals(opponentModel.getTelephoneHome(), opponent.getTelephoneHome());
        assertEquals(opponentModel.getTelephoneWork(), opponent.getTelephoneWork());
        assertEquals(opponentModel.getTelephoneMobile(), opponent.getTelephoneMobile());
        assertEquals(opponentModel.getFaxNumber(), opponent.getFaxNumber());
        assertEquals(opponentModel.getEmailAddress(), opponent.getEmailAddress());
        assertEquals(opponentModel.getOtherInformation(), opponent.getOtherInformation());
        assertEquals(opponentModel.getEmploymentStatus(), opponent.getEmploymentStatus());
        assertEquals(opponentModel.getEmployerName(), opponent.getEmployerName());
        assertEquals(opponentModel.getLegalAided(), opponent.getLegalAided());
        assertEquals(opponentModel.getCertificateNumber(), opponent.getCertificateNumber());
        assertEquals(opponentModel.getCourtOrderedMeansAssessment(), opponent.getCourtOrderedMeansAssessment());
        assertEquals(opponentModel.getAssessedIncome(), opponent.getAssessedIncome());
        assertEquals(opponentModel.getAssessedIncomeFrequency(), opponent.getAssessedIncomeFrequency());
        assertEquals(opponentModel.getAssessedAssets(), opponent.getAssessedAssets());
        assertEquals(opponentModel.getAssessmentDate(), opponent.getAssessmentDate());
        assertEquals(opponentModel.getOrganisationName(), opponent.getOrganisationName());
        assertEquals(opponentModel.getCurrentlyTrading(), opponent.getCurrentlyTrading());
        assertEquals(opponentModel.getContactNameRole(), opponent.getContactNameRole());
        assertEquals(opponentModel.getConfirmed(), opponent.getConfirmed());
        assertEquals(opponentModel.getAppMode(), opponent.getAppMode());
        assertEquals(opponentModel.getAmendment(), opponent.getAmendment());
        assertEquals(opponentModel.getAward(), opponent.getAward());
        assertEquals(opponentModel.getPublicFundingApplied(), opponent.getPublicFundingApplied());
        assertEquals(opponentModel.getSharedInd(), opponent.getSharedInd());
        assertEquals(opponentModel.getDeleteInd(), opponent.getDeleteInd());
    }

    @Test
    public void testToOpponent_checkBooleanDefaults() {
        OpponentDetail opponentDetail =
            new OpponentDetail();

        Opponent opponent = mapper.toOpponent(opponentDetail);

        assertFalse(opponent.getAmendment());
        assertFalse(opponent.getAward());
        assertFalse(opponent.getSharedInd());

        assertTrue(opponent.getAppMode());
        assertTrue(opponent.getDeleteInd());
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

        OpponentDetail opponentDetail = mapper.toOpponentModel(opponent);

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
        LinkedCaseDetail linkedCaseModel = new LinkedCaseDetail();
        linkedCaseModel.setAuditTrail(buildAuditDetail());
        linkedCaseModel.setLscCaseReference("LSC123");
        linkedCaseModel.setRelationToCase("Relation1");
        linkedCaseModel.setProviderCaseReference("Provider123");
        linkedCaseModel.setFeeEarner("FeeEarner1");
        linkedCaseModel.setStatus("Active");
        ClientDetail linkedCaseClient = new ClientDetail();
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
        LinkedCaseDetail linkedCaseModel = mapper.toLinkedCaseModel(linkedCase);

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
        AddressDetail detailAddress =
            new AddressDetail();
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
        CostStructureDetail detailCosts =
            new CostStructureDetail();
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
        ApplicationDetail detail = new ApplicationDetail();
        detail.setCaseReferenceNumber("CASE-001");

        ApplicationProviderDetails providerDetails = new ApplicationProviderDetails();
        providerDetails.setProviderCaseReference("CASE-001");
        providerDetails.setProvider(new IntDisplayValue().id(1234).displayValue("Provider Display"));
        providerDetails.setOffice(new IntDisplayValue().id(1).displayValue("Office 1"));
        providerDetails.setSupervisor(new StringDisplayValue().id("Supervisor").displayValue("Supervisor Display"));
        providerDetails.setFeeEarner(new StringDisplayValue().id("Fee Earner").displayValue("Fee Earner Display"));
        detail.setProviderDetails(providerDetails);

        // Convert ApplicationDetail to Application
        Application application = mapper.toApplication(detail);

        // Assertions
        assertEquals("CASE-001", application.getLscCaseReference());
        assertEquals(1234, Integer.parseInt(application.getProviderId()));
        assertEquals("Provider Display", application.getProviderDisplayValue());
        assertEquals("CASE-001", application.getProviderCaseReference());
        assertEquals(1, application.getOfficeId());
        assertEquals("Office 1", application.getOfficeDisplayValue());
    }

    @Test
    public void testToAddressWithNullValues() {
        // Construct ApplicationDetailCorrespondenceAddress with null values
        AddressDetail detailAddress =
            new AddressDetail();

        // Convert ApplicationDetailCorrespondenceAddress to Address
        Address address = mapper.toAddress(detailAddress);

        // Assertions
        assertNull(address.getPostCode());
        assertNull(address.getHouseNameNumber());
    }

    @Test
    public void testToCostStructureWithZeroValues() {
        // Construct ApplicationDetailCosts with zero values
        CostStructureDetail detailCosts =
            new CostStructureDetail();
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
        AddressDetail detailAddress = mapper.toAddressModel(address);

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

        CostStructureDetail detailCosts = mapper.toCostStructureModel(costStructure);

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
        assertEquals(application.getId().intValue(), applicationDetail.getId());
        assertEquals(Integer.valueOf(12345), applicationDetail.getProviderDetails().getProvider().getId());
        assertEquals("Provider Display Value", applicationDetail.getProviderDetails().getProvider().getDisplayValue());
        assertEquals("Provider Case Reference", applicationDetail.getProviderDetails().getProviderCaseReference());
        assertEquals(Integer.valueOf(67890), applicationDetail.getProviderDetails().getOffice().getId());
        assertEquals("Office Display Value", applicationDetail.getProviderDetails().getOffice().getDisplayValue());
        assertEquals("John", applicationDetail.getClient().getFirstName());
        assertEquals("Doe", applicationDetail.getClient().getSurname());
        assertEquals("Client Reference", applicationDetail.getClient().getReference());
        assertEquals("Supervisor ID", applicationDetail.getProviderDetails().getSupervisor().getId());
        assertEquals("Supervisor Display Value", applicationDetail.getProviderDetails().getSupervisor().getDisplayValue());
        assertEquals("Fee Earner ID", applicationDetail.getProviderDetails().getFeeEarner().getId());
        assertEquals("Fee Earner Display Value", applicationDetail.getProviderDetails().getFeeEarner().getDisplayValue());
        assertEquals("Provider Contact ID", applicationDetail.getProviderDetails().getProviderContact().getId());
        assertEquals("Provider Contact Display Value", applicationDetail.getProviderDetails().getProviderContact().getDisplayValue());
        assertEquals("Category of Law", applicationDetail.getCategoryOfLaw().getId());
        assertEquals("Category of Law Display Value", applicationDetail.getCategoryOfLaw().getDisplayValue());
        assertEquals("Relation to Linked Case", applicationDetail.getRelationToLinkedCase());
    }

    private static Application getApplication() {
        Application application = new Application();
        application.setId(9876L);
        application.setProviderId("12345");
        application.setProviderDisplayValue("Provider Display Value");
        application.setProviderCaseReference("Provider Case Reference");
        application.setOfficeId(67890);
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
        List<OpponentDetail> opponentDetails = applicationDetail.getOpponents();
        assertNotNull(opponentDetails);
        assertEquals(2, opponentDetails.size());

        OpponentDetail opponentDetail1 = opponentDetails.get(0);
        assertEquals("Relative", opponentDetail1.getRelationshipToCase());
        assertEquals("Type1", opponentDetail1.getType());
        assertNotNull(opponentDetail1.getAuditTrail());  // assuming AuditTrail maps to AuditDetail with a straightforward mapping

        OpponentDetail opponentDetail2 = opponentDetails.get(1);
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
        List<ProceedingDetail> proceedingDetails = applicationDetail.getProceedings();
        assertNotNull(proceedingDetails);
        assertEquals(2, proceedingDetails.size());

        ProceedingDetail proceedingDetail1 = proceedingDetails.get(0);
        assertEquals("Stage1", proceedingDetail1.getStage());
        assertNotNull(proceedingDetail1.getAuditTrail());  // assuming AuditTrail maps to AuditDetail with a straightforward mapping

        ProceedingDetail proceedingDetail2 = proceedingDetails.get(1);
        assertEquals("Stage2", proceedingDetail2.getStage());
        assertNotNull(proceedingDetail2.getAuditTrail());  // assuming AuditTrail maps to AuditDetail with a straightforward mapping
    }

    @Test
    public void testAddApplicationTypeWithNullType() {
        Application application = new Application();

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
        DevolvedPowersDetail devolvedPowers = new DevolvedPowersDetail();
        devolvedPowers.setContractFlag("Y");
        devolvedPowers.setUsed(false);
        applicationType.setDevolvedPowers(devolvedPowers);

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
        List<PriorAuthorityDetail> priorAuthorityDetails = applicationDetail.getPriorAuthorities();
        assertNotNull(priorAuthorityDetails);
        assertEquals(2, priorAuthorityDetails.size());

        PriorAuthorityDetail priorAuthorityDetail1 = priorAuthorityDetails.get(0);
        assertNotNull(priorAuthorityDetail1.getAuditTrail());  // assuming AuditTrail maps to AuditDetail with a straightforward mapping

        PriorAuthorityDetail priorAuthorityDetail2 = priorAuthorityDetails.get(1);
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
        application.setOfficeId(1);
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
        AddressDetail addressModel = new AddressDetail();

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

    @Test
    void updateCostStructure_updatesFieldsWhenModelIsNotNull() {
        CostStructure costStructure = new CostStructure();
        CostStructureDetail costStructureModel = new CostStructureDetail()
            .defaultCostLimitation(new BigDecimal("100.00"))
            .requestedCostLimitation(new BigDecimal("200.00"))
            .grantedCostLimitation(new BigDecimal("300.00"));

        mapper.updateCostStructure(costStructure, costStructureModel);

        assertEquals(new BigDecimal("100.00"), costStructure.getDefaultCostLimitation());
        assertEquals(new BigDecimal("200.00"), costStructure.getRequestedCostLimitation());
        assertEquals(new BigDecimal("300.00"), costStructure.getGrantedCostLimitation());
    }

    @Test
    void updateCostStructure_doesNothingWhenModelIsNull() {
        CostStructure costStructure = new CostStructure();

        mapper.updateCostStructure(costStructure, null);

        assertEquals(new BigDecimal("0"),costStructure.getDefaultCostLimitation());
        assertEquals(new BigDecimal("0"), costStructure.getRequestedCostLimitation());
        assertEquals(new BigDecimal("0"), costStructure.getGrantedCostLimitation());
    }

    @Test
    void updateLinkedCase_updatesFieldsWhenModelIsNotNull() {
        LinkedCase linkedCase = new LinkedCase();
        LinkedCaseDetail linkedCaseModel = new LinkedCaseDetail()
            .lscCaseReference("LSC123")
            .relationToCase("Relation1")
            .providerCaseReference("Provider123")
            .feeEarner("FeeEarner1")
            .status("Active");

        mapper.updateLinkedCase(linkedCase, linkedCaseModel);

        assertEquals("LSC123", linkedCase.getLscCaseReference());
        assertEquals("Relation1", linkedCase.getRelationToCase());
        assertEquals("Provider123", linkedCase.getProviderCaseReference());
        assertEquals("FeeEarner1", linkedCase.getFeeEarner());
        assertEquals("Active", linkedCase.getStatus());
    }

    @Test
    void updateLinkedCase_doesNothingWhenModelIsNull() {
        LinkedCase linkedCase = new LinkedCase();

        mapper.updateLinkedCase(linkedCase, null);

        assertNull(linkedCase.getLscCaseReference());
        assertNull(linkedCase.getRelationToCase());
        assertNull(linkedCase.getProviderCaseReference());
        assertNull(linkedCase.getFeeEarner());
        assertNull(linkedCase.getStatus());
    }

    @Test
    void updatePriorAuthority_updatesFieldsWhenModelIsNotNull() {
        PriorAuthority priorAuthority = new PriorAuthority();
        PriorAuthorityDetail priorAuthorityModel = new PriorAuthorityDetail()
            .ebsId("EBSID")
            .status("Status")
            .summary("Summary")
            .justification("Justification");

        mapper.updatePriorAuthority(priorAuthority, priorAuthorityModel);

        assertEquals("EBSID", priorAuthority.getEbsId());
        assertEquals("Status", priorAuthority.getStatus());
        assertEquals("Summary", priorAuthority.getSummary());
        assertEquals("Justification", priorAuthority.getJustification());
    }

    @Test
    void updatePriorAuthority_doesNothingWhenModelIsNull() {
        PriorAuthority priorAuthority = new PriorAuthority();

        mapper.updatePriorAuthority(priorAuthority, null);

        assertNull(priorAuthority.getEbsId());
        assertNull(priorAuthority.getStatus());
        assertNull(priorAuthority.getSummary());
        assertNull(priorAuthority.getJustification());
    }

    @Test
    void testUpdateOpponent() {
        Date date = new Date();
        Opponent opponentBefore = buildOpponent(date);
        Opponent opponent = buildOpponent(date);

        OpponentDetail opponentModel = buildOpponentModel(createdAt);

        mapper.updateOpponent(opponent, opponentModel);

        assertNotNull(opponent);

        // attributes that should be unchanged
        assertEquals(opponentBefore.getId(), opponent.getId());
        assertEquals(opponentBefore.getAuditTrail(), opponent.getAuditTrail());

        assertEquals(opponentModel.getOrganisationType(), opponent.getOrganisationType());
        assertEquals(opponentModel.getEbsId(), opponent.getEbsId());
        assertEquals(opponentModel.getType(), opponent.getType());
        assertEquals(opponentModel.getTitle(), opponent.getTitle());
        assertEquals(opponentModel.getFirstName(), opponent.getFirstName());
        assertEquals(opponentModel.getMiddleNames(), opponent.getMiddleNames());
        assertEquals(opponentModel.getSurname(), opponent.getSurname());
        assertEquals(opponentModel.getDateOfBirth(), opponent.getDateOfBirth());
        assertEquals(opponentModel.getNationalInsuranceNumber(), opponent.getNationalInsuranceNumber());
        assertEquals(opponentModel.getRelationshipToCase(), opponent.getRelationshipToCase());
        assertEquals(opponentModel.getRelationshipToClient(), opponent.getRelationshipToClient());
        assertEquals(opponentModel.getTelephoneHome(), opponent.getTelephoneHome());
        assertEquals(opponentModel.getTelephoneWork(), opponent.getTelephoneWork());
        assertEquals(opponentModel.getTelephoneMobile(), opponent.getTelephoneMobile());
        assertEquals(opponentModel.getFaxNumber(), opponent.getFaxNumber());
        assertEquals(opponentModel.getEmailAddress(), opponent.getEmailAddress());
        assertEquals(opponentModel.getOtherInformation(), opponent.getOtherInformation());
        assertEquals(opponentModel.getEmploymentStatus(), opponent.getEmploymentStatus());
        assertEquals(opponentModel.getEmployerName(), opponent.getEmployerName());
        assertEquals(opponentModel.getLegalAided(), opponent.getLegalAided());
        assertEquals(opponentModel.getCertificateNumber(), opponent.getCertificateNumber());
        assertEquals(opponentModel.getCourtOrderedMeansAssessment(), opponent.getCourtOrderedMeansAssessment());
        assertEquals(opponentModel.getAssessedIncome(), opponent.getAssessedIncome());
        assertEquals(opponentModel.getAssessedIncomeFrequency(), opponent.getAssessedIncomeFrequency());
        assertEquals(opponentModel.getAssessedAssets(), opponent.getAssessedAssets());
        assertEquals(opponentModel.getAssessmentDate(), opponent.getAssessmentDate());
        assertEquals(opponentModel.getOrganisationName(), opponent.getOrganisationName());
        assertEquals(opponentModel.getCurrentlyTrading(), opponent.getCurrentlyTrading());
        assertEquals(opponentModel.getContactNameRole(), opponent.getContactNameRole());
        assertEquals(opponentModel.getConfirmed(), opponent.getConfirmed());
        assertEquals(opponentModel.getAppMode(), opponent.getAppMode());
        assertEquals(opponentModel.getAmendment(), opponent.getAmendment());
        assertEquals(opponentModel.getAward(), opponent.getAward());
        assertEquals(opponentModel.getPublicFundingApplied(), opponent.getPublicFundingApplied());
        assertEquals(opponentModel.getSharedInd(), opponent.getSharedInd());
        assertEquals(opponentModel.getDeleteInd(), opponent.getDeleteInd());
    }

    @Test
    public void testToBaseApplication() {
        Application application = getApplication();

        BaseApplicationDetail result = mapper.toBaseApplication(application);

        assertNotNull(result);
        assertEquals(application.getId().intValue(), result.getId());
        assertEquals(application.getLscCaseReference(), result.getCaseReferenceNumber());
        assertEquals(application.getCategoryOfLaw(), result.getCategoryOfLaw().getId());
        assertEquals(application.getCategoryOfLawDisplayValue(), result.getCategoryOfLaw().getDisplayValue());
        assertEquals(application.getActualStatus(), result.getStatus().getId());
        assertEquals(application.getDisplayStatus(), result.getStatus().getDisplayValue());
        assertEquals(application.getClientFirstName(), result.getClient().getFirstName());
        assertEquals(application.getClientSurname(), result.getClient().getSurname());
        assertEquals(application.getClientReference(), result.getClient().getReference());

        assertEquals(Integer.valueOf(application.getProviderId()), result.getProviderDetails().getProvider().getId());
        assertEquals(application.getProviderDisplayValue(), result.getProviderDetails().getProvider().getDisplayValue());
        assertEquals(application.getOfficeId(), result.getProviderDetails().getOffice().getId());
        assertEquals(application.getOfficeDisplayValue(), result.getProviderDetails().getOffice().getDisplayValue());
        assertEquals(application.getSupervisorDisplayValue(), result.getProviderDetails().getSupervisor().getDisplayValue());
        assertEquals(application.getFeeEarnerDisplayValue(), result.getProviderDetails().getFeeEarner().getDisplayValue());
        assertEquals(application.getProviderContactDisplayValue(), result.getProviderDetails().getProviderContact().getDisplayValue());
        assertEquals(application.getProviderCaseReference(), result.getProviderDetails().getProviderCaseReference());
    }

    @Test
    public void testToApplicationDetails() {
        Page<Application> applications = new PageImpl<>(Arrays.asList(new Application(), new Application()));

        ApplicationDetails result = mapper.toApplicationDetails(applications);

        assertNotNull(result);
        assertEquals(2, result.getSize());
        assertEquals(2, result.getContent().size());
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

    private Opponent buildOpponent(Date date) {
        Opponent opponent = new Opponent();
        opponent.setId(Long.valueOf("11111"));
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
        opponent.setId(Long.parseLong("1"));
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

    public OpponentDetail buildOpponentModel(java.util.Date date) {
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
}
