package MainGame;

import java.io.IOException;

public class sys_SpriteLibrary
{
	//Screen
	public static sys_Sprite sprite_screen_BlackScreen;
	public static sys_Sprite sprite_screen_PreTitleScreen0;
	public static sys_Sprite sprite_screen_PreTitleScreen1;
	public static sys_Sprite sprite_screen_LogInScreen;
	public static sys_Sprite sprite_screen_AccountCreationScreen;
	public static sys_Sprite sprite_screen_TitleScreen;
	public static sys_Sprite sprite_screen_ReportScreen;
	public static sys_Sprite sprite_screen_LobbyScreen;
	public static sys_Sprite sprite_screen_HostingServerScreen;
	
	public static sys_Sprite sprite_screen_SessionReviewScreen_YouHaveSurvived;
	public static sys_Sprite sprite_screen_SessionReviewScreen_YouHaveDied;
	public static sys_Sprite sprite_screen_SessionReviewScreen_format;
	public static sys_Sprite sprite_screen_SessionReviewScreen_texts;
	
	public static sys_Sprite sprite_screen_ServerScreen;
	public static sys_Sprite sprite_screen_ServerScreen_map;
	
	public static sys_Sprite sprite_screen_background;
	
	//Button
	public static sys_Sprite sprite_button_start;
	public static sys_Sprite sprite_button_ok;
	public static sys_Sprite sprite_button_Back;
	public static sys_Sprite sprite_button_logIn;
	public static sys_Sprite sprite_button_Report;
	public static sys_Sprite sprite_button_Create;
	public static sys_Sprite sprite_button_NewUser;
	public static sys_Sprite sprite_button_Quit;
	public static sys_Sprite sprite_button_HostANewServer;
	public static sys_Sprite sprite_button_serverList;
	public static sys_Sprite sprite_button_refresh;
	
	//InputBox
	public static sys_Sprite sprite_inputBox_inputBox0;
	public static sys_Sprite sprite_inputBox_inputBox1;
	
	//Armor
	public static sys_Sprite sprite_SarverrArmor;
	
	//Gun
	public static sys_Sprite sprite_gun_Rifle_Ascal;
	public static sys_Sprite sprite_gun_Shotgun_Aifse;
	public static sys_Sprite sprite_gun_SMG_Eessl;
	public static sys_Sprite sprite_gun_Sniper_Hease;
	
	//Icon
	public static sys_Sprite sprite_icon_rank;
	public static sys_Sprite sprite_icon_killCount;
	
	//Map
	public static sys_Sprite sprite_map_wall_ice;
	public static sys_Sprite sprite_map_floor_ice;
	public static sys_Sprite sprite_map_basic_ice;
	
	//Etc
	public static sys_Sprite sprite_playerListPad_admin;
	public static sys_Sprite sprite_playerListPad_player;
	public static sys_Sprite sprite_playerListPad_normal;
	

