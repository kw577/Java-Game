package com.kw.owls.window;

import com.kw.owls.framework.GameObject;

//klasa sluzy do tego aby mozna bylo zwiedzac caly obszar gry - tzn po przejsciu do krawedzi ekranu laduje sie kolejny obszar gry

public class Camera {
	
	private float x, y;

	public Camera(float x, float y) {

		this.x = x;
		this.y = y;
	}
	
	
	public void tick(GameObject player) {
		//update wspolrzednych kamery - mozna przyjac rozne wzory w zaleznosci od tego w jak chcemy zeby nastepowalo wczytywanie terenu
		
		//x--; // test - gyby zawartscia tej funckji byla ta jedna linia kodu - kamera przemieszczalaby sie jednostajnie w prawo
		
		x = -player.getX() - Game.WIDTH/2 + 200;
		//y = -player.getY() + 350; // kamera zawsze sledzi ruchy gracza po osi y
		
		
		y = -800;
		if(player.getY() < 900) {
			y+=-(player.getY()-900);
		}
		
		
		System.out.println("\nCamera: wsp X: " + this.x + " wspY: " + this.y);
		//x += ((player.getX() - x) - 1000/2) * 0.05f;
		//y += ((player.getY() - y) - 563/2) * 0.05f;
		
		// Warunki brzegowe ???
		//if(x <= 0) x = 0;
		//if(x >= 1032 + 16) x = 1032 + 16;
		//if(y <= 0) y = 0;
		//if(y >= 563 - 32) y = 563 - 32;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
}
