package com.microsoft.sqlserver.jdbc.unit;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.microsoft.sqlserver.testframework.AbstractSQLGenerator;
import com.microsoft.sqlserver.testframework.AbstractTest;
import com.microsoft.sqlserver.testframework.PrepUtil;
import com.microsoft.sqlserver.testframework.Utils;
import com.microsoft.sqlserver.testframework.util.RandomUtil;

/**
 * A class for testing the UTF8 support changes.
 */
@RunWith(JUnitPlatform.class)
public class UTF8SupportTest extends AbstractTest {
    private static Connection connection;
    private static String databaseName;
    private static String tableName;

    /**
     * Test against UTF8 CHAR type.
     * 
     * @throws SQLException
     */
    @Test
    public void testChar() throws SQLException {
        createTable("char(10)");
        validate("teststring");
        // This is 10 UTF-8 bytes. D1 82 D0 B5 D1 81 D1 82 31 32
        validate("тест12");
        // E2 95 A1 E2 95 A4 E2 88 9E 2D
        validate("╡╤∞-");

        createTable("char(4000)");
        validate(String.join("", Collections.nCopies(400, "teststring")));
        validate(String.join("", Collections.nCopies(400, "тест12")));
        validate(String.join("", Collections.nCopies(400, "╡╤∞-")));

        createTable("char(4001)");
        validate(String.join("", Collections.nCopies(400, "teststring")) + "1");
        validate(String.join("", Collections.nCopies(400, "тест12")) + "1");
        validate(String.join("", Collections.nCopies(400, "╡╤∞-")) + "1");

        createTable("char(8000)");
        validate(String.join("", Collections.nCopies(800, "teststring")));
        validate(String.join("", Collections.nCopies(800, "тест12")));
        validate(String.join("", Collections.nCopies(800, "╡╤∞-")));
    }

    /**
     * Test against UTF8 VARCHAR type.
     * 
     * @throws SQLException
     */
    @Test
    public void testVarchar() throws SQLException {
        createTable("varchar(10)");
        validate("teststring");
        validate("тест12");
        validate("╡╤∞-");

        createTable("varchar(4000)");
        validate(String.join("", Collections.nCopies(400, "teststring")));
        validate(String.join("", Collections.nCopies(400, "тест12")));
        validate(String.join("", Collections.nCopies(400, "╡╤∞-")));

        createTable("varchar(4001)");
        validate(String.join("", Collections.nCopies(400, "teststring")) + "1");
        validate(String.join("", Collections.nCopies(400, "тест12")) + "1");
        validate(String.join("", Collections.nCopies(400, "╡╤∞-")) + "1");

        createTable("varchar(8000)");
        validate(String.join("", Collections.nCopies(800, "teststring")));
        validate(String.join("", Collections.nCopies(800, "тест12")));
        validate(String.join("", Collections.nCopies(800, "╡╤∞-")));

        createTable("varchar(MAX)");
        validate(String.join("", Collections.nCopies(800, "teststring")));
        validate(String.join("", Collections.nCopies(800, "тест12")));
        validate(String.join("", Collections.nCopies(800, "╡╤∞-")));
    }

    @BeforeAll
    public static void setUp() throws ClassNotFoundException, SQLException {
        databaseName = RandomUtil.getIdentifier("UTF8Database");
        tableName = AbstractSQLGenerator.escapeIdentifier(RandomUtil.getIdentifier("RequestBoundaryTable"));
        connection = PrepUtil.getConnection(getConfiguredProperty("mssql_jdbc_test_connection_properties"));
        createDatabaseWithUTF8Collation();
        connection.setCatalog(databaseName);
    }
    
    @AfterAll
    public static void cleanUp() throws SQLException {
        Utils.dropDatabaseIfExists(databaseName, connection.createStatement());
    }

    private static void createDatabaseWithUTF8Collation() throws SQLException {
        try (Statement stmt = connection.createStatement();) {
            stmt.executeUpdate("CREATE DATABASE " + AbstractSQLGenerator.escapeIdentifier(databaseName) + " COLLATE Cyrillic_General_100_CS_AS_UTF8");
        }
    }

    private static void createTable(String columnType) throws SQLException {
        try (Statement stmt = connection.createStatement();) {
            Utils.dropTableIfExists(tableName, stmt);
            stmt.executeUpdate("CREATE TABLE " + tableName + " (c " + columnType + ")");
        }
    }

    public void clearTable() throws SQLException {
        try (Statement stmt = connection.createStatement();) {
            stmt.executeUpdate("DELETE FROM " + tableName);
        }
    }

    public void validate(String value) throws SQLException {
        if (Utils.serverSupportsUTF8(connection)) {
            try (PreparedStatement psInsert = connection.prepareStatement("INSERT INTO " + tableName + " VALUES(?)");
                    PreparedStatement psFetch = connection.prepareStatement("SELECT * FROM " + tableName);
                    Statement stmt = connection.createStatement();) {
                clearTable();
                // Used for exact byte comparison.
                byte[] valueBytes = value.getBytes(StandardCharsets.UTF_8);

                psInsert.setString(1, value);
                psInsert.executeUpdate();

                // Fetch using Statement.
                ResultSet rsStatement = stmt.executeQuery("SELECT * FROM " + tableName);
                rsStatement.next();
                // Compare Strings.
                assertEquals(value, rsStatement.getString(1));
                // Test UTF8 sequence returned from getBytes().
                assertArrayEquals(valueBytes, rsStatement.getBytes(1));

                // Fetch using PreparedStatement.
                ResultSet rsPreparedStatement = psFetch.executeQuery();
                rsPreparedStatement.next();
                assertEquals(value, rsPreparedStatement.getString(1));
                assertArrayEquals(valueBytes, rsPreparedStatement.getBytes(1));
            }
        }
    }
}
