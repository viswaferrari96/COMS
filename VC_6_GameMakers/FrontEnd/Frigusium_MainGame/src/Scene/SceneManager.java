package Scene;

import java.util.ArrayList;

import MainGame.Game;

public class SceneManager
{
	private ArrayList<Scene> sceneList = new ArrayList<>();
	private String scene = "Log In";
	private String scene_next = "";
	
	private boolean sceneChange = false;
	private int sceneChangeProcedure = 0;
	
	public SceneManager( Game game )
	{
		sceneList.add(new Scene_PreStartScreen0(game,this));
		sceneList.add(new Scene_PreStartScreen1(game,this));
		sceneList.add(new Scene_LogIn(game,this));
		sceneList.add(new Scene_CreateAccount(game,this));
		sceneList.add(new Scene_StartScreen(game,this));
		sceneList.add(new Scene_Report(game,this));
		sceneList.add(new Scene_Lobby(game,this));
		sceneList.add(new Scene_HostANewServer(game,this));
		sceneList.add(new Scene_InServer(game,this));
		sceneList.add(new Scene_InGame(game,this));
		sceneList.add(new Scene_SessionReview(game,this));
		sceneChangeRequest(scene);
	}

	
	private int sceneIndex ( String scene )
	{
		int n = 0;
		
		if ( scene.equals("Pre Start Screen 0")) { return n; }n++;
		if ( scene.equals("Pre Start Screen 1")) { return n; }n++;
		if ( scene.equals("Log In")) { return n; }n++;
		if ( scene.equals("Create Account")) { return n; }n++;
		if ( scene.equals("Start Screen")) { return n; }n++;
		if ( scene.equals("Report")) { return n; }n++;
		if ( scene.equals("Lobby")) { return n; }n++;
		if ( scene.equals("Host a New Server")) { return n; }n++;
		if ( scene.equals("In Server")) { return n; }n++;
		if ( scene.equals("In Game")) { return n; }n++;
		if ( scene.equals("Session Review")) { return n; }n++;
		return -1;
	}
	
	public void step ()
	{
		if (!sceneChange ) { sceneStep(scene); } 
		if ( sceneChange ) { sceneChange(); }
	}
	
	public void sceneChangeRequest( String scene ) { scene_next = scene; sceneChange = true; }
	
	public void sceneChange()
	{
		int n = 0;
		
		if ( sceneChangeProcedure == n )
		{
			sceneList.get(sceneIndex(scene)).setCleanUpComplete(false);
			sceneChangeProcedure++;
		}
		
		n++;
		
		if ( sceneChangeProcedure == n )
		{
			if ( scene.equals(scene_next)) { sceneChangeProcedure++; }
			else
			{
				sceneStepCleanUp(scene);
				
				if (sceneList.get(sceneIndex(scene)).getCleanUpComplete())
				{
					sceneChangeProcedure++;
				}
			}
		}
		
		n++;

		if ( sceneChangeProcedure == n )
		{
			sceneCleanUp(scene);
			sceneSetUp(scene_next);
			scene = scene_next;
			sceneChangeProcedure++;
		}
		
		n++;
		
		if ( sceneChangeProcedure == n )
		{
			sceneList.get(sceneIndex(scene)).setSetUpComplete(false);
			sceneChangeProcedure++;
		}
		
		n++;
		
		if (sceneChangeProcedure == n )
		{
			sceneStepSetUp(scene);
			
			if (sceneList.get(sceneIndex(scene)).getSetUpComplete())
			{
				sceneChange = false;
			}
		}
	}
	
	public void sceneSetUp( String scene ) { sceneList.get(sceneIndex(scene)).sceneSetUp();}
	public void sceneStepSetUp( String scene ) { sceneList.get(sceneIndex(scene)).sceneStepSetUp();}
	public void sceneStep( String scene ) { sceneList.get(sceneIndex(scene)).sceneStep(); sceneChangeProcedure = 0;}
	public void sceneStepCleanUp( String scene ) { sceneList.get(sceneIndex(scene)).sceneStepCleanUp();}
	public void sceneCleanUp( String scene ) { sceneList.get(sceneIndex(scene)).sceneCleanUp();}
	

	public String getScene() { return scene; }
}
