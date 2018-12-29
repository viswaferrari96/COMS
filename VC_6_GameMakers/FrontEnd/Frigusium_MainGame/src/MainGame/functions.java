package MainGame;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class functions
{
	public static double clamp ( double val, double min, double max )
	{
		if ( val < min ) { return min; }
		else if ( val > max ) { return max; }
		else { return val; }
	}
	
	public static double getDistance ( double x1, double y1, double x2, double y2 )
	{
		return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
	}
	
	public static double getDirection ( double x1, double y1, double x2, double y2 )
	{
		return Math.toDegrees(Math.atan2(-(y2-y1),(x2-x1)));
	}
	
	public static double angleMoving ( double angle_original, double angle_goal, double spd )
	{
		angle_original = angleCorrect(angle_original);
		angle_goal = angleCorrect(angle_goal);
		
		double angle_difference = angle_goal-angle_original;
		
		if ( angle_difference > 180 ) { return (angle_difference - 360)/spd; }
		else if ( angle_difference <=-180 ) { return (angle_difference + 360)/spd; }
		return angle_difference/spd;
	}
	
	public static double angleCorrect ( double angle )
	{
		if ( angle >= 360 ) { return angle - 360; }
		if ( angle < 0 ) { return angle + 360; }
		return angle;
	}
	
	public static double angleRealDifference ( double angle1, double angle2 )
	{
		double angle = Math.abs(angleCorrect(angle1)-angleCorrect(angle2));
		if ( angle >= 180 ) { return 360 - angle; }
		return angle;
	}

	public static AlphaComposite makeTransparent ( float alpha ) 
	{
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type,alpha));
	}

	public static BufferedImage modifyImage( BufferedImage img, double angle , double xscale, double yscale)
	{
		int canvasSize = (int) Math.sqrt(img.getWidth()*img.getWidth() + img.getHeight()*img.getHeight());
		BufferedImage toReturn = new BufferedImage(canvasSize,canvasSize,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D)toReturn.getGraphics();
		g2.rotate(Math.toRadians(-angle),canvasSize/2,canvasSize/2);
		g2.scale(xscale,yscale);
		int drawPointx = toReturn.getWidth()/2 - img.getWidth()/2;
		int drawPointy = toReturn.getHeight()/2 - img.getHeight()/2;
		g2.drawImage(img,drawPointx,drawPointy,null);
		
		return toReturn;
	}
	public static GameObject closestKind(String name, GameObject itself)
	{
		sys_CustomLL<GameObject> list = Game.OBJECT_MANAGER.getObjectList();
		sys_CustomLL<GameObject>.Node<GameObject> cur = list.getFirstNode();
		
		GameObject toReturn = null;
		double shortest = -1;
		while ( cur != null )
		{
			GameObject examine = cur.getData();
			if ( examine != null && examine != itself && examine.getName().equals(name))
			{
				double temp = getDistance(itself.getX(),itself.getY(),examine.getX(),examine.getY());
				if ( temp <= shortest || shortest == -1)
				{
					toReturn = examine;
					shortest = temp;
				}
			}
			cur = cur.getNextNode();
		}
		return toReturn;
	}
	
	public static boolean terrainCollisionCheck( int x, int y )
	{
		int ix = coordToIndex(x);
		int iy = coordToIndex(y);
		
		int curIndice = Game.MAP[iy][ix];
		
		if ( curIndice == 16 || curIndice < 0 ) { return false; }
		return true;
	}
	
	public static int coordToIndex ( int n )
	{
		int toReturn = n/32 + Game.MAP.length/2;
		if ( n < 0 ) { toReturn --; }
		return toReturn;
	}
}
