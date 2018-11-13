package com.kw.owls.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.kw.owls.framework.GameObject;
import com.kw.owls.framework.ObjectId;
import com.kw.owls.framework.SpriteSheet;
import com.kw.owls.window.BufferedImageLoader;
import com.kw.owls.window.Game;
import com.kw.owls.window.Handler;

public class OwlDaisy extends GameObject{

    private Handler handler;
    private Game game;
    private BufferedImage owl_image;
    private BufferedImage level_map;
    private int width = 32;
    private int height = 32;
    private BufferedImage position_marking;
    private SpriteSheet positionSpriteSheet;
    private SpriteSheet owlSpriteSheet;
    
    // ask owl for help
    private boolean playerAsist = false; // okresla czy Daisy pomaga graczowi
    private boolean askedForHelp = false; // do przeprowadzania procedury nawiazywania wspolpracy
    private int askedHelpTimer = 0; // odmierza czas w ktorym trzeba sie zdecydowac na pomoc sowki
    private final int askedHelpTimerMax = 500; // po tym czasie zniknie okno nawiazywania wspolpracy
    
    
    // timery animacji
    private int mark_position_timer = 0; // odmierzanie czasu do zmiany animacji
    private int position_changeAnimation = 5; // czas po ktorym zmieni sie animacja
   
    // Wprawadzenie timera powoduje rowniez zmniejszenie losci obliczen
    private int timer = 5; // czas reakcji - czas po jakim nastapi np korekta toru lotu, lub decyzji o pomocy dla gracza
    private final int max_speed = 15;
    private float acceleration = 0.4f;
    private int speed;
    private int minSpeed = 3;
  
    // wspolrzedne punktu docelowego lotu
    private float follow_point_x;
    private float follow_point_y;
 
    // pomocniczo do skanowania terenu
    int[][] surrunding_area = new int[3][3]; // pomocniczo do ustalenie kierunku rozpoczecia ruchu
 
    // wspolrzedne pomocnicze
    int[] angle = new int[6];
    int choosed_angle;
    private float dx1;
    private float dy1;
    private float temp_velX;
    private float temp_velY;
    private int dist;
    private int wsp_velX;
    private int wsp_velY;
 
    // Do wykrywania kierunkow kolizyjnych - usprawnienie funkcji choose_direction()
    // - choose_direction() wybiera kierunek na ktorym nie znajduje sie zadna przeszkoda i jest on dodatkowo najszybszy pod wzgledem przemieszczania sie do punktu docelowego (min kat miedzy wektorem sledzacym i wektorem predkosci)
    // sporadycznie moga jednak wystepowac koizje ze wzgledu na niekorzystne ulozenie obiektow - drobne nalozenia sie obszarow kolizyjnych w ich narozach
    // wtedy wybierany jest drugi w kolejnosci kierunek (znowu po wzgledem wartosci kata)
    private int collision_timer = 0; // zlicza czas miedzy sasiednimi kolizjami
    private float prev_velX = 0; // wartosc wektora predkosci przy ktorym ostatnio doszlo do kolizji - wartosc poczatkowa
    private float prev_velY = 0; // wartosc wektora predkosci przy ktorym ostatnio doszlo do kolizji
    private int collision_dir1_counter = 0; // licznik zliczajacy ilosc kolizji typu1 - kilka razy pod rzad ten sam wektor predkosci powoduje kolizje
    private int collision_dir2_counter = 0; // licznik zliczajacy ilosc kolizji typu2 - na przemian prostopadle wektory predkosci
    /*
     * Koliza typu 2 wystepuje zazwyczaj w sekwencjach predkosci jak: [-3,-3], [0,3], [-3, 3] [0, -3]
     * czyli po wykryciu kolizji w ruchu na wprost - zostaje wybrany w funcji choose_direction() kierunek [0,3]
     * nastepnie funkcja follow_point() zmienia kierunek na [-3,3] ale poniewaz wystepujacy na wprost blok nie zostal jeszcze ominiety
     * wystepuje kolizja i ponowne uruchomienie funkcji choose_direction(), ktora wybiera kierunek [0, -3] a  potem
     * follow_point() zmienia go na [-3, -3] ale znowu nie udalo sie calkiem ominac kolidujacy blok
     * rozwiazaniem w takim przypadku jest opoznienie wlaczenia funckji follow_point()
     *
     * */
   
    private int sequence_collision_timer = 8; // czas w ktorym jesli wystapia 2 kolizje o takich samych zwrotach (typ 1) - zostana zaliczone do sekwencji
    private int sequence_perpendicular_collision_timer = 20; // czas w ktorym jesli wystapia 2 kolizje dla wektorow prostopadlych (typ 2) - zostana zaliczone do sekwencji, 20 - ustalone na podstawie obserwacji logow
   
    
    
    // do renderowania animacji
 	private boolean turned_left = false; // rysunki animacji gdy gracz jest zwrocony w lewo
 	private boolean turned_right = true; // rysunki animacji gdy gracz jest zwrocony w prawo - domyslnie gre zaczyna sie animacja tego typu
   
    // timery animacji
    private int owl_anim_timer = 0; // odmierzanie czasu do zmiany animacji
    private int owl_changeAnimation = 8; // czas po ktorym zmieni sie animacja
   
    
    // do funkcji oceny terenu (niebezpieczenstwa podczas wspinaczki)
    private int check_distance = 12; // taka odleglosc bedzie sprawdzona (tyle kafelkow terenu przed graczem)
    int[] terrain_safety = new int[check_distance]; // przechowuje informacje o bezpieczenstwie terenu - 1 - oznacza ze jest punkt na ktory mozna skoczyc, 0 - oznacza dol
    private int rowsAmount = 8; // ilosc skanowanych kafelkow terenu w kazdej kolumnie - (3 + 1 + 4) - gracz potrafi skoczyc na wys. 3 kafelkow 1 - kafelek na ktorym stoi, 4 - kafelki ponizej
    private int check_safety_timer = 0; // czas co jaki bedzie sprawdzane bezpieczenstwo terenu
    private final int check_safety_timerStart = 30;
    int[] reference_levels = new int[check_distance]; // zapisuje poziomy odniesienia dla kazdej analizowanej kolumny mapy - poziom odniesienia -> wyjasnienie w opisie funcji check_terrain_safety()
    private int helpFactor = 4; // zalezy od aktualnego poziomu zdrowia - okresla jak duza musi byc przepasc w terenie aby Daisy automatycznie pomogla graczowi
    private boolean player_turned_right = true;
    private boolean player_turned_left = false;
    private boolean update_help_point = false;
    
    
    // wspolrzedne punktu pomocy przy wspinaczce
    private float help_point_x = -1;
    private float help_point_y = -1;
    
    
    public OwlDaisy(int x, int y, ObjectId id, Handler handler, Game game) {
        super(x, y, id);
        this.handler = handler;
        this.game = game;
     
        BufferedImageLoader loader = new BufferedImageLoader();
     
     
        try {
            owl_image = loader.loadImage("/owls/daisy_spritesheet.png");
            level_map = loader.loadImage("/levels/Level_" + game.getGameLevel() + ".png");
            position_marking = loader.loadImage("/others/position_marking.png");
        } catch (Exception e){
            e.printStackTrace();
        }
       
       
              
        positionSpriteSheet = new SpriteSheet(position_marking);
        owlSpriteSheet = new SpriteSheet(owl_image);
        
       
    }


