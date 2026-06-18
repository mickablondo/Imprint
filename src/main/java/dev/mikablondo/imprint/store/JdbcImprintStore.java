package dev.mikablondo.imprint.store;

import dev.mikablondo.imprint.ImprintStore;
import dev.mikablondo.imprint.core.exception.ImprintError;
import dev.mikablondo.imprint.core.exception.ImprintException;
import dev.mikablondo.imprint.core.utils.UUIDUtils;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@link ImprintStore} implementation that stores the serialized object in a database,
 * and returns a short UUID as seed.
 * The JdbcImprintStore needs to receive a datasource that is already configured to connect to the database where the imprint_store table is located.
 * The table should have the following structure:
 * <pre>
 * CREATE TABLE imprint_store (
 *     id VARCHAR(8) PRIMARY KEY,
 *     data BYTEA NOT NULL
 * );
 * </pre>
 */
@RequiredArgsConstructor
public class JdbcImprintStore implements ImprintStore {

    private final DataSource dataSource;

    /**
     * {@inheritDoc}
     *
     * @implSpec Stores the binary data in memory and returns a unique key.
     */
    @Override
    public String save(byte[] data) {
        final String sql = "INSERT INTO imprint_store (id, data) VALUES (?, ?)";
        final int maxAttempts = 5;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            String key = UUIDUtils.generate();

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, key);
                ps.setBytes(2, data);
                ps.executeUpdate();

                return key;
            } catch (SQLException e) {
                String sqlState = e.getSQLState();

                // SQLState class '23' = integrity constraint violation
                boolean constraintViolation = sqlState != null && sqlState.startsWith("23");

                if (constraintViolation && attempt < maxAttempts) {
                    // if duplicate key
                    continue;
                }

                throw new ImprintException(ImprintError.JDBC_SAVE_FAILED, e);
            }
        }

        throw new ImprintException(ImprintError.JDBC_SAVE_FAILED);
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec Retrieves the binary data from memory using the provided key.
     */
    @Override
    public byte[] load(String key) {
        String sql = "SELECT data FROM imprint_store WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, key);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new ImprintException(ImprintError.JDBC_NOT_FOUND);
                }
                return rs.getBytes(1);
            }
        } catch (SQLException e) {
            throw new ImprintException(ImprintError.JDBC_LOAD_FAILED, e);
        }
    }
}
