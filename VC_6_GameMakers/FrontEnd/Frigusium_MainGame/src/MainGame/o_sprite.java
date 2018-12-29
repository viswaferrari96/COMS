package MainGame;

import java.awt.Graphics;

public class o_sprite extends GameObject
{
	public static String NAME = "o_sprite";
	
	private sys_Sprite own_sprite = null;
	private int imageIndex = 0;

	
	public o_sprite( double x, double y, sys_Sprite sprite)
	{
		super(NAME, x, y);
		own_sprite = sprite;
	}

	@Override
	public void step() {}

	@Override
	public void draw(Graphics g)
	{
		if ( own_sprite != null &&
			 Math.abs(-Game.CAMERA.getX() - getX() + Game.WIDTH/2) < Game.WIDTH/2 + 64&&
			 Math.abs(-Game.CAMERA.getY() - getY() + Game.HEIGHT/2) < Game.HEIGHT/2 +64)
		{
			g.drawImage( own_sprite.getImage(imageIndex), (int)x - own_sprite.getCenterX(), (int)y - own_sprite.getCenterY(),null);
		}
	}

	@Override
	public void destroy() {}
	
	public void setSprite ( sys_Sprite own_sprite ) { this.own_sprite = own_sprite; }
	public sys_Sprite getSprite () { return own_sprite; }
	
	public void setImageIndex ( int imageIndex ) { this.imageIndex = imageIndex; }
	public int getImageIndex () { return imageIndex; }
}
