package com.kw.owls.window;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import com.kw.owls.framework.ObjectId;
import com.kw.owls.objects.Test;


public class Game extends Canvas implements Runnable{

	
	/**
	 *  Wygenerowany numer seryjny (nieobowiazkowe)
	 */
	private static final long serialVersionUID = 504219620878017710L;

	private boolean running = false;
	private Thread thread;
	
	Handler handler;
	
	// konstruktor
	public Game() {
		new Window(800, 600, "Building game", this);
		start();
		
		handler = new Handler();
			
		handler.addObject(new Test(100, 100, ObjectId.Player));
	}
	
	
	public synchronized void start() {
		if(running)
			return;
		
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	
	
	private void stop() {
		// TODO Auto-generated method stub
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	// petla gry !!!! - odswiezanie ekranu
	// funkcja run() jest w wiekszosci przypadkow zbudowana w taki sposob i mozna ja po prostu skopiowac
	public void run() {
		
		this.requestFocus(); // dzieki temu po wlaczeniu gry klawiatura okno gry jest aktywne od razu - nie trzeba w nie najpierw kliknac aby moc sterowac gra
		long lastTime = System.nanoTime(); // Returns the current value of the running Java Virtual Machine's high-resolution time source, in nanoseconds. 
		double amountOfTicks = 60.0; // zmieniajac ta wartosc mozna spowodowac opoznienie lub przyspieszenie animacji
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis(); // Returns the current value of the running Java Virtual Machine's high-resolution time source, in nanoseconds.
		int updates = 0;
		int frames = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				tick(); // update ekranu gry (60 / s) 
				updates++;
				delta--;
						
			}
			if(running)
				render();
				frames++;
					
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
				updates = 0;
			}
		}
		stop();
		}
	
	

	// updatowanie danych gry
	private void tick() {
		
		handler.tick();
		
	}

	// updatowanie widoku ekranu
	private void render() {
		
		BufferStrategy bs = this.getBufferStrategy(); // refers to Canvas class - represents the mechanism with which to organize complex memory on a particular Canvas or Window
		if(bs == null) {
			this.createBufferStrategy(3); // create a BufferStrategy with that number of buffers
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		/////////////////////////////
		// renderowanie grafiki gry
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		handler.render(g);
		
		/////////////////////////////
		g.dispose();
		bs.show();
		
	}
	
	public static void main(String args[]) {
		new Game();
	}
	
}
