//---------------------------------------------------------------------------------------------------------------------------------
// File: DBConnection.java
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
 

package com.microsoft.sqlserver.testframework;

import java.sql.SQLException;

import com.microsoft.sqlserver.jdbc.SQLServerConnection;

/*
 * Wrapper class for SQLServerConnection
 */
public class DBConnection extends AbstractParentWrapper{

	// TODO: add Isolation Level
	// TODO: add auto commit
	// TODO: add connection Savepoint and rollback
	// TODO: add additional connection properties
	// TODO: add DataSource support
	private SQLServerConnection connection = null;
	
	public DBConnection()
	{
		super(null, null, "connection");
	}
	
	/**
	 * establish connection
	 * @param connectionString
	 */
	public void getConnection(String connectionString) {
		try {
			connection = PrepUtil.getConnection(connectionString);
			setInternal(connection);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	void setInternal(Object internal){
		this.internal = internal;
	}
	
	/**
	 * 
	 * @return Statement wrapper
	 */
	public DBStatement createStatement() {
		DBStatement dbstatement = new DBStatement(this);
		return dbstatement.createStatement();
	}
	
}
