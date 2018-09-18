package com.kw.owls.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.kw.owls.framework.GameObject;
import com.kw.owls.framework.ObjectId;
import com.kw.owls.window.Handler;

public class Player extends GameObject{

	private float width = 48, height = 96;
	
	private boolean supported = false; //zmienna oznaczajaca czy player stoi na jakims bloku
	private float gravity = 0.2f;
	private final float MAX_SPEED = 10;  // maksymalna przyjeta szybkosc spadania
	Handler handler;
	
	public Player(float x, float y, ObjectId id, Handler handler) {
		super(x, y, id);
		this.handler = handler;
	}

	
	public void tick() {	
		
		if(jumping || falling) {
			
			velY += gravity; // bo   v = g*t    - rownianie spadku swobodnego
			
			if(velY > MAX_SPEED) {
				velY = MAX_SPEED;
			}
			
		}
		
		// ruch gracza
		if (handler.isRight()) velX = 5;
		else if (!handler.isLeft()) velX = 0;
		
		if (handler.isLeft()) velX = -5;
		else if (!handler.isRight()) velX = 0;
		
		
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
					
					x = tempObject.getX() - width;
				} 
				
				// Left collision
				if(getBoundsLeft().intersects(tempObject.getBounds())) { // jesli gracz stoi na jakims bloku
					
					x = tempObject.getX() + 35;
				}
				
				
				
			}
		} 
		if(supported == false)
			falling = true;
		
		System.out.println("\nvelY: " + velY);
		System.out.println("\nfalling: " + falling);
		System.out.println("\njumping: " + jumping);
		
	}

	public void render(Graphics g) {
		
		g.setColor(Color.blue);
		g.fillRect((int) x, (int) y, (int) width, (int) height);
		
		Graphics2D g2d = (Graphics2D) g;
		g.setColor(Color.RED);
		
		g2d.draw(getBounds());
		g2d.draw(getBoundsRight());
		g2d.draw(getBoundsLeft());
		g2d.draw(getBoundsTop());
		
	}

	// do wykrywania kolizji z innymi obiektami od dolu
	public Rectangle getBounds() {
		// generowany prostokat do okreslaania kolizji od daolu (okreslania czy Player stoi na jakims obiekcie jest powiekszony od dolu o 1 px dla lepszego dzialania wykrywania kolizji
		return new Rectangle((int) x+(int)((width/2)-((width/2)/2))-2, (int) y + ((int)(height/2)), (int) width/2 + 4, (int) height/2 + 1);
	}
	
	// do wykrywania kolizji od gory
	public Rectangle getBoundsTop() {
		
		return new Rectangle((int) x + (int)((width/2)-((width/2)/2)), (int) y, (int) width/2, (int) height/2);
	}
	
	// do wykrywania kolizji z prawej strony
	public Rectangle getBoundsRight() {
		
		return new Rectangle((int) x + (int)width - 5, (int) y + 10, (int) 5, (int) height - 20);
	}
	
	// do wykrywania kolizji z lewej strony
	public Rectangle getBoundsLeft() {
		
		return new Rectangle((int) x, (int) y + 10, (int) 5, (int) height -20);
	}

	// napisac osobna funkcje do wykrywania kolizji z pociskami (nie ma potrzeby przeprowadzania obliczen dla 4rech osobnych funkcji)
}
