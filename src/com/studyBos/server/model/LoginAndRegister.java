package com.studyBos.server.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.spi.DirStateFactory.Result;
import javax.sql.StatementEventListener;

import com.studyBos.common.LoginInfo;
import com.studyBos.common.RegisterMessage;
import com.studyBos.server.util.Code;
import com.studyBos.server.util.ConnectionFactory;

public class LoginAndRegister implements Runnable{

	private Socket socket;
	private Statement statement;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private BufferedWriter bw;
	Connection connection;
	
	
	public LoginAndRegister(Socket s) {
		// TODO Auto-generated constructor stub
		connection = ConnectionFactory.getConnection();
		this.socket = socket;
	}
	
	@Override
	public void run(){
		
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			Object b = ois.readObject();
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			if(b instanceof RegisterMessage){
				RegisterMessage rm = (RegisterMessage)b;
				
			}else if(b instanceof LoginInfo){
	
			}
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//注册信息
	public void registerInfo(RegisterMessage registerMessage){
		
		try {
			statement = connection.createStatement();
			// TODO 2018/9/15
			String sql = "insert into register values ('"+registerMessage.getUserId()+"','"+registerMessage.getPassword()+"',"+
					registerMessage.getIdentity()+",'"+registerMessage.getPhoneNum()+"','"+registerMessage.getQq()+"','"+
					registerMessage.getWeiXin()+"','"+registerMessage.getGrade()+"','"+registerMessage.getProfession()+"')";
			//向客户端返回响应码
			bw.write(Code.REGISTER_SUCCESS);
		} catch (SQLException e) {
			try {
				bw.write(Code.REGISTRE_ERROR);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{

			try {
				socket.close();
				bw.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//登录管理
	public void manageLogin(LoginInfo li){
		try {
			String sql = "select * from register where userId = ? and pwd = ?";
			//准备SQL语句，然后，替换问号
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, li.getUserId());
			pstmt.setString(2, li.getPassword());
			//查询获得结果
			rs= pstmt.executeQuery();
			//判断是否有记录
			if(rs.next()){
				//登录信息验证成功时，将用户添加到线程管理器中
				bw.write(Code.LOGIN_SUCCESS);
				ServerConnectThread sct = new ServerConnectThread(this.socket,li.getUserId());
				ManageClientThread.addClientThread(li.getUserId(), sct);
				sct.start();
			}else{
				bw.write(Code.LOGIN_ERROR);
			}
		} catch (SQLException e) {
			try {
				bw.write(Code.LOGIN_ERROR);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {

			try {
				connection.close();
				pstmt.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
