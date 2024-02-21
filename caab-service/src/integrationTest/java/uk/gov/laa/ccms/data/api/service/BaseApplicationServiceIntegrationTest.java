package uk.gov.laa.ccms.data.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.laa.ccms.caab.api.audit.AuditorAwareImpl.currentUserHolder;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import uk.gov.laa.ccms.caab.api.entity.Application;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.repository.ApplicationRepository;
import uk.gov.laa.ccms.caab.api.service.ApplicationService;
import uk.gov.laa.ccms.caab.model.Address;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.ApplicationProviderDetails;
import uk.gov.laa.ccms.caab.model.ApplicationType;
import uk.gov.laa.ccms.caab.model.AuditDetail;
import uk.gov.laa.ccms.caab.model.Client;
import uk.gov.laa.ccms.caab.model.CostLimit;
import uk.gov.laa.ccms.caab.model.CostStructure;
import uk.gov.laa.ccms.caab.model.DevolvedPowers;
import uk.gov.laa.ccms.caab.model.IntDisplayValue;
import uk.gov.laa.ccms.caab.model.Opponent;
import uk.gov.laa.ccms.caab.model.StringDisplayValue;

public abstract class BaseApplicationServiceIntegrationTest {

  @Autowired
  private ApplicationService applicationService;
  @Autowired
  private ApplicationRepository applicationRepository;
  private final String lscCaseReference = "LSC001";
  private final Integer providerId = 123456;
  private final String providerCaseReference = "PRD_CASE_REF_001";
  private final String providerDisplayValue = "Provider Display";
  private final Integer officeId = 987;
  private final String officeDisplayValue = "Office Display";
  private final String supervisor = "Supervisor1";
  private final String supervisorDisplayValue = "Supervisor Display";
  private final String feeEarner = "Fee Earner";
  private final String feeEarnerDisplayValue = "Fee Earner Display";
  private final String providerContact = "Provider Contact";
  private final String providerContactDisplayValue = "Provider Contact Display";
  private final String categoryOfLaw = "Category of Law";
  private final String categoryOfLawDisplayValue = "Category of Law Display";
  private final String relationToLinkedCase = "Relation to Linked Case";
  private final Boolean opponentAppliedForFunding = false;
  private final String displayStatus = "Display Status";
  private final String actualStatus = "Actual Status";
  private final String clientFirstName = "Client First Name";
  private final String clientSurname = "Client Surname";
  private final String clientReference = "Client Reference";
  private final Boolean amendment = false;
  private final Boolean meansAssessmentAmended = false;
  private final Boolean meritsAssessmentAmended = false;
  private final Boolean costLimitChanged = false;
  private final BigDecimal costLimitAtTimeOfMerits = BigDecimal.valueOf(10000);
  private final String applicationType = "Application Type";
  private final String applicationTypeDisplayValue = "Application Type Display";
  private final Boolean devolvedPowersUsed = false;
  private final Date dateDevolvedPowersUsed = Date.from(Instant.now());
  private final String devolvedPowersContractFlag = "Devolved Powers Contract Flag";
  private final Boolean meritsReassessmentReqdInd = false;
  private final Boolean larScopeFlag = true;
  private final Boolean leadProceedingChanged = false;

  //cost structure
  private final BigDecimal defaultCostLimitation = BigDecimal.valueOf(11111.11);
  private final BigDecimal grantedCostLimitation = BigDecimal.valueOf(22222.22);
  private final BigDecimal requestedCostLimitation = BigDecimal.valueOf(33333.33);

  //Address
  private final boolean noFixedAbode = false;
  private final String postCode = "SW1A 1AA";
  private final String houseNameNumber = "10";
  private final String addressLine1 = "Downing Street";
  private final String addressLine2 = "Westminster";
  private final String city = "London";
  private final String county = "Greater London";
  private final String country = "GBR";
  private final String careOf = "Prime Minister's Office";
  private final String preferredAddress = "PROVIDER";

  private final String caabUserLoginId = "audit@user.com";

  @BeforeEach
  public void setup() {
    currentUserHolder.set(caabUserLoginId);
  }

