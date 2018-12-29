package Scene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

import MainGame.Game;
import MainGame.GameObject;
import MainGame.functions;
import MainGame.o_button;
import MainGame.o_particle_ember;
import MainGame.o_stuff;
import MainGame.sys_SpriteLibrary;
import Network.net_Database;

public class Scene_SessionReview extends Scene
{
	private int timer = 0;
	
	private int cx = Game.WIDTH/2;
	private int cy = Game.HEIGHT/2;
	private int w = Game.WIDTH;
	private int h = Game.HEIGHT;
	
	private o_stuff blackBackground = null;
	private o_stuff winOrLost = null;
	private o_stuff format = null;
	private o_stuff texts = null;
	private o_stuff killedBy = null;
	private o_stuff blackScreen = null;
	
	private double winOrLost_x = 0;
	private double winOrLost_y = 0;
	
	private double format_x = 0;
	private double format_y = 0;
	
	private double texts_x = 0;
	private double texts_y = 0;
	
	private ArrayList<o_stuff> rankList = null;
	private ArrayList<o_stuff> killCountList = null;
	
	private GameObject button_ok = null;
	
	private int timer_particleGenerate_ember = 0;

	public Scene_SessionReview(Game game, SceneManager sceneManager) { super(game,sceneManager); }

	
	@Override
	public void sceneSetUp()
	{
		timer = 0;
		
		Game.CAMERA.setX(0);
		Game.CAMERA.setY(0);
		
		rankList = new ArrayList<>();
		killCountList = new ArrayList<>();
		
		blackBackground = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_BlackScreen));
		winOrLost = (o_stuff) Game.createGameObject(new o_stuff(cx,cy,sys_SpriteLibrary.sprite_screen_SessionReviewScreen_YouHaveDied));
		format = (o_stuff) Game.createGameObject(new o_stuff(cx,cy+h/10,sys_SpriteLibrary.sprite_screen_SessionReviewScreen_format));
		texts = (o_stuff) Game.createGameObject(new o_stuff(cx,cy+h/10,sys_SpriteLibrary.sprite_screen_SessionReviewScreen_texts));

		blackScreen = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_BlackScreen));
		
		if ( Game.STAT_RANK == 0 ) { winOrLost.setSprite(sys_SpriteLibrary.sprite_screen_SessionReviewScreen_YouHaveSurvived);}
		
		networkingStuff();
	}
	@Override
	public void sceneStepSetUp()
	{
		Game.CAMERA.setX(0);
		Game.CAMERA.setY(0);
		
		blackBackground.setAlpha(1f);
		winOrLost.setAlpha(0f);
		format.setAlpha(0f);
		texts.setAlpha(0f);
		blackScreen.setAlpha(0f);
		blackScreen.setLayer(10);
		
		winOrLost_x = winOrLost.getX();
		winOrLost_y = winOrLost.getY();
		
		format_x = format.getX();
		format_y = format.getY();
		
		texts_x = texts.getX();
		texts_y = texts.getY();
		
		
		setSetUpComplete(true);
	}
	@Override
	public void sceneStep()
	{
		timer++;
		
		if ( timer > 60 )
		{
			winOrLost.setAlpha(1f);
			double rx = cx + (Game.MOUSE_X-cx)/120;
			double ry = cy + (Game.MOUSE_Y-cy)/120;
			winOrLost.setX( winOrLost.getX()+(rx-winOrLost.getX())/10 );	
			winOrLost.setY( winOrLost.getY()+(ry-winOrLost.getY())/10 );
		}
		
		if ( timer > 120 )
		{
			format.setAlpha((float)functions.clamp(format.getAlpha()+0.1f,0,1));
			format_x += (cx-format_x)/5;
			format_y += (cy-format_y)/5;
			double rx = format_x + (Game.MOUSE_X-format_x)/50;
			double ry = format_y + (Game.MOUSE_Y-format_y)/50;
			format.setX( format.getX()+(rx-format.getX())/10 );
			format.setY( format.getY()+(ry-format.getY())/10 );
		}
		
		if ( timer > 150 )
		{
			texts.setAlpha((float)functions.clamp(texts.getAlpha()+0.1f,0,1));
			texts_x += (cx-texts_x)/5;
			texts_y += (cy-texts_y)/5;
			double rx = texts_x + (Game.MOUSE_X-texts_x)/40;
			double ry = texts_y + (Game.MOUSE_Y-texts_y)/40;
			texts.setX( texts.getX()+(rx-texts.getX())/10 );
			texts.setY( texts.getY()+(ry-texts.getY())/10 );
		}
		
		if ( timer > 200 )
		{
			if ( rankList.size() < (Game.playerNum_original-Game.STAT_RANK) && timer%10 == 0)
			{
				rankList.add((o_stuff) Game.createGameObject(
						new o_stuff(280 + rankList.size()*20,123,sys_SpriteLibrary.sprite_icon_rank)));
			}
			else
			{
				if ( killCountList.size() < Game.STAT_KILLCOUNT && timer%10 == 0)
				{
					killCountList.add((o_stuff) Game.createGameObject(
							new o_stuff(280 + killCountList.size()*20,176,sys_SpriteLibrary.sprite_icon_killCount)));
				}
				else
				{
					if ( Game.STAT_RANK > 0 )
					{
						if ( killedBy == null )
						{
							killedBy = (o_stuff) Game.createGameObject(new o_stuff(280,227,Game.STAT_KILLEDBY));
						}
						else
						{
							int tx = 280;
							int ty = 227;
							
							double rx = tx + (Game.MOUSE_X-tx)/40;
							double ry = ty + (Game.MOUSE_Y-ty)/40;
							
							killedBy.setX( killedBy.getX()+(rx - killedBy.getX())/10 );
							killedBy.setY( killedBy.getY()+(ry - killedBy.getY())/10 );
						}
					}
				}
			}
			
			for ( int i = 0; i < rankList.size(); i++ )
			{
				int tx = 280 + i*20;
				int ty = 123;
				
				double rx = tx + (Game.MOUSE_X-tx)/40;
				double ry = ty + (Game.MOUSE_Y-ty)/40;
				rankList.get(i).setX( rankList.get(i).getX()+(rx-rankList.get(i).getX())/10 );
				rankList.get(i).setY( rankList.get(i).getY()+(ry-rankList.get(i).getY())/10 );
			}
			
			for ( int i = 0; i < killCountList.size(); i++ )
			{
				int tx = 280 + i*20;
				int ty = 176;
				
				double rx = tx + (Game.MOUSE_X-tx)/40;
				double ry = ty + (Game.MOUSE_Y-ty)/40;
				killCountList.get(i).setX( killCountList.get(i).getX()+(rx-killCountList.get(i).getX())/10 );
				killCountList.get(i).setY( killCountList.get(i).getY()+(ry-killCountList.get(i).getY())/10 );
			}
		}
		
		if ( timer > 380 )
		{
			if ( button_ok == null ) button_ok = Game.createGameObject(new o_button(320,320,sys_SpriteLibrary.sprite_button_ok));
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
		for ( int i = 0; i < rankList.size(); i++ )
		{
			Game.deleteGameObject(rankList.get(i));
		}
		
		for ( int i = 0; i < killCountList.size(); i++ )
		{
			Game.deleteGameObject(killCountList.get(i));
		}
		
		if ( blackScreen != null ) { Game.deleteGameObject(blackScreen); blackScreen = null; }
		if ( blackBackground != null ) { Game.deleteGameObject(blackBackground); blackBackground = null; }
		if ( winOrLost != null ) { Game.deleteGameObject(winOrLost); winOrLost = null; }
		if ( format != null ) { Game.deleteGameObject(format); format = null; }
		if ( texts != null ) { Game.deleteGameObject(texts); texts = null; }
		if ( killedBy != null ) { Game.deleteGameObject(killedBy); killedBy = null; }
		
		if ( button_ok != null ) { Game.deleteGameObject(button_ok); button_ok = null; }
		
		rankList.clear();
		killCountList.clear();
	}
	
	
	private void buttonControl()
	{
		o_button b_ok = (o_button)button_ok;
		
		if ( b_ok.getReleased())
		{
			getSceneManager().sceneChangeRequest("In Server");
		}
	}
	
	private void networkingStuff()
	{
		Game.PLAYER_kill += Game.STAT_KILLCOUNT;
		Game.PLAYER_rank += Game.STAT_RANK;
		
		if ( Game.STAT_RANK <= 0 ) { Game.PLAYER_win++;}
		else { Game.PLAYER_lost++;  Game.PLAYER_death++;}
		
		// Network
		String URLaddressBase = "searchByUsername";
		String URLaddressUpdate_win = URLaddressBase + "/setWin/" + Game.username + "/" + Game.PLAYER_win; 
		String URLaddressUpdate_lost = URLaddressBase + "/setLost/" + Game.username + "/" + Game.PLAYER_lost; 
		String URLaddressUpdate_kill = URLaddressBase + "/setKill/" + Game.username + "/" + Game.PLAYER_kill; 
		String URLaddressUpdate_death = URLaddressBase + "/setDeath/" + Game.username + "/" + Game.PLAYER_death; 
		String URLaddressUpdate_rank = URLaddressBase + "/setRank/" + Game.username + "/" + Game.PLAYER_rank;
		
		net_Database.dataSet(URLaddressUpdate_win);
		net_Database.dataSet(URLaddressUpdate_lost);
		net_Database.dataSet(URLaddressUpdate_kill);
		net_Database.dataSet(URLaddressUpdate_death);
		net_Database.dataSet(URLaddressUpdate_rank);
	}
}