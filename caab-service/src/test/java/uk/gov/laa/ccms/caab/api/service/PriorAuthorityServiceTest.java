package uk.gov.laa.ccms.caab.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.ApplicationMapper;
import uk.gov.laa.ccms.caab.api.repository.PriorAuthorityRepository;
import uk.gov.laa.ccms.caab.model.PriorAuthority;

@ExtendWith(MockitoExtension.class)
class PriorAuthorityServiceTest {

  @Mock
  private PriorAuthorityRepository priorAuthorityRepository;

  @Mock
  private ApplicationMapper applicationMapper;

  @InjectMocks
  private PriorAuthorityService priorAuthorityService;

  @Test
  void removePriorAuthority_whenAuthorityExists_removesPriorAuthority() {
    Long priorAuthorityId = 1L;

    when(priorAuthorityRepository.existsById(priorAuthorityId)).thenReturn(true);
    doNothing().when(priorAuthorityRepository).deleteById(priorAuthorityId);

    priorAuthorityService.removePriorAuthority(priorAuthorityId);

    verify(priorAuthorityRepository).existsById(priorAuthorityId);
    verify(priorAuthorityRepository).deleteById(priorAuthorityId);
  }

  @Test
  void removePriorAuthority_whenAuthorityNotExists_throwsException() {
    Long priorAuthorityId = 1L;

    when(priorAuthorityRepository.existsById(priorAuthorityId)).thenReturn(false);

    CaabApiException exception = assertThrows(CaabApiException.class, () ->
        priorAuthorityService.removePriorAuthority(priorAuthorityId));

    assertEquals("Prior Authority with id: 1 not found", exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

  @Test
  void updatePriorAuthority_whenAuthorityExists_updatesPriorAuthority() {
    Long priorAuthorityId = 1L;
    PriorAuthority priorAuthorityModel = new PriorAuthority();

    uk.gov.laa.ccms.caab.api.entity.PriorAuthority priorAuthorityEntity = new uk.gov.laa.ccms.caab.api.entity.PriorAuthority();
    priorAuthorityEntity.setId(priorAuthorityId);
    uk.gov.laa.ccms.caab.api.entity.ReferenceDataItem referenceDataItemEntity =
        new uk.gov.laa.ccms.caab.api.entity.ReferenceDataItem();
    priorAuthorityEntity.setItems(List.of(referenceDataItemEntity));

    when(priorAuthorityRepository.findById(priorAuthorityId)).thenReturn(Optional.of(priorAuthorityEntity));
    when(priorAuthorityRepository.save(priorAuthorityEntity)).thenReturn(priorAuthorityEntity);

    priorAuthorityService.updatePriorAuthority(priorAuthorityId, priorAuthorityModel);

    verify(applicationMapper).updatePriorAuthority(priorAuthorityEntity, priorAuthorityModel);
    verify(priorAuthorityRepository).findById(priorAuthorityId);
    verify(priorAuthorityRepository).save(priorAuthorityEntity);
  }

  @Test
  void updatePriorAuthority_whenAuthorityNotExists_throwsException() {
    Long priorAuthorityId = 1L;
    PriorAuthority priorAuthorityModel = new PriorAuthority();

    when(priorAuthorityRepository.findById(priorAuthorityId)).thenReturn(Optional.empty());

    CaabApiException exception = assertThrows(CaabApiException.class, () ->
        priorAuthorityService.updatePriorAuthority(priorAuthorityId, priorAuthorityModel));

    assertEquals("Prior Authority with id 1 not found", exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

}
