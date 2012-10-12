package datastructure;

import java.util.Vector;
public class Grid {
	
	private static int noRows;
	private static int noColumns;
	private static boolean isSolved;
	public static Cell[][] cellLst;

	/*
	 * Setter Methods
	 */
	public void setRows(int rows){
		noRows = rows;
	}
	public void setColumn(int cols){
		noColumns = cols;
	}
	public void setisSolved(boolean solved){
		isSolved = solved;
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
	public static boolean getisSolvedr(){
		return isSolved;
	}
	
	/**
	 * Function to get the Set of all the Wall in random order
	 * @return : Wall List of the Grid
	 */
	public Vector<Wall> getAllWalls(){
		Vector<Wall> wallLst = new Vector<Wall>(2*(noRows+1)*(noColumns+1));
		int i, j;
		for( i = 0; i < noRows; ++i)
			wallLst.add(cellLst[i][0].getLeftWall());
		for( j = 0; j < noColumns; ++j)
			wallLst.add(cellLst[0][j].getTopWall());
		for(  i = 0; i < noRows; ++i){
			for( j = 0; j < noColumns; ++j){
				wallLst.add(cellLst[i][j].getBottomWall());
				wallLst.add(cellLst[i][j].getRightWall());
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
	public void updateCellList(int rows, int cols, int[][] inpCellVal){
		this.setRows(rows);
		this.setColumn(cols);
		cellLst = new Cell[noRows][noColumns];
		int i, j;
		for( i = 0; i < noRows; ++i)
			for( j = 0; j < noColumns; ++j)
				cellLst[i][j] = new Cell(inpCellVal[i][j], new Coordinate(i, j));
		isSolved = false;
	}
}
