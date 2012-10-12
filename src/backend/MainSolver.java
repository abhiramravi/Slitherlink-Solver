package backend;

import datastructure.*;

public class MainSolver {
	public Grid gameGrid;
	private int rowSize, colSize;
	
	public MainSolver(Grid g){
		this.gameGrid = g;
		rowSize = g.getRows();
		colSize = g.getColumns();
	}
	
	private boolean checkBounds(int x, int y){
		return ( x < 0 || y < 0 || x >= rowSize || y >= colSize) ? false :  true;
	}
	
	public void basicSolver(){
		Cell[][] cellLst = Grid.cellLst;
		Cell tmpCell;
		int i, j;
		
		/*
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
						if(checkBounds(i, j-1) && !tmpCell.getBottomWall().getFixed())
							tmpCell.getBottomWall().setFixed(true, true);
						if(checkBounds(i, j+1) && !tmpCell.getBottomWall().getFixed())
							tmpCell.getBottomWall().setFixed(true, true);
					}
					if(checkBounds(i-1, j) && cellLst[i][j].getNodeVal() == 3){
						tmpCell = cellLst[i-1][j];
						if(!tmpCell.getTopWall().getFixed())
							tmpCell.getTopWall().setFixed(true, true);
						if(!tmpCell.getLeftWall().getFixed())
							tmpCell.getLeftWall().setFixed(true, true);
						if(!tmpCell.getRightWall().getFixed())
							tmpCell.getRightWall().setFixed(true, true);
						if(checkBounds(i, j-1) && !cellLst[i][j-1].getTopWall().getFixed())
							tmpCell.getTopWall().setFixed(true, true);
						if(checkBounds(i, j+1) && !cellLst[i][j+1].getTopWall().getFixed())
							tmpCell.getTopWall().setFixed(true, true);
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
							tmpCell.getLeftWall().setFixed(true, true);
						if(checkBounds(i-1, j) && !cellLst[i-1][j].getLeftWall().getFixed())
							tmpCell.getLeftWall().setFixed(true, true);
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
							tmpCell.getRightWall().setFixed(true, true);
						if(checkBounds(i-1, j) && !cellLst[i-1][j].getRightWall().getFixed())
							tmpCell.getRightWall().setFixed(true, true);
					}			
				}
			}
		}
		
		/*
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
	
	public void handleCorner(Cell tmpCell){
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
				Cell[][] tmpCellList = gameGrid.cellLst;
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

}
