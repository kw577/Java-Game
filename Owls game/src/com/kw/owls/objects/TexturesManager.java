package com.kw.owls.objects;


import java.awt.image.BufferedImage;

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
		} catch (Exception e){
			e.printStackTrace();
		}				
	}
		
		
	
}