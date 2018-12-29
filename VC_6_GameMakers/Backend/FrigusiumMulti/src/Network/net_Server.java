package Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import Packet.Packet;
import Packet.Packet.PacketTypes;
import Packet.Packet00Login;
import Packet.Packet02PlayerSetUp;
import Packet.Packet03PlayerIndivisualUpdate;
import Packet.Packet04PlayerGlobalUpdate;
import Packet.Packet05BulletUpdate;

public class net_Server extends Thread
{
	private DatagramSocket socket;
	private ArrayList<net_UserConnectionInfo> playerConnectionInfoList = new ArrayList<net_UserConnectionInfo>();
	private ArrayList<net_UserGameInfo> playerGameInfoList = new ArrayList<net_UserGameInfo>();
	private int serialNumber = 0;
	
	public net_Server ()
	{
		try
		{
			this.socket = new DatagramSocket(2223);
		}
		catch (SocketException e) { e.printStackTrace(); }
	}
	
	@Override
	public void run()
	{
		System.out.println("----------Server ON----------");
		while (true)
		{
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data,data.length);
			
			try
			{
				socket.receive(packet);
			}
			catch (IOException e) { e.printStackTrace(); }
			
			packetSort(packet.getData(),packet.getAddress(),packet.getPort());
			playersUpdate();
		}
	}
	
	private void packetSort(byte[] data, InetAddress address, int port)
	{
		String message = new String(data).trim();
		PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
		
		switch (type)
		{
			default :
				
			case INVALID :
				break;
				
			case LOGIN :
				Packet00Login packet = new Packet00Login(data);
				System.out.println("[ IP : "+address.getHostAddress()+"	Port:"+port + "]	" + packet.getUsername() + " has joined the server");
				
				net_UserConnectionInfo playerConnectionInfo = new net_UserConnectionInfo((int)(Math.random()*300-150),(int)(Math.random()*300-150),packet.getUsername(),address,port,serialNumber);
				net_UserGameInfo playerGameInfo = new net_UserGameInfo(playerConnectionInfo.getX(), playerConnectionInfo.getY(),0,200,playerConnectionInfo.getSerialNumber());
				playerConnectionInfoList.add (playerConnectionInfo);
				playerGameInfoList.add(playerGameInfo);
				serialNumber++;
				
				Packet02PlayerSetUp playerConnectionPacket = new Packet02PlayerSetUp(playerConnectionInfoList);
				sendDataToAllClients(playerConnectionPacket.getData());
				
				break;
				
			case DISCONNECT :
				break;
				
			case PLAYER_INDIVISUAL_UPDATE :
				Packet03PlayerIndivisualUpdate updatePacket = new Packet03PlayerIndivisualUpdate(data);
				int serialCur = updatePacket.getSerialNumber();
				
				for ( int i = 0; i < playerGameInfoList.size(); i ++ )
				{
					net_UserGameInfo playerInfoCur = playerGameInfoList.get(i);
					if ( playerInfoCur.getSerialNumber() == serialCur )
					{
						playerInfoCur.setX(updatePacket.getX());
						playerInfoCur.setY(updatePacket.getY());
						playerInfoCur.setBlood(updatePacket.getBlood());
						playerInfoCur.setAngle(updatePacket.getAngle());
					}
				}
				break;
				
			case PLAYER_GLOBAL_UPDATE :
				break;

			case BULLET_UPDATE :
				sendDataToAllClients(data);
				break;
		}
	}

	public void sendData( byte[] data, InetAddress ip, int port )
	{
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		
		try { socket.send(packet); }
		catch (IOException e) { e.printStackTrace(); }
	}
	
	public void sendDataToAllClients(byte[] data)
	{
		for ( net_UserConnectionInfo client : playerConnectionInfoList )
		{
			sendData( data, client.getIp(), client.getPort());
		}
	}
	
	public void playersUpdate ()
	{
		Packet04PlayerGlobalUpdate playerGameInfo = new Packet04PlayerGlobalUpdate(playerGameInfoList);
		sendDataToAllClients(playerGameInfo.getData());
	}
	
	public static void main ( String[] args )
	{
		(new net_Server()).start();
	}
}