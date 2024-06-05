package uk.gov.laa.ccms.data.api;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
@DirtiesContext
public interface IntegrationTestInterface {

  OracleContainerSingleton oracleContainerSingleton = OracleContainerSingleton.getInstance();

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", oracleContainerSingleton.getOracleContainer()::getJdbcUrl);
    registry.add("spring.datasource.username", oracleContainerSingleton.getOracleContainer()::getUsername);
    registry.add("spring.datasource.password", oracleContainerSingleton.getOracleContainer()::getPassword);

    registry.add("laa.ccms.springboot.starter.auth.authorized-clients", () -> "[{\"name\":\"caab-ui\",\"roles\":[\"ALL\"],\"token\":\"c196393a-f279-45ba-b5d5-f93e6d30465a\"}]");
    registry.add("laa.ccms.springboot.starter.auth.authorized-roles", () -> "[{\"name\":\"ALL\",\"URIs\":[\"/**\"]}]");
    registry.add("laa.ccms.springboot.starter.auth.unprotected-uris", () -> "[\"\"]");
  }
}

