package datastructure;

import java.util.Vector;
public class Grid {
	
	private static int noRows;
	private static int noColumns;
	public static boolean isSolved;
	public static Cell[][] cellLst;
	public static DisjointSet ds;

	/*
	 * Setter Methods
	 */
	public void setRows(int rows){
		noRows = rows;
	}
	public void setColumn(int cols){
		noColumns = cols;
	}
	
	/*
	 * Getter Methods
	 */
	public static int getRows(){
		return noRows;
	}
	public static int getColumns(){
		return noColumns;
	}
	
	/**
	 * Function to get the Set of all the Wall in random order
	 * @return : Wall List of the Grid
	 */
	public static Vector<Wall> getAllWalls(){
		return getAllWalls(cellLst);
	}
	
	public static Vector<Wall> getAllWalls(Cell[][] cellArr){
		Vector<Wall> wallLst = new Vector<Wall>(2*noRows*noColumns + noColumns + noRows);
		int i, j;
		for( i = 0; i < noRows; ++i)
			wallLst.add(cellArr[i][0].getLeftWall());
		for( j = 0; j < noColumns; ++j)
			wallLst.add(cellArr[0][j].getTopWall());
		for(  i = 0; i < noRows; ++i){
			for( j = 0; j < noColumns; ++j){
				wallLst.add(cellArr[i][j].getBottomWall());
				wallLst.add(cellArr[i][j].getRightWall());
			}
		}
		return wallLst;
	}
	
	/**
	 * Function to initialize the entire Grid with the Input values
	 * @param rows : Number of rows in the grid
	 * @param cols : Number of columns in the grid
	 * @param inpCellVal : The Game board input numbers
	 */
	public static void updateCellList(int rows, int cols, int[][] inpCellVal){
		noRows = rows;
		noColumns = cols;
		cellLst = new Cell[noRows][noColumns];
		int i, j;
		for( i = 0; i < noRows; ++i)
			for( j = 0; j < noColumns; ++j)
				cellLst[i][j] = new Cell(inpCellVal[i][j], new Coordinate(i, j));
		ds = new DisjointSet(noColumns*noRows);
		isSolved = false;
	}
}