/**
 * Licensed to ESUP-Portail under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * ESUP-Portail licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.esupportail.dining.web.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {

	private Connection connection;
	private Statement statement;

	// db connection infos in src/main/resources/defaults.properties
	public DatabaseConnector(String db_driver, String db_infos, String db_user,
			String db_pwd) {
		try {
			Class.forName(db_driver).newInstance();
			this.connection = DriverManager.getConnection(db_infos, db_user,
					db_pwd);
			this.connection.setAutoCommit(true);
			this.statement = this.connection.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
		} catch (Exception e) {
			// Problem with the db connection
			e.printStackTrace();
		}
	}

	public ResultSet executeQuery(String query) throws SQLException {
		return this.statement.executeQuery(query);
	}

	public void executeUpdate(String update) throws SQLException {
		this.statement.executeUpdate(update);
	}

}
