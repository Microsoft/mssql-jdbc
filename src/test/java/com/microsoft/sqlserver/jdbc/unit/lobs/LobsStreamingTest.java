package com.microsoft.sqlserver.jdbc.unit.lobs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.microsoft.sqlserver.jdbc.RandomUtil;
import com.microsoft.sqlserver.jdbc.TestUtils;
import com.microsoft.sqlserver.testframework.AbstractTest;


@RunWith(JUnitPlatform.class)
public class LobsStreamingTest extends AbstractTest {

    private static final int LOB_ARRAY_SIZE = 500; // number of rows to insert into the table and compare
    private static final int LOB_LENGTH_MIN = 8000;
    private static final int LOB_LENGTH_MAX = 32000;
    private static final String ASCII_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()-=_+,./;'[]<>?:{}|`~\"\\";
    private static final String UNICODE_CHARACTERS = ASCII_CHARACTERS
            + "Ǥ⚌c♮ƺåYèĢù⚏Ȓ★ǌäõpƸŃōoƝĤßuÙőƆE♹gǇÜŬȺǱ!Û☵ŦãǁĸNQŰǚǻTÖC]ǶýåÉbɉ☩=\\ȍáźŗǃĻýű☓☄¸T☑ö^k☏I:x☑⚀läiȉ☱☚⚅ǸǎãÂ";
    private static String tableName = null;

    private static enum LoB {
        CLOB,
        NCLOB
    };

    @BeforeEach
    public void init() throws SQLException {
        tableName = RandomUtil.getIdentifier("streamingTest");
    }

