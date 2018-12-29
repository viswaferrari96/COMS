package MainGame;
import java.awt.Color;
import java.awt.Graphics;

public class o_effect_hitmark extends GameObject
{
	private static String NAME = "o_effect_hitmark";
	private int LifeTime = 30;
	public o_effect_hitmark( double x, double y )
	{
		super(NAME, x, y);
		setLayer(2);
	}

	@Override
	public void step()
	{
		if ( LifeTime <= 0 ) { delete(); }
		LifeTime--;
	}

	@Override
	public void draw(Graphics g)
	{
		g.setColor(Color.red);
		for ( int ix = -1 ; ix < 1; ix ++ )
		{
			for ( int iy = -1; iy < 1; iy ++ )
			{
				g.drawLine((int)x+ix,(int)y+iy-4,(int)x+ix+4,(int)y+iy);
				g.drawLine((int)x+ix,(int)y+iy-4,(int)x+ix-4,(int)y+iy);
				g.drawLine((int)x+ix,(int)y+iy+4,(int)x+ix+4,(int)y+iy);
				g.drawLine((int)x+ix,(int)y+iy+4,(int)x+ix-4,(int)y+iy);
			}
		}
	}

	@Override
	public void destroy()
	{
	}

}
