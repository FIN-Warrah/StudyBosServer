package com.studyBos.server.util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Map;

import com.studyBos.server.model.ManageClientThread;
import com.studyBos.server.model.ServerConnectThread;

public class Transition {
	
	private static ObjectOutputStream oos;
	
	public synchronized static void sendToAll(Object o){
		Iterator iterator = ManageClientThread.hMap.entrySet().iterator();//»ñÈ¡µü´úÆ÷
		while(iterator.hasNext()){
			try {
				Map.Entry entry = (Map.Entry) iterator.next();
				ServerConnectThread sct = (ServerConnectThread) entry.getValue();
				oos = new ObjectOutputStream(sct.getSocket().getOutputStream());
				oos.writeObject(o);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
}