    public void tick() {
      
    	    	
    	if(this.playerAsist) {
    		 //////////////////////////////////////////////////////////////////
            // W KAZDYM CYKLU GRY
            timer--;
            x += velX;
            y += velY;
         
            // granice planszy - musza byc ustawione gdyz jesli Daisy zblizy sie do granicy planszy
            // funkcja readMap() bedzie chciala pobrac z mapy piksele o wartosci ujemnej co wyrzuci blad
            // zamiast ustalania granic w ponizszy sposob mozna by tez modyfikowac funkcje read map
             if(x < 32) x = 32;
             if(x > 15900) x = 15900;
             //if(y < 150) y = 150; // granica gorna planszy  
             //////////////////
                 
         
            // te wartosci musza byc obliczane na poczatku kazdej funkcji tick()
            
            if(game.getPlayer_click_x() > 0 && game.getPlayer_click_y() > 0) {
                this.follow_point_x = game.getPlayer_click_x();
                this.follow_point_y = game.getPlayer_click_y();
            }
            else if(this.help_point_x > 0 && this.help_point_y > 0) {
            	this.follow_point_x = this.help_point_x;
            	this.follow_point_y = this.help_point_y;
            }
            else {
                this.follow_point_x = handler.getPlayer_x() - 50;
                this.follow_point_y = handler.getPlayer_y() - 100;
            }
            
         
            dx1 = follow_point_x - this.x;
            dy1 = follow_point_y - this.y;
         
            dist = (int) (Math.sqrt(dx1 * dx1 + dy1 * dy1));
           
            // ustalanie kierunku i sybkosci poruszania sie
            set_velocity_vector();
           
            //////////////////////////////////////////////////////////////////         
           
             
         
            // Sprawdzane co 5 cykli gry
            if (timer <= 0) {
                timer = 5;
             
                //////////////////
                // Ustalanie punktu docelowego lotu
             
             
             
                this.follow_point(follow_point_x, follow_point_y);
             
            }
         
          
            // Sprawdzana w kazdym cyklu gry
            collision();
             
          
            //System.out.println("");
            
            //System.out.println("\n\n\nPlayer turned_right: " + this.player_turned_right);
            //System.out.println("\nPlayer turned_left: " + this.player_turned_left);
            
            // Ocena terenu przed graczem
            this.playerDirection();
            this.check_safety_timer++;
            
            
            //if(game.isPlayer_supported()) this.update_help_point = true;
            
           // if(game.isPlayer_supported_daisy()) this.update_help_point = false;
            
            
            if(game.isPlayer_supported() && !game.isPlayer_supported_daisy()) this.update_help_point = true;
            else this.update_help_point = false;
            
            //System.out.println("\nPlayer supported: " + game.isPlayer_supported());
            //System.out.println("\nPlayer supported on Daisy: " + game.isPlayer_supported_daisy());
            //System.out.println("\nupdate_help_point: " + this.update_help_point);
            
            if(this.check_safety_timer >= this.check_safety_timerStart && this.update_help_point) { // game.isPlayer_supported()  - nie liczy nowego help_point gdy gracz skacze
            	//System.out.println("\nPlayer wsp. X: " + handler.getPlayer_x());
            	
            	if(player_turned_right && handler.getPlayer_y() < 1200 && handler.getPlayer_x() < 15500) {
            		this.check_terrain_safety((int)((handler.getPlayer_x() + 16)/32), (int)((handler.getPlayer_y() + 16)/32 + 3), check_distance, rowsAmount);
   
            	}
            	else if(player_turned_left && handler.getPlayer_y() < 1200 && handler.getPlayer_x() > 400) {
            		this.check_terrain_safety2((int)((handler.getPlayer_x() + 16)/32), (int)((handler.getPlayer_y() + 16)/32 + 3), check_distance, rowsAmount);
   
            	}
            	
            	
            	this.check_safety_timer = 0;
            }
            
                    
            
            
            
    	} 
    	
    	if (!this.playerAsist) {
    		
    		wait_for_player();
    		
    	}
       
    }
     
   
    
    
    
    private void wait_for_player() {
		// sprawdza czy player wybral daisy do pomocy 
		//System.out.println("\naskedForHelpTimer: " + this.askedHelpTimer);
		// w kazdej rundzie mozna wybrac tylko 1 sowke do pomocy i nie mozna juz zmienic wyboru az do zakonczenia rundy
		if(!game.isAskingForHelp() && !game.isDaisyHelping() && !game.isBetsyHelping() && !game.isZiggyHelping()) {
			//System.out.println("\nWaiting for player");
			if(this.getBounds3().contains(new Point(game.getPlayer_click_x(),game.getPlayer_click_y()))) {
				//System.out.println("\nChecking point");
				game.setAskingForHelp(true);
				game.setOwlName("Daisy");
				this.askedForHelp = true;
				
				//System.out.println("\n\n\nDaisy will be helping you");
				//game.setDaisyHelping(true);
    			//this.playerAsist = true;
    			
    			game.setPlayer_click_x(-1);
    			game.setPlayer_click_y(-1);
    		}
		}
		
		else if(this.askedForHelp) {
			askedHelpTimer++;
			
			game.setPlayer_click_x(-1);
			game.setPlayer_click_y(-1);
			// uplynal czas 
			if(askedHelpTimer > askedHelpTimerMax) {
				game.setAskingForHelp(false);
				game.setOwlName("");
				this.askedForHelp = false;
				this.askedHelpTimer = 0;
			}
		}
		
		if(this.askedForHelp && game.isPlayerConfirm()) {
			this.playerAsist = true;
			game.setDaisyHelping(true);
			
			game.setAskingForHelp(false);
			game.setPlayerConfirm(false);
			this.askedForHelp = false;
			this.askedHelpTimer = 0;
			
		}
		if(this.askedForHelp && game.isPlayerReject()) {
			game.setAskingForHelp(false);
			game.setOwlName("");
			game.setPlayerReject(false);
			this.askedForHelp = false;
			this.askedHelpTimer = 0;
		}
    }
   
