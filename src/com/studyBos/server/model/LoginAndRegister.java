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
	
	//ע����Ϣ
	public void registerInfo(RegisterMessage registerMessage){
		
		try {
			statement = connection.createStatement();
			// TODO 2018/9/15
			String sql = "insert into register values ('"+registerMessage.getUserId()+"','"+registerMessage.getPassword()+"',"+
					registerMessage.getIdentity()+",'"+registerMessage.getPhoneNum()+"','"+registerMessage.getQq()+"','"+
					registerMessage.getWeiXin()+"','"+registerMessage.getGrade()+"','"+registerMessage.getProfession()+"')";
			//��ͻ��˷�����Ӧ��
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
	
	//��¼����
	public void manageLogin(LoginInfo li){
		try {
			String sql = "select * from register where userId = ? and pwd = ?";
			//׼��SQL��䣬Ȼ���滻�ʺ�
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, li.getUserId());
			pstmt.setString(2, li.getPassword());
			//��ѯ��ý��
			rs= pstmt.executeQuery();
			//�ж��Ƿ��м�¼
			if(rs.next()){
				//��¼��Ϣ��֤�ɹ�ʱ�����û���ӵ��̹߳�������
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
