package io.github.mickablondo.imprint.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Test class for {@link JdbcImprintStore} using TestContainers to test the database connection and functionality.
 * /!\ Needs a running Docker daemon to run the tests.
 */
@Testcontainers
class JdbcImprintStoreIT {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17");

    private JdbcImprintStore store;

    @BeforeEach
    void setUp() throws Exception {
        // create schema
        try (Connection connection =
                     DriverManager.getConnection(
                             postgres.getJdbcUrl(),
                             postgres.getUsername(),
                             postgres.getPassword());
             Statement statement = connection.createStatement()) {

            statement.execute("""
                CREATE TABLE IF NOT EXISTS imprint_store (
                    id VARCHAR(64) PRIMARY KEY,
                    data BYTEA NOT NULL
                )
            """);
        }

        // create store
        DataSource dataSource = createDataSource();
        store = new JdbcImprintStore(dataSource);
    }

    @Test
    void should_save_and_load_data() {
        // GIVEN
        byte[] data = "hello-imprint".getBytes();
        // WHEN
        String key = store.save(data);
        byte[] loaded = store.load(key);
        // THEN
        assertArrayEquals(data, loaded);
    }

    private DataSource createDataSource() {
        org.postgresql.ds.PGSimpleDataSource ds =
                new org.postgresql.ds.PGSimpleDataSource();

        ds.setURL(postgres.getJdbcUrl());
        ds.setUser(postgres.getUsername());
        ds.setPassword(postgres.getPassword());

        return ds;
    }
}
