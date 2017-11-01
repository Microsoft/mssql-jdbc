/*
 * Microsoft JDBC Driver for SQL Server
 * 
 * Copyright(c) Microsoft Corporation All rights reserved.
 * 
 * This program is made available under the terms of the MIT License. See the LICENSE file in the project root for more information.
 */

package com.microsoft.sqlserver.jdbc;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

public final class SQLServerDataTable {

    int rowCount = 0;
    int columnCount = 0;
    Map<Integer, SQLServerDataColumn> columnMetadata = null;
    Set<String> columnNames = null;
    Map<Integer, Object[]> rows = null;

    private String tvpName = null;

    /**
     * The constant in the Java programming language, sometimes referred to as a type code, that identifies the type TVP.
     * 
     * @throws SQLServerException
     *             when an error occurs
     */
    // Name used in CREATE TYPE
    public SQLServerDataTable() throws SQLServerException {
        columnMetadata = new LinkedHashMap<>();
        columnNames = new HashSet<>();
        rows = new HashMap<>();
    }

    /**
     * Clears this data table.
     */
    public synchronized void clear() {
        rowCount = 0;
        columnCount = 0;
        columnMetadata.clear();
        rows.clear();
    }

    /**
     * Retrieves an iterator on the rows of the data table.
     * 
     * @return an iterator on the rows of the data table.
     */
    public synchronized Iterator<Entry<Integer, Object[]>> getIterator() {
        if ((null != rows) && (null != rows.entrySet())) {
            return rows.entrySet().iterator();
        }
        return null;
    }

    /**
     * Adds meta data for the specified column
     * 
     * @param columnName
     *            the name of the column
     * @param sqlType
     *            the sql type of the column
     * @throws SQLServerException
     *             when an error occurs
     */
    public synchronized void addColumnMetadata(String columnName,
            int sqlType) throws SQLServerException {
        // column names must be unique
        Util.checkDuplicateColumnName(columnName, columnNames);
        columnMetadata.put(columnCount++, new SQLServerDataColumn(columnName, sqlType));
    }

    /**
     * Adds meta data for the specified column
     * 
     * @param column
     *            the name of the column
     * @throws SQLServerException
     *             when an error occurs
     */
    public synchronized void addColumnMetadata(SQLServerDataColumn column) throws SQLServerException {
        // column names must be unique
        Util.checkDuplicateColumnName(column.columnName, columnNames);
        columnMetadata.put(columnCount++, column);
    }


    /**
     * Adds one row of data to the data table.
     * 
     * @param values
     *            values to be added in one row of data to the data table.
     * @throws SQLServerException
     *             when an error occurs
     */
    public synchronized void addRow(Object... values) throws SQLServerException {
        try {
            int columnCount = columnMetadata.size();

            if ((null != values) && values.length > columnCount) {
                MessageFormat form = new MessageFormat(SQLServerException.getErrString("R_moreDataInRowThanColumnInTVP"));
                Object[] msgArgs = {};
                throw new SQLServerException(null, form.format(msgArgs), null, 0, false);
            }

            Iterator<Entry<Integer, SQLServerDataColumn>> columnsIterator = columnMetadata.entrySet().iterator();
            Object[] rowValues = new Object[columnCount];
            int currentColumn = 0;
            while (columnsIterator.hasNext()) {
                Object val = null;

                if ((null != values) && (currentColumn < values.length) && (null != values[currentColumn]))
                    val = values[currentColumn];
                currentColumn++;
                Map.Entry<Integer, SQLServerDataColumn> pair = columnsIterator.next();
                JDBCType jdbcType = JDBCType.of(pair.getValue().javaSqlType);
                internalAddrow(jdbcType, val, rowValues, pair);
            }
            rows.put(rowCount++, rowValues);
        }
        catch (NumberFormatException e) {
            throw new SQLServerException(SQLServerException.getErrString("R_TVPInvalidColumnValue"), e);
        }
        catch (ClassCastException e) {
            throw new SQLServerException(SQLServerException.getErrString("R_TVPInvalidColumnValue"), e);
        }

    }
    
