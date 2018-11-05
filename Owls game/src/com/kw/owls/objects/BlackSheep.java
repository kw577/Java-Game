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
import com.kw.owls.framework.SpriteSheet;
import com.kw.owls.window.BufferedImageLoader;
import com.kw.owls.window.Game;
import com.kw.owls.window.Handler;


// jest przeciwnikiem - moze zadawac graczowi obrazenia - utrata punktow zdrowia 
public class BlackSheep extends GameObject{

    private int width = 70;
    private int height = 54;
    
    private int health = 5; // po utracie tych punktow owca na pewien czas zostanie ogluszona
    private final int max_health = 5;
    
    private boolean injured = false; // zaraz po uderzeniu pociskiem ta zmienna przyjmuje wartosc true
    private int injuredTimer = 0;
    private final int injuredTimerStart = 100;
    
    private BufferedImage sheep_images = null;
    private SpriteSheet sheepSpriteSheet;
    
    private Handler handler;
    private Game game;
    
    private float gravity = 0.2f;
    private final int max_fall_velocity = 15;
    
    private BufferedImage level_map;
    
    
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
    
    private boolean stunned = false; // czy owca zostala ogluszona (przez pociski rzucane prze gracza lub OwlZiggy)
    private int stunned_timer = 0;
    private int stunned_timer_start = 1000; // timer unieruchomienia owcy
    
    // renderowanie animacji
 	private boolean turned_left = false; // rysunki animacji gdy obiekt jest zwrocony w lewo
 	private boolean turned_right = true; // rysunki animacji gdy obiekt jest zwrocony w prawo - domyslnie gre zaczyna sie animacja tego typu
   
 	// timery animacji
    private int sheep_anim_timer = 0; // odmierzanie czasu do zmiany animacji
    private int sheep_changeAnimation = 25; // czas po ktorym zmieni sie animacja
    
    private int stunned_anim_timer = 0; // odmierzanie czasu do zmiany animacji
    private int stunned_changeAnimation = 8; // czas po ktorym zmieni sie animacja
    
	// kosntruktor
	public BlackSheep(int x, int y, ObjectId id, Handler handler, Game game) {
		super(x, y, id);
		this.handler = handler;
		this.game = game;
		
		rand = new Random();
		
		 BufferedImageLoader loader = new BufferedImageLoader();
	     
	     
	     try {
	            
	         level_map = loader.loadImage("/levels/Level_" + game.getGameLevel() + ".png");
	         sheep_images = loader.loadImage("/objects/blackSheep_spritesheet.png");
	         
	         
	     } catch (Exception e){
	         e.printStackTrace();
	     }
		
   
	     
		
		sheepSpriteSheet = new SpriteSheet(sheep_images);
			
	     
	     
	     
	     
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
		
		
		// !!!! ta funkcja musi byc po funkcji collision() !!!!
		// do testow - utrata punktow zdrowia - przejscie w stan stunned 
		//this.health--; // DO TESTOW
		
		//if(health < 100 && health > 0) System.out.println("\nHealth: " + this.health);
		if(this.health <= 0) {
			this.stunned = true;
			
		}
		
		if(this.stunned) {
			this.stunned_timer++;
			//System.out.println("\nstunned_timer: " + this.stunned_timer);
			if(this.stunned_timer > this.stunned_timer_start) {
				
				
				this.stunned_timer = 0;
				this.stunned = false;
				this.health = this.max_health;
				
				this.injuredTimer = 0;
				this.injured = false;
				
				// zerowanie timerow animacji
				this.sheep_anim_timer = 0;
				this.stunned_anim_timer = 0;
				this.direction_timer = 0;
			}
			
		}
		
		if(this.injured) {
			this.injuredTimer++;
			//System.out.println("\nSheep - injuredTimer: " + this.injuredTimer);
			if(this.injuredTimer >= this.injuredTimerStart) {
				this.injured = false;
				this.injuredTimer = 0;
			}
		}
	}


