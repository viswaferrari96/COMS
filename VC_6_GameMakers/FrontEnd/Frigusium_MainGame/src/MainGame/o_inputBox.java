package MainGame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class o_inputBox extends GameObject implements KeyListener
{
	private Game mom;
	
	private int wordNumLimit = -1;
	
	private String basicMessage = "";
	private String typedMessage = "";
	private String errorMessage = "";
	
	private boolean entered = false;
	private boolean clicked = false;
	private boolean error = false;
	
	private static String NAME = "o_inputBox";
	private sys_Sprite own_sprite = null;
	private int buttonWidth = 0;
	private int buttonHeight = 0;
	
	private boolean mouseTouched = false;
	private boolean released = false;
	
	public o_inputBox(double x, double y, Game mom, sys_Sprite sprite)
	{
		super(NAME, x, y);
		
		this.mom = mom;
		mom.addKeyListener(this);
		
		own_sprite = sprite;
		buttonWidth = own_sprite.getWidth();
		buttonHeight = own_sprite.getHeight();
	}

	public o_inputBox(double x, double y, Game mom, sys_Sprite sprite, String basicMessage, String errorMessage, int wordNumLimit)
	{
		super(NAME, x, y);

		this.mom = mom;
		mom.addKeyListener(this);
		
		own_sprite = sprite;
		buttonWidth = own_sprite.getWidth();
		buttonHeight = own_sprite.getHeight();
		
		this.wordNumLimit = wordNumLimit;
		this.basicMessage = basicMessage;
		this.errorMessage = errorMessage;
	}
	
	@Override
	public void step()
	{
		if ( clicked )
		{
		}
		
		if ( Game.MOUSE_X < x + buttonWidth/2 && Game.MOUSE_X > x - buttonWidth/2 &&
			 Game.MOUSE_Y < y + buttonHeight/2 && Game.MOUSE_Y > y - buttonHeight/2 )
		{
			mouseTouched = true;
		}
		else
		{
			mouseTouched = false;
		}
		
		released = Game.MOUSE_CLICK_LEFT_released;
		
		if ( mouseTouched && released ){clicked = true;}
		if (!mouseTouched && released ){clicked = false;}
	}

	@Override
	public void draw(Graphics g)
	{
		//Box Display
		int draw_x = (int)x - own_sprite.getWidth()/2;
		int draw_y = (int)y - own_sprite.getHeight()/2;
		
		BufferedImage unclickedImage = own_sprite.getImage(0);
		BufferedImage clickedImage = own_sprite.getImage(1);
		BufferedImage unclickedErrorImage = own_sprite.getImage(2);
		BufferedImage clickedErrorImage = own_sprite.getImage(3);
		
		if ( clicked )
		{
			if ( error )
			{
				g.drawImage(clickedErrorImage,draw_x,draw_y,null);
			}
			else
			{
				g.drawImage(clickedImage,draw_x,draw_y,null);
			}
		}
		else
		{
			if ( error )
			{
				g.drawImage(unclickedErrorImage,draw_x,draw_y,null);
			}
			else
			{
				g.drawImage(unclickedImage,draw_x,draw_y,null);
			}
		}
		
		//Text Display
		
		g.setColor(new Color(145,52,52));
		if ( error ) { g.drawString(errorMessage,(int)(x-buttonWidth/2),(int)(y+buttonHeight/2+5)); }
		
		if ( !clicked ) { g.setColor(new Color(145,145,145));}
		else { g.setColor(new Color(0,0,0));}
		
		if ( typedMessage.length() > 0 )
		{
			g.drawString(typedMessage,(int)(x-buttonWidth/2 + 5),(int)(y + 2));
		}
		else if ( !clicked )
		{
			g.drawString(basicMessage,(int)(x-buttonWidth/2 + 5),(int)(y + 2));
		}
	}

	@Override
	public void destroy() 
	{
		mom.removeKeyListener(this);
	}
	
	public boolean entered() { return clicked && entered; }
	public void setEntered( boolean entered ) { this.entered = entered; }
	
	public void setString( String message ) { typedMessage = message; }
	public String getString() { return typedMessage; }
	
	public boolean clicked() { return clicked; }
	
	public void setError( boolean error ) { this.error = error; }
	public boolean getError() { return error; }
	
	public void setWordNumLimit( int wordNumLimit ) { this.wordNumLimit = wordNumLimit; }
	public int getWordNumLimit() { return wordNumLimit; }
	
	public void setBasicMessage( String basicMessage ) { this.basicMessage = basicMessage; }
	public String getBasicMessage () { return basicMessage; }
	
	public void setErrorMessage( String errorMessage ) { this.errorMessage = errorMessage; }
	public String getErrorMessage () { return errorMessage; }
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		if ( clicked )
		{
			if ( e.getKeyCode() == 10 )
			{
				entered = true;
			}
			if ( e.getKeyCode() == 8 )
			{
				typedMessage = typedMessage.substring(0, (int)functions.clamp(typedMessage.length()-1,0,wordNumLimit));
			}
			else if ( e.getKeyCode() > 31 && typedMessage.length() < wordNumLimit)
			{
				typedMessage += e.getKeyChar();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if ( clicked )
		{
			if ( e.getKeyCode() == 10 )
			{
				entered = false;
			}
		}
		else
		{
			entered = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}
}
