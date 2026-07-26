package io.github.mickablondo.imprint.store.jdbc;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This enum is intended to represent database-related constants.
 *
 * @author mickablondo
 */
@Getter
@AllArgsConstructor
public enum ImprintStoreDBEnum {
    TABLE("imprint_store");

    private final String value;

    @Getter
    @AllArgsConstructor
    public enum Column {
        ID("id"),
        DATA("data");

        private final String value;
    }
}
