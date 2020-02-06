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
 * ���ã�
 * 		1. ����Server������Socket������I/O��
 * 		2. ���ն�Ӧ�û�����Ϣ��Message���󣩣�����ת���������û�
 * 			- ��ϢȺ������ʵ�֣�����channelList�е�Channel�������ε���Channel�����send����
 * 			- ˽����Ϣ��ȻȺ���������Client�˴���
 * 
 * @author Meng_Li
 *
 */
public class Channel implements Runnable{
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private DataOutputStream dos;	//���ڴ���ϵͳ�����Ϣ
	private DataInputStream dis;	//���ڴ���ϵͳ�����Ϣ
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
//			//���߿ͻ���id
//			this.dos.writeInt(id);
//			//��ȡ�û��ǳ�
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
		System.out.println("Channel�̹߳ر�");
		channelList.remove(this);
		MyUtils.release(ois, socket, oos);
	}
}
