package me.kippy.quiplash;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.events.EventHandler;
import com.samczsun.skype4j.events.Listener;
import com.samczsun.skype4j.events.chat.message.MessageReceivedEvent;

public class UserChat implements Listener {
	
	@EventHandler
	public void onChat(MessageReceivedEvent e) {
		
		String m = e.getMessage().getContent().asPlaintext();
		String id = e.getMessage().getSender().getId();
		Chat c = e.getChat();
		
		if(Main.gs == GameState.LOGIN) {
			if(m.equalsIgnoreCase("join")) {
				boolean newPlayer = true;
				Player p = Utilities.getObject(id);
				if(p != null) {
						newPlayer = false;
						Utilities.sendMessage(c, "You are already in the game!");
				}
				
				if(newPlayer) {
					Main.players.add(new Player(id, c));
					Main.playerCount ++;
					System.out.println(Main.playerCount + ": " + id + " has joined the game!");
					Utilities.sendMessage(c, "You have joined the game!");
				}
				
			}else if(m.equalsIgnoreCase("leave")) {
				boolean newPlayer = true;
				Player p = Utilities.getObject(id);
				newPlayer = false;
				if(p != null) {
					Main.players.remove(p);
					Main.playerCount--;
					System.out.println(id + " has left the game!");
					Utilities.sendMessage(c, "You have left the game!");
				}
				
				if(newPlayer) {
					Utilities.sendMessage(c, "You never joined the game!");
				}
				
			}else{
				Utilities.sendMessage(c, "I don't understand!");
			}
		}else if(Main.gs == GameState.WRITEPROMPT) {
			Player p = Utilities.getObject(id);
			if(p != null) {
				if(p.getPrompt() != null) {
					Utilities.sendMessage(c, "You already wrote a prompt!");
				}else{
					p.setPrompt(m);
					System.out.println(id + " is ready!");
				}
			}
		}else if(Main.gs == GameState.WRITERESPONSE) {
			for(int i = 0; i < Main.players.size(); i++) {
				Player p = Main.players.get(i);
				if(p.getPlayerid() == id) {
					Player r;
					if(i <= 0) {
						r = Main.players.get(Main.players.size() - 1);
					}else{
						r = Main.players.get(i - 1);
					}
					if(r.getResponse1() != null) {
						if(i >= Main.players.size() - 1) {
							r = Main.players.get(0);
						}else{
							r = Main.players.get(i + 1);
						}
						if(r.getResponse2() != null) {
							Utilities.sendMessage(p.getChat(), "You finished both of your prompts!");
						}else{
							r.setResponse2(m);
							System.out.println(p.getPlayerid() + " is ready!");
							Utilities.sendMessage(p.getChat(), "You finshed answering prompts!");
						}
					}else{
						r.setResponse1(m);
						Player q;
						if(i + 1 > Main.players.size() - 1) {
							q = Main.players.get(0);
						}else{
							q = Main.players.get(i + 1);
						}
						Utilities.sendMessage(p.getChat(), "Your next prompt is: " + q.getPrompt());
					}
				}
			}
		}else if(Main.gs == GameState.VOTING) {
			for(int i = 0; i < Main.players.size(); i++) {
				Player p = Main.players.get(i);
				if(p.getPlayerid() == id) {
					if(p.hasVoted) {
						Utilities.sendMessage(p.getChat(), "You already voted!");
					}else{
						if(m.equalsIgnoreCase("1")) {
							p.setHasVoted(true);
							Main.vote1++;
						}else if(m.equalsIgnoreCase("2")) {
							p.setHasVoted(true);
							Main.vote2++;
						}else{
							Utilities.sendMessage(p.getChat(), "That is not a response!");
						}
					}
				}
			}
		}
		
	}

}
