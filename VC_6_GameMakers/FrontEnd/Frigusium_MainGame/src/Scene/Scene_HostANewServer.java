package Scene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import MainGame.Game;
import MainGame.functions;
import MainGame.o_button;
import MainGame.o_inputBox;
import MainGame.o_stuff;
import MainGame.sys_SpriteLibrary;
import Network.net_Database;

public class Scene_HostANewServer extends Scene
{
	private int w = Game.WIDTH;
	private int h = Game.HEIGHT;
	
	private o_stuff HostANewServerScreen = null;
	private o_stuff blackScreen = null;
	
	private o_inputBox inputBox_serverName = null;
	
	private o_button button_create = null;
	private o_button button_back = null;

	public Scene_HostANewServer(Game game, SceneManager sceneManager) { super(game, sceneManager); }

	
	@Override
	public void sceneSetUp()
	{
		HostANewServerScreen = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_HostingServerScreen));
		blackScreen = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_BlackScreen));
		
		inputBox_serverName = (o_inputBox) Game.createGameObject(new o_inputBox(w/2,h/2 - 15,getGame(),sys_SpriteLibrary.sprite_inputBox_inputBox0));
		inputBox_serverName.setWordNumLimit(15);
		inputBox_serverName.setBasicMessage("Server Name");
		inputBox_serverName.setErrorMessage("Server Name Exists Already");
		
		button_create = (o_button) Game.createGameObject(new o_button(w/2,h/2 + 55 ,sys_SpriteLibrary.sprite_button_Create));
		button_back = (o_button) Game.createGameObject(new o_button(w/2,h/2 + 85 ,sys_SpriteLibrary.sprite_button_Back));
		
		blackScreen.setLayer(1);
		blackScreen.setAlpha(1f);
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
		buttonControl();
	}
	@Override
	public void sceneStepCleanUp()
	{
		blackScreen.setAlpha((float)functions.clamp(blackScreen.getAlpha()+0.025f,0,1));
		if ( blackScreen.getAlpha() == 1.f )
		{
			setCleanUpComplete(true);
		}
	}
	@Override
	public void sceneCleanUp()
	{
		if ( HostANewServerScreen != null ) { HostANewServerScreen.delete(); HostANewServerScreen = null; }
		if ( blackScreen != null ) { blackScreen.delete(); blackScreen = null; }
		if ( inputBox_serverName != null ) { inputBox_serverName.delete(); inputBox_serverName = null; }
		if ( button_create != null ) { button_create.delete(); button_create = null;}
		if ( button_back != null ) { button_back.delete(); button_create = null;}
	}

	private void buttonControl()
	{
		if ( button_create.getReleased())
		{
			networkingStuff();
		}
		if ( button_back.getReleased())
		{
			getSceneManager().sceneChangeRequest("Lobby");
		}
	}
	
	private void networkingStuff()
	{
		String servernameInput = inputBox_serverName.getString();
		
		boolean usernameError = false;
		
		// Network
		String result = net_Database.dataServerGet("searchByServername/print/"+servernameInput);
		System.out.println(result); 
		if (!result.equals("no such userServer")) { usernameError = true; }
		 
		if ( !usernameError )
		{
			String ip = "null";
			try { ip = Inet4Address.getLocalHost().getHostAddress(); }
			catch (UnknownHostException e) {e.printStackTrace();}
			
			net_Database.dataServerSet("create/"+servernameInput+"/"+ ip + "/" + Game.username);
		}
		
		if ( usernameError ) { inputBox_serverName.setError(true); }
		else
		{
			String serverID = net_Database.dataServerGet("searchByServername/getId/"+servernameInput);
			net_Database.dataSet("searchByUsername/setJoinedServerID/"+Game.username+"/"+serverID);
			getSceneManager().sceneChangeRequest("In Server");
		}
	}
}
