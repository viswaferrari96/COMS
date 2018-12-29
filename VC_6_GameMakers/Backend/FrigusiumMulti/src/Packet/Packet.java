package Packet;

import Network.net_Client;
import Network.net_Server;

public abstract class Packet
{
	public static enum PacketTypes
	{
		INVALID(-1), 
		LOGIN(00), 
		DISCONNECT(01), 
		PLAYERSETUP(02), 
		PLAYER_INDIVISUAL_UPDATE(03), 
		PLAYER_GLOBAL_UPDATE(04),
		BULLET_UPDATE(05);
		
		private int packetId;
		
		private PacketTypes(int packetId)
		{
			this.packetId = packetId;
		}
		
		public int getId()
		{
			return packetId;
		}
	}
	
	public byte packetId;
	
	public Packet( int packetId )
	{
		this.packetId = (byte) packetId;
	}
	
	public abstract byte[] getData();
	
	public abstract void writeData( net_Client client );
	
	public abstract void writeData( net_Server server );
	
	public String readData( byte[] data )
	{
		String message = new String(data).trim();
		return message.substring(2);
	}

	public static PacketTypes lookupPacket( String packetId )
	{
		try { return lookupPacket (Integer.parseInt(packetId)); }
		catch ( NumberFormatException e )
		{
			return PacketTypes.INVALID;
		}
	}
	
	public static PacketTypes lookupPacket(int id )
	{
		for ( PacketTypes p : PacketTypes.values())
		{
			if ( p.getId() == id ) return p;
		}
		
		return PacketTypes.INVALID;
	}
}
