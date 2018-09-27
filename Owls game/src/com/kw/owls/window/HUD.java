package com.kw.owls.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

// klasa monitoruje i wyswietla stan parametrow gry - zdrowie gracza, ilosc punktow i.t.d

public class HUD {

	private Game game;

	// Konstruktor
	public HUD(Game game) {
		
		this.game = game;
	}
	
	
	public void render(Graphics g) {
		g.setColor(Color.blue);
		
		Font font = new Font("Jokerman",Font.BOLD, 25);
		g.setFont(font);
		
		 int ter = 123;
		
		g.drawString("Health: x" + game.getHealth(), 35, 40);
		g.drawString("Score: x" + game.getFlowers(), 35, 80);
		System.out.println("\n Obliczenia: ");
		int x = 110;
		int y = 40;
		int temp = game.getHealth();
		int counter = 0;
		
		/*
		while(temp > 0) {
			temp--;
			g.drawImage(health_image, x, y, null);
			counter++;
			x += 24;
			if (counter >= 5) {
				counter = 0;
				y += 24;
				x = 110;
			}
			
		}
		*/
		
	}
	
	
	
	
}
