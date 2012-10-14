package datastructure;

/**
 * This Class Implements the Disjoint Sets ADT with the efficient Path Compression and Union by Rank Algorithm
 * @author sabari
 *
 */
public class DisjointSet {

	/**
	 * parent : Stores the parent or root number 
	 * root : Stores the rank, that is, the Maximum length of the leaves from this node
	 */
	private int[] parent;
	private int[] rank;
	
	/**
	 * This is the MakeSet function of the disjoint set object
	 */
	public DisjointSet(int row, int col) {
		// TODO Auto-generated constructor stub
		parent = new int[row*col + 1];
		rank = new int[row*col + 1];
		for( int i = 1; i < parent.length; i++)
		{
			parent[i] = i;						//Initially every node is its own parent and hence has rank zero
			rank[i] = 0;
		}
	}
	
	/**
	 * Implements Recursive method of Path Compression
	 * @param cell_no : The node for which the parent number is to be found
	 * @return : Parent number of the given node
	 */
	public int findSet( int cell_no )
	{
		if( parent[cell_no] != cell_no )
			parent[cell_no] = findSet(parent[cell_no]);
		return parent[cell_no];
	}
	
	/**
	 * Implements Union By Rank
	 * @param cell1 : The node which has to be united with the 'cell2' node
	 */
	public void union( int cell1, int cell2 )
	{
		LinkSet( findSet(cell1), findSet(cell2) );
	}

	/**
	 * Function to union the Sets whose representatives are given the arguments of the function
	 * @param findSet1 : The first node's representative
	 * @param findSet2 : The second node's representative
	 */
	private void LinkSet(int findSet1, int findSet2) {
		// TODO Auto-generated method stub
		if( rank[findSet1] > rank[findSet2] )
			parent[findSet2] = findSet1;
		else
		{
			parent[findSet1] = findSet2;
			if( rank[findSet1] == rank[findSet2] )
				rank[findSet2]++;
		}
	}	
	
}
