package com.kw.owls.objects;


import java.awt.image.BufferedImage;

import com.kw.owls.window.BufferedImageLoader;

// Klasa pobiera tekstury uzywane w grze - w ten sposob gra szybiej sie laduje
// Zastosowanie klasy TexturesManager pozwala na zwiekszenie szybkosci ladowania gry
public class TexturesManager {


	// Textury uzywane w grze
	private BufferedImage grass_texture = null;
	

	public BufferedImage getGrass_texture() {
		return grass_texture;
	}


	public void setGrass_texture(BufferedImage grass_texture) {
		this.grass_texture = grass_texture;
	}


	// konstruktor
	public TexturesManager() {
		
		BufferedImageLoader loader = new BufferedImageLoader();
		
		
		try {
			grass_texture = loader.loadImage("/textures/grass_1.png");
		} catch (Exception e){
			e.printStackTrace();
		}				
	}
		
		
	
}