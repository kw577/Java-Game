package com.kw.owls.background;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.kw.owls.framework.ObjectId;
import com.kw.owls.window.BufferedImageLoader;

// oznaczenie konca rundy - brak interakcji z innymi obiektami - element tylko wizualny
public class SignpostNextRound extends BackgroundObject{

	private BufferedImage signpost = null;
			
	
	public SignpostNextRound(float x, float y, ObjectId id) {
		super(x, y, id);

		BufferedImageLoader loader = new BufferedImageLoader();
		
		
		signpost = loader.loadImage("/background/signpost_next_round.png");
	}
	

	public void tick() {
		// zrobic jakis ruch albo animacje elementow w tle ???
	}


	public void render(Graphics g) {
		
		g.drawImage(signpost, (int)x, (int)(y-115), null);
	}



}
