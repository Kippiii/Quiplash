package me.kippy.quiplash;

import com.samczsun.skype4j.chat.Chat;

public class Player {
	public String playerid, createdPrompt, response1, response2;
	public Chat chat;
	public int score = 0;
	public boolean hasVoted, hasSorted = false;
	
	Player(String id, Chat c) {
		this.playerid = id;
		this.chat = c;
	}
	
	public String getPlayerid() {
		return this.playerid;
	}
	
	public Chat getChat() {
		return this.chat;
	}
	
	public void setPrompt(String s) {
		this.createdPrompt = s;
	}
	
	public String getPrompt() {
		return this.createdPrompt;
	}
	
	public void setResponse1(String s) {
		this.response1 = s;
	}
	
	public String getResponse1() {
		return this.response1;
	}
	
	public void setResponse2(String s) {
		this.response2 = s;
	}
	
	public String getResponse2() {
		return this.response2;
	}
	
	public void addScore() {
		this.score ++;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public void setHasVoted(boolean b) {
		this.hasVoted = b;
	}
	
	public boolean getHasVoted() {
		return this.hasVoted;
	}
	
	public void setSorted(boolean b) {
		this.hasSorted = b;
	}
	
	public boolean getSorted() {
		return this.hasSorted;
	}

}
