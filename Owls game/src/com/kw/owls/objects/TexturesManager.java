package com.kw.owls.objects;


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.kw.owls.window.BufferedImageLoader;

// Klasa pobiera tekstury uzywane w grze - w ten sposob gra szybiej sie laduje
// Zastosowanie klasy TexturesManager pozwala na zwiekszenie szybkosci ladowania gry
public class TexturesManager {


	// Textury uzywane w grze
	private BufferedImage grass_texture = null;
	private BufferedImage ground_texture = null;
	private BufferedImage rocks_texture = null;
	private BufferedImage water_surface_1 = null;
	private BufferedImage water_surface_2 = null;
	private BufferedImage water_mid = null;
	private BufferedImage water_deep = null;
	private BufferedImage water_flower_1 = null;
	private BufferedImage water_flower_2 = null;
	private BufferedImage water_flower_3 = null;
	private BufferedImage usual_flower_1 = null;
	private BufferedImage usual_flower_2 = null;
	private BufferedImage usual_flower_3 = null;
	private BufferedImage usual_flower_4 = null;
	private BufferedImage usual_flower_5 = null;
	private BufferedImage usual_flower_6 = null;
	private BufferedImage usual_flower_7 = null;
	private BufferedImage spiky_bush = null;
	// konstruktor
	public TexturesManager() {
		
		BufferedImageLoader loader = new BufferedImageLoader();
		
				
		try {
			grass_texture = loader.loadImage("/textures/grass_1.png");
			ground_texture = loader.loadImage("/textures/ground_1.png");
			rocks_texture = loader.loadImage("/textures/rocks.png");
			
			water_surface_1 = loader.loadImage("/textures/water_surface_1.png");
			water_surface_2 = loader.loadImage("/textures/water_surface_2.png");
			water_mid = loader.loadImage("/textures/water_mid.png");
			water_deep = loader.loadImage("/textures/water_deep.png");
			water_flower_1 = loader.loadImage("/textures/water_flower_1.png");
			water_flower_2 = loader.loadImage("/textures/water_flower_2.png");
			water_flower_3 = loader.loadImage("/textures/water_flower_3.png");
			
			usual_flower_1 = loader.loadImage("/textures/usual_flower_1.png");
			usual_flower_2 = loader.loadImage("/textures/usual_flower_2.png");
			usual_flower_3 = loader.loadImage("/textures/usual_flower_3.png");
			usual_flower_4 = loader.loadImage("/textures/usual_flower_4.png");
			usual_flower_5 = loader.loadImage("/textures/usual_flower_5.png");
			usual_flower_6 = loader.loadImage("/textures/usual_flower_6.png");
			usual_flower_7 = loader.loadImage("/textures/usual_flower_7.png");
			
			spiky_bush = loader.loadImage("/textures/spiky_bush.png");
		} catch (Exception e){
			e.printStackTrace();
		}				
	}

	
	public BufferedImage getGrass_texture() {
		return grass_texture;
	}

	public BufferedImage getGround_texture() {
		return ground_texture;
	}
	
	public BufferedImage getRocks_texture() {
		return rocks_texture;
	}

	public BufferedImage getWater_surface_1() {
		return water_surface_1;
	}

	
	public BufferedImage getWater_surface_2() {
		return water_surface_2;
	}
	
	public BufferedImage getWater_mid() {
		return water_mid;
	}
	
	public BufferedImage getWater_deep() {
		return water_deep;
	}
	
	public BufferedImage getSpiky_bush() {
		return spiky_bush;
	}
	
	
	public BufferedImage getWater_flower(int type) {
		if(type == 0)
			return water_flower_1;
		if(type == 1)
			return water_flower_2;
		else
			return water_flower_3;
	}

	public BufferedImage getUsual_flower(int type) {
		if(type == 0)
			return usual_flower_1;
		if(type == 1)
			return usual_flower_2;
		if(type == 2)
			return usual_flower_3;
		if(type == 3)
			return usual_flower_4;
		if(type == 4)
			return usual_flower_5;
		if(type == 5)
			return usual_flower_6;
		else
			return usual_flower_7;
	}
		
		
	
}