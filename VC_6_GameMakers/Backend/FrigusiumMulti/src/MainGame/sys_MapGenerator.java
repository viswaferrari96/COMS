package MainGame;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class sys_MapGenerator
{
	private int mapWidth;
	private int mapHeight;
	private int[][] mapChunk_num;
	private sys_MapChunk[][] mapChunk;
	
	public sys_MapGenerator()
	{
		mapGenerate();
	}
	public void mapGenerate()
	{
		mapChunk = new sys_MapChunk[100][100];
		mapChunk_num = new int[mapChunk.length][mapChunk[0].length];
		
		mapWidth = mapChunk_num[0].length;
		mapHeight = mapChunk_num.length;
		
		double landRatio = 0.2;
		// 0 is filled, 15 is empty
		
		/*
		 * this fills the whole array with 15, which is empty
		 */
		for ( int iy = 0; iy < mapHeight; iy++ )
		{
			for ( int ix = 0; ix < mapWidth; ix++ )
			{
				mapChunk_num[iy][ix] = 15;
			}
		}
		
		
		
		// Sets the start point
		int startCoord_x = mapWidth/2;
		int startCoord_y = mapHeight/2;
		
		int coordCur_x = startCoord_x;
		int coordCur_y = startCoord_y;
		
		int landSize = 0;
		int mapSize = mapChunk_num.length * mapChunk_num[0].length;
		
		// Map generation
		while ( true )
		{
			if ( mapChunk_num[coordCur_y][coordCur_x] != 0 ) { landSize++; }
			mapChunk_num[coordCur_y][coordCur_x] = 0;
			
			if ( (double)landSize/mapSize > landRatio ) { break; }
			
			int sideChoose = (int)(Math.random()*4);
			
			if ( sideChoose == 0 ) { coordCur_x++; }
			if ( sideChoose == 1 ) { coordCur_x--; }
			if ( sideChoose == 2 ) { coordCur_y++; }
			if ( sideChoose == 3 ) { coordCur_y--; }
			
			if ( coordCur_x < 1 || coordCur_x+1 >= mapWidth || coordCur_y < 1 || coordCur_y+1 >= mapHeight )
			{
				coordCur_x = startCoord_x;
				coordCur_y = startCoord_y;
			}
		}
		
		// Map Refine
		for ( int iy = 0; iy < mapHeight; iy++ )
		{
			for ( int ix = 0; ix < mapWidth; ix++ )
			{
				if ( mapChunk_num[iy][ix] == 0 )
				{
					boolean emptyRight;
					if ( ix+1 >= mapWidth ) { emptyRight = true;}
					else if ( mapChunk_num[iy][ix+1] == 15 ) { emptyRight = true; }
					else { emptyRight = false; }
					
					boolean emptyLeft;
					if ( ix-1 < 0 ) { emptyLeft = true;}
					else if ( mapChunk_num[iy][ix-1] == 15 ) { emptyLeft = true; }
					else { emptyLeft = false; }
					
					boolean emptyDown;
					if ( iy+1 >= mapWidth ) { emptyDown = true;}
					else if ( mapChunk_num[iy+1][ix] == 15 ) { emptyDown = true; }
					else { emptyDown = false; }
					
					boolean emptyUp;
					if ( iy-1 < 0 ) { emptyUp = true;}
					else if ( mapChunk_num[iy-1][ix] == 15 ) { emptyUp = true; }
					else { emptyUp = false; }
					
					if ( emptyRight ) { mapChunk_num[iy][ix] = 1; }
					if ( emptyDown) { mapChunk_num[iy][ix] = 2; }
					if ( emptyLeft ) { mapChunk_num[iy][ix] = 3; }
					if ( emptyUp ) { mapChunk_num[iy][ix] = 4; }
					
					if ( emptyRight && emptyDown ) { mapChunk_num[iy][ix] = 5; }
					if ( emptyDown && emptyLeft ) { mapChunk_num[iy][ix] = 6; }
					if ( emptyLeft && emptyUp ) { mapChunk_num[iy][ix] = 7; }
					if ( emptyUp && emptyRight ) { mapChunk_num[iy][ix] = 8; }
					if ( emptyLeft && emptyRight ) { mapChunk_num[iy][ix] = 9; }
					if ( emptyUp && emptyDown ) { mapChunk_num[iy][ix] = 10; }
					
					if ( emptyRight && emptyDown && emptyLeft) { mapChunk_num[iy][ix] = 11; }
					if ( emptyDown && emptyLeft && emptyUp) { mapChunk_num[iy][ix] = 12; }
					if ( emptyLeft && emptyUp && emptyRight) { mapChunk_num[iy][ix] = 13; }
					if ( emptyUp && emptyRight && emptyDown) { mapChunk_num[iy][ix] = 14; }
					
					mapChunk[iy][ix] = new sys_MapChunk(mapChunk_num[iy][ix]);
				}
			}
		}
	}

	
	public void printBluePrint()
	{		
		for ( int iy = 0; iy < mapHeight; iy++ )
		{
			System.out.print("\n");
			for ( int ix = 0; ix < mapWidth; ix++ )
			{
				int cur = mapChunk_num[iy][ix];
				if ( cur != 15 ) { System.out.print("[]");}
				else { System.out.print("  "); }
			}
		}
	}
	
	public BufferedImage toImage()
	{
		int chunkSize = 10;
		BufferedImage image = new BufferedImage(mapWidth*chunkSize,mapHeight*chunkSize,BufferedImage.TYPE_INT_RGB);
		int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		
		for ( int y = 0; y < mapHeight; y++ )
		{
			for ( int x = 0; x < mapWidth; x++ )
			{
				int originx = x*chunkSize;
				int originy = y*chunkSize;
				
				sys_MapChunk cur = mapChunk[y][x];
				if ( cur != null )
				{
					boolean[][] curChunk = cur.getArray();
					
					for ( int iy = 0; iy < curChunk.length; iy++ )
					{
						for ( int ix = 0; ix < curChunk[0].length; ix++ )
						{
							int curx = originx + ix - chunkSize;//curChunk[0].length/3;
							int cury = originy + iy - chunkSize;//curChunk.length/3;
							int index = (cury*(mapWidth*chunkSize)) + curx;
							
							if ( index >= 0 && index < pixels.length)
							{
								if ( curChunk[iy][ix] ) { pixels[index] = 0xFFFFFF; }
							}
						}
					}
				}
			}
		}
		return image;
	}
}
