package uk.gov.laa.ccms.caab.mapper;

import org.junit.jupiter.api.Test;
import uk.gov.laa.ccms.api.entity.Address;
import uk.gov.laa.ccms.api.entity.Application;
import uk.gov.laa.ccms.api.entity.CostStructure;
import uk.gov.laa.ccms.api.mapper.ApplicationMapper;
import uk.gov.laa.ccms.api.mapper.ApplicationMapperImpl;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.ApplicationDetailCorrespondenceAddress;
import uk.gov.laa.ccms.caab.model.ApplicationDetailCosts;
import uk.gov.laa.ccms.caab.model.ApplicationDetailProvider;
import uk.gov.laa.ccms.caab.model.IntDisplayValue;
import uk.gov.laa.ccms.caab.model.StringDisplayValue;
import uk.gov.laa.ccms.caab.model.ApplicationDetailClient;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationMapperTest {

    private ApplicationMapper mapper = new ApplicationMapperImpl();

    @Test
    public void testApplicationMapping() {
        // Construct ApplicationDetail
        ApplicationDetail detail = new ApplicationDetail();
        detail.setCaseReferenceNumber("caseRef123");
        detail.setProvider(new ApplicationDetailProvider().id("providerId").displayValue("providerDisp").caseReference("providerCase"));
        detail.setOffice(new IntDisplayValue().id(1).displayValue("officeDisp"));
        detail.setSupervisor(new StringDisplayValue().id("supervisorId").displayValue("supervisorDisp"));
        detail.setFeeEarner(new StringDisplayValue().id("feeEarnerId").displayValue("feeEarnerDisp"));
        detail.setProviderContact(new StringDisplayValue().id("providerContactId").displayValue("providerContactDisp"));
        detail.setCategoryOfLaw(new StringDisplayValue().id("categoryLawId").displayValue("categoryLawDisp"));
        detail.setStatus(new StringDisplayValue().id("statusId").displayValue("statusDisp"));
        detail.setClient(new ApplicationDetailClient().firstName("clientFirst").surname("clientSurname").reference("clientRef"));
        detail.setApplicationType(new StringDisplayValue().id("appTypeId").displayValue("appTypeDisp"));
        detail.setMeritsReassessmentRequired(true);
        detail.setLeadProceedingChanged(true);

        // Convert ApplicationDetail to Application
        Application application = mapper.toApplication(detail);

        assertNull(application.getId()); // ID should be ignored and not set
        assertEquals("caseRef123", application.getLscCaseReference());
        assertEquals("providerId", application.getProviderId());
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
        ApplicationDetailCorrespondenceAddress detailAddress = new ApplicationDetailCorrespondenceAddress();
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
        ApplicationDetailCosts detailCosts = new ApplicationDetailCosts();
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
        detail.setProvider(new ApplicationDetailProvider());
        detail.getProvider().setId("PROV-001");
        detail.getProvider().setDisplayValue("Provider Display");
        detail.getProvider().setCaseReference("CASE-001");
        detail.setOffice(new IntDisplayValue().id(1).displayValue("Office 1"));
        detail.setSupervisor(new StringDisplayValue().id("Supervisor").displayValue("Supervisor Display"));
        detail.setFeeEarner(new StringDisplayValue().id("Fee Earner").displayValue("Fee Earner Display"));

        // Convert ApplicationDetail to Application
        Application application = mapper.toApplication(detail);

        // Assertions
        assertEquals("CASE-001", application.getLscCaseReference());
        assertEquals("PROV-001", application.getProviderId());
        assertEquals("Provider Display", application.getProviderDisplayValue());
        assertEquals("CASE-001", application.getProviderCaseReference());
        assertEquals(1L, application.getOfficeId());
        assertEquals("Office 1", application.getOfficeDisplayValue());
    }

    @Test
    public void testToAddressWithNullValues() {
        // Construct ApplicationDetailCorrespondenceAddress with null values
        ApplicationDetailCorrespondenceAddress detailAddress = new ApplicationDetailCorrespondenceAddress();

        // Convert ApplicationDetailCorrespondenceAddress to Address
        Address address = mapper.toAddress(detailAddress);

        // Assertions
        assertNull(address.getPostCode());
        assertNull(address.getHouseNameNumber());
    }

    @Test
    public void testToCostStructureWithZeroValues() {
        // Construct ApplicationDetailCosts with zero values
        ApplicationDetailCosts detailCosts = new ApplicationDetailCosts();
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

}
