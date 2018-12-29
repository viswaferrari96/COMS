package Network;

import java.net.InetAddress;

public class net_UserConnectionInfo
{
	private String username;
	private InetAddress ip;
	private int port;
	private int x;
	private int y;
	private int serialNumber;

	public net_UserConnectionInfo ( int x, int y, String username, InetAddress ip, int port, int serialNumber)
	{
		this.x = x;
		this.y = y;
		this.username = username;
		this.ip = ip;
		this.port = port;
		this.serialNumber = serialNumber;
	}

	public void setX( int x ) { this.x = x; }
	public void setY( int y ) { this.y = y; }
	public int getX() { return x; }
	public int getY() { return y; }
	public String getUsername() { return username; }
	public InetAddress getIp() { return ip; }
	public int getPort() { return port; }
	public int getSerialNumber() { return serialNumber; }
}
