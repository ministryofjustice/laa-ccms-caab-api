package uk.gov.laa.ccms.caab.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import uk.gov.laa.ccms.caab.api.entity.Address;
import uk.gov.laa.ccms.caab.api.entity.Application;
import uk.gov.laa.ccms.caab.api.entity.CostStructure;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.ApplicationMapper;
import uk.gov.laa.ccms.caab.api.repository.ApplicationRepository;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.ApplicationDetails;
import uk.gov.laa.ccms.caab.model.ApplicationProviderDetails;
import uk.gov.laa.ccms.caab.model.ApplicationType;
import uk.gov.laa.ccms.caab.model.LinkedCase;
import uk.gov.laa.ccms.caab.model.PriorAuthority;
import uk.gov.laa.ccms.caab.model.Proceeding;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {
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
        ApplicationDetail applicationDetail = new ApplicationDetail();
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
        ApplicationDetail applicationDetail = new ApplicationDetail();
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
        ApplicationDetail expectedResponse = new ApplicationDetail();

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
        ApplicationProviderDetails providerDetails = new ApplicationProviderDetails();

        when(applicationRepository.findById(id)).thenReturn(Optional.empty());

        CaabApiException exception = assertThrows(CaabApiException.class, () -> {
            applicationService.putProviderDetails(id, providerDetails);
        });

        verify(applicationRepository).findById(id);

        assertEquals("Application with id 1 not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void getLinkedCasesForApplication_whenExists_returnsLinkedCases() {
        Long id = 1L;
        List<LinkedCase> expectedLinkedCases = Arrays.asList(new LinkedCase(), new LinkedCase()); // Assuming some mock linked cases
        Application application = new Application();
        application.setLinkedCases(new ArrayList<>()); // Set linked cases in the application

        when(applicationRepository.findById(id)).thenReturn(Optional.of(application));
        when(applicationMapper.toLinkedCaseModelList(any())).thenReturn(expectedLinkedCases);

        List<LinkedCase> result = applicationService.getLinkedCasesForApplication(id);

        verify(applicationRepository).findById(id);
        verify(applicationMapper).toLinkedCaseModelList(application.getLinkedCases());

        assertEquals(expectedLinkedCases, result);
    }

    @Test
    void getLinkedCasesForApplication_whenNotExists_throwsException() {
        Long id = 1L;

        when(applicationRepository.findById(id)).thenReturn(Optional.empty());

        CaabApiException exception = assertThrows(CaabApiException.class, () -> {
            applicationService.getLinkedCasesForApplication(id);
        });

        verify(applicationRepository).findById(id);

        assertEquals("Linked cases for application with id " + id + " not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void getApplicationCorrespondenceAddress_whenExists_returnsAddress() {
        Long id = 1L;
        uk.gov.laa.ccms.caab.model.Address expectedAddress = new uk.gov.laa.ccms.caab.model.Address(); // Mock address
        Application application = new Application();
        application.setCorrespondenceAddress(new Address()); // Set a mock address in the application

        when(applicationRepository.findById(id)).thenReturn(Optional.of(application));
        when(applicationMapper.toAddressModel(any())).thenReturn(expectedAddress);

        uk.gov.laa.ccms.caab.model.Address result = applicationService.getApplicationCorrespondenceAddress(id);

        verify(applicationRepository).findById(id);
        verify(applicationMapper).toAddressModel(application.getCorrespondenceAddress());

        assertEquals(expectedAddress, result);
    }

    @Test
    void getApplicationCorrespondenceAddress_whenNotExists_throwsException() {
        Long id = 1L;

        when(applicationRepository.findById(id)).thenReturn(Optional.empty());

        CaabApiException exception = assertThrows(CaabApiException.class, () -> {
            applicationService.getApplicationCorrespondenceAddress(id);
        });

        verify(applicationRepository).findById(id);

        assertEquals("Correspondence address for application with id " + id + " not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void putCorrespondenceAddress_whenExists_updatesAddress() {
        Long id = 1L;
        uk.gov.laa.ccms.caab.model.Address newAddress = new uk.gov.laa.ccms.caab.model.Address(); // Mock new address
        Application application = new Application();

        when(applicationRepository.findById(id)).thenReturn(Optional.of(application));

        applicationService.putCorrespondenceAddress(id, newAddress);

        verify(applicationRepository).findById(id);
        verify(applicationMapper).addCorrespondenceAddressToApplication(application, newAddress);
        verify(applicationRepository).save(application);
    }

    @Test
    void putCorrespondenceAddress_whenNotExists_throwsException() {
        Long id = 1L;
        uk.gov.laa.ccms.caab.model.Address newAddress = new uk.gov.laa.ccms.caab.model.Address();

        when(applicationRepository.findById(id)).thenReturn(Optional.empty());

        CaabApiException exception = assertThrows(CaabApiException.class, () -> {
            applicationService.putCorrespondenceAddress(id, newAddress);
        });

        verify(applicationRepository).findById(id);

        assertEquals("Application with id " + id + " not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }


    @Test
    void createLinkedCaseForApplication_whenApplicationExists_createsLinkedCase() {
        Long applicationId = 1L;
        LinkedCase linkedCase = new LinkedCase();
        Application application = new Application();
        application.setLinkedCases(new ArrayList<>());

        uk.gov.laa.ccms.caab.api.entity.LinkedCase linkedCaseEntity = new uk.gov.laa.ccms.caab.api.entity.LinkedCase();

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(applicationMapper.toLinkedCase(linkedCase)).thenReturn(linkedCaseEntity);

        applicationService.createLinkedCaseForApplication(applicationId, linkedCase);

        verify(applicationRepository).findById(applicationId);
        verify(applicationMapper).toLinkedCase(linkedCase);
        verify(applicationRepository).save(application);
        assertTrue(application.getLinkedCases().contains(linkedCaseEntity));
    }

    @Test
    void createLinkedCaseForApplication_whenApplicationNotExists_throwsException() {
        Long applicationId = 1L;
        LinkedCase linkedCase = new LinkedCase();

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.empty());

        CaabApiException exception = assertThrows(CaabApiException.class, () ->
            applicationService.createLinkedCaseForApplication(applicationId, linkedCase));

        assertEquals("Application with id 1 not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    /**
     * Test case get applications returns data.
     */
    @Test
    void getApplications_returnsData() {
        Application application = new Application();
        application.setLscCaseReference("caseref");
        application.setProviderCaseReference("provref");
        application.setClientSurname("surname");
        application.setClientReference("clientref");
        application.setFeeEarner("feeearner");
        application.setOfficeId(100);
        application.setActualStatus("stat");

        Pageable pageable = Pageable.unpaged();

        Page<Application> applicationPage = new PageImpl<>(List.of(application));
        ApplicationDetails expectedResponse = new ApplicationDetails();

        when(applicationMapper.toApplicationDetails(applicationPage)).thenReturn(expectedResponse);
        when(applicationRepository.findAll(any(Example.class), eq(pageable))).thenReturn(applicationPage);

        ApplicationDetails response = applicationService.getApplications(
            application.getLscCaseReference(),
            application.getProviderCaseReference(),
            application.getClientSurname(),
            application.getClientReference(),
            application.getFeeEarner(),
            application.getOfficeId(),
            application.getActualStatus(),
            pageable
        );

        verify(applicationMapper).toApplicationDetails(applicationPage);
        verify(applicationRepository).findAll(any(Example.class), eq(pageable));

        assertEquals(response, expectedResponse);
    }

    @Test
    void getProceedingsForApplication_WhenExists_ReturnsProceedings() {
        Long applicationId = 1L;
        Application application = new Application();
        application.setProceedings(new ArrayList<>());
        Proceeding proceedingModel = new Proceeding();
        application.getProceedings().add(new uk.gov.laa.ccms.caab.api.entity.Proceeding());

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(applicationMapper.toProceedingModel(any())).thenReturn(proceedingModel);

        List<Proceeding> result = applicationService.getProceedingsForApplication(applicationId);

        verify(applicationRepository).findById(applicationId);
        verify(applicationMapper, times(application.getProceedings().size())).toProceedingModel(any());

        assertFalse(result.isEmpty());
        assertEquals(proceedingModel, result.get(0));
    }

    @Test
    void getProceedingsForApplication_WhenNotExists_ThrowsException() {
        Long applicationId = 1L;

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.empty());

        CaabApiException exception = assertThrows(CaabApiException.class, () ->
            applicationService.getProceedingsForApplication(applicationId));

        assertEquals("Application with id 1 not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void createProceedingForApplication_WhenApplicationExists_CreatesProceeding() {
        Long applicationId = 1L;
        Proceeding proceeding = new Proceeding();
        Application application = new Application();
        uk.gov.laa.ccms.caab.api.entity.Proceeding proceedingEntity = new uk.gov.laa.ccms.caab.api.entity.Proceeding();
        application.setProceedings(new ArrayList<>());
        proceedingEntity.setScopeLimitations(new ArrayList<>());

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(applicationMapper.toProceeding(proceeding)).thenReturn(proceedingEntity);

        applicationService.createProceedingForApplication(applicationId, proceeding);

        verify(applicationRepository).findById(applicationId);
        verify(applicationMapper).toProceeding(proceeding);
        verify(applicationRepository).save(application);
        assertTrue(application.getProceedings().contains(proceedingEntity));
    }

    @Test
    void createProceedingForApplication_WhenApplicationNotExists_ThrowsException() {
        Long applicationId = 1L;
        Proceeding proceeding = new Proceeding();

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.empty());

        CaabApiException exception = assertThrows(CaabApiException.class, () ->
            applicationService.createProceedingForApplication(applicationId, proceeding));

        assertEquals("Application with id 1 not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void getPriorAuthoritiesForApplication_WhenExists_ReturnsPriorAuthorities() {
        Long applicationId = 1L;
        Application application = new Application();
        application.setPriorAuthorities(new ArrayList<>());
        PriorAuthority priorAuthorityModel = new PriorAuthority();
        application.getPriorAuthorities().add(new uk.gov.laa.ccms.caab.api.entity.PriorAuthority());

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(applicationMapper.toPriorAuthorityModel(any())).thenReturn(priorAuthorityModel);

        List<PriorAuthority> result = applicationService.getPriorAuthoritiesForApplication(applicationId);

        verify(applicationRepository).findById(applicationId);
        verify(applicationMapper, times(application.getPriorAuthorities().size())).toPriorAuthorityModel(any());

        assertFalse(result.isEmpty());
        assertEquals(priorAuthorityModel, result.get(0));
    }

    @Test
    void getPriorAuthoritiesForApplication_WhenNotExists_ThrowsException() {
        Long applicationId = 1L;

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.empty());

        CaabApiException exception = assertThrows(CaabApiException.class, () ->
            applicationService.getPriorAuthoritiesForApplication(applicationId));

        assertEquals("Application with id 1 not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void createPriorAuthorityForApplication_WhenApplicationExists_CreatesPriorAuthority() {
        Long applicationId = 1L;
        PriorAuthority priorAuthority = new PriorAuthority();
        Application application = new Application();
        uk.gov.laa.ccms.caab.api.entity.PriorAuthority priorAuthorityEntity = new uk.gov.laa.ccms.caab.api.entity.PriorAuthority();
        application.setPriorAuthorities(new ArrayList<>());
        priorAuthorityEntity.setItems(new ArrayList<>());

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(applicationMapper.toPriorAuthority(priorAuthority)).thenReturn(priorAuthorityEntity);

        applicationService.createPriorAuthorityForApplication(applicationId, priorAuthority);

        verify(applicationRepository).findById(applicationId);
        verify(applicationMapper).toPriorAuthority(priorAuthority);
        verify(applicationRepository).save(application);
        assertTrue(application.getPriorAuthorities().contains(priorAuthorityEntity));
    }

    @Test
    void createPriorAuthorityForApplication_WhenApplicationNotExists_ThrowsException() {
        Long applicationId = 1L;
        PriorAuthority priorAuthority = new PriorAuthority();

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.empty());

        CaabApiException exception = assertThrows(CaabApiException.class, () ->
            applicationService.createPriorAuthorityForApplication(applicationId, priorAuthority));

        assertEquals("Application with id 1 not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void getApplicationCostStructure_WhenExists_ReturnsCostStructure() {
        Long applicationId = 1L;
        Application application = new Application();
        application.setCosts(new CostStructure());
        uk.gov.laa.ccms.caab.model.CostStructure expectedCostStructure = new uk.gov.laa.ccms.caab.model.CostStructure();

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(applicationMapper.toCostStructureModel(application.getCosts())).thenReturn(expectedCostStructure);

        uk.gov.laa.ccms.caab.model.CostStructure result = applicationService.getApplicationCostStructure(applicationId);

        verify(applicationRepository).findById(applicationId);
        verify(applicationMapper).toCostStructureModel(application.getCosts());

        assertEquals(expectedCostStructure, result);
    }

    @Test
    void getApplicationCostStructure_WhenNotExists_ThrowsException() {
        Long applicationId = 1L;

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.empty());

        CaabApiException exception = assertThrows(CaabApiException.class, () ->
            applicationService.getApplicationCostStructure(applicationId));

        assertEquals("Cost structure for application with id 1 not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void putCostStructure_WhenExists_UpdatesCostStructure() {
        Long applicationId = 1L;
        uk.gov.laa.ccms.caab.model.CostStructure costStructure = new uk.gov.laa.ccms.caab.model.CostStructure();
        Application application = new Application();

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));

        applicationService.putCostStructure(applicationId, costStructure);

        verify(applicationRepository).findById(applicationId);
        verify(applicationMapper).addCostStructureToApplication(application, costStructure);
        verify(applicationRepository).save(application);
    }

    @Test
    void putCostStructure_WhenNotExists_ThrowsException() {
        Long applicationId = 1L;
        uk.gov.laa.ccms.caab.model.CostStructure costStructure = new uk.gov.laa.ccms.caab.model.CostStructure();

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.empty());

        CaabApiException exception = assertThrows(CaabApiException.class, () ->
            applicationService.putCostStructure(applicationId, costStructure));

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


}

