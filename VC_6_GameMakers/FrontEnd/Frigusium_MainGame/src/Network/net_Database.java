package Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

import MainGame.Game;

public class net_Database
{
	public static String serverIP = "localhost";
	//public static String serverIP = "proj309-vc-06.misc.iastate.edu";
	
	public static String getServerIP() { return serverIP; }
	
	public static String dataGet( String URLaddress ){ return databaseGet("players/" + URLaddress); }
	public static void dataSet( String URLaddress ){ databaseSet("players/" + URLaddress); }

	public static String dataServerGet( String URLaddress ){ return databaseGet("userServers/" + URLaddress); }
	public static void dataServerSet( String URLaddress ){ databaseSet("userServers/" + URLaddress); }
	
	public static void deleteServer( int serverID )
	{
		String servername = net_Database.dataServerGet("searchById/getServername/"+serverID);
		String playerListString = dataServerGet("searchByServername/getAllUsers/print/"+servername);
		ArrayList<String> playerList = new ArrayList<>();
		Scanner s = new Scanner(playerListString);
		while ( s.hasNext() ) { playerList.add(s.next().trim()); } s.close();
		
		for ( int i = 0; i < playerList.size(); i ++ )
		{
			dataSet("searchByUsername/setJoinedServerID/"+playerList.get(i)+"/-1");
		}

		dataServerSet("deleteById/"+Game.HostingServerID);
	}
	private static String databaseGet( String URLaddress )
	{
		try
		{
			URL u = new URL("http://"+serverIP+":8080/" + URLaddress);
			URLConnection uc = u.openConnection();
			BufferedReader br = new BufferedReader( new InputStreamReader(uc.getInputStream()));
			
			String dataGet = br.readLine();
			String dataAdd = br.readLine();
			while ( dataAdd!= null )
			{
				dataGet += dataAdd;
				dataAdd = br.readLine();
			}
			
			return dataGet;
		}
		catch (MalformedURLException e) {}
		catch (IOException e) {}
		
		return null;
	}
	private static void databaseSet( String URLaddressUpdate )
	{
		try
		{
			URLConnection urlConnection;
			BufferedReader bufferReader;
			
			String URLaddress = "http://"+serverIP+":8080/" + URLaddressUpdate;
			
			urlConnection = (new URL(URLaddress)).openConnection();
			bufferReader = new BufferedReader( new InputStreamReader(urlConnection.getInputStream()));
			bufferReader.readLine();
		}
		catch (MalformedURLException e) {}
		catch (IOException e) {}
	}
}
