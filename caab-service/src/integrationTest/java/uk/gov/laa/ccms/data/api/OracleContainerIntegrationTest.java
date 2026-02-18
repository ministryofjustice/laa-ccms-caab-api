package uk.gov.laa.ccms.data.api;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.oracle.OracleContainer;

@Testcontainers
@DirtiesContext
public abstract class OracleContainerIntegrationTest {

    @Container
    @ServiceConnection
    public static OracleContainer oracleContainer = new OracleContainer("gvenzl/oracle-free:23-slim-faststart")
            .withUsername("XXCCMS_PUI")
            .withPassword("XXCCMS_PUI");
}