  @Test
  public void testSaveApplication_requiredFields(){
    ApplicationDetail application = buildRequiredApplicationDetail();

    Long savedId = applicationService.createApplication(application);

    // Assert that the saved application has been assigned an ID
    assertNotNull(savedId);

    // Fetch the saved application from the database
    Application fetchedApplication = applicationRepository.findById(savedId)
        .orElse(null);

    // Assert that the fetched application is not null and has the expected values
    assertNotNull(fetchedApplication);
    assertEquals(lscCaseReference, fetchedApplication.getLscCaseReference());
    assertEquals(providerId.toString(), fetchedApplication.getProviderId());
    assertEquals(categoryOfLaw, fetchedApplication.getCategoryOfLaw());
    assertEquals(clientReference, fetchedApplication.getClientReference());
  }

  @Test
  public void testSaveApplication_allFields(){
    ApplicationDetail application = buildApplication();

    Long savedId = applicationService.createApplication(application);

    // Assert that the saved application has been assigned an ID
    assertNotNull(savedId);

    // Fetch the saved application from the database
    Application fetchedApplication = applicationRepository.findById(savedId).orElse(null);

    // Assert that the fetched application is not null and has the expected values
    assertNotNull(fetchedApplication);
    assertEquals(lscCaseReference, fetchedApplication.getLscCaseReference());
    assertEquals(providerId.toString(), fetchedApplication.getProviderId());
    assertEquals(providerCaseReference, fetchedApplication.getProviderCaseReference());
    assertEquals(providerDisplayValue, fetchedApplication.getProviderDisplayValue());
    assertEquals(officeId, fetchedApplication.getOfficeId().intValue());
    assertEquals(officeDisplayValue, fetchedApplication.getOfficeDisplayValue());
    assertEquals(supervisor, fetchedApplication.getSupervisor());
    assertEquals(supervisorDisplayValue, fetchedApplication.getSupervisorDisplayValue());
    assertEquals(feeEarner, fetchedApplication.getFeeEarner());
    assertEquals(feeEarnerDisplayValue, fetchedApplication.getFeeEarnerDisplayValue());
    assertEquals(providerContact, fetchedApplication.getProviderContact());
    assertEquals(providerContactDisplayValue, fetchedApplication.getProviderContactDisplayValue());
    assertEquals(categoryOfLaw, fetchedApplication.getCategoryOfLaw());
    assertEquals(categoryOfLawDisplayValue, fetchedApplication.getCategoryOfLawDisplayValue());
    assertEquals(relationToLinkedCase, fetchedApplication.getRelationToLinkedCase());
    assertEquals(opponentAppliedForFunding, fetchedApplication.getOpponentAppliedForFunding());
    assertEquals(displayStatus, fetchedApplication.getDisplayStatus());
    assertEquals(actualStatus, fetchedApplication.getActualStatus());
    assertEquals(clientFirstName, fetchedApplication.getClientFirstName());
    assertEquals(clientSurname, fetchedApplication.getClientSurname());
    assertEquals(clientReference, fetchedApplication.getClientReference());
    assertEquals(amendment, fetchedApplication.getAmendment());
    assertEquals(meansAssessmentAmended, fetchedApplication.getMeansAssessmentAmended());
    assertEquals(meritsAssessmentAmended, fetchedApplication.getMeritsAssessmentAmended());
    assertEquals(costLimitChanged, fetchedApplication.getCostLimitChanged());
    assertEquals(costLimitAtTimeOfMerits, fetchedApplication.getCostLimitAtTimeOfMerits());
    assertEquals(applicationType, fetchedApplication.getApplicationType());
    assertEquals(applicationTypeDisplayValue, fetchedApplication.getApplicationTypeDisplayValue());
    assertEquals(devolvedPowersUsed, fetchedApplication.getDevolvedPowersUsed());

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    sdf.setTimeZone(TimeZone.getTimeZone("BST"));
    assertEquals(sdf.format(dateDevolvedPowersUsed), sdf.format(fetchedApplication.getDateDevolvedPowersUsed()));

    assertEquals(devolvedPowersContractFlag, fetchedApplication.getDevolvedPowersContractFlag());
    assertEquals(meritsReassessmentReqdInd, fetchedApplication.getMeritsReassessmentReqdInd());
    assertEquals(larScopeFlag, fetchedApplication.getLarScopeFlag());
    assertEquals(leadProceedingChanged, fetchedApplication.getLeadProceedingChangedOpaInput());

    assertEquals(caabUserLoginId, fetchedApplication.getAuditTrail().getLastSavedBy());
    assertEquals(caabUserLoginId, fetchedApplication.getAuditTrail().getCreatedBy());
    assertNotNull(fetchedApplication.getAuditTrail().getCreated());
    assertNotNull(fetchedApplication.getAuditTrail().getLastSaved());
  }

