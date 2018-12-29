package MainGame;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;

public class sys_UI
{
	private boolean UI_switch = true;
	
	private float UI_alpha = 0.f;
	
	private double blood = 0;
	private double blood_max = 0;
	private double stamina = 0;
	private double stamina_max = 0;
	
	private double colorRatioCur_blood = 1;
	private int colorTimer_blood = 0;

	private double textPlayerY = 0;
	private double textPublicY = 0;
	
	private LinkedList<String> textPlayer = new LinkedList<String>();
	private LinkedList<String> textPublic = new LinkedList<String>();
	
	public void initialize()
	{
		textPlayerY = 0;
		textPublicY = 0;
		
		textPlayer.clear();
		textPublic.clear();
	}
	
	public void step()
	{
		if ( UI_switch )
		{
			if ( Game.PLAYER != null)
			{
				this.blood = Game.PLAYER.getBlood();
				this.blood_max = Game.PLAYER.getBloodMax();
				this.stamina = ((o_controllerable) Game.PLAYER).getStamina();
				this.stamina_max = ((o_controllerable) Game.PLAYER).getStaminaMax();
			}
			else { blood = 0; }
			
			if ( colorTimer_blood <= 0 ) { colorRatioCur_blood = blood/blood_max; colorTimer_blood = (int)(120*(blood/blood_max)); }
			else { colorTimer_blood--; }
			
			colorRatioCur_blood += (1 - colorRatioCur_blood)/10;
			textPlayerY += (0-textPlayerY)/15;
			textPublicY += (0-textPublicY)/15;
			
			if ( UI_alpha < 1.f ) { UI_alpha += 0.02f; }
			if ( UI_alpha >= 1.f ) { UI_alpha = 1.f; }
		}
		else
		{
			if ( UI_alpha > 0.f ) { UI_alpha -= 0.02f; }
			if ( UI_alpha <= 0.f ) { UI_alpha = 0.f; }
		}
	}
	
	public void draw ( Graphics g )
	{ 
		Graphics2D g2d = (Graphics2D) g;
		
		// Health
		
		g2d.setComposite(functions.makeTransparent(0.4f*UI_alpha));
		g.setColor(Color.black);
		g.fillRect(49, 29, (int)blood_max+2, 6);
		
		g2d.setComposite(functions.makeTransparent(0.6f*UI_alpha));
		int colorCom = (int) functions.clamp((255*colorRatioCur_blood),0,255);
		g.setColor(new Color(255, colorCom,colorCom));
		g.fillRect(50, 30, (int)blood_max, 4);
		
		g2d.setComposite(functions.makeTransparent(1f*UI_alpha));
		colorCom = (int) functions.clamp((255*(blood/blood_max)),0,255);
		g.setColor(new Color(255, colorCom,colorCom));
		g.fillRect(50, 30, (int)blood, 4);
		g.setColor(Color.white);
		g.drawLine(50, 29, 50, 35); g.drawLine(50 + (int)(functions.clamp(blood,0,blood_max))-1, 29, 50 + (int)(functions.clamp(blood,0,blood_max))-1, 35);
		
		

		//Stamina
		g2d.setComposite(functions.makeTransparent(0.4f*UI_alpha));
		g.setColor(Color.black);
		g.fillRect(49, 35, (int)stamina_max+2, 4);

		g2d.setComposite(functions.makeTransparent(0.6f*UI_alpha));
		g.setColor(Color.white);
		g.fillRect(50, 36, (int)stamina_max, 2);
		
		g2d.setComposite(functions.makeTransparent(1f*UI_alpha));
		colorCom = (int) functions.clamp((255*(stamina/stamina_max)),0,255);
		g.setColor(new Color(255, colorCom,colorCom));
		g.fillRect(50, 36, (int)stamina, 2);
		g.setColor(Color.white);
		g.drawLine(50, 36, 50, 38); g.drawLine(50 + (int)stamina-1, 35, 50 + (int)stamina-1, 38);
		
		
		

		g2d.setComposite(functions.makeTransparent(1f*UI_alpha));
		g.setColor(Color.black);
		FontMetrics metrics = g.getFontMetrics();
		for ( int i = textPlayer.size()-1; i >= 0 ; i--)
		{
			g.setColor(new Color(150,150,150));
			g.drawString(textPlayer.get(i),(int)Game.WIDTH/2 - metrics.stringWidth(textPlayer.get(i))/2, Game.HEIGHT - 80 + (textPlayer.size()-i)*16+ (int)textPlayerY);
		
			if ( i == textPlayer.size()-1 )
			{
				g.setColor(Color.white);
				g.drawString(textPlayer.get(i),(int)Game.WIDTH/2 - metrics.stringWidth(textPlayer.get(i))/2, Game.HEIGHT - 81 + (textPlayer.size()-i)*16+ (int)textPlayerY);
			}
		}

		for ( int i = textPublic.size()-1; i >= 0 ; i--)
		{
			g.setColor(Color.white);
			g.drawString(textPublic.get(i),(int)Game.WIDTH - metrics.stringWidth(textPublic.get(i)) - 10, 30 + (i)*16 + (int)textPublicY );
		}
		
		g.setColor(Color.white);
		g.drawString( Game.getPlayerNum() + " / " + Game.getPlayerNumOriginal(), Game.WIDTH-60, 18);
	}
	
	public void addPlayerText ( String toAdd )
	{
		textPlayerY -= 16;
		textPlayer.add(toAdd);
		while ( textPlayer.size() > 3 )
		{
			textPlayer.removeFirst();
		}
	}

	public void addPublicText ( String toAdd )
	{
		textPublicY += 16;
		textPublic.add(toAdd);
		if ( textPublic.size() > 5 )
		{
			textPublic.removeFirst();
		}
	}
	
	public void turnUIon() { UI_switch = true; }
	public void turnUIoff() { UI_switch = false; }
	public boolean getUI() { return UI_switch; }
}
