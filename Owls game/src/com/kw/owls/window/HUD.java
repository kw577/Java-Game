package com.kw.owls.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import com.kw.owls.framework.STATE;
import com.kw.owls.framework.SpriteSheet;

// klasa monitoruje i wyswietla stan parametrow gry - zdrowie gracza, ilosc punktow i.t.d

public class HUD extends MouseAdapter implements MouseMotionListener {

	private Game game;
	private BufferedImage player_av = null;
	private BufferedImage flower_av = null;
	private SpriteSheet numbersSpriteSheet = null;
	private BufferedImage numbers = null;
	
	private BufferedImage window_buttons = null;
	private SpriteSheet buttonsSpriteSheet = null;
	private BufferedImage textarea = null;	
	private BufferedImage daisy_av = null;	
	
	
	//Do podswietlania przyciskow menu
	private boolean userHooverYes = false;
	private boolean userHooverNo = false;
	
	// Pomocniczo do generowania grafiki przedstawiajacej stan punktacji
	private int score_copy = 0;
	private int pom = 0;
	private int counter = 0; // zlicza ile cyfr ma liczba reprezentujaca ilosc punktow uzyskane przez gracza 
	
	private int healthTimer = 35; // timer oznaczenia niskiego stanu punktow zdrowia - ramki wokol avataru gracza
	private int healthTimerStart = 35;
	private int healthAlert = -1;
	
	
	
	
	
	// Konstruktor
	public HUD(Game game) {
		
		this.game = game;
		
		// zaladowanie grafiki 
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			// siatka col x row = 44 x 50 px dla pliku numbers_spritesheet.png oraz 35 x 40 px dla pliku numbers_smaller_spritesheet.png
			numbers = loader.loadImage("/hud/numbers_smaller_spritesheet.png"); 
			player_av = loader.loadImage("/hud/player_avatar.png");
			flower_av = loader.loadImage("/hud/flower_avatar.png");
			
			
			
			window_buttons = loader.loadImage("/hud/window_buttons.png");
			textarea = loader.loadImage("/hud/textarea.png");
			daisy_av = loader.loadImage("/hud/daisy_av.png");
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		numbersSpriteSheet = new SpriteSheet(numbers);
		buttonsSpriteSheet = new SpriteSheet(window_buttons);
	}
	
	
	public void render(Graphics g) {
		
				
		// komunikat o niskim stanie zdrowia gracza - migajaca ramka wokol avataru
		if(game.getHealth() <= 1) {
			
			healthTimer--;
			if(healthTimer <= 0) {
				healthAlert *= -1;
				healthTimer = healthTimerStart;
			}
			
			if(healthAlert < 0){
			g.setColor(Color.red);
			g.fillOval(0, 0, 70, 70);
			}
		}
		
		
		
		//// wyswietlanie stanu zdrowia gracza w tym bloku
		g.drawImage(player_av, 4, 3, null);
		g.drawImage(numbersSpriteSheet.grabImage(3, 2, 35, 40), 70, 12, null); // znak "x" 
		
		if(game.getHealth() >= 0)
		g.drawImage(numbersSpriteSheet.grabImage(game.getHealth() + 1, 1, 35, 40), 100, 10, null); // wyswietla obrazek odpowiadajacy ilosci punktow zdrowia
		
		////
			
		
		if(game.getLostFlowers() > 0) {  
			Font font = new Font("Arial",Font.BOLD, 25);
			g.setFont(font);
		
			g.setColor(Color.red);
			g.drawString("-" + game.getLostFlowers(), 740, 85);
		}
		
		
		// wyswietla ilosc zdobytych punktow 
		g.drawImage(flower_av, 715, 0, null);
		
		score_copy = game.getFlowers();
		while(score_copy > 0) {
			pom = score_copy % 10;
			g.drawImage(numbersSpriteSheet.grabImage(pom + 1, 1, 35, 40), 685 - counter*35, 10, null); // wyswietla obrazek odpowiadajacy ilosci punktow zdrowia
				
			score_copy = (int)(score_copy - pom)/10;
			counter++;
		}
		counter = 0;
	
		//
		
		
		
		// wyswietla komunikat do nawiazania wspolpracy z sowka
		if(game.isAskingForHelp()) {
			
			g.drawImage(textarea, 190, 40, null);
			
			if(!userHooverYes)
				g.drawImage(buttonsSpriteSheet.grabImage(1, 1, 90, 60), 210, 130, null);
			else
				g.drawImage(buttonsSpriteSheet.grabImage(2, 1, 90, 60), 210, 130, null);
			
			
			if(!userHooverNo)
				g.drawImage(buttonsSpriteSheet.grabImage(1, 2, 90, 60), 500, 130, null);
			else
				g.drawImage(buttonsSpriteSheet.grabImage(2, 2, 90, 60), 500, 130, null);
			
			
			Font fnt = new Font("arial", 1, 20);
			g.setFont(fnt);
			g.setColor(Color.black);
			
			g.drawString("Do you want " + game.getOwlName() + " to help you", 250, 90);
			g.drawString("with searching flowers?", 285, 120);
			
			
			if(game.getOwlName() == "Daisy") {
				g.drawImage(daisy_av, 355, 140, null);
			}
			
	
			
			// stare okno - ZACHOWAC DO TESTOW !!
			//Font fnt2 = new Font("arial", 1, 20);
			//g.setFont(fnt2);
			
			//g.setColor(Color.red);
			
			//g.drawRect(200, 50, 400, 150);
						
			//g.setColor(Color.red);
			
			//g.drawString("Do you want " + game.getOwlName() + " to help you", 220, 80);
			//g.drawString("with searching flowers?", 220, 120);
					
			//g.drawRect(220, 140, 70, 40);
			//g.drawString("YES", 235, 170);
			
			//g.drawRect(510, 140, 70, 40);
			//g.drawString("NO", 525, 170);
		}
		
		if(!game.isAskingForHelp()) {
			this.userHooverNo = false;
			this.userHooverYes = false;
		}
		
	}
	
	
	
	public void mousePressed(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		
		if(game.isAskingForHelp()) {
			
				//Przycisk YES
				if(mouseOver(mx, my, 220, 140, 70, 40)) {
					game.setPlayerConfirm(true);
				}
		
				//Przycisk NO
				if(mouseOver(mx, my, 510, 140, 70, 40)) {
					game.setPlayerReject(true);
				}
		
		} 
		
	}
	
		
	public void mouseReleased(MouseEvent e) {
		
	}
	
	
	// Metoda interfejsu MouseMotionListener
		public void mouseMoved(MouseEvent e) {
			int mx = e.getX();
			int my = e.getY();
			
			if(game.isAskingForHelp()) {
				if(mouseOver(mx, my, 220, 140, 70, 40)) {
					userHooverYes = true;
					userHooverNo = false;
				} 
				else if(mouseOver(mx, my, 510, 140, 70, 40)) {
					userHooverYes = false;
					userHooverNo = true;
				} 
				else {
					userHooverYes = false;
					userHooverNo = false;
				}
			}		
			
		}
	
		
	// metoda sprawdzajaca czy kliknieto w jakis przycisk menu
	private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
		if((mx > x) && (mx < x + width)) {
			if((my > y) && (my < y + height)) {
				return true;
			} else return false;
		} else return false;
	}
	
	
	
	
}