    // Funkcja ustala wektor predkosci - kierunek - wartosc oraz wektor rozpoczecia ruchu
    private void set_velocity_vector(){
       
        // Rozpoczecie ruchu - w sytuacji gdy poprzednio velX = velY = 0
        if(velX == 0 && velY == 0 && dist > 50) {
         
            this.choose_direction();
        }
       
      
        // Ustalanie szybkosci - w kazdym cyklu
        // zmiana kierunkow nastepuje co 6 cykli, lub po kolizji czyli po ustalenie predkosci min 6 cykli bedzie z ta predkoscia
           // omijajac przeszkody szybkosc wynosi minSpeed
           // na prostych odcinkach szybkosc jest wyzsza i zalezy od odleglosci od gracza i jego szybkosci
         
        // gracz porusza sie
           if(Math.abs(velX) > 0 && velY == 0 && dist > 50 && Math.abs(handler.getPlayer_velX()) > 4 ) {
               if(velX > 0) velX = Math.abs(handler.getPlayer_velX());
               if(velX < 0) velX = -Math.abs(handler.getPlayer_velX());
           }
         
         if(Math.abs(velX) > 0 && velY == 0 && dist > 100 && Math.abs(handler.getPlayer_velX()) > 4 ) {
               if(velX > 0) velX = (float)1.20 * Math.abs(handler.getPlayer_velX());
               if(velX < 0) velX = -(float)1.20 * Math.abs(handler.getPlayer_velX());
           }
             
        // gracz nie porusza sie            
           if(Math.abs(velX) > 0 && velY == 0 && dist < 200 && dist >= 50 && handler.getPlayer_velX() == 0 ) {
               if(velX > 0) velX = minSpeed;
               if(velX < 0) velX = -minSpeed;
           }
         
        if(Math.abs(velX) > 0 && velY == 0 && dist >= 200 && handler.getPlayer_velX() == 0 ) {
               if(velX > 0) velX = 2*minSpeed;
               if(velX < 0) velX = -2*minSpeed;
           }
         
           // omijanie przeszkod odbywa sie z predkoscia nie wieksza niz 6 - tylko na prostych odcinkach wyzsza szybkosc
           if(Math.abs(velX) >= 6 && Math.abs(velY) >= 6) {
               if(velX > 0) velX = minSpeed;
               if(velX < 0) velX = -minSpeed;
               if(velY > 0) velY = minSpeed;
               if(velY < 0) velY = -minSpeed;
           }
           if(Math.abs(velX) > 0 && velY == 0 && dist > 700) {
        	   if(velX > 0) velX = max_speed;
               if(velX < 0) velX = -max_speed;
           }
               
         
               
        if(dist < 50) {
                   
            velX = 0;
            velY = 0;
        }
      
      
        //System.out.println("Owl Daisy - velX: " + velX + "  velY: " + velY + " dist: " + dist);
      
      
      
    }
     
    private void collision() {
        this.collision_timer++;
       
        //System.out.println("\nSprawdzam kolizje - timer: " + collision_timer + "                        licz.kol.1: " + collision_dir1_counter + "    licz.kol.2: " + collision_dir2_counter);
       
        // Zabezpieczenie zeby nie przekroczyc zasiegu int
        if(collision_timer > 9999999) {
            collision_timer = 0;
        }
       
       
        for(int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);
           
            if(tempObject.getId() == ObjectId.Block || tempObject.getId() == ObjectId.Ground) {
                         
                         
                if(getBounds1().intersects(tempObject.getBounds())) {
                    // jezeli wystapi kolizja zapamietuje na jakim kierunku i zeruje licznik czasu do nastepnej kolizji
                    //System.out.println("\n##############################################################");
                    //System.out.println("\nKolizja !!!! - od poprzedniej kolizji minelo: " + collision_timer);
                    //System.out.println("\nvelX: " + velX + " velY: " + velY);
                    //System.out.println("\nprev_velX: " + prev_velX + " prev_velY: " + prev_velY);
                    // kolizja - kilka razy ten sam kierunek predkosci powoduje kolizje
                    if(prev_velX == velX && prev_velY == velY && collision_timer < sequence_collision_timer) {
                        this.collision_dir1_counter++;
                        //System.out.println("\nKolizja typu 1 - ten sam kierunek i zwrot po raz: " + collision_dir1_counter);
                       
                    }
                   
                    // kolizja - kilka razy na przemian prostopadle kierunki predkosci powoduja kolizje
                    if((prev_velX * velX + prev_velY*velY == 0) && collision_timer < (sequence_perpendicular_collision_timer)) { // (sequence_perpendicular_collision_timer) - wartosc przyjeta na podstawie obserwacji - z logow wynika ze cykl takiej kolizji wychodzi ok 12 - 16
                        this.collision_dir2_counter++;
                        //System.out.println("\nKolizja typu 2 - wektory prostopadle - po raz: " + collision_dir2_counter);
                       
                    }
                   
                    // zaamietuje kierunki przy ktorych ostatnio doszlo do kolizji
                    this.prev_velX = velX;
                    this.prev_velY = velY;
                   
                    // zaczyna odmierzac czas do kolejnej kolizji
                    collision_timer = 0;
                   
                   
                   
                   
                    // zachowanie OwlDaisy po kolizji
                    this.x -= 3*velX;
                    this.y -= 3*velY;
                 
                    this.velX = 0;
                    this.velY = 0;
                   
                }
                         
            }
         
        } 
       
     if(this.collision_timer >= sequence_collision_timer) {
         this.collision_dir1_counter = 0;
     }
    
