package MainGame;

import java.awt.image.BufferedImage;

public class sys_Sprite
{
	private BufferedImage spriteSheet;
	private BufferedImage[] image;
	private int width;
	private int height;
	private int centerX;
	private int centerY;
	
	public sys_Sprite(BufferedImage spriteSheet, int row, int col, int width, int height, int centerX, int centerY )
	{
		this.spriteSheet = spriteSheet;
		this.image = new BufferedImage[col*row];
		this.width = width;
		this.height = height;
		this.centerX = centerX;
		this.centerY = centerY;
		
		int imageNum = 0;
		for ( int n_col = 0; n_col < col; n_col++ )
		{
			for ( int n_row = 0; n_row < row; n_row++ )
			{
				image[imageNum] = grabImage(n_col,n_row,width,height);
				imageNum++;
			}
		}
	}

	public sys_Sprite(BufferedImage spriteSheet, int centerX, int centerY )
	{
		this.spriteSheet = spriteSheet;
		this.image = new BufferedImage[1];
		this.width = spriteSheet.getWidth();
		this.height = spriteSheet.getHeight();
		this.centerX = centerX;
		this.centerY = centerY;
		
		int imageNum = 0;
		image[imageNum] = spriteSheet;
		imageNum++;
	}
	
	public BufferedImage getImage ( int n ) { return image[n]; }
	public int getImageNum () { return image.length; }
	public int getWidth () { return width; }
	public int getHeight () { return height; }
	public int getCenterX () { return centerX; }
	public int getCenterY () { return centerY; }
	
	private BufferedImage grabImage( int col, int row, int width, int height )
	{
		BufferedImage img = spriteSheet.getSubimage(row*width, col*height, width, height);
		return img;
	}
}
