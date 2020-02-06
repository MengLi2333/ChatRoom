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
 * �ͻ���
 * ����Ŀ�꣺���û������շ���Ϣ
 * Ĭ�Ϸ�����Ϊlocalhost:6669
 * 
 * ���ã�
 * 		1. �������ӷ�����
 * 		2. ��ȡ�û���Ϣ���û��ǳƣ�
 * 		3. ����Sender��Receiver�߳�
 * 
 * Sender������
 * Sender extends Thread���ӿ���̨��ȡ��Ϣ��������������
 * 
 * Receiver������
 * Receiver extends Thread���ӷ�������ȡ��Ϣ����ӡ������̨
 * 
 * @author Meng_Li
 *
 */
public class Client {
	private Socket socket;
	private InetAddress addr;
	private String name;
	private DataInputStream dis;	//���ڴ���ϵͳ�����Ϣ
	private DataOutputStream dos;	//���ڴ���ϵͳ�����Ϣ
	
	public Client(int port, InetAddress addr, String name) throws IOException {
		System.out.println("���������");
		this.socket = new Socket(addr, port);
		System.out.println("������������");
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
//			//��ȡ��������������id
//			this.id = this.dis.readInt();
//			//�����������û����ǳ�
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
		System.out.println("��ӭʹ�ã����¼��");
		String name = new BufferedReader(new InputStreamReader(System.in, "gbk")).readLine();
		System.out.println("���յ�name:" + name);
		Client client = new Client(name);
		client.openClient();
	}
	
}
