package Packet;

import java.util.ArrayList;
import java.util.Scanner;

import Network.net_Client;
import Network.net_Server;
import Network.net_UserGameInfo;

public class Packet04PlayerGlobalUpdate extends Packet
{
	ArrayList<net_UserGameInfo> list;
	public Packet04PlayerGlobalUpdate( byte[] list  )
	{
		super(04);
		this.list = stringToArrayList(readData(list));
	}

	public Packet04PlayerGlobalUpdate( ArrayList<net_UserGameInfo> list )
	{
		super(04);
		this.list = list;
	}

	@Override
	public byte[] getData()
	{
		String toReturn = "04";
		
		for ( int i = 0; i < list.size(); i++ )
		{
			toReturn += list.get(i).getX() + " ";
			toReturn += list.get(i).getY() + " ";
			toReturn += list.get(i).getAngle() + " ";
			toReturn += list.get(i).getBlood() + " ";
			toReturn += list.get(i).getSerialNumber() + "\n";
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
	
	public ArrayList<net_UserGameInfo> getPlayerList() { return list; }
	
	public ArrayList<net_UserGameInfo> stringToArrayList( String list )
	{
		ArrayList<net_UserGameInfo> toReturn = new ArrayList<net_UserGameInfo>();
		
		Scanner reader = new Scanner(list);
		
		while ( reader.hasNext() )
		{
			String x = reader.next().trim();
			String y = reader.next().trim();
			String angle = reader.next().trim();
			String blood = reader.next().trim();
			String serialNumber = reader.next().trim();
			
			try
			{
				toReturn.add( new net_UserGameInfo(Integer.parseInt(x),
											 Integer.parseInt(y),
											 Integer.parseInt(angle),
											 Integer.parseInt(blood),
											 Integer.parseInt(serialNumber)));
			}
			catch (NumberFormatException e) {e.printStackTrace();}
		}
		reader.close();
		
		return toReturn;
	}
}

