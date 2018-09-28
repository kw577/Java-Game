package com.kw.owls.objects;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.kw.owls.framework.GameObject;
import com.kw.owls.framework.ObjectId;

public class WaterMid extends GameObject{

	private TexturesManager textures;
	
	public WaterMid(float x, float y, ObjectId id, TexturesManager textures) {
		super(x, y, id);
		
		this.textures = textures;


	}

	public void tick() {
		// TODO Auto-generated method stub

	}


	public void render(Graphics g) {
		

		g.drawImage(textures.getWater_mid(), (int)x, (int)y, null);
		
	}


	public Rectangle getBounds() {
		
		return new Rectangle((int) x, (int) y, 32, 32);
	}


}
