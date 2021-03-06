package com.kw.owls.window;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import com.kw.owls.framework.KeyInput;
import com.kw.owls.framework.MouseInput;
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
	private HUD hud;
	

	// Parametry gry
	public static STATE gameState = STATE.Menu;
	private int gameLevel = 0;
	private static final int max_health = 7;
	private int health = 7;  // poziom zdrowia gracza
	private int flowers = 0; // ilosc zebranych kwiatkow 
	private int lostFlowers = 0; //otrzymuje informacje z klasy Player ze nastapila utrata punktow i przekazuje ja do HUD.java	
	private int maxFlowersAmount = 0; // zlicza wszystkie kwiatki jakie sa do zdobycia
	
	
	// do sterowania sowkami - przechowuje wspolrzedne punktu wybranego przez gracza (przyciski myszy)
    private int player_click_x = -1; // -1 to wartosc startowa - oznacza ze gracz nie wybral zadnego punktu
    private int player_click_y = -1;
    
    private boolean player_supported = false;
    private boolean player_supported_daisy = false;   


	// Wspolpraca z sowkami - ktaora sowka pomaga w danej rundzie
    private boolean daisyHelping = false;
    private boolean betsyHelping = false;
    private boolean ziggyHelping = false;
    private boolean askingForHelp = false; // nie mozna jednoczesnie prosic 2 sowek o pomoc
    private String owlName = ""; 
    private boolean playerConfirm = false; // czy gracz potwierdzil chec wspolpracy z sowka
    private boolean playerReject = false; // czy gracz wybral przycisk "NIE"
    
	
	// konstruktor
	public Game() {
		new Window(800, 600, "Building game", this);
		
			
		start();
				
		handler = new Handler();
		bcg_handler = new BackgroundHandler();
		menu = new Menu(this, handler);
		spawner = new Spawner(handler, bcg_handler, this);
		hud = new HUD(this);
		camera = new Camera(0, 0); // poczatkowe polozenie kamery sledzacej gracza 
		
		
		this.addMouseListener(menu);
		this.addMouseMotionListener(menu); // podswietlanie przycikow menu gdy sie na nie najedzie myszka
		this.addKeyListener(new KeyInput(handler, this));
		
		this.addMouseListener(hud);
		this.addMouseMotionListener(hud);
		
		// do sterowania sowkami podczas gry
		this.addMouseListener(new MouseInput(handler, camera, this));
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
				//System.out.println("FPS: " + frames);
								
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
		else if(gameState == STATE.Menu || gameState == STATE.Help || gameState == STATE.End || gameState == STATE.Defeat) {
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
			
			hud.render(g); // ponizej bloku translate - ma byc wyswietlane w stalym punkcie okna gry
		}	
		else if(gameState == STATE.Menu || gameState == STATE.Help || gameState == STATE.End || gameState == STATE.Defeat) {
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
			
	
	public static int getMaxHealth() {
		return max_health;
	}
	

	public int getHealth() {
		return health;
	}


	public void setHealth(int health) {
		this.health = health;
	}


	public int getFlowers() {
		return flowers;
	}


	public void setFlowers(int flowers) {
		this.flowers = flowers;
	}
	
	
	public int getLostFlowers() {
		return lostFlowers;
	}


	public void setLostFlowers(int lostFlowers) {
		this.lostFlowers = lostFlowers;
	}
	
	public int getPlayer_click_x() {
		return player_click_x;
	}


	public void setPlayer_click_x(int player_click_x) {
		this.player_click_x = player_click_x;
	}


	public int getPlayer_click_y() {
		return player_click_y;
	}


	public void setPlayer_click_y(int player_click_y) {
		this.player_click_y = player_click_y;
	}

	
	public boolean isDaisyHelping() {
		return daisyHelping;
	}


	public void setDaisyHelping(boolean daisyHelping) {
		this.daisyHelping = daisyHelping;
	}


	public boolean isBetsyHelping() {
		return betsyHelping;
	}


	public void setBetsyHelping(boolean betsyHelping) {
		this.betsyHelping = betsyHelping;
	}


	public boolean isZiggyHelping() {
		return ziggyHelping;
	}


	public void setZiggyHelping(boolean ziggyHelping) {
		this.ziggyHelping = ziggyHelping;
	}

	public boolean isAskingForHelp() {
		return askingForHelp;
	}


	public void setAskingForHelp(boolean askingForHelp) {
		this.askingForHelp = askingForHelp;
	}

	
	public String getOwlName() {
		return owlName;
	}


	public void setOwlName(String owlName) {
		this.owlName = owlName;
	}
	
	
	public boolean isPlayerConfirm() {
		return playerConfirm;
	}


	public void setPlayerConfirm(boolean playerConfirm) {
		this.playerConfirm = playerConfirm;
	}

	
	public boolean isPlayerReject() {
		return playerReject;
	}


	public void setPlayerReject(boolean playerReject) {
		this.playerReject = playerReject;
	}

	
	
    public boolean isPlayer_supported() {
		return player_supported;
	}


	public void setPlayer_supported(boolean player_supported) {
		this.player_supported = player_supported;
	}
	
	
	public boolean isPlayer_supported_daisy() {
		return player_supported_daisy;
	}


	public void setPlayer_supported_daisy(boolean player_supported_daisy) {
		this.player_supported_daisy = player_supported_daisy;
	}
	
	
	
	public int getMaxFlowersAmount() {
		return maxFlowersAmount;
	}


	public void setMaxFlowersAmount(int maxFlowersAmount) {
		this.maxFlowersAmount = maxFlowersAmount;
	}
	
	
	public static void main(String args[]) {
		new Game();
	}
	
}
