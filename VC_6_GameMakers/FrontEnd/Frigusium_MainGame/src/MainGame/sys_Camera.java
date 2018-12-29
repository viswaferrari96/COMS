package MainGame;

public class sys_Camera
{
	private double x,y;
	private GameObject follow = null;
	private int WIDTH;
	private int HEIGHT;
	
	public sys_Camera(double x, double y, int WIDTH, int HEIGHT )
	{
		this.x = x;
		this.y = y;
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
	}

	public sys_Camera(GameObject target, double x, double y, int WIDTH, int HEIGHT )
	{
		this.x = - x + WIDTH/2;
		this.y = - y + HEIGHT/2;
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		follow = target;
		step();
	}
	
	public void step()
	{
		if ( follow != null )
		{
			double goal_x = follow.getX() + (Game.MOUSE_X - follow.getX())/4;
			double goal_y = follow.getY() + ((Game.MOUSE_Y - follow.getY())/4)*(16/9.);
			
			double camera_goal_x = - goal_x + WIDTH/2;
			double camera_goal_y = - goal_y + HEIGHT/2;
			
			this.x += (camera_goal_x-x)/20;	
			this.y += (camera_goal_y-y)/20;
		}
	}
	
	public void setTarget( GameObject target )
	{
		this.follow = target;
	}
	
	public void setX( double x ){ this.x = x; }
	public void setY( double y ){ this.y = y; }
	public double getX(){ return x; }
	public double getY(){ return y; }
}
