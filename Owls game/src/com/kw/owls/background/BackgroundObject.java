package com.kw.owls.background;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import com.kw.owls.framework.ObjectId;

public abstract class BackgroundObject {

	protected float x, y; // polozenie
	protected ObjectId id;
	
	// konstruktor
	public BackgroundObject(float x, float y, ObjectId id) {
		
		this.x = x;
		this.y = y;
		this.id = id;
	}
	
	public abstract void tick(); // zrobic jakis ruch albo animacje elementow w tle ???
	public abstract void render(Graphics g);
	
	
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

	
}
