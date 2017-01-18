package com.microsoft.sqlserver.jdbc.tvp;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import com.microsoft.sqlserver.jdbc.SQLServerPreparedStatement;
import com.microsoft.sqlserver.testframework.AbstractTest;
import com.microsoft.sqlserver.testframework.DBConnection;
import com.microsoft.sqlserver.testframework.DBResultSet;
import com.microsoft.sqlserver.testframework.DBStatement;

@RunWith(JUnitPlatform.class)
public class TVPSchemaTest extends AbstractTest {

    private static DBConnection conn = null;
    static DBStatement stmt = null;
    static DBResultSet rs = null;
    static String expectecValue1 = "hello";
    static String expectecValue2 = "world";
    static String expectecValue3 = "again";
    protected static String schemaName = "anotherSchma";
    protected static String tvpNameWithouSchema = "charTVP";
    protected static String tvpNameWithSchema = "[" + schemaName + "].[" + tvpNameWithouSchema + "]";
    protected static String charTable = "[" + schemaName + "].[tvpCharTable]";
    protected static String procedureName = "[" + schemaName + "].[procedureThatCallsTVP]";

    /**
     * 
     * @throws SQLException
     */
    @Test
    @DisplayName("TVPSchema_PreparedStatement_StoredProcedure()")
    public void testTVPSchema_PreparedStatement_StoredProcedure() throws SQLException {
        conn = new DBConnection(connectionString);
        stmt = conn.createStatement();

        dropProcedure();
        dropTables();
        dropTVPS();

        dropAndCreateSchema();

        createTVPS();
        createTables();
        createPreocedure();

        final String sql = "{call " + procedureName + "(?)}";

        SQLServerDataTable tvp = new SQLServerDataTable();
        tvp.addColumnMetadata("PlainChar", java.sql.Types.CHAR);
        tvp.addColumnMetadata("PlainVarchar", java.sql.Types.VARCHAR);
        tvp.addColumnMetadata("PlainVarcharMax", java.sql.Types.VARCHAR);

        tvp.addRow(expectecValue1, expectecValue2, expectecValue3);
        tvp.addRow(expectecValue1, expectecValue2, expectecValue3);
        tvp.addRow(expectecValue1, expectecValue2, expectecValue3);
        tvp.addRow(expectecValue1, expectecValue2, expectecValue3);
        tvp.addRow(expectecValue1, expectecValue2, expectecValue3);

        SQLServerPreparedStatement P_C_statement = (SQLServerPreparedStatement) connection.prepareStatement(sql);

        P_C_statement.setStructured(1, tvpNameWithSchema, tvp);
        P_C_statement.execute();

        rs = stmt.executeQuery("select * from " + charTable);

        while (rs.next()) {
            String actualValue1 = rs.getString(1);
            String actualValue2 = rs.getString(2);
            String actualValue3 = rs.getString(3);

            assertTrue(actualValue1.trim().equals(expectecValue1),
                    "actual value does not match expected value." + "\n\tExpected value: " + expectecValue1 + "\n\tActual value: " + actualValue1);

            assertTrue(actualValue2.trim().equals(expectecValue2),
                    "actual value does not match expected value." + "\n\tExpected value: " + expectecValue2 + "\n\tActual value: " + actualValue2);

            assertTrue(actualValue3.trim().equals(expectecValue3),
                    "actual value does not match expected value." + "\n\tExpected value: " + expectecValue3 + "\n\tActual value: " + actualValue3);
        }
        terminateVariation();
    }

