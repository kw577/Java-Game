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
public class Sheep extends GameObject{

    private int width = 70;
    private int height = 54;
    
    private Handler handler;
    private Game game;
    
    private float gravity = 0.2f;
    private final int max_fall_velocity = 15;
    
    private BufferedImage level_map;
    private BufferedImage sheep = null;
    
    // do ustalenie kierunku ruchu
    private Random rand;
    private int direction_timer = 0; // generowana losowo liczba okreslajaca jak dlugo obiekt porusza sie w danym kierunku
    private int choose_direction; // generowana losowo - do wyboru kierunku poruszania sie
    
    // do obliczen kolizji - wsystkie obliczenia kolizji sa przeprowadzane dla obiektow z wycinka mapy 7x7 kafelkow wokol Owcy 
    int[][] surrunding_area = new int[7][7]; // pomocniczo do sprawdzenia poprawnosci ustalenia otaczajacego terenu
    public LinkedList<Rectangle> miniHandler = new LinkedList<Rectangle>(); // przechowuje liste obiektow w zeskanowanym wycinku terenu
    
    private int check_collision_timer = 1; // aby zmniejszyc ilosc obliczen - kolizja bedzie sprawdzana co drugi cykl
    
    
    // do odliczania odleglosci od gracza
    private float dx1;
    private float dy1;
    private int dist;
    private int check_dist_timer = 0; // co 10 cykli bedzie sprawdzana odleglosc do gracza (czas rekcji owcy) - 10 ustawiane w funckji set_direction
    
    private boolean chasing_player = false; // sprawdza czy owca biegnie za graczem
    
	// kosntruktor
	public Sheep(int x, int y, ObjectId id, Handler handler, Game game) {
		super(x, y, id);
		this.handler = handler;
		this.game = game;
		
		rand = new Random();
		
		 BufferedImageLoader loader = new BufferedImageLoader();
	     
	     
	     try {
	            
	         level_map = loader.loadImage("/levels/Level_" + game.getGameLevel() + ".png");
	         sheep = loader.loadImage("/sheep_1.png");
	         
	         
	     } catch (Exception e){
	         e.printStackTrace();
	     }
		
	}
	

	public void tick() {
		
		choose_direction();
		
		
		
		x += velX;
		y += velY;
		
		// granice planszy - musza byc ustawione gdyz jesli obiekt zblizy sie do granicy planszy
        // funkcja check_terrain bedzie chciala pobrac z mapy piksele o wartosci ujemnej co wyrzuci blad
        // zamiast ustalania granic w ponizszy sposob mozna by tez modyfikowac funkcje check_terrain
		if(x < 100) x = 100;
        if(x > 15800) x = 15800;
		
		
		//System.out.println("Sheep - wsp. X: " + this.x + "    wsp. y: " + this.y);
		
		this.check_collision_timer--;
		if(check_collision_timer <= 0){ // funckja kolizji sprawdzana co drugi cykl aby zmniejszyc ilosc obliczen
			collision();
			this.check_collision_timer = 1;
		}
		
		
		
	}


