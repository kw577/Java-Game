package com.kw.owls.window;

import java.awt.Graphics;
import java.util.LinkedList;

import com.kw.owls.framework.GameObject;

// klasa przechowuje wszystkie obiekty gry

public class Handler {

	public LinkedList<GameObject> object = new LinkedList<GameObject>();
	
	// ruch gracza - spr. KeyInput.java - implementacja w ten sposob zapobiega efektowi "sticky keys"
	private boolean up = false, down = false, right = false, left = false;
	
	private GameObject tempObject;
	
	
	public void tick() {
		for(int i = 0; i < object.size(); i++) {
			tempObject = object.get(i);
			
			tempObject.tick();
		}
	}
	
	
	public boolean isUp() {
		return up;
	}


	public void setUp(boolean up) {
		this.up = up;
	}


	public boolean isDown() {
		return down;
	}


	public void setDown(boolean down) {
		this.down = down;
	}


	public boolean isRight() {
		return right;
	}


	public void setRight(boolean right) {
		this.right = right;
	}


	public boolean isLeft() {
		return left;
	}


	public void setLeft(boolean left) {
		this.left = left;
	}


	public void render(Graphics g) {
		for(int i = 0; i < object.size(); i++) {
			tempObject = object.get(i);
			
			tempObject.render(g);
		}
	}
	
	
	public void addObject(GameObject object) {
		this.object.add(object);
	}
	
	
	public void removeObject(GameObject object) {
		this.object.remove(object);
	}
	
	public void clearHandler() {
		this.object.clear();	
	
	}
	
}
