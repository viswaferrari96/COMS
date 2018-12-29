package MainGame;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;

public class GameObjectManager
{
	private sys_CustomLL<GameObject> ObjectList = new sys_CustomLL<GameObject>();
	private sys_CustomLL<col_Hitbox> HitboxList = new sys_CustomLL<col_Hitbox>();
	private ArrayList<GameObject> drawList;
	private int ObjectCount;
	
	private Comparator<GameObject> sorter = new Comparator<GameObject>()
		{
			public int compare ( GameObject a, GameObject b )
			{
				return a.compareTo(b);
			}
		};
		
	
	public void step()
	{
		sys_CustomLL<GameObject>.Node<GameObject> cur = ObjectList.getFirstNode();
		ObjectCount = 0;
		while ( cur != null )
		{
			cur.getData().step();
			cur = cur.getNextNode();
			ObjectCount++;
		}
		listCheck();
		createDrawList();
	}
	
	public void draw( Graphics g )
	{		
		for ( int i = 0; i < drawList.size(); i++ )
		{
			GameObject toRun = drawList.get(i);
			if ( toRun != null )
			{
				int compareX = (int) Math.abs(toRun.getX()-(-Game.CAMERA.getX()+Game.WIDTH/2));
				int compareY = (int) Math.abs(toRun.getY()-(-Game.CAMERA.getY()+Game.HEIGHT/2));
				
				if ( compareX < ((double)Game.WIDTH)*1.5 && compareY < ((double)Game.HEIGHT)*1.5)
				{
				}
					toRun.draw(g);
			}
		}
	}
	
	private void listCheck()
	{
		if ( ObjectCount != ObjectList.size())
		{
			sys_CustomLL<GameObject>.Node<GameObject> cur = ObjectList.getFirstNode();
			sys_CustomLL<GameObject> copy = new sys_CustomLL<GameObject>();
			while ( cur != null )
			{
				copy.add(cur.getData());
				cur = cur.getNextNode();
			}
			ObjectList = copy;
		}
	}
	
	private void createDrawList()
	{
		drawList = ObjectList.toArrayList();
		try { drawList.sort(sorter); }
		catch ( Exception e ) {}
	}
	
	
	public void addGameObject( GameObject ob ) { ObjectList.add(ob); }
	public void removeGameObject( GameObject ob ) { ObjectList.remove(ob); }
	public int getObjectNum() { return ObjectList.size(); }
	
	public sys_CustomLL<GameObject> getObjectList() { return ObjectList; }
	
	
	
	public void addHitbox( col_Hitbox ob ) { HitboxList.add(ob); }
	public void removeHitbox( col_Hitbox ob ) { HitboxList.remove(ob); }
	public int getHitboxNum() { return HitboxList.size(); }
	
	public sys_CustomLL<col_Hitbox> getHitboxList() { return HitboxList; }
	
	public void printAllOut()
	{
		System.out.println( ObjectCount + "  " + HitboxList.size());
		
		sys_CustomLL<GameObject>.Node<GameObject> cur = ObjectList.getFirstNode();
		
		while ( cur != null )
		{
			System.out.println(cur.getData().getName());
			cur = cur.getNextNode();
		}
		if ( cur == null ) { System.out.println("\n"); }
		
		while ( cur != null )
		{
			System.out.println(cur.getData().getName());
			cur = cur.getNextNode();
		}
	}
}