	private void choose_direction() {
		
		// Obliczanie odleglosci od gracza
		if(!stunned) {
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
					//System.out.println("Odleglosc od gracza:   dx1 = " + dx1 + "    dy1 = " + dy1 + "     dist = " + dist + "   opcja 1");
								
					this.chasing_player = true;
					
					
					if(dx1 > 0) velX = -5;
					else velX = 5;
				}
				else if(dist <= 60 && dy1 > 30 && dy1 < 50) {
					//System.out.println("Odleglosc od gracza:   dx1 = " + dx1 + "    dy1 = " + dy1 + "     dist = " + dist + "   opcja 2");
					
					this.chasing_player = true;
					
					velX = 0;
				}
				else {
					
					this.chasing_player = false;
					
					// zwolnienie po zakonczeniu poscigu gracza
					if(velX > 1) velX = 0.5f;
					if(velX < -1) velX = -0.5f;
					
					////////////////////////////////////////
					// Poruszanie sie gdy w poblizu nie ma gracza
						
					this.direction_timer--;
					if(this.direction_timer <= 0) {
						//System.out.println("Odleglosc od gracza:   dx1 = " + dx1 + "    dy1 = " + dy1 + "     dist = " + dist + "   opcja 3");
						
						// losujemy czas w ktorym owca bedzie poruszac sie w danym kierunku
						this.direction_timer = (rand.nextInt(5) + 3) * 100;
						//System.out.println("\n\n\nSheep  -  prev velX: " + velX + "      prev velY: " + velY);
						//System.out.println("\nUstalono direction_timer:        " + this.direction_timer);
						
						this.choose_direction = rand.nextInt(2);
						//System.out.println("\nchoose_direction:        " + this.choose_direction);
						
						// jesli wczesniej owca stala w miejscu
						if(this.velX == 0) {
							if(this.choose_direction == 0)
								velX = 0.5f;
							else
								velX = -0.5f;
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
				
		}	
				
				
		if(this.stunned) {
			velX = 0;
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
		
		
		
		
		
		
		// Gdy pocisk uderzy owce
		for(int i = 0; i < handler.missiles.size(); i++) {
			GameObject tempObject = handler.missiles.get(i);
			
			
			
			if(tempObject.getId() == ObjectId.Missile) {		
			
				if(getBounds().intersects(tempObject.getBounds())) { // jesli pocisk uderzy owce
					
					if(this.injured == false) {
						this.health--;
						this.injured = true;
						
						
					}
					
					handler.removeMissile(tempObject);
				} 
		
			}
		
		}
		
	
		
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
		
		 direction();
		
		 
		  if(!stunned) {
			 this.sheep_anim_timer++;
			 
			 
			 if(velX == 0) {
				 if(turned_right) {
					 g.drawImage(sheepSpriteSheet.grabImage((int)(1), 1, 75, 65), (int)(x-2), (int)(y-8), null);    
				 }
				 if(turned_left) {
					 g.drawImage(sheepSpriteSheet.grabImage((int)(1), 4, 75, 65), (int)(x-2), (int)(y-8), null);    
				 }
			 }
			 
			 if(velX != 0) {
				 
				 
				 if(turned_right) {
					 g.drawImage(sheepSpriteSheet.grabImage((int)(sheep_anim_timer/sheep_changeAnimation + 1), 2, 75, 65), (int)(x-2), (int)(y-8), null);    
				 }
				 if(turned_left) {
					 g.drawImage(sheepSpriteSheet.grabImage((int)(sheep_anim_timer/sheep_changeAnimation + 1), 5, 75, 65), (int)(x-2), (int)(y-8), null);    
				 }
			 }
			 
			 if(this.chasing_player) this.sheep_anim_timer++; // przyspieszenie timera animacji jesli owca biegnie za graczem 
			 
			 if(this.sheep_anim_timer >= (sheep_changeAnimation * 2 - 1)) {
		            this.sheep_anim_timer = 0;
		        }

		 }
		 
		 if(stunned) {
			 this.stunned_anim_timer++;
			 
			 if(turned_right) {
				 g.drawImage(sheepSpriteSheet.grabImage((int)(stunned_anim_timer/stunned_changeAnimation + 1), 3, 75, 65), (int)(x-2), (int)(y-8), null);    
			 }
			 if(turned_left) {
				 g.drawImage(sheepSpriteSheet.grabImage((int)(stunned_anim_timer/stunned_changeAnimation + 1), 6, 75, 65), (int)(x-2), (int)(y-8), null);    
			 }
			 
			 if(this.stunned_anim_timer >= (stunned_changeAnimation * 3 - 1)) {
		            this.stunned_anim_timer = 0;
		     }
		 }
		 
		 
	 
		    
		 //g.setColor(Color.green);
		 //g.fillOval((int)x, (int)y, 5, 5);
	     
	     Graphics2D g2d = (Graphics2D) g;
	     g.setColor(Color.RED);
		
	     g2d.draw(getBoundsTop());
	     g2d.draw(getBoundsBottom());
	     
	     g.setColor(Color.yellow);
	     g2d.draw(getBounds());
	     
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
	 		
	 		if(this.turned_left && !this.stunned) {
	 			return new Rectangle((int)x, (int)y, width/2, height/2 + 10);
	 		}
	 		else if(this.turned_right && !this.stunned) {
	 			return new Rectangle((int)(x + width/2), (int)y, width/2, height/2 + 10);
	 		}
	 		
	 		// w przciwnym razie zostaje zwrocony obszar poza obszarem gry tak aby nie moglo dojsc do kolizji - utraty punktow gry
	 		else return new Rectangle((int)(x + width/4), (int)(y + height + 20), width/2, 5); // mozna by tez zwrocic prostokat w zupelnie dowolnym miekscu planszy poza obszarem gry
	 	}
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	public Rectangle getBoundsTop() {
	 	
	 		return new Rectangle((int)x, (int)y, width, (height-10));
	 	}
	 	
	 	
	 	public Rectangle getBoundsBottom() {
	 		
	 		return new Rectangle((int)(x+width/4), (int)(y+height-10), width/2, 10);
		}
	
	
	 private void direction() {
			// pomocniczo do renderowania animacji - sprawdza czy Daisy porusza sie w lewo czy w prawo
			if(this.velX < 0) {
				turned_right = false;
				turned_left = true;
			}
			if(this.velX > 0) {
				turned_right = true;
				turned_left = false;
			}
		
			
		}
	
	// !!! Uwaga - dodac getBounds - obrys glowy - do funkcji zjadania kwiatkow
	
}
