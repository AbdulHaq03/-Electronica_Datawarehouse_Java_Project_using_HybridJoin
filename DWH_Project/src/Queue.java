
public class Queue {
	
	public class Node{
		String Data;
		Node next;
		Node prev;
		
		public Node(String data)
		{
			Data = data;
			this.prev = null;
			this.next = null;
		}
	}
	
	Node Head;
	Node Tail;
	
	public Queue()
	{
		this.Head = null;
		this.Tail = null;
	}
	
	public void enqueue(String Data)
	{
		Node newNode = new Node(Data);
		
		if (Tail == null)
		{
			Head = Tail = newNode;
		}
		else {
			Tail.next = newNode;
			newNode.prev = Tail;
			Tail = newNode;
		}
	}
	
	public String dequeue()
	{
		String Data = null;
		if (Head != null)
		{
			Data = Head.Data;
			Head = Head.next;
			if(Head == null)
			{
				Tail = null;
			}
			else {
				Head.prev = null;
			}
		}
		return Data;
	}
	
	public boolean isEmpty()
	{
		if (Head == null)
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	public void print()
	{
		Node Current = Head;
		
		while(Current != null)
		{
			System.out.println(Current.Data + " ");
			Current = Current.next;
		}
	}
}
