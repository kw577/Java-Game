package com.kw.owls.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.kw.owls.framework.STATE;
import com.kw.owls.framework.SpriteSheet;

public class Menu extends MouseAdapter implements MouseMotionListener {

	private Game game;
	private Handler handler;

	
	//Textury uzywane w menu gry
	private BufferedImage buttons = null;
	private SpriteSheet buttonsSpriteSheet = null;
	private BufferedImage autumn_tree = null;
	private BufferedImage clouds = null;
	private BufferedImage flowers = null;	
	private BufferedImage owl = null;	
	
	//Do podswietlania przyciskow menu
	private boolean userHooverPlay = false;
	private boolean userHooverHelp = false;
	private boolean userHooverExit = false;
				
	private boolean GraphicsLoaded = false; // zmienna z informacja czy zostaly zaladowane grafiki gry
	
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
					game.gameState = STATE.LoadLevel;
					
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
	
		}  else if(game.gameState == STATE.End) {
			
			// Przycisk powrotu do menu gry 
			if(mouseOver(mx, my, 300, 350, 200, 64)) {
				game.gameState = STATE.Menu;
				
			} 

		} else if(game.gameState == STATE.Defeat) {
			
			// Przycisk powrotu do menu gry 
			if(mouseOver(mx, my, 300, 350, 200, 64)) {
				game.gameState = STATE.Menu;
				
			} 

		} 
		
	}
	
	// Metoda interfejsu MouseMotionListener
	public void mouseMoved(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		
		if(game.gameState == STATE.Menu) {
			if(mouseOver(mx, my, 300, 150, 200, 64)) {
				userHooverPlay = true;
				userHooverHelp = false;
				userHooverExit = false;
			} 
			else if(mouseOver(mx, my, 300, 250, 200, 64)) {
				userHooverPlay = false;
				userHooverHelp = true;
				userHooverExit = false;
			} 
			else if(mouseOver(mx, my,  300, 350, 200, 64)) {
				userHooverPlay = false;
				userHooverHelp = false;
				userHooverExit = true;
			}
			else {
				userHooverPlay = false;
				userHooverHelp = false;
				userHooverExit = false;
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
		
		// pobranie grafik
		if(!GraphicsLoaded) {
			BufferedImageLoader loader = new BufferedImageLoader();
						
			try {
				buttons = loader.loadImage("/textures/buttons_spritesheet.png");
				autumn_tree = loader.loadImage("/background/tree_autumn_2.png");
				clouds = loader.loadImage("/background/cloud_1.png");
				flowers = loader.loadImage("/background/flowers_1.png");
				owl = loader.loadImage("/background/owl_1.png");
			} catch (Exception e){
				e.printStackTrace();
			}
						
			buttonsSpriteSheet = new SpriteSheet(buttons);
		}
		
		
		g.setColor(new Color(190, 220, 235));
		g.fillRect(0, 0, game.getWidth(), game.getHeight());
		
		g.setColor(Color.white);
		//g.fillRect(170, 450, 70, 40);
		g.fillOval(190, 450, 95, 95);
		
		g.setColor(new Color(130, 180, 0));
		g.fillOval(-300, 500, 1600, 300);
				
		
		if(game.gameState == STATE.Menu) {
			
			g.drawImage(autumn_tree, -70, -70, null);
			g.drawImage(clouds, 550, 50, null);
			g.drawImage(clouds, 200, 0, null);
			g.drawImage(flowers, 500, 350, null);
			g.drawImage(owl, 180, 420, null);
			
			
			Font fnt = new Font("arial", 1, 50);
			Font fnt2 = new Font("arial", 1, 30);
			g.setFont(fnt);
							
			//g.setColor(Color.red);
			//g.drawString("Menu", 330, 70);
						
			
			if(!userHooverPlay)
				g.drawImage(buttonsSpriteSheet.grabImage(1, 1, 220, 84), 290, 140, null);
			else
				g.drawImage(buttonsSpriteSheet.grabImage(2, 1, 220, 84), 290, 140, null);
			
			
			//g.setFont(fnt2);
			//g.drawRect(300, 150, 200, 64);
			//g.drawString("Play", 360, 190);
					
			
			if(!userHooverHelp)
				g.drawImage(buttonsSpriteSheet.grabImage(1, 2, 220, 84), 290, 235, null);
			else
				g.drawImage(buttonsSpriteSheet.grabImage(2, 2, 220, 84), 290, 235, null);
						
			
			//g.drawRect(300, 250, 200, 64);
			//g.drawString("Help", 360, 290);
			
			
			if(!userHooverExit)
				g.drawImage(buttonsSpriteSheet.grabImage(1, 3, 220, 84), 290, 332, null);
			else
				g.drawImage(buttonsSpriteSheet.grabImage(2, 3, 220, 84), 290, 332, null);
					
			//g.drawRect(300, 350, 200, 64);
			//g.drawString("Quit", 360, 390);
						
			
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
			g.drawString("-> Good luck :) :) :) :) !!!", 50, 330);
			g.setFont(fnt2);
			g.drawRect(300, 350, 200, 64);
			g.drawString("Back", 360, 390);
						
		} else if(game.gameState == STATE.End) {
			Font fnt = new Font("arial", 1, 50);
			g.setFont(fnt);
			Font fnt2 = new Font("arial", 1, 30);
			Font fnt3 = new Font("arial", 1, 20);
			g.setColor(Color.white);
			g.drawString("VICTORY", 330, 70);
			
			g.setFont(fnt3);
			g.drawString("-> Your Score: " + game.getFlowers(), 50, 170);

			g.setFont(fnt2);
			g.drawRect(300, 350, 200, 64);
			g.drawString("Menu", 360, 390);
						
		} else if(game.gameState == STATE.Defeat) {
			Font fnt = new Font("arial", 1, 50);
			g.setFont(fnt);
			Font fnt2 = new Font("arial", 1, 30);
			Font fnt3 = new Font("arial", 1, 20);
			g.setColor(Color.white);
			g.drawString("You lose", 330, 70);
			
			g.setFont(fnt3);
			g.drawString("-> Your Score: " + game.getFlowers(), 50, 170);

			g.setFont(fnt2);
			g.drawRect(300, 350, 200, 64);
			g.drawString("Menu", 360, 390);
						
		} 
		
	}
	
}
