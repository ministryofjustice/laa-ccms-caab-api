package uk.gov.laa.ccms.caab.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown in the caab api for application exceptions.
 */
@Getter
public class CaabApiException extends RuntimeException {

  private String errorMessage;

  private HttpStatus httpStatus;

  /**
   * The exception thrown for specific caab exceptions.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *                {@link #getMessage()} method.
   */
  public CaabApiException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
    this.errorMessage = message;
  }

}
