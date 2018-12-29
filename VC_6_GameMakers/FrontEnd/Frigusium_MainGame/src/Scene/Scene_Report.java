package Scene;

import java.awt.Color;

import MainGame.Game;
import MainGame.GameObject;
import MainGame.functions;
import MainGame.o_button;
import MainGame.o_stuff;
import MainGame.sys_SpriteLibrary;

public class Scene_Report extends Scene
{
	private o_stuff blackScreen = null;
	private o_stuff reportScreen = null;
	private GameObject button_ok = null;
	private o_stuff[] report = null;

	public Scene_Report(Game game, SceneManager sceneManager) { super(game,sceneManager); }

	
	@Override
	public void sceneSetUp()
	{
		Game.CAMERA.setX(0);
		Game.CAMERA.setY(0);
		
		blackScreen = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_BlackScreen));
		reportScreen = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_ReportScreen));
		button_ok = Game.createGameObject(new o_button(Game.WIDTH/2,Game.HEIGHT*(8./9),sys_SpriteLibrary.sprite_button_ok));
		
		report = new o_stuff[7];
		report[0] = (o_stuff)Game.createGameObject(new o_stuff(210,90,""));
		
		report[1] = (o_stuff)Game.createGameObject(new o_stuff(210,130,""));
		report[2] = (o_stuff)Game.createGameObject(new o_stuff(210,150,""));
		
		report[3] = (o_stuff)Game.createGameObject(new o_stuff(210,190,""));
		
		report[4] = (o_stuff)Game.createGameObject(new o_stuff(210,230,""));
		report[5] = (o_stuff)Game.createGameObject(new o_stuff(210,250,""));
		
		report[6] = (o_stuff)Game.createGameObject(new o_stuff(210,290,""));
		
		int sessionPlayed = Game.PLAYER_lost+Game.PLAYER_win;
		int sessionSurvived = Game.PLAYER_win;
		int sessionDied = Game.PLAYER_death;
		double sessionSurvivalRate = ((double)Game.PLAYER_win)/Game.PLAYER_death;
		int sessionKillcount = Game.PLAYER_kill;
		int sessionDeathcount = Game.PLAYER_death;
		double sessionKillDeathRate = ((double)Game.PLAYER_kill)/Game.PLAYER_death;
		
		report[0].setString("Sessions played    :       " + sessionPlayed+"\n");
		report[1].setString("Sessions Survived  :       " + sessionSurvived+"\n");
		report[2].setString("Sessions Died      :       " + sessionDied+"\n");
		report[3].setString("Survival Rate      :       " + sessionSurvivalRate+"\n");
		report[4].setString("Kill Counts        :       " + sessionKillcount+"\n");
		report[5].setString("Death Counts       :       " + sessionDeathcount+"\n");
		report[6].setString("Kill/Death Ratio   :       " + sessionKillDeathRate+"\n");

		for ( int i = 0; i < 7; i ++ )
		{
			report[i].setStringColor(new Color(145,52,52));
		}
		blackScreen.setLayer(10);
		blackScreen.setAlpha(1f);
	}
	@Override
	public void sceneStepSetUp()
	{
		blackScreen.setAlpha((float)functions.clamp(blackScreen.getAlpha()-0.005f,0,1));
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
		if ( reportScreen != null ) { Game.deleteGameObject(reportScreen); reportScreen = null; }
		if ( button_ok != null ) { Game.deleteGameObject(button_ok); button_ok = null; }
		
		for ( int i = 0; i < 7; i ++ )
		{
			if ( report[i] != null ) { report[i].delete(); report[i] = null; }
		}
	}
	
	
	private void buttonControl()
	{
		o_button b_ok = (o_button)button_ok;
		
		if ( b_ok.getReleased())
		{
			getSceneManager().sceneChangeRequest("Start Screen");
		}
	}
}