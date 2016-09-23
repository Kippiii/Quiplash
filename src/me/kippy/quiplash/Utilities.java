package me.kippy.quiplash;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.exceptions.ConnectionException;

public class Utilities {
	
	public static void sendMessage(Chat c, String msg) {
		try {
			c.sendMessage(msg);
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
	}
	
	public static void allSendMessage(String msg) {
		for(int i = 0; i < Main.players.size(); i++) {
			try {
				Main.players.get(i).getChat().sendMessage(msg);
			} catch (ConnectionException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Player getObject(String id) {
		Player p = null;
		for(int i = 0; i < Main.players.size(); i++) {
			if(Main.players.get(i).getPlayerid() == id) {
				p = Main.players.get(i);
			}
		}
		return p;
	}

}
