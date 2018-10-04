package com.studyBos.server.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionFactory{

	//加载jdbc驱动
	private static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	//用映射连接特定的数据库,连接服务器和数据库sample
	private static String url = "jdbc:sqlserver://localhost:1433;DatabaseName=Acount";
	//数据库的登录名和密码
	private static String userName = "MyAccount";
	private static String password = "ssc547012";


	public static Connection getConnection() {
		try {
			//建立与数据库的连接并登陆特定的数据库,加载并初始化驱动
			Class.forName(driver);
			return DriverManager.getConnection(url,userName,password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}