
package com.shishu.utility.jdbc;

import java.io.Serializable;
import java.sql.Connection;

/**
 * Provides a database connection.
 */
public interface ConnectionProvider extends Serializable {
    /**
     * method must be idempotent.
     */
    void prepare();

    /**
     *
     * @return a DB connection over which the queries can be executed.
     */
    Connection getConnection();

    /**
     * called once when the system is shutting down, should be idempotent.
     */
    void cleanup();
}