  @Test
  @Transactional
  public void testSaveApplication_opponent(){
    ApplicationDetail application = buildRequiredApplicationDetail();
    Opponent builtOpponent = buildOpponent(new Date());
    application.setOpponents(new ArrayList<>());
    application.getOpponents().add(builtOpponent);

    Long savedId = applicationService.createApplication(application);

    // Fetch the saved application from the database
    Application fetchedApplication = applicationRepository.findById(savedId).orElse(null);

    // Assert that the fetched application is not null and has the expected values
    assertNotNull(fetchedApplication);
    assertNotNull(fetchedApplication.getOpponents());
    assertEquals(1, fetchedApplication.getOpponents().size());

    uk.gov.laa.ccms.caab.api.entity.Opponent fetchedOpponent =
        fetchedApplication.getOpponents().get(0);
    
    assertNotNull(fetchedOpponent.getAddress());
    assertEquals(builtOpponent.getAmendment(), fetchedOpponent.getAmendment());
    assertEquals(builtOpponent.getAppMode(), fetchedOpponent.getAppMode());
    assertEquals(builtOpponent.getAssessedAssets(), fetchedOpponent.getAssessedAssets());
    assertEquals(builtOpponent.getAssessedIncome(), fetchedOpponent.getAssessedIncome());
    assertEquals(builtOpponent.getAssessedIncomeFrequency(), fetchedOpponent.getAssessedIncomeFrequency());
    assertEquals(builtOpponent.getAssessmentDate(), fetchedOpponent.getAssessmentDate());
    assertEquals(caabUserLoginId, fetchedOpponent.getAuditTrail().getCreatedBy());
    assertEquals(builtOpponent.getAward(), fetchedOpponent.getAward());
    assertEquals(builtOpponent.getCertificateNumber(), fetchedOpponent.getCertificateNumber());
    assertEquals(builtOpponent.getConfirmed(), fetchedOpponent.getConfirmed());
    assertEquals(builtOpponent.getContactNameRole(), fetchedOpponent.getContactNameRole());
    assertEquals(builtOpponent.getCourtOrderedMeansAssessment(), fetchedOpponent.getCourtOrderedMeansAssessment());
    assertEquals(builtOpponent.getCurrentlyTrading(), fetchedOpponent.getCurrentlyTrading());
    assertEquals(builtOpponent.getDateOfBirth(), fetchedOpponent.getDateOfBirth());
    assertEquals(builtOpponent.getDeleteInd(), fetchedOpponent.getDeleteInd());
    assertEquals(builtOpponent.getEbsId(), fetchedOpponent.getEbsId());
    assertEquals(builtOpponent.getEmailAddress(), fetchedOpponent.getEmailAddress());
    assertEquals(builtOpponent.getEmployerAddress(), fetchedOpponent.getEmployerAddress());
    assertEquals(builtOpponent.getEmployerName(), fetchedOpponent.getEmployerName());
    assertEquals(builtOpponent.getEmploymentStatus(), fetchedOpponent.getEmploymentStatus());
    assertEquals(builtOpponent.getFaxNumber(), fetchedOpponent.getFaxNumber());
    assertEquals(builtOpponent.getFirstName(), fetchedOpponent.getFirstName());
    assertEquals(builtOpponent.getLegalAided(), fetchedOpponent.getLegalAided());
    assertEquals(builtOpponent.getMiddleNames(), fetchedOpponent.getMiddleNames());
    assertEquals(builtOpponent.getNationalInsuranceNumber(), fetchedOpponent.getNationalInsuranceNumber());
    assertEquals(builtOpponent.getOrganisationName(), fetchedOpponent.getOrganisationName());
    assertEquals(builtOpponent.getOrganisationType().getId(), fetchedOpponent.getOrganisationType());
    assertEquals(builtOpponent.getOtherInformation(), fetchedOpponent.getOtherInformation());
    assertEquals(builtOpponent.getPublicFundingApplied(), fetchedOpponent.getPublicFundingApplied());
    assertEquals(builtOpponent.getRelationshipToCase(), fetchedOpponent.getRelationshipToCase());
    assertEquals(builtOpponent.getRelationshipToClient(), fetchedOpponent.getRelationshipToClient());
    assertEquals(builtOpponent.getSharedInd(), fetchedOpponent.getSharedInd());
    assertEquals(builtOpponent.getSurname(), fetchedOpponent.getSurname());
    assertEquals(builtOpponent.getTelephoneHome(), fetchedOpponent.getTelephoneHome());
    assertEquals(builtOpponent.getTelephoneMobile(), fetchedOpponent.getTelephoneMobile());
    assertEquals(builtOpponent.getTelephoneWork(), fetchedOpponent.getTelephoneWork());
    assertEquals(builtOpponent.getTitle(), fetchedOpponent.getTitle());
    assertEquals(builtOpponent.getType(), fetchedOpponent.getType());

    assertEquals(caabUserLoginId, fetchedOpponent.getAuditTrail().getLastSavedBy());
    assertEquals(caabUserLoginId, fetchedOpponent.getAuditTrail().getCreatedBy());
    
  }
  
  
  @Test
  @Transactional
  public void testSaveApplication_costStructure(){

    ApplicationDetail application = buildRequiredApplicationDetail();
    application.setCosts(buildCosts());

    Long savedId = applicationService.createApplication(application);

    // Fetch the saved application from the database
    Application fetchedApplication = applicationRepository.findById(savedId).orElse(null);

    // Assert that the fetched application is not null and has the expected values
    assertNotNull(fetchedApplication);
    assertNotNull(fetchedApplication.getCosts());
    assertEquals(defaultCostLimitation, fetchedApplication.getCosts().getDefaultCostLimitation());
    assertEquals(grantedCostLimitation, fetchedApplication.getCosts().getGrantedCostLimitation());
    assertEquals(requestedCostLimitation, fetchedApplication.getCosts().getRequestedCostLimitation());

    assertEquals(caabUserLoginId, fetchedApplication.getCosts().getAuditTrail().getLastSavedBy());
    assertEquals(caabUserLoginId, fetchedApplication.getCosts().getAuditTrail().getCreatedBy());
  }

