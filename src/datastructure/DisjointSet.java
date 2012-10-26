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
	public int[] parent;
	public int[] rank;
	public int arrSize;
	
	/**
	 * This is the MakeSet function of the disjoint set object
	 * @param size : The number of nodes to be created in the Disjoint Set
	 */
	public DisjointSet(int size) {
		/*
		 * Let the array index '0' indicate yellow cell and '1' indicate orange cell
		 */
		arrSize = size + 2;
		parent = new int[arrSize];
		rank = new int[arrSize];
		for( int i = 0; i < arrSize; i++){
			/*
			 * Initially every node is its own parent and hence has rank zero
			 */
			parent[i] = i;
			rank[i] = 0;
		}
	}
	
	/**
	 * Implements Recursive method of Path Compression
	 * @param cell_no : The node for which the parent number is to be found
	 * @return : Parent number of the given node
	 */
	public int findSet( int cell_no ){
		if( parent[cell_no] != cell_no )
			parent[cell_no] = findSet(parent[cell_no]);
		return parent[cell_no];
	}
	
	/**
	 * Implements Union By Rank
	 * @param cell1 : The node which has to be united with the 'cell2' node
	 */
	public void union( int cell1, int cell2 ){
		int a = findSet(cell1);
		int b = findSet(cell2);
		if(a == 0)
			LinkSet(a, b);
		else
			LinkSet(b, a);
	}

	/**
	 * Function to union the Sets whose representatives are given the arguments of the function
	 * @param findSet1 : The first node's representative
	 * @param findSet2 : The second node's representative
	 */
	private void LinkSet(int findSet1, int findSet2) {
		if( rank[findSet2] > rank[findSet1] )
			parent[findSet1] = findSet2;
		else
		{
			parent[findSet2] = findSet1;
			if( rank[findSet1] == rank[findSet2] )
				rank[findSet2]++;
		}
	}	
	
	/**
	 * Function to get the copy of the current DisjointSet object
	 * @return copy : Copy of 'this' DisjointSet
	 */
	public DisjointSet getCopy(){
		DisjointSet copy = new DisjointSet(arrSize);
		for(int i = 0; i < arrSize; ++i){
			copy.parent[i] = this.parent[i];
			copy.rank[i] = this.rank[i];
		}
		return copy;
	}
}