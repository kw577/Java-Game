package com.kw.owls.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.kw.owls.framework.GameObject;
import com.kw.owls.framework.ObjectId;


public class Block extends GameObject{

	public Block(float x, float y, ObjectId id) {
		super(x, y, id);

	}

	
	

	public void tick() {
		// TODO Auto-generated method stub
		x +=1;
	}


	public void render(Graphics g) {
		
		g.setColor(Color.red);
		g.fillRect((int) x,(int) y, 32, 32);
	}


	public Rectangle getBounds() {
		
		return new Rectangle((int) x, (int) y, 32, 32);
	}



	



}
