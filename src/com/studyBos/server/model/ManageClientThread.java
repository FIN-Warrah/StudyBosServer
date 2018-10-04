package com.studyBos.server.model;

import java.util.HashMap;


public class ManageClientThread {
	public static HashMap<String, ServerConnectThread> hMap = new HashMap<>();
	
	//添加客户通讯线程
	public static void addClientThread(String userId,ServerConnectThread ct){
		hMap.put(userId, ct);
	}

	public static ServerConnectThread getClientThread(String userId){
		//如果不存在该值时返回null
		return (ServerConnectThread)hMap.get(userId);
	}
	
	public static void deleteClientThread(String userId){
		hMap.remove(userId);
	}
}
