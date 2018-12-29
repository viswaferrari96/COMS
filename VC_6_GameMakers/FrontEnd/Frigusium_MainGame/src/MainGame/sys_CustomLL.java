package MainGame;

import java.util.ArrayList;

public class sys_CustomLL<T>
{
	private Node<T> firstNode;
	private Node<T> lastNode;
	private int numberOfEntries;

	public sys_CustomLL() { initializeDataFields(); }

	public void clear() { initializeDataFields(); }

	private void initializeDataFields()
	{
		firstNode = null;
		lastNode = firstNode;
		numberOfEntries = 0;
	}

	public void add( T newEntry )
	{
		Node<T> newNode = new Node<T>(newEntry);

		if ( newEntry != null )
		{
			if (isEmpty()) { firstNode = newNode; }
			else { lastNode.setNextNode(newNode); }
			lastNode = newNode;

			//System.out.println(((GameObject)newNode.getData()).getName());
			numberOfEntries++;
		}
	}
	
	public T remove ( T Object )
	{
		Node<T> cur = firstNode;
		Node<T> pre = firstNode;
		
		while ( cur != null )
		{
			if ( cur.getData() == Object )
			{
				if ( cur == firstNode ) { firstNode = firstNode.getNextNode(); }
				else if ( cur == lastNode ) { pre.setNextNode(null); lastNode = pre;}
				else { pre.setNextNode(cur.getNextNode()); }
				
				//System.out.println("removing");
				numberOfEntries--;
				break;
			}
			pre = cur;
			cur = cur.getNextNode();
		}
		
		return null;
	}
 
	public boolean isEmpty()
	{
		if (numberOfEntries == 0)
		{
			assert firstNode == null : "numberOfEntries is 0 but firstNode is not null!";
			return true;
		}
		else
		{
			assert firstNode != null : "numberOfEntries is not 0 but firstNode is null!";
			return false;
		}
	}
	
	public T get( int givenPosition )
	{
		assert !isEmpty() && (0 <= givenPosition) && (givenPosition < numberOfEntries);
		Node<T> currentNode = firstNode;

		for (int counter = 0; counter < givenPosition; counter++)
		{
			currentNode = currentNode.getNextNode();
		}
		assert currentNode != null;
		return currentNode.getData();
	}
	
	public ArrayList<T> toArrayList()
	{
		ArrayList<T> copy = new ArrayList<T>();
		Node<T> cur = firstNode;
		while ( cur != null )
		{
			copy.add(cur.getData());
			cur = cur.getNextNode();
		}
		return copy;
	}
	
	public int size() { return numberOfEntries; }
	
	public Node<T> getFirstNode() { return firstNode; }
	
	@SuppressWarnings("hiding")
	public class Node<T>
	{
		private T data;
		private Node<T> next;

		private Node(T dataPortion)
		{
			data = dataPortion;
			next = null;
		}

		private Node(T dataPortion, Node<T> nextNode)
		{
			data = dataPortion;
			next = nextNode;
		}

		public T getData() { return data; }
		public Node<T> getNextNode() { return next; }

		private void setNextNode(Node<T> nextNode) { next = nextNode; }
	}
}