  @Test
  @Transactional
  public void testSaveApplication_address(){
    ApplicationDetail application = buildRequiredApplicationDetail();
    application.setCorrespondenceAddress(buildAddress());

    Long savedId = applicationService.createApplication(application);

    // Fetch the saved application from the database
    Application fetchedApplication = applicationRepository.findById(savedId).orElse(null);

    // Assert that the fetched application is not null and has the expected values
    assertNotNull(fetchedApplication);
    assertNotNull(fetchedApplication.getCorrespondenceAddress());

    // Assert the properties of the correspondence address
    assertEquals(noFixedAbode, fetchedApplication.getCorrespondenceAddress().getNoFixedAbode());
    assertEquals(postCode, fetchedApplication.getCorrespondenceAddress().getPostCode());
    assertEquals(houseNameNumber, fetchedApplication.getCorrespondenceAddress().getHouseNameNumber());
    assertEquals(addressLine1, fetchedApplication.getCorrespondenceAddress().getAddressLine1());
    assertEquals(addressLine2, fetchedApplication.getCorrespondenceAddress().getAddressLine2());
    assertEquals(city, fetchedApplication.getCorrespondenceAddress().getCity());
    assertEquals(county, fetchedApplication.getCorrespondenceAddress().getCounty());
    assertEquals(country, fetchedApplication.getCorrespondenceAddress().getCountry());
    assertEquals(careOf, fetchedApplication.getCorrespondenceAddress().getCareOf());
    assertEquals(preferredAddress, fetchedApplication.getCorrespondenceAddress().getPreferredAddress());

    // Audit Trail asserts for the address
    assertEquals(caabUserLoginId, fetchedApplication.getCorrespondenceAddress().getAuditTrail().getLastSavedBy());
    assertEquals(caabUserLoginId, fetchedApplication.getCorrespondenceAddress().getAuditTrail().getCreatedBy());
  }

