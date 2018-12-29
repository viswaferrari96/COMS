package MainGame;

import java.awt.Graphics;
import java.util.Random;

public abstract class GameObject
{
	protected String objectName;
	
	protected Random r = new Random();
	
	protected double x;
	protected double y;
	protected double spd = 0;
	protected double spd_x = 0;
	protected double spd_y = 0;
	protected double blood;
	protected double blood_max;
	
	private GameObject mom = null;
	private col_Hitbox own_hitbox = null;
	
	private int layer = 0;
	private int depth = 0;
	private boolean isDeleted = false;
	
	public GameObject ( String NAME, double x, double y)
	{
		this.x = x;
		this.y = y;
		this.objectName = NAME;
	}

	public GameObject ( String NAME, double x, double y, GameObject mom )
	{
		this.x = x;
		this.y = y;
		this.objectName = NAME;
		this.mom = mom;
	}
	
	public abstract void step ();
	public abstract void draw ( Graphics g );
	public abstract void destroy();

	public void delete() { destroy(); isDeleted = true; Game.deleteGameObject(this); }
	
	public boolean getDeleted() { return isDeleted; }
	
	public int getLayer() { return layer; }
	public int getDepth() { return depth; }
	public double getBlood() { return blood; }
	public double getBloodMax() { return blood_max; }
	public double getX() { return x; }
	public double getY() { return y; }
	public double getSpd() { return Math.sqrt(spd_x*spd_x + spd_y*spd_y); }
	public double getSpdX() { return spd_x; }
	public double getSpdY() { return spd_y; }
	public String getName() { return objectName; }
	public GameObject getMom() { return mom; }
	public col_Hitbox getHitbox() { return own_hitbox; };
	
	public void setLayer( int layer ) { this.layer = layer; }
	public void setDepth( int depth ) { this.depth = depth; }
	public void setBlood( double blood ) { this.blood = blood; }
	public void setBloodMax( double blood_max ) { this.blood_max = blood_max; }
	public void setX( double x ) { this.x = x; }
	public void setY( double y ) { this.y = y; }
	public void setSpd( double spd ) { this.spd = spd; }
	public void setpdX( double spdx ) { this.spd_x = spdx; }
	public void setSpdY( double spdy) { this.spd_y = spdy; }
	public void setMom( GameObject mom ) { this.mom = mom; }
	public void setHitbox( col_Hitbox own_hitbox) { this.own_hitbox = own_hitbox; };
	
	public void updateHitbox() { if ( own_hitbox != null ) { own_hitbox.setX((int)x); own_hitbox.setY((int)y); }}


	public int compareTo(GameObject o)
	{
		if ( o.getLayer() > getLayer() ) { return -1; }
		else if ( o.getLayer() < getLayer() ) { return 1; }
		else
		{
			if ( o.getDepth() > getDepth() ) { return -1; }
			else if ( o.getDepth() < getDepth() ) { return 1; }
			else { return 0; }
		}
	}
}

