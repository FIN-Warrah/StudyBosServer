package com.studyBos.server.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionFactory{

	//����jdbc����
	private static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	//��ӳ�������ض������ݿ�,���ӷ����������ݿ�sample
	private static String url = "jdbc:sqlserver://localhost:1433;DatabaseName=Acount";
	//���ݿ�ĵ�¼��������
	private static String userName = "MyAccount";
	private static String password = "ssc547012";


	public static Connection getConnection() {
		try {
			//���������ݿ�����Ӳ���½�ض������ݿ�,���ز���ʼ������
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