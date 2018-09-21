package com.kw.owls.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.kw.owls.framework.GameObject;
import com.kw.owls.framework.ObjectId;
import com.kw.owls.window.BufferedImageLoader;
import com.kw.owls.window.Handler;

public class Player extends GameObject{

	private float width = 48, height = 96;
	private BufferedImage player_image;
	private boolean supported = false; //zmienna oznaczajaca czy player stoi na jakims bloku
	private float gravity = 0.2f;
	private final float MAX_SPEED = 15;  // maksymalna przyjeta szybkosc spadania  
	private final float MAX_SPEED_Running = 10; // maksymalna szybkosc biegu
	Handler handler;
	private int accelerateTimer = 0;
	private final int accelerateTimerMax = 50; // okresla czas po jakim nastapi zwiekszenie szybkosci ruchu
	
	public Player(float x, float y, ObjectId id, Handler handler) {
		super(x, y, id);
		this.handler = handler;
		
		BufferedImageLoader loader = new BufferedImageLoader();
		
		player_image = loader.loadImage("/player_image2.png");
		
	}

	
	public void tick() {	
		
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
		
		//////////////////
		
		collision();
		
	}

	
	private void collision() {
		// TODO Auto-generated method stub
		supported = false;
		
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			
			
			if(tempObject.getId() == ObjectId.Block) {		
			
				
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
		} 
		if(supported == false)
			falling = true;
		
		System.out.println("\nvelY: " + velY);
		System.out.println("\nfalling: " + falling);
		System.out.println("\njumping: " + jumping);
		System.out.println("\nPlayer: wsp X: " + this.x + " wspY: " + this.y);
		System.out.println("\nPlayer: velX: " + this.velX + " velY: " + this.velY);
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
		
		g.drawImage(player_image, (int)x, (int)y, null);
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

	// napisac osobna funkcje do wykrywania kolizji z pociskami (nie ma potrzeby przeprowadzania obliczen dla 4rech osobnych funkcji)
}
