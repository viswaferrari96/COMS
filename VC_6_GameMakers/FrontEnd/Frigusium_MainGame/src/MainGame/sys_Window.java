package MainGame;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

import Network.net_Database;

public class sys_Window extends Canvas
{
	private static final long serialVersionUID = -6199435956418397009L;
	private JFrame frame;
	private boolean fullScreen = false;
	
	public sys_Window ( int width, int height, String title, Game game)
	{
		width = (int)(width*Game.SCREENSCALE);
		height = (int)(height*Game.SCREENSCALE);
		
		frame = new JFrame(title);
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.setPreferredSize( new Dimension(width,height));
		frame.setMaximumSize( new Dimension(width,height));
		frame.setMinimumSize( new Dimension(width,height));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(game);
		frame.setVisible(true);
		
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//fullScreen = true;
	}
	
	public boolean isFullScreen() { return fullScreen; }
	public void close()
	{
		if ( Game.HostingServerID != -1)
		{
			net_Database.deleteServer(Game.HostingServerID);
		}
	
		if ( Game.gameClient != null ) { Game.gameClient.kill(); }
		if ( Game.gameServer != null ) { Game.gameServer.kill(); }
		
		net_Database.dataSet("searchByUsername/setJoinedServerID/"+Game.username+"/-1");
		net_Database.dataSet("searchByUsername/setIPaddress/"+Game.username+"/notConnected");
		net_Database.dataSet("searchByUsername/setIPaddress/"+Game.username+"/notConnected");
		frame.dispose();
		System.exit(0);
	}
	public int getWindowX() { return frame.getX(); }
	public int getWindowY() { return frame.getY(); }
}
