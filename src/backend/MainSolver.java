package backend;

import java.util.ArrayList;
import java.util.List;

import datastructure.Cell;
import datastructure.DisjointSet;
import datastructure.Grid;

public class MainSolver {
	private static int rowSize = Grid.getRows(), colSize = Grid.getColumns();
	private static Cell[][] cellLst = Grid.cellLst;
	private static Cell outerCell = new Cell(-1);
	private static DisjointSet ds;
	
	public static boolean checkBounds(int x, int y){
		return ( x < 0 || y < 0 || x >= rowSize || y >= colSize) ? false :  true;
	}
	
	public static int getIndex(int x, int y){
		return (x*rowSize) + y + 2;
	}
	
	public static void basicSolver(){
		Grid.ds = new DisjointSet(rowSize*colSize);
		ds = Grid.ds;
		outerCell.setCellColor(1, true);
		Cell tmpCell;
		int i, j;
		
		/*
		 * Works!
		 * Checking for '0' in the grid.
		 * Setting all the four edges of those cells to 'Fixed' and 'Not Active'.  
		 */
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				if(cellLst[i][j].getNodeVal() == 0){
					cellLst[i][j].getLeftWall().setFixed(true, false);
					cellLst[i][j].getRightWall().setFixed(true, false);
					cellLst[i][j].getBottomWall().setFixed(true, false);
					cellLst[i][j].getTopWall().setFixed(true, false);
					if(checkBounds(i+1, j))
						ds.union( getIndex(i, j), getIndex(i+1, j));
					else
						ds.union( 0, getIndex(i, j));
					if(checkBounds(i-1, j))
						ds.union( getIndex(i, j), getIndex(i-1, j));
					else
						ds.union( 0, getIndex(i, j));
					if(checkBounds(i, j+1))
						ds.union( getIndex(i, j), getIndex(i, j+1));
					else
						ds.union( 0, getIndex(i, j));
					if(checkBounds(i, j-1))
						ds.union( getIndex(i, j), getIndex(i, j-1));
					else
						ds.union( 0, getIndex(i, j));
				}
			}
		}
		
		/*
		 * Works!
		 * Checking for Adjacent '0' and '3'
		 * Setting all the edges other than the sharing edge to 'Fixed' and 'Active'.
		 */
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				if(cellLst[i][j].getNodeVal() == 0){
					if(checkBounds(i+1,j) && cellLst[i+1][j].getNodeVal() == 3){
						tmpCell = cellLst[i+1][j];
						ds.union(getIndex(i, j), getIndex(i+1, j));
						if(!tmpCell.getBottomWall().getFixed())
							tmpCell.getBottomWall().setFixed(true, true);
						if(!tmpCell.getLeftWall().getFixed())
							tmpCell.getLeftWall().setFixed(true, true);
						if(!tmpCell.getRightWall().getFixed())
							tmpCell.getRightWall().setFixed(true, true);
						if(checkBounds(i, j-1) && !cellLst[i][j-1].getBottomWall().getFixed())
							cellLst[i][j-1].getBottomWall().setFixed(true, true);
						if(checkBounds(i, j+1) && !cellLst[i][j+1].getBottomWall().getFixed())
							cellLst[i][j+1].getBottomWall().setFixed(true, true);						
					}
					if(checkBounds(i-1, j) && cellLst[i-1][j].getNodeVal() == 3){
						tmpCell = cellLst[i-1][j];
						ds.union(getIndex(i, j), getIndex(i-1, j));
						if(!tmpCell.getTopWall().getFixed())
							tmpCell.getTopWall().setFixed(true, true);
						if(!tmpCell.getLeftWall().getFixed())
							tmpCell.getLeftWall().setFixed(true, true);
						if(!tmpCell.getRightWall().getFixed())
							tmpCell.getRightWall().setFixed(true, true);
						if(checkBounds(i, j-1) && !cellLst[i][j-1].getTopWall().getFixed())
							cellLst[i][j-1].getTopWall().setFixed(true, true);
						if(checkBounds(i, j+1) && !cellLst[i][j+1].getTopWall().getFixed())
							cellLst[i][j+1].getTopWall().setFixed(true, true);
					}
					if(checkBounds(i, j-1) && cellLst[i][j-1].getNodeVal() == 3){
						tmpCell = cellLst[i][j-1];
						ds.union(getIndex(i, j), getIndex(i, j-1));
						if(!tmpCell.getTopWall().getFixed())
							tmpCell.getTopWall().setFixed(true, true);
						if(!tmpCell.getLeftWall().getFixed())
							tmpCell.getLeftWall().setFixed(true, true);
						if(!tmpCell.getBottomWall().getFixed())
							tmpCell.getBottomWall().setFixed(true, true);
						if(checkBounds(i+1, j) && !cellLst[i+1][j].getLeftWall().getFixed())
							cellLst[i+1][j].getLeftWall().setFixed(true, true);
						if(checkBounds(i-1, j) && !cellLst[i-1][j].getLeftWall().getFixed())
							cellLst[i-1][j].getLeftWall().setFixed(true, true);
					}
					if(checkBounds(i, j+1) && cellLst[i][j+1].getNodeVal() == 3){
						tmpCell = cellLst[i][j+1];
						ds.union(getIndex(i, j), getIndex(i, j+1));
						if(!tmpCell.getTopWall().getFixed())
							tmpCell.getTopWall().setFixed(true, true);
						if(!tmpCell.getRightWall().getFixed())
							tmpCell.getRightWall().setFixed(true, true);
						if(!tmpCell.getBottomWall().getFixed())
							tmpCell.getBottomWall().setFixed(true, true);
						if(checkBounds(i+1, j) && !cellLst[i+1][j].getRightWall().getFixed())
							cellLst[i+1][j].getRightWall().setFixed(true, true);
						if(checkBounds(i-1, j) && !cellLst[i-1][j].getRightWall().getFixed())
							cellLst[i-1][j].getRightWall().setFixed(true, true);
					}			
				}
			}
		}
		
		/*
		 * Works!
		 * Checking for Diagonal '0' and '3'
		 * Setting the two edges incident at the common corner ( of 0 and 3 ) to 'Fixed' and 'Active' 
		 */
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				if(cellLst[i][j].getNodeVal() == 0){
					if(checkBounds(i+1, j+1) && cellLst[i+1][j+1].getNodeVal() == 3){
						tmpCell = cellLst[i+1][j+1];
						if(!tmpCell.getLeftWall().getFixed())
							tmpCell.getLeftWall().setFixed(true, true);
						if(!tmpCell.getTopWall().getFixed())
							tmpCell.getTopWall().setFixed(true, true);
					}
					if(checkBounds(i-1, j-1) && cellLst[i-1][j-1].getNodeVal() == 3){
						tmpCell = cellLst[i-1][j-1];
						if(!tmpCell.getBottomWall().getFixed())
							tmpCell.getBottomWall().setFixed(true, true);
						if(!tmpCell.getRightWall().getFixed())
							tmpCell.getRightWall().setFixed(true, true);
					}
					if(checkBounds(i+1, j-1) && cellLst[i+1][j-1].getNodeVal() == 3){
						tmpCell = cellLst[i+1][j-1];
						if(!tmpCell.getRightWall().getFixed())
							tmpCell.getRightWall().setFixed(true, true);
						if(!tmpCell.getTopWall().getFixed())
							tmpCell.getTopWall().setFixed(true, true);
					}
					if(checkBounds(i-1, j+1) && cellLst[i-1][j+1].getNodeVal() == 3){
						tmpCell = cellLst[i-1][j+1];
						if(!tmpCell.getLeftWall().getFixed())
							tmpCell.getLeftWall().setFixed(true, true);
						if(!tmpCell.getBottomWall().getFixed())
							tmpCell.getBottomWall().setFixed(true, true);
					}
				}
			}
		}
		
		/*
		 * Works!
		 * Checking for Two Adjacent 3's
		 * Setting the sharing edge and the two other parallel edges to it to 'Fixed' and 'Active' 
		 */
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				if(cellLst[i][j].getNodeVal() == 3){
					if(checkBounds(i, j+1) && cellLst[i][j+1].getNodeVal() == 3){
						if(!cellLst[i][j].getLeftWall().getFixed())
							cellLst[i][j].getLeftWall().setFixed(true, true);
						if(!cellLst[i][j].getRightWall().getFixed())
							cellLst[i][j].getRightWall().setFixed(true, true);
						if(!cellLst[i][j+1].getRightWall().getFixed())
							cellLst[i][j+1].getRightWall().setFixed(true, true);
					}
					if(checkBounds(i, j-1) && cellLst[i][j-1].getNodeVal() == 3){
						if(!cellLst[i][j].getLeftWall().getFixed())
							cellLst[i][j].getLeftWall().setFixed(true, true);
						if(!cellLst[i][j].getRightWall().getFixed())
							cellLst[i][j].getRightWall().setFixed(true, true);
						if(!cellLst[i][j-1].getLeftWall().getFixed())
							cellLst[i][j-1].getLeftWall().setFixed(true, true);
					}
					if(checkBounds(i+1, j) && cellLst[i+1][j].getNodeVal() == 3){
						if(!cellLst[i][j].getTopWall().getFixed())
							cellLst[i][j].getTopWall().setFixed(true, true);
						if(!cellLst[i][j].getBottomWall().getFixed())
							cellLst[i][j].getBottomWall().setFixed(true, true);
						if(!cellLst[i+1][j].getBottomWall().getFixed())
							cellLst[i+1][j].getBottomWall().setFixed(true, true);
					}
					if(checkBounds(i-1, j) && cellLst[i-1][j].getNodeVal() == 3){
						if(!cellLst[i][j].getBottomWall().getFixed())
							cellLst[i][j].getBottomWall().setFixed(true, true);
						if(!cellLst[i][j].getTopWall().getFixed())
							cellLst[i][j].getTopWall().setFixed(true, true);
						if(!cellLst[i-1][j].getTopWall().getFixed())
							cellLst[i-1][j].getTopWall().setFixed(true, true);
					}
				}
			}
		}
		
		/*
		 * Works!
		 * Checking for 2 Diagonal 3's
		 * Setting the outermost opposite edges ( 2 per cell ) to 'Fixed' and 'Active'
		 */
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				if(cellLst[i][j].getNodeVal() == 3){
					if(checkBounds(i+1, j+1) && cellLst[i+1][j+1].getNodeVal() == 3){
						if(!cellLst[i+1][j+1].getBottomWall().getFixed())
							cellLst[i+1][j+1].getBottomWall().setFixed(true, true);
						if(!cellLst[i+1][j+1].getRightWall().getFixed())
							cellLst[i+1][j+1].getRightWall().setFixed(true, true);
						if(!cellLst[i][j].getTopWall().getFixed())
							cellLst[i][j].getTopWall().setFixed(true, true);
						if(!cellLst[i][j].getLeftWall().getFixed())
							cellLst[i][j].getLeftWall().setFixed(true, true);
					}
					if(checkBounds(i-1, j-1) && cellLst[i-1][j-1].getNodeVal() == 3){
						if(!cellLst[i-1][j-1].getTopWall().getFixed())
							cellLst[i-1][j-1].getTopWall().setFixed(true, true);
						if(!cellLst[i-1][j-1].getLeftWall().getFixed())
							cellLst[i-1][j-1].getLeftWall().setFixed(true, true);
						if(!cellLst[i][j].getBottomWall().getFixed())
							cellLst[i][j].getBottomWall().setFixed(true, true);
						if(!cellLst[i][j].getRightWall().getFixed())
							cellLst[i][j].getRightWall().setFixed(true, true);
					}
					if(checkBounds(i+1, j-1) && cellLst[i+1][j-1].getNodeVal() == 3){
						if(!cellLst[i+1][j-1].getBottomWall().getFixed())
							cellLst[i+1][j-1].getBottomWall().setFixed(true, true);
						if(!cellLst[i+1][j-1].getLeftWall().getFixed())
							cellLst[i+1][j-1].getLeftWall().setFixed(true, true);
						if(!cellLst[i][j].getTopWall().getFixed())
							cellLst[i][j].getTopWall().setFixed(true, true);
						if(!cellLst[i][j].getRightWall().getFixed())
							cellLst[i][j].getRightWall().setFixed(true, true);
					}
					if(checkBounds(i-1, j+1) && cellLst[i-1][j+1].getNodeVal() == 3){
						if(!cellLst[i-1][j+1].getTopWall().getFixed())
							cellLst[i-1][j+1].getTopWall().setFixed(true, true);
						if(!cellLst[i-1][j+1].getRightWall().getFixed())
							cellLst[i-1][j+1].getRightWall().setFixed(true, true);
						if(!cellLst[i][j].getBottomWall().getFixed())
							cellLst[i][j].getBottomWall().setFixed(true, true);
						if(!cellLst[i][j].getLeftWall().getFixed())
							cellLst[i][j].getLeftWall().setFixed(true, true);
					}
				}
			}
		}
		
		/*
		 * Checking for Corner cells
		 */
		handleCorner(cellLst[0][0]);
		handleCorner(cellLst[0][colSize-1]);
		handleCorner(cellLst[rowSize-1][0]);
		handleCorner(cellLst[rowSize-1][colSize-1]);
		
		/*
		 * For coloring the cells to check whether its inside or outside the grid
		 */
		colorBorderCells();
		colorZeroCells();
		int n = 1000;
		while(n > 0){
			//if( ds.findSet(getIndex(4, 0)) == 8)
				//break;
			colorOneAdj();			
			colorTwoAdj();
			colorThreeAdj();
			colorZeroAdj();
			cellAroundCorner();
			oneCellNotColored();
			--n;
		}
		
		ArrayList<Cell> unColored =  Grid.getUncoloredCells();
		for( i = 0; i < rowSize; ++i, System.out.println())
			for(  j = 0; j < colSize; ++j)
				System.out.print(ds.findSet(getIndex(i, j)) + " ");		
	}
	
	/*
	 * Works like a charm!
	 * To handle the numbers present in the corners of the grid
	 */
	public static void handleCorner(Cell tmpCell){
		int x = tmpCell.getPosition().getX();
		int y = tmpCell.getPosition().getY();
		switch(tmpCell.getNodeVal()){
			case 1:{
				ds.union(0, getIndex(x, y));
				if(x == 0 && y == 0){
					if(!tmpCell.getTopWall().getFixed())
						tmpCell.getTopWall().setFixed(true, false);
					if(!tmpCell.getLeftWall().getFixed())
						tmpCell.getLeftWall().setFixed(true, false);
				}
				if(x == rowSize-1 && y == 0){
					if(!tmpCell.getBottomWall().getFixed())
						tmpCell.getBottomWall().setFixed(true, false);
					if(!tmpCell.getLeftWall().getFixed())
						tmpCell.getLeftWall().setFixed(true, false);
				}
				if(x == rowSize-1 && y == colSize-1){
					if(!tmpCell.getBottomWall().getFixed())
						tmpCell.getBottomWall().setFixed(true, false);
					if(!tmpCell.getRightWall().getFixed())
						tmpCell.getRightWall().setFixed(true, false);
				}
				if(x == 0 && y == colSize-1){
					if(!tmpCell.getTopWall().getFixed())
						tmpCell.getTopWall().setFixed(true, false);
					if(!tmpCell.getRightWall().getFixed())
						tmpCell.getRightWall().setFixed(true, false);
				}
			}
			break;
			case 3:{
				ds.union(1, getIndex(x, y));
				if(x == 0 && y == 0){
					if(!tmpCell.getTopWall().getFixed())
						tmpCell.getTopWall().setFixed(true, true);
					if(!tmpCell.getLeftWall().getFixed())
						tmpCell.getLeftWall().setFixed(true, true);
				}
				if(x == rowSize-1 && y == 0){
					if(!tmpCell.getBottomWall().getFixed())
						tmpCell.getBottomWall().setFixed(true, true);
					if(!tmpCell.getLeftWall().getFixed())
						tmpCell.getLeftWall().setFixed(true, true);
				}
				if(x == rowSize-1 && y == colSize-1){
					if(!tmpCell.getBottomWall().getFixed())
						tmpCell.getBottomWall().setFixed(true, true);
					if(!tmpCell.getRightWall().getFixed())
						tmpCell.getRightWall().setFixed(true, true);
				}
				if(x == 0 && y == colSize-1){
					if(!tmpCell.getTopWall().getFixed())
						tmpCell.getTopWall().setFixed(true, true);
					if(!tmpCell.getRightWall().getFixed())
						tmpCell.getRightWall().setFixed(true, true);
				}
			}
			break;
			case 2:{
				Cell[][] tmpCellList = Grid.cellLst;
				if(x == 0 && y == 0){
					if(!tmpCellList[1][0].getLeftWall().getFixed())
						tmpCellList[1][0].getLeftWall().setFixed(true, true);
					if(!tmpCellList[0][1].getTopWall().getFixed())
						tmpCellList[0][1].getTopWall().setFixed(true, true);
				}
				if(x == rowSize-1 && y == 0){
					if(!tmpCellList[x-1][0].getLeftWall().getFixed())
						tmpCell.getLeftWall().setFixed(true, true);
					if(!tmpCellList[x][1].getBottomWall().getFixed())
						tmpCellList[x][1].getBottomWall().setFixed(true, true);
				}
				if(x == rowSize-1 && y == colSize-1){
					if(!tmpCellList[x][y-1].getBottomWall().getFixed())
						tmpCellList[x][y-1].getBottomWall().setFixed(true, true);
					if(!tmpCellList[x-1][y].getRightWall().getFixed())
						tmpCellList[x-1][y].getRightWall().setFixed(true, true);
				}
				if(x == 0 && y == colSize-1){
					if(!tmpCellList[x][y-1].getTopWall().getFixed())
						tmpCellList[x][y-1].getTopWall().setFixed(true, true);
					if(!tmpCellList[x+1][y].getRightWall().getFixed())
						tmpCellList[x+1][y].getRightWall().setFixed(true, true);
				}
			}
			
		}
	}
	
	/*
	 * Works!
	 * Coloring Cells if the border Wall is set Alive, with color '2'
	 */
	private static void colorBorderCells(){
		int i;
		for( i = 0 ; i < colSize; ++i){
			if(cellLst[0][i].getTopWall().getIsActive() && cellLst[0][i].getCellColor() == 0){
				cellLst[0][i].setCellColor(2, true);
				ds.union(1, getIndex(0, i));
			}
			if(cellLst[rowSize-1][i].getBottomWall().getIsActive() && cellLst[rowSize-1][i].getCellColor() == 0){
				cellLst[rowSize-1][i].setCellColor(2, true);
				ds.union(1, getIndex(rowSize-1, i));
			}
		}
		for( i = 0; i < rowSize; ++i){
			if(cellLst[i][0].getLeftWall().getIsActive() && cellLst[i][0].getCellColor() == 0){
				cellLst[i][0].setCellColor(2, true);
				ds.union(1, getIndex(i, 0));
			}
			if(cellLst[i][colSize-1].getRightWall().getIsActive() && cellLst[i][colSize-1].getCellColor() == 0){
				cellLst[i][colSize-1].setCellColor(2, true);
				ds.union(1, getIndex(i, colSize-1));
			}
		}
	}
	
	/*
	 * Works!
	 * Draw line between in border, if the Cell comes in 'Inside' part 
	 */
	
	private static void colorBorderLines(){
		int i;
		for( i = 0 ; i < colSize; ++i){
			if(cellLst[0][i].getCellColor() > 1 && !cellLst[0][i].getTopWall().getIsActive()){
				cellLst[0][i].getTopWall().setFixed(true, true);
				ds.union(1, getIndex(0, i));
			}
			if(cellLst[rowSize-1][i].getCellColor() > 1 && !cellLst[rowSize-1][i].getBottomWall().getIsActive()){
				cellLst[rowSize-1][i].getBottomWall().setFixed(true, true);
				ds.union(1, getIndex(rowSize-1, i));
			}
		}
		for( i = 0; i < rowSize; ++i){
			if(cellLst[i][0].getCellColor() > 1 && !cellLst[i][0].getLeftWall().getIsActive() ){
				cellLst[i][0].getLeftWall().setFixed(true, true);
				ds.union(1, getIndex(i, 0));
			}
			if(cellLst[i][colSize-1].getCellColor() > 1 && !cellLst[i][colSize-1].getRightWall().getIsActive()){
				cellLst[i][colSize-1].getRightWall().setFixed(true, true);
				ds.union(1, getIndex(i, colSize-1));
			}
		}
	}
	/*
	 * Works!
	 * Adding Complimentary colors, every time a line is crossed 
	 */
	private static void colorLineSep(){
		int i, j, color;
		Cell tmpCell;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				tmpCell = cellLst[i][j];
				if(tmpCell.getCellColor() != 0){
					color = tmpCell.getCellColor() != 1 ? 1 : 2;
					if(checkBounds(i-1, j) && tmpCell.getTopWall().getIsActive() && cellLst[i-1][j].getCellColor() == 0)
						cellLst[i-1][j].setCellColor(color, true);
					if(checkBounds(i+1, j) && tmpCell.getBottomWall().getIsActive() && cellLst[i+1][j].getCellColor() == 0)
						cellLst[i+1][j].setCellColor(color, true);
					if(checkBounds(i, j+1) && tmpCell.getRightWall().getIsActive() && cellLst[i][j+1].getCellColor() == 0)
						cellLst[i][j+1].setCellColor(color, true);
					if(checkBounds(i, j-1) && tmpCell.getLeftWall().getIsActive() && cellLst[i][j-1].getCellColor() == 0)
						cellLst[i][j-1].setCellColor(color, true);
				}
			}
		}
	}
	
	/*
	 * Works!
	 * To draw Lines between cells with different colors
	 */
	private static void drawLineColorSep(){
		int i, j, color;
		Cell tmpCell;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j ){
				tmpCell = cellLst[i][j];
				color = tmpCell.getCellColor();
				if( color != 0){
					if(checkBounds(i-1, j) && cellLst[i-1][j].getCellColor() != 0 && !tmpCell.getTopWall().getIsActive())
						tmpCell.getTopWall().setFixed(true, cellLst[i-1][j].getCellColor() != color );
					if(checkBounds(i+1, j) && cellLst[i+1][j].getCellColor() != 0 && !tmpCell.getBottomWall().getIsActive())
						tmpCell.getBottomWall().setFixed(true, cellLst[i+1][j].getCellColor() != color );
					if(checkBounds(i, j-1) && cellLst[i][j-1].getCellColor() != 0 && !tmpCell.getLeftWall().getIsActive())
						tmpCell.getLeftWall().setFixed(true, cellLst[i][j-1].getCellColor() != color);
					if(checkBounds(i, j+1) && cellLst[i][j+1].getCellColor() != 0 && !tmpCell.getRightWall().getIsActive())
						tmpCell.getRightWall().setFixed(true, cellLst[i][j+1].getCellColor() != color );
				}
			}
		}
	}
	
	/*
	 * Works!
	 * Coloring the '0' cells present in the border along with its neighbors, with color '1' 
	 */
	private static void colorZeroCells(){
		int i;
		for( i = 0; i < colSize; ++i){
			if(cellLst[0][i].getNodeVal() == 0 && cellLst[0][i].getCellColor() == 0){
				cellLst[0][i].setCellColor(1, true);
				cellLst[1][i].setCellColor(1, true);
				if(checkBounds(0, i+1) && cellLst[0][i+1].getCellColor() == 0)
					cellLst[0][i+1].setCellColor(1, true);
				if(checkBounds(0, i-1) && cellLst[0][i-1].getCellColor() == 0)
					cellLst[0][i-1].setCellColor(1, true);
			}
			if(cellLst[rowSize-1][i].getNodeVal() == 0 && cellLst[rowSize-1][i].getCellColor() == 0){
				cellLst[rowSize-1][i].setCellColor(1, true);
				cellLst[rowSize-2][i].setCellColor(1, true);
				if(checkBounds(rowSize-1, i-1) && cellLst[rowSize-1][i-1].getCellColor() == 0)
					cellLst[rowSize-1][i-1].setCellColor(1, true);
				if(checkBounds(rowSize-1, i+1) && cellLst[rowSize-1][i+1].getCellColor() == 0)
					cellLst[rowSize-1][i+1].setCellColor(1, true);
			}
		}
		
		for( i = 1; i < rowSize-1; ++i){
			if(cellLst[i][0].getNodeVal() == 0 && cellLst[i][0].getCellColor() == 0){
				cellLst[i][0].setCellColor(1, true);
				cellLst[i][1].setCellColor(1, true);
				if(checkBounds(i+1, 0) && cellLst[i+1][0].getCellColor() == 0)
					cellLst[i+1][0].setCellColor(1);
				if(checkBounds(i-1, 0) && cellLst[i-1][0].getCellColor() == 0)
					cellLst[i-1][0].setCellColor(1);
			}
			if(cellLst[i][colSize-1].getNodeVal() == 0 && cellLst[i][colSize-1].getCellColor() == 0){
				cellLst[i][colSize-1].setCellColor(1, true);
				cellLst[i][colSize-2].setCellColor(1, true);
				if(checkBounds(i-1, colSize-1) && cellLst[i-1][colSize-1].getCellColor() == 0)
					cellLst[i-1][colSize-1].setCellColor(1);
				if(checkBounds(i+1, colSize-1) && cellLst[i+1][colSize-1].getCellColor() == 0)
					cellLst[i+1][colSize-1].setCellColor(1);
			}
		}
		colorLineSep();
		drawLineColorSep();
		colorBorderLines();
	}
	
	/*
	 * Works!
	 * check for '2' cell with 2 adjacent cells having same/different colors
	 */
	private static void colorTwoAdj(){
		int i, j, color;
		Cell tmpCell;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				List<Cell> notColored = new ArrayList<Cell>();
				List<Cell> Color1 = new ArrayList<Cell>();
				List<Cell> Color2 = new ArrayList<Cell>();
				tmpCell = cellLst[i][j];
				color = tmpCell.getCellColor();
				if(tmpCell.getNodeVal() == 2){
					oneTwoThreeSubFunction(i-1, j, Color1, Color2, notColored, tmpCell);
					oneTwoThreeSubFunction(i+1, j, Color1, Color2, notColored, tmpCell);
					oneTwoThreeSubFunction(i, j-1, Color1, Color2, notColored, tmpCell);
					oneTwoThreeSubFunction(i, j+1, Color1, Color2, notColored, tmpCell);
					if(Color1.size() == 2){
						for( Cell k : notColored)
							k.setCellColor(2, true);
					}
					else if(Color2.size() == 2){
						for( Cell k : notColored)
							k.setCellColor(1, true);
					}
					if(color == 1){
						for( Cell k : Color1)
							ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
						for( Cell k : notColored)
							if(k.getCellColor() == 1)
								ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
					}
					else if(color == 2){
						for( Cell k : Color2)
							ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
						for( Cell k : notColored)
							if(k.getCellColor() == 2)
								ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
					}
				}
			}
		}
		colorLineSep();
		drawLineColorSep();
		colorBorderLines();
	}
	
	/*
	 * Works!
	 */
	private static void colorOneAdj(){
		int i, j, color;
		Cell tmpCell;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				tmpCell = cellLst[i][j];
				color = tmpCell.getCellColor();
				if(tmpCell.getNodeVal() == 1){
					List<Cell> Color1 = new ArrayList<Cell>();
					List<Cell> Color2 = new ArrayList<Cell>();
					List<Cell> notColored = new ArrayList<Cell>();
					oneTwoThreeSubFunction(i+1, j, Color1, Color2, notColored, tmpCell);
					oneTwoThreeSubFunction(i-1, j, Color1, Color2, notColored, tmpCell);
					oneTwoThreeSubFunction(i, j-1, Color1, Color2, notColored, tmpCell);
					oneTwoThreeSubFunction(i, j+1, Color1, Color2, notColored, tmpCell);
					if(color == 0 ){
						if(Color1.size() > 1)
							tmpCell.setCellColor(1);
						else if(Color2.size() > 1)
							tmpCell.setCellColor(2);
						color = tmpCell.getCellColor();
					}
					if(color == 1){
						if(Color2.size() > 0){
							for( Cell k : notColored)
								k.setCellColor(1, true);
						}
						else if(Color1.size() == 3){
							for( Cell k : notColored)
								k.setCellColor(2, true);
						}
					}
					else if(color == 2){
						if(Color1.size() > 0){
							for( Cell k : notColored)
								k.setCellColor(2, true);
						}
						else if(Color2.size() == 3){
							for( Cell k : notColored)
								k.setCellColor(1, true);
						}
					}
					if(color == 1){
						for( Cell k : Color1)
							ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
						for( Cell k : notColored)
							if(k.getCellColor() == 1)
								ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
					}
					else if(color == 2){
						for( Cell k : Color2)
							ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
						for( Cell k : notColored)
							if(k.getCellColor() == 2)
								ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
					}					
				}
			}
		}
		colorLineSep();
		drawLineColorSep();
		colorBorderLines();
	}
	
	/*
	 * works!
	 */
	private static void colorThreeAdj(){
		int i, j, color;
		Cell tmpCell;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				tmpCell = cellLst[i][j];
				color = tmpCell.getCellColor();
				if(tmpCell.getNodeVal() == 3){
					List<Cell> Color1 = new ArrayList<Cell>();
					List<Cell> Color2 = new ArrayList<Cell>();
					List<Cell> notColored = new ArrayList<Cell>();
					oneTwoThreeSubFunction(i+1, j, Color1, Color2, notColored, tmpCell);
					oneTwoThreeSubFunction(i-1, j, Color1, Color2, notColored, tmpCell);
					oneTwoThreeSubFunction(i, j-1, Color1, Color2, notColored, tmpCell);
					oneTwoThreeSubFunction(i, j+1, Color1, Color2, notColored, tmpCell);
					if(color == 0 ){
						if(Color1.size() > 1)
							tmpCell.setCellColor(2);
						else if(Color2.size() > 1)
							tmpCell.setCellColor(1);
						color = tmpCell.getCellColor();
					}				
					if(color == 1){
						if(Color1.size() > 0){
							for( Cell k : notColored)
								k.setCellColor(2, true);
						}
						else if(Color2.size() == 3)
							for( Cell k : notColored)
								k.setCellColor(1, true);
					}
					else if(color == 2){
						if(Color2.size() > 0){
							for( Cell k : notColored)
								k.setCellColor(1, true);
						}
						else if(Color1.size() == 3)
							for( Cell k : notColored)
								k.setCellColor(2, true);
					}
					if(color == 1){
						for( Cell k : Color1)
							ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
						for( Cell k : notColored)
							if(k.getCellColor() == 1)
								ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
					}
					else if(color == 2){
						for( Cell k : Color2)
							ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
						for( Cell k : notColored)
							if(k.getCellColor() == 2)
								ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
					}
				}
			}
		}
		colorLineSep();
		drawLineColorSep();
		colorBorderLines();
	}
	
	private static void oneTwoThreeSubFunction(int i, int j, List<Cell> Color1, List<Cell> Color2, List<Cell> notColored, Cell tmpCell){
		if(!checkBounds(i, j))
			Color1.add(outerCell);
		else if(cellLst[i][j].getCellColor() == 1)
			Color1.add(cellLst[i][j]);
		else if(cellLst[i][j].getCellColor() == 2)
			Color2.add(cellLst[i][j]);
		else
			notColored.add(cellLst[i][j]);

	}
	
	/*
	 * Works!
	 */
	private static void cellAroundCorner(){
		int i, j, color;
		Cell tmpCell;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				tmpCell = cellLst[i][j];
				color = tmpCell.getCellColor();
				if(checkBounds(i+1, j+1) && tmpCell.getRightWall().getIsActive() && tmpCell.getBottomWall().getIsActive())
					cellAroundCornerSubFunction(i+1, j+1, tmpCell, color);
				if(checkBounds(i+1, j-1) && tmpCell.getLeftWall().getIsActive() && tmpCell.getBottomWall().getIsActive())
					cellAroundCornerSubFunction(i+1, j-1, tmpCell, color);
				if(checkBounds(i-1, j+1) && tmpCell.getRightWall().getIsActive() && tmpCell.getTopWall().getIsActive())
					cellAroundCornerSubFunction(i-1, j+1, tmpCell, color);
				if(checkBounds(i-1, j-1) && tmpCell.getLeftWall().getIsActive() && tmpCell.getTopWall().getIsActive())
					cellAroundCornerSubFunction(i-1, j-1, tmpCell, color);
			}
		}
		colorLineSep();
		drawLineColorSep();
		colorBorderLines();
	}
	
	private static void cellAroundCornerSubFunction(int i, int j, Cell tmpCell, int color){
		if(color != 0){
			if(cellLst[i][j].getCellColor() == 0)
				cellLst[i][j].setCellColor(color == 1 ? 2 : 1, true);
		}
		else{
			if(cellLst[i][j].getCellColor() != 0)
				tmpCell.setCellColor(cellLst[i][j].getCellColor() == 1 ? 2 : 1, true);
		}
	}
	
	private static void oneCellNotColored(){
		int i, j, color;
		Cell tmpCell;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				tmpCell = cellLst[i][j];
				color = tmpCell.getCellColor();
				if(tmpCell.getNodeVal() == 1){
					List<Cell> Color1 = new ArrayList<Cell>();
					List<Cell> Color2 = new ArrayList<Cell>();
					List<Cell> notColored = new ArrayList<Cell>();
					oneTwoThreeSubFunction(i+1, j, Color1, Color2, notColored, tmpCell);
					oneTwoThreeSubFunction(i-1, j, Color1, Color2, notColored, tmpCell);
					oneTwoThreeSubFunction(i, j-1, Color1, Color2, notColored, tmpCell);
					oneTwoThreeSubFunction(i, j+1, Color1, Color2, notColored, tmpCell);
					if(color == 0 && Color1.size() < 2 && Color2.size() < 2){
						if(checkBounds(i-1, j) && tmpCell.getTopWall().getIsActive() && cellLst[i-1][j].getCellColor() == 0)
							tmpCell.setCellColor(Color1.size()==0 ? 2 : 1, true);
						else if(checkBounds(i+1,j) && tmpCell.getBottomWall().getIsActive() && cellLst[i+1][j].getCellColor() == 0)
							tmpCell.setCellColor(Color1.size()==0 ? 2 : 1, true);
						else if(checkBounds(i,j+1) && tmpCell.getRightWall().getIsActive() && cellLst[i][j+1].getCellColor() == 0)
							tmpCell.setCellColor(Color1.size()==0 ? 2 : 1, true);
						else if(checkBounds(i,j-1) && tmpCell.getLeftWall().getIsActive() && cellLst[i][j-1].getCellColor() == 0)
							tmpCell.setCellColor(Color1.size()==0 ? 2 : 1, true);						
					}
				}
			}
		}
		colorLineSep();
		drawLineColorSep();
		colorBorderLines();
	}
	
	private static void colorZeroAdj(){
		int i, j, color;
		Cell tmpCell;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				tmpCell = cellLst[i][j];
				color = tmpCell.getCellColor();
				if(tmpCell.getNodeVal() == 0){
					if(color != 0){
						zeroSubFunction(i+1, j, color);
						zeroSubFunction(i-1, j, color);
						zeroSubFunction(i, j+1, color);
						zeroSubFunction(i, j-1, color);
					}
					else{
						List<Cell> Color1 = new ArrayList<Cell>();
						List<Cell> Color2 = new ArrayList<Cell>();
						List<Cell> notColored = new ArrayList<Cell>();
						oneTwoThreeSubFunction(i+1, j, Color1, Color2, notColored, tmpCell);
						oneTwoThreeSubFunction(i-1, j, Color1, Color2, notColored, tmpCell);
						oneTwoThreeSubFunction(i, j-1, Color1, Color2, notColored, tmpCell);
						oneTwoThreeSubFunction(i, j+1, Color1, Color2, notColored, tmpCell);
						if(Color1.size() > 0){
							tmpCell.setCellColor(1, true);
							for( Cell k : notColored)
								k.setCellColor(1, true);
						}
						else if( Color2.size() > 0){
							tmpCell.setCellColor(2, true);
							for( Cell k : notColored)
								k.setCellColor(2, true);
						}
					}
				}
			}
		}
	}
	
	private static void zeroSubFunction(int i, int j, int color){
		if(checkBounds(i, j) && cellLst[i][j].getCellColor() == 0)
			cellLst[i][j].setCellColor(color, true);
	}
	
	private static void connectAdj(){
		int i, j, color;
		Cell tmpCell;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				tmpCell = cellLst[i][j];
				color = tmpCell.getCellColor();
				if(color != 0){
					if(checkBounds(i+1, j) && cellLst[i+1][j].getCellColor() == color){
						
					}
				}
			}
		}
	}
}
