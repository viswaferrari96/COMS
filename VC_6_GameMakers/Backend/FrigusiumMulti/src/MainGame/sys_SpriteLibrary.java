package MainGame;

import java.io.IOException;

public class sys_SpriteLibrary
{
	public static sys_Sprite sprite_SarverrArmor;
	public static sys_Sprite sprite_gun_Rifle_Ascal;
	public static sys_Sprite sprite_gun_Shotgun_Aifse;
	public static sys_Sprite sprite_gun_SMG_Eessl;
	public static sys_Sprite sprite_gun_Sniper_Hease;

	public sys_SpriteLibrary() throws IOException 
	{
		sys_BufferedImageLoader spriteLoader = new sys_BufferedImageLoader(); 
		sprite_SarverrArmor = new sys_Sprite(spriteLoader.loadImage("/armor_SaverrArmor.png"),12,9,32,32,16,16);
		sprite_gun_Rifle_Ascal = new sys_Sprite(spriteLoader.loadImage("/gun_Rifle_ASCAL(AcceleratedShardChargingAutomaticLauncher).png"),7,7);
		sprite_gun_Shotgun_Aifse = new sys_Sprite(spriteLoader.loadImage("/gun_Shotgun_AIFSE(ArtificialInternallyFracturedShardsEmitter).png"),7,7);
		sprite_gun_SMG_Eessl = new sys_Sprite(spriteLoader.loadImage("/gun_SMG_EESSL(EnhancedEmittingSpeedShardLauncher).png"),7,7);
		sprite_gun_Sniper_Hease = new sys_Sprite(spriteLoader.loadImage("/gun_Sniper_HEASE(HighlyEnhancedAccuracyShardsEmitter).png"),7,7);
	}
	
}
