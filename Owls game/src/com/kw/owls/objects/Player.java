package com.kw.owls.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.kw.owls.framework.GameObject;
import com.kw.owls.framework.ObjectId;
import com.kw.owls.framework.SpriteSheet;
import com.kw.owls.window.BufferedImageLoader;
import com.kw.owls.window.Game;
import com.kw.owls.window.Handler;

public class Player extends GameObject{

	private float width = 48, height = 96;
	private BufferedImage player_images_all;
	private boolean supported = false; //zmienna oznaczajaca czy player stoi na jakims bloku
	private float gravity = 0.2f;
	private final float MAX_SPEED = 15;  // maksymalna przyjeta szybkosc spadania  
	private final float MAX_SPEED_Running = 10; // maksymalna szybkosc biegu
	Handler handler;
	private Game game; 
	private int accelerateTimer = 0;
	private final int accelerateTimerMax = 50; // okresla czas po jakim nastapi zwiekszenie szybkosci ruchu
	
	private SpriteSheet playerSpriteSheet;
	
	
	// do renderowania animacji
	private boolean turned_left = false; // rysunki animacji gdy gracz jest zwrocony w lewo
	private boolean turned_right = true; // rysunki animacji gdy gracz jest zwrocony w prawo - domyslnie gre zaczyna sie animacja tego typu
	private boolean high_fall = false; // upadek z duzej wysokosci
	
	
	// timery do renderowania animacji
	private final int blink_eye_timer = 5000; // czas do kolejnego mrugniecia
	private int b_e_timer = 5000; // aktualny status timera
	private final int start_timer2 = 100;
	private int timer2 = 100; // upadek z duzej wysokosci - timer animacji
	private int running_timer = 15; // czas do zmiany animacji biegania
	private final int running_timer_start = 15;
	private int choose_run_animation = 1; // przyjmuje wartosc (-1) lub 1 - kazdej wartosci odpowiada inny obrazek animacji
	
	
	// timery zmiany stanu zdrowia i punktow - do testow
	private final int timer_test_start = 100;
	private int timer_test = 100;
	
	// timery okreslajace czas po jakim gracz ponownie moze stracic punkty zdrowia i punkty gry
	private boolean injured = false;
	
	private boolean underWater = false; // przechowuje informacje czy gracz znajduje sie pod woda
	private int divingTimer = 1000; // timer nurkowania - ilosc czasu jaka gracz moze byc pod woda bez utraty punktow zdrowia
	private int divingTimerStart = 1000;
	
	private int injuredTimer = 200;
	private int injuredTimerStart = 200;
	
	private boolean lostFlowers = false;
	private Random random;
	private int lostFlowersTimer = 200;
	private int lostFlowersTimerStart = 200;
	private int temp; // zmienna pomocnicza
	
