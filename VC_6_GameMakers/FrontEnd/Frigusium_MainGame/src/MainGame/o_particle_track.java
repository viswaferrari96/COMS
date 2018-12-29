package MainGame;

import java.awt.Color;
import java.awt.Graphics;

public class o_particle_track extends GameObject
{
	private static String NAME = "o_particle_track";
	
	private double size;
	private int time = 0;
	
	public o_particle_track( double x, double y, double size )
	{
		super( NAME, x, y);
		this.size = size;
		
		spd_x = 0;
		spd_y = 0;
	}

	public void step()
	{
		time ++;
		size -= 0.02;
		if ( size <= 0 ) { delete(); }
	}

	public void draw(Graphics g)
	{
		Color color =new Color((int)functions.clamp(255-time,0,255),
							   (int)functions.clamp(255-time*3,0,255),
							   (int)functions.clamp(255-time*6,0,255));

		g.setColor(color);
		g.fillOval((int)(x-size/2),(int)(y-size/2),(int)size,(int)size);
	}

	public void destroy()
	{
	}
}
