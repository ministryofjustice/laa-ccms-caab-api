package uk.gov.laa.ccms.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the CAAB application.
 *
 * <p>This class initializes and starts the Spring Boot application, allowing
 * for the full lifecycle management of the CAAB system components and services.</p>
 */
@SpringBootApplication
public class CaabApiApplication {
  public static void main(String[] args) {
    SpringApplication.run(CaabApiApplication.class, args);
  }

}
