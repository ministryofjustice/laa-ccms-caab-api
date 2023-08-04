package uk.gov.laa.ccms.data.api.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.CacheAnnotationParser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import uk.gov.laa.ccms.api.CAABApplication;
import uk.gov.laa.ccms.api.entity.Address;
import uk.gov.laa.ccms.api.entity.Application;
import uk.gov.laa.ccms.api.entity.AuditTrail;
import uk.gov.laa.ccms.api.entity.CostStructure;
import uk.gov.laa.ccms.api.repository.ApplicationRepository;
import uk.gov.laa.ccms.data.api.AbstractIntegrationTest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlMergeMode.MergeMode.MERGE;

@SpringBootTest(classes = CAABApplication.class)
@SqlMergeMode(MERGE)
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "/sql/application_tables_create_schema.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/sql/application_tables_drop_schema.sql")
public class ApplicationRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ApplicationRepository applicationRepository;
    private final String lscCaseReference = "LSC001";
    private final String providerId = "PRD001";
    private final String providerCaseReference = "PRD_CASE_REF_001";
    private final String providerDisplayValue = "Provider Display";
    private final Long officeId = 1L;
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
    private final String devolvedPowersUsed = "false";
    private final Date dateDevolvedPowersUsed = Date.from(Instant.now());
    private final String devolvedPowersContractFlag = "Devolved Powers Contract Flag";
    private final Boolean meritsReassessmentReqdInd = false;
    private final String larScopeFlag = "Y";
    private final Boolean leadProceedingChanged = false;
    private final String createdBy = "Test User";
    private final String modifiedBy = "Test User";

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

    @Test
    public void testSaveApplication_requiredFields(){
        Application application = buildRequiredApplication();

        Application savedApplication = applicationRepository.save(application);

        // Assert that the saved application has been assigned an ID
        assertNotNull(savedApplication.getId());

        // Fetch the saved application from the database
        Application fetchedApplication = applicationRepository.findById(savedApplication.getId()).orElse(null);

        // Assert that the fetched application is not null and has the expected values
        assertNotNull(fetchedApplication);
        assertEquals(lscCaseReference, fetchedApplication.getLscCaseReference());
        assertEquals(providerId, fetchedApplication.getProviderId());
        assertEquals(categoryOfLaw, fetchedApplication.getCategoryOfLaw());
        assertEquals(clientReference, fetchedApplication.getClientReference());
    }



    @Test
    public void testSaveApplication_allFields(){
        Application application = buildApplication();

        Application savedApplication = applicationRepository.save(application);

        // Assert that the saved application has been assigned an ID
        assertNotNull(savedApplication.getId());

        // Fetch the saved application from the database
        Application fetchedApplication = applicationRepository.findById(savedApplication.getId()).orElse(null);

        // Assert that the fetched application is not null and has the expected values
        assertNotNull(fetchedApplication);
        assertEquals(lscCaseReference, fetchedApplication.getLscCaseReference());
        assertEquals(providerId, fetchedApplication.getProviderId());
        assertEquals(providerCaseReference, fetchedApplication.getProviderCaseReference());
        assertEquals(providerDisplayValue, fetchedApplication.getProviderDisplayValue());
        assertEquals(officeId, fetchedApplication.getOfficeId());
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
        assertEquals(amendment, fetchedApplication.isAmendment());
        assertEquals(meansAssessmentAmended, fetchedApplication.isMeansAssessmentAmended());
        assertEquals(meritsAssessmentAmended, fetchedApplication.isMeritsAssessmentAmended());
        assertEquals(costLimitChanged, fetchedApplication.isCostLimitChanged());
        assertEquals(costLimitAtTimeOfMerits, fetchedApplication.getCostLimitAtTimeOfMerits());
        assertEquals(applicationType, fetchedApplication.getApplicationType());
        assertEquals(applicationTypeDisplayValue, fetchedApplication.getApplicationTypeDisplayValue());
        assertEquals(devolvedPowersUsed, fetchedApplication.getDevolvedPowersUsed());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("BST"));
        assertEquals(sdf.format(dateDevolvedPowersUsed), sdf.format(fetchedApplication.getDateDevolvedPowersUsed()));

        assertEquals(devolvedPowersContractFlag, fetchedApplication.getDevolvedPowersContractFlag());
        assertEquals(meritsReassessmentReqdInd, fetchedApplication.isMeritsReassessmentReqdInd());
        assertEquals(larScopeFlag, fetchedApplication.getLarScopeFlag());
        assertEquals(leadProceedingChanged, fetchedApplication.isLeadProceedingChangedOpaInput());

        assertEquals(modifiedBy, fetchedApplication.getAuditTrail().getModifiedBy());
        assertEquals(createdBy, fetchedApplication.getAuditTrail().getCreatedBy());
        assertNotNull(fetchedApplication.getAuditTrail().getCreated());
        assertNotNull(fetchedApplication.getAuditTrail().getModified());

    }

    @Test
    public void testSaveApplication_costStructure(){
        Application application = buildRequiredApplication();
        application.setCosts(buildCosts());

        Application savedApplication = applicationRepository.save(application);

        // Assert that the saved application has been assigned an ID
        assertNotNull(savedApplication.getId());

        // Fetch the saved application from the database
        Application fetchedApplication = applicationRepository.findById(savedApplication.getId()).orElse(null);

        // Assert that the fetched application is not null and has the expected values
        assertNotNull(fetchedApplication);
        assertNotNull(fetchedApplication.getCosts());
        assertEquals(defaultCostLimitation, fetchedApplication.getCosts().getDefaultCostLimitation());
        assertEquals(grantedCostLimitation, fetchedApplication.getCosts().getGrantedCostLimitation());
        assertEquals(requestedCostLimitation, fetchedApplication.getCosts().getRequestedCostLimitation());

        assertEquals(modifiedBy, fetchedApplication.getCosts().getAuditTrail().getModifiedBy());
        assertEquals(createdBy, fetchedApplication.getCosts().getAuditTrail().getCreatedBy());
        assertNotNull(fetchedApplication.getCosts().getAuditTrail().getCreated());
        assertNotNull(fetchedApplication.getCosts().getAuditTrail().getModified());
    }

    @Test
    public void testSaveApplication_address(){
        Application application = buildRequiredApplication();
        application.setCorrespondenceAddress(buildAddress());

        Application savedApplication = applicationRepository.save(application);

        // Assert that the saved application has been assigned an ID
        assertNotNull(savedApplication.getId());

        // Fetch the saved application from the database
        Application fetchedApplication = applicationRepository.findById(savedApplication.getId()).orElse(null);

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
        assertEquals(modifiedBy, fetchedApplication.getCorrespondenceAddress().getAuditTrail().getModifiedBy());
        assertEquals(createdBy, fetchedApplication.getCorrespondenceAddress().getAuditTrail().getCreatedBy());
        assertNotNull(fetchedApplication.getCorrespondenceAddress().getAuditTrail().getCreated());
        assertNotNull(fetchedApplication.getCorrespondenceAddress().getAuditTrail().getModified());

    }

    private Application buildRequiredApplication() {
        Application application = new Application();
        application.setLscCaseReference(lscCaseReference);
        application.setProviderId(providerId);
        application.setCategoryOfLaw(categoryOfLaw);
        application.setClientReference(clientReference);
        return application;
    }

    private Application buildApplication(){
        Application application = buildRequiredApplication();
        //provider details
        application.setProviderCaseReference(providerCaseReference);
        application.setProviderDisplayValue(providerDisplayValue);
        //office
        application.setOfficeId(officeId);
        application.setOfficeDisplayValue(officeDisplayValue);
        //supervisor
        application.setSupervisor(supervisor);
        application.setSupervisorDisplayValue(supervisorDisplayValue);
        //fee earner
        application.setFeeEarner(feeEarner);
        application.setFeeEarnerDisplayValue(feeEarnerDisplayValue);
        //provider contact
        application.setProviderContact(providerContact);
        application.setProviderContactDisplayValue(providerContactDisplayValue);
        //category of law
        application.setCategoryOfLawDisplayValue(categoryOfLawDisplayValue);

        application.setRelationToLinkedCase(relationToLinkedCase);
        application.setOpponentAppliedForFunding(opponentAppliedForFunding);
        application.setDisplayStatus(displayStatus);
        application.setActualStatus(actualStatus);
        application.setClientFirstName(clientFirstName);
        application.setClientSurname(clientSurname);
        application.setClientReference(clientReference);
        application.setAmendment(amendment);
        application.setMeansAssessmentAmended(meansAssessmentAmended);
        application.setMeritsAssessmentAmended(meritsAssessmentAmended);
        application.setCostLimitChanged(costLimitChanged);
        application.setCostLimitAtTimeOfMerits(costLimitAtTimeOfMerits);

        application.setApplicationType(applicationType);
        application.setApplicationTypeDisplayValue(applicationTypeDisplayValue);
        application.setDevolvedPowersUsed(devolvedPowersUsed);
        application.setDateDevolvedPowersUsed(dateDevolvedPowersUsed);
        application.setDevolvedPowersContractFlag(devolvedPowersContractFlag);
        application.setMeritsReassessmentReqdInd(meritsReassessmentReqdInd);
        application.setLarScopeFlag(larScopeFlag);
        application.setLeadProceedingChangedOpaInput(leadProceedingChanged);

        application.setAuditTrail(buildAuditTrail());

        return application;
    }

    private AuditTrail buildAuditTrail(){
        return new AuditTrail(modifiedBy, createdBy);
    }

    private CostStructure buildCosts(){
        CostStructure costs = new CostStructure(buildAuditTrail());

        costs.setDefaultCostLimitation(defaultCostLimitation);
        costs.setGrantedCostLimitation(grantedCostLimitation);
        costs.setRequestedCostLimitation(requestedCostLimitation);

        return costs;
    }

    private Address buildAddress(){
        Address address = new Address(buildAuditTrail());
        address.setNoFixedAbode(noFixedAbode);
        address.setPostCode(postCode);
        address.setHouseNameNumber(houseNameNumber);
        address.setAddressLine1(addressLine1);
        address.setAddressLine2(addressLine2);
        address.setCity(city);
        address.setCounty(county);
        address.setCountry(country);
        address.setCareOf(careOf);
        address.setPreferredAddress(preferredAddress);
        return address;
    }




}
