package Scene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;

import MainGame.Game;
import MainGame.functions;
import MainGame.o_button;
import MainGame.o_inputBox;
import MainGame.o_stuff;
import MainGame.sys_SpriteLibrary;
import Network.net_Database;

public class Scene_CreateAccount extends Scene
{
	private int w = Game.WIDTH;
	private int h = Game.HEIGHT;
	
	private o_stuff blackBackground = null;
	private o_stuff blackScreen = null;
	
	private o_inputBox inputBox_username = null;
	private o_inputBox inputBox_password = null;
	private o_inputBox inputBox_passwordConfirm = null;
	
	private o_button button_create = null;
	private o_button button_back = null;

	public Scene_CreateAccount(Game game, SceneManager sceneManager) { super(game, sceneManager); }

	
	@Override
	public void sceneSetUp()
	{
		blackBackground = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_AccountCreationScreen));
		blackScreen = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_BlackScreen));
		
		inputBox_username = (o_inputBox) Game.createGameObject(new o_inputBox(w/2,h/2 - 15,getGame(),sys_SpriteLibrary.sprite_inputBox_inputBox0));
		inputBox_username.setWordNumLimit(15);
		inputBox_username.setBasicMessage("Username");
		inputBox_username.setErrorMessage("Username Exists Already");
		
		inputBox_password = (o_inputBox) Game.createGameObject(new o_inputBox(w/2,h/2 + 15,getGame(),sys_SpriteLibrary.sprite_inputBox_inputBox0));
		inputBox_password.setWordNumLimit(15);
		inputBox_password.setBasicMessage("Password");
		inputBox_password.setErrorMessage("Password Mistyped");
		
		inputBox_passwordConfirm = (o_inputBox) Game.createGameObject(new o_inputBox(w/2,h/2 + 30,getGame(),sys_SpriteLibrary.sprite_inputBox_inputBox0));
		inputBox_passwordConfirm.setWordNumLimit(15);
		inputBox_passwordConfirm.setBasicMessage("Password Confirm");
		inputBox_passwordConfirm.setErrorMessage("Password Mis-typed");
		
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
		if ( blackBackground != null ) { blackBackground.delete(); blackBackground = null; }
		if ( blackScreen != null ) { blackScreen.delete(); blackScreen = null; }
		if ( inputBox_username != null ) { inputBox_username.delete(); inputBox_username = null; }
		if ( inputBox_password != null ) { inputBox_password.delete(); inputBox_password = null; }
		if ( inputBox_passwordConfirm != null ) { inputBox_passwordConfirm.delete(); inputBox_passwordConfirm = null; }
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
			getSceneManager().sceneChangeRequest("Log In");
		}
	}
	
	private void networkingStuff()
	{
		String usernameInput = inputBox_username.getString();
		String passwordInput = inputBox_password.getString();
		String passwordConfirmInput = inputBox_passwordConfirm.getString();
		
		boolean usernameError = false;
		boolean passwordError = false;
		
		// Network
		String result = net_Database.dataGet("searchByUsername/print/"+usernameInput);
		
		System.out.println(result);
		if (!result.equals("no such player")) { usernameError = true; }
		System.out.println(usernameError);
		if (!passwordInput.equals(passwordConfirmInput)) { passwordError = true; }
		
		if ( !usernameError && !passwordError )
		{
			net_Database.dataSet("create/"+usernameInput+"/"+passwordInput);
		}
		
		if ( usernameError ) { inputBox_username.setError(true); }
		if ( passwordError ) { inputBox_password.setError(true);inputBox_passwordConfirm.setError(true); }
		
		if ( !usernameError && !passwordError )
		{
			getSceneManager().sceneChangeRequest("Log In");
		}
	}
}
