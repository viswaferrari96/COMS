package Scene;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

import MainGame.Game;
import MainGame.functions;
import MainGame.o_stuff;
import MainGame.sys_MapGenerator;
import MainGame.sys_Sprite;
import MainGame.sys_SpriteLibrary;
import Network.net_Database;
import Network.net_TCPchatting;

public class Scene_InServer extends Scene
{
	private String ServerName = null;
	private Integer ServerID = null;
	private String HostIP = null;
	private String HostName = null;
	
	private o_stuff blackScreen = null;
	private o_stuff inServerScreen = null;
	private o_stuff inServerScreen_map = null;
	
	private o_stuff TCPconnectionInput = null;
	private net_TCPchatting TCPsocket = null;
	
	private ArrayList<o_stuff> playerList = new ArrayList<>();
	private ArrayList<o_stuff> playerList_toDelete = new ArrayList<>();
	private ArrayList<String> playerNameList = new ArrayList<>();
	
	private o_stuff minimap = null;
	private boolean mapCreated = false;
	
	private int timer = 0;
	

	public Scene_InServer(Game game, SceneManager sceneManager) { super(game,sceneManager); }
	
	@Override
	public void sceneSetUp()
	{
		Game.CAMERA.setX(0);
		Game.CAMERA.setY(0);
		   
		blackScreen = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_BlackScreen));
		blackScreen.setLayer(10);
		blackScreen.setAlpha(1f);

		inServerScreen = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_ServerScreen));
		
		inServerScreen_map = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_ServerScreen_map));
		inServerScreen_map.setDepth(1);
		inServerScreen_map.setAlpha(0.f);
		
		TCPconnectionInput = (o_stuff) Game.createGameObject(new o_stuff(275,80,""));
		TCPconnectionInput.setAlpha(0.0f);
		
		networkingStuff();
		if ( HostName.equals(Game.username))
		{
			mapGenerate();
		}
	}
	@Override
	public void sceneStepSetUp()
	{
		blackScreen.setAlpha((float)functions.clamp(blackScreen.getAlpha()-0.025f,0,1));
		if ( blackScreen.getAlpha() == 0.f )
		{
			setSetUpComplete(true);
		}
	}
	@Override
	public void sceneStep()
	{
		Game.CAMERA.setX(0);
		Game.CAMERA.setY(0);

		if ( ServerID == -1 ) { getSceneManager().sceneChangeRequest("Lobby"); }
		else
		{

			TCPcommunication();
			if (!HostName.equals(Game.username))
			{
				if ( mapCreated == false )
				{
					TCPsocket.send("SYSTEM"+HostName+" MapRequest " + Game.username+" nlc");
					mapCreated = true;
				}
			}
			
			if ( inServerScreen_map.getAlpha() < 1.f )
			{
				inServerScreen_map.setAlpha((float)functions.clamp(inServerScreen_map.getAlpha()+0.025f,0,1));
				//minimap.setAlpha(inServerScreen_map.getAlpha());
			}
			else
			{
				if ( timer == 20 ) { playersCheck(); timer = 0;} timer++;
				playerPadsMoving();
			}
			
			
			if ( Game.MOUSE_CLICK_LEFT_pressed && HostName.equals(Game.username) )
			{
				if ( Game.gameServer != null )
				{
					System.out.println("Server is still open!");
				}
				else
				{
					getSceneManager().sceneChangeRequest("In Game");
					for ( int i = 0; i < playerNameList.size(); i++ )
					{
						String playerName = playerNameList.get(i);
						if ( !playerName.equals(Game.username) ) TCPsocket.send("SYSTEM"+playerName+" Start"+" nlc");
					}
				}
			}
			buttonControl();
		}
	}
	@Override
	public void sceneStepCleanUp()
	{
		blackScreen.setAlpha((float)functions.clamp(blackScreen.getAlpha()+0.05f,0,1));
		if ( blackScreen.getAlpha() == 1.f )
		{
			setCleanUpComplete(true);
		}
	}
	@Override
	public void sceneCleanUp()
	{
		ServerName = null;
		ServerID = null;
		HostIP = null;
		HostName = null;
		
		if ( minimap != null ) { minimap.delete(); minimap = null; }
		mapCreated = false;
		
		if ( blackScreen != null ) { blackScreen.delete(); blackScreen = null; }
		if ( inServerScreen != null ) { inServerScreen.delete(); inServerScreen = null; }
		if ( inServerScreen_map != null ) { inServerScreen_map.delete(); inServerScreen_map = null; }
		if ( TCPconnectionInput != null ) { TCPconnectionInput.delete(); TCPconnectionInput = null; }
		if ( TCPsocket != null ) { TCPsocket.close(); TCPsocket = null; }
		for ( int i = 0; i < playerList.size(); i++ ) { playerList.get(i).delete(); } playerList = new ArrayList<>(); 
		for ( int i = 0; i < playerList_toDelete.size(); i++ ) { playerList_toDelete.get(i).delete(); } playerList_toDelete = new ArrayList<>();
		playerNameList = new ArrayList<>();
	}
	
	
	private void buttonControl()
	{
	}

	private void TCPcommunication()
	{
		if ( TCPconnectionInput.getString() != "" )
		{
			String recievedMessage = TCPconnectionInput.getString().trim();
			TCPconnectionInput.setString("");
			
			
			Scanner ts = new Scanner (recievedMessage);
			String linedOutMessage = "";
			while ( ts.hasNext() )
			{
				String nextWord = ts.next();
				if (nextWord.equals("nlc")) { linedOutMessage += "\n"; }
				else { linedOutMessage += nextWord + " "; }
			}
			ts.close();
			
			Scanner ls = new Scanner (linedOutMessage);
			while ( ls.hasNextLine())
			{
				String nextLine = ls.nextLine();
				Scanner s = new Scanner(nextLine);
				
				String messageSort = s.next();
				if (  messageSort.equals("Start"))
				{
					getSceneManager().sceneChangeRequest("In Game");
				}
				if ( messageSort.equals("MapRequest"))
				{
					String sendTo = s.next().trim();
					for ( int i = 0; i < Game.MAP.length; i++ )
					{
						TCPsocket.send("SYSTEM"+sendTo+" MapSet " + mapToString(i)+" nlc");
					}
					TCPsocket.send("SYSTEM"+sendTo+" MapGenerate"+" nlc");
				}
				if ( messageSort.equals("MapSet"))
				{
					String decide = s.next();
					if ( !decide.equals("empty"))
					{
						int mapSize = Integer.parseInt(decide);
						
						int mapLine = Integer.parseInt(s.next());
						
						if (Game.MAP == null) { Game.MAP = new int[mapSize][mapSize]; }
						for ( int ix = 0; ix < mapSize; ix ++ ) {  Game.MAP[mapLine][ix] = Integer.parseInt(s.next()); }
					}
				}
				if ( messageSort.equals("MapGenerate"))
				{
					mapGenerate();
					System.out.println("map generated");
				}
				s.close();
			}
			ls.close();
		}
	}
	
	
	private void mapGenerate()
	{
		int mapSize = 130;
		sys_MapGenerator mapGenerator;
		
		if ( Game.MAP == null )
		{
			mapGenerator = new sys_MapGenerator(mapSize,0.2);
			Game.MAP = mapGenerator.getMapArray();
		}
		else
		{
			mapGenerator = new sys_MapGenerator(Game.MAP);
			System.out.println("map copied");
		}
		
		minimap = (o_stuff)Game.createGameObject(new o_stuff(47 + (233-mapSize)/2,67 + (233-mapSize)/2,new sys_Sprite(mapGenerator.toImage(),0,0)));
		minimap.setAlpha(1.f);
		
		while (!TCPsocket.isOpen()) {}
		for ( int i = 0; i < playerNameList.size(); i++ )
		{
			String playerName = playerNameList.get(i);
			if ( !playerName.equals(Game.username) )
			{
				for ( int j = 0; j < Game.MAP.length; j++ )
				{
					TCPsocket.send("SYSTEM"+playerName+" MapSet " + mapToString(j)+" nlc");
				}
				TCPsocket.send("SYSTEM"+playerName+" MapGenerate"+" nlc");
				System.out.println("map sent");
			}
		}
	}
	
	private String mapToString( int line )
	{
		String toReturn = "";
		int[][] map = Game.MAP;
		
		if ( map != null )
		{
			toReturn += map.length + " " + line + " ";
			for ( int ix = 0; ix < map.length; ix++ )
			{
				toReturn += map[line][ix] +" ";
			}
		}
		else
		{
			toReturn = "empty";
		}
		
		return toReturn;
	}
	
	private void networkingStuff()
	{
		ServerID = Integer.parseInt(net_Database.dataGet("searchByUsername/getJoinedServerID/"+Game.username));
		
		if ( ServerID != -1 )
		{
			ServerName = net_Database.dataServerGet("searchById/getServername/"+ServerID);
			HostIP = net_Database.dataServerGet("searchByServername/getHostIPaddress/"+ServerName);
			HostName = net_Database.dataServerGet("searchByServername/getHostname/"+ServerName);
			
			Game.JoiningServerIP = HostIP;
			if ( HostName.equals(Game.username)) {Game.HostingServerID = ServerID;}
			
			String address = "ws://"+net_Database.serverIP+":8080/websocket/" + "------"+Game.username;
			try 
			{
				TCPsocket = new net_TCPchatting(new URI(address),TCPconnectionInput);
				TCPsocket.connect();
			}
			catch (URISyntaxException e) {}
		}
	}
	
	private void playersCheck()
	{
		String playerListString = net_Database.dataServerGet("searchByServername/getAllUsers/print/"+ServerName);
		ArrayList<String> newList = new ArrayList<>();
		
		if ( playerListString != null )
		{
			Scanner s = new Scanner(playerListString); 
			while ( s.hasNext()) { newList.add(s.next().trim()); } s.close();
		}
		
		for ( int i = 0; i < playerNameList.size(); i ++ )
		{
			if (!newList.contains(playerNameList.get(i)))
			{
				for ( int j = 0; j < playerList.size(); j ++ )
				{
					if ( playerList.get(j).getString().equals(playerNameList.get(i)))
					{
						playerList_toDelete.add(playerList.get(j));
					}
				}
			}
		}
		
		for ( int i = 0; i < playerList_toDelete.size(); i++ )
		{
			
			playerList.remove(playerList_toDelete.get(i));
		}
		
		for ( int i = 0; i < newList.size(); i++ )
		{
			if ( !playerNameList.contains(newList.get(i)))
			{
				String playerName = newList.get(i);
				
				double create_x = 284;
				double create_y = 66;
				if ( playerList.size() > 0 ) { create_y = playerList.get(playerList.size()-1).getY()+40; }
				
				o_stuff toAddObject = (o_stuff)Game.createGameObject(new o_stuff(create_x,create_y,sys_SpriteLibrary.sprite_playerListPad_normal));
				toAddObject.setString(playerName);
				toAddObject.setAlpha(0.f);
				toAddObject.setPrintAdjust_x(20);
				toAddObject.setPrintAdjust_y(12);
				
				if ( toAddObject.getString().equals(Game.username))
				{
					toAddObject.setSprite(sys_SpriteLibrary.sprite_playerListPad_player);
				}
				if (HostName.equals(playerName))
				{
					toAddObject.setSprite(sys_SpriteLibrary.sprite_playerListPad_admin);
				}
				
				playerList.add(toAddObject);
			}
		}
		
		playerNameList = newList;
		
		if ( newList.size() == 0 )
		{
			getSceneManager().sceneChangeRequest("Lobby");
			Game.JoiningServerIP = null;
		}
	}
	
	private void playerPadsMoving()
	{
		for ( int i = 0; i < playerList.size(); i++ )
		{
			double goal_y;
			if ( i == 0 ) { goal_y = 66; }
			else { goal_y = playerList.get(i-1).getY() + 24; }
			
			o_stuff cur = playerList.get(i);
			
			if ( cur.getY() > goal_y ) { cur.setY(cur.getY()-1); }
			if ( cur.getAlpha() < 1.f && ( i == 0 || playerList.get(i-1).getAlpha() > 0.7f) ) { cur.setAlpha((float)functions.clamp(cur.getAlpha()+0.075f,0,1));}
		}
		
		for ( int i = 0; i < playerList_toDelete.size(); i++ )
		{
			o_stuff cur = playerList_toDelete.get(i);
			
			cur.setX(cur.getX()+2);
			if ( cur.getAlpha() > 0.f ) { cur.setAlpha((float)functions.clamp(cur.getAlpha()-0.05f,0,1));}
			else
			{
				playerList_toDelete.remove(i);
				cur.delete();
			}
		}
	}
}















