package MainGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Scanner;

public class o_stuff extends GameObject
{
	public static String NAME = "o_stuff";
	
	private float alpha = 1f;
	
	private sys_Sprite own_sprite = null;
	private int own_printAdjust_x = 0;
	private int own_printAdjust_y = 0;
	private String own_print = null;
	private Color own_printColor = Color.white;
	private Color own_printIDcolor = new Color(145,54,54);
	private int own_printMaxCapacityLine = 19;
	private double ani_frame = 0;
	private double ani_spd = 0;
	private int ani_frameNum = 0;
	private int ani_imageIndex = 0;
	private boolean isMessenger = false;

	
	public o_stuff( double x, double y, sys_Sprite sprite)
	{
		super(NAME, x, y);
		own_sprite = sprite;
	}
	public o_stuff( double x, double y, String print)
	{
		super(NAME, x, y);
		own_print = print;
	}


	@Override
	public void step()
	{
		if ( own_sprite != null )
		{
			ani_frame = (ani_frame+ani_spd)%ani_frameNum;
			ani_imageIndex = (int)functions.clamp(ani_frame,0,own_sprite.getImageNum()-1);
		}
	}

	@Override
	public void draw(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		if ( own_sprite != null )
		{
			g2d.setComposite(functions.makeTransparent(alpha));
			
			g.drawImage( own_sprite.getImage((int)functions.clamp(ani_imageIndex,0,own_sprite.getImageNum()-1)),
					(int)x - own_sprite.getCenterX(),
					(int)y - own_sprite.getCenterY(),
					null);
			g2d.setComposite(functions.makeTransparent(1f));
		}
		
		if ( own_print != null )
		{
			g2d.setComposite(functions.makeTransparent(alpha));
			g.setColor(own_printColor);
			
			Scanner s = new Scanner(own_print);
			int lc = 0;
			int il = 0;
			while ( s.hasNextLine()) { lc++; s.nextLine(); } s.close();
			il = lc-own_printMaxCapacityLine;
			if ( il > 0 )
			{
				String tempPrint = "";
				int lineCount = 0;
				s = new Scanner(own_print);
				while ( s.hasNextLine())
				{
					String tempLine = s.nextLine();
					if ( lineCount >= il ) { tempPrint += tempLine+"\n"; }
					lineCount++;
				}
				
				own_print = tempPrint;
				s.close();
			}
			
			
			s = new Scanner(own_print);
			int lineGap = 12;
			int lineCount = 0;
			
			while ( s.hasNextLine())
			{
				String line = s.nextLine();
				Scanner is = new Scanner(line);
				int wordCount = 0;
				int wordNum = 0;

				boolean colorChange = false;
				while ( is.hasNext())
				{
					String word = is.next();
					if ( wordCount == 0 && getIsMessenger() )
					{
						wordCount ++;
						String wordTemp = word.substring(0, 6);
						String colorPart = "#"+word.substring(6, 12);
						if ( wordTemp.equals("MT_SM-"))
						{
							own_printIDcolor = Color.decode(colorPart);
							g.setColor(new Color(204,181,78));
							colorChange = true; continue;
						}
						if ( wordTemp.equals("MT_CM-"))
						{
							own_printIDcolor = Color.decode(colorPart);
							g.setColor(own_printIDcolor);
							continue;
						}
					}
					g.drawString(" " + word,(int)x+wordCount + own_printAdjust_x,(int)y+lineGap*lineCount + own_printAdjust_y);
					
					if ( colorChange )
					{
						if ( wordNum == 0 ) { g.setColor(own_printIDcolor); }
						else { g.setColor(new Color(204,181,78)); }
					}
					else { g.setColor(own_printColor); }
					
					wordCount += g.getFontMetrics().stringWidth((word+" "))+2;
					wordNum++;
				}
				lineCount++;
				is.close();
			}
			s.close();
			
			g2d.setComposite(functions.makeTransparent(1f));
		}
	}

	@Override
	public void destroy() {}
	
	public void setSprite ( sys_Sprite own_sprite )
	{
		this.own_sprite = own_sprite;
		ani_frameNum = this.own_sprite.getImageNum();
	}
	public sys_Sprite getSprite () { return own_sprite; }
	
	public void setString ( String own_print )
	{
		this.own_print = own_print;
	}
	public String getString() { return own_print; }
	
	public void setAniSpd ( double ani_spd ) { this.ani_spd = ani_spd; }
	public void setAniFrame ( double ani_frame ) { this.ani_frame = ani_frame; }
	
	public void setAlpha ( float alpha ) { this.alpha = alpha; }
	public float getAlpha () { return alpha; }
	public void setStringColor(Color color) { own_printColor = color; }
	public void setIDcolor(Color own_printIDcolor) { this.own_printIDcolor  = own_printIDcolor; }
	
	public void setIsMessenger ( boolean isMessenger ) { this.isMessenger = isMessenger; }
	public boolean getIsMessenger() { return isMessenger; }
	
	public void setPrintAdjust_x ( int x ) { own_printAdjust_x = x; }
	public int getPrintAdjust_x() { return own_printAdjust_x; }
	
	public void setPrintAdjust_y ( int y ) { own_printAdjust_y = y; }
	public int getPrintAdjust_y() { return own_printAdjust_y; }
}
