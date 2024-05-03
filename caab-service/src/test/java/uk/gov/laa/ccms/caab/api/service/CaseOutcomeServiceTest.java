package uk.gov.laa.ccms.caab.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildCaseOutcome;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import uk.gov.laa.ccms.caab.api.entity.CaseOutcome;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.CaseOutcomeMapper;
import uk.gov.laa.ccms.caab.api.repository.CaseOutcomeRepository;
import uk.gov.laa.ccms.caab.model.CaseOutcomeDetail;
import uk.gov.laa.ccms.caab.model.CaseOutcomeDetails;

@ExtendWith(MockitoExtension.class)
class CaseOutcomeServiceTest {
    @Mock
    private CaseOutcomeRepository repository;

    @Mock
    private CaseOutcomeMapper mapper;

    @InjectMocks
    private CaseOutcomeService caseOutcomeService;

    @Test
    void getCaseOutcomeDetails_queriesCorrectly() {
        final String caseRef = "123";
        final String providerId = "456";

        CaseOutcomeDetails expectedResponse = new CaseOutcomeDetails();

        List<CaseOutcome> caseOutcomeList = List.of(buildCaseOutcome());

        when(repository.findAll(any(Example.class))).thenReturn(caseOutcomeList);
        when(mapper.toCaseOutcomeDetails(caseOutcomeList)).thenReturn(expectedResponse);

        CaseOutcomeDetails result = caseOutcomeService.getCaseOutcomes(
            caseRef, providerId);

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(repository).findAll(any(Example.class));
        verify(mapper).toCaseOutcomeDetails(caseOutcomeList);
    }

    @Test
    void getCaseOutcomeDetail_retrievesCorrectly() {
        final Long caseOutcomeId = 123L;

        CaseOutcomeDetail expectedResponse = new CaseOutcomeDetail();

        CaseOutcome caseOutcome = buildCaseOutcome();

        when(repository.findById(caseOutcomeId)).thenReturn(Optional.of(caseOutcome));
        when(mapper.toCaseOutcomeDetail(caseOutcome)).thenReturn(expectedResponse);

        CaseOutcomeDetail result = caseOutcomeService.getCaseOutcome(caseOutcomeId);

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(repository).findById(caseOutcomeId);
        verify(mapper).toCaseOutcomeDetail(caseOutcome);
    }

    @Test
    void createCaseOutcome_createsSuccessfully() {
        CaseOutcomeDetail caseOutcomeDetail = new CaseOutcomeDetail();
        CaseOutcome caseOutcome = buildCaseOutcome();

        when(mapper.toCaseOutcome(eq(caseOutcomeDetail), any(List.class))).thenReturn(caseOutcome);
        when(repository.save(caseOutcome)).thenReturn(caseOutcome);

        Long result = caseOutcomeService.createCaseOutcome(caseOutcomeDetail);

        assertNotNull(result);
        assertEquals(caseOutcome.getId(), result);

        verify(mapper).toCaseOutcome(eq(caseOutcomeDetail), any(List.class));
        verify(repository).save(caseOutcome);
    }

    @Test
    void removeCaseOutcome_whenExists_removesEntry() {
        Long caseOutcomeId = 1L;
        when(repository.existsById(caseOutcomeId)).thenReturn(true);
        doNothing().when(repository).deleteById(caseOutcomeId);

        caseOutcomeService.removeCaseOutcome(caseOutcomeId);

        verify(repository).existsById(caseOutcomeId);
        verify(repository).deleteById(caseOutcomeId);
    }

    @Test
    void removeCaseOutcome_whenNotExists_throwsException() {
        Long caseOutcomeId = 1L;
        when(repository.existsById(caseOutcomeId)).thenReturn(false);

        CaabApiException exception = assertThrows(CaabApiException.class, () ->
            caseOutcomeService.removeCaseOutcome(caseOutcomeId));

        assertEquals("CaseOutcome with id: 1 not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void removeCaseOutcomeDetails_deletesBasedOnExample() {
        CaseOutcome caseOutcome = buildCaseOutcome();

        List<CaseOutcome> caseOutcomes =
            List.of(buildCaseOutcome());

        when(repository.findAll(any(Example.class))).thenReturn(caseOutcomes);

        caseOutcomeService.removeCaseOutcomes(
            caseOutcome.getLscCaseReference(),
            caseOutcome.getProviderId());

        verify(repository).findAll(any(Example.class));
        verify(repository).deleteAll(caseOutcomes);

    }

}

