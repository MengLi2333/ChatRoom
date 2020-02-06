package ml.ChatRoom.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ml.ChatRoom.Server.Message;

/**
 * Sender
 * 作用：
 * 		1. 接收控制台的消息，将信息处理打包为Message对象
 * 		2. 将Message对象发送给服务器
 * 
 * @author Meng_Li
 *
 */
public class Sender implements Runnable{
	private ObjectOutputStream oos;
	private BufferedReader br;
	private String name;
	private boolean isLive;
	
	/**
	 * 
	 * @param ois 给服务器
	 * @param dis 给控制台
	 */
	public Sender(ObjectOutputStream oos, BufferedReader br, String name) {
		this.oos = oos;
		this.br = br;
		this.name = name;
		this.isLive = true;
	}
	
	public void receive() {
		try {
			String str = this.br.readLine();
			Message msg;
			Pattern p = Pattern.compile("\\s?((?:(?:\\\\p)|(?:\\\\s)){1,2})\\s+(.+?):(.+)");
			Matcher m = p.matcher(str);
			if (m.matches()) {
				//isSys
				Pattern p1 = Pattern.compile("\\\\s");
				Matcher m1 = p1.matcher(m.group(1));
				//isPrivate
				Pattern p2 = Pattern.compile("\\\\p");
				Matcher m2 = p2.matcher(m.group(1));
				
				if (m1.matches() && m2.matches()) {	//isSys == true && isPrivate == true
					msg = new Message(m.group(3), this.name, true, true, m.group(2));
				} else if (m1.matches() && !m2.matches()) {	//isSys == true && isPrivate == false
					msg = new Message(m.group(3), this.name, true, false, null);
				} else {	//isSys == false && isPrivate == true
					msg = new Message(m.group(3), this.name, false, true, m.group(2));
				}
			} else {	//isSys == false && isPrivate == false
				msg = new Message(str, this.name, false, false, null);
			}
			this.send(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(Message msg) {
		try {
			oos.writeObject(msg);
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
		MyUtils.release(this.oos, this.br);
	}
}