	private void choose_direction() {
		
		// Obliczanie odleglosci od gracza
				check_dist_timer--;
				
				if(check_dist_timer <= 0) {
					
					dx1 = this.x - handler.getPlayer_x();
				    dy1 = this.y - handler.getPlayer_y();
				 
				    dist = (int) (Math.sqrt(dx1 * dx1 + dy1 * dy1));
					
				    
				    check_dist_timer = 15;
				}
			    
				////////////////////////////////////////
				// Poruszanie sie gdy w poblizu jest gracz
				if(dist < 300 && dist > 60 && dy1 > 30 && dy1 < 50) // gracz znajduje sie w bliskiej odleglosci na tej samej wysokosci terenu
				{
					System.out.println("Odleglosc od gracza:   dx1 = " + dx1 + "    dy1 = " + dy1 + "     dist = " + dist + "   opcja 1");
								
					this.chasing_player = true;
					
					if(dx1 > 0) velX = -3;
					else velX = 3;
				}
				else if(dist <= 60 && dy1 > 30 && dy1 < 50) {
					System.out.println("Odleglosc od gracza:   dx1 = " + dx1 + "    dy1 = " + dy1 + "     dist = " + dist + "   opcja 2");
					
					this.chasing_player = true;
					
					velX = 0;
				}
				else {
					
					this.chasing_player = false;
					////////////////////////////////////////
					// Poruszanie sie gdy w poblizu nie ma gracza
						
					this.direction_timer--;
					if(this.direction_timer <= 0) {
						System.out.println("Odleglosc od gracza:   dx1 = " + dx1 + "    dy1 = " + dy1 + "     dist = " + dist + "   opcja 3");
						
						// losujemy czas w ktorym owca bedzie poruszac sie w danym kierunku
						this.direction_timer = (rand.nextInt(5) + 3) * 100;
						System.out.println("\n\n\nSheep  -  prev velX: " + velX + "      prev velY: " + velY);
						System.out.println("\nUstalono direction_timer:        " + this.direction_timer);
						
						this.choose_direction = rand.nextInt(2);
						System.out.println("\nchoose_direction:        " + this.choose_direction);
						
						// jesli wczesniej owca stala w miejscu
						if(this.velX == 0) {
							if(this.choose_direction == 0)
								velX = 0.8f;
							else
								velX = -0.8f;
						}
						
						// jesli wczesniej owca poruszala sie
						else if(this.velX != 0) {
							if(this.choose_direction == 0)
								velX = 0;
							else
								velX *= -1;  // zmiana kierunku poruszania sie na przeciwny
						}
						
					}
					//System.out.println("\ndirection_timer: " + this.direction_timer);
				
				}
				
				
				
				///////////////////////
				
				velY += gravity;
				
				
				// maksymalna szybkosc spadania
				if(velY > this.max_fall_velocity) 
					velY = this.max_fall_velocity;
				
				
	}


	private void collision() {
		// TODO Auto-generated method stub
		this.check_terrain((int)((this.x+16)/32 - 2), (int)((this.y+16)/32 - 2), 7, 7);
	    
		   /*
	       System.out.println("Sheep - Tabela terenu otaczajacego");
	       for(int xx = 0; xx < 7; xx++) {
	           for(int yy = 0; yy < 7; yy++) {
	               System.out.print(surrunding_area[yy][xx] + " ");
	           }
	           System.out.println("");
	       }
		   */
		
		
		for(int i = 0; i < miniHandler.size(); i++) {
			Rectangle tempObject = miniHandler.get(i);
			
						
				if(getBoundsBottom().intersects(tempObject)) { // jesli obiekt stoi na jakims bloku
					
					y = (float)(tempObject.getY() - height); // nalezy tak dobrac wspolrzedna y aby player odbijal sie od bloku
					
					velY = 0;
				} 
				
				if(getBoundsTop().intersects(tempObject)) { // jesli obiekt uderzy w jakis blok przy poruszaniu sie
					
					if(!this.chasing_player) {
						  this.x -= 3*velX;
			                 
		                  this.velX *= -1;
					}
                    
					else if(this.chasing_player) { // gdy sciga gracza
						  this.x -= 1*velX;
			                 
		                  this.velX = 0;
					}
  
					
				}
			
		}
		// pozakonczeniu iteracji usuwamy zawartosc miniHandler
		miniHandler.clear();
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
		 //g.setColor(Color.yellow);
		 //g.fillRect((int)x, (int)y, width, height);
		
		 g.drawImage(sheep, (int)x, (int)y, null);
		
		 //g.setColor(Color.green);
		 //g.fillOval((int)x, (int)y, 5, 5);
	     
	     Graphics2D g2d = (Graphics2D) g;
	     g.setColor(Color.RED);
		
	     g2d.draw(getBoundsTop());
	     g2d.draw(getBoundsBottom());
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
		
		return new Rectangle((int)x, (int)y, width, height);
	}
	
	public Rectangle getBoundsTop() {
	
		return new Rectangle((int)x, (int)y, width, (height-10));
	}
	
	
	public Rectangle getBoundsBottom() {
		
		return new Rectangle((int)(x+width/4), (int)(y+height-10), width/2, 10);
	}
	
	
	
	// !!! Uwaga - dodac getBounds - obrys glowy - do funkcji zjadania kwiatkow
	
}
