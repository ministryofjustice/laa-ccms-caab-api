package uk.gov.laa.ccms.caab.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import uk.gov.laa.ccms.caab.api.entity.Address;
import uk.gov.laa.ccms.caab.api.entity.Application;
import uk.gov.laa.ccms.caab.api.entity.AuditTrail;
import uk.gov.laa.ccms.caab.api.entity.CostStructure;
import uk.gov.laa.ccms.caab.api.entity.Opponent;
import uk.gov.laa.ccms.caab.api.entity.PriorAuthority;
import uk.gov.laa.ccms.caab.api.entity.Proceeding;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.ApplicationProviderDetails;
import uk.gov.laa.ccms.caab.model.ApplicationType;
import uk.gov.laa.ccms.caab.model.AuditDetail;
import uk.gov.laa.ccms.caab.model.Client;
import uk.gov.laa.ccms.caab.model.DevolvedPowers;
import uk.gov.laa.ccms.caab.model.IntDisplayValue;
import uk.gov.laa.ccms.caab.model.StringDisplayValue;

public class ApplicationMapperTest {

    private ApplicationMapper mapper = new ApplicationMapperImpl();

    @Test
    public void testApplicationMapping() {
        // Construct ApplicationDetail
        ApplicationDetail detail = new ApplicationDetail();
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

    }

    @Test
    public void testAddressMapping() {
        // Construct ApplicationDetailCorrespondenceAddress
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
        ApplicationDetail detail = new ApplicationDetail();
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
        uk.gov.laa.ccms.caab.model.Address detailAddress = mapper.toAddress(address);

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

        uk.gov.laa.ccms.caab.model.CostStructure detailCosts = mapper.toCostStructure(costStructure);

        assertEquals(new BigDecimal("100.00"), detailCosts.getDefaultCostLimitation());
        assertEquals(new BigDecimal("200.00"), detailCosts.getGrantedCostLimitation());
        assertEquals(new BigDecimal("300.00"), detailCosts.getRequestedCostLimitation());
    }

    @Test
    public void testToProceedingDetail() {
        // Construct Proceeding
        Proceeding proceeding = new Proceeding();
        AuditTrail auditTrail = new AuditTrail();
        // Set fields on auditTrail as needed
        proceeding.setAuditTrail(auditTrail);
        proceeding.setStage("Stage1");

        // Convert Proceeding to ProceedingDetail
        uk.gov.laa.ccms.caab.model.Proceeding proceedingDetail = mapper.toProceeding(proceeding);

        // Assertions
        assertEquals(mapper.toAuditDetail(auditTrail), proceedingDetail.getAuditTrail());
        assertEquals("Stage1", proceedingDetail.getStage());
    }

    @Test
    public void testToPriorAuthorityDetail() {
        // Construct PriorAuthority
        PriorAuthority priorAuthority = new PriorAuthority();
        AuditTrail auditTrail = new AuditTrail();
        // Set fields on auditTrail as needed
        priorAuthority.setAuditTrail(auditTrail);

        // Convert PriorAuthority to PriorAuthorityDetail
        uk.gov.laa.ccms.caab.model.PriorAuthority priorAuthorityDetail = mapper.toPriorAuthority(priorAuthority);

        // Assertions
        assertEquals(mapper.toAuditDetail(auditTrail), priorAuthorityDetail.getAuditTrail());
    }

    @Test
    public void testToOpponentDetail() {
        // Construct Opponent
        Opponent opponent = new Opponent();
        AuditTrail auditTrail = new AuditTrail();
        // Set fields on auditTrail as needed
        opponent.setAuditTrail(auditTrail);
        opponent.setRelationshipToCase("Relation1");
        opponent.setType("Type1");

        // Convert Opponent to OpponentDetail
        uk.gov.laa.ccms.caab.model.Opponent opponentDetail = mapper.toOpponent(opponent);

        // Assertions
        assertEquals(mapper.toAuditDetail(auditTrail), opponentDetail.getAuditTrail());
        assertEquals("Relation1", opponentDetail.getRelationshipToCase());
        assertEquals("Type1", opponentDetail.getType());
    }

    @Test
    public void testToAuditDetail() {
        // Construct AuditTrail
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setModified(new Date());
        auditTrail.setModifiedBy("ModifiedBy1");
        auditTrail.setCreated(new Date());
        auditTrail.setCreatedBy("CreatedBy1");

        // Convert AuditTrail to AuditDetail
        AuditDetail auditDetail = mapper.toAuditDetail(auditTrail);

        // Assertions
        assertEquals(auditTrail.getModified(), auditDetail.getLastSaved());
        assertEquals(auditTrail.getModifiedBy(), auditDetail.getLastSavedBy());
        assertEquals(auditTrail.getCreated(), auditDetail.getCreated());
        assertEquals(auditTrail.getCreatedBy(), auditDetail.getCreatedBy());
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
        ApplicationType applicationType = null;
        String caabUserLoginId = "user123";

        mapper.addApplicationType(application, applicationType, caabUserLoginId);

        assertNull(application.getApplicationType());
        assertNull(application.getApplicationTypeDisplayValue());
        assertNull(application.getDevolvedPowersUsed());
        assertNull(application.getDateDevolvedPowersUsed());
        assertNull(application.getDevolvedPowersContractFlag());
        assertEquals("user123", application.getAuditTrail().getModifiedBy());
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

        mapper.addApplicationType(application, applicationType, caabUserLoginId);

        assertEquals("TEST",application.getApplicationType());
        assertEquals("TEST123",application.getApplicationTypeDisplayValue());
        assertFalse(application.getDevolvedPowersUsed());
        assertNull(application.getDateDevolvedPowersUsed());
        assertEquals("Y",application.getDevolvedPowersContractFlag());
        assertEquals("user123", application.getAuditTrail().getModifiedBy());
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
        ApplicationProviderDetails providerDetails = null;
        String caabUserLoginId = "user123";

        mapper.addProviderDetails(application, providerDetails, caabUserLoginId);

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
        assertEquals("user123", application.getAuditTrail().getModifiedBy());
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

        mapper.addProviderDetails(application, providerDetails, caabUserLoginId);

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
        assertEquals("user123", application.getAuditTrail().getModifiedBy());
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
}
