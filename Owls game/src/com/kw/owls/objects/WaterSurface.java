package com.kw.owls.objects;


import java.awt.Graphics;
import java.awt.Rectangle;

import com.kw.owls.framework.GameObject;
import com.kw.owls.framework.ObjectId;

public class WaterSurface extends GameObject{

	private TexturesManager textures;
	
	private final int animation_timer_start = 25;
	private int animation_timer = 25;
	private int choose_animation = -1; // pomocniczo do wyboru odpowiedniego obrazka animacji
	
	
	public WaterSurface(float x, float y, ObjectId id, TexturesManager textures) {
		super(x, y, id);
		
		this.textures = textures;


	}

	
	

	public void tick() {
		// TODO Auto-generated method stub
		//x +=1;
	}


	public void render(Graphics g) {
		
		
		animation_timer--;
		if(animation_timer <= 0) {
			this.choose_animation *= (-1);
			animation_timer = animation_timer_start;
		}
		
		if(choose_animation < 0)
			g.drawImage(textures.getWater_surface_1(), (int)x, (int)y, null);
		else
			g.drawImage(textures.getWater_surface_2(), (int)x, (int)y, null);
	}


	public Rectangle getBounds() {
		
		return new Rectangle((int) x, (int) y, 32, 32);
	}


}

