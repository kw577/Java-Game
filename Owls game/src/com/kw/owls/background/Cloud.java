package com.kw.owls.background;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.kw.owls.framework.ObjectId;
import com.kw.owls.window.BufferedImageLoader;

// chmury w tle - brak interakcji z innymi obiektami - ew. w metodzie tick() - dodac jakis ruch albo inna animacje

public class Cloud extends BackgroundObject{

	private BufferedImage clouds = null;
	private int type; // 1 - 4 w zaleznosci od typu chmury laduje sie inny obrazek
	
	public Cloud(float x, float y, ObjectId id, int type) {
		super(x, y, id);

		BufferedImageLoader loader = new BufferedImageLoader();
		this.type = type;
		
		clouds = loader.loadImage("/cloud_" + type + ".png");
	}
	

	public void tick() {
		// zrobic jakis ruch albo animacje elementow w tle ???
	}


	public void render(Graphics g) {
		
		//g.setColor(Color.red);
		//g.drawRect((int) x,(int) y, 32, 32);
		
		g.drawImage(clouds, (int)x, (int)y, null);
	}



}