     if(this.collision_timer >= sequence_perpendicular_collision_timer) {
         this.collision_dir2_counter = 0;
     }
   
       
    }


    private void follow_point(float point_x, float point_y) {
     
        //info
        //System.out.println("\nFollow point: " + point_x + " " + point_y);
        //System.out.println("\nOwlDaisy: x: " + x + "    y: " + y);
      
        // obliczanie wektora odleglosci do punktu docelowego
        dx1 = point_x - this.x;
        dy1 = point_y - this.y;
     
        // ustalanie odleglosci od punktu docelowego
        dist = (int) (Math.sqrt(dx1 * dx1 + dy1 * dy1));
 
     
     
        temp_velX = velX;
        temp_velY = velY;
                 

     
        // w zaleznosci od aktualnego wektora predkosci
        if(velX != 0 && velY == 0) {
             
            // sprawdzam wektor (velX, 0) - zachowanie aktualnego wektora predkosci
            angle[0] = (int) angle(dx1, dy1, velX, 0);
         
            // sprawdzam wektor (velX, +velY) - wtedy velY = abs(velX)
            angle[1] = (int) angle(dx1, dy1, velX, Math.abs(velX));
                     
            // sprawdzam wektor (velX, -velY) - wtedy velY = abs(velX)
            angle[2] = (int) angle(dx1, dy1, velX, -Math.abs(velX));
         
            // sprawdzam kolizyjnosc kierunkow angle0, angle1, angle2
            angle[3] = wall_collision(velX, velY);
            angle[4] = wall_collision(velX, Math.abs(velX));
            angle[5] = wall_collision(velX, -Math.abs(velX));
         
         
            // Wybieram najmniejszy kat
            choosed_angle = this.choose_angle();
            //System.out.println("\nWybrano kat na pozycji " + choosed_angle);
         
            //System.out.println("\nOwlDaisy: velX: " + velX + "    velY: " + velY);
            if(choosed_angle == 0)
                temp_velY = 0;
            else if(choosed_angle == 1)
                temp_velY = Math.abs(velX);
            else if(choosed_angle == 2)
                temp_velY = -Math.abs(velX);
                         
         
            velY = temp_velY;     
         
        } else if (velX == 0 && velY != 0) {
         
            // sprawdzam wektor (0, velY) - zachowanie aktualnego wektora predkosci
            angle[0] = (int) angle(dx1, dy1, 0, velY);
                     
            // sprawdzam wektor (+velX, velY) - wtedy velX = abs(velY)
            angle[1] = (int) angle(dx1, dy1, Math.abs(velY), velY);
                                 
            // sprawdzam wektor (-velX, velY) - wtedy velX = - abs(velY)
            angle[2] = (int) angle(dx1, dy1, -Math.abs(velY), velY);
                 
            // sprawdzam kolizyjnosc kierunkow angle0, angle1, angle2
            angle[3] = wall_collision(velX, velY);
            angle[4] = wall_collision(Math.abs(velY), velY);
            angle[5] = wall_collision(-Math.abs(velY), velY);
         
         
            // Wybieram najmniejszy kat
            choosed_angle = this.choose_angle();
            //System.out.println("\nWybrano kat na pozycji " + choosed_angle);
         
            if(choosed_angle == 0)
                temp_velX = 0;
            else if(choosed_angle == 1)
                temp_velX = Math.abs(velY);
            else if(choosed_angle == 2)
                temp_velX = -Math.abs(velY);
         
                         
            velX = temp_velX;
        } else if (velX != 0 && velY != 0) {
     
         
            // sprawdzam wektor (velX, velY) - zachowanie aktualnego wektora predkosci
            angle[0] = (int) angle(dx1, dy1, velX, velY);
                                 
            // sprawdzam wektor (velX, 0)
            angle[1] = (int) angle(dx1, dy1, velX, 0);
                                             
            // sprawdzam wektor (0, velY)
            angle[2] = (int) angle(dx1, dy1, 0, velY);
                     
            // sprawdzam kolizyjnosc kierunkow angle0, angle1, angle2
            angle[3] = wall_collision(velX, velY);
            angle[4] = wall_collision(velX, 0);
            angle[5] = wall_collision(0, velY);
         
         
            // Wybieram najmniejszy kat
            choosed_angle = this.choose_angle();
            //System.out.println("\nWybrano kat na pozycji " + choosed_angle);
         
            if(choosed_angle == 0) {
                temp_velX = velX;
                temp_velY = velY;
            } 
            else if(choosed_angle == 1)
                temp_velY = 0;
            else if(choosed_angle == 2)
                temp_velX = 0;
         
            velX = temp_velX;
            velY = temp_velY;
        }
     
     
     
     
     
    }
     
     
     
     
     
 

 
 
 
 
 
 
    public void render(Graphics g) {
       
        // zaznacza punkt wybrany przez gracza do sterowania ruchem OwlDaisy
       
        if(this.playerAsist && game.getPlayer_click_x() > 0 && game.getPlayer_click_x() > 0) {
            g.setColor(Color.red);
            //g.fillOval((int)game.getPlayer_click_x(), (int)game.getPlayer_click_y(), 32, 32);
           
            this.mark_position_timer++;
           
            g.drawImage(positionSpriteSheet.grabImage((int)(mark_position_timer/position_changeAnimation + 1), 1, 30, 37), (int)(game.getPlayer_click_x() - 15), (int)(game.getPlayer_click_y()-30), null);
           
            if(this.mark_position_timer >= (position_changeAnimation * 6 - 1)) {
                this.mark_position_timer = 0;
            }
        }
       
       
        
        
        
        ///// renderowanie Daisy gdy pomaga graczowi
        if(this.playerAsist) {
        
	        this.owl_anim_timer++;
	        direction();
	        
	        g.setColor(Color.white);
	        
	        if(turned_right) {
	        	g.fillOval((int) (x + 15), (int) (y+5), 17, 17);
	        	g.drawImage(owlSpriteSheet.grabImage((int)(owl_anim_timer/owl_changeAnimation + 1), 3, 100, 50), (int)(x - 37), (int)(y-7), null);
	        }
	        	
	        if(turned_left) {
	        	g.fillOval((int) (x), (int) (y+5), 17, 17);
	        	g.drawImage(owlSpriteSheet.grabImage((int)(owl_anim_timer/owl_changeAnimation + 1), 1, 100, 50), (int)(x - 31), (int)(y-7), null);
	            
	        }
	        
	        if(this.owl_anim_timer >= (owl_changeAnimation * 4 - 1)) {
	            this.owl_anim_timer = 0;
	        }
        }
        
        // renderwanie Daisy gdy czeka na gracza
        if(!this.playerAsist) {
        	g.setColor(Color.white);
        	if((handler.getPlayer_x() - this.x) >= 0) { // Daisy patrzy w prawo
        		g.fillOval((int) (x + 15), (int) (y+5), 17, 17);
	        	g.drawImage(owlSpriteSheet.grabImage((int)(1), 4, 100, 50), (int)(x - 37), (int)(y-7), null);
        	}
        	else { // Daisy patrzy w lewo
        		g.fillOval((int) (x), (int) (y+5), 17, 17);
	        	g.drawImage(owlSpriteSheet.grabImage((int)(1), 2, 100, 50), (int)(x - 31), (int)(y-7), null);
	            
        	}
        }
        
        //////////////
        
        g.setColor(Color.green);
        //g.fillOval((int)x, (int)y, 32, 32);
     
        
     
        //g.setColor(Color.cyan);
        //g.fillRect((int)x, (int)y, (int)width, (int)height);
     
        //g.drawImage(textures.getSpiky_bush(), (int)x, (int)y, null);
     
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.RED);
     
      
      
        // POMOCNICZE - NIE USUWAC !!!
     
        // strefy "widzenia" - do podstawowej funkcji ruchu
        //g2d.draw(getBounds2(0,-2));
        //g2d.draw(getBounds2(0,-3));
        //g2d.draw(getBounds2(0,-6));
     
        //g2d.draw(getBounds2(0,2));
        //g2d.draw(getBounds2(0,4));
        //g2d.draw(getBounds2(0,6));
     
        //g2d.draw(getBounds2(-2,0));
        //g2d.draw(getBounds2(-4,0));
        //g2d.draw(getBounds2(-6,0));
     
     
        //g2d.draw(getBounds2(2,0));
        //g2d.draw(getBounds2(4,0));
        //g2d.draw(getBounds2(6,0));
     
     
        //g2d.draw(getBounds2(2,2));
        //g2d.draw(getBounds2(4,4));
        //g2d.draw(getBounds2(6,6));
     
     
        //g2d.draw(getBounds2(-2,2));
        //g2d.draw(getBounds2(-4,4));
        //g2d.draw(getBounds2(-6,6));
     
        //g2d.draw(getBounds2(2,-2));
        //g2d.draw(getBounds2(4,-4));
        //g2d.draw(getBounds2(6,-6));
     
     
        //g2d.draw(getBounds2(-2,-2));
        //g2d.draw(getBounds2(-4,-4));
        //g2d.draw(getBounds2(-6,-6));
     
        // obrys do wykrywania kolizji z elementami otoczenia
        //g.setColor(Color.green);
        //g2d.draw(getBounds1());
        //g.setColor(Color.cyan);
        //g2d.draw(getBounds3());

        //g.setColor(Color.red);
        //g2d.draw(getBounds());
    }


   private void choose_direction(){
       // (this.x + width/2)/32 - 1) - skanuje teren dookola OwlDaisy - start w pkt (-1, -1) wzg polozenia OwlDaisy
       this.readMap((int)((this.x + width/2)/32 - 1), (int)((this.y + height/2)/32 - 1), 3, 3);
    
       //System.out.println("OwlDaisy - Tabela terenu otaczajacego");
       for(int xx = 0; xx < 3; xx++) {
           for(int yy = 0; yy < 3; yy++) {
               //System.out.print(surrunding_area[yy][xx] + " ");
           }
           //System.out.println("");
       }
    
    
       /* zmiany w tabeli ruchu - ustalenie priorytetu pol - jesli na jadnym z srodkow bokow skanowanego obszaru (wsp (1,0), (1,2), (0,1), (2,1))
          jest 1 - czyli teren ktory nie powoduje kolizje - przylegajace do niego elementy narozna o wartosci 0 - zostaja zmienione na 3
          jesli na jadnym z narozy skanowanego obszaru (wsp (0,0), (0,2), (2,0), (2,2)) jest 1
          - przylegajace do tego naroza elementy przymia wartosc 2
          w pierwszej kolejnosci beda wybierane kierunki oznaczone jako 0 a dopiero potem te oznaczone jako 2, 3
          gdyz przemieszczanie sie w takim elemencie naroznym jest nadal podatne na kolizje
       */
    
       // priorytetyzowanie kierunkow - w pierwszej kolejnosci wybiera sie pola oznaczone jako 0 a potem jako 2 (1 oznaczaja pola kolizyjne)
       if (surrunding_area[0][0] == 1) {
           if(surrunding_area[0][1] == 0) surrunding_area[0][1] = 2;
           if(surrunding_area[1][0] == 0) surrunding_area[1][0] = 2;
       }
       if (surrunding_area[2][0] == 1) {
           if(surrunding_area[1][0] == 0) surrunding_area[1][0] = 2;
           if(surrunding_area[2][1] == 0) surrunding_area[2][1] = 2;
       }
       if (surrunding_area[0][2] == 1) {
           if(surrunding_area[0][1] == 0) surrunding_area[0][1] = 2;
           if(surrunding_area[1][2] == 0) surrunding_area[1][2] = 2;
       }
       if (surrunding_area[2][2] == 1) {
           if(surrunding_area[1][2] == 0) surrunding_area[1][2] = 2;
           if(surrunding_area[2][1] == 0) surrunding_area[2][1] = 2;
       }
       if (surrunding_area[0][1] == 1) {
           if(surrunding_area[0][0] == 0) surrunding_area[0][0] = 3;
           if(surrunding_area[0][2] == 0) surrunding_area[0][2] = 3;
       }
       if (surrunding_area[2][1] == 1) {
           if(surrunding_area[2][0] == 0) surrunding_area[2][0] = 3;
           if(surrunding_area[2][2] == 0) surrunding_area[2][2] = 3;
       }
       if (surrunding_area[1][0] == 1) {
           if(surrunding_area[0][0] == 0) surrunding_area[0][0] = 3;
           if(surrunding_area[2][0] == 0) surrunding_area[2][0] = 3;
       }
       if (surrunding_area[1][2] == 1) {
           if(surrunding_area[0][2] == 0) surrunding_area[0][2] = 3;
           if(surrunding_area[2][2] == 0) surrunding_area[2][2] = 3;
       }
     
     
     
     
     
    
       //System.out.println("OwlDaisy - Tabela terenu otaczajacego z priorytetem wyboru pol");
       for(int xx = 0; xx < 3; xx++) {
           for(int yy = 0; yy < 3; yy++) {
               //System.out.print(surrunding_area[yy][xx] + " ");
           }
           //System.out.println("");
       }
    
       // Jesli zostanie wykryta sekwencja kolizji na tym samym kierunku (wiecej niz 5 razy z rzedu)
       if (collision_dir1_counter > 5) {
           int change_value_x = 1;
           int change_value_y = 1;
          
           if(prev_velX < 0) change_value_x = 0;
           if(prev_velX == 0) change_value_x = 1;
           if(prev_velX > 0) change_value_x = 2;
          
           if(prev_velY < 0) change_value_y = 0;
           if(prev_velY == 0) change_value_y = 1;
           if(prev_velY > 0) change_value_y = 2;
          
           // zmiana priorytetu wyboru tego kierunku na nizszy
           surrunding_area[change_value_x][change_value_y] = 2;
          
           //System.out.println("/n####################### UWAGA #################################3");
            //System.out.println("/nOwlDaisy - Tabela terenu otaczajacego z uwzglednieniem sekwencyjnych kolizji");
               for(int xx = 0; xx < 3; xx++) {
                   for(int yy = 0; yy < 3; yy++) {
                       //System.out.print(surrunding_area[yy][xx] + " ");
                   }
                   //System.out.println("");
               }
          
         
           // zerowanie licznikow kolizji
           collision_dir1_counter = 0;
          
       }
      
      
      
      
      
      
      
      
    
       // Wybor kierunku ruchu na podstawie tabeli terenu z priorytetami
    
    
    
     
     
     //int choose_x; // wspolrzedna x kafelka w tabeli terenu
        //int choose_y; // wspolrzedna y kafelka w tabeli terenu
        int angle_min = 9999; // minimalny kat - wartosc startowa
        int current_angle; // kat w aktualnym cyklu petli for
        int current_velX = minSpeed;
        int current_velY = minSpeed;
        int best_velX = minSpeed; // wartosc startowa
        int best_velY = minSpeed; // wartosc startowa
      
        for(int xx = 0; xx < 3; xx++) {
            for(int yy = 0; yy < 3; yy++) {
                if(surrunding_area[xx][yy] == 0) {
                  
                    if(xx == 0 && yy == 0) {
                        current_velX = -minSpeed;
                        current_velY = -minSpeed;
                        //System.out.println("\n 1 ");
                    }
                    if(xx == 1 && yy == 0) {
                        current_velX = 0;
                        current_velY = -minSpeed;
                        //System.out.println("\n 2 ");
                    }
                    if(xx == 2 && yy == 0) {
                        current_velX = minSpeed;
                        current_velY = -minSpeed;
                        //System.out.println("\n 3 ");
                    }
                    if(xx == 0 && yy == 1) {
                        current_velX = -minSpeed;
                        current_velY = 0;
                        //System.out.println("\n 4 ");
                    }
                    if(xx == 1 && yy == 1) {
                        //System.out.println("\n 5 ");
                        continue;
                    }
                    if(xx == 2 && yy == 1) {
                        current_velX = minSpeed;
                        current_velY = 0;
                        //System.out.println("\n 6 ");
                    }
                    if(xx == 0 && yy == 2) {
                        current_velX = -minSpeed;
                        current_velY = minSpeed;
                        //System.out.println("\n 7 ");
                    }
                    if(xx == 1 && yy == 2) {
                        current_velX = 0;
                        current_velY = minSpeed;
                        //System.out.println("\n 8 ");
                    }
                    if(xx == 2 && yy == 2) {
                        current_velX = minSpeed;
                        current_velY = minSpeed;
                        //System.out.println("\n 9 ");

                    }
                  
                    current_angle = (int) angle(dx1, dy1, current_velX, current_velY);
                    //System.out.println("\n current_angle: " + current_angle);
                    if(current_angle < angle_min) {
                        angle_min = current_angle;
                        best_velX = current_velX;
                        best_velY = current_velY;
                    }
                    //System.out.println("\n Angle_min: " + angle_min);
                }
  
            }
          
        }
      
        // jesli w surrounding_area nie bylo zadnego pola oznaczonego jako 0 - w nastepnej kolejnosci beda brane pod uwage pola oznaczone jako 2
        if(angle_min == 9999) { // angle_min nie zmieni wartosci - tylko gdy nie bylo ani jednego pola "0"
            //System.out.println("\nPrzeszukiwanie pol o wartosci kolizyjnosci - 2");
            for(int xx = 0; xx < 3; xx++) {
                for(int yy = 0; yy < 3; yy++) {
                    if(surrunding_area[xx][yy] == 2) {
                        // mozna w ten sposob rowniez opisac warunki dla surrunding_area[xx][yy] == 0  - ale jest to wtedy mniej czytelne
                        if(xx == 0) current_velX = -minSpeed;
                        if(xx == 1) current_velX = 0;
                        if(xx == 2) current_velX = minSpeed;
                        if(yy == 0) current_velY = -minSpeed;
                        if(yy == 1) current_velY = 0;
                        if(yy == 2) current_velY = minSpeed;
                      
                        current_angle = (int) angle(dx1, dy1, current_velX, current_velY);
                        if(current_angle < angle_min) {
                            angle_min = current_angle;
                            best_velX = current_velX;
                            best_velY = current_velY;
                        }
                    }
      
                }
              
            }
        }
      
        // jesli w surrounding_area nie bylo zadnego pola oznaczonego jako 0 ani jako 2 - w nastepnej kolejnosci beda brane pod uwage pola oznaczone jako 3
        if(angle_min == 9999) { // angle_min nie zmieni wartosci - tylko gdy nie bylo ani jednego pola "0"
            //System.out.println("\nPrzeszukiwanie pol o wartosci kolizyjnosci - 3");
            for(int xx = 0; xx < 3; xx++) {
                for(int yy = 0; yy < 3; yy++) {
                    if(surrunding_area[xx][yy] == 3) {
                      
                        if(xx == 0) current_velX = -minSpeed;
                        if(xx == 1) current_velX = 0;
                        if(xx == 2) current_velX = minSpeed;
                        if(yy == 0) current_velY = -minSpeed;
                        if(yy == 1) current_velY = 0;
                        if(yy == 2) current_velY = minSpeed;
                      
                        current_angle = (int) angle(dx1, dy1, current_velX, current_velY);

                        if(current_angle < angle_min) {
                            angle_min = current_angle;
                            best_velX = current_velX;
                            best_velY = current_velY;
                        }

                    }
      
                }
              
            }
        }
      
        //System.out.println("\nBest vel_X: " + best_velX + "Best vel_Y: " + best_velY);
     
     
       this.velX = best_velX;
       this.velY = best_velY;
     
            
     
       /*
     
       // do testow - wektor poczatkowy ruchu
       wsp_velX = (int) Math.round(dx1/dist);
       wsp_velY = (int) Math.round(dy1/dist);
                
       velX = 4 * wsp_velX;
       velY = 4 * wsp_velY;
                
       // gracz sie nie porusza
       if(handler.getPlayer_velX() == 0) {
           velX = 4 * wsp_velX;
           velY = 4 * wsp_velY;
                
       }
    
       */
     
     
       //System.out.println("\nUstalanie nowego wektora predkosci: " + velX + " " + velY);
    
       // po ustaleniu kierunku poczatkowego jest on utrzymywany przez co najmniej 10 cykli
       timer = 10;
      
      
      
      
      
       // Jesli zostanie wykryta sekwencja kolizji na tym samym kierunku (wiecej niz 5 razy z rzedu)
       if (collision_dir2_counter > 5) {
          
          
           //System.out.println("/n####################### UWAGA #################################");
           //System.out.println("/nZwiekszono timer do ze wzgledu na wystapienie sekwencji kolizji na prostopadlych wektorach predkosci");
          
           timer = 15; // opoznienie funckji follow_point()
         
           // zerowanie licznikow kolizji
           collision_dir2_counter = 0;
          
       }
      
      
      
   }
 
 
    // loading the level map (start_x, start_y) - wspolrzedne pixela od ktorego zacyna sie przeszukiwanie mapy, amount_columns - ilosc kolumn pixeli
    private void readMap(int start_x, int start_y, int amount_columns, int amount_rows) {
        //int w = level_map.getWidth();
        //int h = level_map.getHeight();
     
        //System.out.println("\nPixel start: " + start_x + " " + start_y);
     
        // na podstawie wczytanej mapki laduje sie obszar gry
        for(int xx = 0; xx < amount_columns; xx++) {
            for(int yy = 0; yy < amount_rows; yy++) {
                int pixel = level_map.getRGB(start_x + xx, start_y + yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
             
                // Block
                if(red == 0 && green == 255 && blue == 0)
                    surrunding_area[xx][yy] = 1;
                else if(red == 0 && green == 0 && blue == 0) // Ground
                    surrunding_area[xx][yy] = 1;
                else if(red == 200 && green == 200 && blue == 200) // Rocks
                    surrunding_area[xx][yy] = 1;
                else if(green == 255 && red == 170 && blue == 255) // WaterSurface
                    surrunding_area[xx][yy] = 1; 
                else if(green == 150 && red == 110 && blue == 190) // WaterMid
                    surrunding_area[xx][yy] = 1;
                else if(green == 90 && red == 50 && blue == 120) // WaterDeep
                    surrunding_area[xx][yy] = 1;
                else
                    surrunding_area[xx][yy] = 0;
         
             
            }
 
        }
     

     
    }
 
    
    
    
    // !!!! uzywana gdy gracz jest zwrocony w prawo
    // sprawdza czy przed graczem znajduje sie niebezpieczny teren 
    // startowym kafelkiem powienien byc zawsze kafelek na ktorym aktualnie stoi gracz
    private void check_terrain_safety(int start_x, int start_y, int amount_columns, int amount_rows) {
        
    	// poziom odniesienia - kafelek wzgledem ktorego szukamy terenu na ktorym mozna bezpiecznie stanac - Block, Ground, Rocks
    	// dla kazdego poziomu odniesienia sprawdzamy max ilosc kafelkow = amount_rows - (przyjeto 8) 3 kafelki powyzej poziomu odniesienia oraz 4 ponizej (razem wraz z poziomem odniesienia daje to 8)
    	// pierwszy od gory napotkany punkt bedacy obiektem klasy Block, Ground lub Rocks zostanie nowym poziomem odniesienia
    	// jesli w danej kolumnie nie ma takiego kafelka przechodzimy do natepnej kolumny (zwiekszenie wspolrzednej x)
    	// a poziom odniesienia zostaje bez zmian
    	// (start_x, start_y) to kafelek na ktorym stoi gracz
    	
    	
    	int prev_loop_refLevel = start_y; // zapamietuje poziom odiesienia z pooprzedniej kolumny
    	int current_level_checked;
        //System.out.println("\nPixel start: " + start_x + " " + start_y);
     
       
        for(int xx = 0; xx < amount_columns; xx++) {
        	
            for(int yy = 0; yy < amount_rows; yy++) {
            	current_level_checked = prev_loop_refLevel - 3 + yy;
                int pixel = level_map.getRGB(start_x + xx, current_level_checked); // -3  - bo gracz moze skoczyc na wys 3 kafelkow terenu 
                //System.out.println("\nSprawdzono wspolrzedna: " + (start_x + xx) + "    " + (current_level_checked));
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
             
                                
                
                if(red == 0 && green == 255 && blue == 0) // Block
                    terrain_safety[xx] = 1;
                else if(red == 0 && green == 0 && blue == 0) // Ground
                	terrain_safety[xx] = 1;
                else if(red == 200 && green == 200 && blue == 200) // Rocks
                	terrain_safety[xx] = 1;
                else
                	terrain_safety[xx] = 0;
         
                // jesli w sprawdzanym miejscu nie ma kafelka - sprawdza sie teren ponizej
                if(terrain_safety[xx] == 1) {
                	prev_loop_refLevel = current_level_checked;
                	yy = amount_rows; // wyjscie z petli - jesli znaleziono kafelek terenu - sprawdzamy natepna kolumne - a poziomem odniesienia bedzie znaleziony kafelek z poprzedniej kolumny
                }
                
                
              
                
            }
            
            reference_levels[xx]=prev_loop_refLevel;
            //System.out.println("\nReference_level: " + reference_levels[xx] + "\n");
        }
     
        /*
        System.out.println("\n\nTabela terrain_safety: ");
        for(int xx = 0; xx < terrain_safety.length; xx++) {
        	System.out.print(terrain_safety[xx] + " ");
        	
        }
        */
        
        // wyznaczenie help_point
        
         
        int currentArrayLength = 0; // dlugosc ciagu 0 - obszaru bez terenu na ktorym mozna bezpiecznie stanac
        // ciagiem tym bedzie pierwszy ciag zer o dlugosci wiekszej niz helpFactor napotkany w tabeli terrain_safety
        
        int maxArrayLength = 0; // maksymalny znaleziony ciag 0
        int temp_position = 0; // pozycja maksymalnego znalezionego ciagu 0 w tabeli terrain_safety
        
        
        for(int xx = 0; xx < terrain_safety.length; xx++) {
        	if(terrain_safety[xx] == 0) {
        		currentArrayLength++;
        	}
        	else if(terrain_safety[xx] == 1) { // (xx == terrain_safety.length - 1) - ostatni element w tabeli
        		if(currentArrayLength > maxArrayLength) {
        			maxArrayLength = currentArrayLength;
        			temp_position = xx;
        			
        			if(maxArrayLength >= this.helpFactor) {
        				//System.out.println("\nZakonczono poszukiwania przed koncem tabeli !!");
        				break;
        			}
        		}
        		currentArrayLength = 0;
        	}
        	
        	//System.out.println("\n xx= " + xx + "   currentArrayLength: " + currentArrayLength + "   maxArrayLength: " + maxArrayLength + "     temp_position: " + temp_position);
        }
        // warunek sprawdzony ponownie po zakonczeniu petli - gdy tabela jest zkonczona 0
        if(currentArrayLength > maxArrayLength) {
			maxArrayLength = currentArrayLength;
			temp_position = terrain_safety.length;
			currentArrayLength = 0;
		}
        
        //System.out.println("\nMax ciag 0: " + maxArrayLength + "  na poz. " + temp_position);
        
        
        if(maxArrayLength >= this.helpFactor) {
        	this.help_point_x = handler.getPlayer_x() + (temp_position - maxArrayLength/2)*32;
        	this.help_point_y = reference_levels[temp_position-1] * 32 + 50;
        }
        else {
        	this.help_point_x = -1;
        	this.help_point_y = -1;
        }
        
        //System.out.println("\nhelp_point_x: " + help_point_x + "  help_point_y: " + help_point_y);
    }
    
    
    
    // !!!! uzywana gdy gracz jest zwrocony w lewo
    private void check_terrain_safety2(int start_x, int start_y, int amount_columns, int amount_rows) {
            	    	
    	int prev_loop_refLevel = start_y; // zapamietuje poziom odiesienia z pooprzedniej kolumny
    	int current_level_checked;
        //System.out.println("\nPixel start: " + start_x + " " + start_y);
            
        for(int xx = 0; xx < amount_columns; xx++) {
        	
            for(int yy = 0; yy < amount_rows; yy++) {
            	current_level_checked = prev_loop_refLevel - 3 + yy;
                int pixel = level_map.getRGB(start_x - xx, current_level_checked); // -3  - bo gracz moze skoczyc na wys 3 kafelkow terenu 
                //System.out.println("\nSprawdzono wspolrzedna: " + (start_x - xx) + "    " + (current_level_checked));
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
             
                                
                
                if(red == 0 && green == 255 && blue == 0) // Block
                    terrain_safety[xx] = 1;
                else if(red == 0 && green == 0 && blue == 0) // Ground
                	terrain_safety[xx] = 1;
                else if(red == 200 && green == 200 && blue == 200) // Rocks
                	terrain_safety[xx] = 1;
                else
                	terrain_safety[xx] = 0;
         
                // jesli w sprawdzanym miejscu nie ma kafelka - sprawdza sie teren ponizej
                if(terrain_safety[xx] == 1) {
                	prev_loop_refLevel = current_level_checked;
                	yy = amount_rows; // wyjscie z petli - jesli znaleziono kafelek terenu - sprawdzamy natepna kolumne - a poziomem odniesienia bedzie znaleziony kafelek z poprzedniej kolumny
                }
                
                
              
                
            }
            
            reference_levels[xx]=prev_loop_refLevel;
            //System.out.println("\nReference_level: " + reference_levels[xx] + "\n");
        }
     
        /*
        
        System.out.println("\n\nTabela terrain_safety: ");
        for(int xx = 0; xx < amount_columns; xx++) {
        	System.out.print(terrain_safety[terrain_safety.length - xx - 1] + " ");
        	
        }
        */
        
        // wyznaczenie help_point
        
        
        int currentArrayLength = 0; // dlugosc ciagu 0 - obszaru bez terenu na ktorym mozna bezpiecznie stanac
        // ciagiem tym bedzie pierwszy ciag zer o dlugosci wiekszej niz helpFactor napotkany w tabeli terrain_safety
        
        int maxArrayLength = 0; // maksymalny znaleziony ciag 0
        int temp_position = 0; // pozycja maksymalnego znalezionego ciagu 0 w tabeli terrain_safety
        
        for(int xx = 0; xx < terrain_safety.length; xx++) {
        	if(terrain_safety[xx] == 0) {
        		currentArrayLength++;
        	}
        	else if(terrain_safety[xx] == 1) { // (xx == terrain_safety.length - 1) - ostatni element w tabeli
        		if(currentArrayLength > maxArrayLength) {
        			maxArrayLength = currentArrayLength;
        			temp_position = xx;
        			
        			if(maxArrayLength >= this.helpFactor) {
        				//System.out.println("\nZakonczono poszukiwania przed koncem tabeli !!");
        				break;
        			}
        		}
        		currentArrayLength = 0;
        	}
        	
        	//System.out.println("\n xx= " + xx + "   currentArrayLength: " + currentArrayLength + "   maxArrayLength: " + maxArrayLength + "     temp_position: " + temp_position);
        }
        // warunek sprawdzony ponownie po zakonczeniu petli - gdy tabela jest zkonczona 0
        if(currentArrayLength > maxArrayLength) {
			maxArrayLength = currentArrayLength;
			temp_position = terrain_safety.length;
			currentArrayLength = 0;
		}
        
        //System.out.println("\nMax ciag 0: " + maxArrayLength + "  na poz. " + temp_position);
        
        
        //System.out.println("\nPlayer: wsp X: " + handler.getPlayer_x());
        //System.out.println("\nmaxArrayLength: " + maxArrayLength);
        //System.out.println("\ntemp_position: " + temp_position);
        
        
        if(maxArrayLength >= this.helpFactor) {
        	this.help_point_x = handler.getPlayer_x() - (temp_position - maxArrayLength/2)*32;
        	this.help_point_y = reference_levels[temp_position-1] * 32 + 50;
        }
        else {
        	this.help_point_x = -1;
        	this.help_point_y = -1;
        }
        
        //System.out.println("\nhelp_point_x: " + help_point_x + "  help_point_y: " + help_point_y);
        
        
        
        
        
        
        
    }  
    
       
    public double angle(float dx1, float dy1, float dx2, float dy2) {
     
     
     
        double wector1_length = (Math.sqrt(dx1 * dx1 + dy1 * dy1));
        double wector2_length = (Math.sqrt(dx2 * dx2 + dy2 * dy2));
     
        double cosinus = (dx1 * dx2 + dy1 * dy2) / (wector1_length * wector2_length);
     
        double wynik = Math.acos(cosinus) * 180 / 3.14152; 
     
     
     
     
        return wynik;
    }
 
 
    // sprawdza czy nastapi kolizja ze sciana przy przyjetym wektorze predkosci
    public int wall_collision(float vx, float vy) {
         int wynik = 0;
        for(int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);
            if(tempObject.getId() == ObjectId.Block || tempObject.getId() == ObjectId.Ground) {
             
                if(getBounds2(vx, vy).intersects(tempObject.getBounds())) {
                wynik = 1;
                i = handler.object.size();
                }
            }
        }
     
     
        return wynik;
     
     
    }
 
 
    public int choose_angle() {
        // Funkcja sluzy korekty toru lotu - do wybierania najlepszszego, bezkolizyjnego kierunku ruchu
        //int[] angle = new int[6];
        //angle[0] = 20;
        //angle[1] = 10;
        //angle[2] = 3;
     
        //angle[3] = 0;
        //angle[4] = 1;
        //angle[5] = 1;
     
     
        //Wydruk tabeli
        //System.out.println("\nTabela kierunkow: ");
        for(int i = 0; i < 6; i++) {
            //System.out.println(angle[i] + " ");
        }
     
     
        ///
     
     
     
     
     
        int angle_min = 9999;
        int angle_min_position = 0; // jesli nie znajdzie sie zadny bezkolizyjny kierunek - wybrany zostanie aktualny kat
     
        // wybieram pierwszy niekolizyjny kierunek
        for(int i = 3; i < 6; i++) {
            if(angle[i] == 0) {
             
                angle_min_position = i - 3;
                angle_min = angle[angle_min_position];
                break;
            }
        }
     
        //System.out.println("\nFirst choosed angle: "+ angle_min + " Position: " + angle_min_position);
        for(int i = 0; i < 3; i++) {
            if(angle[i] < angle_min && angle[i+3] == 0) {
                         
             
                angle_min_position = i;
                angle_min = angle[angle_min_position];
             
            }
        }
     
     
        // sprawdzam, czy jest jakis mniejsz ybezkolizyjny kierunek
     
     
     
        //System.out.println("\nFinally choosed angle: "+ angle_min + " Position: " + angle_min_position);
     
        //
         return angle_min_position;
     
    }
 
    // uzywana do pomocy graczowi we wspinaczce
    public Rectangle getBounds() {
     
    	if(this.playerAsist && (this.help_point_x > 0 || game.getPlayer_click_x() > 0) && dist < 100 ) {
    		return new Rectangle((int)(x), (int)(y), (int)32, (int)32);
    	}
    	
        return new Rectangle((int)(-100), (int)(-100), (int)10, (int)10); // poza obszarem gry
    }
    
    
    // uzywana do wykrywania kolizji
    public Rectangle getBounds1() {
     
        return new Rectangle((int)(x+6), (int)(y+6), (int)20, (int)20);
    }
 
 
    // uzywana do korekty ruchu - unikanie scian
    public Rectangle getBounds2(float vx, float vy) {
    
        if(vx > 0 && vy == 0) {
            return new Rectangle((int) x, (int) y, (int)(vx*12+64), (int)32);
        }
        else if(vx < 0 && vy == 0) {
            return new Rectangle((int) (x+vx*12-32), (int) y, (int)(-vx*12+64), (int)32);
        }
        else if(vx == 0 && vy > 0) {
            return new Rectangle((int) x, (int) y, (int) 32, (int)(vy*12+64));
        }
        else if(vx == 0 && vy < 0) {
            return new Rectangle((int) x, (int) (y+vy*12-32), (int) 32, (int)(-vy*12+64));
        }
        else if(vx > 0 && vy > 0) {
            return new Rectangle((int) (x+20+6*vx), (int) (y+20+6*vy), (int) 32, (int)(32));
        }
        else if(vx < 0 && vy > 0) {
            return new Rectangle((int) (x-20+6*vx), (int) (y+20+6*vy), (int) 32, (int)(32));
        }
        else if(vx > 0 && vy < 0) {
            return new Rectangle((int) (x+20+6*vx), (int) (y-20+6*vy), (int) 32, (int)(32));
        }
        else if(vx < 0 && vy < 0) {
            return new Rectangle((int) (x-20+6*vx), (int) (y-20+6*vy), (int) 32, (int)(32));
        }
    
    
        else return new Rectangle((int)(x + 11*vx), (int)(y + 11*vy), (int)24, (int)24);
            
        //return new Rectangle((int)(x + 11*vx), (int)(y + 11*vy), (int)32, (int)32);
    }
 
    
    // uzywana do sprawdzenia czy player wybral Daisy do wspolpracy
    public Rectangle getBounds3() {
     
        return new Rectangle((int)x, (int)y, (int)width, (int)height);
    }
    
    
    private void direction() {
		// pomocniczo do renderowania animacji - sprawdza czy Daisy porusza sie w lewo czy w prawo
		if(this.velX < 0) {
			turned_right = false;
			turned_left = true;
		}
		if(this.velX > 0) {
			turned_right = true;
			turned_left = false;
		}
	
		
	}
    
    private void playerDirection() {
		// pomocniczo do renderowania animacji - sprawdza czy Daisy porusza sie w lewo czy w prawo
		if(handler.getPlayer_velX() < 0) {
			player_turned_right = false;
			player_turned_left = true;
		}
		if(handler.getPlayer_velX() > 0) {
			player_turned_right = true;
			player_turned_left = false;
		}
	
		
	}
 
}