    private String getRandomString(int length, String validCharacters) {
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * validCharacters.length());
            salt.append(validCharacters.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    // closing the scanner closes the Inputstream, and the driver needs the stream to fill LoBs
    @SuppressWarnings("resource")
    private String getStringFromInputStream(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is, java.nio.charset.StandardCharsets.US_ASCII).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private String getStringFromReader(Reader r, long l) throws IOException {
        // read the Reader contents into a buffer and return the complete string
        final StringBuilder stringBuilder = new StringBuilder((int) l);
        char[] buffer = new char[(int) l];
        while (true) {
            int amountRead = r.read(buffer, 0, (int) l);
            if (amountRead == -1) {
                break;
            }
            stringBuilder.append(buffer, 0, amountRead);
        }
        return stringBuilder.toString();
    }

    private void createLobTable(Statement stmt, String table, LoB l) throws SQLException {
        String columnType = (l == LoB.CLOB) ? "varchar(max)" : "nvarchar(max)";
        stmt.execute("CREATE TABLE [" + table + "] (id int, lobValue " + columnType + ")");
    }

    private ArrayList<String> createRandomStringArray(LoB l) {
        String characterPool = (l == LoB.CLOB) ? ASCII_CHARACTERS : UNICODE_CHARACTERS;
        ArrayList<String> string_array = new ArrayList<>();
        IntStream.range(0, LOB_ARRAY_SIZE).forEach(i -> string_array.add(
                getRandomString(ThreadLocalRandom.current().nextInt(LOB_LENGTH_MIN, LOB_LENGTH_MAX), characterPool)));
        return string_array;
    }

    private void insertData(Connection conn, String table, ArrayList<String> lobs) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO [" + table + "] VALUES(?,?)")) {
            for (int i = 0; i < lobs.size(); i++) {
                Clob c = conn.createClob();
                c.setString(1, lobs.get(i));
                pstmt.setInt(1, i);
                pstmt.setClob(2, c);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    @Test
    @DisplayName("testLengthAfterStream")
    public void testLengthAfterStream() throws SQLException, IOException {
        try (Connection conn = DriverManager.getConnection(connectionString);) {
            try (Statement stmt = conn.createStatement()) {
                TestUtils.dropTableIfExists(tableName, stmt);
                createLobTable(stmt, tableName, LoB.CLOB);
                ArrayList<String> lob_data = createRandomStringArray(LoB.CLOB);
                insertData(conn, tableName, lob_data);

                ResultSet rs = stmt.executeQuery("SELECT * FROM [" + tableName + "] ORDER BY id ASC");
                while (rs.next()) {
                    Clob c = rs.getClob(2);
                    Reader r = c.getCharacterStream();
                    long clobLength = c.length();
                    String recieved = getStringFromReader(r, clobLength);// streaming string
                    assertEquals(lob_data.get(rs.getInt(1)), recieved);// compare streamed string to initial string
                }
            } finally {
                try (Statement stmt = conn.createStatement()) {
                    TestUtils.dropTableIfExists(tableName, stmt);
                }
            }
        }
    }

    @Test
    @DisplayName("testClobsVarcharASCII")
    public void testClobsVarcharASCII() throws SQLException {
        try (Connection conn = DriverManager.getConnection(connectionString)) {
            try (Statement stmt = conn.createStatement()) {
                TestUtils.dropTableIfExists(tableName, stmt);
                createLobTable(stmt, tableName, LoB.CLOB);
                ArrayList<String> lob_data = createRandomStringArray(LoB.CLOB);
                insertData(conn, tableName, lob_data);

                ArrayList<Clob> lobsFromServer = new ArrayList<>();
                ResultSet rs = stmt.executeQuery("SELECT * FROM [" + tableName + "] ORDER BY id ASC");
                while (rs.next()) {
                    int index = rs.getInt(1);
                    Clob c = rs.getClob(2);
                    assertEquals(c.length(), lob_data.get(index).length());
                    lobsFromServer.add(c);
                    String recieved = getStringFromInputStream(c.getAsciiStream());// streaming string
                    assertEquals(lob_data.get(index), recieved);// compare streamed string to initial string
                }
                rs.close();
                for (int i = 0; i < lob_data.size(); i++) {
                    String recieved = getStringFromInputStream(lobsFromServer.get(i).getAsciiStream());// non-streaming
                                                                                                       // string
                    assertEquals(recieved, lob_data.get(i));// compare static string to streamed string
                }
            } finally {
                try (Statement stmt = conn.createStatement()) {
                    TestUtils.dropTableIfExists(tableName, stmt);
                }
            }
        }
    }

    @Test
    @DisplayName("testNClobsNVarcharASCII")
    public void testNClobsVarcharASCII() throws SQLException, IOException {
        try (Connection conn = DriverManager.getConnection(connectionString)) {
            try (Statement stmt = conn.createStatement()) {
                TestUtils.dropTableIfExists(tableName, stmt);
                createLobTable(stmt, tableName, LoB.NCLOB);
                // Testing AsciiStream, use Clob string set or characters will be converted to '?'
                ArrayList<String> lob_data = createRandomStringArray(LoB.CLOB);
                insertData(conn, tableName, lob_data);

                ArrayList<NClob> lobsFromServer = new ArrayList<>();
                ResultSet rs = stmt.executeQuery("SELECT * FROM [" + tableName + "] ORDER BY id ASC");
                while (rs.next()) {
                    int index = rs.getInt(1);
                    NClob c = rs.getNClob(2);
                    assertEquals(c.length(), lob_data.get(index).length());
                    lobsFromServer.add(c);
                    String recieved = getStringFromInputStream(c.getAsciiStream());// NClob AsciiStream is never
                                                                                   // streamed
                    assertEquals(lob_data.get(index), recieved);// compare string to initial string
                }
            } finally {
                try (Statement stmt = conn.createStatement()) {
                    TestUtils.dropTableIfExists(tableName, stmt);
                }
            }
        }
    }

    @Test
    @DisplayName("testClobsVarcharCHARA")
    public void testClobsVarcharCHARA() throws SQLException, IOException {
        try (Connection conn = DriverManager.getConnection(connectionString)) {
            try (Statement stmt = conn.createStatement()) {
                TestUtils.dropTableIfExists(tableName, stmt);
                createLobTable(stmt, tableName, LoB.CLOB);
                ArrayList<String> lob_data = createRandomStringArray(LoB.CLOB);
                insertData(conn, tableName, lob_data);

                ArrayList<Clob> lobsFromServer = new ArrayList<>();
                ResultSet rs = stmt.executeQuery("SELECT * FROM [" + tableName + "] ORDER BY id ASC");
                while (rs.next()) {
                    int index = rs.getInt(1);
                    Clob c = rs.getClob(2);
                    assertEquals(c.length(), lob_data.get(index).length());
                    lobsFromServer.add(c);
                    String recieved = getStringFromReader(c.getCharacterStream(), c.length());// streaming string
                    assertEquals(lob_data.get(index), recieved);// compare streamed string to initial string
                }
                rs.close();
                for (int i = 0; i < lob_data.size(); i++) {
                    String recieved = getStringFromReader(lobsFromServer.get(i).getCharacterStream(),
                            lobsFromServer.get(i).length());// non-streaming string
                    assertEquals(recieved, lob_data.get(i));// compare static string to streamed string
                }
            } finally {
                try (Statement stmt = conn.createStatement()) {
                    TestUtils.dropTableIfExists(tableName, stmt);
                }
            }
        }
    }

    @Test
    @DisplayName("testNClobsVarcharCHARA")
    public void testNClobsVarcharCHARA() throws SQLException, IOException {
        try (Connection conn = DriverManager.getConnection(connectionString)) {
            try (Statement stmt = conn.createStatement()) {
                TestUtils.dropTableIfExists(tableName, stmt);
                createLobTable(stmt, tableName, LoB.NCLOB);
                ArrayList<String> lob_data = createRandomStringArray(LoB.NCLOB);
                insertData(conn, tableName, lob_data);

                ArrayList<NClob> lobsFromServer = new ArrayList<>();
                ResultSet rs = stmt.executeQuery("SELECT * FROM [" + tableName + "] ORDER BY id ASC");
                while (rs.next()) {
                    int index = rs.getInt(1);
                    NClob c = rs.getNClob(2);
                    assertEquals(c.length(), lob_data.get(index).length());
                    lobsFromServer.add(c);
                    String recieved = getStringFromReader(c.getCharacterStream(), c.length());// streaming string
                    assertEquals(lob_data.get(index), recieved);// compare streamed string to initial string
                }
                rs.close();
                for (int i = 0; i < lob_data.size(); i++) {
                    String recieved = getStringFromReader(lobsFromServer.get(i).getCharacterStream(),
                            lobsFromServer.get(i).length());// non-streaming string
                    assertEquals(recieved, lob_data.get(i));// compare static string to streamed string
                }
            } finally {
                try (Statement stmt = conn.createStatement()) {
                    TestUtils.dropTableIfExists(tableName, stmt);
                }
            }
        }
    }
}
