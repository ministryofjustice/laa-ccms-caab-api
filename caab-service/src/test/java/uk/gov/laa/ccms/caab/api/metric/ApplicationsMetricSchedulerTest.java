package uk.gov.laa.ccms.caab.api.metric;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.prometheus.metrics.model.registry.PrometheusRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.laa.ccms.caab.api.service.ApplicationService;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApplicationsMetricSchedulerTest")
class ApplicationsMetricSchedulerTest {

  @Mock
  PrometheusRegistry prometheusRegistry;
  @Mock
  ApplicationService applicationService;
  ApplicationsMetricScheduler applicationsMetricScheduler;

  @BeforeEach
  void beforeEach(){
    applicationsMetricScheduler = new ApplicationsMetricScheduler(prometheusRegistry,
        applicationService);
  }

  @Test
  @DisplayName("Verify gauge initialized")
  void verifyGaugeInitialized(){
    // Then
    verify(prometheusRegistry, times(1))
        .register(applicationsMetricScheduler.getTotalApplicationsInFlightGauge());
  }

  @Test
  @DisplayName("Should update gauge with total applications in TDS")
  void shouldUpdateGaugeWithTotalApplicationsInTDS(){
    // Given
    when(applicationService.getTotalApplications()).thenReturn(578L);
    // When
    applicationsMetricScheduler.updateTotalApplicationsGauge();
    // Then
    assertThat(applicationsMetricScheduler.getTotalApplicationsInFlightGauge().get())
        .isEqualTo(578L);
  }
}