package uk.gov.laa.ccms.caab.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.laa.ccms.api.entity.Address;
import uk.gov.laa.ccms.api.entity.Application;
import uk.gov.laa.ccms.api.entity.AuditTrail;
import uk.gov.laa.ccms.api.entity.CostStructure;
import uk.gov.laa.ccms.api.mapper.ApplicationMapper;
import uk.gov.laa.ccms.api.service.ApplicationService;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.api.repository.ApplicationRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    private static final String CAAB_USER_LOGIN_ID = "testUser";
    private static final String OLD_USER = "oldUser";

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ApplicationMapper applicationMapper;

    @InjectMocks
    private ApplicationService applicationService;

    /**
     * Test case to verify that an application is created with correspondence address and costs.
     */
    @Test
    void createApplication_createsApplicationWithCorrespondenceAddressAndCosts() {
        ApplicationDetail applicationDetail = new ApplicationDetail(null,null,null,null);
        Application application = new Application();

        mockMapperAndRepository(applicationDetail, application);

        applicationService.createApplication(CAAB_USER_LOGIN_ID, applicationDetail);

        verifyInteractionsWithMocks(applicationDetail, application);
        verifyAuditTrailSetOnEntities(application);
    }

    /**
     * Test case to verify that an application is updated with existing address and costs.
     */
    @Test
    void createApplication_updatesExistingAddressAndCosts() {
        ApplicationDetail applicationDetail = new ApplicationDetail();
        Application application = createApplicationWithExistingAddressAndCosts();

        mockMapperAndRepository(applicationDetail, application);

        applicationService.createApplication(CAAB_USER_LOGIN_ID, applicationDetail);

        verifyInteractionsWithMocks(applicationDetail, application);
        verifyAuditTrailSetOnEntities(application);
    }

    /**
     * Helper method to setup the mocking behaviour of applicationMapper and applicationRepository.
     */
    private void mockMapperAndRepository(ApplicationDetail applicationDetail, Application application) {
        when(applicationMapper.toApplication(applicationDetail)).thenReturn(application);
        when(applicationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    /**
     * Helper method to verify the interactions with applicationMapper and applicationRepository.
     */
    private void verifyInteractionsWithMocks(ApplicationDetail applicationDetail, Application application) {
        verify(applicationMapper).toApplication(applicationDetail);
        verify(applicationRepository).save(application);
    }

    /**
     * Helper method to verify that AuditTrail is set on the Application, Address, and CostStructure.
     */
    private void verifyAuditTrailSetOnEntities(Application application) {
        assertAuditTrailSet(CAAB_USER_LOGIN_ID, application.getAuditTrail());
        assertAuditTrailSet(CAAB_USER_LOGIN_ID, application.getCorrespondenceAddress().getAuditTrail());
        assertAuditTrailSet(CAAB_USER_LOGIN_ID, application.getCosts().getAuditTrail());
    }

    /**
     * Helper method to assert that AuditTrail is correctly set on individual entity.
     */
    private void assertAuditTrailSet(String userId, AuditTrail auditTrail) {
        assert auditTrail != null;
        assert userId.equals(auditTrail.getCreatedBy());
        assert userId.equals(auditTrail.getModifiedBy());
    }

    /**
     * Helper method to create an Application with existing Address and CostStructure.
     */
    private Application createApplicationWithExistingAddressAndCosts() {
        AuditTrail auditTrailOld = createAuditTrail(OLD_USER);

        Address existingAddress = new Address(auditTrailOld);
        CostStructure existingCosts = new CostStructure(auditTrailOld);

        Application application = new Application();
        application.setCorrespondenceAddress(existingAddress);
        application.setCosts(existingCosts);

        return application;
    }

    /**
     * Helper method to create a new AuditTrail object.
     */
    private AuditTrail createAuditTrail(String userId) {
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setCreatedBy(userId);
        auditTrail.setModifiedBy(userId);
        return auditTrail;
    }
}

