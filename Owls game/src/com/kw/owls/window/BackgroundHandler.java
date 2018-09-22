package com.kw.owls.window;

import java.awt.Graphics;
import java.util.LinkedList;

import com.kw.owls.background.BackgroundObject;
import com.kw.owls.framework.GameObject;

// klasa przechowuje wszystkie obiekty w tle

public class BackgroundHandler {

	public LinkedList<BackgroundObject> object = new LinkedList<BackgroundObject>();
	
	private BackgroundObject tempObject;
	
	public void render(Graphics g) {
		for(int i = 0; i < object.size(); i++) {
			tempObject = object.get(i);
			
			tempObject.render(g);
		}
	}
	
	
	public void addObject(BackgroundObject object) {
		this.object.add(object);
	}
	
	
	public void removeObject(BackgroundObject object) {
		this.object.remove(object);
	}
	
}