    /**
     * 
     * @throws SQLException
     */
    @Test
    @DisplayName("TVPSchema_CallableStatement_StoredProcedure()")
    public void testTVPSchema_CallableStatement_StoredProcedure() throws SQLException {
        conn = new DBConnection(connectionString);
        stmt = conn.createStatement();

        dropProcedure();
        dropTables();
        dropTVPS();

        dropAndCreateSchema();

        createTVPS();
        createTables();
        createPreocedure();

        final String sql = "{call " + procedureName + "(?)}";

        SQLServerDataTable tvp = new SQLServerDataTable();
        tvp.addColumnMetadata("PlainChar", java.sql.Types.CHAR);
        tvp.addColumnMetadata("PlainVarchar", java.sql.Types.VARCHAR);
        tvp.addColumnMetadata("PlainVarcharMax", java.sql.Types.VARCHAR);

        tvp.addRow(expectecValue1, expectecValue2, expectecValue3);
        tvp.addRow(expectecValue1, expectecValue2, expectecValue3);
        tvp.addRow(expectecValue1, expectecValue2, expectecValue3);
        tvp.addRow(expectecValue1, expectecValue2, expectecValue3);
        tvp.addRow(expectecValue1, expectecValue2, expectecValue3);

        SQLServerCallableStatement P_C_statement = (SQLServerCallableStatement) connection.prepareCall(sql);

        P_C_statement.setStructured(1, tvpNameWithSchema, tvp);
        P_C_statement.execute();

        rs = stmt.executeQuery("select * from " + charTable);

        while (rs.next()) {
            String actualValue1 = rs.getString(1);
            String actualValue2 = rs.getString(2);
            String actualValue3 = rs.getString(3);

            assertTrue(actualValue1.trim().equals(expectecValue1),
                    "actual value does not match expected value." + "\n\tExpected value: " + expectecValue1 + "\n\tActual value: " + actualValue1);

            assertTrue(actualValue2.trim().equals(expectecValue2),
                    "actual value does not match expected value." + "\n\tExpected value: " + expectecValue2 + "\n\tActual value: " + actualValue2);

            assertTrue(actualValue3.trim().equals(expectecValue3),
                    "actual value does not match expected value." + "\n\tExpected value: " + expectecValue3 + "\n\tActual value: " + actualValue3);

        }
        terminateVariation();
    }

    /**
     * 
     * @throws SQLException
     * @throws IOException
     */
    @Test
    @DisplayName("TVPSchema_Prepared_InsertCommand")
    public void testTVPSchema_Prepared_InsertCommand() throws SQLException, IOException {
        conn = new DBConnection(connectionString);
        stmt = conn.createStatement();

        dropProcedure();
        dropTables();
        dropTVPS();

        dropAndCreateSchema();

        createTVPS();
        createTables();

        SQLServerDataTable tvp = new SQLServerDataTable();
        tvp.addColumnMetadata("PlainChar", java.sql.Types.CHAR);
        tvp.addColumnMetadata("PlainVarchar", java.sql.Types.VARCHAR);
        tvp.addColumnMetadata("PlainVarcharMax", java.sql.Types.VARCHAR);

        tvp.addRow(expectecValue1, expectecValue2, expectecValue3);
        tvp.addRow(expectecValue1, expectecValue2, expectecValue3);
        tvp.addRow(expectecValue1, expectecValue2, expectecValue3);
        tvp.addRow(expectecValue1, expectecValue2, expectecValue3);
        tvp.addRow(expectecValue1, expectecValue2, expectecValue3);

        SQLServerPreparedStatement P_C_stmt = (SQLServerPreparedStatement) connection
                .prepareStatement("INSERT INTO " + charTable + " select * from ? ;");

        P_C_stmt.setStructured(1, tvpNameWithSchema, tvp);
        P_C_stmt.executeUpdate();

        rs = stmt.executeQuery("select * from " + charTable);

        while (rs.next()) {
            String actualValue1 = rs.getString(1);
            String actualValue2 = rs.getString(2);
            String actualValue3 = rs.getString(3);

            assertTrue(actualValue1.trim().equals(expectecValue1),
                    "actual value does not match expected value." + "\n\tExpected value: " + expectecValue1 + "\n\tActual value: " + actualValue1);

            assertTrue(actualValue2.trim().equals(expectecValue2),
                    "actual value does not match expected value." + "\n\tExpected value: " + expectecValue2 + "\n\tActual value: " + actualValue2);

            assertTrue(actualValue3.trim().equals(expectecValue3),
                    "actual value does not match expected value." + "\n\tExpected value: " + expectecValue3 + "\n\tActual value: " + actualValue3);
        }
        terminateVariation();
    }

