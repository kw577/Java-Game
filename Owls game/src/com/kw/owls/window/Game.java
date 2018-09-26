package com.kw.owls.window;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import com.kw.owls.framework.KeyInput;
import com.kw.owls.framework.ObjectId;
import com.kw.owls.framework.STATE;



public class Game extends Canvas implements Runnable{

	// nalezy utworzyc folder w ktorym beda przechowywane grafiki gry - folder res  ->
	// nastepnie:    project explorer -> prawy przycisk myszy na projekcie -> Properties -> Java Build Path -> Add class folder -> zaznaczyc utworzony przed chwila folder res -> OK
	
	/**
	 *  Wygenerowany numer seryjny (nieobowiazkowe)
	 */
	private static final long serialVersionUID = 504219620878017710L;

	private boolean running = false;
	private Thread thread;
	
	private Handler handler;
	private BackgroundHandler bcg_handler;
	private Camera camera;
	private Menu menu;
	private Spawner spawner;
	
	
	// Parametry gry
	public static STATE gameState = STATE.Menu;
	private int gameLevel = 0;
	
		
	// konstruktor
	public Game() {
		new Window(800, 600, "Building game", this);
		
			
		start();
				
		handler = new Handler();
		bcg_handler = new BackgroundHandler();
		menu = new Menu(this, handler);
		spawner = new Spawner(handler, bcg_handler, this);
		camera = new Camera(0, 0); // poczatkowe polozenie kamery sledzacej gracza 
		
		
		this.addMouseListener(menu);
		this.addMouseMotionListener(menu); // podswietlanie przycikow menu gdy sie na nie najedzie myszka
		this.addKeyListener(new KeyInput(handler));
		
		
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
			
		if(gameState == STATE.Game) {
			
	
			spawner.checkLevel();
	
			
			handler.tick();
			
			// Kamera sledzi ruchy gracza
			for(int i = 0; i<handler.object.size(); i++) {
				if(handler.object.get(i).getId() == ObjectId.Player) {
					camera.tick(handler.object.get(i));
				}
			}
		}
		else if(gameState == STATE.Menu || gameState == STATE.Help) {
			menu.tick();
			
		}
		else if(gameState == STATE.LoadLevel) {
			spawner.load();
			
		}
	
	}

	// updatowanie widoku ekranu
	private void render() {
		
		BufferStrategy bs = this.getBufferStrategy(); // refers to Canvas class - represents the mechanism with which to organize complex memory on a particular Canvas or Window
		if(bs == null) {
			this.createBufferStrategy(3); // create a BufferStrategy with that number of buffers
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		
		Graphics2D g2d = (Graphics2D) g;
		/////////////////////////////
		// renderowanie grafiki gry w tym bloku
		
		if(gameState == STATE.Game) {
			g.setColor(new Color(190, 220, 235));
			g.fillRect(0, 0, getWidth(), getHeight());
		
		
		
		
			g2d.translate(camera.getX(), camera.getY()); // sledzenie ruchu gracza  
		
			bcg_handler.render(g);
			handler.render(g);
		
		
			g2d.translate(-camera.getX(), -camera.getY()); // sledzenie ruchu gracza
		}	
		else if(gameState == STATE.Menu || gameState == STATE.Help) {
			menu.render(g);
					
		}
		else if(gameState == STATE.LoadLevel) {
			spawner.render(g);
			
		}
		/////////////////////////////
		g.dispose();
		bs.show();
		
	}
	

			
	public int getGameLevel() {
		return gameLevel;
	}

	
	public void setGameLevel(int gameLevel) {
		this.gameLevel = gameLevel;
	}		
			
			
	public static void main(String args[]) {
		new Game();
	}
	
}
