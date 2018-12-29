package MainGame;

public class sys_MapChunk
{
	private int chunkWidth = 10;
	private int chunkHeight = 10;
	private double roughness = 4;
	
	private boolean filled[][] = new boolean[chunkWidth*3][chunkHeight*3];
	
	public void printOut()
	{
		for( int iy = 0; iy < chunkWidth*3; iy++ )
		{
			System.out.print("\n");
			for ( int ix = 0; ix < chunkHeight*3; ix++ )
			{
				boolean cur = filled[iy][ix];
				
				if ( cur ) { System.out.print("[]"); }
				else { System.out.print("  ");}
			}
		}
	}
	
	public sys_MapChunk( int n )
	{
		boolean right = false;
		boolean down = false;
		boolean left = false;
		boolean up = false;
		
		if ( n == 1 ) { right = true; }
		if ( n == 2 ) { down = true; }
		if ( n == 3 ) { left = true; }
		if ( n == 4 ) { up = true; }
		
		if ( n == 5 ) { right = true; down = true; }
		if ( n == 6 ) { down = true; left = true; }
		if ( n == 7 ) { left = true; up = true; }
		if ( n == 8 ) { up = true; right = true; }
		if ( n == 9 ) { left = true; right = true; }
		if ( n == 10 ) { up = true; down = true; }

		if ( n == 11 ) { right = true; down = true; left = true;}
		if ( n == 12 ) { down = true; left = true; up = true;}
		if ( n == 13 ) { left = true; up = true; right = true;}
		if ( n == 14 ) { up = true; right = true; down = true;}
		
		for( int iy = chunkWidth; iy < chunkWidth*2; iy++ )
		{
			for ( int ix = chunkHeight; ix < chunkHeight*2; ix++ )
			{
				filled[iy][ix] = true;
			}
		}
		
		boolean rightDown = !(right && down);
		boolean downLeft = !(down && left);
		boolean leftUp = !(left && up);
		boolean upRight = !(up && right);
		
		if ( right ) { carveDetail(chunkHeight,chunkWidth,chunkWidth*2-1,0,upRight,rightDown); }
		if ( left) { carveDetail(chunkHeight,chunkWidth,chunkWidth,2,leftUp,downLeft); }
		if ( down ) { carveDetail(chunkWidth,chunkHeight,chunkHeight*2-1,1,downLeft,rightDown); }
		if ( up ) { carveDetail(chunkWidth,chunkHeight,chunkHeight,3,leftUp,upRight); }
	}
	
	public boolean[][] getArray() { return filled; }
	
	private void carveDetail ( int chunkFirst, int chunkSecond, int chunkStartPoint, int side, boolean startPolish, boolean endPolish)
	{
		double carve[] = new double[chunkFirst];
		for ( int i = 0; i < chunkFirst; i++ )
		{
			if ( i == 0 )
			{
				if ( startPolish ) { carve[i] = 0; }
				else carve[i] = Math.random()*roughness*4 - roughness*2;
			}
			else { carve[i] = carve[i-1] + Math.random()*roughness - roughness/2; }
		}
		
		double endPoint = carve[chunkFirst-1];

		if ( endPolish )
		{
			for ( int i = 0; i < chunkFirst; i++ )
			{
				double ratio = (endPoint/(chunkFirst-1))*i;
				carve [i] -= ratio;
			}
		}
		

		for ( int i = 0; i < chunkFirst; i++ )
		{
			carve [i] = functions.clamp(carve [i], -10, 10);
		}

		int ci = 0;
		for ( int n = chunkFirst; n < chunkFirst*2; n++ )
		{
			if ( carve[ci] > 0 )
			{
				for ( int i = 0; i < carve[ci]; i++ )
				{
					if ( side == 0 ) {filled[n][chunkStartPoint + i ] = true;}
					if ( side == 1 ) {filled[chunkStartPoint + i ][n] = true;}
					if ( side == 2 ) {filled[n][chunkStartPoint + i ] = false;}
					if ( side == 3 ) {filled[chunkStartPoint + i ][n] = false;}
				}
			}
			else
			{
				for ( int i = 0; i > carve[ci]; i-- )
				{
					if ( side == 0 ) {filled[n][chunkStartPoint + i ] = false;}
					if ( side == 1 ) {filled[chunkStartPoint + i ][n] = false;}
					if ( side == 2 ) {filled[n][chunkStartPoint + i ] = true;}
					if ( side == 3 ) {filled[chunkStartPoint + i ][n] = true;}
				}
			}
			ci++;
		}
	}
}
