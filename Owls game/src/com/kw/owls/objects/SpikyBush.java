package com.kw.owls.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.kw.owls.framework.GameObject;
import com.kw.owls.framework.ObjectId;

public class SpikyBush extends GameObject{

	private float width = 60, height = 70;
	private TexturesManager textures;
	
		
	public SpikyBush(int x, int y, ObjectId id, TexturesManager textures) {
		super(x, y, id);

		this.textures = textures;
	}


	public void tick() {
	
		
	}


	public void render(Graphics g) {
		
		//g.setColor(Color.cyan);
		//g.fillRect((int)x, (int)y, (int)width, (int)height);
		
		g.drawImage(textures.getSpiky_bush(), (int)x, (int)y, null);
		
		//Graphics2D g2d = (Graphics2D) g;
		//g.setColor(Color.RED);
		
		
		// pomocnicze
		//g2d.draw(getBounds());
		
	}


	public Rectangle getBounds() {
	
		return new Rectangle((int)(x+width/4-5), (int)(y+height/4-5), (int)(width/2+5), (int)(height/2));
	}
	
}

