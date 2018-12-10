/*
 * Microsoft JDBC Driver for SQL Server Copyright(c) Microsoft Corporation All rights reserved. This program is made
 * available under the terms of the MIT License. See the LICENSE file in the project root for more information.
 */
package com.microsoft.sqlserver.jdbc.osgi;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Dictionary;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.jdbc.DataSourceFactory;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import com.microsoft.sqlserver.testframework.AbstractTest;

import org.eclipse.gemini.blueprint.mock.MockBundleContext;
import org.eclipse.gemini.blueprint.mock.MockServiceRegistration;


@RunWith(JUnitPlatform.class)
public class DataFactoryTest extends AbstractTest {

    @Test
    public void testDataFactory() throws SQLException {
        DataSourceFactory dsFactory = new SQLServerDataSourceFactory();
        Properties props = new Properties();
        props.setProperty(DataSourceFactory.JDBC_URL, connectionString);
        DataSource ds = dsFactory.createDataSource(props);
        try (Connection c = ds.getConnection(); Statement s = c.createStatement()) {
            try (ResultSet rs = s.executeQuery("SELECT 1")) {
                assertTrue("Resultset is empty.", rs.next());
            }
        }
    }

    @Test
    public void testActivator() throws Exception {
        BundleContext bc = new MockBundleContext() {
            private ServiceReference<?> sr;

            @Override
            public ServiceReference<?> getServiceReference(String clazz) {
                return sr;
            }

            @Override
            public ServiceRegistration<?> registerService(String[] clazzes, Object service, Dictionary properties) {
                MockServiceRegistration reg = new MockServiceRegistration(properties);
                sr = reg.getReference();
                return reg;
            }
        };
        Activator a = new com.microsoft.sqlserver.jdbc.osgi.Activator();
        a.start(bc);
        
        ServiceReference<DataSourceFactory> sr = bc.getServiceReference(DataSourceFactory.class);
        String[] propertyKeys = sr.getPropertyKeys();
        boolean correctClass = false;
        boolean correctName = false;
        boolean correctVersion = false;
        SQLServerDriver s = new SQLServerDriver();
        
        for (String key : propertyKeys) {
            if (key.equals(DataSourceFactory.OSGI_JDBC_DRIVER_CLASS)) {
                String bundleClassName = (String) sr.getProperty(key);
                String actualClassName = s.getClass().getName();
                assertTrue("Driver class name mismatch. Expected: " + bundleClassName + ", Actual: " + actualClassName
                        + ".", bundleClassName.equals(actualClassName));
                correctClass = true;
            } else if (key.equals(DataSourceFactory.OSGI_JDBC_DRIVER_NAME)) {
                String bundleDriverName = (String) sr.getProperty(key);
                String actualDriverName = "Microsoft JDBC Driver for SQL Server";
                assertTrue("Driver name mismatch. Expected: " + bundleDriverName + ", Actual: " + actualDriverName
                        + ".", bundleDriverName.equals(actualDriverName));
                correctName = true;
            } else if (key.equals(DataSourceFactory.OSGI_JDBC_DRIVER_VERSION)) {
                String bundleDriverVer = (String) sr.getProperty(key);
                String actualDriverVer = s.getMajorVersion() + "." + s.getMinorVersion();
                assertTrue("Driver version mismatch. Expected: " + bundleDriverVer + ", Actual: " + actualDriverVer
                        + ".", bundleDriverVer.equals(actualDriverVer));
                correctVersion = true;
            }
        }
        assertTrue("Not all properties were checked.", correctClass && correctName && correctVersion);
        a.stop(bc);
    }
}
