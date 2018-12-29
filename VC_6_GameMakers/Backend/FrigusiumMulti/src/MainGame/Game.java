package MainGame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;

import Network.net_Client;
import Packet.Packet00Login;

public class Game extends Canvas implements Runnable
{
	private static final long serialVersionUID = 558950231427595327L;
	
	public static String username;
	
	public static GameObjectManager OBJECT_MANAGER;
	
	public static sys_SpriteLibrary SPRITE_LIBRARY;
	public static sys_Window WINDOW;
	public static sys_Camera CAMERA;
	public static GameObject PLAYER = null;
	public static sys_UI UI;
	
	public static int MOUSE_X;
	public static int MOUSE_Y;
	
	public static boolean MOUSE_CLICK_LEFT = false;
	public static boolean MOUSE_CLICK_LEFT_pre = false;
	public static boolean MOUSE_CLICK_LEFT_pressed = false;
	public static boolean MOUSE_CLICK_LEFT_released = false;
	
	public static boolean MOUSE_CLICK_RIGHT = false;
	public static boolean MOUSE_CLICK_RIGHT_pre = false;
	public static boolean MOUSE_CLICK_RIGHT_pressed = false;
	public static boolean MOUSE_CLICK_RIGHT_released = false;
	
	public static boolean[] KEY = new boolean[10];
	public static boolean[] KEY_pre = new boolean[KEY.length];
	public static boolean[] KEY_pressed = new boolean[KEY.length];
	public static boolean[] KEY_released = new boolean[KEY.length];

	private Thread thread;
	
	private boolean running = false;
	
	private static String TITLE = "Don't be gay";
	public static int WIDTH = 640;
	public static int HEIGHT = (WIDTH/16)*9;
	public static double SCREENSCALE = 3;
	private double FPS = 60;
	
	private static int gameTime = 0;
	
	private int framesCount = 0;
	private int objectsCount = 0;
	
	private static int playerNum_original;
	private static int playerNum;
	
	private static net_Client gameClient;
	
	private BufferedImage img = (new sys_MapGenerator()).toImage();
	// Game Constructor
	public Game ()
	{
		// Initiate the Game set up
		Initiate();
		
		// Start the game
		start();
	}
	
	
	
	// Initialize and set up the game
	private void Initiate()
	{
		running = true;
		
		username = JOptionPane.showInputDialog("Enter your username");
		gameClient = new net_Client(this,"localhost");
		gameClient.start();
		
		// Keyboard Input initialization
		for ( int i = 0; i < KEY.length; i++ ){ KEY[i] = false; }
		for ( int i = 0; i < KEY.length; i++ ){ KEY_pre[i] = false; }
		
		// Add Key Listener
		this.addKeyListener( new sys_KeyInput() );
		this.addMouseListener( new sys_MouseInput() );
		
		// Sprite Library Creation
		try { SPRITE_LIBRARY = new sys_SpriteLibrary(); }
		catch (IOException e) {}
		
		// Window Creation
		WINDOW = new sys_Window(WIDTH,HEIGHT,TITLE,this);
		
		// Game Object Manager Creation
		OBJECT_MANAGER = new GameObjectManager();
		
		// Player Creation
//		try { PLAYER = createGameObject(new o_player(0,0,"Go fuck yourself",InetAddress.getLocalHost(),2222)); }
//		catch (UnknownHostException e) { e.printStackTrace(); }
		Packet00Login playerLogIn = new Packet00Login(username);
		playerLogIn.writeData(gameClient);
		
		// Camera Creation
		CAMERA = new sys_Camera(0,0,WIDTH,HEIGHT); 
		
		// UI Creation
		UI = new sys_UI();
	}
	
	// Start the Game
	public synchronized void start()
	{
		// Thread Creation
		thread = new Thread(this);
		thread.start();
	}
	
	

