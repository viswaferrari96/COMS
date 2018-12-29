package MainGame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import MainGame.GameObject;

public class o_button extends GameObject
{

	private static String NAME = "o_b_button";
	private sys_Sprite own_sprite = null;
	private int buttonWidth = 0;
	private int buttonHeight = 0;
	private boolean mouseTouched = false;
	private boolean pressed = false;
	private boolean pressing = false;
	private boolean released = false;
	
	public o_button(double x, double y, sys_Sprite sprite)
	{
		super(NAME, x, y);
		own_sprite = sprite;
		buttonWidth = own_sprite.getWidth();
		buttonHeight = own_sprite.getHeight();
	}

	@Override
	public void step()
	{
		if ( Game.MOUSE_X < x + buttonWidth/2 && Game.MOUSE_X > x - buttonWidth/2 &&
			 Game.MOUSE_Y < y + buttonHeight/2 && Game.MOUSE_Y > y - buttonHeight/2 )
		{
			mouseTouched = true;
		}
		else
		{
			mouseTouched = false;
		}
		
		if ( mouseTouched )
		{
			pressing = Game.MOUSE_CLICK_LEFT;
			pressed = Game.MOUSE_CLICK_LEFT_pressed;
			released = Game.MOUSE_CLICK_LEFT_released;
		}
		else
		{
			pressing = false;
			pressed = false;
			released = false;
		}
	}

	@Override
	public void draw(Graphics g)
	{
		int draw_x = (int)x - own_sprite.getWidth()/2;
		int draw_y = (int)y - own_sprite.getHeight()/2;
		
		BufferedImage normalImage = own_sprite.getImage(0);
		BufferedImage touchedImage = own_sprite.getImage(1);
		BufferedImage pressedImage = own_sprite.getImage(2);
		
		if ( pressing ) { g.drawImage(pressedImage,draw_x,draw_y,null);}
		else if ( mouseTouched ) { g.drawImage(touchedImage,draw_x,draw_y,null);}
		else { g.drawImage(normalImage,draw_x,draw_y,null);} 
	}

	@Override
	public void destroy()
	{
	}
	
	public boolean getPressed() { return pressed; }
	public boolean getPressing() { return pressing; }
	public boolean getReleased() { return released; }
}
