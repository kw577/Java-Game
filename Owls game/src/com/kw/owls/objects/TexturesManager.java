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
	private BufferedImage water_surface_1 = null;
	private BufferedImage water_surface_2 = null;
	private BufferedImage water_mid = null;
	private BufferedImage water_deep = null;
	private BufferedImage water_flower_1 = null;
	private BufferedImage water_flower_2 = null;
	private BufferedImage water_flower_3 = null;
	
	// konstruktor
	public TexturesManager() {
		
		BufferedImageLoader loader = new BufferedImageLoader();
		
				
		try {
			grass_texture = loader.loadImage("/textures/grass_1.png");
			ground_texture = loader.loadImage("/textures/ground_1.png");
			water_surface_1 = loader.loadImage("/textures/water_surface_1.png");
			water_surface_2 = loader.loadImage("/textures/water_surface_2.png");
			water_mid = loader.loadImage("/textures/water_mid.png");
			water_deep = loader.loadImage("/textures/water_deep.png");
			water_flower_1 = loader.loadImage("/textures/water_flower_1.png");
			water_flower_2 = loader.loadImage("/textures/water_flower_2.png");
			water_flower_3 = loader.loadImage("/textures/water_flower_3.png");
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
	
	
	public Image getWater_flower(int type) {
		if(type == 0)
			return water_flower_1;
		if(type == 1)
			return water_flower_2;
		else
			return water_flower_3;
	}


		
		
	
}