  @Test
  @Transactional
  @Sql(scripts = "/sql/application_insert.sql")
  public void testGetApplication() {
    Long applicationId = 24L;

    ApplicationDetail result = applicationService.getApplication(applicationId);

    // Assert the result
    assertNotNull(result);

    assertEquals("300001644517", result.getCaseReferenceNumber());
    assertEquals(26517, result.getProviderDetails().getProvider().getId());
    assertEquals("329635", result.getProviderDetails().getProviderCaseReference());
    assertEquals("SWITALSKI'S SOLICITORS LTD", result.getProviderDetails().getProvider().getDisplayValue());
    assertEquals(145512, result.getProviderDetails().getOffice().getId());
    assertEquals("SWITALSKI'S SOLICITORS LTD-2L847Q", result.getProviderDetails().getOffice().getDisplayValue());
    assertEquals("2854148", result.getProviderDetails().getSupervisor().getId());
    assertEquals("David Greenwood", result.getProviderDetails().getSupervisor().getDisplayValue());
    assertEquals("2027148", result.getProviderDetails().getFeeEarner().getId());
    assertEquals("Carole Spencer", result.getProviderDetails().getFeeEarner().getDisplayValue());
    assertEquals("2027079", result.getProviderDetails().getProviderContact().getId());
    assertEquals("CAROLE.SPENCER@SWITALSKIS.COM", result.getProviderDetails().getProviderContact().getDisplayValue());
    assertEquals("AAP", result.getCategoryOfLaw().getId());
    assertEquals("Claim Against Public Authority", result.getCategoryOfLaw().getDisplayValue());
    assertNull(result.getRelationToLinkedCase());
    assertFalse(result.getOpponentAppliedForFunding());
    assertEquals("Unsubmitted", result.getStatus().getDisplayValue());
    assertEquals("UNSUBMITTED", result.getStatus().getId());
    assertEquals("Phil", result.getClient().getFirstName());
    assertEquals("Payne", result.getClient().getSurname());
    assertEquals("PhilTest", result.getClient().getReference());
    assertEquals("ECF", result.getApplicationType().getId());
    assertEquals("Exceptional Case Funding", result.getApplicationType().getDisplayValue());
  }

