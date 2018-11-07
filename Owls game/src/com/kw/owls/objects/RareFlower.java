package com.kw.owls.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.kw.owls.framework.GameObject;
import com.kw.owls.framework.ObjectId;

// za zebranie tych kwiatkow dostaje sie 3 punkty - poza tym nie roznia sie niczym od UsualFlower i WaterFlower
public class RareFlower extends GameObject{

	private float width = 32, height = 48;
	private TexturesManager textures;
	private int type; // typ kwiatka - w zaleznosci od niego przyjmuje sie renderowany obrazek - parametr ten jest generowany losowo w klasie Spawner.java 
	
	public RareFlower(int x, int y, ObjectId id, TexturesManager textures, int type) {
		super(x, y, id);
		
		this.textures = textures;
		this.type = type;
	}
	

	public void tick() {

		
	}

	
	public void render(Graphics g) {
		//g.setColor(Color.cyan);
		//g.fillRect((int)x, (int)y, (int)width, (int)height);
		
		g.drawImage(textures.getRare_flower(type), (int)x, (int)y, null);
		
		//Graphics2D g2d = (Graphics2D) g;
		//g.setColor(Color.RED);
		
		
		// pomocnicze 
		//g2d.draw(getBounds());
	}

	
	public Rectangle getBounds() {

		return new Rectangle((int)(x + width/4), (int)(y), (int)(width/2), (int)(height));
	}

}
