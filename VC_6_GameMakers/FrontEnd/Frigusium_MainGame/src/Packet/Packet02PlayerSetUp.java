package Packet;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import Network.net_Client;
import Network.net_Server;
import Network.net_UserConnectionInfo;

public class Packet02PlayerSetUp extends Packet
{
	ArrayList<net_UserConnectionInfo> list;
	public Packet02PlayerSetUp( byte[] list  )
	{
		super(02);
		this.list = stringToArrayList(readData(list));
	}

	public Packet02PlayerSetUp( ArrayList<net_UserConnectionInfo> list )
	{
		super(02);
		this.list = list;
	}

	@Override
	public byte[] getData()
	{
		String toReturn = "02";
		
		for ( int i = 0; i < list.size(); i++ )
		{
			toReturn += list.get(i).getX() + " ";
			toReturn += list.get(i).getY() + " ";
			toReturn += list.get(i).getUsername() + " !@#$UNE ";
			toReturn += list.get(i).getIp().toString() + " ";
			toReturn += list.get(i).getPort() + " ";
			toReturn += list.get(i).getSerialNumber() + "\n";
			System.out.println( "ran" );
		}
		return toReturn.getBytes();
	}

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
	
	public ArrayList<net_UserConnectionInfo> getPlayerList() { return list; }
	
	public ArrayList<net_UserConnectionInfo> stringToArrayList( String list )
	{
		ArrayList<net_UserConnectionInfo> toReturn = new ArrayList<net_UserConnectionInfo>();
		
		Scanner reader = new Scanner(list);
		
		while ( reader.hasNext() )
		{
			String x = reader.next().trim();
			String y = reader.next().trim();
			
			String username = "";
			String usernameTemp = "";
			while ( !usernameTemp.equalsIgnoreCase("!@#$UNE"))
			{
				username += " " + usernameTemp;
				usernameTemp = reader.next().trim();
			}
			username = username.trim();
			
			String ip = reader.next().substring(1);
			String port = reader.next().trim();
			String serialNumber = reader.next().trim();
			
			//System.out.println(x + " " + y + " " + username + " " + ip + " " + port + " " + serialNumber);
			
			try
			{
				toReturn.add( new net_UserConnectionInfo(Integer.parseInt(x),
											 Integer.parseInt(y),		
											 username,
											 InetAddress.getByName(ip),
											 Integer.parseInt(port),
											 Integer.parseInt(serialNumber)));
			}
			
			catch (NumberFormatException e) {e.printStackTrace();}
			catch (UnknownHostException e) {e.printStackTrace();}
		}
		reader.close();
		
		return toReturn;
	}
}