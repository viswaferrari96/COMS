package MainGame;

import java.net.InetAddress;

import Network.net_Client;

public class o_player extends o_controllerable
{
	private net_Client client;
	private InetAddress ip;
	private int port;
	
	private double x_pre;
	private double y_pre;
	
	private int stopTime = 0;
	private int connectionTime = 0;
	
	public o_player(double x, double y, String playerName, InetAddress ip, int port)
	{
		super(x, y, playerName);
		
		this.client = Game.getClient();
		this.ip = ip;
		this.port = port;
		x_pre = x;
		y_pre = y;
	}
	
	@Override
	public void step()
	{
		super.step();
		client.playerUpdate();
	}
	
	@Override
	public void movementControl()
	{
		if ( Game.PLAYER == this )
		{
			super.movementControl();
		}
		else
		{
			connectionTime ++;
			
			double x_difference = x - x_pre;
			double y_difference = y - y_pre;
			
			spd_x = x_difference/connectionTime;
			spd_y = y_difference/connectionTime;

			double spd_preCorrection = getSpd();
			if ( spd_preCorrection > spd_max)
			{
				spd_x *= spd_max/spd_preCorrection;
				spd_y *= spd_max/spd_preCorrection;
			}
			
			if ( functions.getDistance(0, 0, x_difference, y_difference) > 0 )
			{
				spd_dir = functions.angleCorrect(functions.getDirection(0, 0, x_difference, y_difference));
				spd = getSpd();
				stopTime = 0;
			}
			else { stopTime++; }
			
			if ( stopTime > 12 ) { spd = 0; }
			
			spd_dirFinal = spd_dir;
			
			if ( connectionTime == 1 )
			{
				x_pre = x;
				y_pre = y;
			}
			
			x += spd_x;
			y += spd_y;
		}
	}
	
	@Override
	public void bulletControl(int fire_x,int fire_y,double dmg,double spd,double angle,double lifeTime,int height)
	{
		if ( Game.PLAYER == this )
		{
			client.bulletUpdate(fire_x, fire_y, dmg, spd, angle, lifeTime, height);
		}
	}
	
	public void setConnectionTime ( int connectionTime ) { this.connectionTime = connectionTime; }
	public void setMouseX ( int mouse_x ) { this.mouse_x = mouse_x; }
	public void setMouseY ( int mouse_y ) { this.mouse_y = mouse_y; }
	
	public InetAddress getIp() { return ip; }
	public int getPort() { return port; }
}