package MainGame;

import java.awt.Color;
import java.awt.Graphics;

public class o_particle_ember extends GameObject
{
	private static String NAME = "o_particle_ember";
	
	private double spd = r.nextInt(70)/100. + 0.03;
	private double angle = r.nextInt(360);
	private double angleChange = r.nextInt(10)-5;
	
	private double size = 3;
	private double time = 0;
	
	private int bufferNum = 5;
	private double[] buffer_x = new double[bufferNum];
	private double[] buffer_y = new double[bufferNum];
	
	private double correctX = 0;
	private double correctY = 0;
	
	private boolean colorModified = false;
	private int color_r = 255;
	private int color_g = 255;
	private int color_b = 255;
	
	public o_particle_ember( double x, double y )
	{
		super( NAME, x, y);
	}

	public o_particle_ember( double x, double y, double angle )
	{
		super( NAME, x, y);
		this.angle = angle;
	}
	
	public o_particle_ember( double x, double y, double angle, Color color, double correctX, double correctY, double spdRate )
	{
		super( NAME, x, y);
		this.angle = angle;
		this.correctX = correctX;
		this.correctY = correctY;
		this.spd*= spdRate;
		
		this.colorModified = true;
		this.color_r = color.getRed();
		this.color_g = color.getGreen();
		this.color_b = color.getBlue();
	}
	
	public void create()
	{
		spd_x = 0;
		spd_y = 0;
		

		for ( int i = 0; i < bufferNum; i++ )
		{
			buffer_x[i] = x;
			buffer_y[i] = y;
		}
	}

	public void step()
	{
		time ++;
		size -= 0.01;
		move();
		
		for ( int i = 0; i < bufferNum; i++ )
		{
			if ( i == bufferNum-1 ) { buffer_x[i] = x; buffer_y[i] = y; }
			else
			{
				buffer_x[i] = buffer_x[i+1];
				buffer_y[i] = buffer_y[i+1];
			}
		}
		
		if ( size <= 1 ) { delete(); }
	}

	public void draw(Graphics g)
	{
		Color color;
		if ( colorModified )
		{
			color =new Color((int)functions.clamp(color_r-time,0,255),
								   (int)functions.clamp(color_g-time,0,255),
								   (int)functions.clamp(color_b-time,0,255));
		}
		else
		{
			color =new Color((int)functions.clamp(255-time,0,255),
								   (int)functions.clamp(255-time*3,0,255),
								   (int)functions.clamp(255-time*6,0,255));
		}

		g.setColor(color);
		for ( int i = 0; i < bufferNum; i++ )
		{
			g.fillRect((int)(buffer_x[i]-size/2 + correctX*(bufferNum-i)),(int)(buffer_y[i]-size/2 + correctY*(bufferNum-i)),(int)size,(int)size);
		}
		
	}

	public void destroy()
	{
	}
	
	
	private void move()
	{
		spd_x = Math.cos(Math.toRadians(angle))*spd + correctX;
		spd_y =-Math.sin(Math.toRadians(angle))*spd + correctY;
		
		angle += angleChange;
		
		angleChange = functions.clamp(angleChange+((r.nextInt(100)-50)/100.),-4,4);
		
		x += spd_x;
		y += spd_y;
	}
}
