/*
 * Microsoft JDBC Driver for SQL Server Copyright(c) Microsoft Corporation All rights reserved. This program is made
 * available under the terms of the MIT License. See the LICENSE file in the project root for more information.
 */
package com.microsoft.sqlserver.jdbc.preparedStatement;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.microsoft.sqlserver.jdbc.RandomUtil;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.TestResource;
import com.microsoft.sqlserver.jdbc.TestUtils;
import com.microsoft.sqlserver.testframework.AbstractSQLGenerator;
import com.microsoft.sqlserver.testframework.AbstractTest;


/**
 * Tests with sql queries using preparedStatement without parameters
 * 
 *
 */
@RunWith(JUnitPlatform.class)
public class RegressionTest extends AbstractTest {
    static Connection con = null;

    static String tableName = AbstractSQLGenerator.escapeIdentifier(RandomUtil.getIdentifier("table"));



    /**
     * Setup before test
     * 
     * @throws SQLException
     */
    @BeforeAll
    public static void setupTest() throws SQLException {
        con = DriverManager.getConnection(connectionString);
        try (Statement stmt = con.createStatement()) {
            TestUtils.dropTableIfExists(tableName, stmt);
        }
    }

    /**
     * Tests creating view using preparedStatement
     * 
     * @throws SQLException
     */
    @Test
    public void createViewTest() throws SQLException {
        try (PreparedStatement pstmt1 = con.prepareStatement("create view " + tableName + " as select 1 a");
                PreparedStatement pstmt2 = con.prepareStatement("drop view " + tableName)) {
            pstmt1.execute();
            pstmt2.execute();
        } catch (SQLException e) {
            fail(TestResource.getResource("R_createDropViewFailed") + TestResource.getResource("R_errorMessage")
                    + e.getMessage());
        }
    }

    /**
     * Tests creating schema using preparedStatement
     * 
     * @throws SQLException
     */
    @Test
    public void createSchemaTest() throws SQLException {
        try (PreparedStatement pstmt1 = con.prepareStatement("create schema x");
                PreparedStatement pstmt2 = con.prepareStatement("drop schema x")) {
            pstmt1.execute();
            pstmt2.execute();
        } catch (SQLException e) {
            fail(TestResource.getResource("R_createDropSchemaFailed") + TestResource.getResource("R_errorMessage")
                    + e.getMessage());
        }
    }

    /**
     * Test creating and dropping tabel with preparedStatement
     * 
     * @throws SQLException
     */
    @Test
    public void createTableTest() throws SQLException {
        try (PreparedStatement pstmt1 = con.prepareStatement("create table " + tableName + " (col1 int)");
                PreparedStatement pstmt2 = con.prepareStatement("drop table " + tableName)) {
            pstmt1.execute();
            pstmt2.execute();
        } catch (SQLException e) {
            fail(TestResource.getResource("R_createDropTableFailed") + TestResource.getResource("R_errorMessage")
                    + e.getMessage());
        }
    }

    /**
     * Tests creating/altering/dropping table
     * 
     * @throws SQLException
     */
    @Test
    public void alterTableTest() throws SQLException {
        try (PreparedStatement pstmt1 = con.prepareStatement("create table " + tableName + " (col1 int)");
                PreparedStatement pstmt2 = con.prepareStatement("ALTER TABLE " + tableName + " ADD column_name char;");
                PreparedStatement pstmt3 = con.prepareStatement("drop table " + tableName)) {
            pstmt1.execute();
            pstmt2.execute();
            pstmt3.execute();
        } catch (SQLException e) {
            fail(TestResource.getResource("R_createDropAlterTableFailed") + TestResource.getResource("R_errorMessage")
                    + e.getMessage());
        }
    }

    /**
     * Tests with grant queries
     * 
     * @throws SQLException
     */
    @Test
    public void grantTest() throws SQLException {
        try (PreparedStatement pstmt1 = con.prepareStatement("create table " + tableName + " (col1 int)");
                PreparedStatement pstmt2 = con.prepareStatement("grant select on " + tableName + " to public");
                PreparedStatement pstmt3 = con.prepareStatement("revoke select on " + tableName + " from public");
                PreparedStatement pstmt4 = con.prepareStatement("drop table " + tableName)) {
            pstmt1.execute();
            pstmt2.execute();
            pstmt3.execute();
            pstmt4.execute();
        } catch (SQLException e) {
            fail(TestResource.getResource("R_grantFailed") + TestResource.getResource("R_errorMessage")
                    + e.getMessage());
        }
    }

    /**
     * Test with large string and batch
     * 
     * @throws SQLException
     */
    @Test
    public void batchWithLargeStringTest() throws Exception {
        batchWithLargeStringTestInternal("BatchInsert");
    }

    @Test
    public void batchWithLargeStringTestUseBulkCopyAPI() throws Exception {
        batchWithLargeStringTestInternal("BulkCopy");
    }

