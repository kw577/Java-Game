package com.kw.owls.framework;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

public abstract class GameObject {

	protected float x, y; // polozenie
	protected ObjectId id;
	protected float velX = 0, velY = 0; // ruch
	
	protected boolean falling = true;  // do symulacji dzialania grawitacji
	protected boolean jumping = false;  // do symulacji dzialania grawitacji
	
	// konstruktor
	public GameObject(float x, float y, ObjectId id) {
		
		this.x = x;
		this.y = y;
		this.id = id;
	}
	
	
	public abstract void tick();
	public abstract void render(Graphics g);
	
	// collision detection
	public abstract Rectangle getBounds();
	
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getVelX() {
		return velX;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}

	public float getVelY() {
		return velY;
	}

	public void setVelY(float velY) {
		this.velY = velY;
	}


	public boolean isFalling() {
		return falling;
	}


	public void setFalling(boolean falling) {
		this.falling = falling;
	}


	public boolean isJumping() {
		return jumping;
	}


	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}
	
	
}
