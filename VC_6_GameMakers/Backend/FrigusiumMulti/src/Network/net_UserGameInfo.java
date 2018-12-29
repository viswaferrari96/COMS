package Network;

public class net_UserGameInfo
{
	private int x;
	private int y;
	private int angle;
	private int blood;
	private int serialNumber;

	public net_UserGameInfo ( int x, int y, int angle, int blood, int serialNumber)
	{
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.blood = blood;
		this.serialNumber = serialNumber;
	}

	public void setX( int x ) { this.x = x; }
	public void setY( int y ) { this.y = y; }
	public void setAngle( int angle ) { this.angle = angle; }
	public void setBlood( int blood ) { this.blood = blood; }
	
	public int getX() { return x; }
	public int getY() { return y; }
	public int getAngle() { return angle; }
	public int getBlood() { return blood; }
	
	public int getSerialNumber() { return serialNumber; }
}
