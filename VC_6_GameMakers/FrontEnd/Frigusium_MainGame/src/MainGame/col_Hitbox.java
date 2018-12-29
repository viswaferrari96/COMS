package MainGame;

import java.awt.Rectangle;

public class col_Hitbox
{
	private int x;
	private int y;
	private int width;
	private int height;
	private GameObject mom = null;
	
	public col_Hitbox ( int x, int y, int width, int height, GameObject mom )
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.mom = mom;
	}
	
	public Rectangle getHitBox()
	{
		return new Rectangle(x-width/2,y-height/2,width,height);
	}
	
	public void setX ( int x ) { this.x = x; }
	public void setY ( int y ) { this.y = y; }
	public void setWidth ( int width ) { this.width = width; }
	public void setHeight ( int height ) { this.height = height; }
	public void setMom( GameObject mom ) { this.mom = mom; }
	
	public int getX () { return x; }
	public int getY () { return y; }
	public int getWidth () { return width; }
	public int getHeight () { return height; }
	public GameObject getMom() { return mom; }
}
