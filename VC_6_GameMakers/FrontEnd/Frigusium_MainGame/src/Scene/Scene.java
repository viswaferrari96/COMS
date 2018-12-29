package Scene;

import MainGame.Game;

public abstract class Scene
{
	private Game game;
	private SceneManager sceneManager;
	private boolean setUpComplete = false;
	private boolean cleanUpComplete = false;
	
	public Scene( Game game, SceneManager sceneManager )
	{
		this.game = game;
		this.sceneManager = sceneManager;
	}
	
	public abstract void sceneSetUp();
	public abstract void sceneStepSetUp();
	public abstract void sceneStep();
	public abstract void sceneStepCleanUp();
	public abstract void sceneCleanUp();

	public Game getGame() { return game; }
	public SceneManager getSceneManager() { return sceneManager; }
	
	public boolean getSetUpComplete() { return setUpComplete; }
	public void setSetUpComplete( boolean setUpComplete) { this.setUpComplete = setUpComplete; }
	
	public boolean getCleanUpComplete() { return cleanUpComplete; }
	public void setCleanUpComplete( boolean cleanUpComplete) { this.cleanUpComplete = cleanUpComplete; }
}