	public Player(float x, float y, ObjectId id, Handler handler, Game game) {
		super(x, y, id);
		this.handler = handler;
		this.game = game;
		
		BufferedImageLoader loader = new BufferedImageLoader();
		random = new Random();
		
		try {
			player_images_all = loader.loadImage("/player_images_all.png");
		} catch (Exception e){
			e.printStackTrace();
		}
		
		playerSpriteSheet = new SpriteSheet(player_images_all);
		
		
		
	}

	
	public void tick() {	
		
		
		
		
		
		// do celow testowych zmiany poziomu zdrowia i punktacji wyswietlanych przez hud 
		timer_test--;
		if (timer_test <= 0) {
			//if(game.getHealth() > 0)
			//game.setHealth(game.getHealth()-1);
			
			//game.setFlowers(game.getFlowers() + 1);
			timer_test = timer_test_start;
		}
		
		
		if(game.getFlowers() == 0)
		{
			game.setFlowers(10);
		}
		
		
		///////
		
		
		
		if(jumping || falling) {
			
			velY += gravity; // bo   v = g*t    - rownianie spadku swobodnego
			
			if(velY > MAX_SPEED) {
				velY = MAX_SPEED;
			}
			
		}
		
 		// ruch gracza
 		if (handler.isRight()) {
 			accelerateTimer--;
 			if(accelerateTimer <= 0) {
 				if(velX == 0) velX += 4;
 				else velX += 2;
 				
 				accelerateTimer = accelerateTimerMax;
 				if(velX >= MAX_SPEED_Running) velX = MAX_SPEED_Running;
 			}
 			
 		}
 		else if (!handler.isLeft()) {
 			velX = 0;
 			accelerateTimer = 0;
 		}
 		
 		if (handler.isLeft()) {
 			accelerateTimer--;
 			if(accelerateTimer <= 0) {
 				if(velX == 0) velX -= 4;
 				else velX -= 2;
 				
 				accelerateTimer = accelerateTimerMax;
 				if(velX <= -MAX_SPEED_Running) velX = -MAX_SPEED_Running;
 			}
 		}
 		else if (!handler.isRight()) {
 			velX = 0;
 			accelerateTimer = 0;
 		}
		
		
		// skok
		if (handler.isUp() && jumping == false) {
			velY = -6;
			jumping = true;
		}
		
		
		x += velX;
		y += velY; 
		
		// granice planszy - dodatkowo nalezy w ustawic granice dzialania kamery
		if(x < 0) x = 0; 
		if(x > 15950) x = 15950; 
		
		//////////////////
		
		collision();
		
		// upadek z duzej wysokosci - gracz gubi kwiatki (1 - 3 szt.)
		if(lostFlowers == false && velY > 14) {
			
			losePoints();
		}
		
		// upadek z duzej wysokosci - gracz traci 1 pkt zdrowia
		if(high_fall == true &&  velY == 0 && injured == false) {
			
			injured = true;
			
			if(game.getHealth() > 0) {
				game.setHealth(game.getHealth() - 1);
			}
		}
		
		
		// odliczanie timera po ktorym gracz znowu moze stracic punkty gry
		if(lostFlowers == true) {
			lostFlowersTimer--;
			if(lostFlowersTimer <= 0) {
				lostFlowers = false;
				game.setLostFlowers(0);
				lostFlowersTimer = lostFlowersTimerStart;
			}
		}
		
		// odliczanie timera po ktorym gracz znowu moze stracic punkty zdrowia
		if(injured == true) {
			injuredTimer--;
			if(injuredTimer <= 0) {
				injured = false;
				injuredTimer = injuredTimerStart;
			}
		}
		
	}

	
	private void collision() {
		// TODO Auto-generated method stub
		supported = false;
		underWater = false; 
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			
			
			if(tempObject.getId() == ObjectId.Block || tempObject.getId() == ObjectId.Ground) {		
			
				
				// Player uderzy glowa w jakis blok
				if(getBoundsTop().intersects(tempObject.getBounds())) { // jesli gracz stoi na jakims bloku
					
					y = tempObject.getY() + 32; // nalezy tak dobrac wspolrzedna y aby player odbijal sie od bloku
					
					velY = 0;
				} 
				
				// Player stoi na jakims bloku
				if(getBounds().intersects(tempObject.getBounds())) { // jesli gracz stoi na jakims bloku
					
					y = tempObject.getY() - height;
					
					supported = true; // jesli gracz znajduje sie chociaz na jednym bloku  
					velY = 0;
					jumping = false;
					falling = false;

				} 
				
				// Right collision
				if(getBoundsRight().intersects(tempObject.getBounds())) { // jesli gracz stoi na jakims bloku 
					velX = 0;
					x = tempObject.getX() - width;
				} 
				
				// Left collision
				if(getBoundsLeft().intersects(tempObject.getBounds())) { // jesli gracz stoi na jakims bloku 
					velX = 0;
					x = tempObject.getX() + 35;
				}
				
				
				
			}
			
			// gracz wpada do wody sredniej glebokosci - gubi losowa ilosc kwiatkow (1 - 3)
			if(lostFlowers == false && tempObject.getId() == ObjectId.WaterMid) {
				
				
				if(getBoundsWhole().intersects(tempObject.getBounds())) { 
					losePoints();
				}
				
			}
			
			// gracz wpada do glebokiej wody traci punkty zdrowia
			if(tempObject.getId() == ObjectId.WaterDeep) {
				
				if(getBoundsWhole().intersects(tempObject.getBounds())) { 
					
					underWater = true;
					divingTimer--;
					
					if(divingTimer <= 0 && injured == false) {
						if(game.getHealth() > 0)
							game.setHealth(game.getHealth() - 1);
						
						injured = true;
					}
					
					
					
					
				}
				
			}
			
			
			
			
			
			
		} 
		if(supported == false)
			falling = true;
		
