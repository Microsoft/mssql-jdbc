package com.microsoft.sqlserver.jdbc.AlwaysEncrypted;

import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.microsoft.sqlserver.jdbc.SQLServerColumnEncryptionAzureKeyVaultProvider;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerPreparedStatement;
import com.microsoft.sqlserver.jdbc.SQLServerResultSet;
import com.microsoft.sqlserver.jdbc.SQLServerStatement;
import com.microsoft.sqlserver.jdbc.TestResource;
import com.microsoft.sqlserver.jdbc.TestUtils;
import com.microsoft.sqlserver.testframework.AbstractTest;
import com.microsoft.sqlserver.testframework.Constants;
import com.microsoft.sqlserver.testframework.PrepUtil;


/**
 * Tests involving MSI authentication
 */
@RunWith(JUnitPlatform.class)
@Tag(Constants.MSI)
public class MSITest extends AESetup {
    /*
     * Test basic MSI auth
     */
    @Test
    public void testAuth() throws SQLException {
        try (SQLServerConnection con = PrepUtil.getConnection(connectionString)) {} catch (Exception e) {
            fail(TestResource.getResource("R_loginFailed") + e.getMessage());
        }
    }

    /*
     * Test MSI auth using datasource
     */
    @Test
    public void testDSAuth() throws SQLException {
        SQLServerDataSource ds = new SQLServerDataSource();
        AbstractTest.updateDataSource(connectionString, ds);

        try (Connection con = ds.getConnection(); Statement stmt = con.createStatement()) {} catch (Exception e) {
            fail(TestResource.getResource("R_loginFailed") + e.getMessage());
        }
    }

    /*
     * Test AKV with with credentials
     */
    @Test
    public void testCharAkvWithCred() throws SQLException {
        if (null != keyVaultProviderClientId && null != keyVaultProviderClientKey) {
            akvProvider = new SQLServerColumnEncryptionAzureKeyVaultProvider(keyVaultProviderClientId,
                    keyVaultProviderClientKey);
            map.put(Constants.AZURE_KEY_VAULT_NAME, akvProvider);
            SQLServerConnection.registerColumnEncryptionKeyStoreProviders(map);
        }

        testCharAkv();
    }

    /*
     * Test AKV with MSI
     */
    public void testCharAkvWithMSI() throws SQLException {
        akvProvider = new SQLServerColumnEncryptionAzureKeyVaultProvider();
        map.put(Constants.AZURE_KEY_VAULT_NAME, akvProvider);
        SQLServerConnection.registerColumnEncryptionKeyStoreProviders(map);

        testCharAkv();
    }

    private void testCharAkv() throws SQLException {
        String sql = "select * from " + CHAR_TABLE_AE;
        try (SQLServerConnection con = PrepUtil.getConnection(AETestConnectionString);
                SQLServerStatement stmt = (SQLServerStatement) con.createStatement();
                SQLServerPreparedStatement pstmt = (SQLServerPreparedStatement) TestUtils.getPreparedStmt(con, sql,
                        stmtColEncSetting)) {
            TestUtils.dropTableIfExists(CHAR_TABLE_AE, stmt);
            createTable(CHAR_TABLE_AE, cekAkv, charTable);
            String[] values = createCharValues(false);
            populateCharNormalCase(values);

            try (ResultSet rs = (stmt == null) ? pstmt.executeQuery() : stmt.executeQuery(sql)) {
                int numberOfColumns = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    AECommon.testGetString(rs, numberOfColumns, values);
                    AECommon.testGetObject(rs, numberOfColumns, values);
                }
            }
        }

    }

    /*
     * Test AKV with with credentials
     */
    @Test
    public void testNumericAkvWithCred() throws SQLException {
        if (null != keyVaultProviderClientId && null != keyVaultProviderClientKey) {
            akvProvider = new SQLServerColumnEncryptionAzureKeyVaultProvider(keyVaultProviderClientId,
                    keyVaultProviderClientKey);
            map.put(Constants.AZURE_KEY_VAULT_NAME, akvProvider);
            SQLServerConnection.registerColumnEncryptionKeyStoreProviders(map);
        }

        testNumericAKV();
    }

    /*
     * Test AKV with MSI
     */
    public void testNumericAkvWithMSI() throws SQLException {
        akvProvider = new SQLServerColumnEncryptionAzureKeyVaultProvider();
        map.put(Constants.AZURE_KEY_VAULT_NAME, akvProvider);
        SQLServerConnection.registerColumnEncryptionKeyStoreProviders(map);

        testNumericAKV();
    }

    private void testNumericAKV() throws SQLException {
        String sql = "select * from " + NUMERIC_TABLE_AE;
        try (SQLServerConnection con = PrepUtil.getConnection(AETestConnectionString);
                SQLServerStatement stmt = (SQLServerStatement) con.createStatement();
                SQLServerPreparedStatement pstmt = (SQLServerPreparedStatement) TestUtils.getPreparedStmt(con, sql,
                        stmtColEncSetting)) {
            TestUtils.dropTableIfExists(NUMERIC_TABLE_AE, stmt);
            createTable(NUMERIC_TABLE_AE, cekAkv, numericTable);
            String[] values = createNumericValues(false);
            populateNumeric(values);

            try (SQLServerResultSet rs = (stmt == null) ? (SQLServerResultSet) pstmt.executeQuery()
                                                        : (SQLServerResultSet) stmt.executeQuery(sql)) {
                int numberOfColumns = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    AECommon.testGetString(rs, numberOfColumns, values);
                    AECommon.testGetObject(rs, numberOfColumns, values);
                    AECommon.testGetBigDecimal(rs, numberOfColumns, values);
                    AECommon.testWithSpecifiedtype(rs, numberOfColumns, values);
                }
            } catch (Exception e) {
                fail(TestResource.getResource("R_loginFailed") + e.getMessage());
            }
        }
    }
}
