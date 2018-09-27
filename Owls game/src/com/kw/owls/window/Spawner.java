package com.kw.owls.window;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.kw.owls.background.Cloud;
import com.kw.owls.background.TreeAutumn;
import com.kw.owls.framework.GameObject;
import com.kw.owls.framework.ObjectId;
import com.kw.owls.framework.STATE;
import com.kw.owls.objects.Block;
import com.kw.owls.objects.Player;
import com.kw.owls.objects.TexturesManager;

// klasa zajmuje sie generowaniem odpowiednich przeciwnikow i odpowiedniej ich ilosci w zaleznosci od rundy

public class Spawner {

	private Handler handler;
	private BackgroundHandler bcg_handler;
	private Game game;
	private Random rand = new Random();
	private String level_image_nr;
	private TexturesManager textures;
	
	// Timery ekranu ladowania gry - minimalny czas wyswietlania ekranu przejscia miedzy rundami
	private int loading_timer = 150;
	private int loading_timer_start = 150;
	
	private BufferedImage level = null;
	

	// konstruktor
	public Spawner(Handler handler, BackgroundHandler bcg_handler, Game game) {
		this.handler = handler;
		this.bcg_handler = bcg_handler;
		this.game = game;
		
		// zastosowanie klasy TexturesManager pozwala na zwiekszenie szybkosci ladowania gry
		textures = new TexturesManager();		
	}
	
	
	
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(0, 0, game.getWidth(), game.getHeight());
	}




	public void checkLevel() {
		// Sprawdza czy zakonczyla sie biezaca runda - ??? np po przejsciu do konca planszy ???
		
		
		// Testowo - runda zmienia sie gdy gracz przejdzie do konca planszy
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ObjectId.Player) {
				if(tempObject.getX() >= 15500 && game.getGameLevel() < 4) {
					game.gameState = STATE.LoadLevel;
					// clear haandler
				}
				
				
				// zakonczenie - przejscie calej gry
				if(tempObject.getX() >= 15500 && game.getGameLevel() == 4) {
					game.gameState = STATE.End;
					
					//Zerowanie stanu gry
					game.setGameLevel(0);
					game.setHealth(game.getMaxHealth());
					game.setFlowers(0);
					
					
					// usuwanie poprzedniej rundy
					handler.clearHandler();	
					bcg_handler.clearHandler();
				}
				
				
			}
			
		
	
			
		}
	}


	

	public void load() {
		// Loading runds
		if(game.getGameLevel() == 0 && loading_timer == loading_timer_start) {
			game.setGameLevel(1);
			level_image_nr = "/levels/Level_" + game.getGameLevel() + ".png";
			
			BufferedImageLoader loader = new BufferedImageLoader();
			level = loader.loadImage(level_image_nr);

		
			
			// zaladowanie mapy gry 
			

			loadLevel(level);
			
			// aby nie zostal zapamietany ruch gracza z poprzedniej rundy - po kazdym "if" lub na koncu tak jak tu zastosowano
			//handler.setDown(false);
			//handler.setLeft(false);
			//handler.setRight(false);
			//handler.setUp(false);
			
			
			//game.gameState = STATE.Game; //mozna dodawac ten zapis po kazdym "if" lub na koncu tak jak tu zastosowano
		}
		else if(game.getGameLevel() == 1 && loading_timer == loading_timer_start) {
			game.setGameLevel(2);
			
			// ustawienie poziomu zdrowia przy rozpoczeciu kolejnej rundy
			game.setHealth(game.getMaxHealth());
			
			handler.clearHandler();	
			bcg_handler.clearHandler();
			
			level_image_nr = "/levels/Level_" + game.getGameLevel() + ".png";
			
			BufferedImageLoader loader = new BufferedImageLoader();
			level = loader.loadImage(level_image_nr);

		
			
			// zaladowanie mapy gry 
			

			loadLevel(level);
			
			
			//game.gameState = STATE.Game;
		}
		else if(game.getGameLevel() == 2 && loading_timer == loading_timer_start) {
			game.setGameLevel(3);
			
			// ustawienie poziomu zdrowia przy rozpoczeciu kolejnej rundy
			game.setHealth(game.getMaxHealth());
			
			// usuwanie poprzedniej rundy
			handler.clearHandler();	
			bcg_handler.clearHandler();
			
			level_image_nr = "/levels/Level_" + game.getGameLevel() + ".png";
			
			BufferedImageLoader loader = new BufferedImageLoader();
			level = loader.loadImage(level_image_nr);

		
			
			// zaladowanie mapy gry 
			

			loadLevel(level);
			
			
			//game.gameState = STATE.Game;
		}
		else if(game.getGameLevel() == 3 && loading_timer == loading_timer_start) {
			game.setGameLevel(4);
			
			// ustawienie poziomu zdrowia przy rozpoczeciu kolejnej rundy
			game.setHealth(game.getMaxHealth());
			
			// usuwanie poprzedniej rundy
			handler.clearHandler();	
			bcg_handler.clearHandler();
			
			level_image_nr = "/levels/Level_" + game.getGameLevel() + ".png";
			
			BufferedImageLoader loader = new BufferedImageLoader();
			level = loader.loadImage(level_image_nr);

		
			
			// zaladowanie mapy gry 
			

			loadLevel(level);
			
			
			//game.gameState = STATE.Game;
		}
		
		// aby ekran przyjcia miedzy rundami trwal ustalona ilosc czasu  
		loading_timer--;
		if(loading_timer <= 0) {
			loading_timer = loading_timer_start;
			
			// aby nie zostaly zapamietane ruchy z poprzedniej rundy
			handler.setDown(false);
			handler.setLeft(false);
			handler.setRight(false);
			handler.setUp(false);
			
			game.gameState = STATE.Game;
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
					handler.addObject(new Block(xx*32, yy*32, ObjectId.Block, textures));
				
				if(green == 0 && red == 0 && blue == 255)
					handler.addObject(new Player(xx*32, yy*32, ObjectId.Player, handler, game));
				
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
		
		

		
		
	
}
