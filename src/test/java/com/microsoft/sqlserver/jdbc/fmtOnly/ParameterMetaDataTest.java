package com.microsoft.sqlserver.jdbc.fmtOnly;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.microsoft.sqlserver.jdbc.TestUtils;
import com.microsoft.sqlserver.testframework.AbstractTest;


public class ParameterMetaDataTest extends AbstractTest {

    private static final String tableName = "[test_jdbc_" + UUID.randomUUID() + "]";

    @BeforeEach
    public void setupTests() throws SQLException {
        try (Connection c = DriverManager.getConnection(AbstractTest.connectionString);
                Statement s = c.createStatement()) {
            s.execute("CREATE TABLE " + tableName
                    + " (cBigint bigint, cNumeric numeric, cBit bit, cSmallint smallint, cDecimal decimal, "
                    + "cSmallmoney smallmoney, cInt int, cTinyint tinyint, cMoney money, cFloat float, "
                    + "cReal real, cDate date, cDatetimeoffset datetimeoffset, cDatetime2 datetime2, "
                    + "cSmalldatetime smalldatetime, cDatetime datetime, cTime time, cChar char, cVarchar varchar, "
                    + "cText text, cNchar nchar, cNvarchar nvarchar, cNtext ntext, cBinary binary, "
                    + "cVarbinary varbinary, cImage image);");
        }
    }

    @AfterEach
    public void cleanupTests() throws SQLException {
        try (Connection c = DriverManager.getConnection(AbstractTest.connectionString);
                Statement s = c.createStatement()) {
            TestUtils.dropTableIfExists(tableName, s);
        }
    }

    @Test
    public void compareStoredProcTest() throws SQLException {
        List<String> l = Arrays.asList(
                "SELECT * INTO " + tableName + " FROM " + tableName + " WHERE cBigint > ? " + "AND cBigint < ?;",

                "SET NOCOUNT ON;SELECT p.cText AS Product, p.cVarchar AS 'List Price' FROM " + tableName + " AS p JOIN "
                        + tableName + " AS s ON p.cInt = s.cInt WHERE s.[cVarchar] LIKE ? AND p.cFloat < ?;");
        l.forEach(this::compareFmtAndSp);
    }

    private void compareFmtAndSp(String userSQL) {
        try (Connection c = DriverManager.getConnection(AbstractTest.connectionString + ";useFmtOnly=true")) {
            PreparedStatement stmt1 = c.prepareStatement(userSQL);
            PreparedStatement stmt2 = connection.prepareStatement(userSQL);
            ParameterMetaData pmd1 = stmt1.getParameterMetaData();
            ParameterMetaData pmd2 = stmt2.getParameterMetaData();
            assertEquals(pmd1.getParameterCount(), pmd2.getParameterCount());
            for (int i = 1; i <= pmd1.getParameterCount(); i++) {
                System.out.println(pmd1.getParameterClassName(i));
                assertEquals(pmd1.getParameterClassName(i), pmd2.getParameterClassName(i));
                
                System.out.println(pmd1.getParameterMode(i));
                assertEquals(pmd1.getParameterMode(i), pmd2.getParameterMode(i));
                
                System.out.println(pmd1.getParameterType(i));
                assertEquals(pmd1.getParameterType(i), pmd2.getParameterType(i));
                
                System.out.println(pmd1.getParameterTypeName(i));
                assertEquals(pmd1.getParameterTypeName(i), pmd2.getParameterTypeName(i));;
                
                System.out.println(pmd1.getPrecision(i));
                assertEquals(pmd1.getPrecision(i), pmd2.getPrecision(i));;
                
                System.out.println(pmd1.getScale(i));
                assertEquals(pmd1.getScale(i), pmd2.getScale(i));
            }
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }
}
