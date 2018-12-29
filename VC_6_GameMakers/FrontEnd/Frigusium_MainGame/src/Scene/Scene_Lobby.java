package Scene;

import java.awt.Color;
import java.net.Inet4Address;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import MainGame.Game;
import MainGame.functions;
import MainGame.o_button;
import MainGame.o_inputBox;
import MainGame.o_stuff;
import MainGame.sys_SpriteLibrary;
import Network.net_Database;
import Network.net_TCPchatting;

public class Scene_Lobby extends Scene
{
	private o_stuff blackScreen = null;
	private o_stuff lobbyScreen = null;
	
	private o_stuff chattingContents = null;
	private net_TCPchatting chattingSocket = null;
	
	private o_inputBox chattingInputBox = null;
	
	private o_button b_hostANewServer = null;
	private o_button b_serverRefresh = null;

	private ArrayList<o_button> serverButtonList = null;
	private ArrayList<o_stuff> serverNameList = null;
	private ArrayList<o_stuff> serverPlayerNumList = null;
	private ArrayList<Integer> serverIDList = null;

	public Scene_Lobby(Game game, SceneManager sceneManager) { super(game,sceneManager); }
	
	@Override
	public void sceneSetUp()
	{
		Game.MAP = null;
		Game.CAMERA.setX(0);
		Game.CAMERA.setY(0);
		   
		blackScreen = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_BlackScreen));
		blackScreen.setLayer(10);
		blackScreen.setAlpha(1f);

		lobbyScreen = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_LobbyScreen));
		
		chattingInputBox = (o_inputBox) Game.createGameObject( new o_inputBox(432,328,getGame(),sys_SpriteLibrary.sprite_inputBox_inputBox1));
		chattingInputBox.setWordNumLimit(100);
		chattingInputBox.setBasicMessage("Enter Message");
		chattingInputBox.setErrorMessage("Don't do this shit man");
		
		chattingContents = (o_stuff) Game.createGameObject(new o_stuff(275,80,""));
		chattingContents.setStringColor(new Color(255,255,255));
		chattingContents.setAlpha(1.0f);
		chattingContents.setIsMessenger(true);
		
		b_hostANewServer = (o_button) Game.createGameObject(new o_button(146,328,sys_SpriteLibrary.sprite_button_HostANewServer));
		b_serverRefresh = (o_button) Game.createGameObject(new o_button(146,299,sys_SpriteLibrary.sprite_button_refresh));
		
		networkingStuff();
		serverRefresh();
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
		
		chattingContents.setX(275);
		chattingContents.setY(80);
		
		buttonControl();
		chattingControl();
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
		if ( blackScreen != null ) { blackScreen.delete(); blackScreen = null; }
		if ( lobbyScreen != null ) { lobbyScreen.delete(); lobbyScreen = null; }
		if ( chattingContents != null ) { chattingContents.delete(); chattingContents = null; }
		if ( chattingSocket != null ) { chattingSocket.close(); chattingSocket = null; }
		if ( chattingInputBox != null ) { chattingInputBox.delete(); chattingInputBox = null; }
		if ( b_hostANewServer != null ) { b_hostANewServer.delete(); b_hostANewServer = null; }
		if ( b_serverRefresh != null ) { b_serverRefresh.delete(); b_serverRefresh = null; }

		if ( serverButtonList != null ) { for ( int i = 0; i < serverButtonList.size(); i++ ) { serverButtonList.get(i).delete(); }}
		if ( serverNameList != null ) { for ( int i = 0; i < serverNameList.size(); i++ ) { serverNameList.get(i).delete(); }}
		if ( serverPlayerNumList != null ) { for ( int i = 0; i < serverPlayerNumList.size(); i++ ) { serverPlayerNumList.get(i).delete(); }}
		
		serverButtonList = null;
		serverNameList = null;
		serverPlayerNumList = null;
		serverIDList = null;
	}
	
	private void buttonControl()
	{
		if ( b_hostANewServer.getReleased())
		{
			getSceneManager().sceneChangeRequest("Host a New Server");
		}
		if ( b_serverRefresh.getReleased())
		{
			serverRefresh();
		}
		
		if ( serverButtonList != null )
		{
			for ( int i = 0; i < serverButtonList.size(); i++ )
			{
				if ( serverButtonList.get(i).getReleased())
				{
					String serverID = net_Database.dataServerGet("searchByServername/getId/"+serverNameList.get(i).getString());
					net_Database.dataSet("searchByUsername/setJoinedServerID/"+Game.username+"/"+serverID);
					getSceneManager().sceneChangeRequest("In Server");
				}
			}
		}
	}
	
	private void chattingControl()
	{
		if ( chattingInputBox.entered() )
		{
			chattingInputBox.setEntered(false);
			chattingSocket.send(chattingInputBox.getString()+"\n");
			chattingInputBox.setString("");
		}
	}
	
	private void networkingStuff()
	{
		Color userColor = Color.getHSBColor((float)Math.random(), .63f, .57f);
		String UserEncodedColor = String.format("%02X%02X%02X", userColor.getRed(),userColor.getGreen(),userColor.getBlue()); 
		String address = "ws://"+net_Database.serverIP+":8080/websocket/" + UserEncodedColor + Game.username;
		try 
		{
			chattingSocket = new net_TCPchatting(new URI(address),chattingContents);
			chattingSocket.connect();
		}
		catch (URISyntaxException e) {}
	}
	
	private void serverRefresh()
	{
		if ( serverButtonList != null ) { for ( int i = 0; i < serverButtonList.size(); i++ ) { serverButtonList.get(i).delete(); }}
		if ( serverNameList != null ) { for ( int i = 0; i < serverNameList.size(); i++ ) { serverNameList.get(i).delete(); }}
		if ( serverPlayerNumList != null ) { for ( int i = 0; i < serverPlayerNumList.size(); i++ ) { serverPlayerNumList.get(i).delete(); }}
		serverButtonList = new ArrayList<>();
		serverNameList = new ArrayList<>();
		serverPlayerNumList = new ArrayList<>();
		serverIDList = new ArrayList<>();
		
		// Network
		String serverListString = net_Database.dataServerGet("allServersListAndPrint");
		
		if ( serverListString != null )
		{
			Scanner serverReader = new Scanner(serverListString);
			
			int index = 0;
			while ( serverReader.hasNext() )
			{
				o_button serverButton = (o_button) Game.createGameObject(new o_button(146,79+15*index,sys_SpriteLibrary.sprite_button_serverList));
				o_stuff serverName = (o_stuff) Game.createGameObject(new o_stuff(45,82+15*index,serverReader.next()));
				int serverID = serverReader.nextInt();
				o_stuff serverPlayerNum = (o_stuff) Game.createGameObject(new o_stuff(227,82+15*index,serverReader.next()+"/10"));
				serverName.setStringColor(Color.white);
				serverPlayerNum.setStringColor(Color.white);
				
				serverButtonList.add(serverButton);
				serverNameList.add(serverName);
				serverPlayerNumList.add(serverPlayerNum);
				serverIDList.add(serverID);
				index++;
			}
			serverReader.close();
		}
	}
}
