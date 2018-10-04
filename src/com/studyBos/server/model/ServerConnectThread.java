package com.studyBos.server.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyStore.Entry;
import java.security.interfaces.RSAKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;

import javax.naming.ldap.ManageReferralControl;

import com.studyBos.common.AnswerInfo;
import com.studyBos.common.ChatMessage;
import com.studyBos.common.HeadPortraitPack;
import com.studyBos.common.LiveMessage;
import com.studyBos.common.ProblemPack;
import com.studyBos.common.requestCode;
import com.studyBos.server.util.Code;
import com.studyBos.server.util.ConnectionFactory;
import com.studyBos.server.util.Transition;

public class ServerConnectThread extends Thread{
	
	private String sctUserId;
	private Connection  conn;
	private PreparedStatement pstmt;
	private Statement stmt;
	private ConnectionFactory factory;
	private ResultSet result;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private boolean isDestroyed = false;
	
	
	public Socket getSocket() {
		return socket;
	}

	public ServerConnectThread(Socket socket,String userId) throws IOException {
		// TODO Auto-generated constructor stub
		isDestroyed = false;
		this.socket = socket;
		this.sctUserId = userId;
		ois = new ObjectInputStream(socket.getInputStream());
		factory = new ConnectionFactory();
	}
	

	@Override
	public void run(){
		while(true){
			try {
				ois  = new ObjectInputStream(socket.getInputStream());
				Object o = ois.readObject();
				processer(o);
				if(isDestroyed){
					break;
				}
			} catch (IOException | ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	@SuppressWarnings("static-access")
	public synchronized void processer(Object o) throws SQLException {
		conn = factory.getConnection();
		if(o instanceof ChatMessage){
			try {
				//实现消息的转发
				ChatMessage cm = (ChatMessage)o; 
				ServerConnectThread sct = ManageClientThread.getClientThread(cm.getGetter());
				if(sct == null){
					String sql = "insert into chatMess values("+"'"+cm.getGetter()+"','"+cm.getSender()+"','"+cm.getMess()+"','"+
							cm.getDate()+"')";
					stmt = conn.createStatement();
					stmt.execute(sql);
				}else{
					oos = new ObjectOutputStream(sct.getSocket().getOutputStream());
					oos.writeObject(cm);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(o instanceof HeadPortraitPack){
			//将图片存入到数据库中
			try {
				stmt = conn.createStatement();
				HeadPortraitPack hpp = (HeadPortraitPack)o;
				String sql = "insert into portrait values("+"'"+hpp.getUserId()+"',"+hpp.getImageByte()+",'"+hpp.getPortraitName()+"','"+
				hpp.getFormat()+"')";
				//完成写入数据库
				stmt.executeQuery(sql);
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(o instanceof ProblemPack){
			//将问题推送出去，并且把问题存起来
			stmt = conn.createStatement();
			ProblemPack pp = (ProblemPack)o;
			String sql = "insert into problem values("+"'"+pp.getAskerId()+"',"+pp.getProHash()+",'"+pp.getProblem()+"',"+
			pp.getImageBytes()+",'"+pp.getAsktime()+"','"+pp.getProblem()+"','"+pp.getProject()+"')";
			//完成写入数据库
			stmt.executeQuery(sql);
			//向在线用户推送问题
			Transition.sendToAll(pp);
		}else if(o instanceof requestCode){
			//接收客户端的请求
			requestCode rCode = new requestCode();
			int code = rCode.getRequestCode();
			String sql = null;
			switch (code) {
				//更新聊天数据
				case 0x06:
					sql = "select * from chatMess where userId = '"+sctUserId+"'";
					pstmt = conn.prepareStatement(sql);
					result = pstmt.executeQuery();
					while(result.next()){
						ChatMessage chatmesss=new ChatMessage();
						chatmesss.setGetter(result.getString("userId"));
						chatmesss.setSender(result.getString("from"));
						chatmesss.setDate(result.getString("date"));
						chatmesss.setMess(result.getString("mess"));
						try {
							oos = new ObjectOutputStream(this.socket.getOutputStream());
							oos.writeObject(chatmesss);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				break;
			
				//更新问题信息
				case 0x07:
					sql = "select * from problem";
					pstmt = conn.prepareStatement(sql);
					result = pstmt.executeQuery();
					while(result.next()){
						ProblemPack pp = new ProblemPack();
						pp.setAskerId(result.getString("askerId"));
						pp.setProHash(result.getInt("proHash"));
						pp.setProblem(result.getString("problem"));
						pp.setImageBytes(result.getBytes("image"));
						pp.setAsktime(result.getString("askTime"));
						pp.setProfession(result.getString("profession"));
						pp.setProject(result.getString("project"));
							
						try {
							oos = new ObjectOutputStream(this.socket.getOutputStream());
							oos.writeObject(pp);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
					
				//更新直播信息
				case 0x08:
					sql = "select * from liveMess";
					pstmt = conn.prepareStatement(sql);
					result = pstmt.executeQuery();
					while(result.next()){
						LiveMessage  lm = new LiveMessage();
						lm.setRoomId(result.getString("roomId"));
						lm.setSubject(result.getString("subject"));
						lm.setRoomMaster(result.getString("roomMaster"));
						lm.setUserId(result.getString("userId"));
						try {
							oos = new ObjectOutputStream(this.socket.getOutputStream());
							oos.writeObject(lm);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				
				//更新头像
				case 0x09:
					sql = "select * from portrait where userId = '"+sctUserId+"'";
					pstmt = conn.prepareStatement(sql);
					result = pstmt.executeQuery();
					while(result.next()){
						HeadPortraitPack hpp = new HeadPortraitPack();
						hpp.setUserId(sctUserId);
						hpp.setImageByte(result.getBytes("portrait"));
						hpp.setPortraitName(result.getString("portraitName"));
						hpp.setFormat(result.getString("format"));
						try {
							oos = new ObjectOutputStream(this.socket.getOutputStream());
							oos.writeObject(hpp);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
					
				case 0x0A:
					try {
						this.socket.close();
						isDestroyed = true;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					break;
					
					//更新回答信息
					case 0x0B:
						sql = "select * from answer";
						pstmt = conn.prepareStatement(sql);
						result = pstmt.executeQuery();
						while(result.next()){
							AnswerInfo ai = new AnswerInfo();
							ai.setAnswerId(result.getString("askerId"));
							ai.setProblemHash(result.getInt("proHash"));
							ai.setAnswerTime(result.getString("answerTime"));
							ai.setAnswer(result.getString("answer"));
							try {
								oos = new ObjectOutputStream(this.socket.getOutputStream());
								oos.writeObject(ai);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						break;

				default:
					break;
			}
		}else if(o instanceof LiveMessage){
			//推送和存储直播的信息
			stmt = conn.createStatement();
			LiveMessage lm = (LiveMessage)o;
			String sql = "insert into liveMess values('"+lm.getRoomId()+"','"+lm.getSubject()+"','"+lm.getRoomMaster()+"','"+
			lm.getUserId()+"')";
			stmt.executeQuery(sql);
			Transition.sendToAll(lm);
		}else if(o instanceof AnswerInfo){
			//将问题的答案存起来
			stmt = conn.createStatement();
			AnswerInfo ai = (AnswerInfo)o;
			String sql = "insert into answer values('"+ai.getAnswerId()+"',"+ai.getProblemHash()+",'"+ai.getAnswerTime()+"','"+
			ai.getAnswer()+"')";
			stmt.executeQuery(sql);
			//将答案推送出去
			Transition.sendToAll(o);
		}
		
		stmt.close();
		conn.close();
	}
}
