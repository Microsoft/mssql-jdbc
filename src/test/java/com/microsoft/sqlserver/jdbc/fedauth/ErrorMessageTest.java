/*
 * Microsoft JDBC Driver for SQL Server Copyright(c) Microsoft Corporation All rights reserved. This program is made
 * available under the terms of the MIT License. See the LICENSE file in the project root for more information.
 */
package com.microsoft.sqlserver.jdbc.fedauth;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.microsoft.sqlserver.jdbc.TestUtils;
import com.microsoft.sqlserver.testframework.Constants;


@RunWith(JUnitPlatform.class)
@Tag(Constants.Fedauth)
public class ErrorMessageTest extends FedauthCommon {

    String userName = "abc" + azureUserName;

    @BeforeAll
    public static void setupTests() throws Throwable {
        FedauthCommon.getFedauthInfo();
    }

    @Test
    public void testWrongAccessTokenWithConnectionStringUserName() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";"
                + "HostNameInCertificate=" + hostNameInCertificate;
        try {
            Properties info = new Properties();
            info.setProperty("accesstoken", "test");

            try (Connection connection = DriverManager.getConnection(connectionUrl, info)) {}
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(e.getMessage());
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(), e.getMessage().contains(ERR_MSG_LOGIN_FAILED));
        }
    }

    @Test
    public void testWrongAccessTokenWithDatasource() throws SQLException {
        try {
            SQLServerDataSource ds = new SQLServerDataSource();
            ds.setServerName(azureServer);
            ds.setDatabaseName(azureDatabase);
            ds.setAccessToken("test");
            ds.setHostNameInCertificate(hostNameInCertificate);

            try (Connection connection = ds.getConnection()) {}
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(e.getMessage());
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(), e.getMessage().contains(ERR_MSG_LOGIN_FAILED));
        }
    }

    @Test
    public void testCorrectAccessTokenPassedInConnectionString() {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";" + "accesstoken="
                + accessToken + ";HostNameInCertificate=" + hostNameInCertificate;

        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(), e.getMessage().contains(ERR_MSG_LOGIN_FAILED));
        }
    }

    @Test
    public void testNotProvideWithConnectionStringUserName() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";" + "userName="
                + azureUserName + ";password=" + azurePassword + ";";

        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(e.getMessage());
            }

            String wrongUserName = azureUserName.split("@")[1];
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(), e.getMessage()
                    .startsWith(ERR_MSG_CANNOT_OPEN_SERVER + " \"" + wrongUserName + "\" requested by the login."));
        }
    }

    @Test
    public void testNotProvideWithDatasource() throws SQLException {
        try {
            SQLServerDataSource ds = new SQLServerDataSource();
            ds.setServerName(azureServer);
            ds.setDatabaseName(azureDatabase);
            ds.setUser(azureUserName);
            ds.setPassword(azurePassword);

            try (Connection connection = ds.getConnection()) {}
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(e.getMessage());
            }

            String wrongUserName = azureUserName.split("@")[1];
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(), e.getMessage()
                    .startsWith(ERR_MSG_CANNOT_OPEN_SERVER + " \"" + wrongUserName + "\" requested by the login."));
        }
    }

    @Test
    public void testNotProvideWithConnectionStringUser() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";" + "user="
                + azureUserName + ";password=" + azurePassword + ";";
        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(e.getMessage());
            }

            String wrongUserName = azureUserName.split("@")[1];
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(), e.getMessage()
                    .startsWith(ERR_MSG_CANNOT_OPEN_SERVER + " \"" + wrongUserName + "\" requested by the login."));
        }
    }

    @Test
    public void testSQLPasswordWithAzureDBWithConnectionStringUserName() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";" + "userName="
                + azureUserName + ";password=" + azurePassword + ";"
                + "Authentication=SqlPassword;HostNameInCertificate=" + hostNameInCertificate;
        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            String wrongUserName = azureUserName.split("@")[1];
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(), e.getMessage()
                    .startsWith(ERR_MSG_CANNOT_OPEN_SERVER + " \"" + wrongUserName + "\" requested by the login."));
        }
    }

    @Test
    public void testSQLPasswordWithAzureDBWithDatasource() throws SQLException {
        try {
            SQLServerDataSource ds = new SQLServerDataSource();
            ds.setServerName(azureServer);
            ds.setDatabaseName(azureDatabase);
            ds.setUser(azureUserName);
            ds.setPassword(azurePassword);
            ds.setAuthentication("SqlPassword");
            ds.setHostNameInCertificate(hostNameInCertificate);

            try (Connection connection = ds.getConnection()) {}
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            String wrongUserName = azureUserName.split("@")[1];
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(), e.getMessage()
                    .startsWith(ERR_MSG_CANNOT_OPEN_SERVER + " \"" + wrongUserName + "\" requested by the login."));
        }
    }

    @Test
    public void testSQLPasswordWithAzureDBWithConnectionStringUser() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";" + "user="
                + azureUserName + ";password=" + azurePassword + ";"
                + "Authentication=SqlPassword;HostNameInCertificate=" + hostNameInCertificate;
        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            String wrongUserName = azureUserName.split("@")[1];
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(), e.getMessage()
                    .startsWith(ERR_MSG_CANNOT_OPEN_SERVER + " \"" + wrongUserName + "\" requested by the login."));
        }
    }

    @Test
    public void testSQLPasswordWithUntrustedSqlDB() throws SQLException {
        try {
            java.util.Properties info = new Properties();
            info.put("Authentication", "SqlPassword");

            try (Connection connection = DriverManager.getConnection(connectionString, info)) {}
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith(ERR_MSG_SQL_AUTH_FAILED_SSL));
        }
    }

    @Test
    public void testADPasswordUnregisteredUserWithConnectionStringUserName() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";" + "userName="
                + userName + ";password=" + azurePassword + ";"
                + "Authentication=ActiveDirectoryPassword;HostNameInCertificate=" + hostNameInCertificate;

        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (SQLServerException e) {
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage()
                            .contains(ERR_MSG_FAILED_AUTHENTICATE + " the user " + userName
                                    + " in Active Directory (Authentication=ActiveDirectoryPassword).")
                            && e.getCause().getCause().getMessage().contains(ERR_MSG_SIGNIN_ADD));
        }
    }

    @Test
    public void testADPasswordUnregisteredUserWithDatasource() throws SQLException {
        try {
            SQLServerDataSource ds = new SQLServerDataSource();
            ds.setServerName(azureServer);
            ds.setDatabaseName(azureDatabase);
            ds.setUser(userName);
            ds.setPassword(azurePassword);
            ds.setAuthentication("ActiveDirectoryPassword");
            ds.setHostNameInCertificate(hostNameInCertificate);

            try (Connection connection = ds.getConnection()) {}
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (SQLServerException e) {
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage()
                            .contains(ERR_MSG_FAILED_AUTHENTICATE + " the user " + userName
                                    + " in Active Directory (Authentication=ActiveDirectoryPassword).")
                            && e.getCause().getCause().getMessage().contains(ERR_MSG_SIGNIN_ADD));
        }
    }

    @Test
    public void testADPasswordUnregisteredUserWithConnectionStringUser() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";" + "user="
                + userName + ";password=" + azurePassword + ";"
                + "Authentication=ActiveDirectoryPassword;HostNameInCertificate=" + hostNameInCertificate;
        try (Connection connection = DriverManager.getConnection(connectionUrl)) {

            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (SQLServerException e) {
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage()
                            .contains(ERR_MSG_FAILED_AUTHENTICATE + " the user " + userName
                                    + " in Active Directory (Authentication=ActiveDirectoryPassword).")
                            && e.getCause().getCause().getMessage().contains(ERR_MSG_SIGNIN_ADD));
        }
    }

    @Test
    public void testAuthenticationAgainstSQLServerWithActivedirectorypassword() throws SQLException {
        java.util.Properties info = new Properties();
        info.put("TrustServerCertificate", "true");
        info.put("Authentication", "activedirectorypassword");
        try (Connection connection = DriverManager.getConnection(connectionString, info)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(), e.getMessage().contains(ERR_MSG_LOGIN_FAILED));
        }
    }

    @Test
    public void testAuthenticationAgainstSQLServerWithActivedirectoryIntegrated() throws SQLException {
        java.util.Properties info = new Properties();
        info.put("TrustServerCertificate", "true");
        info.put("Authentication", "activedirectoryIntegrated");

        // remove the username and password property
        String newConnectionURL = TestUtils.removeProperty(TestUtils.removeProperty(connectionString, "user"),
                "password");
        try (Connection connection = DriverManager.getConnection(newConnectionURL, info)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(), e.getMessage().contains(ERR_MSG_LOGIN_FAILED));
        }
    }

    @Test
    public void testNotSpecifiedWithConnectionStringUserName() throws SQLException {
        boolean retry = true;
        int trials = 0;
        while (retry && trials < 5) {
            trials++;
            try {
                // testNotSpecified with connectionStringUserName
                String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";"
                        + "userName=" + azureUserName + ";password=" + azurePassword + ";"
                        + "Authentication=NotSpecified;";
                try (Connection connection = DriverManager.getConnection(connectionUrl)) {}
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            } catch (Exception e) {
                if (!(e instanceof SQLServerException)) {
                    fail(EXPECTED_EXCEPTION_NOT_THROWN);
                }

                if (e.getMessage().startsWith("The TCP/IP connection to the host")) {
                    System.out.println("Re-attempting connection to " + azureServer);
                    continue;
                }

                String wrongUserName = azureUserName.split("@")[1];
                assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(), e.getMessage()
                        .startsWith(ERR_MSG_CANNOT_OPEN_SERVER + " \"" + wrongUserName + "\" requested by the login."));
                retry = false;
            }
        }
    }

    @Test
    public void testNotSpecifiedWithDataSource() throws SQLException {
        try {
            SQLServerDataSource ds = new SQLServerDataSource();
            ds.setServerName(azureServer);
            ds.setDatabaseName(azureDatabase);
            ds.setUser(azureUserName);
            ds.setPassword(azurePassword);
            ds.setAuthentication("NotSpecified");

            try (Connection connection = ds.getConnection()) {}
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }

            if (e.getMessage().startsWith("The TCP/IP connection to the host")) {
                System.out.println("Re-attempting connection to " + azureServer);
            }

            String wrongUserName = azureUserName.split("@")[1];
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(), e.getMessage()
                    .startsWith(ERR_MSG_CANNOT_OPEN_SERVER + " \"" + wrongUserName + "\" requested by the login."));
        }
    }

    @Test
    public void testNotSpecifiedWithConnectionStringUser() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";" + "user="
                + azureUserName + ";password=" + azurePassword + ";" + "Authentication=NotSpecified;";
        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }

            if (e.getMessage().startsWith("The TCP/IP connection to the host")) {
                System.out.println("Re-attempting connection to " + azureServer);
            }

            String wrongUserName = azureUserName.split("@")[1];
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(), e.getMessage()
                    .startsWith(ERR_MSG_CANNOT_OPEN_SERVER + " \"" + wrongUserName + "\" requested by the login."));
        }
    }

    @Test
    public void testAccessTokenAgainstSQLServer() throws SQLException {
        java.util.Properties info = new Properties();
        info.put("accesstoken", accessToken);
        info.put("TrustServerCertificate", "true");
        String newConnectionURL = TestUtils.removeProperty(TestUtils.removeProperty(connectionString, "user"),
                "password");
        try (Connection connection = DriverManager.getConnection(newConnectionURL, info)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(), e.getMessage().contains(ERR_MSG_LOGIN_FAILED));
        }
    }

    @Test
    public void testADPasswordWrongPasswordWithConnectionStringUserName() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";" + "userName="
                + azureUserName + ";password=WrongPassword;"
                + "Authentication=ActiveDirectoryPassword;HostNameInCertificate=" + hostNameInCertificate;

        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }

            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(), e.getMessage()
                    .contains(ERR_MSG_FAILED_AUTHENTICATE + " the user " + azureUserName
                            + " in Active Directory (Authentication=ActiveDirectoryPassword).")
                    && (e.getCause().getCause().getMessage().toLowerCase().contains("invalid username or password")
                            || e.getCause().getCause().getMessage().contains(ERR_MSG_SIGNIN_TOO_MANY)));
        }
    }

    @Test
    public void testADPasswordWrongPasswordWithDatasource() throws SQLException {
        try {
            SQLServerDataSource ds = new SQLServerDataSource();
            ds.setServerName(azureServer);
            ds.setDatabaseName(azureDatabase);
            ds.setUser(azureUserName);
            ds.setPassword("WrongPassword");
            ds.setAuthentication("ActiveDirectoryPassword");
            ds.setHostNameInCertificate(hostNameInCertificate);

            try (Connection connection = ds.getConnection()) {}
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }

            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(), e.getMessage()
                    .contains(ERR_MSG_FAILED_AUTHENTICATE + " the user " + azureUserName
                            + " in Active Directory (Authentication=ActiveDirectoryPassword).")
                    && (e.getCause().getCause().getMessage().toLowerCase().contains("invalid username or password")
                            || e.getCause().getCause().getMessage().contains(ERR_MSG_SIGNIN_TOO_MANY)));
        }
    }

    @Test
    public void testADPasswordWrongPasswordWithConnectionStringUser() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";" + "user="
                + azureUserName + ";password=WrongPassword;"
                + "Authentication=ActiveDirectoryPassword;HostNameInCertificate=" + hostNameInCertificate;
        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }

            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(), e.getMessage()
                    .contains(ERR_MSG_FAILED_AUTHENTICATE + " the user " + azureUserName
                            + " in Active Directory (Authentication=ActiveDirectoryPassword).")
                    && (e.getCause().getCause().getMessage().toLowerCase().contains("invalid username or password")
                            || e.getCause().getCause().getMessage().contains(ERR_MSG_SIGNIN_TOO_MANY)));
        }
    }

    @Test
    public void testSetAuthenticationWithIntegratedSecurityTrueWithDatasource() throws SQLException {
        try {
            SQLServerDataSource ds = new SQLServerDataSource();
            ds.setServerName(azureServer);
            ds.setDatabaseName(azureDatabase);
            ds.setUser(azureUserName);
            ds.setPassword(azurePassword);
            ds.setAuthentication("ActiveDirectoryPassword");
            ds.setHostNameInCertificate(hostNameInCertificate);
            ds.setIntegratedSecurity(true);

            try (Connection connection = ds.getConnection()) {}
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith(ERR_MSG_NOT_AUTH_AND_IS));
        }
    }

    @Test
    public void testSetAuthenticationWithIntegratedSecurityTrueWithConnectionStringUserName() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";" + "userName="
                + azureUserName + ";password=" + azurePassword + ";"
                + "Authentication=ActiveDirectoryPassword;HostNameInCertificate=" + hostNameInCertificate
                + ";IntegratedSecurity=true;";
        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith(ERR_MSG_NOT_AUTH_AND_IS));
        }
    }

    @Test
    public void testSetAuthenticationWithIntegratedSecurityTrueWithConnectionStringUser() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";" + "user="
                + azureUserName + ";password=" + azurePassword + ";"
                + "Authentication=ActiveDirectoryPassword;HostNameInCertificate=" + hostNameInCertificate
                + ";IntegratedSecurity=true;";
        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith(ERR_MSG_NOT_AUTH_AND_IS));
        }
    }

    @Test
    public void testADIntegratedWithUserAndPasswordWithDataSource() throws SQLException {
        try {
            SQLServerDataSource ds = new SQLServerDataSource();
            ds.setServerName(azureServer);
            ds.setDatabaseName(azureDatabase);
            ds.setUser(azureUserName);
            ds.setPassword(azurePassword);
            ds.setAuthentication("ActiveDirectoryIntegrated");
            ds.setHostNameInCertificate(hostNameInCertificate);
            try (Connection connection = ds.getConnection()) {}
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith(ERR_MSG_NOT_AUTH_AND_USER_PASSWORD));
        }
    }

    @Test
    public void testADIntegratedWithUserAndPasswordWithConnectionStringUserName() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";" + "userName="
                + azureUserName + ";password=" + azurePassword + ";"
                + "Authentication=ActiveDirectoryIntegrated;HostNameInCertificate=" + hostNameInCertificate;
        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith(ERR_MSG_NOT_AUTH_AND_USER_PASSWORD));
        }
    }

    @Test
    public void testADIntegratedWithUserAndPasswordWithConnectionStringUser() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";" + "user="
                + azureUserName + ";password=" + azurePassword + ";"
                + "Authentication=ActiveDirectoryIntegrated;HostNameInCertificate=" + hostNameInCertificate;
        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith(ERR_MSG_NOT_AUTH_AND_USER_PASSWORD));
        }
    }

    @Test
    public void testSetBothAccessTokenAndAuthentication() throws SQLException {
        try {
            SQLServerDataSource ds = new SQLServerDataSource();
            ds.setServerName(azureServer);
            ds.setDatabaseName(azureDatabase);
            ds.setAuthentication("ActiveDirectoryIntegrated");
            ds.setAccessToken(accessToken);
            ds.setHostNameInCertificate(hostNameInCertificate);

            try (Connection connection = ds.getConnection()) {}
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith(ERR_MSG_CANNOT_SET_ACCESS_TOKEN));
        }
    }

    @Test
    public void testAccessTokenWithIntegratedSecurityTrue() throws SQLException {
        try {
            SQLServerDataSource ds = new SQLServerDataSource();
            ds.setServerName(azureServer);
            ds.setDatabaseName(azureDatabase);
            ds.setIntegratedSecurity(true);
            ds.setAccessToken(accessToken);
            ds.setHostNameInCertificate(hostNameInCertificate);

            try (Connection connection = ds.getConnection()) {}
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith(ERR_MSG_CANNOT_SET_ACCESS_TOKEN));
        }
    }

    @Test
    public void testAccessTokenWithUserAndPasswordWithDatasource() throws SQLException {
        try {
            SQLServerDataSource ds = new SQLServerDataSource();

            ds.setServerName(azureServer);
            ds.setDatabaseName(azureDatabase);
            ds.setAccessToken(accessToken);
            ds.setUser(azureUserName);
            ds.setPassword(azurePassword);
            ds.setHostNameInCertificate(hostNameInCertificate);

            try (Connection connection = ds.getConnection()) {}
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith(ERR_MSG_CANNOT_SET_ACCESS_TOKEN));
        }
    }

    @Test
    public void testAccessTokenWithUserAndPasswordWithConnectionStringUserName() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";" + "userName="
                + azureUserName + ";password=" + azurePassword + ";" + "HostNameInCertificate=" + hostNameInCertificate;
        Properties info = new Properties();
        info.setProperty("accesstoken", accessToken);

        try (Connection connection = DriverManager.getConnection(connectionUrl, info)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith(ERR_MSG_CANNOT_SET_ACCESS_TOKEN));
        }
    }

    @Test
    public void testAccessTokenWithUserAndPasswordWithConnectionStringUser() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";" + "user="
                + azureUserName + ";password=" + azurePassword + ";" + "HostNameInCertificate=" + hostNameInCertificate;

        Properties info = new Properties();
        info.setProperty("accesstoken", accessToken);

        try (Connection connection = DriverManager.getConnection(connectionUrl, info)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith(ERR_MSG_CANNOT_SET_ACCESS_TOKEN));
        }
    }

    @Test
    public void testAccessTokenEmpty() throws SQLException {
        try {
            String accessToken = "";
            SQLServerDataSource ds = new SQLServerDataSource();

            ds.setServerName(azureServer);
            ds.setDatabaseName(azureDatabase);
            ds.setAccessToken(accessToken);
            ds.setHostNameInCertificate(hostNameInCertificate);

            try (Connection connection = ds.getConnection()) {}
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().contains(ERR_MSG_ACCESS_TOKEN_EMPTY));
        }
    }

    @Test
    public void testADPasswordWithoutUser() throws SQLException {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setServerName(azureServer);
        ds.setDatabaseName(azureDatabase);
        ds.setPassword(azurePassword);
        ds.setAuthentication("ActiveDirectoryPassword");
        ds.setHostNameInCertificate(hostNameInCertificate);

        try {
            try (Connection connection = ds.getConnection()) {}
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith(ERR_MSG_BOTH_USERNAME_PASSWORD));
        }
    }

    @Test
    public void testADPasswordWithoutPasswordWithDatasource() throws SQLException {
        try {
            // testADPasswordWithoutPassword with dataSource
            SQLServerDataSource ds = new SQLServerDataSource();

            ds.setServerName(azureServer);
            ds.setDatabaseName(azureDatabase);
            ds.setUser(azureUserName);
            ds.setAuthentication("ActiveDirectoryPassword");
            ds.setHostNameInCertificate(hostNameInCertificate);
            try (Connection connection = ds.getConnection()) {}
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith(ERR_MSG_BOTH_USERNAME_PASSWORD));
        }
    }

    @Test
    public void testADPasswordWithoutPasswordWithConnectionStringUserName() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";" + "userName="
                + azureUserName + ";" + "Authentication=ActiveDirectoryPassword;HostNameInCertificate="
                + hostNameInCertificate;
        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith(ERR_MSG_BOTH_USERNAME_PASSWORD));
        }
    }

    @Test
    public void testADPasswordWithoutPasswordWithConnectionStringUser() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";" + "user="
                + azureUserName + ";" + "Authentication=ActiveDirectoryPassword;HostNameInCertificate="
                + hostNameInCertificate;
        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith(ERR_MSG_BOTH_USERNAME_PASSWORD));
        }
    }

    @Test
    public void testSqlPasswordWithoutUser() throws SQLException {
        try {
            SQLServerDataSource ds = new SQLServerDataSource();
            ds.setServerName(azureServer);
            ds.setDatabaseName(azureDatabase);
            ds.setPassword(azurePassword);
            ds.setAuthentication("SqlPassword");
            ds.setHostNameInCertificate(hostNameInCertificate);

            try (Connection connection = ds.getConnection()) {}
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith(ERR_MSG_BOTH_USERNAME_PASSWORD));
        }
    }

    @Test
    public void testSqlPasswordWithoutPasswordWithDatasource() throws SQLException {
        try {
            SQLServerDataSource ds = new SQLServerDataSource();
            ds.setServerName(azureServer);
            ds.setDatabaseName(azureDatabase);
            ds.setUser(azureUserName);
            ds.setAuthentication("SqlPassword");
            ds.setHostNameInCertificate(hostNameInCertificate);

            try (Connection connection = ds.getConnection()) {}
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith(ERR_MSG_BOTH_USERNAME_PASSWORD));
        }
    }

    @Test
    public void testSqlPasswordWithoutPasswordWithConnectionStringUserName() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";" + "userName="
                + azureUserName + ";" + "Authentication=SqlPassword;HostNameInCertificate=" + hostNameInCertificate;

        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith(ERR_MSG_BOTH_USERNAME_PASSWORD));
        }
    }

    @Test
    public void testSqlPasswordWithoutPasswordWithConnectionStringUser() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://" + azureServer + ";database=" + azureDatabase + ";" + "user="
                + azureUserName + ";" + "Authentication=SqlPassword;HostNameInCertificate=" + hostNameInCertificate;

        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith(ERR_MSG_BOTH_USERNAME_PASSWORD));
        }
    }

    @Test
    public void testInvalidAuthentication() throws SQLException {
        try {
            SQLServerDataSource ds = new SQLServerDataSource();
            ds.setServerName(azureServer);
            ds.setDatabaseName(azureDatabase);
            ds.setUser(azureUserName);
            ds.setPassword(azurePassword);
            ds.setAuthentication("ActiveDirectoryPass");
            ds.setHostNameInCertificate(hostNameInCertificate);

            try (Connection connection = ds.getConnection()) {}
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            if (!(e instanceof SQLServerException)) {
                fail(EXPECTED_EXCEPTION_NOT_THROWN);
            }
            assertTrue(INVALID_EXCEPION_MSG + ": " + e.getMessage(),
                    e.getMessage().startsWith("The authentication value") && e.getMessage().endsWith("is not valid."));
        }
    }
}