    /**
     * 
     * @throws SQLException
     * @throws IOException
     */
    @Test
    @DisplayName("TVPSchema_Callable_InsertCommand()")
    public void testTVPSchema_Callable_InsertCommand() throws SQLException, IOException {
        conn = new DBConnection(connectionString);
        stmt = conn.createStatement();

        dropProcedure();
        dropTables();
        dropTVPS();

        dropAndCreateSchema();

        createTVPS();
        createTables();

        SQLServerDataTable tvp = new SQLServerDataTable();
        tvp.addColumnMetadata("PlainChar", java.sql.Types.CHAR);
        tvp.addColumnMetadata("PlainVarchar", java.sql.Types.VARCHAR);
        tvp.addColumnMetadata("PlainVarcharMax", java.sql.Types.VARCHAR);

        tvp.addRow(expectecValue1, expectecValue2, expectecValue3);
        tvp.addRow(expectecValue1, expectecValue2, expectecValue3);
        tvp.addRow(expectecValue1, expectecValue2, expectecValue3);
        tvp.addRow(expectecValue1, expectecValue2, expectecValue3);
        tvp.addRow(expectecValue1, expectecValue2, expectecValue3);

        SQLServerCallableStatement P_C_stmt = (SQLServerCallableStatement) connection.prepareCall("INSERT INTO " + charTable + " select * from ? ;");

        P_C_stmt.setStructured(1, tvpNameWithSchema, tvp);
        P_C_stmt.executeUpdate();

        rs = stmt.executeQuery("select * from " + charTable);

        while (rs.next()) {
            String actualValue1 = rs.getString(1);
            String actualValue2 = rs.getString(2);
            String actualValue3 = rs.getString(3);

            assertTrue(actualValue1.trim().equals(expectecValue1),
                    "actual value does not match expected value." + "\n\tExpected value: " + expectecValue1 + "\n\tActual value: " + actualValue1);

            assertTrue(actualValue2.trim().equals(expectecValue2),
                    "actual value does not match expected value." + "\n\tExpected value: " + expectecValue2 + "\n\tActual value: " + actualValue2);

            assertTrue(actualValue3.trim().equals(expectecValue3),
                    "actual value does not match expected value." + "\n\tExpected value: " + expectecValue3 + "\n\tActual value: " + actualValue3);
        }
        terminateVariation();
    }

    private void dropProcedure() throws SQLException {
        String sql = " IF EXISTS (select * from sysobjects where id = object_id(N'" + procedureName + "') and OBJECTPROPERTY(id, N'IsProcedure') = 1)"
                + " DROP PROCEDURE " + procedureName;
        stmt.execute(sql);
    }

    private static void dropTables() throws SQLException {
        stmt.executeUpdate("if object_id('" + charTable + "','U') is not null" + " drop table " + charTable);
    }

    private static void dropTVPS() throws SQLException {
        stmt.executeUpdate("IF EXISTS (SELECT * FROM sys.types WHERE is_table_type = 1 AND name = '" + tvpNameWithouSchema + "') " + " drop type "
                + tvpNameWithSchema);
    }

    private static void dropAndCreateSchema() throws SQLException {
        stmt.execute("if EXISTS (SELECT * FROM sys.schemas where name = 'anotherSchma') drop schema anotherSchma");
        stmt.execute("CREATE SCHEMA anotherSchma");
    }

    private static void createPreocedure() throws SQLException {
        String sql = "CREATE PROCEDURE " + procedureName + " @InputData " + tvpNameWithSchema + " READONLY " + " AS " + " BEGIN " + " INSERT INTO "
                + charTable + " SELECT * FROM @InputData" + " END";

        stmt.execute(sql);
    }

    private void createTables() throws SQLException {
        String sql = "create table " + charTable + " (" + "PlainChar char(50) null," + "PlainVarchar varchar(50) null,"
                + "PlainVarcharMax varchar(max) null," + ");";
        stmt.execute(sql);
    }

    private void createTVPS() throws SQLException {
        String TVPCreateCmd = "CREATE TYPE " + tvpNameWithSchema + " as table ( " + "PlainChar char(50) null," + "PlainVarchar varchar(50) null,"
                + "PlainVarcharMax varchar(max) null" + ")";
        stmt.executeUpdate(TVPCreateCmd);
    }

    private void terminateVariation() throws SQLException {
        if (null != conn) {
            conn.close();
        }
        if (null != stmt) {
            stmt.close();
        }
    }

}
