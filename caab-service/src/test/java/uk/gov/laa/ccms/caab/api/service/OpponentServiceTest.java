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
import uk.gov.laa.ccms.caab.api.repository.OpponentRepository;
import uk.gov.laa.ccms.caab.model.Opponent;

@ExtendWith(MockitoExtension.class)
class OpponentServiceTest {

  @Mock
  private OpponentRepository opponentRepository;

  @Mock
  private ApplicationMapper applicationMapper;

  @InjectMocks
  private OpponentService opponentService;

  @Test
  void removeOpponent_whenExists_removesOpponent() {
    Long opponentId = 1L;
    when(opponentRepository.existsById(opponentId)).thenReturn(true);
    doNothing().when(opponentRepository).deleteById(opponentId);

    opponentService.removeOpponent(opponentId);

    verify(opponentRepository).existsById(opponentId);
    verify(opponentRepository).deleteById(opponentId);
  }

  @Test
  void removeOpponent_whenNotExists_throwsException() {
    Long opponentId = 1L;
    when(opponentRepository.existsById(opponentId)).thenReturn(false);

    CaabApiException exception = assertThrows(CaabApiException.class, () ->
        opponentService.removeOpponent(opponentId));

    assertEquals("Opponent with id: 1 not found", exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

  @Test
  void updateOpponent_whenExists_updatesOpponent() {
    Long opponentId = 1L;
    Opponent opponentModel = new Opponent();
    uk.gov.laa.ccms.caab.api.entity.Opponent opponentEntity = new uk.gov.laa.ccms.caab.api.entity.Opponent();
    opponentEntity.setId(opponentId);

    when(opponentRepository.findById(opponentId)).thenReturn(Optional.of(opponentEntity));
    when(opponentRepository.save(opponentEntity)).thenReturn(opponentEntity);

    opponentService.updateOpponent(opponentId, opponentModel);

    verify(applicationMapper).updateOpponent(opponentEntity, opponentModel);
    verify(opponentRepository).findById(opponentId);
    verify(opponentRepository).save(opponentEntity);
  }

  @Test
  void updateOpponent_whenNotExists_throwsException() {
    Long opponentId = 1L;
    Opponent opponentModel = new Opponent();

    when(opponentRepository.findById(opponentId)).thenReturn(Optional.empty());

    CaabApiException exception = assertThrows(CaabApiException.class, () ->
        opponentService.updateOpponent(opponentId, opponentModel));

    assertEquals("Opponent with id 1 not found", exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }
}
