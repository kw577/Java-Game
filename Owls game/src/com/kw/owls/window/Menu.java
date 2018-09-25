package com.kw.owls.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import com.kw.owls.framework.STATE;

public class Menu extends MouseAdapter{

	private Game game;
	private Handler handler;

	private Random r = new Random();
	
	public Menu(Game game, Handler handler) {
		this.game = game;
		this.handler = handler;

	}

	public void mousePressed(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		
		if(game.gameState == STATE.Menu) {
			
				
				// Przycisk Play
				if(mouseOver(mx, my, 300, 150, 200, 64)) {
					game.gameState = STATE.Game;
					
				}
		
				
				
				// Przycisk Help
				if(mouseOver(mx, my, 300, 250, 200, 64)) {
					game.gameState = STATE.Help;
					
				}
				
				// Przycisk Quit
				if(mouseOver(mx, my, 300, 350, 200, 64)) {
					System.exit(1);	
				}
				
				
		
		} else if(game.gameState == STATE.Help) {
				
			// Przycisk Back
			if(mouseOver(mx, my, 300, 350, 200, 64)) {
				game.gameState = STATE.Menu;
				
			}
	
		}  
		
	}
	
	
	public void mouseReleased(MouseEvent e) {
		
	}
	
	
	
	
	// metoda sprawdzajaca czy kliknieto w jakis przycisk menu
	private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
		if((mx > x) && (mx < x + width)) {
			if((my > y) && (my < y + height)) {
				return true;
			} else return false;
		} else return false;
	}
	
	public void tick() {
		
			
	}
	
	public void render(Graphics g) {
		
		g.setColor(new Color(190, 220, 235));
		g.fillRect(0, 0, game.getWidth(), game.getHeight());
		
				
		g.setColor(new Color(130, 180, 0));
		g.fillOval(-300, 500, 1600, 300);
		
		
		if(game.gameState == STATE.Menu) {
			Font fnt = new Font("arial", 1, 50);
			Font fnt2 = new Font("arial", 1, 30);
			g.setFont(fnt);
			
			g.setColor(Color.white);
			g.drawString("Menu", 330, 70);
			
			g.setFont(fnt2);
			g.drawRect(300, 150, 200, 64);
			g.drawString("Play", 360, 190);
					
			g.drawRect(300, 250, 200, 64);
			g.drawString("Help", 360, 290);
			
			g.drawRect(300, 350, 200, 64);
			g.drawString("Quit", 360, 390);
			

			
			
		} else if(game.gameState == STATE.Help) {
			Font fnt = new Font("arial", 1, 50);
			g.setFont(fnt);
			Font fnt2 = new Font("arial", 1, 30);
			Font fnt3 = new Font("arial", 1, 20);
			g.setColor(Color.white);
			g.drawString("Help", 330, 70);
			
			g.setFont(fnt3);
			g.drawString("-> Use arrow keys <- and -> to move", 50, 170);
			g.drawString("-> Use spacebar to jump", 50, 210);
			g.drawString("-> Shoot with alt key", 50, 250);
			g.drawString("-> Collect as much coins as you can find", 50, 290);
			g.drawString("-> Good luck :) :) :) !!", 50, 330);
			g.setFont(fnt2);
			g.drawRect(300, 350, 200, 64);
			g.drawString("Back", 360, 390);
						
		} 
		
	}
}
