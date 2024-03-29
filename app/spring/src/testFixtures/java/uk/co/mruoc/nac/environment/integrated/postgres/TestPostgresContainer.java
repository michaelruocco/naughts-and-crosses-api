package uk.co.mruoc.nac.environment.integrated.postgres;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class TestPostgresContainer extends PostgreSQLContainer<TestPostgresContainer> {

  private static final String DATABASE_NAME = "naughts-and-crosses-api";
  private static final String USERNAME = "postgres";
  private static final String PASSWORD = "postgres";

  public TestPostgresContainer() {
    super(DockerImageName.parse("postgres:16"));
    withDatabaseName(DATABASE_NAME);
    withUsername(USERNAME);
    withPassword(PASSWORD);
    withInitScript("postgres/create-empty-schema.sql");
  }
}
