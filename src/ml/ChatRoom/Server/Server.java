package ml.ChatRoom.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ������
 * ����Ŀ�꣺���û������շ���Ϣ
 * Ĭ�Ϸ�����Ϊlocalhost:6669
 * 
 * ���ã�
 * 		1. ���տͻ�����������
 * 		2. Ϊÿ���ͻ��˽���һ��Channel�̣߳�������id
 * 		3. ������Channel�̷߳���channelList������
 * 
 * Channel������Channel extends Thread
 * 		- ÿ���û���Ӧһ��Channel�߳�
 * 		- Channel���幦�ܣ����ն�Ӧ�û���������Ϣ��������������������û�
 * 
 * @author Meng_Li
 * 
 */
public class Server {
	private ServerSocket serversocket;
	private CopyOnWriteArrayList<Channel> channelList;
	private boolean isOpen;
	private int count;   //���������û���Ψһid
	
	public Server(int port) throws IOException {
		this.serversocket = new ServerSocket(port);
		this.channelList = new CopyOnWriteArrayList<Channel>();
		this.isOpen = true;
		this.count = 0;
	}
	
	public Server() throws IOException {
		this(6669);
	}
	
	public void openServer() {
		this.isOpen = true;
		while(isOpen) {
			try {
				Socket socket = this.serversocket.accept();
				this.count++;
				System.out.println("���յ�һ���û���id��" + this.count);
				Channel channel = new Channel(socket, this.channelList, this.count);
				this.channelList.add(channel);
				new Thread(channel).start();
				System.out.println("����Channel�߳�");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			this.serversocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeServer() {
		this.isOpen = false;
		for (Channel channel: channelList) {
			channel.close();
		}
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println("------Server-------");
		Server server = new Server();
		System.out.println("------��ʼ��Server-------");
		server.openServer();
	}
	
}
