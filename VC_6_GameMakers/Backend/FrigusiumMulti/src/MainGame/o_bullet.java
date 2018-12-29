package MainGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class o_bullet extends GameObject
{
	private static String NAME = "o_bullet";
	
	private double dmg = 0;
	
	private double angle;
	private double lifeTime;
	private double lifeTime_original;
	
	private int drawTailNum = 0;
	private double x_pre;
	private double y_pre;
	
	private int height = 15;
	
	private boolean state_delete = false;
	
	public o_bullet(double x, double y, GameObject mom, double lifeTime) 
	{
		super(NAME, x, y, mom);
		this.lifeTime = lifeTime;
		this.lifeTime_original = lifeTime;
		x_pre = x;
		y_pre = y;
		setLayer(1);
	}

	public void step()
	{
		if ( !state_delete ) { movementControl();}
	}

	public void draw(Graphics g)
	{
		int size = 4;
		double xmove = Math.cos(Math.toRadians(angle));
		double ymove =-Math.sin(Math.toRadians(angle));
		
		double x_temp = x;
		double y_temp = y-height;
		
		for ( int i = 0; i < drawTailNum; i ++)
		{
			int sizeFinal = size;
			Color color = new Color((int)functions.clamp(255-((double)i/drawTailNum)*20,0,255),
								    (int)functions.clamp(225-((double)i/drawTailNum)*80,0,255),
								    (int)functions.clamp(185-((double)i/drawTailNum)*160,0,255));

			g.setColor(color);
			g.fillOval((int)(x_temp),(int)(y_temp),(int)sizeFinal,(int)sizeFinal);
			x_temp -= xmove;
			y_temp -= ymove;
		}
		
		x_pre = x;
		y_pre = y;
		
		if ( state_delete ) { delete(); }
	}

	public void destroy()
	{
	}
	
	
	public void movementControl()
	{
		double spd_final = spd*(lifeTime/lifeTime_original);
		int moved = 0;
		
		while ( moved+1 < spd_final )
		{
			sys_CustomLL<col_Hitbox> list = Game.OBJECT_MANAGER.getHitboxList();

			sys_CustomLL<col_Hitbox>.Node<col_Hitbox> cur = list.getFirstNode();
			while ( cur != null )
			{
				GameObject examine = cur.getData().getMom();
				if ( examine != null && examine != getMom() )//&& examine != Game.PLAYER)
				{
					if ( examine.getHitbox().getHitBox().intersects(getBounds()))
					{
						state_delete = true;
						examine.setBlood(examine.getBlood()-dmg);
						if ( examine.getName().equals("o_controllerable"))
						{
							o_controllerable playable = (o_controllerable)examine;
							playable.setLastHitter(getMom());
							
							if ( playable == Game.PLAYER )
							{
								int shooting_recoil_x = (int) (Math.cos(Math.toRadians(angle))*dmg);
								int shooting_recoil_y =-(int) (Math.sin(Math.toRadians(angle))*dmg);
								Game.CAMERA.setX(Game.CAMERA.getX() + shooting_recoil_x);
								Game.CAMERA.setY(Game.CAMERA.getY() + shooting_recoil_y);
							}
						}
						Game.createGameObject(new o_effect_hitmark(x,y-height));
						break;
					}
				}
				cur = cur.getNextNode();
			}
			
			if ( state_delete ) { break; }
			else
			{
				x += Math.cos(Math.toRadians(angle));
				y -= Math.sin(Math.toRadians(angle));
				moved++;
			}
		}
		
		if ( !state_delete )
		{
			x += Math.cos(Math.toRadians(angle))*(spd_final-moved);
			y -= Math.sin(Math.toRadians(angle))*(spd_final-moved);
			
			lifeTime --;
			
			if ( lifeTime <= 0 ) { state_delete = true;}
		}
		
		drawTailNum = (int) functions.getDistance(x, y, x_pre, y_pre);
		
	}
	
	public void setLifeTime ( double lifeTime ) { this.lifeTime = lifeTime; }
	public void setAngle ( double angle ) { this.angle = angle; }
	public void setDmg ( double dmg ) { this.dmg = dmg; }
	public void setHeight ( int height ) { this.height = height; }
	
	public Rectangle getBounds()
	{
		int size = 4;
		return new Rectangle((int)x-size/2,(int)y-size/2,size,size);
	}
}
