package ml.ChatRoom.Client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;

import ml.ChatRoom.Server.Message;

/**
 * Receiver
 * 作用：
 * 		1. 接收服务器的消息（Message对象）
 * 		2. 处理Message对象，将处理得到的信息打印到控制台
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
				if (!msg.getTarget().equals(this.name)) {	//不是发给自己的就结束
					return ;
				}
				if (msg.isSys()) {
					bw.write("系统通知（私密）：" + msg.getMsg());
				} else {
					bw.write(msg.getSenderName() + " 对你说（私密）：" + msg.getMsg());
				}
			} else {
				if (msg.isSys()) {
					bw.write("系统通知：" + msg.getMsg());
				} else {
					bw.write(msg.getSenderName() + " 对大家说：" + msg.getMsg());
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
