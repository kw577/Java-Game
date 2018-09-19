package com.kw.owls.framework;

import java.awt.image.BufferedImage;

// Potrzebne do edycji zdjec:
// https://www194.lunapic.com/editor/?action=transparent      - przezroczyste tlo - clipArt
// https://www194.lunapic.com/editor/
// http://resizeimage.net/



//Klasa pozwala pobrac obraz a nastepnie wybrac z niego jakis wycinek - na razie nie uzywana
public class SpriteSheet {
	
	private BufferedImage image;
	
	public SpriteSheet(BufferedImage image) {
		this.image = image;
	}
	
	public BufferedImage grabImage(int col, int row, int width, int height) {
		BufferedImage img =  image.getSubimage((col*width)-width, (row*height)-height, width, height);
		return img;
	}

}
