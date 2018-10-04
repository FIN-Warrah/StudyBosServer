package com.studyBos.server.model;

import java.util.HashMap;


public class ManageClientThread {
	public static HashMap<String, ServerConnectThread> hMap = new HashMap<>();
	
	//��ӿͻ�ͨѶ�߳�
	public static void addClientThread(String userId,ServerConnectThread ct){
		hMap.put(userId, ct);
	}

	public static ServerConnectThread getClientThread(String userId){
		//��������ڸ�ֵʱ����null
		return (ServerConnectThread)hMap.get(userId);
	}
	
	public static void deleteClientThread(String userId){
		hMap.remove(userId);
	}
}
