package Scene;

import MainGame.Game;
import MainGame.functions;
import MainGame.o_stuff;
import MainGame.sys_SpriteLibrary;

public class Scene_PreStartScreen1 extends Scene
{
	
	private o_stuff blackScreen = null;
	private o_stuff preTitleScreen = null;
	
	private int timer = 0;

	public Scene_PreStartScreen1(Game game, SceneManager sceneManager) { super(game, sceneManager); }

	
	@Override
	public void sceneSetUp()
	{
		blackScreen = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_BlackScreen));
		preTitleScreen = (o_stuff) Game.createGameObject(new o_stuff(0,0,sys_SpriteLibrary.sprite_screen_PreTitleScreen1));
	
		blackScreen.setLayer(1);
		blackScreen.setAlpha(1f);
	}
	@Override
	public void sceneStepSetUp()
	{
		blackScreen.setAlpha((float)functions.clamp(blackScreen.getAlpha()-0.01f,0,1));
		if ( blackScreen.getAlpha() == 0.f )
		{
			setSetUpComplete(true);
		}
	}
	@Override
	public void sceneStep()
	{
		timer++;
		if ( timer > 45 )
		{
			getSceneManager().sceneChangeRequest("Log In");
		}
	}
	@Override
	public void sceneStepCleanUp()
	{
		blackScreen.setAlpha((float)functions.clamp(blackScreen.getAlpha()+0.01f,0,1));
		if ( blackScreen.getAlpha() == 1.f )
		{
			setCleanUpComplete(true);
		}
	}
	@Override
	public void sceneCleanUp()
	{
		if ( blackScreen != null ) { Game.deleteGameObject(blackScreen); blackScreen = null; }
		if ( preTitleScreen != null ) { Game.deleteGameObject(preTitleScreen); preTitleScreen = null; }
	}
}
