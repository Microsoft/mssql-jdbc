/*
 * Microsoft JDBC Driver for SQL Server Copyright(c) Microsoft Corporation All rights reserved. This program is made
 * available under the terms of the MIT License. See the LICENSE file in the project root for more information.
 */
package com.microsoft.sqlserver.jdbc.fedauth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;

import com.microsoft.sqlserver.jdbc.RandomUtil;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.TestUtils;
import com.microsoft.sqlserver.testframework.AbstractSQLGenerator;
import com.microsoft.sqlserver.testframework.Constants;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;


@RunWith(JUnitPlatform.class)
@Tag("slow")
@Tag(Constants.Fedauth)
public class ConnectionSuspensionTest extends FedauthCommon {

    static String charTable = TestUtils.escapeSingleQuotes(
            AbstractSQLGenerator.escapeIdentifier(RandomUtil.getIdentifier("JDBC_ConnectionSuspension")));

    @Test
    public void testAccessTokenExpiredThenCreateNewStatement() throws SQLException {
        getFedauthInfo();
        long secondsPassed = 0;
        long start = System.currentTimeMillis();
        try {
            SQLServerDataSource ds = new SQLServerDataSource();

            if (enableADIntegrated) {
                ds.setServerName(azureServer);
                ds.setDatabaseName(azureDatabase);
                ds.setAuthentication("ActiveDirectoryIntegrated");
                ds.setHostNameInCertificate(hostNameInCertificate);
            } else {
                ds.setServerName(azureServer);
                ds.setDatabaseName(azureDatabase);
                ds.setUser(azureUserName);
                ds.setPassword(azurePassword);
                ds.setAuthentication("ActiveDirectoryPassword");
                ds.setHostNameInCertificate(hostNameInCertificate);
            }

            try (Connection connection = (SQLServerConnection) ds.getConnection();
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT SUSER_SNAME()")) {
                rs.next();
                assertTrue(azureUserName.equals(rs.getString(1)));

                if (!enableADIntegrated) {
                    try {
                        TestUtils.dropTableIfExists(charTable, stmt);
                        createTable(charTable, stmt);
                        populateCharTable(charTable, connection);
                        testChar(charTable, stmt);
                    } finally {
                        TestUtils.dropTableIfExists(charTable, stmt);
                    }
                }

                while (secondsPassed < secondsBeforeExpiration) {
                    Thread.sleep(TimeUnit.MINUTES.toMillis(5)); // Sleep for 2 minutes

                    secondsPassed = (System.currentTimeMillis() - start) / 1000;
                    try (Statement stmt1 = connection.createStatement();
                            ResultSet rs1 = stmt1.executeQuery("SELECT SUSER_SNAME()")) {
                        rs1.next();
                        assertTrue(azureUserName.equals(rs.getString(1)));

                        if (!enableADIntegrated) {
                            try {
                                TestUtils.dropTableIfExists(charTable, stmt1);
                                createTable(charTable, stmt1);
                                populateCharTable(charTable, connection);
                                testChar(charTable, stmt1);
                            } finally {
                                TestUtils.dropTableIfExists(charTable, stmt1);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().contains(ERR_MSG_RESULTSET_IS_CLOSED));
        }
    }

    @Test
    public void testAccessTokenExpiredThenExecuteUsingSameStatement() throws SQLException {
        getFedauthInfo();
        long secondsPassed = 0;
        long start = System.currentTimeMillis();
        try {

            SQLServerDataSource ds = new SQLServerDataSource();

            if (enableADIntegrated) {
                ds.setServerName(azureServer);
                ds.setDatabaseName(azureDatabase);
                ds.setAuthentication("ActiveDirectoryIntegrated");
                ds.setHostNameInCertificate(hostNameInCertificate);
            } else {
                ds.setServerName(azureServer);
                ds.setDatabaseName(azureDatabase);
                ds.setUser(azureUserName);
                ds.setPassword(azurePassword);
                ds.setAuthentication("ActiveDirectoryPassword");
                ds.setHostNameInCertificate(hostNameInCertificate);
            }

            try (Connection connection = ds.getConnection(); Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT SUSER_SNAME()")) {
                rs.next();
                assertTrue(azureUserName.equals(rs.getString(1)));

                if (!enableADIntegrated) {
                    try {
                        TestUtils.dropTableIfExists(charTable, stmt);
                        createTable(charTable, stmt);
                        populateCharTable(charTable, connection);
                        testChar(charTable, stmt);
                    } finally {
                        TestUtils.dropTableIfExists(charTable, stmt);
                    }
                }

                while (secondsPassed < secondsBeforeExpiration) {
                    Thread.sleep(TimeUnit.MINUTES.toMillis(5)); // Sleep for 2 minutes

                    secondsPassed = (System.currentTimeMillis() - start) / 1000;
                    try (ResultSet rs1 = stmt.executeQuery("SELECT SUSER_SNAME()")) {
                        rs1.next();
                        assertTrue(azureUserName.equals(rs.getString(1)));
                    }
                }
                if (!enableADIntegrated) {
                    try {
                        TestUtils.dropTableIfExists(charTable, stmt);
                        createTable(charTable, stmt);
                        populateCharTable(charTable, connection);
                        testChar(charTable, stmt);
                    } finally {
                        TestUtils.dropTableIfExists(charTable, stmt);
                    }
                }
            }
        } catch (Exception e) {
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().contains(ERR_MSG_RESULTSET_IS_CLOSED));
        }
    }

    private void createTable(String charTable, Statement stmt) throws SQLException {
        String createTableSql = "create table " + charTable + " (" + "PlainChar char(20) null,"
                + "PlainVarchar varchar(50) null," + "PlainVarcharMax varchar(max) null," + "PlainNchar nchar(30) null,"
                + "PlainNvarchar nvarchar(60) null," + "PlainNvarcharMax nvarchar(max) null" + ");";

        stmt.execute(createTableSql);
    }

    private void populateCharTable(String charTable, Connection connection) throws SQLException {
        String sql = "insert into " + charTable + " values( " + "?,?,?,?,?,?" + ")";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 6; i++) {
                pstmt.setString(i, "hello world!!!");
            }
            pstmt.execute();
        }
    }

    private void testChar(String charTable, Statement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery("select * from " + charTable)) {
            int numberOfColumns = rs.getMetaData().getColumnCount();
            rs.next();

            for (int i = 1; i <= numberOfColumns; i++) {
                try {
                    assertTrue(rs.getString(i).trim().equals("hello world!!!"));
                } catch (Exception e) {
                    fail(e.getMessage());
                }
            }
        }
    }

    @AfterAll
    public static void terminate() throws SQLException {
        try (Connection conn = DriverManager.getConnection(connectionString); Statement stmt = conn.createStatement()) {
            TestUtils.dropTableIfExists(charTable, stmt);
        }
    }
}