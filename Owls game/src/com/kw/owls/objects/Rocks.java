package com.kw.owls.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.kw.owls.framework.GameObject;
import com.kw.owls.framework.ObjectId;

// klasa ma takie same wlasciwosci jak klasy: Block, Ground
// - jako id przyjmuje ObjectId.Block zeby uniknac zmian w innych klasach w funkcjach collision()

public class Rocks extends GameObject{

	private TexturesManager textures;
	
	public Rocks(float x, float y, ObjectId id, TexturesManager textures) {
		super(x, y, id);
		
		this.textures = textures;

	}


	public void tick() {

	}


	public void render(Graphics g) {
		
		//g.setColor(Color.red);
		//g.drawRect((int) x,(int) y, 32, 32);
		
		g.drawImage(textures.getRocks_texture(), (int)x, (int)y, null);
	}


	public Rectangle getBounds() {
		
		return new Rectangle((int) x, (int) y, 32, 32);
	}



	



}
