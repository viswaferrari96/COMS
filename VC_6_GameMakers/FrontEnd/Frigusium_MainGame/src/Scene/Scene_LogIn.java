package Scene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import MainGame.Game;
import MainGame.functions;
import MainGame.o_button;
import MainGame.o_inputBox;
import MainGame.o_stuff;
import MainGame.sys_SpriteLibrary;
import Network.net_Database;

public class Scene_LogIn extends Scene
{
	private int w = Game.WIDTH;
	private int h = Game.HEIGHT;
	
	private o_stuff blackBackground = null;
	private o_stuff blackScreen = null;
	
	private o_inputBox inputBox_username = null;
	private o_inputBox inputBox_password = null;
	
	private o_button b_logIn = null;
	private o_button b_newUser= null;

	public Scene_LogIn(Game game, SceneManager sceneManager) { super(game, sceneManager); }

	
	@Override
	public void sceneSetUp()
	{
		blackBackground = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_LogInScreen));
		blackScreen = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_BlackScreen));
		
		inputBox_username = (o_inputBox) Game.createGameObject(new o_inputBox(w/2,h/2 - 15,getGame(),sys_SpriteLibrary.sprite_inputBox_inputBox0));
		inputBox_username.setWordNumLimit(15);
		inputBox_username.setBasicMessage("Username");
		inputBox_username.setErrorMessage("Unregistered Username");
		
		inputBox_password = (o_inputBox) Game.createGameObject(new o_inputBox(w/2,h/2 + 15,getGame(),sys_SpriteLibrary.sprite_inputBox_inputBox0));
		inputBox_password.setWordNumLimit(15);
		inputBox_password.setBasicMessage("Password");
		inputBox_password.setErrorMessage("Incorrect Password");
		
		b_logIn = (o_button) Game.createGameObject(new o_button(w/2,h/2 + 40,sys_SpriteLibrary.sprite_button_logIn));
		b_newUser = (o_button) Game.createGameObject(new o_button(w/2,h/2 + 70,sys_SpriteLibrary.sprite_button_NewUser));
		
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
		if ( blackBackground != null ) { blackBackground.delete(); blackBackground = null; }
		if ( blackScreen != null ) { blackScreen.delete(); blackScreen = null; }
		if ( inputBox_username != null ) { inputBox_username.delete(); inputBox_username = null; }
		if ( inputBox_password != null ) { inputBox_password.delete(); inputBox_password = null; }
		if ( b_logIn != null ) { b_logIn.delete(); b_logIn = null; }
		if ( b_newUser != null ) { b_newUser.delete(); b_newUser = null; }
		
	}

	private void buttonControl()
	{
		if ( b_logIn.getReleased())
		{
			networkingStuff();
		}
		if ( b_newUser.getReleased())
		{
			getSceneManager().sceneChangeRequest("Create Account");
		}
	}
	
	private void networkingStuff()
	{
		String usernameInput = inputBox_username.getString();
		String passwordInput = inputBox_password.getString();
		
		boolean usernameError = false;
		boolean passwordError = false;
		
		// Network
		String password = net_Database.dataGet("searchByUsername/getPassword/"+usernameInput);

		if ( password == null ) { usernameError = true;}
		else if (!passwordInput.equals(password)) { passwordError = true; }
		
		if ( usernameError ) { inputBox_username.setError(true); }
		if ( passwordError ) { inputBox_password.setError(true); }
		
		if ( !usernameError && !passwordError )
		{
			Game.username = usernameInput;
			getSceneManager().sceneChangeRequest("Start Screen");
			
			String ip = "null";
			try { ip = Inet4Address.getLocalHost().getHostAddress(); }
			catch (UnknownHostException e) {e.printStackTrace();}
			
			while(!net_Database.dataGet("searchByUsername/getIPaddress/" + Game.username).equals(ip))
			{
				net_Database.dataSet("searchByUsername/setIPaddress/"+ Game.username + "/" + ip);
			}
		}
	}
}
