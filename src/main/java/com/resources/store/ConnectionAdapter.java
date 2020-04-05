package com.resources.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.resources.IOContainer.annotations.Injectable;

@Injectable()
public class ConnectionAdapter {
	Connection conn = null;
	final String USER = "root";
    final String PASSWORD = "1235";
	final String DB_URL = "jdbc:mysql://localhost:3306/employeedb";
	
	public Connection initConnection() {
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	
	
	
}
