package me.kippy.quiplash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.SkypeBuilder;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.exceptions.InvalidCredentialsException;
import com.samczsun.skype4j.exceptions.NotParticipatingException;

public class Main {
	public static GameState gs;
	public static ArrayList<Player> players = new ArrayList<Player>();
	public static int playerCount, countdown, voteNumber, vote1, vote2, numberSorted = 1;
	public static boolean voteReady;
	
	public static void main(String[] args) {
		
		Skype skype = new SkypeBuilder(Login.username, Login.password).withAllResources().build();
		
		try {
			skype.login();
		} catch (NotParticipatingException e) {
			e.printStackTrace();
		} catch (InvalidCredentialsException e) {
			e.printStackTrace();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
		
		try {
			skype.subscribe();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
		
		registerEvents(skype);
		
		System.out.println("Logged in!");
		
		gs = GameState.LOGIN;
		
		playerCount = 0;
		
		(new Thread(new Running())).start();
		
		voteReady = false;
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String command = scanner.next();
		if(command.equalsIgnoreCase("start")) {
			gs = GameState.WRITEPROMPT;
			System.out.println("The game has begun!");
			countdown = 31;
			Utilities.allSendMessage("The game has begun! Type a prompt that you want people to respond to!");
		}
		
	}
	
	public static void registerEvents(Skype skype) {
		
		skype.getEventDispatcher().registerListener(new UserChat());
		
	}

	public static void sendPrompts() {
		for(int i = 0; i < players.size(); i++) {
			Player p = players.get(i);
			
			if(p.getPrompt() == null) {
				Main.players.remove(i);
				playerCount --;
				System.out.println(p.getPlayerid() + " has been kicked from the game!");
				Utilities.sendMessage(p.getChat(), "You were kicked from the game due to inactivity.");
				break;
			}
			
			Player r;
			if(i - 1 < 0) {
				r = players.get(players.size() - 1);
			}else{
				r = players.get(i - 1);
			}
			
			Utilities.sendMessage(p.getChat(), "Answer this prompt: " + r.getPrompt());
			
		}
		
		countdown = 61;
		
	}
	
	public static void vote(int i) {
		
		Player p = players.get(i);
		voteReady = true;
		voteNumber = i;
		vote1 = 0;
		vote2 = 0;
		for(int i1 = 0; i1 < players.size(); i1++) {
			players.get(i1).setHasVoted(false);
		}
		boolean noResponse1 = false, noResponse2 = false;
		String m = p.getPrompt() + "\n";
		if(p.getResponse1() == null) {
			noResponse1 = true;
		}
		if(p.getResponse2() == null) {
			noResponse2 = true;
		}
		System.out.println(p.getPlayerid() + "'s prompt: ");
		System.out.println(p.getPrompt());
		if(!noResponse1) {
			System.out.println("1: " + p.getResponse1());
			m += "1: " + p.getResponse1() + "\n";
		}
		if(!noResponse2) {
			System.out.println("2: " + p.getResponse2());
			m += "2: " + p.getResponse2();
		}
		Utilities.allSendMessage(m);
		if(noResponse1 || noResponse2) {
			Player r;
			if(noResponse1 && !noResponse2) {
				if(i - 1 < 0) {
					r = players.get(players.size() - 1);
				}else{
					r = players.get(i-1);
				}
				String message = "Because of no response, " + r.getPlayerid() + " has won the round by default.";
				r.addScore();
				System.out.println(message);
				Utilities.allSendMessage(message);
			}else if (!noResponse1 && noResponse2) {
				if(i + 1 > players.size() - 1) {
					r = players.get(0);
				}else{
					r = players.get(i + 1);
				}
				String message = "Because of no response, " + r.getPlayerid() + " has won the round by default.";
				r.addScore();
				System.out.println(message);
				Utilities.allSendMessage(message);
			}else{
				String message = "No one responded to the question. No one gets points.";
				System.out.println(message);
				Utilities.allSendMessage(message);
			}
			vote(Main.voteNumber + 1);	
		}else{
			countdown = 21;
		}
	}

	public static void voteResults() {
		String msg = "1: " + vote1 + "\n";
		msg += "2: " + vote2 + "\n";
		if(vote1 > vote2) {
			Player r;
			if(voteNumber + 1 > players.size() - 1) {
				r = players.get(0);
			}else{
				r = players.get(voteNumber + 1);
			}
			msg += r.getPlayerid() + " has won the round!";
			r.addScore();
		}else if(vote2 > vote1) {
			Player r;
			if(voteNumber - 1 < 0) {
				r = players.get(players.size() - 1);
			}else{
				r = players.get(voteNumber - 1);
			}
			msg += r.getPlayerid() + " has won the round!";
			r.addScore();
		}else{
			msg += "Tie! No one gets points!";
		}
		
		System.out.println(msg);
		Utilities.allSendMessage(msg);
	}
	
	public static void gameResults() {
		
		final List<Integer> scores = new ArrayList<Integer>();
		for(int i = 0; i < players.size(); i++) {
			scores.add(players.get(i).getScore());
		}
		Collections.sort(scores);
		String msg = "";
		for(int i = 0; i < scores.size(); i++) {
			for(int i1 = 0; i1 < players.size(); i1++) {
				if(!players.get(i1).hasSorted) {
					if(players.get(i1).getScore() == scores.get(i)) {
						msg += numberSorted + ": " + players.get(i1).getPlayerid() + " - " + players.get(i1).getScore() + "\n";
						players.get(i1).setSorted(true);
						numberSorted++;
					}
				}
			}
		}
		System.out.println(msg);
		Utilities.allSendMessage(msg);
	}
	
}
