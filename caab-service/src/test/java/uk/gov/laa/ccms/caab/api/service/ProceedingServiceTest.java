package uk.gov.laa.ccms.caab.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import uk.gov.laa.ccms.caab.api.entity.Proceeding;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.ApplicationMapper;
import uk.gov.laa.ccms.caab.api.repository.ProceedingRepository;
import uk.gov.laa.ccms.caab.model.ProceedingDetail;
import uk.gov.laa.ccms.caab.model.ScopeLimitationDetail;


@ExtendWith(MockitoExtension.class)
class ProceedingServiceTest {

  @Mock
  private ProceedingRepository proceedingRepository;

  @Mock
  private ApplicationMapper applicationMapper;

  @InjectMocks
  private ProceedingService proceedingService;

  @Test
  void removeProceeding_whenExists_removesProceeding() {
    Long proceedingId = 1L;
    when(proceedingRepository.existsById(proceedingId)).thenReturn(true);
    doNothing().when(proceedingRepository).deleteById(proceedingId);

    proceedingService.removeProceeding(proceedingId);

    verify(proceedingRepository).existsById(proceedingId);
    verify(proceedingRepository).deleteById(proceedingId);
  }

  @Test
  void removeProceeding_whenNotExists_throwsException() {
    Long proceedingId = 1L;
    when(proceedingRepository.existsById(proceedingId)).thenReturn(false);

    CaabApiException exception = assertThrows(CaabApiException.class, () ->
        proceedingService.removeProceeding(proceedingId));

    assertEquals("Proceeding with id: 1 not found", exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

  @Test
  void updateProceeding_whenExists_updatesProceeding() {
    Long proceedingId = 1L;
    ProceedingDetail proceedingModel = new ProceedingDetail();
    Proceeding proceedingEntity = new Proceeding();
    proceedingEntity.setId(proceedingId);
    uk.gov.laa.ccms.caab.api.entity.ScopeLimitation scopeLimitationEntity = new uk.gov.laa.ccms.caab.api.entity.ScopeLimitation();
    proceedingEntity.setScopeLimitations(List.of(scopeLimitationEntity));

    when(proceedingRepository.findById(proceedingId)).thenReturn(Optional.of(proceedingEntity));
    when(proceedingRepository.save(proceedingEntity)).thenReturn(proceedingEntity);

    proceedingService.updateProceeding(proceedingId, proceedingModel);

    verify(applicationMapper).updateProceeding(proceedingEntity, proceedingModel);
    verify(proceedingRepository).findById(proceedingId);
    verify(proceedingRepository).save(proceedingEntity);
  }

  @Test
  void updateProceeding_whenNotExists_throwsException() {
    Long proceedingId = 1L;
    ProceedingDetail proceedingModel = new ProceedingDetail();

    when(proceedingRepository.findById(proceedingId)).thenReturn(Optional.empty());

    CaabApiException exception = assertThrows(CaabApiException.class, () ->
        proceedingService.updateProceeding(proceedingId, proceedingModel));

    assertEquals("Proceeding with id 1 not found", exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

  @Test
  void getScopeLimitationsForProceeding_whenExists_returnsScopeLimitations() {
    Long proceedingId = 1L;
    Proceeding proceedingEntity = new Proceeding();
    uk.gov.laa.ccms.caab.api.entity.ScopeLimitation scopeLimitationEntity = new uk.gov.laa.ccms.caab.api.entity.ScopeLimitation();
    proceedingEntity.setScopeLimitations(List.of(scopeLimitationEntity));

    when(proceedingRepository.findById(proceedingId)).thenReturn(Optional.of(proceedingEntity));
    when(applicationMapper.toScopeLimitationModel(any())).thenAnswer(i -> new ScopeLimitationDetail());

    List<ScopeLimitationDetail> result = proceedingService.getScopeLimitationsForProceeding(proceedingId);

    assertFalse(result.isEmpty());
    verify(proceedingRepository).findById(proceedingId);
  }

  @Test
  void getScopeLimitationsForProceeding_whenNotExists_throwsException() {
    Long proceedingId = 1L;

    when(proceedingRepository.findById(proceedingId)).thenReturn(Optional.empty());

    CaabApiException exception = assertThrows(CaabApiException.class, () ->
        proceedingService.getScopeLimitationsForProceeding(proceedingId));

    assertEquals("Application with id 1 not found", exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

  @Test
  void createScopeLimitationForProceeding_whenProceedingExists_createsScopeLimitation() {
    Long proceedingId = 1L;
    ScopeLimitationDetail scopeLimitation = new ScopeLimitationDetail();
    Proceeding proceedingEntity = new Proceeding();
    uk.gov.laa.ccms.caab.api.entity.ScopeLimitation scopeLimitationEntity = new uk.gov.laa.ccms.caab.api.entity.ScopeLimitation();
    List<uk.gov.laa.ccms.caab.api.entity.ScopeLimitation> scopeLimitationEntities = new ArrayList<>();
    scopeLimitationEntities.add(scopeLimitationEntity);
    proceedingEntity.setScopeLimitations(scopeLimitationEntities);

    when(proceedingRepository.findById(proceedingId)).thenReturn(Optional.of(proceedingEntity));
    when(proceedingRepository.save(proceedingEntity)).thenReturn(proceedingEntity);
    when(applicationMapper.toScopeLimitation(scopeLimitation)).thenReturn(scopeLimitationEntity);

    proceedingService.createScopeLimitationForProceeding(proceedingId, scopeLimitation);

    verify(proceedingRepository).findById(proceedingId);
    verify(proceedingRepository).save(any(Proceeding.class));
  }

  @Test
  void createScopeLimitationForProceeding_whenProceedingNotExists_throwsException() {
    Long proceedingId = 1L;
    ScopeLimitationDetail scopeLimitation = new ScopeLimitationDetail();

    when(proceedingRepository.findById(proceedingId)).thenReturn(Optional.empty());

    CaabApiException exception = assertThrows(CaabApiException.class, () ->
        proceedingService.createScopeLimitationForProceeding(proceedingId, scopeLimitation));

    assertEquals("Proceeding with id 1 not found", exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }
}
