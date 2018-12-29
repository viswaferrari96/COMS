package Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import MainGame.Game;
import MainGame.GameObject;
import MainGame.o_bullet;
import MainGame.o_player;
import Packet.Packet;
import Packet.Packet.PacketTypes;
import Packet.Packet02PlayerSetUp;
import Packet.Packet03PlayerIndivisualUpdate;
import Packet.Packet04PlayerGlobalUpdate;
import Packet.Packet05BulletUpdate;

public class net_Client extends Thread
{
	private InetAddress ip;
	private DatagramSocket socket;
	private ArrayList<net_UserConnectionInfo> localPlayerConnectionInfoList = new ArrayList<net_UserConnectionInfo>();
	private ArrayList<net_UserGameInfo> localPlayerGameInfoList = new ArrayList<net_UserGameInfo>();
	private ArrayList<GameObject> playerObjectList = new ArrayList<GameObject>();
	private int serialNumber;
	
	public net_Client ( Game game, String ip )
	{
		try
		{
			this.ip = InetAddress.getByName(ip);
			this.socket = new DatagramSocket();
		}
		catch (UnknownHostException e) { e.printStackTrace(); }
		catch (SocketException e) { e.printStackTrace(); }
	}
	
	@Override
	public void run()
	{
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
				break;
				
			case DISCONNECT :
				break;
				
			case PLAYERSETUP :
				Packet02PlayerSetUp packet = new Packet02PlayerSetUp(data);
				ArrayList<net_UserConnectionInfo> playerList = packet.getPlayerList();
				
				if ( playerList.size() != localPlayerConnectionInfoList.size())
				{
					for ( int i = 0; i < playerList.size(); i ++ )
					{
						net_UserConnectionInfo cur = playerList.get(i);
						
						if ( i >= localPlayerConnectionInfoList.size())
						{
							localPlayerConnectionInfoList.add(cur);
							GameObject player = new o_player(cur.getX(),cur.getY(),cur.getUsername(),cur.getIp(),cur.getPort());
							Game.createGameObject(player);
							playerObjectList.add(player);
							
							if ( cur.getUsername().equals(Game.username) )
							{
								Game.PLAYER = player;
								this.serialNumber = cur.getSerialNumber();
							}
						}
					}
				}
				
				break;
				
			case PLAYER_INDIVISUAL_UPDATE :
				break;
			
			case PLAYER_GLOBAL_UPDATE :
				Packet04PlayerGlobalUpdate GameInfopacket = new Packet04PlayerGlobalUpdate(data);
				ArrayList<net_UserGameInfo> playerGameInfoList = GameInfopacket.getPlayerList();
				
				for ( int i = 0; i < playerGameInfoList.size(); i ++ )
				{
					net_UserGameInfo cur = playerGameInfoList.get(i);
					
					GameObject curPlayer = playerObjectList.get(i);
					
					if ( curPlayer != Game.PLAYER )
					{
						curPlayer.setX(cur.getX());
						curPlayer.setY(cur.getY());
						curPlayer.setBlood(cur.getBlood());
						((o_player) curPlayer).setAngle(cur.getAngle());
						((o_player) curPlayer).setConnectionTime(0);
					}
				}
				
				break;
				
			case BULLET_UPDATE :
				Packet05BulletUpdate bulletPacket = new Packet05BulletUpdate(data);

				int fire_x = bulletPacket.getFire_x();
				int fire_y = bulletPacket.getFire_y();
				double dmg = bulletPacket.getDmg();
				double spd = bulletPacket.getSpd();
				double angle = bulletPacket.getAngle();
				double lifeTime = bulletPacket.getLifeTime();
				int height = bulletPacket.getHeight();
				int serialNumber = bulletPacket.getSerialNumber();

				GameObject owner = null;
				for ( int i = 0; i < localPlayerGameInfoList.size(); i ++ )
				{
					net_UserGameInfo playerInfoCur = localPlayerGameInfoList.get(i);
					if ( playerInfoCur.getSerialNumber() == serialNumber )
					{
						owner = playerObjectList.get(i);
					}
				}
				
				o_bullet own_bullet = (o_bullet) Game.createGameObject(new o_bullet(fire_x,fire_y,owner,60));
				own_bullet.setDmg(dmg);
				own_bullet.setSpd(spd);
				own_bullet.setAngle(angle);
				own_bullet.setLifeTime(lifeTime);
				own_bullet.setHeight(height);
				
				break;
		}
	}
	
	public void sendData( byte[] data )
	{
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, 2223);
		
		try { socket.send(packet); }
		catch (IOException e) { e.printStackTrace(); }
	}
	
	public void playerUpdate()
	{
		o_player player = (o_player)Game.PLAYER;
		Packet03PlayerIndivisualUpdate updatePacket = new Packet03PlayerIndivisualUpdate((int)player.getX(),
																						 (int)player.getY(),
																						 (int)player.getAngle(),
																						 (int)player.getBlood(),
																						 serialNumber);
		sendData(updatePacket.getData());
	}
	
	public void bulletUpdate(int fire_x,int fire_y,double dmg,double spd,double angle,double lifeTime,int height)
	{
		Packet05BulletUpdate bulletPacket = new Packet05BulletUpdate(fire_x,fire_y,dmg,spd,angle,lifeTime,height,serialNumber);
		sendData(bulletPacket.getData());
	}
}
