//---------------------------------------------------------------------------------------------------------------------------------
// File: SqlChar.java
//
//
// Microsoft JDBC Driver for SQL Server
// Copyright(c) Microsoft Corporation
// All rights reserved.
// MIT License
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files(the "Software"), 
//  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
//  and / or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions :
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
// THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
//  IN THE SOFTWARE.
//---------------------------------------------------------------------------------------------------------------------------------
 

package com.microsoft.sqlserver.testframework.sqlType;

import java.sql.JDBCType;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.RandomStringUtils;

/*
 * Restricting the size of char/varchar to 4000 and nchar/nvarchar to 2000 
 * to accommodate SQL Sever limitation of having of having maximum allowable table row size to 8060 
 */
public class SqlChar extends SqlType {

	public SqlChar() {
		this("char", JDBCType.CHAR, 4000);
	}

	SqlChar(String name, JDBCType jdbctype, int precision) {
		super(name, jdbctype, precision, 0, SqlTypeValue.CHAR.minValue, SqlTypeValue.CHAR.maxValue, SqlTypeValue.CHAR.nullValue, VariableLengthType.Precision);
		generatePrecision();
	}

	public Object createdata() {
		int dataLength = ThreadLocalRandom.current().nextInt(precision);
		return StringEscapeUtils.escapeSql(RandomStringUtils.randomAscii(dataLength));
	}
}