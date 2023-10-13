//package uk.gov.laa.ccms.caab.mapper;
//
//<<<<<<< HEAD
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import org.jetbrains.annotations.NotNull;
//=======
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.math.BigDecimal;
//>>>>>>> main
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//import uk.gov.laa.ccms.api.entity.Address;
//import uk.gov.laa.ccms.api.entity.Application;
//import uk.gov.laa.ccms.api.entity.AuditTrail;
//import uk.gov.laa.ccms.api.entity.CostStructure;
//import uk.gov.laa.ccms.api.entity.Opponent;
//import uk.gov.laa.ccms.api.entity.PriorAuthority;
//import uk.gov.laa.ccms.api.entity.Proceeding;
//import uk.gov.laa.ccms.api.mapper.ApplicationMapper;
//import uk.gov.laa.ccms.api.mapper.ApplicationMapperImpl;
//import uk.gov.laa.ccms.caab.model.ApplicationDetail;
//<<<<<<< HEAD
//import uk.gov.laa.ccms.caab.model.ApplicationDetailCorrespondenceAddress;
//import uk.gov.laa.ccms.caab.model.ApplicationDetailCosts;
//import uk.gov.laa.ccms.caab.model.ApplicationDetailProvider;
//import uk.gov.laa.ccms.caab.model.AuditDetail;
//=======
//import uk.gov.laa.ccms.caab.model.Client;
//>>>>>>> main
//import uk.gov.laa.ccms.caab.model.IntDisplayValue;
//import uk.gov.laa.ccms.caab.model.OpponentDetail;
//import uk.gov.laa.ccms.caab.model.PriorAuthorityDetail;
//import uk.gov.laa.ccms.caab.model.ProceedingDetail;
//import uk.gov.laa.ccms.caab.model.StringDisplayValue;
//
//public class ApplicationMapperTest {
//
//    private ApplicationMapper mapper = new ApplicationMapperImpl();
//
//    @Test
//    public void testApplicationMapping() {
//        // Construct ApplicationDetail
//        ApplicationDetail detail = new ApplicationDetail();
//        detail.setCaseReferenceNumber("caseRef123");
//        detail.setProviderCaseReference("providerCase");
//        detail.setProvider(new IntDisplayValue().id(1234).displayValue("providerDisp"));
//        detail.setOffice(new IntDisplayValue().id(1).displayValue("officeDisp"));
//        detail.setSupervisor(new StringDisplayValue().id("supervisorId").displayValue("supervisorDisp"));
//        detail.setFeeEarner(new StringDisplayValue().id("feeEarnerId").displayValue("feeEarnerDisp"));
//        detail.setProviderContact(new StringDisplayValue().id("providerContactId").displayValue("providerContactDisp"));
//        detail.setCategoryOfLaw(new StringDisplayValue().id("categoryLawId").displayValue("categoryLawDisp"));
//        detail.setStatus(new StringDisplayValue().id("statusId").displayValue("statusDisp"));
//        detail.setClient(new Client().firstName("clientFirst").surname("clientSurname").reference("clientRef"));
//        detail.setApplicationType(new StringDisplayValue().id("appTypeId").displayValue("appTypeDisp"));
//        detail.setMeritsReassessmentRequired(true);
//        detail.setLeadProceedingChanged(true);
//
//        // Convert ApplicationDetail to Application
//        Application application = mapper.toApplication(detail);
//
//        assertNull(application.getId()); // ID should be ignored and not set
//        assertEquals("caseRef123", application.getLscCaseReference());
//        assertEquals(1234, Integer.parseInt(application.getProviderId()));
//        assertEquals("providerDisp", application.getProviderDisplayValue());
//        assertEquals("providerCase", application.getProviderCaseReference());
//        assertEquals(1, application.getOfficeId());
//        assertEquals("officeDisp", application.getOfficeDisplayValue());
//        assertEquals("supervisorId", application.getSupervisor());
//        assertEquals("supervisorDisp", application.getSupervisorDisplayValue());
//        assertEquals("feeEarnerId", application.getFeeEarner());
//        assertEquals("feeEarnerDisp", application.getFeeEarnerDisplayValue());
//        assertEquals("providerContactId", application.getProviderContact());
//        assertEquals("providerContactDisp", application.getProviderContactDisplayValue());
//        assertEquals("categoryLawId", application.getCategoryOfLaw());
//        assertEquals("categoryLawDisp", application.getCategoryOfLawDisplayValue());
//        assertEquals("statusDisp", application.getDisplayStatus());
//        assertEquals("statusId", application.getActualStatus());
//        assertEquals("clientFirst", application.getClientFirstName());
//        assertEquals("clientSurname", application.getClientSurname());
//        assertEquals("clientRef", application.getClientReference());
//        assertEquals("appTypeId", application.getApplicationType());
//        assertEquals("appTypeDisp", application.getApplicationTypeDisplayValue());
//
//    }
//
//    @Test
//    public void testAddressMapping() {
//<<<<<<< HEAD
//        ApplicationDetailCorrespondenceAddress detailAddress = new ApplicationDetailCorrespondenceAddress();
//=======
//        // Construct ApplicationDetailCorrespondenceAddress
//        uk.gov.laa.ccms.caab.model.Address detailAddress = new uk.gov.laa.ccms.caab.model.Address();
//>>>>>>> main
//        detailAddress.setNoFixedAbode(true);
//        detailAddress.setPostcode("12345");
//        detailAddress.setHouseNameOrNumber("House 123");
//        detailAddress.setAddressLine1("Address line 1");
//        detailAddress.setAddressLine2("Address line 2");
//        detailAddress.setCity("City");
//        detailAddress.setCounty("County");
//        detailAddress.setCountry("Country");
//        detailAddress.setCareOf("Care of");
//        detailAddress.setPreferredAddress("Preferred Address");
//
//        // Convert ApplicationDetailCorrespondenceAddress to Address
//        Address address = mapper.toAddress(detailAddress);
//
//        // Assertions
//        assertNull(address.getId()); // ID should be ignored and not set
//        assertTrue(address.getNoFixedAbode());
//        assertEquals("12345", address.getPostCode());
//        assertEquals("House 123", address.getHouseNameNumber());
//        assertEquals("Address line 1", address.getAddressLine1());
//        assertEquals("Address line 2", address.getAddressLine2());
//        assertEquals("City", address.getCity());
//        assertEquals("County", address.getCounty());
//        assertEquals("Country", address.getCountry());
//        assertEquals("Care of", address.getCareOf());
//        assertEquals("Preferred Address", address.getPreferredAddress());
//    }
//
//    @Test
//    public void testCostStructureMapping() {
//        // Construct ApplicationDetailCosts
//        uk.gov.laa.ccms.caab.model.CostStructure detailCosts =
//            new uk.gov.laa.ccms.caab.model.CostStructure();
//        detailCosts.setDefaultCostLimitation(new BigDecimal("100.00"));
//        detailCosts.setGrantedCostLimitation(new BigDecimal("200.00"));
//        detailCosts.setRequestedCostLimitation(new BigDecimal("300.00"));
//
//        // Convert ApplicationDetailCosts to CostStructure
//        CostStructure costStructure = mapper.toCostStructure(detailCosts);
//
//        // Assertions
//        assertNull(costStructure.getId()); // ID should be ignored and not set
//        assertEquals(new BigDecimal("100.00"), costStructure.getDefaultCostLimitation());
//        assertEquals(new BigDecimal("200.00"), costStructure.getGrantedCostLimitation());
//        assertEquals(new BigDecimal("300.00"), costStructure.getRequestedCostLimitation());
//    }
//
//    @Test
//    public void testToApplicationMapping() {
//        // Construct ApplicationDetail
//        ApplicationDetail detail = new ApplicationDetail();
//        detail.setCaseReferenceNumber("CASE-001");
//        detail.setProviderCaseReference("CASE-001");
//        detail.setProvider(new IntDisplayValue().id(1234).displayValue("Provider Display"));
//        detail.setOffice(new IntDisplayValue().id(1).displayValue("Office 1"));
//        detail.setSupervisor(new StringDisplayValue().id("Supervisor").displayValue("Supervisor Display"));
//        detail.setFeeEarner(new StringDisplayValue().id("Fee Earner").displayValue("Fee Earner Display"));
//
//        // Convert ApplicationDetail to Application
//        Application application = mapper.toApplication(detail);
//
//        // Assertions
//        assertEquals("CASE-001", application.getLscCaseReference());
//        assertEquals(1234, Integer.parseInt(application.getProviderId()));
//        assertEquals("Provider Display", application.getProviderDisplayValue());
//        assertEquals("CASE-001", application.getProviderCaseReference());
//        assertEquals(1L, application.getOfficeId());
//        assertEquals("Office 1", application.getOfficeDisplayValue());
//    }
//
//    @Test
//    public void testToAddressWithNullValues() {
//        // Construct ApplicationDetailCorrespondenceAddress with null values
//        uk.gov.laa.ccms.caab.model.Address detailAddress =
//            new uk.gov.laa.ccms.caab.model.Address();
//
//        // Convert ApplicationDetailCorrespondenceAddress to Address
//        Address address = mapper.toAddress(detailAddress);
//
//        // Assertions
//        assertNull(address.getPostCode());
//        assertNull(address.getHouseNameNumber());
//    }
//
//    @Test
//    public void testToCostStructureWithZeroValues() {
//        // Construct ApplicationDetailCosts with zero values
//        uk.gov.laa.ccms.caab.model.CostStructure detailCosts =
//            new uk.gov.laa.ccms.caab.model.CostStructure();
//        detailCosts.setDefaultCostLimitation(new BigDecimal("0.00"));
//        detailCosts.setGrantedCostLimitation(new BigDecimal("0.00"));
//        detailCosts.setRequestedCostLimitation(new BigDecimal("0.00"));
//
//        // Convert ApplicationDetailCosts to CostStructure
//        CostStructure costStructure = mapper.toCostStructure(detailCosts);
//
//        // Assertions
//        assertEquals(new BigDecimal("0.00"), costStructure.getDefaultCostLimitation());
//        assertEquals(new BigDecimal("0.00"), costStructure.getGrantedCostLimitation());
//        assertEquals(new BigDecimal("0.00"), costStructure.getRequestedCostLimitation());
//    }
//
//    @Test
//    public void testToApplicationDetailCorrespondenceAddressMapping() {
//        // Construct Address
//        Address address = new Address();
//        address.setNoFixedAbode(true);
//        address.setPostCode("12345");
//        address.setHouseNameNumber("House 123");
//        address.setAddressLine1("Address line 1");
//        address.setAddressLine2("Address line 2");
//        address.setCity("City");
//        address.setCounty("County");
//        address.setCountry("Country");
//        address.setCareOf("Care of");
//        address.setPreferredAddress("Preferred Address");
//
//        // Convert Address to ApplicationDetailCorrespondenceAddress
//        ApplicationDetailCorrespondenceAddress detailAddress = mapper.toApplicationDetailCorrespondenceAddress(address);
//
//        // Assertions
//        assertTrue(detailAddress.getNoFixedAbode());
//        assertEquals("12345", detailAddress.getPostcode());
//        assertEquals("House 123", detailAddress.getHouseNameOrNumber());
//        assertEquals("Address line 1", detailAddress.getAddressLine1());
//        assertEquals("Address line 2", detailAddress.getAddressLine2());
//        assertEquals("City", detailAddress.getCity());
//        assertEquals("County", detailAddress.getCounty());
//        assertEquals("Country", detailAddress.getCountry());
//        assertEquals("Care of", detailAddress.getCareOf());
//        assertEquals("Preferred Address", detailAddress.getPreferredAddress());
//    }
//
//    @Test
//    public void testToApplicationDetailCostsMapping() {
//        CostStructure costStructure = new CostStructure();
//        costStructure.setDefaultCostLimitation(new BigDecimal("100.00"));
//        costStructure.setGrantedCostLimitation(new BigDecimal("200.00"));
//        costStructure.setRequestedCostLimitation(new BigDecimal("300.00"));
//
//        ApplicationDetailCosts detailCosts = mapper.toApplicationDetailCosts(costStructure);
//
//        assertEquals(new BigDecimal("100.00"), detailCosts.getDefaultCostLimitation());
//        assertEquals(new BigDecimal("200.00"), detailCosts.getGrantedCostLimitation());
//        assertEquals(new BigDecimal("300.00"), detailCosts.getRequestedCostLimitation());
//    }
//
//    @Test
//    public void testToProceedingDetail() {
//        // Construct Proceeding
//        Proceeding proceeding = new Proceeding();
//        AuditTrail auditTrail = new AuditTrail();
//        // Set fields on auditTrail as needed
//        proceeding.setAuditTrail(auditTrail);
//        proceeding.setStage("Stage1");
//
//        // Convert Proceeding to ProceedingDetail
//        ProceedingDetail proceedingDetail = mapper.toProceedingDetail(proceeding);
//
//        // Assertions
//        assertEquals(mapper.toAuditDetail(auditTrail), proceedingDetail.getAuditTrail());
//        assertEquals("Stage1", proceedingDetail.getStage());
//    }
//
//    @Test
//    public void testToPriorAuthorityDetail() {
//        // Construct PriorAuthority
//        PriorAuthority priorAuthority = new PriorAuthority();
//        AuditTrail auditTrail = new AuditTrail();
//        // Set fields on auditTrail as needed
//        priorAuthority.setAuditTrail(auditTrail);
//
//        // Convert PriorAuthority to PriorAuthorityDetail
//        PriorAuthorityDetail priorAuthorityDetail = mapper.toPriorAuthorityDetail(priorAuthority);
//
//        // Assertions
//        assertEquals(mapper.toAuditDetail(auditTrail), priorAuthorityDetail.getAuditTrail());
//    }
//
//    @Test
//    public void testToOpponentDetail() {
//        // Construct Opponent
//        Opponent opponent = new Opponent();
//        AuditTrail auditTrail = new AuditTrail();
//        // Set fields on auditTrail as needed
//        opponent.setAuditTrail(auditTrail);
//        opponent.setRelationshipToCase("Relation1");
//        opponent.setType("Type1");
//
//        // Convert Opponent to OpponentDetail
//        OpponentDetail opponentDetail = mapper.toOpponentDetail(opponent);
//
//        // Assertions
//        assertEquals(mapper.toAuditDetail(auditTrail), opponentDetail.getAuditTrail());
//        assertEquals("Relation1", opponentDetail.getRelationshipToCase());
//        assertEquals("Type1", opponentDetail.getType());
//    }
//
//    @Test
//    public void testToAuditDetail() {
//        // Construct AuditTrail
//        AuditTrail auditTrail = new AuditTrail();
//        auditTrail.setModified(new Date());
//        auditTrail.setModifiedBy("ModifiedBy1");
//        auditTrail.setCreated(new Date());
//        auditTrail.setCreatedBy("CreatedBy1");
//
//        // Convert AuditTrail to AuditDetail
//        AuditDetail auditDetail = mapper.toAuditDetail(auditTrail);
//
//        // Assertions
//        assertEquals(auditTrail.getModified(), auditDetail.getLastSaved());
//        assertEquals(auditTrail.getModifiedBy(), auditDetail.getLastSavedBy());
//        assertEquals(auditTrail.getCreated(), auditDetail.getCreated());
//        assertEquals(auditTrail.getCreatedBy(), auditDetail.getCreatedBy());
//    }
//
//    @Test
//    void testToApplicationDetailWithNullInput() {
//        assertNull(mapper.toApplicationDetail(null));
//    }
//
//    @Test
//    void testToApplicationDetailWithValidInput() {
//        Application application = getApplication();
//
//        // Call toApplicationDetail(Application)
//        ApplicationDetail applicationDetail = mapper.toApplicationDetail(application);
//
//        // Verify the resulting ApplicationDetail instance
//        assertNotNull(applicationDetail);
//        assertEquals(Integer.valueOf(12345), applicationDetail.getProvider().getId());
//        assertEquals("Provider Display Value", applicationDetail.getProvider().getDisplayValue());
//        assertEquals("Provider Case Reference", applicationDetail.getProvider().getCaseReference());
//        assertEquals(Integer.valueOf(67890), applicationDetail.getOffice().getId());
//        assertEquals("Office Display Value", applicationDetail.getOffice().getDisplayValue());
//        assertEquals("John", applicationDetail.getClient().getFirstName());
//        assertEquals("Doe", applicationDetail.getClient().getSurname());
//        assertEquals("Client Reference", applicationDetail.getClient().getReference());
//        assertEquals("Supervisor ID", applicationDetail.getSupervisor().getId());
//        assertEquals("Supervisor Display Value", applicationDetail.getSupervisor().getDisplayValue());
//        assertEquals("Fee Earner ID", applicationDetail.getFeeEarner().getId());
//        assertEquals("Fee Earner Display Value", applicationDetail.getFeeEarner().getDisplayValue());
//        assertEquals("Provider Contact ID", applicationDetail.getProviderContact().getId());
//        assertEquals("Provider Contact Display Value", applicationDetail.getProviderContact().getDisplayValue());
//        assertEquals("Category of Law", applicationDetail.getCategoryOfLaw().getId());
//        assertEquals("Category of Law Display Value", applicationDetail.getCategoryOfLaw().getDisplayValue());
//        assertEquals("Relation to Linked Case", applicationDetail.getRelationToLinkedCase());
//    }
//
//    @NotNull
//    private static Application getApplication() {
//        Application application = new Application();
//        application.setProviderId("12345");
//        application.setProviderDisplayValue("Provider Display Value");
//        application.setProviderCaseReference("Provider Case Reference");
//        application.setOfficeId(67890L);
//        application.setOfficeDisplayValue("Office Display Value");
//        application.setClientFirstName("John");
//        application.setClientSurname("Doe");
//        application.setClientReference("Client Reference");
//        application.setSupervisor("Supervisor ID");
//        application.setSupervisorDisplayValue("Supervisor Display Value");
//        application.setFeeEarner("Fee Earner ID");
//        application.setFeeEarnerDisplayValue("Fee Earner Display Value");
//        application.setProviderContact("Provider Contact ID");
//        application.setProviderContactDisplayValue("Provider Contact Display Value");
//        application.setCategoryOfLaw("Category of Law");
//        application.setCategoryOfLawDisplayValue("Category of Law Display Value");
//        application.setRelationToLinkedCase("Relation to Linked Case");
//        application.setOpponentAppliedForFunding(true);
//        application.setDisplayStatus("Display Status");
//        application.setActualStatus("Actual Status");
//        application.setAmendment(true);
//        application.setApplicationType("Application Type");
//        application.setApplicationTypeDisplayValue("Application Type Display Value");
//        application.setDevolvedPowersUsed("Yes");
//        application.setDateDevolvedPowersUsed(new Date());
//        application.setDevolvedPowersContractFlag("Contract Flag");
//        application.setLarScopeFlag("Scope Flag");
//        application.setMeansAssessmentAmended(true);
//        application.setMeritsAssessmentAmended(true);
//        application.setCostLimitChanged(true);
//        application.setCostLimitAtTimeOfMerits(new BigDecimal("10000.00"));
//        application.setMeritsReassessmentReqdInd(true);
//        application.setLeadProceedingChangedOpaInput(true);
//        return application;
//    }
//
//    @Test
//    void testToApplicationDetailWithValidInput_opponents() {
//        Application application = getApplicationWithOpponents();
//
//        // Call toApplicationDetail(Application)
//        ApplicationDetail applicationDetail = mapper.toApplicationDetail(application);
//
//        // Verify the resulting ApplicationDetail instance
//        assertNotNull(applicationDetail);
//
//        // Assuming there's a method getOpponentDetails in ApplicationDetail class that returns a List of OpponentDetail
//        List<OpponentDetail> opponentDetails = applicationDetail.getOpponents();
//        assertNotNull(opponentDetails);
//        assertEquals(2, opponentDetails.size());
//
//        OpponentDetail opponentDetail1 = opponentDetails.get(0);
//        assertEquals("Relative", opponentDetail1.getRelationshipToCase());
//        assertEquals("Type1", opponentDetail1.getType());
//        assertNotNull(opponentDetail1.getAuditTrail());  // assuming AuditTrail maps to AuditDetail with a straightforward mapping
//
//        OpponentDetail opponentDetail2 = opponentDetails.get(1);
//        assertEquals("Friend", opponentDetail2.getRelationshipToCase());
//        assertEquals("Type2", opponentDetail2.getType());
//        assertNotNull(opponentDetail2.getAuditTrail());  // assuming AuditTrail maps to AuditDetail with a straightforward mapping
//    }
//
//    private Application getApplicationWithOpponents() {
//        Opponent opponent1 = new Opponent();
//        opponent1.setRelationshipToCase("Relative");
//        opponent1.setType("Type1");
//        AuditTrail auditTrail1 = new AuditTrail();  // assuming AuditTrail class has default constructor
//        opponent1.setAuditTrail(auditTrail1);
//
//        Opponent opponent2 = new Opponent();
//        opponent2.setRelationshipToCase("Friend");
//        opponent2.setType("Type2");
//        AuditTrail auditTrail2 = new AuditTrail();  // assuming AuditTrail class has default constructor
//        opponent2.setAuditTrail(auditTrail2);
//
//        List<Opponent> opponents = Arrays.asList(opponent1, opponent2);
//
//        // Create an Application instance and set its properties
//        Application application = new Application();
//        application.setOpponents(opponents);  // assuming setOpponents is a setter method in Application class
//        return application;
//    }
//
//    @Test
//    void testToApplicationDetailWithValidInput_proceedings() {
//        // Create a list of Proceeding instances and set their properties
//        Application application = getApplicationWithProceedings();
//
//        // Call toApplicationDetail(Application)
//        ApplicationDetail applicationDetail = mapper.toApplicationDetail(application);
//
//        // Verify the resulting ApplicationDetail instance
//        assertNotNull(applicationDetail);
//
//        // Assuming there's a method getProceedingDetails in ApplicationDetail class that returns a List of ProceedingDetail
//        List<ProceedingDetail> proceedingDetails = applicationDetail.getProceedings();
//        assertNotNull(proceedingDetails);
//        assertEquals(2, proceedingDetails.size());
//
//        ProceedingDetail proceedingDetail1 = proceedingDetails.get(0);
//        assertEquals("Stage1", proceedingDetail1.getStage());
//        assertNotNull(proceedingDetail1.getAuditTrail());  // assuming AuditTrail maps to AuditDetail with a straightforward mapping
//
//        ProceedingDetail proceedingDetail2 = proceedingDetails.get(1);
//        assertEquals("Stage2", proceedingDetail2.getStage());
//        assertNotNull(proceedingDetail2.getAuditTrail());  // assuming AuditTrail maps to AuditDetail with a straightforward mapping
//    }
//
//    private Application getApplicationWithProceedings() {
//        Proceeding proceeding1 = new Proceeding();
//        proceeding1.setStage("Stage1");
//        AuditTrail auditTrail1 = new AuditTrail();  // assuming AuditTrail class has default constructor
//        proceeding1.setAuditTrail(auditTrail1);
//
//        Proceeding proceeding2 = new Proceeding();
//        proceeding2.setStage("Stage2");
//        AuditTrail auditTrail2 = new AuditTrail();  // assuming AuditTrail class has default constructor
//        proceeding2.setAuditTrail(auditTrail2);
//
//        List<Proceeding> proceedings = Arrays.asList(proceeding1, proceeding2);
//
//        // Create an Application instance and set its properties
//        Application application = new Application();
//        application.setProceedings(proceedings);  // assuming setProceedings is a setter method in Application class
//        return application;
//    }
//
//    @Test
//    void testToApplicationDetailWithValidInput_priorAuthorities() {
//        // Create a list of PriorAuthority instances and set their properties
//        Application application = getApplicationWithPriorAuthorities();
//
//        // Call toApplicationDetail(Application)
//        ApplicationDetail applicationDetail = mapper.toApplicationDetail(application);
//
//        // Verify the resulting ApplicationDetail instance
//        assertNotNull(applicationDetail);
//
//        // Assuming there's a method getPriorAuthorityDetails in ApplicationDetail class that returns a List of PriorAuthorityDetail
//        List<PriorAuthorityDetail> priorAuthorityDetails = applicationDetail.getPriorAuthorities();
//        assertNotNull(priorAuthorityDetails);
//        assertEquals(2, priorAuthorityDetails.size());
//
//        PriorAuthorityDetail priorAuthorityDetail1 = priorAuthorityDetails.get(0);
//        assertNotNull(priorAuthorityDetail1.getAuditTrail());  // assuming AuditTrail maps to AuditDetail with a straightforward mapping
//
//        PriorAuthorityDetail priorAuthorityDetail2 = priorAuthorityDetails.get(1);
//        assertNotNull(priorAuthorityDetail2.getAuditTrail());  // assuming AuditTrail maps to AuditDetail with a straightforward mapping
//    }
//
//    private Application getApplicationWithPriorAuthorities() {
//        PriorAuthority priorAuthority1 = new PriorAuthority();
//        AuditTrail auditTrail1 = new AuditTrail();  // assuming AuditTrail class has default constructor
//        priorAuthority1.setAuditTrail(auditTrail1);
//
//        PriorAuthority priorAuthority2 = new PriorAuthority();
//        AuditTrail auditTrail2 = new AuditTrail();  // assuming AuditTrail class has default constructor
//        priorAuthority2.setAuditTrail(auditTrail2);
//
//        List<PriorAuthority> priorAuthorities = Arrays.asList(priorAuthority1, priorAuthority2);
//
//        // Create an Application instance and set its properties
//        Application application = new Application();
//        application.setPriorAuthorities(priorAuthorities);  // assuming setPriorAuthorities is a setter method in Application class
//        return application;
//    }
//
//
//}
