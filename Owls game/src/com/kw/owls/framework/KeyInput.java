package com.kw.owls.framework;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.kw.owls.window.Handler;

public class KeyInput extends KeyAdapter{

	Handler handler;
	
	
	
	public KeyInput(Handler handler) {

		this.handler = handler;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		
		// wylaczenie gry po wcisniecia przycisku Esc
		if(key == KeyEvent.VK_ESCAPE) {
			System.exit(1);
		}
		
		// implementacja sterowania klawiatura w ten sposob zapobiega efektowi "sticky keys"
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ObjectId.Player) {
				if(key == KeyEvent.VK_SPACE && tempObject.jumping == false && tempObject.falling == false) {
					handler.setUp(true);
				}
				if(key == KeyEvent.VK_LEFT) handler.setLeft(true);
				if(key == KeyEvent.VK_RIGHT) handler.setRight(true);
			}
		}
		 
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ObjectId.Player) {
				if(key == KeyEvent.VK_SPACE) handler.setUp(false);
				if(key == KeyEvent.VK_LEFT) handler.setLeft(false);
				if(key == KeyEvent.VK_RIGHT) handler.setRight(false);
			}
		}
	}
}
