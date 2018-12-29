package MainGame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class sys_KeyInput extends KeyAdapter
{
	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();
		if ( key == KeyEvent.VK_W ) { Game.setKey("w",true); }
		if ( key == KeyEvent.VK_A ) { Game.setKey("a",true); }
		if ( key == KeyEvent.VK_S ) { Game.setKey("s",true); }
		if ( key == KeyEvent.VK_D ) { Game.setKey("d",true); }
		if ( key == KeyEvent.VK_SPACE ) { Game.setKey("space",true); }
		if ( key == KeyEvent.VK_RIGHT ) { Game.setKey("right",true); }
		if ( key == KeyEvent.VK_LEFT ) { Game.setKey("left",true); }
		if ( key == KeyEvent.VK_DOWN ) { Game.setKey("down",true); }
		if ( key == KeyEvent.VK_UP ) { Game.setKey("up",true); }
		if ( key == KeyEvent.VK_ESCAPE ) { Game.setKey("esc",true); }
	}

	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		if ( key == KeyEvent.VK_W ) { Game.setKey("w",false); }
		if ( key == KeyEvent.VK_A ) { Game.setKey("a",false); }
		if ( key == KeyEvent.VK_S ) { Game.setKey("s",false); }
		if ( key == KeyEvent.VK_D ) { Game.setKey("d",false); }
		if ( key == KeyEvent.VK_SPACE ) { Game.setKey("space",false); }
		if ( key == KeyEvent.VK_RIGHT ) { Game.setKey("right",false); }
		if ( key == KeyEvent.VK_LEFT ) { Game.setKey("left",false); }
		if ( key == KeyEvent.VK_DOWN ) { Game.setKey("down",false); }
		if ( key == KeyEvent.VK_UP ) { Game.setKey("up",false); }
		if ( key == KeyEvent.VK_ESCAPE ) { Game.setKey("esc",false); }
	}
}
