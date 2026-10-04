package com.cyberguard.util;

import com.cyberguard.exception.DatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Creates JDBC connections. Credentials come from environment variables / JVM properties,
 * never from frontend code or source code.
 */
public final class DBUtil {
    private static final String URL;
    private static final String USER = cfg("DB_USER", "root");
    private static final String PASSWORD = cfg("DB_PASSWORD", "");

    static {
        String full = cfg("DB_URL", null);
        if (full == null) {
            full = "jdbc:mysql://" + cfg("DB_HOST", "localhost") + ":" + cfg("DB_PORT", "3306") + "/"
                    + cfg("DB_NAME", "cyberguard_db")
                    + "?sslMode=" + cfg("DB_SSLMODE", "PREFERRED")
                    + "&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
        }
        URL = full;
    }

    private DBUtil() { }

    public static String cfg(String key, String fallback) {
        String v = System.getenv(key);
        if (v == null || v.isBlank()) v = System.getProperty(key);
        return (v == null || v.isBlank()) ? fallback : v;
    }

    public static Connection getConnection() throws DatabaseException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DatabaseException("The service is temporarily unavailable (database connection failed). Please try again later.", e);
        }
    }
}
