// ---------------------------------------------------------------------------------------------------------------------------------
// File: BulkTestWrapper.java
//
//
// Microsoft JDBC Driver for SQL Server
// Copyright(c) Microsoft Corporation
// All rights reserved.
// MIT License
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files(the "Software"),
// to deal in the Software without restriction, including without limitation the
// rights to use, copy, modify, merge, publish, distribute, sublicense,
// and / or sell copies of the Software, and to permit persons to whom the
// Software is furnished to do so, subject to the following conditions :
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
// THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
// IN THE SOFTWARE.
// ---------------------------------------------------------------------------------------------------------------------------------
package com.microsoft.sqlserver.jdbc.bulkCopy;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

import com.microsoft.sqlserver.jdbc.SQLServerBulkCopy;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopyOptions;
import com.microsoft.sqlserver.testframework.DBConnection;
import com.microsoft.sqlserver.testframework.DBResultSet;
import com.microsoft.sqlserver.testframework.DBStatement;
import com.microsoft.sqlserver.testframework.DBTable;

/**
 * Wrapper class that has all the data/values needed to execute BulkCopy test
 * case
 */
class BulkCopyTestWrapper {
    /**
     * Test case name
     */
    String testName;

    /**
     * <code>true</code> if SQLServerBulkCopy should use connection object
     */
    private boolean isUsingConnection;

    /**
     * <code>true</code> if SQLServerBulkCopy should include
     * SQLServerBulkCopyOptions
     */
    private boolean useBulkCopyOptions;

    private SQLServerBulkCopyOptions bulkOptions;

    private String connectionString;

    BulkCopyTestWrapper(String connectionString) {
        this.connectionString = connectionString;
    }

    /**
     * 
     * @return
     */
    public boolean isUsingConnection() {
        return isUsingConnection;
    }

    /**
     * 
     * @param isUsingConnection
     *            <code>true</code> if connection object should be passed in
     *            BulkCopy constructor <code>false</code> if connection string
     *            is to be passed to constructor
     */
    public void setUsingConnection(boolean isUsingConnection) {
        this.isUsingConnection = isUsingConnection;
        testName += "isUsingConnection=" + isUsingConnection + ";";
    }

    public boolean isUsingBulkCopyOptions() {
        return useBulkCopyOptions;
    }

    public void useBulkCopyOptions(boolean useBulkCopyOptions) {
        this.useBulkCopyOptions = useBulkCopyOptions;
        testName += "useBulkCopyOptions=" + useBulkCopyOptions + ";";
    }

    public SQLServerBulkCopyOptions getBulkOptions() {
        return bulkOptions;
    }

    public void setBulkOptions(SQLServerBulkCopyOptions bulkOptions) {
        this.bulkOptions = bulkOptions;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

}

class BulkCopyTestUtil {

    static void performBulkCopy(BulkCopyTestWrapper wrapper, DBTable sourceTable) {
        performBulkCopy(wrapper, sourceTable, false);
    }

    static void performBulkCopy(BulkCopyTestWrapper wrapper, DBTable sourceTable, boolean validateResult) {
        DBConnection con = null;
        DBStatement stmt = null;
        DBTable destinationTable = null;
        try {
            con = new DBConnection(wrapper.getConnectionString());
            stmt = con.createStatement();

            destinationTable = sourceTable.cloneSchema();
            stmt.createTable(destinationTable);
            DBResultSet srcResultSet = stmt.executeQuery("SELECT * FROM " + sourceTable.getEscapedTableName() + ";");
            SQLServerBulkCopy bulkCopy;
            if (wrapper.isUsingConnection()) {
                bulkCopy = new SQLServerBulkCopy((Connection) con.product());
            }
            else {
                bulkCopy = new SQLServerBulkCopy(wrapper.getConnectionString());
            }
            if (wrapper.isUsingBulkCopyOptions()) {
                bulkCopy.setBulkCopyOptions(wrapper.getBulkOptions());
            }
            bulkCopy.setDestinationTableName(destinationTable.getEscapedTableName());
            bulkCopy.writeToServer((ResultSet) srcResultSet.product());
            bulkCopy.close();
            if (validateResult) {
                validateValues(con, sourceTable, destinationTable);
            }
        }
        catch (SQLException ex) {
            fail(ex.getMessage());
        }
        finally {
            stmt.dropTable(destinationTable);
            con.close();
        }
    }

