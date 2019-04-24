package com.microsoft.sqlserver.jdbc.fmtOnly;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.microsoft.sqlserver.jdbc.SQLServerPreparedStatement;
import com.microsoft.sqlserver.jdbc.TestUtils;
import com.microsoft.sqlserver.testframework.AbstractTest;


public class APITest extends AbstractTest {

    private static final String tableName = "FMT_API_Test_" + UUID.randomUUID();

    @BeforeAll
    private static void setupTest() throws SQLException {
        try (Statement s = AbstractTest.connection.createStatement()) {
            s.execute("CREATE TABLE [" + tableName
                    + "] (c1 int identity, c2 float, c3 real, c4 bigint, c5 nvarchar(4000))");
        }
    }

    @AfterAll
    private static void cleanupTest() throws SQLException {
        try (Statement s = AbstractTest.connection.createStatement()) {
            TestUtils.dropTableIfExists(tableName, s);
        }
    }

    @Test
    public void publicAPITest() throws SQLException {
        String sql = "INSERT INTO [" + tableName + "] VALUES(?,?,?,?)";
                
        ds.setUseFmtOnly(true);
        try (Connection cStringConnection = DriverManager.getConnection(connectionString + "useFMTOnly=true;");
                Connection statementConnection = DriverManager.getConnection(connectionString);
                Connection dsConnection = ((DataSource) ds).getConnection()) {
            try (PreparedStatement cStringPstmt = cStringConnection.prepareStatement(sql);
                    PreparedStatement statementPstmt = statementConnection.prepareStatement(sql);
                    PreparedStatement dsPstmt = dsConnection.prepareStatement(sql)) {
                ((SQLServerPreparedStatement) statementPstmt).setUseFmtOnly(true);
                ParameterMetaData cStringMD = cStringPstmt.getParameterMetaData();
                ParameterMetaData statementMD = statementPstmt.getParameterMetaData();
                ParameterMetaData dsMD = dsPstmt.getParameterMetaData();
                compare(cStringMD.getParameterCount(),statementMD.getParameterCount(),dsMD.getParameterCount());
                for (int i = 1; i <= cStringMD.getParameterCount();i++) {
                    compare(cStringMD.getParameterClassName(i),statementMD.getParameterClassName(i),dsMD.getParameterClassName(i));
                    compare(cStringMD.getParameterMode(i),statementMD.getParameterMode(i),dsMD.getParameterMode(i));
                    compare(cStringMD.getParameterType(i),statementMD.getParameterType(i),dsMD.getParameterType(i));
                    compare(cStringMD.getParameterTypeName(i),statementMD.getParameterTypeName(i),dsMD.getParameterTypeName(i));
                    compare(cStringMD.getPrecision(i),statementMD.getPrecision(i),dsMD.getPrecision(i));
                    compare(cStringMD.getScale(i),statementMD.getScale(i),dsMD.getScale(i));
                }
            }
        }
    }
    
    private void compare(Object a, Object b, Object c) {
        assertEquals(a,b);
        assertEquals(b,c);
    }
}