	// Main Game Loop
	public void run()
	{
		this.requestFocus(); 
		long time_pre = System.nanoTime();
		double durationForAFrame = 1000000000 / FPS;
		double timeCount = 0.0;
		long timer = System.currentTimeMillis();
		int framesNum = 0;
		
		while (running)
		{
			//long time_cur = System.nanoTime();
			timeCount += (System.nanoTime() - time_pre) / durationForAFrame;
			time_pre = System.nanoTime();
			
			while(timeCount >= 1)	
			{
				//if ( timeCount >= 1 ) { System.out.println(timeCount*durationForAFrame*FPS);}
				timeCount --;
				
				// Run through the whole Object
				step();
				
				// Wait for the Drawing
				try { Thread.sleep(1); }
				catch (InterruptedException e) { e.printStackTrace(); }
				
				// Draw the Game Graphics
				if ( timeCount < 1 ) { draw(); framesNum++;}
			}
			
			// Game Info gets Updated Every 1 second
			if ( System.currentTimeMillis() - timer > 1000)
			{
				timer = System.currentTimeMillis();
				framesCount = framesNum;
				framesNum = 0;
				objectsCount = OBJECT_MANAGER.getObjectNum();
			}
		}
		
		//Stop the Game when the Loop is done
		stop();
	}
	
