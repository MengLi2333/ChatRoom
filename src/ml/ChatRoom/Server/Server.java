package ml.ChatRoom.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 服务器
 * 总体目标：多用户自由收发信息
 * 默认服务器为localhost:6669
 * 
 * 作用：
 * 		1. 接收客户端连接请求
 * 		2. 为每个客户端建立一个Channel线程，并分配id
 * 		3. 将所有Channel线程放在channelList容器中
 * 
 * Channel简述：Channel extends Thread
 * 		- 每个用户对应一个Channel线程
 * 		- Channel具体功能：接收对应用户发出的消息，将其逐个发送至其他用户
 * 
 * @author Meng_Li
 * 
 */
public class Server {
	private ServerSocket serversocket;
	private CopyOnWriteArrayList<Channel> channelList;
	private boolean isOpen;
	private int count;   //用于生成用户的唯一id
	
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
				System.out.println("接收到一个用户，id：" + this.count);
				Channel channel = new Channel(socket, this.channelList, this.count);
				this.channelList.add(channel);
				new Thread(channel).start();
				System.out.println("启动Channel线程");
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
		System.out.println("------初始化Server-------");
		server.openServer();
	}
	
}
