package com.kw.owls.objects;

import java.awt.Color;
import java.awt.Graphics;

import com.kw.owls.framework.GameObject;
import com.kw.owls.framework.ObjectId;

// klasa testowa !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 

public class Test extends GameObject{

	public Test(float x, float y, ObjectId id) {
		super(x, y, id);

	}


	public void tick() {
		// TODO Auto-generated method stub
		
	}


	public void render(Graphics g) {
		
		g.setColor(Color.red);
		g.fillRect((int) x,(int) y, 32, 32);
	}


	public float getX() {
		// TODO Auto-generated method stub
		return x;
	}


	public float getY() {
		// TODO Auto-generated method stub
		return y;
	}


	public void setX(float x) {
		
		this.x = x;
	}


	public void setY(float y) {
		
		this.y = y;
		
	}


	public float getVelX() {
		
		return velX;
	}


	public float getVelY() {
		
		return velY;
	}


	public void setVelX(float velX) {
		
		this.velX = velX;
	}


	public void setVelY(float velY) {
		
		this.velY = velY;
	}


	public ObjectId getId() {
	
		return id;
	}
	



}
