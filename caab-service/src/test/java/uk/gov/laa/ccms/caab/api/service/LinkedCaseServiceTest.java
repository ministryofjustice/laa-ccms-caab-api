package uk.gov.laa.ccms.caab.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.ApplicationMapper;
import uk.gov.laa.ccms.caab.api.repository.LinkedCaseRepository;
import uk.gov.laa.ccms.caab.model.LinkedCase;

@ExtendWith(MockitoExtension.class)
class LinkedCaseServiceTest {

  @Mock
  private LinkedCaseRepository linkedCaseRepository;

  @Mock
  private ApplicationMapper applicationMapper;

  @InjectMocks
  private LinkedCaseService linkedCaseService;

  @Test
  void removeLinkedCaseFromApplication_whenCaseExists_removesLinkedCase() {
      Long linkedCaseId = 2L;

      when(linkedCaseRepository.existsById(linkedCaseId)).thenReturn(true);
      doNothing().when(linkedCaseRepository).deleteById(linkedCaseId);

      linkedCaseService.removeLinkedCase(linkedCaseId);

      verify(linkedCaseRepository).existsById(linkedCaseId);
      verify(linkedCaseRepository).deleteById(linkedCaseId);
  }

  @Test
  void removeLinkedCaseFromApplication_whenCaseNotExists_throwsException() {
      Long linkedCaseId = 2L;

      when(linkedCaseRepository.existsById(linkedCaseId)).thenReturn(false);

      CaabApiException exception = assertThrows(CaabApiException.class, () ->
          linkedCaseService.removeLinkedCase(linkedCaseId));

      assertEquals("Linked case with id: 2 not found", exception.getMessage());
      assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }


  @Test
  void updateLinkedCaseForApplication_whenCaseExists_updatesLinkedCase() {
      Long linkedCaseId = 2L;
      LinkedCase linkedCaseModel = new LinkedCase();

      uk.gov.laa.ccms.caab.api.entity.LinkedCase linkedCaseEntity = new uk.gov.laa.ccms.caab.api.entity.LinkedCase();
      linkedCaseEntity.setId(linkedCaseId);

      when(linkedCaseRepository.findById(linkedCaseId)).thenReturn(Optional.of(linkedCaseEntity));
      when(linkedCaseRepository.save(linkedCaseEntity)).thenReturn(linkedCaseEntity);

      linkedCaseService.updateLinkedCase(linkedCaseId, linkedCaseModel);

      verify(applicationMapper).updateLinkedCase(linkedCaseEntity, linkedCaseModel);
      verify(linkedCaseRepository).findById(linkedCaseId);
      verify(linkedCaseRepository).save(linkedCaseEntity);
  }

  @Test
  void updateLinkedCaseForApplication_whenCaseNotExists_throwsException() {
      Long linkedCaseId = 2L;
      LinkedCase linkedCaseModel = new LinkedCase();

      uk.gov.laa.ccms.caab.api.entity.LinkedCase linkedCaseEntity = new uk.gov.laa.ccms.caab.api.entity.LinkedCase();
      linkedCaseEntity.setId(linkedCaseId);

      when(linkedCaseRepository.findById(linkedCaseId)).thenReturn(Optional.empty());

      CaabApiException exception = assertThrows(CaabApiException.class, () ->
          linkedCaseService.updateLinkedCase(linkedCaseId, linkedCaseModel));

      assertEquals("Linked case with id 2 not found", exception.getMessage());
      assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

}