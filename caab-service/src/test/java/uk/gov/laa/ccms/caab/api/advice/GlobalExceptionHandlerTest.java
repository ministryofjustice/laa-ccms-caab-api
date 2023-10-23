package uk.gov.laa.ccms.caab.api.advice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.advice.GlobalExceptionHandler;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

  @Mock
  private Logger loggerMock;

  @Mock
  private Model model;

  @InjectMocks
  private GlobalExceptionHandler globalExceptionHandler;

  @Test
  public void testHandleDataApiClientException() {
    final String errorMsg = "Test Exception";
    CaabApiException e = new CaabApiException(errorMsg, HttpStatus.BAD_GATEWAY);

    ResponseEntity<Object> response = globalExceptionHandler.handleCaabApiException(e);

    assertEquals(502, response.getStatusCode().value());
  }

}
