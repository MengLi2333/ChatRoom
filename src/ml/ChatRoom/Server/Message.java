package ml.ChatRoom.Server;

import java.io.Serializable;

/**
 * Message
 * 作用：用户间传输的信息
 * 
 * @author Meng_Li
 *
 */
public class Message implements Serializable {

	private static final long serialVersionUID = -8355123905885896644L;
	private String msg;
//	private byte[] msgBytes;
	private String senderName;
//	private byte[] senderNameBytes;
	private String target;	//私聊时的对话方
//	private byte[] targetBytes;
	private boolean isSys;
	private boolean isPrivate;
	
	public Message(String msg, String senderName, boolean isSys, boolean isPrivate, String target) {
		this.msg = msg;
		this.senderName = senderName;
		this.target = target;
		this.isSys = isSys;
		this.isPrivate = isPrivate;
	}
	
	

//	public String getSenderName() {
//		String name = null;
//		try {
//			name = new String(senderNameBytes, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		return name;
//	}

//	public void setSenderName(String senderName) {
//		try {
//			this.senderNameBytes = senderName.getBytes("UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public String getTarget() {
//		String target = null;
//		try {
//			target = new String(targetBytes, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		return target;
//	}
//
//	public void setTarget(String target) {
//		try {
//			this.targetBytes = target.getBytes("UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//	}

	public String getSenderName() {
		return senderName;
	}



	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}



	public String getTarget() {
		return target;
	}



	public void setTarget(String target) {
		this.target = target;
	}



	public String getMsg() {
//		String msg = null;
//		try {
//			msg = new String(msgBytes, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		return msg;
		return this.msg;
	}
	
	public void setMsg(String msg) {
//		try {
//			this.msgBytes = msg.getBytes("UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		this.msg = msg;
	}
	
	public boolean isSys() {
		return isSys;
	}
	
	public void setSys(boolean isSys) {
		this.isSys = isSys;
	}
	
	public boolean isPrivate() {
		return isPrivate;
	}
	
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	
}
