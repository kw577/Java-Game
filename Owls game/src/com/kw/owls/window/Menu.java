package com.kw.owls.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.kw.owls.framework.STATE;
import com.kw.owls.framework.SpriteSheet;

public class Menu extends MouseAdapter implements MouseMotionListener {

	private Game game;
	private Handler handler;

	
	//Textury uzywane w menu gry
	private BufferedImage buttons = null;
	private SpriteSheet buttonsSpriteSheet = null;
	private BufferedImage summer_tree = null;
	private BufferedImage clouds = null;
	private BufferedImage flowers = null;	
	private BufferedImage owl = null;	
	
	// Grafiki do ekranu pomocy
	private BufferedImage player = null;
	private BufferedImage usualFlower = null;
	private BufferedImage waterFlower = null;
	private BufferedImage rareFlower = null;
	
	private BufferedImage daisy_av = null;
	private BufferedImage betsy_av = null;
	private BufferedImage ziggy_av = null;
	
	private BufferedImage sheep_av = null;
	private BufferedImage blackSheep_av = null;
	
	private BufferedImage spikyBush = null;
	
	// Przyciski panelu pomocy
	private BufferedImage helpButtons = null;
	private SpriteSheet helpButtonsSpriteSheet = null;
	
	//Do podswietlania przyciskow menu
	private boolean userHooverPlay = false;
	private boolean userHooverHelp = false;
	private boolean userHooverExit = false;
				
	// Do podswietlania przyciskow menu pomocy
	private boolean userHooverMenu = false;
	private boolean userHooverLeft = false;
	private boolean userHooverRight = false;
	
	private boolean GraphicsLoaded = false; // zmienna z informacja czy zostaly zaladowane grafiki gry
	
	// Do ekranu pomocy
	private int currentHelpPage = 1; // wartosc startowa strona 1 pomocy
	private int amountHelpPages = 5; // ilosc stron pomocy
	
	
	
	private Random r = new Random();
	
	public Menu(Game game, Handler handler) {
		this.game = game;
		this.handler = handler;

	}

	public void mousePressed(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		
		if(game.gameState == STATE.Menu) {
			
				
				// Przycisk Play
				if(mouseOver(mx, my, 300, 150, 200, 64)) {
					game.gameState = STATE.LoadLevel;
					
				}
		
				
				
				// Przycisk Help
				if(mouseOver(mx, my, 300, 250, 200, 64)) {
					game.gameState = STATE.Help;
					
			
					this.userHooverPlay = false;
					this.userHooverHelp = false;
					this.userHooverExit = false;
					
					// zawsze wyswietla pomoc od 1szej strony
					this.currentHelpPage = 1;
				}
				
				// Przycisk Quit
				if(mouseOver(mx, my, 300, 350, 200, 64)) {
					System.exit(1);	
				}
				
				
		
		} else if(game.gameState == STATE.Help) {
				
			// Przycisk Back
			if(mouseOver(mx, my, 300, 480, 200, 64)) {
				game.gameState = STATE.Menu;
				
				this.userHooverMenu = false;
				this.userHooverRight = false;
				this.userHooverLeft = false;
			}
	
			// Strzalka w prawo
			if(mouseOver(mx, my, 520, 492, 80, 40)) {
				
				if(this.currentHelpPage < this.amountHelpPages)
				this.currentHelpPage++;
			}
			
			// Strzalka w lewo
			if(mouseOver(mx, my, 200, 492, 80, 40)) {
				
				if(this.currentHelpPage > 1)
				this.currentHelpPage--;
			}
			
			
		}  else if(game.gameState == STATE.End) {
			
			// Przycisk powrotu do menu gry 
			if(mouseOver(mx, my, 300, 350, 200, 64)) {
				game.gameState = STATE.Menu;
				
			} 

		} else if(game.gameState == STATE.Defeat) {
			
			// Przycisk powrotu do menu gry 
			if(mouseOver(mx, my, 300, 350, 200, 64)) {
				game.gameState = STATE.Menu;
				
			} 

		} 
		
	}
	
