package com.studyBos.server.model;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StudyBosServer {
	
	private ServerSocket ss = null;
	public static boolean isDestroyed = false;
	private ThreadPoolExecutor tpe;
	
	public void startServer() {
		
		// TODO Auto-generated method stub
		try {
			ServerSocket ss=new ServerSocket(9999);
			while(true){
				//阻塞等待
				Socket s = ss.accept();
				
				if(isDestroyed){
					if(isDestroyed){
						ss.close();
						break;
					}
				}
				
				//创建一个核心线程为50，最大线程为100，每个线程在没有任务时的保持时间为1天的能存储50个任务队列(用于当没有线程处理任务时
				//使用)的线程池
				tpe =  new ThreadPoolExecutor(50, 100, 1, TimeUnit.DAYS, new ArrayBlockingQueue<Runnable>(50));
				tpe.execute(new LoginAndRegister(s));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeServer()
	{
		try {
			Socket socket = new Socket("192.168.1.233",9999);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
