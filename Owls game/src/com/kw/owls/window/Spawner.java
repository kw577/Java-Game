package com.kw.owls.window;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.kw.owls.background.Cloud;
import com.kw.owls.background.TreeAutumn;
import com.kw.owls.framework.ObjectId;
import com.kw.owls.framework.STATE;
import com.kw.owls.objects.Block;
import com.kw.owls.objects.Player;

// klasa zajmuje sie generowaniem odpowiednich przeciwnikow i odpowiedniej ich ilosci w zaleznosci od rundy

public class Spawner {

	private Handler handler;
	private BackgroundHandler bcg_handler;
	private Game game;
	private Random rand = new Random();
	private String level_image_nr;

	
	
	private BufferedImage level = null;

	// Tekstury wykorzystywane w grze
	//
	//
	//
	//
	//
	//
	//
	//
	
	
	// konstruktor
	public Spawner(Handler handler, BackgroundHandler bcg_handler, Game game) {
		this.handler = handler;
		this.bcg_handler = bcg_handler;
		this.game = game;

	}
	
	public void tick() {
			
		
		
		if(game.getGameLevel() == 0 && game.gameState == STATE.Game) {
			game.setGameLevel(1);
			level_image_nr = "/wizard_level_" + game.getGameLevel() + ".png";
			
			BufferedImageLoader loader = new BufferedImageLoader();
			//level = loader.loadImage(level_image_nr);
			level = loader.loadImage("/Level_1.png"); 
		
			
			// zaladowanie mapy gry 
			
			
			
			loadLevel(level);
			
			handler.setDown(false);
			handler.setLeft(false);
			handler.setRight(false);
			handler.setUp(false);
			
		} 

		
	}
	
	// loading the level map 
	private void loadLevel(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		
		// na podstawie wczytanej mapki laduje sie obszar gry 
		for(int xx = 0; xx < w; xx++) {
			for(int yy = 0; yy < h; yy++) {
				int pixel = image.getRGB(xx, yy);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				//System.out.println("Red " + red + " green: " + green + " blue " + blue + "\n");
				if(red == 0 && green == 0 && blue == 0)
					handler.addObject(new Block(xx*32, yy*32, ObjectId.Block));
				
				if(green == 0 && red == 0 && blue == 255)
					handler.addObject(new Player(xx*32, yy*32, ObjectId.Player, handler));
				
				// generowanie elementow tla - chmury
				if(green == 255 && red == 0 && blue == 255) // kolor - cyan
					bcg_handler.addObject(new Cloud(xx*32, yy*32, ObjectId.Background, rand.nextInt(4) + 1)); // generowanie widoku chury - wybiera losowo jeden z 4 dostepnych rysunkow 
				
				// generowanie elementow tla - drzewa jesienne
				if(green == 255 && red == 255 && blue == 0) // kolor - zolty
					bcg_handler.addObject(new TreeAutumn(xx*32, yy*32 - 565, ObjectId.Background, rand.nextInt(2) + 1)); // generowanie widoku drzewa - wybiera losowo jeden z 2 dostepnych rysunkow 
				//// Uwaga !!! - wszystkie rysunki drzew powinny miec 600 px wysokosci - aby (yy*32 - 570) zawsze wyznaczylo poprawne polozenie drzewa nad poziomem terenu
				
			}
			System.out.println("\n\nSpawner: Loading map in progress\n\n");
		}
		
	//	handler.addObject(new HomingMissile(100, 100, 4, 0, ID.EnemyBullet, handler)); 
		
	}
		
		
	public void render(Graphics g) {

	}
		
		
	
}
