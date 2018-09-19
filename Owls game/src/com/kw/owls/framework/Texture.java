package com.kw.owls.framework;

import java.awt.image.BufferedImage;

import com.kw.owls.window.BufferedImageLoader;

// czy korzystac z tej klasy ????
// https://www.youtube.com/watch?v=DnsKYv39VfI&list=PLWms45O3n--54U-22GDqKMRGlXROOZtMx&index=12  - opis !!!
// klasa bedzie uzywana docelowo do zaladowania tekstur - na razie sa one wstawione testowo 
public class Texture {

	SpriteSheet bs, ps;
	private BufferedImage block_sheet = null;
	private BufferedImage player_sheet = null; 
	public BufferedImage[] block = new BufferedImage[2]; // wczytywanie 2 grafik
	
	
	// konstruktor
	public Texture() {
		BufferedImageLoader loader = new BufferedImageLoader();
		
		try {
			block_sheet = loader.loadImage("/block_sheet.png");
			player_sheet = loader.loadImage("/player_sheet.png");
		} catch (Exception e){
			e.printStackTrace();
		}
		
		bs = new SpriteSheet(block_sheet);
		ps = new SpriteSheet(player_sheet);
	}
	
	private void getTextures() {
		block[0] = bs.grabImage(1, 1, 32, 32); // przykladowe dane
		block[1] = bs.grabImage(2, 1, 32, 32); 
	}
}
