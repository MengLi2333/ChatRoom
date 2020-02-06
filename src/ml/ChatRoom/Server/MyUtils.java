package ml.ChatRoom.Server;

import java.io.Closeable;
import java.io.IOException;

public class MyUtils {
	private MyUtils() {
	}
	
	public static void release(Closeable... objs) {
		for (Closeable obj: objs) {
			try {
				obj.close();
			} catch (IOException e) {
			}
		}
	}
}