    private void batchWithLargeStringTestInternal(String mode) throws Exception {
        try (Connection con = DriverManager.getConnection(connectionString);) {
            if (mode.equalsIgnoreCase("bulkcopy")) {
                modifyConnectionForBulkCopyAPI((SQLServerConnection) con);
            }

            try (Statement stmt = con.createStatement()) {
                TestUtils.dropTableIfExists("TEST_TABLE", stmt);

                con.setAutoCommit(false);

                // create a table with two columns
                boolean createPrimaryKey = false;
                try {
                    stmt.execute("if object_id('TEST_TABLE', 'U') is not null\ndrop table TEST_TABLE;");
                    if (createPrimaryKey) {
                        stmt.execute("create table TEST_TABLE ( ID int, DATA nvarchar(max), primary key (ID) );");
                    } else {
                        stmt.execute("create table TEST_TABLE ( ID int, DATA nvarchar(max) );");
                    }
                } catch (Exception e) {
                    fail(e.toString());
                }

                con.commit();

                // build a String with 4001 characters
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < 4001; i++) {
                    stringBuilder.append('c');
                }
                String largeString = stringBuilder.toString();

                String[] values = {"a", "b", largeString, "d", "e"};
                // insert five rows into the table; use a batch for each row
                try (PreparedStatement pstmt = con.prepareStatement("insert into TEST_TABLE values (?,?)")) {
                    // 0,a
                    pstmt.setInt(1, 0);
                    pstmt.setNString(2, values[0]);
                    pstmt.addBatch();

                    // 1,b
                    pstmt.setInt(1, 1);
                    pstmt.setNString(2, values[1]);
                    pstmt.addBatch();

                    // 2,ccc...
                    pstmt.setInt(1, 2);
                    pstmt.setNString(2, values[2]);
                    pstmt.addBatch();

                    // 3,d
                    pstmt.setInt(1, 3);
                    pstmt.setNString(2, values[3]);
                    pstmt.addBatch();

                    // 4,e
                    pstmt.setInt(1, 4);
                    pstmt.setNString(2, values[4]);
                    pstmt.addBatch();

                    pstmt.executeBatch();
                } catch (Exception e) {
                    fail(e.toString());
                }
                con.commit();

                // check the data in the table
                Map<Integer, String> selectedValues = new LinkedHashMap<>();
                int id = 0;
                try (PreparedStatement pstmt = con.prepareStatement("select * from TEST_TABLE;")) {
                    try (ResultSet rs = pstmt.executeQuery()) {
                        int i = 0;
                        while (rs.next()) {
                            id = rs.getInt(1);
                            String data = rs.getNString(2);
                            if (selectedValues.containsKey(id)) {
                                fail("Found duplicate id: " + id + " ,actual values is : " + values[i++] + " data is: "
                                        + data);
                            }
                            selectedValues.put(id, data);
                        }
                    }
                } finally {
                    TestUtils.dropTableIfExists("TEST_TABLE", stmt);
                }
            }
        }
    }

    /**
     * Test with large string and tests with more batch queries
     * 
     * @throws SQLException
     */
    @Test
    public void addBatchWithLargeStringTest() throws SQLException {
        try (Statement stmt = con.createStatement()) {
            TestUtils.dropTableIfExists("TEST_TABLE", stmt);

            con.setAutoCommit(false);

            // create a table with two columns
            boolean createPrimaryKey = false;
            try {
                stmt.execute("if object_id('testTable', 'U') is not null\ndrop table testTable;");
                if (createPrimaryKey) {
                    stmt.execute("create table testTable ( ID int, DATA nvarchar(max), primary key (ID) );");
                } else {
                    stmt.execute("create table testTable ( ID int, DATA nvarchar(max) );");
                }
            } catch (Exception e) {
                fail(e.toString());
            }
            con.commit();

            // build a String with 4001 characters
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 4001; i++) {
                stringBuilder.append('x');
            }
            String largeString = stringBuilder.toString();

            // insert five rows into the table; use a batch for each row
            try (PreparedStatement pstmt = con.prepareStatement("insert into testTable values (?,?), (?,?);")) {
                // 0,a
                // 1,b
                pstmt.setInt(1, 0);
                pstmt.setNString(2, "a");
                pstmt.setInt(3, 1);
                pstmt.setNString(4, "b");
                pstmt.addBatch();

                // 2,c
                // 3,d
                pstmt.setInt(1, 2);
                pstmt.setNString(2, "c");
                pstmt.setInt(3, 3);
                pstmt.setNString(4, "d");
                pstmt.addBatch();

                // 4,xxx...
                // 5,f
                pstmt.setInt(1, 4);
                pstmt.setNString(2, largeString);
                pstmt.setInt(3, 5);
                pstmt.setNString(4, "f");
                pstmt.addBatch();

                // 6,g
                // 7,h
                pstmt.setInt(1, 6);
                pstmt.setNString(2, "g");
                pstmt.setInt(3, 7);
                pstmt.setNString(4, "h");
                pstmt.addBatch();

                // 8,i
                // 9,xxx...
                pstmt.setInt(1, 8);
                pstmt.setNString(2, "i");
                pstmt.setInt(3, 9);
                pstmt.setNString(4, largeString);
                pstmt.addBatch();

                pstmt.executeBatch();

                con.commit();
            }

            catch (Exception e) {
                fail(e.toString());
            } finally {
                TestUtils.dropTableIfExists("testTable", stmt);
            }
        }
    }

    /**
     * Cleanup after test
     * 
     * @throws SQLException
     */
    @AfterAll
    public static void cleanup() throws SQLException {
        try (Statement stmt = con.createStatement()) {
            TestUtils.dropTableIfExists("x", stmt);
            TestUtils.dropTableIfExists("TEST_TABLE", stmt);
        }
        if (null != con) {
            con.close();
        }
    }

    private void modifyConnectionForBulkCopyAPI(SQLServerConnection con) throws Exception {
        Field f1 = SQLServerConnection.class.getDeclaredField("isAzureDW");
        f1.setAccessible(true);
        f1.set(con, true);

        con.setUseBulkCopyForBatchInsert(true);
    }
}
