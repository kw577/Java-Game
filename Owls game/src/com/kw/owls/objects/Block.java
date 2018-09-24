package com.kw.owls.objects;


import java.awt.Graphics;
import java.awt.Rectangle;

import com.kw.owls.framework.GameObject;
import com.kw.owls.framework.ObjectId;

public class Block extends GameObject{

	private TexturesManager textures;
	
	public Block(float x, float y, ObjectId id, TexturesManager textures) {
		super(x, y, id);
		
		this.textures = textures;


	}

	
	

	public void tick() {
		// TODO Auto-generated method stub
		//x +=1;
	}


	public void render(Graphics g) {
		
		//g.setColor(Color.red);
		//g.drawRect((int) x,(int) y, 32, 32);
		
		g.drawImage(textures.getGrass_texture(), (int)x, (int)y, null);
	}


	public Rectangle getBounds() {
		
		return new Rectangle((int) x, (int) y, 32, 32);
	}



	



}
