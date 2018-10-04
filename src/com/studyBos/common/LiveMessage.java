package com.studyBos.common;

public class LiveMessage {
	private String roomId;
	private String subject;
	private String roomMaster;
	private String userId;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getRoomMaster() {
		return roomMaster;
	}
	public void setRoomMaster(String roomMaster) {
		this.roomMaster = roomMaster;
	}
}
