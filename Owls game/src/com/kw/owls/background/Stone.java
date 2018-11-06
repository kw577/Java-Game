package com.kw.owls.background;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.kw.owls.framework.ObjectId;
import com.kw.owls.window.BufferedImageLoader;

public class Stone extends BackgroundObject{

	private BufferedImage stone_image = null;
	private int type; // 1 - 3 w zaleznosci od typu laduje sie inny obrazek
		
	
	// Uwaga !!! - wszystkie rysunki powinny miec wymiary 100px x 60px wysokosci
	
	public Stone(float x, float y, ObjectId id, int type) {
		super(x, y, id);

		BufferedImageLoader loader = new BufferedImageLoader();
		this.type = type;
		
		stone_image = loader.loadImage("/background/stone_summer_" + type + ".png");
	}
	

	public void tick() {
		// zrobic jakis ruch albo animacje elementow w tle ???
	}


	public void render(Graphics g) {
		
		g.drawImage(stone_image, (int)x, (int)y, null);
	}



}
