package me.kippy.quiplash;

public class Running implements Runnable {
	
	public void run() {
		
		while(true) {
			if(Main.gs == GameState.WRITEPROMPT) {
				Main.countdown --;
				if(Main.countdown % 10 == 0 || Main.countdown <= 3) {
					System.out.println("You have " + Main.countdown + " seconds left.");
					Utilities.allSendMessage("You have " + Main.countdown + " seconds left.");
				}
				
				if(Main.countdown <= 1) {
					Main.gs = GameState.WRITERESPONSE;
					System.out.println("Time is up!");
					Utilities.allSendMessage("Time is up!");
					Main.sendPrompts();
				}
			}else if(Main.gs == GameState.WRITERESPONSE) {
				Main.countdown --;
				if(Main.countdown % 10 == 0 || Main.countdown <= 3) {
					System.out.println("You have " + Main.countdown + " seconds left.");
					Utilities.allSendMessage("You have " + Main.countdown + " seconds left.");
				}
				
				if(Main.countdown <= 1) {
					Main.gs = GameState.VOTING;
					System.out.println("Time is up!");
					Utilities.allSendMessage("Time is up!");
					
					Main.vote(0);
						
				}
			}if(Main.gs == GameState.VOTING && Main.voteReady) {
				Main.countdown --;
				Main.voteReady = false;
				if(Main.countdown % 10 == 0 || Main.countdown <= 3) {
					System.out.println("You have " + Main.countdown + " seconds left.");
					Utilities.allSendMessage("You have " + Main.countdown + " seconds left.");
				}
				
				if(Main.countdown <= 1) {
	
					System.out.println("Time is up!");
					Utilities.allSendMessage("Time is up!");
					
					Main.voteResults();
					
					if(Main.voteNumber >= Main.players.size() - 1) {
						Main.gs = GameState.RESULTS;
						Main.gameResults();
					}else{
						Main.vote(Main.voteNumber + 1);
					}
					
					
				}else{
					Main.voteReady = true;
				}
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