	public sys_SpriteLibrary() throws IOException 
	{
		sys_BufferedImageLoader spriteLoader = new sys_BufferedImageLoader(); 
		
		
		//Screen Stuff
		sprite_screen_background = new sys_Sprite(spriteLoader.loadImage("/background.png"),0,0);
		sprite_screen_BlackScreen = new sys_Sprite(spriteLoader.loadImage("/BlackScreen.png"),0,0);
		sprite_screen_PreTitleScreen0 = new sys_Sprite(spriteLoader.loadImage("/PreTitleScreen0.png"),0,0);
		sprite_screen_PreTitleScreen1 = new sys_Sprite(spriteLoader.loadImage("/PreTitleScreen1.png"),0,0);
		sprite_screen_TitleScreen = new sys_Sprite(spriteLoader.loadImage("/TitleScreen.png"),0,0);
		sprite_screen_ReportScreen = new sys_Sprite(spriteLoader.loadImage("/ReportScreen.png"),0,0);

		sprite_screen_LogInScreen = new sys_Sprite(spriteLoader.loadImage("/LogInScreen.png"),0,0);
		sprite_screen_AccountCreationScreen = new sys_Sprite(spriteLoader.loadImage("/AccountCreationScreen.png"),0,0);

		sprite_screen_LobbyScreen = new sys_Sprite(spriteLoader.loadImage("/LobbyScreen.png"),0,0);
		sprite_screen_HostingServerScreen = new sys_Sprite(spriteLoader.loadImage("/HostingServerScreen.png"),0,0);
		
		sprite_screen_SessionReviewScreen_YouHaveSurvived = new sys_Sprite(spriteLoader.loadImage("/SessionReviewScreen_YouHaveSurvived.png"));
		sprite_screen_SessionReviewScreen_YouHaveDied = new sys_Sprite(spriteLoader.loadImage("/SessionReviewScreen_YouHaveDied.png"));
		sprite_screen_SessionReviewScreen_format = new sys_Sprite(spriteLoader.loadImage("/SessionReviewScreen_format.png"));
		sprite_screen_SessionReviewScreen_texts = new sys_Sprite(spriteLoader.loadImage("/SessionReviewScreen_texts.png"));
		
		sprite_screen_ServerScreen = new sys_Sprite(spriteLoader.loadImage("/ServerScreen.png"),0,0);
		sprite_screen_ServerScreen_map = new sys_Sprite(spriteLoader.loadImage("/ServerScreen_map.png"),0,0);
		
		//Button stuff
		sprite_button_start = new sys_Sprite(spriteLoader.loadImage("/button_Start.png"),1,3,62,16);
		sprite_button_ok = new sys_Sprite(spriteLoader.loadImage("/button_OK.png"),1,3,62,16);
		sprite_button_Back = new sys_Sprite(spriteLoader.loadImage("/button_Back.png"),1,3,62,16);
		sprite_button_logIn = new sys_Sprite(spriteLoader.loadImage("/button_LogIn.png"),1,3,62,16);
		sprite_button_Report = new sys_Sprite(spriteLoader.loadImage("/button_Report.png"),1,3,62,16);
		sprite_button_Create = new sys_Sprite(spriteLoader.loadImage("/button_Create.png"),1,3,62,16);
		sprite_button_NewUser = new sys_Sprite(spriteLoader.loadImage("/button_NewUser.png"),1,3,62,16);
		sprite_button_Quit = new sys_Sprite(spriteLoader.loadImage("/button_Quit.png"),1,3,62,16);
		sprite_button_HostANewServer = new sys_Sprite(spriteLoader.loadImage("/button_HostANewServer.png"),1,3,212,15);
		sprite_button_serverList = new sys_Sprite(spriteLoader.loadImage("/button_serverList.png"),1,3,206,15);
		sprite_button_refresh = new sys_Sprite(spriteLoader.loadImage("/button_refresh.png"),1,3,206,12);
		
		//InputBox stuff
		sprite_inputBox_inputBox0 = new sys_Sprite(spriteLoader.loadImage("/inputBar.png"),1,4,121,15);
		sprite_inputBox_inputBox1 = new sys_Sprite(spriteLoader.loadImage("/chattingInputBar.png"),1,4,332,15);
		
		//Armor
		sprite_SarverrArmor = new sys_Sprite(spriteLoader.loadImage("/armor_SaverrArmor.png"),12,9,32,32,16,16);
		
		//Gun
		sprite_gun_Rifle_Ascal = new sys_Sprite(spriteLoader.loadImage("/gun_Rifle_ASCAL(AcceleratedShardChargingAutomaticLauncher).png"),7,7);
		sprite_gun_Shotgun_Aifse = new sys_Sprite(spriteLoader.loadImage("/gun_Shotgun_AIFSE(ArtificialInternallyFracturedShardsEmitter).png"),7,7);
		sprite_gun_SMG_Eessl = new sys_Sprite(spriteLoader.loadImage("/gun_SMG_EESSL(EnhancedEmittingSpeedShardLauncher).png"),7,7);
		sprite_gun_Sniper_Hease = new sys_Sprite(spriteLoader.loadImage("/gun_Sniper_HEASE(HighlyEnhancedAccuracyShardsEmitter).png"),7,7);
	
		//Icon
		sprite_icon_rank = new sys_Sprite(spriteLoader.loadImage("/Icon_Rank.png"));
		sprite_icon_killCount = new sys_Sprite(spriteLoader.loadImage("/Icon_KillCount.png"));
		
		//Map
		sprite_map_wall_ice = new sys_Sprite(spriteLoader.loadImage("/map_wall_ice.png"),5,3,32,64,16,48);
		sprite_map_floor_ice = new sys_Sprite(spriteLoader.loadImage("/map_floor_ice.png"),16,16);
		sprite_map_basic_ice = new sys_Sprite(spriteLoader.loadImage("/map_basic_ice.png"),16,48);
		
		//Etc
		sprite_playerListPad_admin = new sys_Sprite(spriteLoader.loadImage("/playerListPad_admin.png"),0,0);
		sprite_playerListPad_player = new sys_Sprite(spriteLoader.loadImage("/playerListPad_player.png"),0,0);
		sprite_playerListPad_normal = new sys_Sprite(spriteLoader.loadImage("/playerListPad_normal.png"),0,0);
	}
	
}
