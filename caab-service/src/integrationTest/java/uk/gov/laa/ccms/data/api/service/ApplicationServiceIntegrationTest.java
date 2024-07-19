package uk.gov.laa.ccms.data.api.service;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlMergeMode.MergeMode.MERGE;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import uk.gov.laa.ccms.caab.api.CaabApiApplication;
import uk.gov.laa.ccms.data.api.IntegrationTestInterface;

@SpringBootTest(classes = CaabApiApplication.class)
@SqlMergeMode(MERGE)
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "/sql/application_tables_create_schema.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/sql/application_tables_drop_schema.sql")
public class ApplicationServiceIntegrationTest extends
    BaseApplicationServiceIntegrationTest implements
    IntegrationTestInterface {

    //this runs all tests in BaseApplicationServiceIntegrationTest, do not add anything here
    //running this class takes longer than the local version, but it is used for running tests in a pipeline

}
