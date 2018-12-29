package Packet;

import java.util.Scanner;

import Network.net_Client;
import Network.net_Server;

public class Packet00Login extends Packet
{
	private String username;
	private int create_x;
	private int create_y;
	
	public Packet00Login( byte[] data )
	{
		super(00);
		String dataRead = readData(data);
		Scanner s = new Scanner(dataRead);
		
		username = s.next();
		create_x = s.nextInt();
		create_y = s.nextInt();
	}

	public Packet00Login( String username, int create_x, int create_y )
	{
		super(00);
		this.username = username;
		this.create_x = create_x;
		this.create_y = create_y;
	}

	@Override
	public byte[] getData() { return ("00" + username + " " + create_x + " " + create_y).getBytes(); }

	@Override
	public void writeData(net_Client client)
	{
		client.sendData(getData());
	}

	@Override
	public void writeData(net_Server server)
	{
		server.sendDataToAllClients(getData());
	}
	
	public String getUsername ()
	{
		return username;
	}

	public int getCreateX() { return create_x; }
	public int getCreateY() { return create_y; }
}