		if(!underWater) divingTimer = divingTimerStart;
		
		/////
		System.out.println("\nvelY: " + velY);
		System.out.println("\nfalling: " + falling);
		System.out.println("\njumping: " + jumping);
		System.out.println("\nPlayer: wsp X: " + this.x + " wspY: " + this.y);
		System.out.println("\nPlayer: velX: " + this.velX + " velY: " + this.velY);
		
		System.out.println("\n\n\nUnder water: " + this.underWater);
		System.out.println("\nDiving timer: " + this.divingTimer);
		System.out.println("\nInjured: " + this.injured);
		/////
		
		
	}

	public void render(Graphics g) {
		
		//g.setColor(Color.blue);
		//g.fillRect((int) x, (int) y, (int) width, (int) height);
		
		//Graphics2D g2d = (Graphics2D) g;
		//g.setColor(Color.RED);
		
		//g2d.draw(getBounds());
		//g2d.draw(getBoundsRight());
		//g2d.draw(getBoundsLeft());
		//g2d.draw(getBoundsTop());
		//g2d.draw(getBoundsWhole());
		
		
		direction();
		
		
		
		
		
		
		
		
		
		// animacja gdy gracz stoi w miejscu bez ruchu
		if(velX == 0 && velY == 0 && high_fall == false) {
			b_e_timer--;
			if(b_e_timer > 0) {
				if(turned_right) 
				g.drawImage(playerSpriteSheet.grabImage(1, 1, 64, 96), (int)x, (int)y, null);
				if(turned_left)
				g.drawImage(playerSpriteSheet.grabImage(8, 3, 64, 96), (int)x - 16, (int)y, null);
			}
			if(b_e_timer < 0) { // gracz mruga

				if(turned_right) 
				g.drawImage(playerSpriteSheet.grabImage(2, 1, 64, 96), (int)x, (int)y, null);
				if(turned_left)
				g.drawImage(playerSpriteSheet.grabImage(7, 3, 64, 96), (int)x - 16, (int)y, null);
				
				if(b_e_timer <= (-100)) // renderowanie obrazka z zamknietymia oczami nastepuje gdy b_e_timer przyjmuje warotsci w zakresie od 0 do (-100) - czyli czas trwania mrugniecia to 120 jedn
				b_e_timer = blink_eye_timer;
			}
			this.running_timer = this.running_timer_start; // ustawienie od poczatku timera zmiany animacji podczas biegu
		}
		
		
		// animacja gdy gracz skacze
		if(velY != 0) {
			if(velY < 0) {
				if(turned_right) 
				g.drawImage(playerSpriteSheet.grabImage(5, 2, 64, 96), (int)x - 16, (int)y, null);
				if(turned_left)
				g.drawImage(playerSpriteSheet.grabImage(4, 4, 64, 96), (int)x, (int)y, null);
			}
			if(velY > 0 && velY < 14) { // gracz mruga

				if(turned_right) 
				g.drawImage(playerSpriteSheet.grabImage(6, 2, 64, 96), (int)x - 16, (int)y, null);
				if(turned_left)
				g.drawImage(playerSpriteSheet.grabImage(3, 4, 64, 96), (int)x, (int)y, null);
				
			}
			if(velY > 14) { // upadek z duzej wysokosci - utrata punktow zdrowia

				if(turned_right) 
				g.drawImage(playerSpriteSheet.grabImage(8, 2, 64, 96), (int)x - 16, (int)y, null);
				if(turned_left)
				g.drawImage(playerSpriteSheet.grabImage(1, 4, 64, 96), (int)x, (int)y, null);
				
				if(!this.high_fall) high_fall = true; 
			}
			
			

		}
		
		// upadek z duzej wysokosci
		if(high_fall == true && velY == 0) {
			if(turned_right) 
			g.drawImage(playerSpriteSheet.grabImage(7, 2, 64, 96), (int)x - 16, (int)y, null);
			if(turned_left)
			g.drawImage(playerSpriteSheet.grabImage(2, 4, 64, 96), (int)x, (int)y, null);
			
			timer2--;
			if(timer2 <= 0) {
				timer2 = start_timer2;
				high_fall = false;
			}
			
			
		}
		
		
		// bieganie w zaleznosci od predkosci inna animacja
		if(velX != 0 && velY == 0 && high_fall == false) {
			
			running_timer--;
			if(running_timer <= 0) {
				this.choose_run_animation *= (-1);
				this.running_timer = this.running_timer_start;
			}
			
			if(Math.abs(velX) < 8 ) {
				if(turned_right && choose_run_animation > 0) 
					g.drawImage(playerSpriteSheet.grabImage(4, 1, 64, 96), (int)x - 16, (int)y, null);
				if(turned_right && choose_run_animation < 0) 
					g.drawImage(playerSpriteSheet.grabImage(8, 1, 64, 96), (int)x - 16, (int)y, null);
				if(turned_left && choose_run_animation > 0) 
					g.drawImage(playerSpriteSheet.grabImage(5, 3, 64, 96), (int)x, (int)y, null);
				if(turned_left && choose_run_animation < 0) 
					g.drawImage(playerSpriteSheet.grabImage(1, 3, 64, 96), (int)x, (int)y, null);
			
			}

			if(Math.abs(velX) >= 8 ) {
				if(turned_right && choose_run_animation > 0) 
					g.drawImage(playerSpriteSheet.grabImage(5, 1, 64, 96), (int)x - 16, (int)y, null);
				if(turned_right && choose_run_animation < 0) 
					g.drawImage(playerSpriteSheet.grabImage(3, 1, 64, 96), (int)x - 16, (int)y, null);
				if(turned_left && choose_run_animation > 0) 
					g.drawImage(playerSpriteSheet.grabImage(4, 3, 64, 96), (int)x, (int)y, null);
				if(turned_left && choose_run_animation < 0) 
					g.drawImage(playerSpriteSheet.grabImage(6, 3, 64, 96), (int)x, (int)y, null);
			
			}
			
			
			
			

			
			
			
		}
		
		
		
		
	}

	private void direction() {
		// pomocniczo do renderowania animacji - sprawdza czy gracz porusza sie w lewo czy w prawo
		if(this.turned_right && handler.isLeft()) {
			turned_right = false;
			turned_left = true;
		}
		if(this.turned_left && handler.isRight()) {
			turned_right = true;
			turned_left = false;
		}
		
	}


	private void losePoints() {
		
		// zmniejszenie ilosci zebranych punktow
		temp = random.nextInt(3) + 1;
		game.setFlowers(game.getFlowers() - temp);
		if(game.getFlowers() < 0) game.setFlowers(0);
		
		
		lostFlowers = true;
		game.setLostFlowers(temp);
		
	}
	
	
	// do wykrywania kolizji z innymi obiektami od dolu
	public Rectangle getBounds() {
		// generowany prostokat do okreslaania kolizji od daolu (okreslania czy Player stoi na jakims obiekcie jest powiekszony od dolu o 1 px dla lepszego dzialania wykrywania kolizji
		return new Rectangle((int) x+(int)((width/2)-((width/2)/2))-5, (int) y + ((int)(height-9)), (int) width/2 + 10, 12);
	}
	
	// do wykrywania kolizji od gory
	public Rectangle getBoundsTop() {
		
		return new Rectangle((int) x + (int)((width/2)-((width/2)/2)-5), (int) y, (int) width/2 + 10, 10);
	}
	
	// do wykrywania kolizji z prawej strony
	public Rectangle getBoundsRight() {
		
		return new Rectangle((int) x + (int)width - 22, (int) y + 12, (int) 22, (int) height - 24);
	}
	
	// do wykrywania kolizji z lewej strony
	public Rectangle getBoundsLeft() {
		
		return new Rectangle((int) x, (int) y + 12, (int) 22, (int) height -24);
	}

	
	// do wykrywania obrazen gracza - obejmuje caly obrys gracza
	public Rectangle getBoundsWhole() {
		
		return new Rectangle((int) x, (int) y + 8, (int) width, (int) height-10);
	}
	// napisac osobna funkcje do wykrywania kolizji z pociskami (nie ma potrzeby przeprowadzania obliczen dla 4rech osobnych funkcji)
}
