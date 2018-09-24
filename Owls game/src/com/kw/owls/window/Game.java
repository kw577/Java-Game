package com.kw.owls.window;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.kw.owls.background.Cloud;
import com.kw.owls.background.TreeAutumn;
import com.kw.owls.framework.KeyInput;
import com.kw.owls.framework.ObjectId;
import com.kw.owls.framework.STATE;
import com.kw.owls.objects.Block;
import com.kw.owls.objects.Player;


public class Game extends Canvas implements Runnable{

	// nalezy utworzyc folder w ktorym beda przechowywane grafiki gry - folder res  ->
	// nastepnie:    project explorer -> prawy przycisk myszy na projekcie -> Properties -> Java Build Path -> Add class folder -> zaznaczyc utworzony przed chwila folder res -> OK
	
	/**
	 *  Wygenerowany numer seryjny (nieobowiazkowe)
	 */
	private static final long serialVersionUID = 504219620878017710L;

	private boolean running = false;
	private Thread thread;
	private BufferedImage level = null; // do celow testowych - docelowo wczytywanie rund w osobnej klasie spawner
	
	
	Random rand; // do losowego generowania niektorych elementow gry - np tlo - docelowo w klasei Spawner
	
	private Handler handler;
	private BackgroundHandler bcg_handler;
	private Camera camera;
	private Menu menu;
	
	
	
	// Parametry gry
	public static STATE gameState = STATE.Menu;
	private int gameLevel = 0;
	
	
	
	// konstruktor
	public Game() {
		new Window(800, 600, "Building game", this);
		
		BufferedImageLoader loader = new BufferedImageLoader();
		level = loader.loadImage("/Level_1.png"); // load the level - for testing
		
		
		
		start();
				
		handler = new Handler();
		bcg_handler = new BackgroundHandler();
		menu = new Menu(this, handler);
		camera = new Camera(0, 0); // poczatkowe polozenie kamery sledzacej gracza 
		rand = new Random();
		
		
		//loadLevel(level);
		
		this.addMouseListener(menu);
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
			
			if(this.gameLevel == 0) {
				this.gameLevel = 1;
				loadLevel(level);
			}
			
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
		/////////////////////////////
		g.dispose();
		bs.show();
		
	}
	
	
	
	// loading the level map - funkcja do celow testowych - docelowo wczytywaniem rund bedzie kontrolowane przez osobna klase Spawner
			private void loadLevel(BufferedImage image) {
				int w = image.getWidth();
				int h = image.getHeight();
				
				// na podstawie wczytanej mapki laduje sie obszar gry 
				for(int xx = 0; xx < w; xx++) {
					for(int yy = 0; yy < h; yy++) {
						int pixel = image.getRGB(xx, yy);
						int red = (pixel >> 16) & 0xff;
						int green = (pixel >> 8) & 0xff;
						int blue = (pixel) & 0xff;
						//System.out.println("Red " + red + " green: " + green + " blue " + blue + "\n");
						if(red == 0 && green == 0 && blue == 0)
							handler.addObject(new Block(xx*32, yy*32, ObjectId.Block));
						
						if(green == 0 && red == 0 && blue == 255)
							handler.addObject(new Player(xx*32, yy*32, ObjectId.Player, handler));
						
						// generowanie elementow tla - chmury
						if(green == 255 && red == 0 && blue == 255) // kolor - cyan
							bcg_handler.addObject(new Cloud(xx*32, yy*32, ObjectId.Background, rand.nextInt(4) + 1)); // generowanie widoku chury - wybiera losowo jeden z 4 dostepnych rysunkow 
						
						// generowanie elementow tla - drzewa jesienne
						if(green == 255 && red == 255 && blue == 0) // kolor - zolty
							bcg_handler.addObject(new TreeAutumn(xx*32, yy*32 - 565, ObjectId.Background, rand.nextInt(2) + 1)); // generowanie widoku drzewa - wybiera losowo jeden z 2 dostepnych rysunkow 
						//// Uwaga !!! - wszystkie rysunki drzew powinny miec 600 px wysokosci - aby (yy*32 - 570) zawsze wyznaczylo poprawne polozenie drzewa nad poziomem terenu
						
					}
					System.out.println("\n\nLoading map in progress\n\n");
				}
				
			//	handler.addObject(new HomingMissile(100, 100, 4, 0, ID.EnemyBullet, handler)); 
				
			}
	
	public static void main(String args[]) {
		new Game();
	}
	
}
