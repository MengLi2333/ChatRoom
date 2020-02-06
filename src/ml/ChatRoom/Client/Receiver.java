package ml.ChatRoom.Client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;

import ml.ChatRoom.Server.Message;

/**
 * Receiver
 * ���ã�
 * 		1. ���շ���������Ϣ��Message����
 * 		2. ����Message���󣬽�����õ�����Ϣ��ӡ������̨
 * 
 * @author Meng_Li
 *
 */
public class Receiver implements Runnable {
	private ObjectInputStream ois;
	private BufferedWriter bw;
	private boolean isLive;
	private String name;
	
	public Receiver(ObjectInputStream ois, BufferedWriter bw, String name) {
		this.isLive = true;
		this.ois = ois;
		this.bw = bw;
		this.name = name;
	}
	
	public void receive() {
		try {
			Message msg = (Message)ois.readObject();
			this.sendToConsole(msg);
		} catch (ClassNotFoundException e) {
			this.close();
		} catch (IOException e) {
			this.close();
		}
	}
	
	public void sendToConsole(Message msg) {
		try {
			if (msg.isPrivate()) {
				if (!msg.getTarget().equals(this.name)) {	//���Ƿ����Լ��ľͽ���
					return ;
				}
				if (msg.isSys()) {
					bw.write("ϵͳ֪ͨ��˽�ܣ���" + msg.getMsg());
				} else {
					bw.write(msg.getSenderName() + " ����˵��˽�ܣ���" + msg.getMsg());
				}
			} else {
				if (msg.isSys()) {
					bw.write("ϵͳ֪ͨ��" + msg.getMsg());
				} else {
					bw.write(msg.getSenderName() + " �Դ��˵��" + msg.getMsg());
				}
			}
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		this.isLive = false;
	}
	
	@Override
	public void run() {
		while (this.isLive) {
			this.receive();
		}
		MyUtils.release(this.ois, this.bw);
	}
	
}
