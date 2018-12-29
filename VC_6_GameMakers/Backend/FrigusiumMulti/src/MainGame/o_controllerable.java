package MainGame;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class o_controllerable extends GameObject
{
	
	private static String NAME = "o_controllerable";
	private String playerName = "";
	private sys_Sprite own_sprite = sys_SpriteLibrary.sprite_SarverrArmor;
	
	private int stat_killCount = 0;
	
	private double time = 0;
	
	protected int mouse_x;
	protected int mouse_y;

	protected boolean mouse_left;
	protected boolean mouse_left_pressed;
	protected boolean mouse_left_released;
	
	protected boolean mouse_right;
	protected boolean mouse_right_pressed;
	protected boolean mouse_right_released;
	
	private boolean key_w = false;
	private boolean key_a = false;
	private boolean key_s = false;
	private boolean key_d = false;
	private boolean key_space = false;
	private boolean key_up = false;
	private boolean key_down = false;
	private boolean key_left = false;
	private boolean key_right = false;
	private boolean key_esc = false;
	
	private double spd_up = 0;
	private double spd_down = 0;
	private double spd_left = 0;
	private double spd_right = 0;

	protected double spd_dir = 0;
	protected double spd_dirFinal = 0;
	protected double spd_max = 4;
	private double spd_accel = 0.04;
	
	private double spd_recoil_x = 0;
	private double spd_recoil_y = 0;
	
	private int timer_particleGenerate_ember = 0;
	private int timer_particleGenerate_track = 0;
	
	private double timer_rightTurn = 120;
	private double timer_leftTurn = -120;

	private boolean state_shooting = false;
	private boolean state_shoot = false;
	
	private GameObject lastHitter = null;
	
	private sys_Sprite shooting_sprite;
	private int shooting_distanceX = -3;
	private int shooting_distanceY = 4;
	private int shooting_bulletNum;
	private double shooting_recoil;
	private double shooting_angle;
	private double shooting_coolTime_val;
	private double shooting_coolTime;
	private double shooting_bulletSpdMin;
	private double shooting_bulletSpdMax;
	private double shooting_spread;
	private double shooting_accuracy;
	private double shooting_dmg_min;
	private double shooting_dmg_max;
	private double shooting_staminaConsume;
	private int shooting_bulletLifeTime;
	
	private o_gun own_gun = (o_gun) Game.createGameObject(new o_gun(this,shooting_distanceX,shooting_distanceY));
	
	private String ani = "stand down";
	private String ani_pre = "walk down";
	private String ani_last = "walk down";
	
	private double ani_spd = 0;
	private double ani_spd_stand = 0.2;
	private double ani_spd_walk = 0.2;
	private double ani_spd_shootWalk = 0.1;
	
	private double ani_frame = 0;
	private int ani_frameNum = 0;
	private int ani_imageIndex = 0;
	private int ani_startFrame = 0;
	private int ani_headingSide = 3;
	
	private double stamina;
	private double stamina_max;
	private double stamina_heal;
	private int timer_staminaHealCoolTime = 0;
	private int timer_staminaHealCoolTime_val;
	
	
	public o_controllerable(double x, double y, String playerName)
	{
		super(NAME, x, y);
		this.playerName = playerName;
		
		stamina_max = 200;
		stamina = stamina_max;
		stamina_heal = 1;
		timer_staminaHealCoolTime_val = 45;
		
		setBloodMax(200);
		setBlood(blood_max);
		setLayer(1);
		setHitbox(Game.createHitbox(new col_Hitbox((int)x,(int)y,12,12,this)));
	}

	public void step()
	{
		inputControl();
		//if ( this != Game.PLAYER ) AI();
		stateControl();
		movementControl();
		gunSetting("rifle");
		shootingControl();
		particleGenerateControl();
		bloodControl();
		staminaControl();
		spriteControl();
		
		setDepth((int)y);
		updateHitbox();
		time++;
	}

	public void draw(Graphics g) 
	{
		// Drawing the Armor
		g.drawImage( own_sprite.getImage((int)functions.clamp(ani_imageIndex,0,own_sprite.getImageNum()-1)),
					(int)x - own_sprite.getWidth()/2,
					(int)y - own_sprite.getHeight() + 3,
					null);
		
		// Drawing the UI
		int healthBar_length = 60;
		int healthBar_thickness = 2;
		
		g.setColor(Color.white);
		String playerName = getPlayerName();
		FontMetrics metrics = g.getFontMetrics();
		g.drawString(playerName,(int)x - metrics.stringWidth(playerName)/2, (int)y-38);
		
		if ( Game.PLAYER != this )
		{
			g.setColor(Color.red);
			g.fillRect((int)x-healthBar_length/2, (int)y-36, healthBar_length, healthBar_thickness);
			g.setColor(Color.white);
			g.fillRect((int)x-healthBar_length/2, (int)y-36, (int)((blood/blood_max)*healthBar_length), healthBar_thickness);
		}
	}
	
	private void AI()
	{
		GameObject target = functions.closestKind(NAME, this);
			//target = Game.PLAYER;
		if ( target != null )
		{
			mouse_x = (int) target.getX();
			mouse_y = (int) target.getY();
			
			if ((Math.abs(target.getX()-getX()) < Game.WIDTH/2) && 
				(Math.abs(target.getY()-getY()) < Game.HEIGHT/2) && 
				 Math.random() < 1)
			{ key_space = true; } else { key_space = false; }
			
			if ( target.getX() > getX() ) { key_d = true; } else { key_d = false; }
			if ( target.getX() < getX() ) { key_a = true; } else { key_a = false; }
			if ( target.getY() > getY() ) { key_s = true; } else { key_s = false; }
			if ( target.getY() < getY() ) { key_w = true; } else { key_w = false; }
		}
		else
		{
			key_space = false;
			key_d = false;
			key_a = false;
			key_s = false;
			key_w = false;
		}
	}
	
	private void stateControl()
	{
		if ( state_shooting || shooting_coolTime > shooting_coolTime_val/2 || key_space )
		{
			state_shoot = true;
		}
		else
		{
			state_shoot = false;
		}
	}
	
	
	private void spriteControl()
	{	
		if ( spd > 0.2 )
		{
			if ( state_shoot )
			{
				if ( functions.angleRealDifference(spd_dir-180,shooting_angle) < 25 )
				{
					if ( spd_dirFinal > 315 || spd_dirFinal <= 45 ) { ani = "walk right"; ani_headingSide = 0;}
					if ( spd_dirFinal > 45 && spd_dirFinal <= 135 ) { ani = "walk up"; ani_headingSide = 1;}
					if ( spd_dirFinal > 135 && spd_dirFinal <= 255 ) { ani = "walk left"; ani_headingSide = 2;}
					if ( spd_dirFinal > 255 && spd_dirFinal <= 315 ) { ani = "walk down"; ani_headingSide = 3;}
				}
				else
				{
					if ( shooting_angle > 315 || shooting_angle <= 45 ) { ani = "shoot walk right"; ani_headingSide = 0;}
					if ( shooting_angle > 45 && shooting_angle <= 135 ) { ani = "shoot walk up"; ani_headingSide = 1;}
					if ( shooting_angle > 135 && shooting_angle <= 255 ) { ani = "shoot walk left"; ani_headingSide = 2;}
					if ( shooting_angle > 255 && shooting_angle <= 315 ) { ani = "shoot walk down"; ani_headingSide = 3;}
				}
			}
			else
			{
				if ( spd_dir > 315 || spd_dir <= 45 ) { ani = "walk right"; ani_headingSide = 0;}
				if ( spd_dir > 45 && spd_dir <= 135 ) { ani = "walk up"; ani_headingSide = 1;}
				if ( spd_dir > 135 && spd_dir <= 255 ) { ani = "walk left"; ani_headingSide = 2;}
				if ( spd_dir > 255 && spd_dir <= 315 ) { ani = "walk down"; ani_headingSide = 3;}
			}
		}
		else
		{
			if ( state_shoot )
			{
				if ( shooting_angle > 315 || shooting_angle <= 45 ) { ani = "shoot right"; ani_headingSide = 0;}
				if ( shooting_angle > 45 && shooting_angle <= 135 ) { ani = "shoot up"; ani_headingSide = 1;}
				if ( shooting_angle > 135 && shooting_angle <= 255 ) { ani = "shoot left"; ani_headingSide = 2;}
				if ( shooting_angle > 255 && shooting_angle <= 315 ) { ani = "shoot down"; ani_headingSide = 3;}
			}
			else
			{
				if ( shooting_angle > 315 || shooting_angle <= 45 ) { ani = "stand right"; ani_headingSide = 0;}
				if ( shooting_angle > 45 && shooting_angle <= 135 ) { ani = "stand up"; ani_headingSide = 1;}
				if ( shooting_angle > 135 && shooting_angle <= 255 ) { ani = "stand left"; ani_headingSide = 2;}
				if ( shooting_angle > 255 && shooting_angle <= 315 ) { ani = "stand down"; ani_headingSide = 3;}
			}
		}
		
		if ( !ani.equals(ani_pre))
		{
			if ( ani.equals("stand right")) 			{ ani_startFrame =  0; ani_frameNum = 12; ani_spd = ani_spd_stand; }
			else if ( ani.equals("stand up")) 			{ ani_startFrame = 12; ani_frameNum = 12; ani_spd = ani_spd_stand; }
			else if ( ani.equals("stand left")) 		{ ani_startFrame = 24; ani_frameNum = 12; ani_spd = ani_spd_stand; }
			else if ( ani.equals("stand down")) 		{ ani_startFrame = 36; ani_frameNum = 12; ani_spd = ani_spd_stand; }
			else if ( ani.equals("walk right")) 		{ ani_startFrame = 48; ani_frameNum = 12; ani_spd = ani_spd_walk; }
			else if ( ani.equals("walk up")) 			{ ani_startFrame = 60; ani_frameNum = 12; ani_spd = ani_spd_walk; }
			else if ( ani.equals("walk left")) 			{ ani_startFrame = 72; ani_frameNum = 12; ani_spd = ani_spd_walk; }
			else if ( ani.equals("walk down")) 			{ ani_startFrame = 84; ani_frameNum = 12; ani_spd = ani_spd_walk; }
			else if ( ani.equals("shoot right")) 		{ ani_startFrame = 96; ani_frameNum = 1; ani_spd = 0; }
			else if ( ani.equals("shoot up")) 			{ ani_startFrame = 97; ani_frameNum = 1; ani_spd = 0; }
			else if ( ani.equals("shoot left")) 		{ ani_startFrame = 98; ani_frameNum = 1; ani_spd = 0; }
			else if ( ani.equals("shoot down")) 		{ ani_startFrame = 99; ani_frameNum = 1; ani_spd = 0; }
			else if ( ani.equals("shoot walk right")) 	{ ani_startFrame = 48; ani_frameNum = 12; ani_spd = ani_spd_shootWalk; }
			else if ( ani.equals("shoot walk up")) 		{ ani_startFrame = 60; ani_frameNum = 12; ani_spd = ani_spd_shootWalk; }
			else if ( ani.equals("shoot walk left")) 	{ ani_startFrame = 72; ani_frameNum = 12; ani_spd = ani_spd_shootWalk; }
			else if ( ani.equals("shoot walk down")) 	{ ani_startFrame = 84; ani_frameNum = 12; ani_spd = ani_spd_shootWalk; }
			
			ani_last = ani_pre;
		}
		ani_pre = ani;
		
		ani_frame = (ani_frame+ani_spd)%ani_frameNum;
		ani_imageIndex = (int)functions.clamp((ani_startFrame + ani_frame),0,own_sprite.getImageNum()-1);
	}
	
	private void gunSetting( String gun )
	{
		if ( gun == "shotgun" )
		{
			shooting_sprite = sys_SpriteLibrary.sprite_gun_Shotgun_Aifse;
			shooting_bulletNum = 20;
			shooting_recoil = 90;
			shooting_coolTime_val = 40;
			shooting_bulletSpdMin = 20;
			shooting_bulletSpdMax = 60;
			shooting_spread = 15;
			shooting_accuracy = 10;
			shooting_dmg_min = 5;
			shooting_dmg_max = 8;
			shooting_bulletLifeTime = 40;
			shooting_staminaConsume = 40;
		}
		if ( gun == "submachinegun" )
		{
			shooting_sprite = sys_SpriteLibrary.sprite_gun_SMG_Eessl;
			shooting_bulletNum = 1;
			shooting_recoil = 12;
			shooting_coolTime_val = 2;
			shooting_bulletSpdMin = 10;
			shooting_bulletSpdMax = 40;
			shooting_spread = 0;
			shooting_accuracy = 20;
			shooting_dmg_min = 5;
			shooting_dmg_max = 15;
			shooting_bulletLifeTime = 40;
			shooting_staminaConsume = 5;
		}
		if ( gun == "rifle" )
		{
			shooting_sprite = sys_SpriteLibrary.sprite_gun_Rifle_Ascal;
			shooting_bulletNum = 1;
			shooting_recoil = 40;
			shooting_coolTime_val = 8;
			shooting_bulletSpdMin = 60;
			shooting_bulletSpdMax = 100;
			shooting_spread = 0;
			shooting_accuracy = 3;
			shooting_dmg_min = 20;
			shooting_dmg_max = 25;
			shooting_bulletLifeTime = 30;
			shooting_staminaConsume = 8;
		}
		if ( gun == "sniper" )
		{
			shooting_sprite = sys_SpriteLibrary.sprite_gun_Sniper_Hease;
			shooting_bulletNum = 1;
			shooting_recoil = 110;
			shooting_coolTime_val = 50;
			shooting_bulletSpdMin = 120;
			shooting_bulletSpdMax = 120;
			shooting_spread = 0;
			shooting_accuracy = 0;
			shooting_dmg_min = 50;
			shooting_dmg_max = 70;
			shooting_bulletLifeTime = 30;
			shooting_staminaConsume = 100;
		}
		
		int gunDepth = 0;
		if ( ani_headingSide == 0 ) { gunDepth = 10; }
		if ( ani_headingSide == 1 ) { gunDepth = -10; }
		if ( ani_headingSide == 2 ) { gunDepth = -10; }
		if ( ani_headingSide == 3 ) { gunDepth = 10; }
		
		own_gun.setSprite(shooting_sprite);
		own_gun.setAngle(shooting_angle);
		own_gun.setDepth(getDepth() + gunDepth);
	}
	
	private void shootingControl()
	{
		double shootingDirX = 0;
		double shootingDirY = 0;
		
		boolean key_pressed = false;
		
		if ( key_right ) { shootingDirX += 10; key_pressed = true; }
		if ( key_left ) 	{ shootingDirX -= 10; key_pressed = true; }
		if ( key_up ) 	{ shootingDirY -= 10; key_pressed = true; }
		if ( key_down ) 	{ shootingDirY += 10; key_pressed = true; }
		
		double shooting_angle_goal;
		if ( key_pressed ) { shooting_angle_goal = functions.getDirection(0, 0, shootingDirX, shootingDirY) ; }
		else { shooting_angle_goal = functions.getDirection(x, y, mouse_x, mouse_y) ;}
		
		shooting_angle = functions.angleCorrect(shooting_angle);
		shooting_angle += functions.angleMoving(shooting_angle,shooting_angle_goal,10);
		
		if ( ( key_space || mouse_left )&&
				 shooting_coolTime <= 0 &&
				 stamina > 0)
			{
				state_shooting = true;
			}
			else
			{
				state_shooting = false;
				shooting_coolTime --;
			}
		
		if ( state_shooting )
		{
			if ( shooting_sprite == null ) { System.out.println("fuckyou"); }
			int gun_fire_x = (int) (x + Math.cos(Math.toRadians(shooting_angle))*(shooting_distanceX + shooting_sprite.getWidth() - shooting_sprite.getCenterX())
							   		  + Math.cos(Math.toRadians(shooting_angle-90))*(shooting_distanceY));
			int gun_fire_y = (int) (y - Math.sin(Math.toRadians(shooting_angle))*(shooting_distanceX + shooting_sprite.getWidth() - shooting_sprite.getCenterX())
			   		  				  - Math.sin(Math.toRadians(shooting_angle-90))*(shooting_distanceY));
			
			double shooting_angleFinal = shooting_angle + r.nextDouble()*shooting_accuracy - r.nextDouble()*shooting_accuracy/2;
			for ( int i = 0; i < shooting_bulletNum; i++ )
			{
				int fire_x = gun_fire_x;
				int fire_y = gun_fire_y;
				double dmg = r.nextDouble()*(shooting_dmg_max-shooting_dmg_min) + shooting_dmg_min;
				double spd = r.nextDouble()*(shooting_bulletSpdMax-shooting_bulletSpdMin) + shooting_bulletSpdMin;
				double angle = r.nextDouble()*shooting_spread - r.nextDouble()*shooting_spread/2 + shooting_angleFinal;
				double lifeTime = shooting_bulletLifeTime;
				int height = own_gun.getHeight();
				
				bulletControl(fire_x,fire_y,dmg,spd,angle,lifeTime,height);
			}
			
			stamina -= shooting_staminaConsume;
			timer_staminaHealCoolTime = timer_staminaHealCoolTime_val;
			
			for ( int i = 0; i < 0; i++ )
			{
				Color color = new Color(255,255,200);
				Game.createGameObject(new o_particle_ember(gun_fire_x,gun_fire_y,shooting_angleFinal + r.nextDouble()*10 - 5,color,spd_x,spd_y,1));
			}
			
			double shooting_recoilAngle = shooting_angleFinal-180 + r.nextDouble()*20 - 10;
			double shooting_recoil_x = Math.cos(Math.toRadians(shooting_recoilAngle))*shooting_recoil;
			double shooting_recoil_y =-Math.sin(Math.toRadians(shooting_recoilAngle))*shooting_recoil;
			
			spd_recoil_x = shooting_recoil_x/5;
			spd_recoil_y = shooting_recoil_y/5;
			
			if ( this == Game.PLAYER )
			{
				Game.CAMERA.setX(Game.CAMERA.getX() + shooting_recoil_x/10);
				Game.CAMERA.setY(Game.CAMERA.getY() + shooting_recoil_y/10);
			}
			
			shooting_coolTime = shooting_coolTime_val;
		}
	}
	
	public void bulletControl(int fire_x,int fire_y,double dmg,double spd,double angle,double lifeTime,int height)
	{
		o_bullet own_bullet = (o_bullet) Game.createGameObject(new o_bullet(fire_x,fire_y,this,60));
		own_bullet.setDmg(dmg);
		own_bullet.setSpd(spd);
		own_bullet.setAngle(angle);
		own_bullet.setLifeTime(lifeTime);
		own_bullet.setHeight(height);
	}
	
	public void destroy()
	{
		if ( lastHitter == Game.PLAYER ) { Game.UI.addPlayerText(getPlayerName() + " Silenced"); }
		Game.UI.addPublicText(getPlayerName() + " Silenced");
		Game.deleteHitbox(getHitbox());
		Game.deleteGameObject(own_gun);
		Game.setPlayerNum(Game.getPlayerNum()-1);
		for ( int i = 0; i < 30; i++ )
		{
			Game.createGameObject(new o_particle_ember(x,y));
		}
	}
	
	public void inputControl()
	{
		if ( Game.PLAYER == this )
		{
			key_w = Game.getKey("w");
			key_a = Game.getKey("a");
			key_s = Game.getKey("s");
			key_d = Game.getKey("d");
			key_space = Game.getKey("space");

			key_up = Game.getKey("up");
			key_down = Game.getKey("down");
			key_left = Game.getKey("left");
			key_right = Game.getKey("right");
			
			mouse_left = Game.MOUSE_CLICK_LEFT;
			mouse_left_pressed = Game.MOUSE_CLICK_LEFT_pressed;
			mouse_left_released = Game.MOUSE_CLICK_LEFT_released;
			
			mouse_right = Game.MOUSE_CLICK_RIGHT;
			mouse_right_pressed = Game.MOUSE_CLICK_RIGHT_pressed;
			mouse_right_released = Game.MOUSE_CLICK_RIGHT_released;
		}
		else
		{
			key_w = false;
			key_a = false;
			key_s = false;
			key_d = false;
			key_space = false;

			key_up = false;
			key_down = false;
			key_left = false;
			key_right = false;
			
			mouse_left = false;
			mouse_left_pressed = false;
			mouse_left_released = false;
			
			mouse_right = false;
			mouse_right_pressed = false;
			mouse_right_released = false;	
		}
		
		mouse_x =  Game.MOUSE_X;
		mouse_y =  Game.MOUSE_Y;

		key_esc = Game.getKey("esc");
		if ( key_esc ){ Game.WINDOW.close(); }
	}
	
	public void movementControl()
	{
		if ( key_w )
		{
			if ( spd_up < spd_max ) { spd_up += spd_accel; }
			else { spd_up = spd_max; }
		}
		else
		{
			if ( spd_up > 0 ) { spd_up -= spd_accel; }
			else { spd_up = 0; }
		}
		
		
		if ( key_s )
		{
			if ( spd_down < spd_max ) { spd_down += spd_accel; }
			else { spd_down = spd_max; }
		}
		else
		{
			if ( spd_down > 0 ) { spd_down -= spd_accel; }
			else { spd_down = 0; }
		}
		
		
		if ( key_a )
		{
			if ( spd_left < spd_max ) { spd_left += spd_accel; }
			else { spd_left = spd_max; }
		}
		else
		{
			if ( spd_left > 0 ) { spd_left -= spd_accel; }
			else { spd_left = 0; }
		}
		
		
		if ( key_d )
		{
			if ( spd_right < spd_max ) { spd_right += spd_accel; }
			else { spd_right = spd_max; }
		}
		else
		{
			if ( spd_right > 0 ) { spd_right -= spd_accel; }
			else { spd_right = 0; }
		}
		
		spd_x = spd_right-spd_left;
		spd_y = spd_down-spd_up;
		
		spd_recoil_x += (0-spd_recoil_x)/10;
		spd_recoil_y += (0-spd_recoil_y)/10;
		
		spd_dir = functions.angleCorrect(functions.getDirection(0, 0, spd_x, spd_y));
		
		double spd_preCorrection = getSpd();
		if ( getSpd() > spd_max)
		{
			spd_x *= spd_max/spd_preCorrection;
			spd_y *= spd_max/spd_preCorrection;
		}
		
		spd_x += spd_recoil_x;
		spd_y += spd_recoil_y;
		
		spd_dirFinal = functions.angleCorrect(functions.getDirection(0, 0, spd_x, spd_y));
		
		x += spd_x;
		y += spd_y;
		
		spd = getSpd();
	}
	
	public void particleGenerateControl()
	{
		if ( getSpd() > spd_max/2) { particleGenerate_ember(); }
		if ( getSpd() > spd_max/2) { particleGenerate_track(); }
	}

	public void particleGenerate_ember()
	{
		int particleNum;
		
		if ( Game.PLAYER == this ) { particleNum = 3; }
		else { particleNum = 1; }
		
		if ( timer_particleGenerate_ember <= 0 )
		{
			int create_x = (int) x;
			int create_y = (int) y;
			for ( int i = 0; i < particleNum; i++ )
			{
				Game.createGameObject(new o_particle_ember(create_x,create_y));
			}
			
			timer_particleGenerate_ember = r.nextInt(10)+1;
		}
		else
		{
			timer_particleGenerate_ember--;
		}
	}
	
	public void particleGenerate_track()
	{
		double traceSize = 4;
		
		if ( timer_particleGenerate_track <= 0 )
		{
			double skatingRange = Math.cos(Math.toRadians(time*3))*2 + 2;
			double spd_dir = functions.getDirection(0,0,spd_x,spd_y);
			int create_x1 = (int) (x + Math.cos(Math.toRadians(spd_dir+90))*skatingRange);
			int create_y1 = (int) (y - Math.sin(Math.toRadians(spd_dir+90))*skatingRange);
			int create_x2 = (int) (x + Math.cos(Math.toRadians(spd_dir-90))*skatingRange);
			int create_y2 = (int) (y - Math.sin(Math.toRadians(spd_dir-90))*skatingRange);
			
			if ( timer_rightTurn <= 0 )
			{
				if (timer_rightTurn <= -200) { timer_rightTurn = 120; }
				Game.createGameObject(new o_particle_track(create_x1,create_y1,functions.clamp(getSpd(),0,spd_max)/spd_max*traceSize));
			} timer_rightTurn--;
			
			if ( timer_leftTurn <= 0 )
			{
				if (timer_leftTurn <= -200) { timer_leftTurn = 120; }
				Game.createGameObject(new o_particle_track(create_x2,create_y2,functions.clamp(getSpd(),0,spd_max)/spd_max*traceSize));
			} timer_leftTurn--;
			
			timer_particleGenerate_track = 0;
		}
		else
		{
			timer_particleGenerate_track--;
		}
	}
	
	public void setLastHitter ( GameObject lastHitter ) { this.lastHitter = lastHitter; }
	public GameObject getLastHitter () { return lastHitter; }
	public void setPlayerName( String playerName ) { this.playerName = playerName; }
	public String getPlayerName () { return playerName; }
	public double getStamina() { return stamina; }
	public double getStaminaMax() { return stamina_max; }

	public void setAngle(int angle) { shooting_angle = angle;}
	public double getAngle() { return shooting_angle; }

	public void bloodControl() { if ( blood <= 0 ) { delete(); } if ( blood >= blood_max ) { blood = blood_max; }}
	
	public void staminaControl()
	{
		if ( timer_staminaHealCoolTime <= 0 ) {stamina += stamina_heal; }
		else { timer_staminaHealCoolTime --; }
		
		if ( stamina <= 0 ){ stamina = 0; }
		if ( stamina >= stamina_max ) { stamina = stamina_max;}
	}
}
