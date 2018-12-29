package MainGame;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class sys_MapGenerator
{
	private int mapScale;
	private int mapWidth;
	private int mapHeight;
	private double landRatio;
	private int[][] map;
	
	private int floor = 16;
	
	public sys_MapGenerator( int mapScale, double landRatio )
	{
		this.mapScale = mapScale;
		this.landRatio = landRatio;
		mapGenerate();
	}

	public sys_MapGenerator( int[][] map )
	{
		mapScale = map.length;
		mapWidth = mapScale;
		mapHeight = mapScale;
		landRatio = 1;
		this.map = map;
	}
	
	public void mapGenerate()
	{
		map = new int[mapScale][mapScale];
		
		mapWidth = map[0].length;
		mapHeight = map.length;
		
		for ( int iy = 0; iy < mapHeight; iy++ )
		{
			for ( int ix = 0; ix < mapWidth; ix++ )
			{
				map[iy][ix] = -1;
			}
		}
		
		
		
		// Sets the start point
		int startCoord_x = mapWidth/2;
		int startCoord_y = mapHeight/2;
		
		int coordCur_x = startCoord_x;
		int coordCur_y = startCoord_y;
		
		int landSize = 0;
		int mapSize = map.length * map[0].length;
		
		// Map generation
		while ( true )
		{
			if ( map[coordCur_y][coordCur_x] != floor ) { landSize++; }
			map[coordCur_y][coordCur_x] = floor;
			
			if ( (double)landSize/mapSize > landRatio ) { break; }
			
			int sideChoose = (int)(Math.random()*4);
			
			if ( sideChoose == 0 ) { coordCur_x++; }
			if ( sideChoose == 1 ) { coordCur_x--; }
			if ( sideChoose == 2 ) { coordCur_y++; }
			if ( sideChoose == 3 ) { coordCur_y--; }
			
			if ( coordCur_x < 2 || coordCur_x+2 >= mapWidth || coordCur_y < 2 || coordCur_y+2 >= mapHeight )
			{
				coordCur_x = startCoord_x;
				coordCur_y = startCoord_y;
			}
		}
		
		// Wall Generation
		for ( int iy = 0; iy < mapHeight; iy++ )
		{
			for ( int ix = 0; ix < mapWidth; ix++ )
			{
				if ( map[iy][ix] == -1 )
				{
					boolean emptyRight;
					if ( ix+1 >= mapWidth ) { emptyRight = false;}
					else if ( map[iy][ix+1] == floor ) { emptyRight = true; }
					else { emptyRight = false; }
					
					boolean emptyLeft;
					if ( ix-1 < 0 ) { emptyLeft = false;}
					else if ( map[iy][ix-1] == floor ) { emptyLeft = true; }
					else { emptyLeft = false; }
					
					boolean emptyDown;
					if ( iy+1 >= mapWidth ) { emptyDown = false;}
					else if ( map[iy+1][ix] == floor ) { emptyDown = true; }
					else { emptyDown = false; }
					
					boolean emptyUp;
					if ( iy-1 < 0 ) { emptyUp = false;}
					else if ( map[iy-1][ix] == floor ) { emptyUp = true; }
					else { emptyUp = false; }
					
					if ( emptyRight ) { map[iy][ix] = 1; }
					if ( emptyDown) { map[iy][ix] = 2; }
					if ( emptyLeft ) { map[iy][ix] = 3; }
					if ( emptyUp ) { map[iy][ix] = 4; }
					
					if ( emptyRight && emptyDown ) { map[iy][ix] = 5; }
					if ( emptyDown && emptyLeft ) { map[iy][ix] = 6; }
					if ( emptyLeft && emptyUp ) { map[iy][ix] = 7; }
					if ( emptyUp && emptyRight ) { map[iy][ix] = 8; }
					if ( emptyLeft && emptyRight ) { map[iy][ix] = 9; }
					if ( emptyUp && emptyDown ) { map[iy][ix] = 10; }
					
					if ( emptyRight && emptyDown && emptyLeft) { map[iy][ix] = 11; }
					if ( emptyDown && emptyLeft && emptyUp) { map[iy][ix] = 12; }
					if ( emptyLeft && emptyUp && emptyRight) { map[iy][ix] = 13; }
					if ( emptyUp && emptyRight && emptyDown) { map[iy][ix] = 14; }
					
					if ( emptyUp && emptyRight && emptyDown && emptyLeft) { map[iy][ix] = 15; }
				}
			}
		}
		// Map Outline
		for ( int iy = 0; iy < mapHeight; iy++ )
		{
			for ( int ix = 0; ix < mapWidth; ix++ )
			{
				if ( map[iy][ix] == -1 )
				{
					boolean emptyRight;
					if ( ix+1 >= mapWidth ) { emptyRight = false;}
					else if ( map[iy][ix+1] > 0 ) { emptyRight = true; }
					else { emptyRight = false; }
					
					boolean emptyLeft;
					if ( ix-1 < 0 ) { emptyLeft = false;}
					else if ( map[iy][ix-1] > 0 ) { emptyLeft = true; }
					else { emptyLeft = false; }
					
					boolean emptyDown;
					if ( iy+1 >= mapWidth ) { emptyDown = false;}
					else if ( map[iy+1][ix] > 0 ) { emptyDown = true; }
					else { emptyDown = false; }
					
					boolean emptyUp;
					if ( iy-1 < 0 ) { emptyUp = false;}
					else if ( map[iy-1][ix] > 0 ) { emptyUp = true; }
					else { emptyUp = false; }
					
					if ( emptyUp || emptyRight || emptyDown || emptyLeft) { map[iy][ix] = 0; }
				}
			}
		}
	}

	public int[][] getMapArray() { return map; }
	
	public void printBluePrint()
	{		
		for ( int iy = 0; iy < mapHeight; iy++ )
		{
			System.out.print("\n");
			for ( int ix = 0; ix < mapWidth; ix++ )
			{
				int cur = map[iy][ix];
				if ( cur == -1) { System.out.print("  "); }
				if ( cur == 0) { System.out.print("||"); }
				if ( cur > 0 && cur < 16 ) { System.out.print("[]"); }
				if ( cur == 16 ) { System.out.print("--"); }
			}
		}
	}
	
	public BufferedImage toImage()
	{
		BufferedImage image = new BufferedImage(mapWidth,mapHeight,BufferedImage.TYPE_INT_ARGB);
		int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		
		for ( int y = 0; y < mapHeight; y++ )
		{
			for ( int x = 0; x < mapWidth; x++ )
			{
				int index = y*mapHeight + x;
				pixels[index] = 0x00000000;
				
				int cur = map[y][x];
				
				if ( cur == floor )
				{
					pixels[index] = 0xFFFFFFFF;
				}
			}
		}
		
		return image;
	}
}
