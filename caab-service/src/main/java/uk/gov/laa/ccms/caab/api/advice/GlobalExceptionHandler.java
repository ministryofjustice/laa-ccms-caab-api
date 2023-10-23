package uk.gov.laa.ccms.caab.api.advice;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;

/**
 * Controller advice class responsible for handling exceptions globally and providing appropriate
 * error responses.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /**
   * Handles the {@link CaabApiException} by logging the error and returning an
   * appropriate error response.
   *
   * @param e the CaabApiException
   * @return the response entity with the status code and error response body.
   */
  @ExceptionHandler(value = {CaabApiException.class})
  public ResponseEntity<Object> handleCaabApiException(
      final CaabApiException e) {

    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("error_message", e.getErrorMessage());
    responseBody.put("http_status", e.getHttpStatus().value());

    return ResponseEntity.status(e.getHttpStatus()).body(responseBody);
  }


}
