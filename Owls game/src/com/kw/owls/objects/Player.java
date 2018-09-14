package com.kw.owls.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.kw.owls.framework.GameObject;
import com.kw.owls.framework.ObjectId;

public class Player extends GameObject{

	private float width = 32, height = 64;
	
	
	private float gravity = 0.25f;
	private final float MAX_SPEED = 10;  // maksymalna przyjeta szybkosc spadania
	
	
	public Player(float x, float y, ObjectId id) {
		super(x, y, id);
		
	}

	
	public void tick() {
		x += velX;
		y += velY;
		
		// symulacja oddzialywania grawitacji
		if(falling || jumping) {
			
			velY += gravity; // bo   v = g*t    - rownianie spadku swobodnego
			
			if(velY > MAX_SPEED) {
				velY = MAX_SPEED;
			}
			
		}
		
	}

	
	public void render(Graphics g) {
		
		g.setColor(Color.blue);
		g.fillRect((int) x, (int) y, (int) width, (int) height);
	}

	// do wykrywania kolizji z innymi obiektami
	public Rectangle getBounds() {
		
		return new Rectangle((int) x, (int) y, (int) width, (int) height);
	}

}
