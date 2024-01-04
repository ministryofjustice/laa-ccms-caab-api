package uk.gov.laa.ccms.caab.api.service;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import uk.gov.laa.ccms.caab.api.entity.Address;
import uk.gov.laa.ccms.caab.api.entity.Application;
import uk.gov.laa.ccms.caab.api.entity.AuditTrail;
import uk.gov.laa.ccms.caab.api.entity.CostStructure;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.ApplicationMapper;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.api.repository.ApplicationRepository;
import uk.gov.laa.ccms.caab.model.ApplicationProviderDetails;
import uk.gov.laa.ccms.caab.model.ApplicationType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        applicationService.createApplication(applicationDetail);

        verifyInteractionsWithMocks(applicationDetail, application);
    }

    /**
     * Test case to verify that an application is updated with existing address and costs.
     */
    @Test
    void createApplication_updatesExistingAddressAndCosts() {
        ApplicationDetail applicationDetail = new ApplicationDetail(null,null,null,null);
        Application application = createApplicationWithExistingAddressAndCosts();

        mockMapperAndRepository(applicationDetail, application);

        applicationService.createApplication(applicationDetail);

        verifyInteractionsWithMocks(applicationDetail, application);
    }

    /**
     * Test case get an application returns data.
     */
    @Test
    void getApplication_returnsData() {
        Application application = new Application();
        ApplicationDetail expectedResponse = new ApplicationDetail(null,null,null,null);

        when(applicationMapper.toApplicationDetail(application)).thenReturn(expectedResponse);
        when(applicationRepository.findById(any())).thenReturn(Optional.of(application));

        ApplicationDetail response = applicationService.getApplication(1L);

        verify(applicationMapper).toApplicationDetail(application);
        verify(applicationRepository).findById(1L);

        assertEquals(response, expectedResponse);
    }

    /**
     * Test case get an application returns null.
     */
    @Test
    void getApplication_returnsNull() {
        when(applicationRepository.findById(any())).thenReturn(Optional.empty());

        // Use assertThrows to check if the method throws the expected exception
        CaabApiException exception = assertThrows(CaabApiException.class, () -> {
            applicationService.getApplication(1L);
        });

        verify(applicationRepository).findById(1L);

        // Optionally, you can check the exception message and HTTP status code
        assertEquals("Application with id 1 not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void getApplicationType_whenExists_returnsApplicationType() {
        Long id = 1L;
        ApplicationType expectedApplicationType = new ApplicationType();

        when(applicationRepository.findById(id)).thenReturn(Optional.of(new Application())); // Assuming Application exists
        when(applicationMapper.toApplicationType(any())).thenReturn(expectedApplicationType);

        ApplicationType result = applicationService.getApplicationType(id);

        verify(applicationRepository).findById(id);
        verify(applicationMapper).toApplicationType(any());

        assertEquals(expectedApplicationType, result);
    }

    /**
     * Test case to get an application type when it does not exist.
     */
    @Test
    void getApplicationType_whenNotExists_throwsException() {
        Long id = 1L;

        when(applicationRepository.findById(id)).thenReturn(Optional.empty());

        // Use assertThrows to check if the method throws the expected exception
        CaabApiException exception = assertThrows(CaabApiException.class, () -> {
            applicationService.getApplicationType(id);
        });

        verify(applicationRepository).findById(id);

        // Optionally, you can check the exception message and HTTP status code
        assertEquals("Application with id 1 not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    /**
     * Test case to patch an application type when the application exists.
     */
    @Test
    void patchApplicationType_whenExists_updatesApplicationType() {
        Long id = 1L;
        ApplicationType applicationType = new ApplicationType();
        Application application = new Application();

        when(applicationRepository.findById(id)).thenReturn(Optional.of(application)); // Assuming Application exists

        applicationService.putApplicationType(id, applicationType);

        verify(applicationRepository).findById(id);
        verify(applicationMapper).addApplicationType(application, applicationType);
        verify(applicationRepository).save(application);
    }

    /**
     * Test case to patch an application type when the application does not exist.
     */
    @Test
    void patchApplicationType_whenNotExists_throwsException() {
        Long id = 1L;
        ApplicationType applicationType = new ApplicationType();

        when(applicationRepository.findById(id)).thenReturn(Optional.empty());

        // Use assertThrows to check if the method throws the expected exception
        CaabApiException exception = assertThrows(CaabApiException.class, () -> {
            applicationService.putApplicationType(id, applicationType);
        });

        verify(applicationRepository).findById(id);

        // Optionally, you can check the exception message and HTTP status code
        assertEquals("Application with id 1 not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void getApplicationProviderDetails_whenExists_returnsProviderDetails() {
        Long id = 1L;
        ApplicationProviderDetails expectedProviderDetails = new ApplicationProviderDetails();
        Application application = new Application();

        when(applicationRepository.findById(id)).thenReturn(Optional.of(application)); // Assuming Application exists
        when(applicationMapper.toProviderDetails(application)).thenReturn(expectedProviderDetails);

        ApplicationProviderDetails result = applicationService.getApplicationProviderDetails(id);

        verify(applicationRepository).findById(id);
        verify(applicationMapper).toProviderDetails(application);

        assertEquals(expectedProviderDetails, result);
    }

    @Test
    void getApplicationProviderDetails_whenNotExists_throwsException() {
        Long id = 1L;

        when(applicationRepository.findById(id)).thenReturn(Optional.empty());

        // Use assertThrows to check if the method throws the expected exception
        CaabApiException exception = assertThrows(CaabApiException.class, () -> {
            applicationService.getApplicationProviderDetails(id);
        });

        verify(applicationRepository).findById(id);

        // Optionally, you can check the exception message and HTTP status code
        assertEquals("Application with id 1 not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void patchProviderDetails_whenExists_updatesProviderDetails() {
        Long id = 1L;
        String caabUserLoginId = "testUser";
        ApplicationProviderDetails providerDetails = new ApplicationProviderDetails();
        Application application = new Application();

        when(applicationRepository.findById(id)).thenReturn(Optional.of(application)); // Assuming Application exists

        applicationService.putProviderDetails(id, providerDetails);

        verify(applicationRepository).findById(id);
        verify(applicationMapper).addProviderDetails(application, providerDetails);
        verify(applicationRepository).save(application);
    }

    @Test
    void patchProviderDetails_whenNotExists_throwsException() {
        Long id = 1L;
        String caabUserLoginId = "testUser";
        ApplicationProviderDetails providerDetails = new ApplicationProviderDetails();

        when(applicationRepository.findById(id)).thenReturn(Optional.empty());

        CaabApiException exception = assertThrows(CaabApiException.class, () -> {
            applicationService.putProviderDetails(id, providerDetails);
        });

        verify(applicationRepository).findById(id);

        assertEquals("Application with id 1 not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
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
        assert userId.equals(auditTrail.getLastSavedBy());
    }

    /**
     * Helper method to create an Application with existing Address and CostStructure.
     */
    private Application createApplicationWithExistingAddressAndCosts() {
        Address existingAddress = new Address();
        CostStructure existingCosts = new CostStructure();

        Application application = new Application();
        application.setCorrespondenceAddress(existingAddress);
        application.setCosts(existingCosts);

        return application;
    }

    /**
     * Helper method to create a new AuditTrail object.
     */
    private AuditTrail createAuditTrail() {
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setCreatedBy(OLD_USER);
        auditTrail.setLastSavedBy(OLD_USER);
        return auditTrail;
    }
}

