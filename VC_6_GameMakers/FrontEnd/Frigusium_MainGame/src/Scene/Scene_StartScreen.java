package Scene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import MainGame.Game;
import MainGame.GameObject;
import MainGame.functions;
import MainGame.o_button;
import MainGame.o_particle_ember;
import MainGame.o_stuff;
import MainGame.sys_SpriteLibrary;
import Network.net_Database;

public class Scene_StartScreen extends Scene
{
	private o_stuff blackScreen = null;
	private o_stuff titleScreen = null;
	private GameObject button_start = null;
	private GameObject button_report = null;
	private GameObject button_quit = null;
	
	private int timer_particleGenerate_ember = 0;

	public Scene_StartScreen(Game game, SceneManager sceneManager) { super(game,sceneManager); }

	
	@Override
	public void sceneSetUp()
	{
		Game.CAMERA.setX(0);
		Game.CAMERA.setY(0);
		
		blackScreen = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_BlackScreen));
		titleScreen = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_TitleScreen));
		button_start = Game.createGameObject(new o_button(Game.WIDTH/2,Game.HEIGHT*(3./4),sys_SpriteLibrary.sprite_button_start));
		button_report = Game.createGameObject(new o_button(Game.WIDTH/2,Game.HEIGHT*(3./4) + 30,sys_SpriteLibrary.sprite_button_Report));
		button_quit = Game.createGameObject(new o_button(Game.WIDTH/2,Game.HEIGHT*(3./4) + 60,sys_SpriteLibrary.sprite_button_Quit));
		
		blackScreen.setLayer(10);
		blackScreen.setAlpha(1f);
		
		networkingStuff();
	}
	@Override
	public void sceneStepSetUp()
	{
		particleGenerate_ember();
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
		
		buttonControl();
		particleGenerate_ember();
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
		if ( blackScreen != null ) { Game.deleteGameObject(blackScreen); blackScreen = null; }
		if ( titleScreen != null ) { Game.deleteGameObject(titleScreen); titleScreen = null; }
		if ( button_start != null ) { Game.deleteGameObject(button_start); button_start = null; }
		if ( button_report != null ) { button_report.delete(); button_report = null; }
		if ( button_quit != null ) { button_quit.delete(); button_report = null; }
	}
	
	
	private void buttonControl()
	{
		o_button b_start = (o_button)button_start;
		o_button b_report = (o_button)button_report;
		o_button b_quit = (o_button)button_quit;
		
		if ( b_start.getReleased())
		{
			getSceneManager().sceneChangeRequest("Lobby");
		}
		
		if ( b_report.getReleased())
		{
			getSceneManager().sceneChangeRequest("Report");
		}
		
		if ( b_quit.getReleased())
		{
			Game.WINDOW.close();
		}
	}
	
	
	private void particleGenerate_ember()
	{
		int particleNum = 1;
		
		if ( timer_particleGenerate_ember <= 0 )
		{
			Random r = new Random();
			int create_x = (int) ( (Math.pow(Math.random(),2)*Game.WIDTH)*(r.nextInt(2)-0.5) + Game.WIDTH/2);
			int create_y = (int) (Game.HEIGHT + Math.random()*0);
			for ( int i = 0; i < particleNum; i++ )
			{
				o_particle_ember ember = (o_particle_ember)Game.createGameObject(new o_particle_ember(create_x,create_y,90+Math.random()*30-15));
				ember.setInitialSpd_x(0);
				ember.setInitialSpd_y(-Math.random()*3);
			}
			
			timer_particleGenerate_ember = (int)(Math.random()*5+1);
		}
		else
		{
			timer_particleGenerate_ember--;
		}
	}
	
	private void networkingStuff()
	{
		// Network
		String URLaddressBase = "searchByUsername";
		
		String URLaddressUpdate_win = URLaddressBase + "/getWin/" + Game.username;
		String URLaddressUpdate_lost = URLaddressBase + "/getLost/" + Game.username;
		String URLaddressUpdate_kill = URLaddressBase + "/getKill/" + Game.username;
		String URLaddressUpdate_death = URLaddressBase + "/getDeath/" + Game.username;
		String URLaddressUpdate_rank = URLaddressBase + "/getRank/" + Game.username;
		
		Game.PLAYER_win = Integer.parseInt(net_Database.dataGet(URLaddressUpdate_win));
		Game.PLAYER_lost = Integer.parseInt(net_Database.dataGet(URLaddressUpdate_lost));
		Game.PLAYER_kill = Integer.parseInt(net_Database.dataGet(URLaddressUpdate_kill));
		Game.PLAYER_death = Integer.parseInt(net_Database.dataGet(URLaddressUpdate_death));
		Game.PLAYER_rank = Integer.parseInt(net_Database.dataGet(URLaddressUpdate_rank));
	}
}