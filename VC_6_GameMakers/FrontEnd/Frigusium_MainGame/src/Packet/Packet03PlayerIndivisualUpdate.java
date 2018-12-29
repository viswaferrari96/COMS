package Packet;

import java.util.Scanner;

import Network.net_Client;
import Network.net_Server;

public class Packet03PlayerIndivisualUpdate extends Packet
{
	private int x;
	private int y;
	private int angle;
	private int blood;
	private int serialNumber;
	
	public Packet03PlayerIndivisualUpdate( byte[] playerUpdateInfo  )
	{
		super(03);
		setCoordinates(readData(playerUpdateInfo));
	}

	public Packet03PlayerIndivisualUpdate( int x, int y, int angle, int blood, int serialNumber )
	{
		super(03);
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.blood = blood;
		this.serialNumber = serialNumber;
	}

	@Override
	public byte[] getData()
	{
		String toReturn = "03";
		toReturn += x + " ";
		toReturn += y + " ";
		toReturn += angle + " ";
		toReturn += blood + " ";
		toReturn += serialNumber + " ";
		
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

	public int getX() { return x; }
	public int getY() { return y; }
	public int getAngle() { return angle; }
	public int getBlood() { return blood; }
	public int getSerialNumber() { return serialNumber; }
	
	public void setCoordinates( String coordinate )
	{
		Scanner reader = new Scanner(coordinate);
		x = Integer.parseInt(reader.next().trim());
		y = Integer.parseInt(reader.next().trim());
		angle = Integer.parseInt(reader.next().trim());
		blood = Integer.parseInt(reader.next().trim());
		serialNumber = Integer.parseInt(reader.next().trim());
		reader.close();
	}
}

