package dkeep.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class JGamePanel extends JPanel {
	
	private char[][] map;
	private Image hero = new ImageIcon("resources/hero.png").getImage();
	private Image herokey = new ImageIcon("resources/herokey.png").getImage();
	private Image heroarmed = new ImageIcon("resources/heroarmed.png").getImage();
	private Image ogre = new ImageIcon("resources/ogre.png").getImage();
	private Image ogrestuned = new ImageIcon("resources/ogrestuned.png").getImage();
	private Image ogreonkey = new ImageIcon("resources/ogreonkey.png").getImage();
	private Image ogreclub = new ImageIcon("resources/ogreclub.png").getImage();
	private Image guard = new ImageIcon("resources/guard.png").getImage();
	private Image guardsleep = new ImageIcon("resources/guardsleep.png").getImage();
	private Image key = new ImageIcon("resources/key.png").getImage();
	private Image dooropen = new ImageIcon("resources/dooropen.png").getImage();
	private Image doorclose = new ImageIcon("resources/doorclose.png").getImage();
	


	public JGamePanel() {
		super();
	}
	
	public void setMap(char[][] map) {
		this.map = map;
		repaint();
	}
	
	public char[][] getMap() {
		return map;
	}
	
	public void setChar(int x, int y, char ch) {
		map[y][x] = ch;
	}
	
	public void createMap(int x, int y) {
		this.map = new char[y][x];
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
				if((i == 0 || i == map.length - 1) || (j == 0 || j == map[i].length - 1)) {
					map[i][j] = 'X';
				}
				else {
					map[i][j] = ' ';
				}
			}
		}
	}
	
		
	public Set<Integer> acceptable() {
		Set<Integer> ret = new HashSet<Integer>();
		boolean hero = false, ogre = false, door = false, key = false;
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
				switch(map[i][j]) {
				case 'I':
					door = (i == 0 || j == 0 || i == (map[i].length - 1) || j == (map[i].length-1));
					break;
				case 'H':
					if(hero) ret.add(1);
					else hero = true;
					break;
				case 'O':
					if(ogre) ret.add(2);
					else ogre = true;
					break;
				case 'k':
					key = true;
					break;
				default:
					break;
		}}}
		return getSetErrors(ret, hero, ogre, key, door);
	}
	
	private Set<Integer> getSetErrors(Set<Integer> ret, boolean hero, boolean ogre, boolean key, boolean door){
		if(!hero) ret.add(1);
		if(!ogre) ret.add(2);
		if(!key) ret.add(3);
		if(!door) ret.add(4);
		return ret;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if (map == null) return;
		super.paintComponent(g);
		int addx = this.getWidth() / map[0].length;
		int addy = this.getHeight() / map.length;
		paintWallAndPath(g, addx, addy);
		paintHero(g, addx, addy);
		paintGuard(g, addx, addy);
		paintOgre(g, addx, addy);
		paintDoorsAndKey(g, addx, addy);
	}
	
	private void paintWallAndPath(Graphics g, int addx, int addy) {
		int x = 0, y = 0;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				char ch = map[i][j];
				if (ch == 'X') {
					g.setColor(Color.BLACK);
					g.fillRect(x, y, addx - 1, addy - 1);
				} else if (ch == ' ') {
					g.setColor(Color.WHITE);
					g.fillRect(x, y, addx - 1, addy - 1);
				}
				x += addx;
			}
			x = 0;
			y += addy;
		}
	}
	
	private void paintHero(Graphics g, int addx, int addy) {
		int x = 0, y = 0;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				char ch = map[i][j];
				if (ch == 'H')
					g.drawImage(hero, x, y, addx - 1, addy - 1, this);
				else if (ch == 'K')
					g.drawImage(herokey, x, y, addx - 1, addy - 1, this);
				else if (ch == 'A')
					g.drawImage(heroarmed, x, y, addx - 1, addy - 1, this);
				x += addx;
			}
			x = 0;
			y += addy;
		}
	}
	
	private void paintGuard(Graphics g, int addx, int addy) {
		int x = 0, y = 0;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				char ch = map[i][j];
				if(ch == 'G') 
					g.drawImage(guard, x, y, addx-1, addy-1, this);
				else if(ch == 'g') 
					g.drawImage(guardsleep, x, y, addx-1, addy-1, this);
				x += addx;
			}
			x = 0;
			y += addy;
		}
	}
	
	private void paintOgre(Graphics g, int addx, int addy) {
		int x = 0, y = 0;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				char ch = map[i][j];
				if(ch == 'O') 
					g.drawImage(ogre, x, y, addx-1, addy-1, this);
				else if(ch == '8') 
					g.drawImage(ogrestuned, x, y, addx-1, addy-1, this);
				else if(ch == '*') 
					g.drawImage(ogreclub, x, y, addx-1, addy-1, this);
				x += addx;
			}
			x = 0;
			y += addy;
		}
	}
	
	private void paintDoorsAndKey(Graphics g, int addx, int addy) {
		int x = 0, y = 0;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				char ch = map[i][j];
				if(ch == 'k') 
					g.drawImage(key, x, y, addx-1, addy-1, this);
				else if(ch == 'I') 
					g.drawImage(doorclose, x, y, addx-1, addy-1, this);
				else if(ch == 'S') 
					g.drawImage(dooropen, x, y, addx-1, addy-1, this);
				else if(ch == '$') 
					g.drawImage(ogreonkey, x, y, addx-1, addy-1, this);
				x += addx;
			}
			x = 0;
			y += addy;
		}
	}
}
