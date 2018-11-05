package com.kw.owls.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Random;
import com.kw.owls.framework.GameObject;
import com.kw.owls.framework.ObjectId;
import com.kw.owls.window.BufferedImageLoader;
import com.kw.owls.window.Game;
import com.kw.owls.window.Handler;


// jest przeciwnikiem - niegrozna dla gracza ale moze mu zabrac kilka kwiatkow - utrata punktow 
public class Missile extends GameObject{

    private int width = 16;
    private int height = 16;
    
    private Handler handler;
    private Game game;
    
    private float gravity = 0.1f;
    private final int max_fall_velocity = 15;
    
    private int timer = 0; // czas po jakim pocisk nabiera predkosci velY > 0
    private final int fallingTimer = 40;
    
    private BufferedImage level_map;
    
   
    private Random rand;
    
    // do obliczen kolizji - wsystkie obliczenia kolizji sa przeprowadzane dla obiektow z wycinka mapy 5x5 kafelkow wokol Pocisku 
    int[][] surrunding_area = new int[5][5]; // pomocniczo do sprawdzenia poprawnosci ustalenia otaczajacego terenu
    public LinkedList<Rectangle> miniHandler = new LinkedList<Rectangle>(); // przechowuje liste obiektow w zeskanowanym wycinku terenu
    
    private int check_collision_timer = 1; // aby zmniejszyc ilosc obliczen - kolizja bedzie sprawdzana co drugi cykl
    
    
	// kosntruktor
	public Missile(int x, int y, ObjectId id, Handler handler, Game game, float velX, float velY) {
		super(x, y, id);
		this.handler = handler;
		this.game = game;
		this.velX = velX;
		this.velY = velY;
		
		rand = new Random();
		
		 BufferedImageLoader loader = new BufferedImageLoader();
	     
	     
	     try {
	            
	         level_map = loader.loadImage("/levels/Level_" + game.getGameLevel() + ".png");
	        	         
	         
	     } catch (Exception e){
	         e.printStackTrace();
	     }
		
   
	     
	     
	}
	

	public void tick() {
		
		// ustalanie predkosci
		if(this.timer < this.fallingTimer) timer++;
		
		// po pewnym czasie pocisk zaczyna spadac
		if(this.timer >= this.fallingTimer) 
			velY += gravity;
		
		if(velY > max_fall_velocity) velY = max_fall_velocity;
		
		System.out.println("\nMissile velX: " + velX + "   velY: " + velY);
		//
		
		
		x += velX;
		y += velY;
		
		// granice planszy - musza byc ustawione gdyz jesli obiekt zblizy sie do granicy planszy
        // funkcja check_terrain bedzie chciala pobrac z mapy piksele o wartosci ujemnej co wyrzuci blad
        // zamiast ustalania granic w ponizszy sposob mozna by tez modyfikowac funkcje check_terrain
		if(x < 70) handler.removeMissile(this);
        if(x > 15800) handler.removeMissile(this);
		
        //
        
        
        
        
        //  !!!!!!!! jezeli zbliza sie do granicy planszy nalezy usunac z handlera ??
        
        
        
        
        
        //
        
        
		
		//System.out.println("Missile - wsp. X: " + this.x + "    wsp. y: " + this.y);
		
		this.check_collision_timer--;
		if(check_collision_timer <= 0){ // funckja kolizji sprawdzana co drugi cykl aby zmniejszyc ilosc obliczen
			collision();
			this.check_collision_timer = 1;
		}
		
				
	}



	private void collision() {
		// TODO Auto-generated method stub
		this.check_terrain((int)((this.x+16)/32 - 2), (int)((this.y+16)/32 - 2), 5, 5); // +16  - do srodka kafelka terenu (kafelki maja rozmiar 32x32)
	    
		   /*
	       System.out.println("Missile - Tabela terenu otaczajacego");
	       for(int xx = 0; xx < 5; xx++) {
	           for(int yy = 0; yy < 5; yy++) {
	               System.out.print(surrunding_area[yy][xx] + " ");
	           }
	           System.out.println("");
	       }
		   */
		
	       //System.out.println("\nPrzed sprawdzeniem kolizji w miniHandler jest el: " + miniHandler.size());
		for(int i = 0; i < miniHandler.size(); i++) {
			Rectangle tempObject = miniHandler.get(i);
			
					
			// !!!! usuwanie pocisku po uderzeniu w sciane
			
			if(getBounds().intersects(tempObject)) { // jesli obiekt stoi na jakims bloku
				
				handler.removeMissile(this);
			}
			
		}
		// pozakonczeniu iteracji usuwamy zawartosc miniHandler
		miniHandler.clear();
		
		//System.out.println("\nPo sprawdzeniu kolizji w miniHandler jest el: " + miniHandler.size() + "\n\n\n");
		
	}

	
	
