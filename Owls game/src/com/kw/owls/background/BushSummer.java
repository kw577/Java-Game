package com.kw.owls.background;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.kw.owls.framework.ObjectId;
import com.kw.owls.window.BufferedImageLoader;

// oznaczenie konca rundy - brak interakcji z innymi obiektami - element tylko wizualny
public class BushSummer extends BackgroundObject{

	private BufferedImage bush_image = null;
			
	
	public BushSummer(float x, float y, ObjectId id) {
		super(x, y, id);

		BufferedImageLoader loader = new BufferedImageLoader();
		
		
		bush_image = loader.loadImage("/background/bush_summer_1.png");
	}
	

	public void tick() {
		// zrobic jakis ruch albo animacje elementow w tle ???
	}


	public void render(Graphics g) {
		
		g.drawImage(bush_image, (int)x, (int)y, null);
	}



}