  @Test
  @Sql(scripts = "/sql/application_insert.sql")
  public void testGetApplication_noData() {
    Long applicationId = 999L;

    // Use assertThrows to check if the method throws the expected exception
    CaabApiException exception = assertThrows(CaabApiException.class, () ->
        applicationService.getApplicationType(applicationId));

    assertEquals(
        String.format("Application with id %s not found", applicationId),
        exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

  @Test
  @Sql(scripts = "/sql/application_insert.sql")
  public void testGetApplicationType() {
    Long applicationId = 24L;

    // Call the service method
    ApplicationType result = applicationService.getApplicationType(applicationId);

    // Assert the result
    assertNotNull(result);

    assertEquals("ECF", result.getId());
    assertEquals("Exceptional Case Funding", result.getDisplayValue());
    assertEquals(false, result.getDevolvedPowers().getUsed());
    assertNull(result.getDevolvedPowers().getDateUsed());
    assertEquals("Yes - Excluding JR Proceedings", result.getDevolvedPowers().getContractFlag());
  }

  @Test
  @Sql(scripts = "/sql/application_insert.sql")
  public void testPutApplicationType() {
    Long applicationId = 24L;
    String amendedApplicationTypeId = "APP TYPE";
    String amendedApplicationTypeDisplay = "Application Type";

    ApplicationType amendedApplicationType
        = new ApplicationType()
        .id(amendedApplicationTypeId)
        .displayValue(amendedApplicationTypeDisplay);

    // Call the service method
    applicationService.putApplicationType(applicationId, amendedApplicationType);

    Application result = applicationRepository.findById(applicationId).orElse(null);

    // Assert the result
    assertNotNull(result);
    assertEquals(amendedApplicationTypeId, result.getApplicationType());
    assertEquals(amendedApplicationTypeDisplay, result.getApplicationTypeDisplayValue());
  }

  @ParameterizedTest
  @CsvSource({
      "24, NewProviderCaseRef1, New Provider Display1, 12345, 67890, FeeEarnerId1, SupervisorId1, ProviderContactId1, TestUser",
      "21, NewProviderCaseRef2, New Provider Display2, 54321, 98760, FeeEarnerId2, SupervisorId2, ProviderContactId2, AnotherUser",
      "41, NewProviderCaseRef3, New Provider Display3, 13579, 24680, FeeEarnerId3, SupervisorId3, ProviderContactId3, ThirdUser"
  })
  @Sql(scripts = "/sql/application_insert.sql")
  public void testPutProviderDetails(
      Long applicationId,
      String newProviderCaseReference,
      String newProviderDisplayValue,
      Integer newProviderId,
      Integer newOfficeId,
      String newFeeEarnerId,
      String newSupervisorId,
      String newProviderContactId,
      String patchedCaabUserLoginId) {

    // Create an ApplicationProviderDetails instance with the provided values
    ApplicationProviderDetails providerDetails = new ApplicationProviderDetails();
    IntDisplayValue provider = new IntDisplayValue().id(newProviderId).displayValue(newProviderDisplayValue);
    IntDisplayValue office = new IntDisplayValue().id(newOfficeId);
    StringDisplayValue feeEarner = new StringDisplayValue().id(newFeeEarnerId);
    StringDisplayValue supervisor = new StringDisplayValue().id(newSupervisorId);
    StringDisplayValue providerContact = new StringDisplayValue().id(newProviderContactId);

    providerDetails.setProvider(provider);
    providerDetails.setProviderCaseReference(newProviderCaseReference);
    providerDetails.setOffice(office);
    providerDetails.setFeeEarner(feeEarner);
    providerDetails.setSupervisor(supervisor);
    providerDetails.setProviderContact(providerContact);

    currentUserHolder.set(patchedCaabUserLoginId);

    // Call the patchProviderDetails method
    applicationService.putProviderDetails(applicationId, providerDetails);

    // Fetch the application from the database after the update
    Application updatedApplication = applicationRepository.findById(applicationId).orElse(null);

    // Assert that the updated application has the expected provider details
    assertNotNull(updatedApplication);
    assertEquals(newProviderCaseReference, updatedApplication.getProviderCaseReference());
    assertEquals(newProviderDisplayValue, updatedApplication.getProviderDisplayValue());

    // Assert for other provider details
    assertEquals(provider.getId().toString(), updatedApplication.getProviderId());
    assertEquals(office.getId().toString(), updatedApplication.getOfficeId().toString());
    assertEquals(feeEarner.getId(), updatedApplication.getFeeEarner());
    assertEquals(supervisor.getId(), updatedApplication.getSupervisor());
    assertEquals(providerContact.getId(), updatedApplication.getProviderContact());

    assertEquals(patchedCaabUserLoginId, updatedApplication.getAuditTrail().getLastSavedBy());
    assertTrue(updatedApplication.getAuditTrail().getLastSaved().after(updatedApplication.getAuditTrail().getCreated()));
  }

  @Test
  @Sql(scripts = "/sql/application_insert.sql")
  public void testPutProviderDetails_ApplicationNotFound() {
    Long nonExistentApplicationId = 999L;

    ApplicationProviderDetails providerDetails = new ApplicationProviderDetails();

    // Use assertThrows to check if the method throws the expected exception
    CaabApiException exception = assertThrows(CaabApiException.class, () ->
        applicationService.putProviderDetails(nonExistentApplicationId, providerDetails));

    assertEquals(
        String.format("Application with id %s not found", nonExistentApplicationId),
        exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }


  private ApplicationDetail buildRequiredApplicationDetail() {
    ApplicationDetail application = new ApplicationDetail();
    application.setCaseReferenceNumber(lscCaseReference);
    application.setProviderDetails(new ApplicationProviderDetails().provider(new IntDisplayValue().id(providerId)));
    application.setCategoryOfLaw(new StringDisplayValue().id(categoryOfLaw));
    application.setClient(new Client().reference(clientReference));
    return application;
  }

  private ApplicationDetail buildApplication(){
    ApplicationDetail application = buildRequiredApplicationDetail();
    //provider details
    ApplicationProviderDetails providerDetails = application.getProviderDetails();
    providerDetails.setProviderCaseReference(providerCaseReference);
    providerDetails.getProvider().setDisplayValue(providerDisplayValue);
    providerDetails.setOffice(new IntDisplayValue()
        .id(officeId)
        .displayValue(officeDisplayValue));
    providerDetails.setSupervisor(new StringDisplayValue()
        .id(supervisor)
        .displayValue(supervisorDisplayValue));
    providerDetails.setFeeEarner(new StringDisplayValue()
        .id(feeEarner)
        .displayValue(feeEarnerDisplayValue));
    providerDetails.setProviderContact(new StringDisplayValue()
        .id(providerContact)
        .displayValue(providerContactDisplayValue));

    //category of law
    application.getCategoryOfLaw().setDisplayValue(categoryOfLawDisplayValue);

    application.setRelationToLinkedCase(relationToLinkedCase);
    application.setOpponentAppliedForFunding(opponentAppliedForFunding);

    StringDisplayValue statusObj = new StringDisplayValue()
        .id(actualStatus)
        .displayValue(displayStatus);
    application.setStatus(statusObj);

    Client clientObj = new Client()
        .reference(clientReference)
        .firstName(clientFirstName)
        .surname(clientSurname);
    application.setClient(clientObj);

    application.setAmendment(amendment);
    application.setMeansAssessmentAmended(meansAssessmentAmended);
    application.setMeritsAssessmentAmended(meritsAssessmentAmended);
    CostLimit costLimitObj = new CostLimit()
        .changed(costLimitChanged)
        .limitAtTimeOfMerits(costLimitAtTimeOfMerits);
    application.setCostLimit(costLimitObj);

    DevolvedPowers devolvedPowersObj = new DevolvedPowers()
        .used(devolvedPowersUsed)
        .dateUsed(dateDevolvedPowersUsed)
        .contractFlag(devolvedPowersContractFlag);
    ApplicationType applicationTypeObj = new ApplicationType()
        .id(applicationType)
        .displayValue(applicationTypeDisplayValue)
        .devolvedPowers(devolvedPowersObj);
    application.setApplicationType(applicationTypeObj);

    application.setMeritsReassessmentRequired(meritsReassessmentReqdInd);
    application.setLarScopeFlag(larScopeFlag);
    application.setLeadProceedingChanged(leadProceedingChanged);

    return application;
  }

  private CostStructure buildCosts(){
    CostStructure costs = new CostStructure();
    costs.setDefaultCostLimitation(defaultCostLimitation);
    costs.setGrantedCostLimitation(grantedCostLimitation);
    costs.setRequestedCostLimitation(requestedCostLimitation);
    return costs;
  }

  private Address buildAddress(){
    Address address = new Address();
    address.setNoFixedAbode(noFixedAbode);
    address.setPostcode(postCode);
    address.setHouseNameOrNumber(houseNameNumber);
    address.setAddressLine1(addressLine1);
    address.setAddressLine2(addressLine2);
    address.setCity(city);
    address.setCounty(county);
    address.setCountry(country);
    address.setCareOf(careOf);
    address.setPreferredAddress(preferredAddress);
    return address;
  }

  public uk.gov.laa.ccms.caab.model.Opponent buildOpponent(java.util.Date date) {
    return new uk.gov.laa.ccms.caab.model.Opponent()
        .address(buildAddress())
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
        .legalAided(Boolean.TRUE)
        .middleNames("midnames")
        .nationalInsuranceNumber("nino")
        .organisationName("orgName")
        .organisationType(new StringDisplayValue().id("orgid").displayValue("org"))
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
