package Scene;

import java.util.ArrayList;

import MainGame.Game;
import MainGame.GameObject;
import MainGame.functions;
import MainGame.o_sprite;
import MainGame.o_stuff;
import MainGame.sys_SpriteLibrary;
import Network.net_Client;
import Network.net_Server;
import Packet.Packet00Login;

public class Scene_InGame extends Scene
{
	private o_stuff background = null;
	private o_stuff blackScreen = null;
	
	private ArrayList<GameObject> PlayerList = new ArrayList<>();
	private ArrayList<GameObject> TerrainObjectList = new ArrayList<>();
	
	private int sessionStartCount = 0;
	private int sessionEndCount = 0;
	private int sessionTimePassed = 0;
	
	private boolean playerCreated = true;
	
	public Scene_InGame(Game game, SceneManager sceneManager) { super(game,sceneManager); }

	@Override
	public void sceneSetUp()
	{
		mapGeneration();
		if ( Game.HostingServerID != -1 )
		{
			Game.gameServer = new net_Server();
			Game.gameServer.start();
		}
		Game.gameClient = null;
		Game.gameClient = new net_Client(Game.JoiningServerIP);
		Game.gameClient.start();

		background = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_background));
		background.setLayer(-10);
		background.setAlpha(1f);
		
		blackScreen = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_BlackScreen));
		blackScreen.setLayer(10);
		blackScreen.setAlpha(1f);
		
		// Player Creation
		Packet00Login playerLogIn = new Packet00Login(Game.username,0,0);
		playerLogIn.writeData(Game.gameClient);
	}
	@Override
	public void sceneStepSetUp()
	{
		
		background.setX(-Game.CAMERA.getX());
		background.setY(-Game.CAMERA.getY());
		if ( playerCreated )
		{
			if ( Game.PLAYER != null )
			{
				Game.CAMERA.setX(-(Game.PLAYER.getX() - Game.WIDTH/2));
				Game.CAMERA.setY(-(Game.PLAYER.getY() + Game.HEIGHT/2));
				playerCreated = false;
			}
		}
		else
		{
			PlayerList = Game.gameClient.getPlayerList();
			Game.playerNum = PlayerList.size();
			Game.playerNum_original = Game.playerNum;
			
			blackScreen.setAlpha((float)functions.clamp(blackScreen.getAlpha()-0.01f,0,1));
			if ( blackScreen.getAlpha() == 0.f )
			{
				sessionStartCount++;
			}
			
			if ( sessionStartCount >= 90 )
			{
				Game.UI.turnUIon();
			}
			if ( sessionStartCount >= 120 )
			{
				setSetUpComplete(true);
			}
		}
		blackScreen.setX(-Game.CAMERA.getX());
		blackScreen.setY(-Game.CAMERA.getY());
	}
	@Override
	public void sceneStep()
	{
		sessionTimePassed++;

		background.setX(-Game.CAMERA.getX());
		background.setY(-Game.CAMERA.getY());
		
		blackScreen.setX(-Game.CAMERA.getX());
		blackScreen.setY(-Game.CAMERA.getY());
		
		Game.playerNum = 0;
		PlayerList = Game.gameClient.getPlayerList();
		for ( int i = 0; i < PlayerList.size(); i++ )
		{
			if ( PlayerList.get(i).getBlood() > 0 ) { Game.playerNum++; }
		}
		
		Game.GameSessionRunning = true;
		
		if ( Game.PLAYER == null || Game.playerNum == 1 )
		//if ( Game.PLAYER == null )
		{
			getSceneManager().sceneChangeRequest("Session Review");
			
			if ( Game.PLAYER != null ) { Game.GameSessionRunning = false; }
		}
	}
	@Override
	public void sceneStepCleanUp()
	{
		background.setX(-Game.CAMERA.getX());
		background.setY(-Game.CAMERA.getY());
		
		blackScreen.setX(-Game.CAMERA.getX());
		blackScreen.setY(-Game.CAMERA.getY());
		
		Game.UI.turnUIoff();
		
		
		sessionEndCount++;
		
		if ( sessionEndCount >= 150 )
		{
			blackScreen.setAlpha((float)functions.clamp(blackScreen.getAlpha()+0.01f,0,1));
		}
		
		if ( blackScreen.getAlpha() == 1.f )
		{
			setCleanUpComplete(true);
		}
	}
	@Override
	public void sceneCleanUp()
	{
		Game.GameSessionRunning = false;
		
		sessionStartCount = 0;
		sessionEndCount = 0;
		sessionTimePassed = 0;

		for ( int i = 0; i < TerrainObjectList.size(); i++ )
		{
			TerrainObjectList.get(i).delete();
		}
		TerrainObjectList = new ArrayList<>();
		Game.MAP = null;
		
		for ( int i = 0; i < PlayerList.size(); i++ )
		{
			if (!PlayerList.get(i).getDeleted()) PlayerList.get(i).delete();
		}
		Game.gameClient.kill();
		
		if ( background != null ) { Game.deleteGameObject(background); background = null; }
		if ( blackScreen != null ) { Game.deleteGameObject(blackScreen); blackScreen = null; }
		
		Game.UI.initialize();
	}
	
	private void mapGeneration() 
	{
		int mapScale = Game.MAP.length;
		
		for( int iy = 0; iy < Game.MAP.length; iy++ )
		{
			for ( int ix = 0; ix < Game.MAP[0].length; ix++)
			{
				int cur = Game.MAP[iy][ix];
				if ( cur >= 0 )
				{
					int create_x = (1-mapScale)*16 + 32*ix;
					int create_y = (1-mapScale)*16 + 32*iy;
					
					GameObject terrain = Game.createGameObject(new o_sprite(create_x,create_y,sys_SpriteLibrary.sprite_map_floor_ice));
					terrain.setLayer(-1);
					TerrainObjectList.add(terrain);
				
					if ( cur == 0 )
					{
						terrain = Game.createGameObject(new o_sprite(create_x,create_y,sys_SpriteLibrary.sprite_map_basic_ice));
						terrain.setDepth(create_y);
						TerrainObjectList.add(terrain);
					}
					else if ( cur > 0 && cur < 16 )
					{
						terrain = Game.createGameObject(new o_sprite(create_x,create_y,sys_SpriteLibrary.sprite_map_wall_ice));
						terrain.setDepth(create_y);
						((o_sprite) terrain).setImageIndex(cur-1);
						TerrainObjectList.add(terrain);
					}
				}
			}
		}
	}
}
