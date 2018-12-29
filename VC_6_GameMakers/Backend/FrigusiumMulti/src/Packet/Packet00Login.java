package Packet;

import Network.net_Client;
import Network.net_Server;

public class Packet00Login extends Packet
{
	private String username;
	
	public Packet00Login( byte[] username )
	{
		super(00);
		this.username = readData(username);
	}

	public Packet00Login( String username )
	{
		super(00);
		this.username = username;
	}

	@Override
	public byte[] getData() { return ("00" + username).getBytes(); }

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
}
