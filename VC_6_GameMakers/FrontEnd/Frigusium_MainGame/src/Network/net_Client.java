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
	private boolean run = false;
	private InetAddress ip;
	private DatagramSocket socket;
	private ArrayList<net_UserConnectionInfo> localPlayerConnectionInfoList = new ArrayList<net_UserConnectionInfo>();
	private ArrayList<net_UserGameInfo> localPlayerGameInfoList = new ArrayList<net_UserGameInfo>();
	private ArrayList<GameObject> playerObjectList = new ArrayList<GameObject>();
	private int serialNumber;
	
	private int savex = 0;
	private int savey = 0;
	
	public net_Client ( String ip )
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
		run = true;
		while (run)
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
		try { socket.close(); join(); } catch (InterruptedException e) {}
	}
	
	public synchronized void kill () { run = false; }
	
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
				
				for ( int i = 0; i < playerList.size(); i ++ )
				{
					net_UserConnectionInfo cur = playerList.get(i);
					
					boolean found = false;
					for ( int j = 0; j < localPlayerConnectionInfoList.size(); j++ )
					{
						if ( localPlayerConnectionInfoList.get(j).getUsername().equals(cur.getUsername()))
						{
							found = true;
							break;
						}
					}
					
					if ( !found )
					{
						localPlayerConnectionInfoList.add(cur);
						localPlayerGameInfoList.add(new net_UserGameInfo(i, i, i, i, i));
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
				
				break;
				
			case PLAYER_INDIVISUAL_UPDATE :
				break;
			
			case PLAYER_GLOBAL_UPDATE :
				Packet04PlayerGlobalUpdate GameInfopacket = new Packet04PlayerGlobalUpdate(data);
				localPlayerGameInfoList = GameInfopacket.getPlayerList();
				
				for ( int i = 0; i < localPlayerGameInfoList.size(); i ++ )
				{
					net_UserGameInfo cur = localPlayerGameInfoList.get(i);
					
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
				int serial = bulletPacket.getSerialNumber();

				GameObject owner = null;
				for ( int i = 0; i < localPlayerGameInfoList.size(); i ++ )
				{
					net_UserGameInfo playerInfoCur = localPlayerGameInfoList.get(i);
					if ( playerInfoCur.getSerialNumber() == serial)
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
		if ( player != null )
		{
			Packet03PlayerIndivisualUpdate updatePacket = new Packet03PlayerIndivisualUpdate((int)player.getX(),
																							 (int)player.getY(),
																							 (int)player.getAngle(),
																							 (int)player.getBlood(),
																							 serialNumber);
			
			savex = (int)player.getX();
			savey = (int)player.getY();
			
			sendData(updatePacket.getData());
		}
		else
		{
			Packet03PlayerIndivisualUpdate updatePacket = new Packet03PlayerIndivisualUpdate(savex,savey,0,0,serialNumber);
			sendData(updatePacket.getData());
		}
	}
	
	public void bulletUpdate(int fire_x,int fire_y,double dmg,double spd,double angle,double lifeTime,int height)
	{
		Packet05BulletUpdate bulletPacket = new Packet05BulletUpdate(fire_x,fire_y,dmg,spd,angle,lifeTime,height,serialNumber);
		sendData(bulletPacket.getData());
	}
	
	public ArrayList<GameObject> getPlayerList ()
	{
		return playerObjectList;
	}
}
