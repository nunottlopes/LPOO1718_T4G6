package dkeep.cli;
import java.util.Scanner;

import dkeep.logic.GameState;

public class UserInput {

	private Scanner s;
	
	public UserInput() {
		s = new Scanner(System.in);
	}
	
	public UserInput(Scanner scan) {
		s = scan;
	}
	
	public boolean readInput(GameState game) {
		System.out.print("\nEnter direction: ");
		
		char option = s.next().charAt(0);
		
		switch(option) {
		case 'w': 
			game.nextMove("up");
			return true;
		case 'a':
			game.nextMove("left");
			return true;
		case 's':
			game.nextMove("down");
			return true;
		case 'd':
			game.nextMove("right");
			return true;
		default:
			return false;				
		}
	}
	
	public void close_scanner() {
		s.close();
	}
}
