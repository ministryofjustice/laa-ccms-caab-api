package uk.gov.laa.ccms.data.api.controller;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_CLASS;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlMergeMode.MergeMode.MERGE;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import uk.gov.laa.ccms.caab.api.CaabApiApplication;

@SpringBootTest(classes = CaabApiApplication.class)
@SqlMergeMode(MERGE)
@ActiveProfiles("local")
@Sql(executionPhase = BEFORE_TEST_CLASS, scripts = "/sql/application_tables_create_schema.sql")
@Sql(executionPhase = AFTER_TEST_CLASS, scripts = "/sql/application_tables_drop_schema.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/sql/delete_data.sql")
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "/sql/delete_data.sql")
public class LinkedCaseControllerIntegrationLocalTest
    extends BaseLinkedCaseControllerIntegrationTest {

  //this runs all tests in BaseLinkedCaseControllerIntegrationTest, do not add anything here
  //this is an easy way to run the tests if you have the containerised database running locally already

}
