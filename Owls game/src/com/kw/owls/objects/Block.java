package com.kw.owls.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.kw.owls.framework.GameObject;
import com.kw.owls.framework.ObjectId;
import com.kw.owls.window.BufferedImageLoader;


public class Block extends GameObject{

	private BufferedImage block_image = null;
	
	
	public Block(float x, float y, ObjectId id) {
		super(x, y, id);
		
		BufferedImageLoader loader = new BufferedImageLoader();
		
		block_image = loader.loadImage("/textures/grass_1.png");

	}

	
	

	public void tick() {
		// TODO Auto-generated method stub
		//x +=1;
	}


	public void render(Graphics g) {
		
		//g.setColor(Color.red);
		//g.drawRect((int) x,(int) y, 32, 32);
		
		g.drawImage(block_image, (int)x, (int)y, null);
	}


	public Rectangle getBounds() {
		
		return new Rectangle((int) x, (int) y, 32, 32);
	}



	



}
