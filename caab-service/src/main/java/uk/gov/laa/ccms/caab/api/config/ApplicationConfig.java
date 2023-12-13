package uk.gov.laa.ccms.caab.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import uk.gov.laa.ccms.caab.api.audit.AuditorAwareImpl;

/**
 * Configuration for the application, and the auditor provider.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class ApplicationConfig {

  @Bean("auditorProvider")
  public AuditorAware<String> auditorProvider() {
    return new AuditorAwareImpl();
  }

}