    /**
     * Adding rows one row of data to data table.
     * @param jdbcType The jdbcType
     * @param val The data value
     * @param rowValues Row of data
     * @param pair pair to be added to data table
     * @throws SQLServerException
     */
    private void internalAddrow(JDBCType jdbcType,
            Object val,
            Object[] rowValues,
            Map.Entry<Integer, SQLServerDataColumn> pair) throws SQLServerException {
    	if(null != val) {
	        SQLServerDataColumn currentColumnMetadata = pair.getValue();
	        boolean isColumnMetadataUpdated = false;
	        int nValueLen;
	        switch (jdbcType) {
	            case BIGINT:
	                rowValues[pair.getKey()] = (val instanceof Number ? ((Number)val).longValue() : Long.parseLong(val.toString()));
	                break;
	
	            case BIT:
	                rowValues[pair.getKey()] = (val instanceof Boolean ? (Boolean)val : Boolean.parseBoolean(val.toString()));
	                break;
	
	            case INTEGER:
	                rowValues[pair.getKey()] = (val instanceof Number ? ((Number)val).intValue() : Integer.parseInt(val.toString()));
	                break;
	
	            case SMALLINT:
	            case TINYINT:
	                rowValues[pair.getKey()] = (val instanceof Number ? ((Number)val).shortValue() : Short.parseShort(val.toString()));
	                break;
	
	            case DECIMAL:
	            case NUMERIC:
	            	BigDecimal bd = (val instanceof BigDecimal ? (BigDecimal) val : new BigDecimal(val.toString()));
                    // BigDecimal#precision returns number of digits in the unscaled value.
                    // Say, for value 0.01, it returns 1 but the precision should be 3 for SQLServer
                    int precision = Util.getValueLengthBaseOnJavaType(bd, JavaType.of(bd), null, null, jdbcType);
                    if (bd.scale() > currentColumnMetadata.scale) {
                        currentColumnMetadata.scale = bd.scale();
                        isColumnMetadataUpdated = true;
                    }
                    if (precision > currentColumnMetadata.precision) {
                        currentColumnMetadata.precision = precision;
                        isColumnMetadataUpdated = true;
                    }

                    // precision equal: the maximum number of digits in integer part + the maximum scale
                    int numberOfDigitsIntegerPart = precision - bd.scale();
                    if (numberOfDigitsIntegerPart > currentColumnMetadata.numberOfDigitsIntegerPart) {
                        currentColumnMetadata.numberOfDigitsIntegerPart = numberOfDigitsIntegerPart;
                        isColumnMetadataUpdated = true;
                    }

                    if (isColumnMetadataUpdated) {
                        currentColumnMetadata.precision = currentColumnMetadata.scale + currentColumnMetadata.numberOfDigitsIntegerPart;
                        columnMetadata.put(pair.getKey(), currentColumnMetadata);
                    }
    	            
                    rowValues[pair.getKey()] = bd;
	                break;
	
	            case DOUBLE:
	                rowValues[pair.getKey()] = (val instanceof Number ? ((Number)val).doubleValue() : Double.parseDouble(val.toString()));
	                break;
	
	            case FLOAT:
	            case REAL:
	                rowValues[pair.getKey()] = (val instanceof Number ? ((Number)val).floatValue() : Float.parseFloat(val.toString()));
	                break;
	
	            case TIMESTAMP_WITH_TIMEZONE:
	            case TIME_WITH_TIMEZONE:
	                DriverJDBCVersion.checkSupportsJDBC42();
	            case DATE:
	            case TIME:
	            case TIMESTAMP:
	            case DATETIMEOFFSET:
	            case DATETIME:
	            case SMALLDATETIME:
	                // Sending temporal types as string. Error from database is thrown if parsing fails
	                // no need to send precision for temporal types, string literal will never exceed DataTypes.SHORT_VARTYPE_MAX_BYTES
	
	                if (val instanceof java.util.Date)
	                    rowValues[pair.getKey()] = val.toString();
	                else if (val instanceof microsoft.sql.DateTimeOffset)
	                    rowValues[pair.getKey()] = val.toString();
	                else if (val instanceof OffsetDateTime)
	                    rowValues[pair.getKey()] = val.toString();
	                else if (val instanceof OffsetTime)
	                    rowValues[pair.getKey()] = val.toString();
	                else
	                    rowValues[pair.getKey()] = (String) val;
	                break;
	
	            case BINARY:
	            case VARBINARY:
	            case LONGVARBINARY:
	                nValueLen = ((byte[]) val).length;
	
	                if (nValueLen > currentColumnMetadata.precision) {
	                    currentColumnMetadata.precision = nValueLen;
	                    columnMetadata.put(pair.getKey(), currentColumnMetadata);
	                }
	                rowValues[pair.getKey()] = (byte[]) val;
	                break;
	
	            case CHAR:
	                if (val instanceof UUID) val = val.toString();
	            case VARCHAR:
	            case NCHAR:
	            case NVARCHAR:
	            case LONGVARCHAR:
	            case LONGNVARCHAR:
	            case SQLXML:
	                nValueLen = (2 * ((String) val).length());
	                if (nValueLen > currentColumnMetadata.precision) {
	                    currentColumnMetadata.precision = nValueLen;
	                    columnMetadata.put(pair.getKey(), currentColumnMetadata);
	                }
	                rowValues[pair.getKey()] = (String) val;
	                break;
	            case SQL_VARIANT:
	                JDBCType internalJDBCType;
	                JavaType javaType = JavaType.of(val);
	                internalJDBCType = javaType.getJDBCType(SSType.UNKNOWN, jdbcType);
	                internalAddrow(internalJDBCType, val, rowValues, pair);
	                break;
	            default:
	                MessageFormat form = new MessageFormat(SQLServerException.getErrString("R_unsupportedDataTypeTVP"));
	                Object[] msgArgs = {jdbcType};
	                throw new SQLServerException(null, form.format(msgArgs), null, 0, false);
	        }
        }else {
        	rowValues[pair.getKey()] = null;
        	if(jdbcType == JDBCType.SQL_VARIANT) {
        		throw new SQLServerException(SQLServerException.getErrString("R_invalidValueForTVPWithSQLVariant"), null);
        	}
        }
    }
    
    public synchronized Map<Integer, SQLServerDataColumn> getColumnMetadata() {
        return columnMetadata;
    }

    public String getTvpName() {
        return tvpName;
    }

    /**
     * Retrieves the column meta data of this data table.
     * @param tvpName
     *            the name of TVP
     */
    public void setTvpName(String tvpName) {
        this.tvpName = tvpName;
    }
}
