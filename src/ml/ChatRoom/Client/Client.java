package ml.ChatRoom.Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 客户端
 * 总体目标：多用户自由收发信息
 * 默认服务器为localhost:6669
 * 
 * 作用：
 * 		1. 请求连接服务器
 * 		2. 获取用户信息（用户昵称）
 * 		3. 开启Sender和Receiver线程
 * 
 * Sender简述：
 * Sender extends Thread：从控制台获取消息，发送至服务器
 * 
 * Receiver简述：
 * Receiver extends Thread：从服务器获取消息，打印到控制台
 * 
 * @author Meng_Li
 *
 */
public class Client {
	private Socket socket;
	private InetAddress addr;
	private String name;
	private DataInputStream dis;	//用于传收系统间的信息
	private DataOutputStream dos;	//用于传收系统间的信息
	
	public Client(int port, InetAddress addr, String name) throws IOException {
		System.out.println("请求服务器");
		this.socket = new Socket(addr, port);
		System.out.println("请求服务器完毕");
		this.addr = addr;
		this.name = name;
		this.dis = new DataInputStream(this.socket.getInputStream());
		this.dos = new DataOutputStream(this.socket.getOutputStream());
		
//		this.init();

	}
	
//	private void init() {
//		
//		try {
//			Thread.sleep(1000);
//			//获取服务器传过来的id
//			this.id = this.dis.readInt();
//			//传给服务器用户的昵称
//			this.dos.writeUTF(name);
//		} catch (IOException | InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		
//	}
	
	public Client(String name) throws UnknownHostException, IOException {
		this(6669, InetAddress.getByName("localhost"), name);
	}

	public InetAddress getAddress() {
		return addr;
	}
	
	public void openClient() throws IOException {
		Sender sender = new Sender(new ObjectOutputStream(socket.getOutputStream()), 
				new BufferedReader(new InputStreamReader(System.in)), this.name);
		Receiver receiver = new Receiver(new ObjectInputStream(socket.getInputStream()), 
				new BufferedWriter(new OutputStreamWriter(System.out)), this.name);
		
		new Thread(sender).start();
		new Thread(receiver).start();
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("欢迎使用，请登录：");
		String name = new BufferedReader(new InputStreamReader(System.in, "gbk")).readLine();
		System.out.println("接收到name:" + name);
		Client client = new Client(name);
		client.openClient();
	}
	
}
