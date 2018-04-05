package dkeep.test;

import static org.junit.Assert.*;
import org.junit.Test;

import dkeep.logic.GameState;
import dkeep.logic.GameState.Movement;
import dkeep.logic.GameState.State;
import dkeep.logic.GameLevel;

public class TestDungeonGameLogicKeep {

	char [][] map = {{'X','X','X','X','X'},
			{'X','H',' ','O','X'},
			{'I',' ',' ',' ','X'},
			{'I','k',' ',' ','X'},
			{'X','X','X','X','X'}};
	
	boolean lever = false;
	
	@Test
	public void testHeroIsCaptureByOgre() {
		GameLevel game_level = new GameLevel(map, lever);
		GameState game = new GameState(game_level);
		game.giveHeroWeapon(false);
		assertFalse(game.isGameover());
		game.move_Hero(Movement.RIGHT);
		assertTrue(game.isGameover());
		assertEquals(State.DEFEAT, game.get_status());
	}
	
	@Test
	public void testHeroGetsKey() {
		GameLevel game_level = new GameLevel(map, lever);
		GameState game = new GameState(game_level);
		game.giveHeroWeapon(false);
		assertEquals('H', game.get_hero_char());
		game.move_Hero(Movement.DOWN);
		game.move_Hero(Movement.DOWN);
		assertTrue(game.check_hero(3, 1));
		assertEquals('K', game.get_hero_char());
	}
	
	@Test
	public void testHeroFailsToOpenDoor() {
		GameLevel game_level = new GameLevel(map, lever);
		GameState game = new GameState(game_level);
		game.giveHeroWeapon(false);
		assertEquals('H', game.get_hero_char());
		game.move_Hero(Movement.DOWN);
		assertTrue(game.check_hero(2, 1));
		game.move_Hero(Movement.LEFT);
		assertTrue(game.check_hero(2, 1));
		assertEquals('I', game_level.getChar(2, 0));
	}
	
	@Test
	public void testHeroSuccessToOpenDoor() {
		GameLevel game_level = new GameLevel(map, lever);
		GameState game = new GameState(game_level);
		game.giveHeroWeapon(false);
		assertEquals('H', game.get_hero_char());
		game.move_Hero(Movement.DOWN);
		game.move_Hero(Movement.DOWN);
		assertTrue(game.check_hero(3, 1));
		assertEquals('K', game.get_hero_char());
		assertEquals('I', game_level.getChar(3, 0));
		game.move_Hero(Movement.LEFT);
		assertTrue(game.check_hero(3, 1));
		assertEquals('S', game_level.getChar(3, 0));
	}
	
	@Test
	public void testHeroSuccessToExitDoor() {
		GameLevel game_level = new GameLevel(map, lever);
		GameState game = new GameState(game_level);
		game.move_Hero(Movement.DOWN);
		game.move_Hero(Movement.DOWN);
		game.move_Hero(Movement.LEFT);
		game.move_Hero(Movement.LEFT);
		assertTrue(game.check_hero(3, 0));
		assertTrue(game.isLevelEnd());
	}
	
	@Test
	public void testHeroStunesOgre() {
		GameLevel game_level = new GameLevel(map, lever);
		GameState game = new GameState(game_level);
		assertFalse(game.isGameover());
		game.setLvl(2);
		game.create_Level();
		game.move_Hero(Movement.RIGHT);
		assertFalse(game.isGameover());
		assertEquals(State.PLAYING, game.get_status());
	}
	
	@Test
	public void testStunedOgre() {
		GameLevel game_level = new GameLevel(map, lever);
		GameState game = new GameState(game_level);
		game.giveHeroWeapon(true);
		assertEquals('A', game.get_hero_char());
		assertFalse(game.isGameover());
		game.move_Hero(Movement.RIGHT);
		assertFalse(game.isGameover());
		assertEquals(State.PLAYING, game.get_status());
		assertEquals('8',game.get_ogre_char(game.get_ogres().get(0)));
	}
	

}