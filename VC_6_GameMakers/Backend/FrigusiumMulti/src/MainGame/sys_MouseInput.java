package MainGame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class sys_MouseInput implements MouseListener
{

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if ( e.getButton() == 1) { Game.MOUSE_CLICK_LEFT = true; }
		if ( e.getButton() == 3) { Game.MOUSE_CLICK_RIGHT = true; }
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if ( e.getButton() == 1) { Game.MOUSE_CLICK_LEFT = false; }
		if ( e.getButton() == 3) { Game.MOUSE_CLICK_RIGHT = false; }
	}
}
