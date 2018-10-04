package com.studyBos.server.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.PublicKey;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.studyBos.server.model.StudyBosServer;

/**
 * �����͹رշ���������ʾ����������ǿ������
 * @author ���ز�
 *
 */

public class MyServerFrame extends JFrame{
	private JPanel jp1;
	private JButton jb1,jb2;
	private StudyBosServer sbs;
	private Thread t;
	
	public static void main(String[] args){
		new MyServerFrame();
	}
	
	public MyServerFrame(){
		sbs=new StudyBosServer();
		jp1 = new JPanel();
		jb1 = new JButton("����������");
		jb2 = new JButton("�رշ�����");
		jp1.add(jb1);
		jp1.add(jb2);

		jb1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						// TODO Auto-generated method stub
						//�����������ťʱ
						StudyBosServer.isDestroyed = false;
						sbs.startServer();
					}
				}).start();
			}
		});
		jb2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				StudyBosServer.isDestroyed = true;
				//������رհ�ťʱ
				sbs.closeServer();
			}
		});
		
		this.add(jp1);
		this.setSize(500,400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}
