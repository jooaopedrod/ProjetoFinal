package application.config;

import java.sql.*;

public class JDBCConnection {

	public static Connection conn;

	public static void JDBCConnect() {

		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/venda", "postgres", "bancodedados");

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}

}
