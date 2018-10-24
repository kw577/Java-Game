package com.kw.owls.framework;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.kw.owls.window.Camera;
import com.kw.owls.window.Game;
import com.kw.owls.window.Handler;


// sterowanie ruchem OwlDaisy i in. podczas gry
public class MouseInput extends MouseAdapter{
	
	private Handler handler;
	private Camera camera;
	private Game game; 
	
	// konstruktor
	public MouseInput(Handler handler, Camera camera, Game game) {

		this.handler = handler;
		this.camera = camera;
		this.game = game;
	}
	
	
	public void mousePressed(MouseEvent e) {
		// uwzglednia pozycje kamery d obliczenia aktualnych wspolrzednych
		if(game.gameState == STATE.Game) {
			int mx = (int) (e.getX() - camera.getX()); // -15 - zeby grafika /others/position_marking.png wyswieltila sie w dobrym miejscu
			int my = (int) (e.getY() - camera.getY()); // -30 - zeby grafika /others/position_marking.png wyswieltila sie w dobrym miejscu
			
			game.setPlayer_click_x(mx);
			game.setPlayer_click_y(my);
		}
		
	}

	
}