    static void validateValues(DBConnection con, DBTable sourceTable, DBTable destinationTable) throws SQLException {
        DBStatement srcStmt = con.createStatement();
        DBStatement dstStmt = con.createStatement();
        DBResultSet srcResultSet = srcStmt.executeQuery("SELECT * FROM " + sourceTable.getEscapedTableName() + ";");
        DBResultSet dstResultSet = dstStmt.executeQuery("SELECT * FROM " + destinationTable.getEscapedTableName() + ";");
        ResultSetMetaData destMeta = ((ResultSet) dstResultSet.product()).getMetaData();
        int totalColumns = destMeta.getColumnCount();

        // verify data from sourceType and resultSet
        while (srcResultSet.next() && dstResultSet.next())
            for (int i = 1; i <= totalColumns; i++) {
                // TODO: check row and column count in both the tables

                Object srcValue, dstValue;
                srcValue = srcResultSet.getObject(i);
                dstValue = dstResultSet.getObject(i);
                // Bulkcopy doesn't guarantee order of insertion -
                // if we need to test several rows either use primary key or
                // validate result based on sql JOIN
                switch (destMeta.getColumnType(i)) {
                    case java.sql.Types.BIGINT:
                        assertTrue((((Long) srcValue).longValue() == ((Long) dstValue).longValue()), "Unexpected bigint value");
                        break;

                    case java.sql.Types.INTEGER:
                        assertTrue((((Integer) srcValue).intValue() == ((Integer) dstValue).intValue()), "Unexpected int value");
                        break;

                    case java.sql.Types.SMALLINT:
                    case java.sql.Types.TINYINT:
                        assertTrue((((Short) srcValue).shortValue() == ((Short) dstValue).shortValue()), "Unexpected smallint/tinyint value");
                        break;

                    case java.sql.Types.BIT:
                        assertTrue((((Boolean) srcValue).booleanValue() == ((Boolean) dstValue).booleanValue()), "Unexpected bit value");
                        break;

                    case java.sql.Types.DECIMAL:
                    case java.sql.Types.NUMERIC:
                        assertTrue(0 == (((BigDecimal) srcValue).compareTo((BigDecimal) dstValue)),
                                "Unexpected decimal/numeric/money/smallmoney value");
                        break;

                    case java.sql.Types.DOUBLE:
                        assertTrue((((Double) srcValue).doubleValue() == ((Double) dstValue).doubleValue()), "Unexpected float value");
                        break;

                    case java.sql.Types.REAL:
                        assertTrue((((Float) srcValue).floatValue() == ((Float) dstValue).floatValue()), "Unexpected real value");
                        break;

                    case java.sql.Types.VARCHAR:
                    case java.sql.Types.NVARCHAR:
                        assertTrue((((String) srcValue).equals((String) dstValue)), "Unexpected varchar/nvarchar value ");
                        break;

                    case java.sql.Types.CHAR:
                    case java.sql.Types.NCHAR:
                        assertTrue((((String) srcValue).equals((String) dstValue)), "Unexpected char/nchar value ");
                        break;

                    case java.sql.Types.TIMESTAMP:
                        assertTrue((((Timestamp) srcValue).getTime() == (((Timestamp) dstValue).getTime())),
                                "Unexpected datetime/smalldatetime/datetime2 value");
                        break;

                    case java.sql.Types.DATE:
                        assertTrue((((Date) srcValue).getTime() == (((Date) dstValue).getTime())), "Unexpected datetime value");
                        break;

                    case java.sql.Types.TIME:
                        assertTrue(((Time) srcValue).getTime() == ((Time) dstValue).getTime(), "Unexpected time value ");
                        break;

                    case microsoft.sql.Types.DATETIMEOFFSET:
                        assertTrue(0 == ((microsoft.sql.DateTimeOffset) srcValue).compareTo((microsoft.sql.DateTimeOffset) dstValue),
                                "Unexpected time value ");
                        break;

                    default:
                        fail("Unhandled JDBCType " + JDBCType.valueOf(destMeta.getColumnType(i)));
                        break;
                }
            }
    }
}
