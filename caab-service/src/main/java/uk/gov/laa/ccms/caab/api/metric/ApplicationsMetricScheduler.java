package uk.gov.laa.ccms.caab.api.metric;

import io.prometheus.metrics.core.metrics.Gauge;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.gov.laa.ccms.caab.api.service.ApplicationService;

@Component
public class ApplicationsMetricScheduler {

  private final Gauge totalApplicationsInFlightGauge;
  private final ApplicationService applicationService;

  public ApplicationsMetricScheduler(PrometheusRegistry prometheusRegistry,
      ApplicationService applicationService) {
    this.totalApplicationsInFlightGauge = Gauge.builder()
        .name("pui_total_applications_in_tds")
        .help("Total number of applications in TDS")
        .register(prometheusRegistry);
    this.applicationService = applicationService;
  }

  /**
   * Updates the applications in flight gauge with the total number of applications in TDS.
   */
  @Scheduled(fixedRate = 10000)
  public void updateTotalApplicationsGauge() {
    totalApplicationsInFlightGauge.set(applicationService
        .getTotalApplications());
  }
}
