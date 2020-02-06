package ml.ChatRoom.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Channel
 * 作用：
 * 		1. 利用Server传来的Socket对象建立I/O流
 * 		2. 接收对应用户的信息（Message对象），将其转发至其他用户
 * 			- 消息群发具体实现：遍历channelList中的Channel对象，依次调用Channel对象的send方法
 * 			- 私发消息依然群发，最后由Client端处理
 * 
 * @author Meng_Li
 *
 */
public class Channel implements Runnable{
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private DataOutputStream dos;	//用于传输系统间的信息
	private DataInputStream dis;	//用于传输系统间的信息
	private CopyOnWriteArrayList<Channel> channelList;
	private boolean isLive;
	
	public Channel(Socket socket, CopyOnWriteArrayList<Channel> channelList, int id) {
		this.socket = socket;
		this.channelList = channelList;
		this.isLive = true;
		
		try {
			this.ois = new ObjectInputStream(this.socket.getInputStream());
			this.oos = new ObjectOutputStream(this.socket.getOutputStream());
			this.dos = new DataOutputStream(this.socket.getOutputStream());
			this.dis = new DataInputStream(this.socket.getInputStream());
			
		} catch (IOException e) {
			e.printStackTrace();
			this.isLive = false;
		}
		
//		this.init();
	}
	
//	private void init() {
//		
//		try {
//			Thread.sleep(1000);
//			//告诉客户端id
//			this.dos.writeInt(id);
//			//获取用户昵称
//			this.name = this.dis.readUTF();
//		} catch (IOException | InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//	}

	public void sendOthers(Message msg) {

		for (Channel other: channelList) {
			if (other != this) {
				other.send(msg);
			}
		}
	}

	public void receive() {
		Message msg = null;
		try {
			msg = (Message)ois.readObject();
			this.sendOthers(msg);
		} catch (ClassNotFoundException e) {
			this.close();
		} catch (IOException e) {
			this.close();
		}
		
	}
	
	public void send(Message msg) {
		try {
			this.oos.writeObject(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		this.isLive = false;
	}
	
	@Override
	public void run() {
		while (isLive) {
			this.receive();
		}
		System.out.println("Channel线程关闭");
		channelList.remove(this);
		MyUtils.release(ois, socket, oos);
	}
}
