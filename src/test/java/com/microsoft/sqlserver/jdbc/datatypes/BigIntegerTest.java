package com.microsoft.sqlserver.jdbc.datatypes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

import com.microsoft.sqlserver.jdbc.RandomUtil;
import com.microsoft.sqlserver.jdbc.TestUtils;
import com.microsoft.sqlserver.testframework.AbstractSQLGenerator;
import com.microsoft.sqlserver.testframework.AbstractTest;


/*
 * This test is for testing the setObject methods for the new data type mappings in JDBC 4.1 for java.math.BigInteger
 */
@RunWith(JUnitPlatform.class)
public class BigIntegerTest extends AbstractTest {

    enum TestType {
        SETOBJECT_WITHTYPE, // This is to test conversions in Table B-5
        SETOBJECT_WITHOUTTYPE, // This is to test conversions in Table B-4
        SETNULL // This is to test setNull method
    };

    final static String tableName = RandomUtil.getIdentifier("BigIntegerTestTable");
    final static String escapedTableName = AbstractSQLGenerator.escapeIdentifier(tableName);

    @Test
    public void testJDBC41BigInteger() throws Exception {
        try (Connection conn = DriverManager.getConnection(connectionString); Statement stmt = conn.createStatement()) {

            // Create the test table
            TestUtils.dropTableIfExists(escapedTableName, stmt);

            String query = "create table " + escapedTableName
                    + " (col1 varchar(100), col2 bigint, col3 real, col4 float, "
                    + "col5 numeric(38,0), col6 int, col7 smallint, col8 char(100), col9 varchar(max), "
                    + "id int IDENTITY primary key)";
            stmt.executeUpdate(query);

            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO " + escapedTableName
                    + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) SELECT * FROM " + escapedTableName + " where id = ?")) {

                // test that the driver converts the BigInteger values greater than LONG.MAX_VALUE and lesser than
                // LONG.MIN_VALUE correctly
                // A random value that is bigger than LONG.MAX_VALUE
                BigInteger bigIntPos = new BigInteger("922337203685477580776767676");
                // A random value that is smaller than LONG.MIN_VALUE
                BigInteger bigIntNeg = new BigInteger("-922337203685477580776767676");

                // Test the setObject method for different types of BigInteger values. Since BigInteger is mapped to
                // JDBC
                // BIGINT, the max and min limits for
                int row = 1;
                testSetObject(escapedTableName, BigInteger.valueOf(Long.MAX_VALUE), row++, pstmt,
                        TestType.SETOBJECT_WITHTYPE);

                testSetObject(escapedTableName, BigInteger.valueOf(Long.MIN_VALUE), row++, pstmt,
                        TestType.SETOBJECT_WITHTYPE);
                testSetObject(escapedTableName, BigInteger.valueOf(10), row++, pstmt, TestType.SETOBJECT_WITHTYPE);
                testSetObject(escapedTableName, BigInteger.valueOf(-10), row++, pstmt, TestType.SETOBJECT_WITHTYPE);
                testSetObject(escapedTableName, BigInteger.ZERO, row++, pstmt, TestType.SETOBJECT_WITHTYPE);
                testSetObject(escapedTableName, bigIntPos, row++, pstmt, TestType.SETOBJECT_WITHTYPE);
                testSetObject(escapedTableName, bigIntNeg, row++, pstmt, TestType.SETOBJECT_WITHTYPE);

                // Test setObject method with SQL TYPE parameter
                testSetObject(escapedTableName, BigInteger.valueOf(Long.MAX_VALUE), row++, pstmt,
                        TestType.SETOBJECT_WITHOUTTYPE);
                testSetObject(escapedTableName, BigInteger.valueOf(Long.MIN_VALUE), row++, pstmt,
                        TestType.SETOBJECT_WITHOUTTYPE);
                testSetObject(escapedTableName, BigInteger.valueOf(1000), row++, pstmt, TestType.SETOBJECT_WITHOUTTYPE);
                testSetObject(escapedTableName, BigInteger.valueOf(-1000), row++, pstmt,
                        TestType.SETOBJECT_WITHOUTTYPE);
                testSetObject(escapedTableName, BigInteger.ZERO, row++, pstmt, TestType.SETOBJECT_WITHOUTTYPE);
                testSetObject(escapedTableName, bigIntPos, row++, pstmt, TestType.SETOBJECT_WITHOUTTYPE);
                testSetObject(escapedTableName, bigIntNeg, row++, pstmt, TestType.SETOBJECT_WITHOUTTYPE);

                // Test setNull
                testSetObject(escapedTableName, bigIntNeg, row++, pstmt, TestType.SETNULL);

                TestUtils.dropTableIfExists(escapedTableName, stmt);
            }
        }
    }

    static void testSetObject(String tableName, BigInteger obj, int id, PreparedStatement pstmt,
            TestType testType) throws SQLException {
        if (TestType.SETOBJECT_WITHTYPE == testType) {
            callSetObjectWithType(obj, pstmt);
        } else if (TestType.SETOBJECT_WITHOUTTYPE == testType) {
            callSetObjectWithoutType(obj, pstmt);
        } else if (TestType.SETNULL == testType) {
            callSetNull(obj, pstmt);
        } else
            assertEquals(true, false, "Invalid test type");

        // The id column
        pstmt.setObject(10, id);

        pstmt.execute();
        pstmt.getMoreResults();
        try (ResultSet rs = pstmt.getResultSet()) {
            rs.next();

            if (TestType.SETNULL == testType) {
                for (int i = 1; 9 >= i; ++i) {
                    // Get the data first before calling rs.wasNull()
                    rs.getString(i);
                    assertEquals(rs.wasNull(), true, "setNull mismatch");
                }
                return;
            }

            if ((0 > obj.compareTo(BigInteger.valueOf(Long.MIN_VALUE)))
                    || (0 < obj.compareTo(BigInteger.valueOf(Long.MAX_VALUE)))) {
                // For the BigInteger values greater/less than Long limits test only the long data type.
                // This test is here just to make sure the driver does not do anything wired when the value is
                // bigger/smaller than JDBC BIGINT
                assertEquals(rs.getString(1), Long.valueOf(obj.longValue()).toString(),
                        "getString(greater/less than Long limits) mismatch");
                assertEquals(rs.getLong(2), obj.longValue(), "getLong(greater/less than Long limits) mismatch");
                // As CHAR is fixed length, rs.getString() returns a string of the size allocated in the database.
                // Need to trim it for comparison.
                assertEquals(rs.getString(8).trim(), Long.valueOf(obj.longValue()).toString(),
                        "getString(greater/less than Long limits (char)) mismatch");

                assertEquals(rs.getString(9), Long.valueOf(obj.longValue()).toString(),
                        "getString(greater/less than Long limits (varchar(max)))) mismatch");
            } else {
                assertEquals(rs.getString(1), obj.toString(), "getString mismatch");
                assertEquals(rs.getLong(2), obj.longValue(), "getLong mismatch");
                assertEquals(rs.getFloat(3), obj.floatValue(), "getFloat mismatch");
                assertEquals(rs.getDouble(4), obj.doubleValue(), "getDouble(float) mismatch");
                assertEquals(rs.getDouble(5), obj.doubleValue(), "getDouble(numeric) mismatch");
                if (obj.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) >= 0) {
                    assertEquals(rs.getInt(6), Integer.MAX_VALUE, "getInt(numeric) mismatch");
                } else if (obj.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) <= 0) {
                    assertEquals(rs.getInt(6), Integer.MIN_VALUE, "getInt(numeric) mismatch");
                } else {
                    assertEquals(rs.getInt(6), obj.intValue(), "getInt(numeric) mismatch");
                }
                if (obj.compareTo(BigInteger.valueOf(Short.MAX_VALUE)) >= 0) {
                    assertEquals(rs.getShort(7), Short.MAX_VALUE, "getShort(numeric) mismatch");
                } else if (obj.compareTo(BigInteger.valueOf(Short.MIN_VALUE)) <= 0) {
                    assertEquals(rs.getShort(7), Short.MIN_VALUE, "getShort(numeric) mismatch");
                } else {
                    assertEquals(rs.getShort(7), obj.shortValue(), "getShort(numeric) mismatch");
                }

                assertEquals(rs.getString(8).trim(), obj.toString(), "getString(char) mismatch");
                assertEquals(rs.getString(9), obj.toString(), "getString(varchar(max)) mismatch");
            }
        }
    }

    static void callSetObjectWithType(BigInteger obj, PreparedStatement pstmt) throws SQLException {
        pstmt.setObject(1, obj, java.sql.Types.VARCHAR);
        pstmt.setObject(2, obj, java.sql.Types.BIGINT);
        pstmt.setObject(3, obj, java.sql.Types.FLOAT);
        pstmt.setObject(4, obj, java.sql.Types.DOUBLE);
        pstmt.setObject(5, obj, java.sql.Types.NUMERIC);
        // Use Integer/Short limits instead of Long limits for the int/smallint column
        if (obj.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) >= 0) {
            pstmt.setObject(6, BigInteger.valueOf(Integer.MAX_VALUE), java.sql.Types.INTEGER);
        } else if (obj.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) <= 0) {
            pstmt.setObject(6, BigInteger.valueOf(Integer.MIN_VALUE), java.sql.Types.INTEGER);
        } else {
            pstmt.setObject(6, obj, java.sql.Types.INTEGER);
        }
        if (obj.compareTo(BigInteger.valueOf(Short.MAX_VALUE)) >= 0) {
            pstmt.setObject(7, BigInteger.valueOf(Short.MAX_VALUE), java.sql.Types.SMALLINT);
        } else if (obj.compareTo(BigInteger.valueOf(Short.MIN_VALUE)) <= 0) {
            pstmt.setObject(7, BigInteger.valueOf(Short.MIN_VALUE), java.sql.Types.SMALLINT);
        } else {
            pstmt.setObject(7, obj, java.sql.Types.SMALLINT);
        }
        pstmt.setObject(8, obj, java.sql.Types.CHAR);
        pstmt.setObject(9, obj, java.sql.Types.LONGVARCHAR);
    }

    static void callSetObjectWithoutType(BigInteger obj, PreparedStatement pstmt) throws SQLException {
        // Cannot send a long value to a column of type int/smallint (even if the long value is small enough to fit in
        // those types)
        pstmt.setObject(1, obj);
        pstmt.setObject(2, obj);
        pstmt.setObject(3, obj);
        pstmt.setObject(4, obj);
        pstmt.setObject(5, obj);
        // Use Integer/Short limits instead of Long limits for the int/smallint column
        if (obj.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) >= 0) {
            pstmt.setObject(6, BigInteger.valueOf(Integer.MAX_VALUE));
        } else if (obj.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) <= 0) {
            pstmt.setObject(6, BigInteger.valueOf(Integer.MIN_VALUE));
        } else {
            pstmt.setObject(6, obj);
        }
        if (obj.compareTo(BigInteger.valueOf(Short.MAX_VALUE)) >= 0) {
            pstmt.setObject(7, BigInteger.valueOf(Short.MAX_VALUE));
        } else if (obj.compareTo(BigInteger.valueOf(Short.MIN_VALUE)) <= 0) {
            pstmt.setObject(7, BigInteger.valueOf(Short.MIN_VALUE));
        } else {
            pstmt.setObject(7, obj);
        }

        pstmt.setObject(8, obj);
        pstmt.setObject(9, obj);
    }

    static void callSetNull(BigInteger obj, PreparedStatement pstmt) throws SQLException {
        pstmt.setNull(1, java.sql.Types.VARCHAR);
        pstmt.setNull(2, java.sql.Types.BIGINT);
        pstmt.setNull(3, java.sql.Types.FLOAT);
        pstmt.setNull(4, java.sql.Types.DOUBLE);
        pstmt.setNull(5, java.sql.Types.NUMERIC);
        pstmt.setNull(6, java.sql.Types.INTEGER);
        pstmt.setNull(7, java.sql.Types.SMALLINT);
        pstmt.setNull(8, java.sql.Types.CHAR);
        pstmt.setNull(9, java.sql.Types.LONGVARCHAR);
    }
}