	// Stop the Game
	public synchronized void stop()
	{
		try
		{
			// close the thread
			thread.join();
			running = false;
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	// Runs Every Frame
	public void step ()
	{
		// Input Setting
		InputSetting();
		
		// Goes Through the whole Game Objects
		OBJECT_MANAGER.step();
		
		// Camera Setting
		if ( PLAYER != null ) CAMERA.setTarget(PLAYER);
		CAMERA.step();
		
		// UI setting
		UI.step();
		
		
		// Game Time Update
		gameTime++;
	}
	
	// Draws the Graphic Components
	public void draw ()
	{	
		BufferStrategy bs = this.getBufferStrategy();
		if ( bs == null )
		{
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		//---------Draw Here----------//

		BufferedImage temporaryCanvas = new BufferedImage(WIDTH,HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics cnvs = temporaryCanvas.getGraphics();
		Graphics cnvs2d = (Graphics2D)cnvs;

		Color color = new Color(231,231,255);
		color = Color.black;
		
		cnvs.setFont(new Font("forgame",Font.PLAIN,7));
		cnvs.setColor(color);
		cnvs.fillRect(0,0, WIDTH, HEIGHT);
		
		cnvs2d.translate((int)CAMERA.getX(), (int)CAMERA.getY());
			OBJECT_MANAGER.draw(cnvs);
		cnvs2d.translate((int)-CAMERA.getX(),(int)-CAMERA.getY());

		cnvs.setColor(Color.white);
		cnvs.drawString( getPlayerNum() + " / " + getPlayerNumOriginal(), WIDTH-60, 18);
		
		cnvs.setColor(Color.white);
		double angle = 0;
		if ( PLAYER!= null )
		{
			GameObject temp  = functions.closestKind(PLAYER.getName(), PLAYER);
			if ( temp != null )
			{
				angle = functions.getDirection(PLAYER.getX(), PLAYER.getY(), temp.getX(), temp.getY());
			}
		}
		cnvs.drawString("FPS: " + framesCount + "     ClosestAngle: " + (int)angle, WIDTH-160, HEIGHT-16);
		
		double s = 0.35;
		
		//cnvs.drawImage(img, 0, 0, (int)(img.getWidth()*s), (int)(img.getHeight()*s), null);
		UI.draw(cnvs);
		g.drawImage(temporaryCanvas,0,0,(int)(WIDTH*SCREENSCALE),(int)(HEIGHT*SCREENSCALE),null);
		
		//---------Draw Here----------//
		
		g.dispose();
		bs.show();
	}
	
	// Update the Keyboard Input Information
	public void InputSetting()
	{
		// Mouse Coordinate Calculation
		MOUSE_X = (int) ((MouseInfo.getPointerInfo().getLocation().getX() - WINDOW.getWindowX())/SCREENSCALE - (int)CAMERA.getX());
		MOUSE_Y = (int) ((MouseInfo.getPointerInfo().getLocation().getY() - WINDOW.getWindowY())/SCREENSCALE - (int)CAMERA.getY());
		
		// Mouse Click Check
		if ( !MOUSE_CLICK_LEFT_pre && MOUSE_CLICK_LEFT  ) { MOUSE_CLICK_LEFT_pressed = true; } else { MOUSE_CLICK_LEFT_pressed = false; }
		if ( MOUSE_CLICK_LEFT_pre && !MOUSE_CLICK_LEFT  ) { MOUSE_CLICK_LEFT_released = true; } else { MOUSE_CLICK_LEFT_released = false; }
		MOUSE_CLICK_LEFT_pre = MOUSE_CLICK_LEFT;
		
		if ( !MOUSE_CLICK_RIGHT_pre && MOUSE_CLICK_RIGHT ) { MOUSE_CLICK_RIGHT_pressed = true; } else { MOUSE_CLICK_RIGHT_pressed = false; }
		if ( MOUSE_CLICK_RIGHT_pre && !MOUSE_CLICK_RIGHT ) { MOUSE_CLICK_RIGHT_released = true; } else { MOUSE_CLICK_RIGHT_released = false; }
		MOUSE_CLICK_RIGHT_pre = MOUSE_CLICK_RIGHT;
		
		// Keyboard Input
		for ( int i = 0; i < KEY.length; i ++ )
		{
			if ( !KEY_pre[i] && KEY[i] ) { KEY_pressed[i] = true; } else { KEY_pressed[i] = false; }
			if ( KEY_pre[i] && !KEY[i] ) { KEY_released[i] = true; } else { KEY_released[i] = false; }
			KEY_pre[i] = KEY[i];
		}
	}

	public static int keyIndex( String keyInput )
	{
		int n = 0;
		if ( keyInput == "w" ){ return n; } n++;
		if ( keyInput == "a" ){ return n; } n++;
		if ( keyInput == "s" ){ return n; } n++;
		if ( keyInput == "d" ){ return n; } n++;
		if ( keyInput == "space" ) { return n; } n++;
		if ( keyInput == "right" ) { return n; } n++;
		if ( keyInput == "left" ) { return n; } n++;
		if ( keyInput == "down" ) { return n; } n++;
		if ( keyInput == "up" ) { return n; } n++;
		if ( keyInput == "esc" ) { return n; } n++;
		return -1;
	}
	public static void setKey(String keyInput, boolean set)
	{
		int index = keyIndex(keyInput);
		if ( index != -1 ) { KEY[index] = set; }
	}
	
	public static boolean getKey(String keyInput)
	{
		int index = keyIndex(keyInput);
		if ( index != -1 ) { return KEY[index]; }
		return false;
	}
	
	public static boolean getKeyPressed(String keyInput)
	{
		int index = keyIndex(keyInput);
		if ( index != -1 ) { return KEY_pressed[index]; }
		return false;
	}

	public static boolean getKeyReleased(String keyInput)
	{
		int index = keyIndex(keyInput);
		if ( index != -1 ) { return KEY_released[index]; }
		return false;
	}
	
	
	public static int getGameTime() { return gameTime; }
	
	public static GameObject createGameObject( GameObject ob )
	{
		if ( ob == null ) { System.out.println("nullyo");}
		OBJECT_MANAGER.addGameObject(ob); 
		return ob;
	}
	
	public static net_Client getClient() { return gameClient; }
	
	public static GameObject deleteGameObject( GameObject ob ) { OBJECT_MANAGER.removeGameObject(ob); return ob;}
	
	public static col_Hitbox createHitbox( col_Hitbox ob ) { OBJECT_MANAGER.addHitbox(ob); return ob;}
	public static col_Hitbox deleteHitbox( col_Hitbox ob ) { OBJECT_MANAGER.removeHitbox(ob); return ob;}

	public static void setPlayerNum( int player ) { playerNum = player; }
	public static int getPlayerNum() { return playerNum; }
	public static int getPlayerNumOriginal() { return playerNum_original; }
	
	public static void main(String[] args)
	{
		new Game();
	}
}