	// Metoda interfejsu MouseMotionListener
	public void mouseMoved(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		
		if(game.gameState == STATE.Menu) {
			if(mouseOver(mx, my, 300, 150, 200, 64)) {
				userHooverPlay = true;
				userHooverHelp = false;
				userHooverExit = false;
			} 
			else if(mouseOver(mx, my, 300, 250, 200, 64)) {
				userHooverPlay = false;
				userHooverHelp = true;
				userHooverExit = false;
			} 
			else if(mouseOver(mx, my,  300, 350, 200, 64)) {
				userHooverPlay = false;
				userHooverHelp = false;
				userHooverExit = true;
			}
			else {
				userHooverPlay = false;
				userHooverHelp = false;
				userHooverExit = false;
			}
		}
		
		if(game.gameState == STATE.Help) {
			if(mouseOver(mx, my, 300, 480, 200, 64)) {
				userHooverMenu = true;
				userHooverLeft = false;
				userHooverRight = false;
			} 
			else if(mouseOver(mx, my, 200, 492, 80, 40)) {
				userHooverMenu = false;
				userHooverLeft = true;
				userHooverRight = false;
			} 
			else if(mouseOver(mx, my,  520, 492, 80, 40)) {
				userHooverMenu = false;
				userHooverLeft = false;
				userHooverRight = true;
			}
			else {
				userHooverMenu = false;
				userHooverLeft = false;
				userHooverRight = false;
			}
		}
		
	}
		
	
	public void mouseReleased(MouseEvent e) {
		
	}
	
		
	// metoda sprawdzajaca czy kliknieto w jakis przycisk menu
	private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
		if((mx > x) && (mx < x + width)) {
			if((my > y) && (my < y + height)) {
				return true;
			} else return false;
		} else return false;
	}
	
	public void tick() {
		
			
	}
	
	
	public void render(Graphics g) {
		
		// pobranie grafik
		if(!GraphicsLoaded) {
			BufferedImageLoader loader = new BufferedImageLoader();
						
			try {
				buttons = loader.loadImage("/textures/buttons_spritesheet.png");
				summer_tree = loader.loadImage("/background/tree_summer_2.png");
				clouds = loader.loadImage("/background/cloud_1.png");
				flowers = loader.loadImage("/background/flowers_1.png");
				owl = loader.loadImage("/background/owl_1.png");
				
				helpButtons = loader.loadImage("/textures/menu_help_buttons.png");
				
				// Grafiki do ekranu pomocy
				player = loader.loadImage("/player_image2.png");
				usualFlower = loader.loadImage("/textures/usual_flower_7.png");
				waterFlower = loader.loadImage("/textures/water_flower_2.png");
				rareFlower = loader.loadImage("/textures/rare_flower_3.png");
				daisy_av = loader.loadImage("/hud/daisy_av.png");
				betsy_av = loader.loadImage("/hud/betsy_av.png");
				ziggy_av = loader.loadImage("/hud/ziggy_av.png");
				
				sheep_av = loader.loadImage("/objects/sheep_av.png");
				blackSheep_av = loader.loadImage("/objects/blackSheep_av.png");
				spikyBush = loader.loadImage("/textures/spiky_bush.png");
				
			} catch (Exception e){
				e.printStackTrace();
			}
						
			buttonsSpriteSheet = new SpriteSheet(buttons);
			helpButtonsSpriteSheet = new SpriteSheet(helpButtons);
		}
		
		
		g.setColor(new Color(190, 220, 235));
		g.fillRect(0, 0, game.getWidth(), game.getHeight());
					
		
		if(game.gameState == STATE.Menu) {
			
			g.setColor(Color.white);
			//g.fillRect(170, 450, 70, 40);
			g.fillOval(192, 450, 95, 95);
			
			g.setColor(new Color(130, 180, 0));
			g.fillOval(-300, 500, 1600, 300);
			
			
			g.drawImage(summer_tree, -80, -70, null);
			g.drawImage(clouds, 550, 50, null);
			g.drawImage(clouds, 200, 0, null);
			g.drawImage(flowers, 500, 350, null);
			g.drawImage(owl, 180, 420, null);
			
			
			Font fnt = new Font("arial", 1, 50);
			Font fnt2 = new Font("arial", 1, 30);
			g.setFont(fnt);
							
			//g.setColor(Color.red);
			//g.drawString("Menu", 330, 70);
						
			
			if(!userHooverPlay)
				g.drawImage(buttonsSpriteSheet.grabImage(1, 1, 220, 84), 290, 140, null);
			else
				g.drawImage(buttonsSpriteSheet.grabImage(2, 1, 220, 84), 290, 140, null);
			
			
			//g.setFont(fnt2);
			//g.drawRect(300, 150, 200, 64);
			//g.drawString("Play", 360, 190);
					
			
			if(!userHooverHelp)
				g.drawImage(buttonsSpriteSheet.grabImage(1, 2, 220, 84), 290, 235, null);
			else
				g.drawImage(buttonsSpriteSheet.grabImage(2, 2, 220, 84), 290, 235, null);
						
			
			//g.drawRect(300, 250, 200, 64);
			//g.drawString("Help", 360, 290);
			
			
			if(!userHooverExit)
				g.drawImage(buttonsSpriteSheet.grabImage(1, 3, 220, 84), 290, 332, null);
			else
				g.drawImage(buttonsSpriteSheet.grabImage(2, 3, 220, 84), 290, 332, null);
					
			//g.drawRect(300, 350, 200, 64);
			//g.drawString("Quit", 360, 390);
						
			
		} else if(game.gameState == STATE.Help) {
			
			
			
			Font fnt = new Font("Serif", Font.BOLD, 35);
			Font fnt2 = new Font("Garamond", Font.BOLD, 25);
			Font fnt3 = new Font("Garamond", Font.BOLD, 30);
			Font fnt4 = new Font("Garamond", Font.BOLD, 22);
			
			if(this.currentHelpPage == 1) {
				g.setFont(fnt);
				g.setColor(Color.black);
				g.drawString("MAIN RULES", 280, 50);
				g.drawString("_____________", 275, 50);
				
				// Rysunek i opis gracza
				g.drawImage(player, 25, 70, null);
				g.setFont(fnt2);	
				g.drawString("Your task is to help this boy to find as many flowers as possible", 90, 100);
				g.drawString("He would like to see his beloved girlfriend and he wants", 90, 130);
				g.drawString("to give her a beautiful bouquet", 90, 160);
				
				// Rysunek i opis kwiatkow
				g.setFont(fnt3);
				g.drawString("You can find a lot of different flowers:", 25, 210);
				g.drawImage(usualFlower, 25, 230, null);
				g.drawImage(waterFlower, 20, 310, null);
				g.drawImage(rareFlower, 25, 400, null);
				
				g.setFont(fnt2);
				g.drawString("Common Flowers - You can find many varieties and colours", 90, 250);
				g.drawString("It's easy to find them - this flower will give you 1 point.", 90, 275);
				
				g.drawString("Water Flowers - You can pick them from water surface", 90, 320);
				g.drawString("Be carefull to not lose your other flowers while picking them", 90, 345);
				g.drawString("Each Water Flower will give you 2 points.", 90, 370);
				
				g.drawString("Rare Flowers - they are very bautifull and unique.", 90, 420);
				g.drawString("It's not easy to find them but each one will give you 3 points.", 90, 445);
				
			}
			
			if(this.currentHelpPage == 2) {
				g.setFont(fnt);
				g.setColor(Color.black);
				g.drawString("OWLS", 345, 50);
				g.drawString("______", 345, 50);
				
				g.setFont(fnt2);
				g.drawString("Sometimes you can meet an owl. Don't be afraid - they're very friendly.", 25, 90);
				g.drawString("They will be very happy to help you with searching flowers.", 25, 120);
				g.drawString("But each owl can help you in different way: ", 25, 150);
				
				// Rysunki i opisy sowek
				g.setColor(Color.WHITE);
				g.fillOval(70, 182, 16, 16);
				g.drawImage(daisy_av, 25, 175, null);
				g.drawImage(betsy_av, 20, 270, null);
				g.drawImage(ziggy_av, 20, 365, null);
				
				
				
				g.setColor(Color.black);
				g.drawString("Daisy - she is very smart. Loves to fly really high and fast.", 120, 190);
				g.drawString("She can help you with climbing on dangerous terrain.", 120, 220);
			
				g.drawString("Betsy - very calm owl - she really likes flowers.", 120, 285);
				g.drawString("You can send her to pick some flowers for you.", 120, 315);
				
				g.drawString("Ziggy - he is really brave and protective.", 120, 380);
				g.drawString("He will help you if anyone attack you.", 120, 410);
			
				g.drawString("Remember that only one owl can help you each round.", 25, 460);
			}
			
			if(this.currentHelpPage == 3) {
				g.setFont(fnt);
				g.setColor(Color.black);
				g.drawString("CONTROL", 310, 50);
				g.drawString("__________", 307, 50);
				
				g.setFont(fnt2);
				g.drawString(">   Use arrow keys to move left / right", 25, 100);
				g.drawString(">   Use spacebar key to jump", 25, 135);
				g.drawString(">   Use V key to throw a rock", 25, 170);
				
				g.drawString(">   If you want to ask an owl for help aim and click with a mouse", 25, 240);
				g.drawString(">   and then confirm that you need a help", 25, 275);
				
				g.drawString(">   You can ask an owl to go and wait in certain place.", 25, 335);
				g.drawString(">   If you want to just aim and click this place with a mouse.", 25, 370);
				g.drawString(">   Then if you use C key - owl will follow you again.", 25, 405);
				
				
			}
			
			if(this.currentHelpPage == 4) {
				g.setFont(fnt);
				g.setColor(Color.black);
				g.drawString("BE CAREFUL", 280, 50);
				g.drawString("_____________", 277, 50);
						
				
				// Rysunki i opisy przeciwnikow
				g.drawImage(sheep_av, 25, 85, null);
				g.drawImage(blackSheep_av, 20, 200, null);
				g.drawImage(spikyBush, 30, 320, null);
				
				
				g.setFont(fnt2);
				g.drawString("Sheep - in fact they are really nice animals. However keep", 120, 100);
				g.drawString("away from them. They won't hurt you but they can eat", 120, 130);
				g.drawString("some of your flowers.", 120, 160);
				
				
				g.drawString("Black sheep - unfriendly, aggressive and don't like strangers", 120, 215);
				g.drawString("Be careful when you meet them. They are very strong and fast", 120, 245);
				g.drawString("and they can harm you badly.", 120, 275);
				
			
				g.drawString("Spiky bush - keep away from it - don't even touch it.", 120, 330);
				g.drawString("It has extremely sharp, poisoned spikes. You will instantly", 120, 360);
				g.drawString("lose some health after touching this !!!", 120, 390);
				
								
			}
			
			if(this.currentHelpPage == 5) {
				g.setFont(fnt);
				g.setColor(Color.black);
				g.drawString("BE CAREFUL", 280, 50);
				g.drawString("_____________", 277, 50);
				
				g.setFont(fnt2);
				g.drawString(">   Be careful not to fall off a large height - in that case", 25, 100);
				g.drawString("      you will lose some health as well as you will drop some flowers.", 25, 130);
				
				g.drawString(">   Water will affect you different depending on the depth:", 25, 180);
				g.setFont(fnt4);
				g.drawString(">   You can stay in shallow water without any worries.", 75, 210);	
				g.drawString("Nor your health neither your flowers will suffer it.", 110, 235);
				
				g.drawString(">   In medium water - you can breathe freely - so it's safe for you.", 75, 280);	
				g.drawString("Unfortunately the same can not be said about your collected flowers", 110, 305);
				g.drawString("In medium water you can easily lose are destroy some of your flowers.", 110, 330);
			
				g.drawString(">   In deep water - you must sometimes jump and take a breath.", 75, 380);	
				g.drawString("If not - you can run out of air. You will get some alert when", 110, 405);
				g.drawString("you need to take a breath. In deep water you can also lose flowers", 110, 430);
				g.drawString("similar as in medium water.", 110, 455);
			
			
			}
			
			
			
			
			
			
			
			// !!!!!!!!  Ponizej elmenty renderowane na kazdej stronie pomocy
			//g.setColor(Color.red);			
			//g.drawRect(300, 480, 200, 64); // pomocniczo
			//g.drawString("Back", 360, 520);
						
						
			if(!userHooverMenu)
				g.drawImage(helpButtonsSpriteSheet.grabImage(1, 1, 220, 85), 291, 470, null);
			else
				g.drawImage(helpButtonsSpriteSheet.grabImage(2, 1, 220, 85), 290, 470, null);
				
						
			// strzalka w lewo
			//g.drawRect(200, 492, 80, 40); // obszar dzialania przycisku - pomocniczo
			if(this.currentHelpPage > 1) {
				if(!userHooverLeft)
					g.drawImage(helpButtonsSpriteSheet.grabImage(1, 3, 220, 85), 173, 468, null);
				else
					g.drawImage(helpButtonsSpriteSheet.grabImage(2, 3, 220, 85), 173, 467, null);
			}

										
			
			// strzalka w prawo
			//g.drawRect(520, 492, 80, 40); // pomocniczo
			if(this.currentHelpPage < this.amountHelpPages) {			
				if(!userHooverRight)
					g.drawImage(helpButtonsSpriteSheet.grabImage(1, 2, 220, 85), 510, 468, null);
				else
					g.drawImage(helpButtonsSpriteSheet.grabImage(2, 2, 220, 85), 509, 468, null);
			}			
			// pomocniczo do sprawdzenia zmiany stron pomocy
			g.setColor(Color.black);
			g.setFont(fnt2);
			g.drawString("Page nr: " + this.currentHelpPage, 650, 520);
						
			
		} else if(game.gameState == STATE.End) {
			Font fnt = new Font("arial", 1, 50);
			g.setFont(fnt);
			Font fnt2 = new Font("arial", 1, 30);
			Font fnt3 = new Font("arial", 1, 20);
			g.setColor(Color.white);
			g.drawString("VICTORY", 330, 70);
			
			g.setFont(fnt3);
			g.drawString("-> Your Score: " + game.getFlowers(), 50, 170);

			g.setFont(fnt2);
			g.drawRect(300, 350, 200, 64);
			g.drawString("Menu", 360, 390);
						
		} else if(game.gameState == STATE.Defeat) {
			Font fnt = new Font("arial", 1, 50);
			g.setFont(fnt);
			Font fnt2 = new Font("arial", 1, 30);
			Font fnt3 = new Font("arial", 1, 20);
			g.setColor(Color.white);
			g.drawString("You lose", 330, 70);
			
			g.setFont(fnt3);
			g.drawString("-> Your Score: " + game.getFlowers(), 50, 170);

			g.setFont(fnt2);
			g.drawRect(300, 350, 200, 64);
			g.drawString("Menu", 360, 390);
						
		} 
		
	}
	
}
