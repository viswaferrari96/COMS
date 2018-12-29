package MainGame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class o_gun extends GameObject
{
	private static String NAME = "o_gun";
	
	private sys_Sprite sprite;
	private double angle;
	private int distanceFromMomX;
	private int distanceFromMomY;
	private int height = 10;
	
	public o_gun(GameObject mom, int distanceFromMomX, int distanceFromMomY)
	{
		super(NAME, mom.getX(), mom.getY(),mom);
		this.distanceFromMomX = distanceFromMomX;
		this.distanceFromMomY = distanceFromMomY;
		setLayer(1);
	}

	@Override
	public void step()
	{
		if ( getMom() != null )
		{
			x = getMom().getX();
			y = getMom().getY();
		}
	}

	@Override
	public void draw(Graphics g)
	{
		// Drawing the Gun
		BufferedImage gun = functions.modifyImage(sprite.getImage(0), angle,1,1);
		
		int gun_adjustX = sprite.getWidth()/2 - sprite.getCenterX();
		int gun_adjustY = sprite.getHeight()/2 - sprite.getCenterY();
		
		int gun_x = (int) (x + Math.cos(Math.toRadians(angle))*(distanceFromMomX+gun_adjustX)
							 + Math.cos(Math.toRadians(angle-90))*(distanceFromMomY+gun_adjustY)) - gun.getWidth()/2;
		int gun_y = (int) (y - Math.sin(Math.toRadians(angle))*(distanceFromMomX+gun_adjustX)
							 - Math.sin(Math.toRadians(angle-90))*(distanceFromMomY+gun_adjustY)) - gun.getHeight()/2;
		
		g.drawImage(gun,(int)gun_x,(int)gun_y-height,gun.getWidth(),gun.getHeight(),null);
		
	}

	public void setSprite ( sys_Sprite sprite ) { this.sprite = sprite; }
	public void setAngle ( double angle ) { this.angle = angle; }
	
	public int getHeight () { return height; }
	
	
	@Override
	public void destroy() {}
}
