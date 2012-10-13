package backend;

import datastructure.Cell;
import datastructure.Grid;

public class MainSolver {
	private static int rowSize, colSize;
	private static Cell[][] cellLst = Grid.cellLst;
	
	
	private static boolean checkBounds(int x, int y){
		return ( x < 0 || y < 0 || x >= rowSize || y >= colSize) ? false :  true;
	}
	
	public static void basicSolver(){
		rowSize = Grid.getRows();
		colSize = Grid.getColumns();
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
				//tmpCell.setCellColor(1);
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
		ColorCells();
	}
	
	private static void ColorCells(){
		int i, j;
		/*
		 * Coloring Cells if the is a border Wall is set Alive, with color '2'
		 */
		for( i = 0 ; i < colSize; ++i){
			if(cellLst[0][i].getTopWall().getIsActive() && !cellLst[0][i].getIsColored())
				cellLst[0][i].setCellColor(2, true);
			if(cellLst[rowSize-1][i].getBottomWall().getIsActive() && !cellLst[rowSize-1][i].getIsColored())
				cellLst[rowSize-1][i].setCellColor(2, true);
		}
		for( i = 1; i < rowSize-1; ++i){
			if(cellLst[i][0].getLeftWall().getIsActive() && !cellLst[i][0].getIsColored())
				cellLst[i][0].setCellColor(2, true);
			if(cellLst[i][colSize-1].getRightWall().getIsActive() && !cellLst[i][colSize-1].getIsColored())
				cellLst[i][colSize-1].setCellColor(2, true);
		}
		
		/*
		 * Coloring the '0' cells present in the border along with its neighbors with color '1' 
		 */
		for( i = 0; i < colSize; ++i){
			if(cellLst[0][i].getNodeVal() == 0 && !cellLst[0][i].getIsColored()){
				cellLst[0][i].setCellColor(1, true);
				cellLst[1][i].setCellColor(1);
				if(checkBounds(0, i+1))
					cellLst[0][i+1].setCellColor(1);
				if(checkBounds(0, i-1))
					cellLst[0][i-1].setCellColor(1);
			}
			if(cellLst[rowSize-1][i].getNodeVal() == 0 && !cellLst[rowSize-1][i].getIsColored()){
				cellLst[rowSize-1][i].setCellColor(1, true);
				cellLst[rowSize-2][i].setCellColor(1);
				if(checkBounds(rowSize-1, i-1))
					cellLst[rowSize-1][i-1].setCellColor(1);
				if(checkBounds(rowSize-1, i+1))
					cellLst[rowSize-1][i+1].setCellColor(1);
			}
		}
		
		for( i = 1; i < rowSize-1; ++i){
			if(cellLst[i][0].getNodeVal() == 0 && !cellLst[i][0].getIsColored()){
				cellLst[i][0].setCellColor(1, true);
				cellLst[i][1].setCellColor(1);
				if(checkBounds(i+1, 0))
					cellLst[i+1][0].setCellColor(1);
				if(checkBounds(i-1, 0))
					cellLst[i-1][0].setCellColor(1);
			}
			if(cellLst[i][colSize-1].getNodeVal() == 0 && !cellLst[i][colSize-1].getIsColored()){
				cellLst[i][colSize-1].setCellColor(1, true);
				cellLst[i][colSize-2].setCellColor(1);
				if(checkBounds(i-1, colSize-1))
					cellLst[i-1][colSize-1].setCellColor(1);
				if(checkBounds(i+1, colSize-1))
					cellLst[i+1][colSize-1].setCellColor(1);
			}
		}
		
		/*
		 * Changing the color every time a line is crossed 
		 */
		Cell tmpCell;
		int color;
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
				else{
					if(checkBounds(i-1, j) && cellLst[i-1][j].getCellColor() != 0 && cellLst[i-1][j].getTopWall().getIsActive())
						tmpCell.setCellColor(cellLst[i-1][j].getCellColor(), true);
					else if(checkBounds(i+1, j) && cellLst[i+1][j].getCellColor() != 0 && cellLst[i+1][j].getTopWall().getIsActive())
						tmpCell.setCellColor(cellLst[i+1][j].getCellColor(), true);
					else if(checkBounds(i, j-1) && cellLst[i][j-1].getCellColor() != 0 && cellLst[i][j-1].getTopWall().getIsActive())
						tmpCell.setCellColor(cellLst[i][j-1].getCellColor(), true);
					else if(checkBounds(i, j+1) && cellLst[i][j+1].getCellColor() != 0 && cellLst[i][j+1].getTopWall().getIsActive())
						tmpCell.setCellColor(cellLst[i][j+1].getCellColor(), true);
				}
			}
		}
	}
}
