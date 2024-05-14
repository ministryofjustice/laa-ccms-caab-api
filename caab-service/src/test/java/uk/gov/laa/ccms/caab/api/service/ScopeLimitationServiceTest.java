package uk.gov.laa.ccms.caab.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import uk.gov.laa.ccms.caab.api.entity.ScopeLimitation;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.ApplicationMapper;
import uk.gov.laa.ccms.caab.api.repository.ScopeLimitationRepository;
import uk.gov.laa.ccms.caab.model.ScopeLimitationDetail;

@ExtendWith(MockitoExtension.class)
class ScopeLimitationServiceTest {

  @Mock
  private ScopeLimitationRepository scopeLimitationRepository;

  @Mock
  private ApplicationMapper applicationMapper;

  @InjectMocks
  private ScopeLimitationService scopeLimitationService;

  @Test
  void removeScopeLimitation_whenExists_removesScopeLimitation() {
    Long scopeLimitationId = 1L;
    when(scopeLimitationRepository.existsById(scopeLimitationId)).thenReturn(true);
    doNothing().when(scopeLimitationRepository).deleteById(scopeLimitationId);

    scopeLimitationService.removeScopeLimitation(scopeLimitationId);

    verify(scopeLimitationRepository).existsById(scopeLimitationId);
    verify(scopeLimitationRepository).deleteById(scopeLimitationId);
  }

  @Test
  void removeScopeLimitation_whenNotExists_throwsException() {
    Long scopeLimitationId = 1L;
    when(scopeLimitationRepository.existsById(scopeLimitationId)).thenReturn(false);

    CaabApiException exception = assertThrows(CaabApiException.class, () ->
        scopeLimitationService.removeScopeLimitation(scopeLimitationId));

    assertEquals("Scope Limitation with id: 1 not found", exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

  @Test
  void updateScopeLimitation_whenExists_updatesScopeLimitation() {
    Long scopeLimitationId = 1L;
    ScopeLimitationDetail scopeLimitationModel = new ScopeLimitationDetail();
    ScopeLimitation scopeLimitationEntity = new ScopeLimitation();
    scopeLimitationEntity.setId(scopeLimitationId);

    when(scopeLimitationRepository.findById(scopeLimitationId)).thenReturn(Optional.of(scopeLimitationEntity));
    when(scopeLimitationRepository.save(scopeLimitationEntity)).thenReturn(scopeLimitationEntity);

    scopeLimitationService.updateScopeLimitation(scopeLimitationId, scopeLimitationModel);

    verify(applicationMapper).updateScopeLimitation(scopeLimitationEntity, scopeLimitationModel);
    verify(scopeLimitationRepository).findById(scopeLimitationId);
    verify(scopeLimitationRepository).save(scopeLimitationEntity);
  }

  @Test
  void updateScopeLimitation_whenNotExists_throwsException() {
    Long scopeLimitationId = 1L;
    ScopeLimitationDetail scopeLimitationModel = new ScopeLimitationDetail();

    when(scopeLimitationRepository.findById(scopeLimitationId)).thenReturn(Optional.empty());

    CaabApiException exception = assertThrows(CaabApiException.class, () ->
        scopeLimitationService.updateScopeLimitation(scopeLimitationId, scopeLimitationModel));

    assertEquals("Scope Limitation with id 1 not found", exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }
}
