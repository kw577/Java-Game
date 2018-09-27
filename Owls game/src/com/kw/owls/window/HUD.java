package com.kw.owls.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.kw.owls.framework.SpriteSheet;

// klasa monitoruje i wyswietla stan parametrow gry - zdrowie gracza, ilosc punktow i.t.d

public class HUD {

	private Game game;
	private BufferedImage player_av = null;
	private BufferedImage flower_av = null;
	private SpriteSheet numbersSpriteSheet = null;
	private BufferedImage numbers = null;
	
	// Pomocniczo do generowania grafiki przedstawiajacej stan punktacji
	private int score_copy = 0;
	private int pom = 0;
	private int counter = 0; // zlicza ile cyfr ma liczba reprezentujaca ilosc punktow uzyskane przez gracza
	
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
		} catch (Exception e){
			e.printStackTrace();
		}
		
		numbersSpriteSheet = new SpriteSheet(numbers);
		
	}
	
	
	public void render(Graphics g) {
		
		//// wyswietlanie stanu zdrowia gracza w tym bloku
		g.drawImage(player_av, 0, 0, null);
		g.drawImage(numbersSpriteSheet.grabImage(3, 2, 35, 40), 60, 12, null); // znak "x" 
		
		if(game.getHealth() >= 0)
		g.drawImage(numbersSpriteSheet.grabImage(game.getHealth() + 1, 1, 35, 40), 100, 10, null); // wyswietla obrazek odpowiadajacy ilosci punktow zdrowia
		
		////
		
		
		// wyswietla ilosc zdobytych punktow 
		g.drawImage(flower_av, 725, 0, null);
		
		score_copy = game.getFlowers();
		while(score_copy > 0) {
			pom = score_copy % 10;
			g.drawImage(numbersSpriteSheet.grabImage(pom + 1, 1, 35, 40), 695 - counter*35, 10, null); // wyswietla obrazek odpowiadajacy ilosci punktow zdrowia
				
			score_copy = (int)(score_copy - pom)/10;
			counter++;
		}
		counter = 0;
	
		//
		
		
	}
	
	
}
