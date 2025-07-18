package uk.gov.laa.ccms.caab.api.metric;

import io.prometheus.metrics.core.metrics.Gauge;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.gov.laa.ccms.caab.api.service.ApplicationService;

/**
 * A scheduled component responsible for updating Prometheus metrics
 * related to the total number of applications in the TDS (Transaction Data Store).
 *
 * @author Jamie Briggs
 */
@Slf4j
@Component
public class ApplicationsMetricScheduler {

  @Getter
  private final Gauge totalApplicationsInFlightGauge;
  private final ApplicationService applicationService;

  /**
   * Constructs an instance of ApplicationsMetricScheduler, which is responsible for
   * registering and updating Prometheus metrics related to the total number of applications
   * in TDS.
   *
   * @param prometheusRegistry the Prometheus registry used to register and manage metrics
   * @param applicationService the service responsible for retrieving application-related data
   */
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
  @Scheduled(cron = "${laa.ccms.caab.scheduled.update-total-applications-metric-cron}")
  public void updateTotalApplicationsGauge() {
    Long totalApplications = applicationService
        .getTotalApplications();
    log.info("Updating total applications in TDS gauge to {}", totalApplications);
    totalApplicationsInFlightGauge.set(totalApplications);
  }
}
