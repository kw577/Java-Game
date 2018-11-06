package com.kw.owls.background;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.kw.owls.framework.ObjectId;
import com.kw.owls.window.BufferedImageLoader;

// drzewa w tle - brak interakcji z innymi obiektami - ew. w metodzie tick() - dodac jakis ruch albo inna animacje

public class TreeSummer extends BackgroundObject{

	private BufferedImage summer_tree = null;
	private int type; // 1 - 2 w zaleznosci od typu drzewa laduje sie inny obrazek
		
	
	// Uwaga !!! - wszystkie rysunki drzew powinny miec 600 px wysokosci
	
	public TreeSummer(float x, float y, ObjectId id, int type) {
		super(x, y, id);

		BufferedImageLoader loader = new BufferedImageLoader();
		this.type = type;
		
		summer_tree = loader.loadImage("/background/tree_summer_" + type + ".png");
	}
	

	public void tick() {
		// zrobic jakis ruch albo animacje elementow w tle ???
	}


	public void render(Graphics g) {
		
		g.drawImage(summer_tree, (int)x, (int)y, null);
	}



}
