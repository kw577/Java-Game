package com.kw.owls.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.kw.owls.framework.GameObject;
import com.kw.owls.framework.ObjectId;



public class WaterFlowers extends GameObject{

	private float width = 48, height = 48;
	private TexturesManager textures;
	private int type; // typ kwiatka - w zaleznosci od niego przyjmuje sie renderowany obrazek - parametr ten jest generowawny losowo w klasie Spawner,java 
	
	public WaterFlowers(int x, int y, ObjectId id, TexturesManager textures, int type) {
		super(x, y, id);
		
		this.textures = textures;
		this.type = type;
	}
	

	public void tick() {

		
	}

	
	public void render(Graphics g) {
		//g.setColor(Color.cyan);
		//g.fillRect((int)x, (int)y, 48, 48);
		
		g.drawImage(textures.getWater_flower(type), (int)x, (int)y, null);
		
		//Graphics2D g2d = (Graphics2D) g;
		//g.setColor(Color.RED);
		
		
		// pomocnicze
		//g2d.draw(getBounds());
	}

	
	public Rectangle getBounds() {

		return new Rectangle((int)(x + width/4), (int)(y+height/2-5), 24, 30);
	}

}
