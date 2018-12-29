package Packet;

import java.util.Scanner;

import Network.net_Client;
import Network.net_Server;

public class Packet05BulletUpdate extends Packet
{
	int fire_x;
	int fire_y;
	double dmg;
	double spd;
	double angle;
	double lifeTime;
	int height;
	int serialNumber;
	
	public Packet05BulletUpdate( byte[] list  )
	{
		super(05);
		decode(readData(list));
	}

	public Packet05BulletUpdate(int fire_x,int fire_y,double dmg,double spd,double angle,double lifeTime,int height, int serialNumber)
	{
		super(05);
		this.fire_x = fire_x;
		this.fire_y = fire_y;
		this.dmg = dmg;
		this.spd = spd;
		this.angle = angle;
		this.lifeTime = lifeTime;
		this.height = height;
		this.serialNumber = serialNumber;
	}

	@Override
	public byte[] getData()
	{
		String toReturn = "05";
		
		toReturn += fire_x + " ";
		toReturn += fire_y + " ";
		toReturn += (float)dmg + " ";
		toReturn += (float)spd + " ";
		toReturn += (float)angle + " ";
		toReturn += (float)lifeTime + " ";
		toReturn += height + " ";
		toReturn += serialNumber + "\n";
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


	public int getFire_x() { return fire_x; }
	public int getFire_y() { return fire_y; }
	public double getDmg() { return dmg; }
	public double getSpd() { return spd; }
	public double getAngle() { return angle; }
	public double getLifeTime() { return lifeTime; }
	public int getHeight() { return height; }
	public int getSerialNumber() { return serialNumber; }
	
	public void decode( String info )
	{
		Scanner reader = new Scanner(info);
		
		fire_x = Integer.parseInt(reader.next().trim());
		fire_y = Integer.parseInt(reader.next().trim());
		dmg = Float.parseFloat(reader.next().trim());
		spd = Float.parseFloat(reader.next().trim());
		angle = Float.parseFloat(reader.next().trim());
		lifeTime = Float.parseFloat(reader.next().trim());
		height = Integer.parseInt(reader.next().trim());
		serialNumber = Integer.parseInt(reader.next().trim());
		
		reader.close();
	}
}