	// loading the level map (start_x, start_y) - wspolrzedne pixela od ktorego zacyna sie przeszukiwanie mapy, amount_columns - ilosc kolumn pixeli
    private void check_terrain(int start_x, int start_y, int amount_columns, int amount_rows) {
        //int w = level_map.getWidth();
        //int h = level_map.getHeight();
     
        //System.out.println("\nPixel start: " + start_x + " " + start_y);
     
        // na podstawie wczytanej mapki laduje sie obszar gry
        for(int xx = 0; xx < amount_columns; xx++) {
            for(int yy = 0; yy < amount_rows; yy++) {
                int pixel = level_map.getRGB(start_x + xx, start_y + yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
             
                // Do miniHandler dodaje jedynie: Block, Ground, Rocks
                // Block
                if(red == 0 && green == 255 && blue == 0) { 
                	surrunding_area[xx][yy] = 1;
                	miniHandler.add(new Rectangle((int) ((start_x + xx)*32), (int) ((start_y + yy)*32), 32, 32));
                	//System.out.println("Dodano prostokat - wsp. x: " + ((start_x + xx)*32) + "    wsp. y: " + ((start_y + yy)*32));
                }
                // Ground    
                else if(red == 0 && green == 0 && blue == 0) {
                	surrunding_area[xx][yy] = 1;
                	miniHandler.add(new Rectangle((int) (start_x + xx)*32, (int) ((start_y + yy)*32), 32, 32));
                }
                    
                // Rocks
                else if(red == 200 && green == 200 && blue == 200) {
                	surrunding_area[xx][yy] = 1;
                	miniHandler.add(new Rectangle((int) (start_x + xx)*32, (int) ((start_y + yy)*32), 32, 32));
                }
                    
                else if(green == 255 && red == 170 && blue == 255) // WaterSurface - nie wchodzi w kolizje z obiektem Sheep - nie dodaje nowego obiektu do miniHandler
                    surrunding_area[xx][yy] = 1; 
                else if(green == 150 && red == 110 && blue == 190) // WaterMid
                    surrunding_area[xx][yy] = 1;
                else if(green == 90 && red == 50 && blue == 120) // WaterDeep
                    surrunding_area[xx][yy] = 1;
                else
                    surrunding_area[xx][yy] = 0;
         
                
            }
 
        }
     
        //System.out.println("\nW miniHandler jest el: " + miniHandler.size());
       
    }
	
	// metody dodawania i usuwania obiektow z listy przechowujacej elementy otaczajacego terenu dla obiektu Sheep
    public void addObject(Rectangle rect) {
		this.miniHandler.add(rect);
	}
	
	
	public void removeObject(Rectangle rect) {
		this.miniHandler.remove(rect);
	}
	
	public void clearHandler() {
		this.miniHandler.clear();	
	
	}
    
    
	

	public void render(Graphics g) {
	
			    
		 g.setColor(Color.gray);
		 g.fillOval((int)x, (int)y, width, height);
	     
	     Graphics2D g2d = (Graphics2D) g;
	     //g.setColor(Color.RED);
		

	     //g2d.draw(getBounds());
	     
	     /*
	     // rysowanie prostokatow z miniHandler - ktore odzwierciedlaja przylegly teren
	     // !!!! UWAGA - aby rysowanie dzialalo nalezy usunac komende miniHandler.clear() z funckji collision() (miniHandler jest czyszczony przed rozpoczeciem funkcji render)
	     for(int i = 0; i < miniHandler.size(); i++) {
				Rectangle tempObject = miniHandler.get(i);
				
				g2d.draw(tempObject);
	     }
	     
	     miniHandler.clear();
	     */
	}

	
	
	// do wykrywania utraty punktow gry przez gracza
	 	public Rectangle getBounds() {
	 		
	 		 return new Rectangle((int)(x), (int)(y), width, height); // mozna by tez zwrocic prostokat w zupelnie dowolnym miekscu planszy poza obszarem gry
	 	}

	
}
