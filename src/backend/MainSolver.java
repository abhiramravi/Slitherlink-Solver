package backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import datastructure.Cell;
import datastructure.Coordinate;
import datastructure.DisjointSet;
import datastructure.Grid;
import datastructure.Wall;

public class MainSolver {
	private static int rowSize, colSize;
	private static Cell[][] cellLst;
	private static Cell outerCell = new Cell(-1);
	private static DisjointSet ds;
	private static int level;
	private static int[][] posArr;
	
	public static boolean checkBounds(int x, int y){
		return ( x < 0 || y < 0 || x >= rowSize || y >= colSize) ? false :  true;
	}
	
	public static int getIndex(int x, int y){
		return checkBounds(x,y) ? ( x*rowSize + y + 2 ) : 0;
	}
	
	private static Cell[][] getCellLstCopy(Cell[][] cellArr){
		int i, j;
		Cell[][] copy = new Cell[rowSize][colSize];
		for( i = 0; i < rowSize; ++i)
			for( j = 0; j < colSize; ++j)
				copy[i][j] = cellArr[i][j].getCopy();
		return copy;
	}
	
	public static void basicSolver(){
		/* 
		 * Initialization of the static variables
		 */
		ds = Grid.ds;
		cellLst = Grid.cellLst;
		outerCell.setCellColor(1, true);
		rowSize = Grid.getRows();
		colSize = Grid.getColumns();
		posArr = new int[rowSize+1][colSize+1];
		level = 0;
		
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
					ds.union( getIndex(i, j), getIndex(i+1, j));
					ds.union( getIndex(i, j), getIndex(i-1, j));
					ds.union( getIndex(i, j), getIndex(i, j+1));
					ds.union( getIndex(i, j), getIndex(i, j-1));
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
		
		/*
		 * For coloring the cells to check whether its inside or outside the grid
		 */
		colorBorderCells();
		colorZeroCells();
		int n = 0;
		boolean isCorrect = true, isSolved = false;
		boolean flag = true, tmpflag;
		while(flag){
			flag = colorOneAdj();
			tmpflag = colorTwoAdj();
			flag = flag ? flag : tmpflag;
			tmpflag = colorThreeAdj();
			flag = flag ? flag : tmpflag;
			tmpflag = colorZeroAdj();
			flag = flag ? flag : tmpflag;
			tmpflag = cellAroundCorner();
			flag = flag ? flag : tmpflag;
			//tmpflag = oneCellNotColored();
			//flag = flag ? flag : tmpflag;
			tmpflag = connectAdjSets();
			flag = flag ? flag : tmpflag;
			if(!CheckBoardStateCorrect()){
				System.out.println("SOMETHING IS WRONG WITH THE CONSTRUCTION AND BASE CASE " + n);
				isCorrect = false;
				break;
			}
			if(isGameOver()){
				System.out.println("GAME OVRE after " + n + " BASE CASE steps");
				isSolved = true;
				break;
			}
			++n;
//			if(n%500 == 0)
//				System.out.println(n);
		}
		if(!isSolved && isCorrect){
			System.out.println("BASE CASE DONE IN " + n + " STEPS");
			/*
			 * Degbuggine print statements
			 */
			for( i = 0; i < rowSize; ++i, System.out.println())
				for(  j = 0; j < colSize; ++j)
					System.out.print(ds.findSet(getIndex(i, j)) + " ");
			
			System.out.println("Calling BackTrack");
			
			if(backTrack()){
				System.out.println("Solution Found");
				System.out.println("BACK TRACK DONE AFTER VISITING LEAVES "+ level + " TIMES");
				Grid.isSolved = true;
				Grid.cellLst = getCellLstCopy(cellLst);
			}
			else{
				System.out.println("Something is wrong");
				Grid.isSolved = false;
			}
			
			/*
			 * Debugging print statements
			 */
			for( i = 0; i < rowSize; ++i, System.out.println())
				for(  j = 0; j < colSize; ++j)
					System.out.print(ds.findSet(getIndex(i, j)) + " ");
			for( i = 0; i < rowSize; ++i, System.out.println())
				for(  j = 0; j < colSize; ++j)
					System.out.print(cellLst[i][j].getCellColor() + " ");
		}
		
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
				//ds.union(1, getIndex(x, y));
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
				if(x == 0 && y == 0){
					if(!cellLst[1][0].getLeftWall().getFixed())
						cellLst[1][0].getLeftWall().setFixed(true, true);
					if(!cellLst[0][1].getTopWall().getFixed())
						cellLst[0][1].getTopWall().setFixed(true, true);
				}
				if(x == rowSize-1 && y == 0){
					if(!cellLst[x-1][0].getLeftWall().getFixed())
						cellLst[x-1][0].getLeftWall().setFixed(true, true);
					if(!cellLst[x][1].getBottomWall().getFixed())
						cellLst[x][1].getBottomWall().setFixed(true, true);
				}
				if(x == rowSize-1 && y == colSize-1){
					if(!cellLst[x][y-1].getBottomWall().getFixed())
						cellLst[x][y-1].getBottomWall().setFixed(true, true);
					if(!cellLst[x-1][y].getRightWall().getFixed())
						cellLst[x-1][y].getRightWall().setFixed(true, true);
				}
				if(x == 0 && y == colSize-1){
					if(!cellLst[x][y-1].getTopWall().getFixed())
						cellLst[x][y-1].getTopWall().setFixed(true, true);
					if(!cellLst[x+1][y].getRightWall().getFixed())
						cellLst[x+1][y].getRightWall().setFixed(true, true);
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
			if(cellLst[0][i].getTopWall().getIsActive() && cellLst[0][i].getCellColor() == 0)
				cellLst[0][i].setCellColor(2, true);
			if(cellLst[rowSize-1][i].getBottomWall().getIsActive() && cellLst[rowSize-1][i].getCellColor() == 0)
				cellLst[rowSize-1][i].setCellColor(2, true);
		}
		for( i = 0; i < rowSize; ++i){
			if(cellLst[i][0].getLeftWall().getIsActive() && cellLst[i][0].getCellColor() == 0)
				cellLst[i][0].setCellColor(2, true);
			if(cellLst[i][colSize-1].getRightWall().getIsActive() && cellLst[i][colSize-1].getCellColor() == 0)
				cellLst[i][colSize-1].setCellColor(2, true);
		}
	}
	
	/*
	 * Works!
	 * Draw line between in border, if the Cell comes in 'Inside' part 
	 */
	
	private static boolean colorBorderLines(){
		int i;
		boolean to_ret = false;
		for( i = 0 ; i < colSize; ++i){
			if(cellLst[0][i].getCellColor() > 1 && !cellLst[0][i].getTopWall().getFixed()){
				cellLst[0][i].getTopWall().setFixed(true, true);
				to_ret = true;
			}
			if(cellLst[rowSize-1][i].getCellColor() > 1 && !cellLst[rowSize-1][i].getBottomWall().getFixed()){
				cellLst[rowSize-1][i].getBottomWall().setFixed(true, true);
				to_ret = true;
			}
		}
		for( i = 0; i < rowSize; ++i){
			if(cellLst[i][0].getCellColor() > 1 && !cellLst[i][0].getLeftWall().getFixed()){
				cellLst[i][0].getLeftWall().setFixed(true, true);
				to_ret = true;
			}
			if(cellLst[i][colSize-1].getCellColor() > 1 && !cellLst[i][colSize-1].getRightWall().getFixed()){
				cellLst[i][colSize-1].getRightWall().setFixed(true, true);
				to_ret = true;
			}
		}
		return to_ret;
	}
	/*
	 * Works!
	 * Adding Complimentary colors, every time a line is crossed 
	 */
	private static boolean colorLineSep(){
		int i, j, color;
		Cell tmpCell;
		boolean to_ret = false;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				tmpCell = cellLst[i][j];
				if(tmpCell.getCellColor() != 0){
					color = tmpCell.getCellColor() != 1 ? 1 : 2;
					if(checkBounds(i-1, j) && tmpCell.getTopWall().getIsActive() && cellLst[i-1][j].getCellColor() == 0 && ds.findSet(getIndex(i, j)) != ds.findSet(getIndex(i-1, j))){
						cellLst[i-1][j].setCellColor(color, true);
						to_ret = true;
					}
					if(checkBounds(i+1, j) && tmpCell.getBottomWall().getIsActive() && cellLst[i+1][j].getCellColor() == 0 && ds.findSet(getIndex(i, j)) != ds.findSet(getIndex(i+1, j))){
						cellLst[i+1][j].setCellColor(color, true);
						to_ret = true;
					}
					if(checkBounds(i, j+1) && tmpCell.getRightWall().getIsActive() && cellLst[i][j+1].getCellColor() == 0 && ds.findSet(getIndex(i, j)) != ds.findSet(getIndex(i, j+1))){
						cellLst[i][j+1].setCellColor(color, true);
						to_ret = true;
					}
					if(checkBounds(i, j-1) && tmpCell.getLeftWall().getIsActive() && cellLst[i][j-1].getCellColor() == 0 && ds.findSet(getIndex(i, j)) != ds.findSet(getIndex(i, j-1))){
						cellLst[i][j-1].setCellColor(color, true);
						to_ret = true;
					}
				}
			}
		}
		return to_ret;
	}
	
	/*
	 * Works!
	 * To draw Lines between cells with different colors
	 */
	private static boolean drawLineColorSep(){
		int i, j, color;
		Cell tmpCell;
		boolean to_ret = false;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j ){
				tmpCell = cellLst[i][j];
				color = tmpCell.getCellColor();
				if( color != 0){
					if(checkBounds(i-1, j) && cellLst[i-1][j].getCellColor() != 0 && !tmpCell.getTopWall().getFixed() ){
						tmpCell.getTopWall().setFixed(true, cellLst[i-1][j].getCellColor() != color );
						to_ret = true;
					}
					if(checkBounds(i+1, j) && cellLst[i+1][j].getCellColor() != 0 && !tmpCell.getBottomWall().getFixed()){
						tmpCell.getBottomWall().setFixed(true, cellLst[i+1][j].getCellColor() != color );
						to_ret = true;
					}
					if(checkBounds(i, j-1) && cellLst[i][j-1].getCellColor() != 0 && !tmpCell.getLeftWall().getFixed()){
						tmpCell.getLeftWall().setFixed(true, cellLst[i][j-1].getCellColor() != color);
						to_ret = true;
					}
					if(checkBounds(i, j+1) && cellLst[i][j+1].getCellColor() != 0 && !tmpCell.getRightWall().getFixed()){
						tmpCell.getRightWall().setFixed(true, cellLst[i][j+1].getCellColor() != color );
						to_ret = true;
					}
				}
			}
		}
		return to_ret;
	}
	
	/*
	 * Works!
	 * Coloring the '0' cells present in the border along with its neighbours, with color '1' 
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
	private static boolean colorTwoAdj(){
		int i, j, color;
		Cell tmpCell;
		boolean to_ret = false;
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
						for( Cell k : notColored){
							k.setCellColor(2, true);
							to_ret = true;
						}
					}
					else if(Color2.size() == 2){
						for( Cell k : notColored){
							k.setCellColor(1, true);
							to_ret = true;
						}
					}
					if(color == 1){
						for( Cell k : Color1){
							if( ds.findSet(getIndex(i, j)) != ds.findSet(getIndex(k.getPosition().getX(), k.getPosition().getY()))){
								ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
								to_ret = true;
							}
						}
						for( Cell k : notColored)
							if(k.getCellColor() == 1 && ds.findSet(getIndex(i, j)) != ds.findSet(getIndex(k.getPosition().getX(), k.getPosition().getY())) ){
								ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
								to_ret = true;
							}
					}
					else if(color == 2){
						for( Cell k : Color2){
							if( ds.findSet(getIndex(i, j)) != ds.findSet(getIndex(k.getPosition().getX(), k.getPosition().getY())) ){
								ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
								to_ret = true;
							}
						}
						for( Cell k : notColored)
							if(k.getCellColor() == 2 && ds.findSet(getIndex(i, j)) != ds.findSet(getIndex(k.getPosition().getX(), k.getPosition().getY())) ){
								ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
								to_ret = true;
							}
					}
				}
			}
		}
		boolean tmp = colorLineSep();
		to_ret = to_ret ? to_ret : tmp;
		tmp = drawLineColorSep();
		to_ret = to_ret ? to_ret : tmp;
		tmp = colorBorderLines();
		to_ret = to_ret ? to_ret : tmp;
		return to_ret;
	}
	
	/*
	 * Works!
	 */
	private static boolean colorOneAdj(){
		int i, j, color;
		boolean to_ret = false;
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
						if(Color1.size() > 1){
							tmpCell.setCellColor(1, true);
							to_ret = true;
						}
						else if(Color2.size() > 1){
							tmpCell.setCellColor(2, true);
							to_ret = true;
						}
						color = tmpCell.getCellColor();
					}
					if(color == 1){
						if(Color2.size() > 0){
							for( Cell k : notColored){
								k.setCellColor(1, true);
								to_ret = true;
							}
						}	
						else if(Color1.size() == 3){
							for( Cell k : notColored){
								k.setCellColor(2, true);
								to_ret = true;
							}
						}
					}
					else if(color == 2){
						if(Color1.size() > 0){
							for( Cell k : notColored){
								k.setCellColor(2, true);
								to_ret = true;
							}
						}
						else if(Color2.size() == 3){
							for( Cell k : notColored){
								k.setCellColor(1, true);
								to_ret = true;
							}
						}
					}
					if(color == 1){
						for( Cell k : Color1){
							if(ds.findSet(getIndex(i, j)) != ds.findSet(getIndex(k.getPosition().getX(), k.getPosition().getY())) ){
								ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
								to_ret = true;
							}
						}
						for( Cell k : notColored)
							if(k.getCellColor() == 1 && ds.findSet(getIndex(i, j)) != ds.findSet(getIndex(k.getPosition().getX(), k.getPosition().getY())) ){
								ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
								to_ret = true;
							}
					}
					else if(color == 2){
						for( Cell k : Color2){
							if(ds.findSet(getIndex(i, j)) != ds.findSet(getIndex(k.getPosition().getX(), k.getPosition().getY())) ){
								ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
								to_ret = true;
							}
						}
						for( Cell k : notColored)
							if(k.getCellColor() == 2 && ds.findSet(getIndex(i, j)) != ds.findSet(getIndex(k.getPosition().getX(), k.getPosition().getY())) ){
								ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
								to_ret = true;
							}
					}					
				}
			}
		}
		boolean tmp = colorLineSep();
		to_ret = to_ret ? to_ret : tmp;
		tmp = drawLineColorSep();
		to_ret = to_ret ? to_ret : tmp;
		tmp = colorBorderLines();
		to_ret = to_ret ? to_ret : tmp;
		return to_ret;
	}
	
	/*
	 * works!
	 */
	private static boolean colorThreeAdj(){
		int i, j, color;
		Cell tmpCell;
		boolean to_ret = false;
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
						if(Color1.size() > 1){
							tmpCell.setCellColor(2, true);
							to_ret = true;
						}
						else if(Color2.size() > 1){
							tmpCell.setCellColor(1, true);
							to_ret = true;
						}
						color = tmpCell.getCellColor();
					}				
					if(color == 1){
						if(Color1.size() > 0){
							for( Cell k : notColored){
								k.setCellColor(2, true);
								to_ret = true;
							}
						}
						else if(Color2.size() == 3)
							for( Cell k : notColored){
								k.setCellColor(1, true);
								to_ret = true;
							}
					}
					else if(color == 2){
						if(Color2.size() > 0){
							for( Cell k : notColored){
								k.setCellColor(1, true);
								to_ret = true;
							}
						}
						else if(Color1.size() == 3)
							for( Cell k : notColored){
								k.setCellColor(2, true);
								to_ret = true;
							}
					}
					if(color == 1){
						for( Cell k : Color1)
							if(ds.findSet(getIndex(i, j)) != ds.findSet(getIndex(k.getPosition().getX(), k.getPosition().getY())) ){
								ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
								to_ret = true;
							}
						for( Cell k : notColored)
							if(k.getCellColor() == 1 && ds.findSet(getIndex(i, j)) != ds.findSet(getIndex(k.getPosition().getX(), k.getPosition().getY())) ){
								ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
								to_ret =true;
							}
					}
					else if(color == 2){
						for( Cell k : Color2)
							if(ds.findSet(getIndex(i, j)) != ds.findSet(getIndex(k.getPosition().getX(), k.getPosition().getY())) ){
								ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
								to_ret =true;
							}
						for( Cell k : notColored)
							if(k.getCellColor() == 2 && ds.findSet(getIndex(i, j)) != ds.findSet( getIndex(k.getPosition().getX(), k.getPosition().getY())) ){
								ds.union(getIndex(i, j), getIndex(k.getPosition().getX(), k.getPosition().getY()));
								to_ret = true;
							}
					}
				}
			}
		}
		boolean tmp = colorLineSep();
		to_ret = to_ret ? to_ret : tmp;
		tmp = drawLineColorSep();
		to_ret = to_ret ? to_ret : tmp;
		tmp = colorBorderLines();
		to_ret = to_ret ? to_ret : tmp;
		return to_ret;
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
	private static boolean cellAroundCorner(){
		int i, j, color;
		Cell tmpCell;
		boolean to_ret = false, tmp;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				tmpCell = cellLst[i][j];
				color = tmpCell.getCellColor();
				if(checkBounds(i+1, j+1) && tmpCell.getRightWall().getIsActive() && tmpCell.getBottomWall().getIsActive()){
					tmp = cellAroundCornerSubFunction(i+1, j+1, tmpCell, color);
					color = tmpCell.getCellColor();
					to_ret = to_ret ? to_ret : tmp;
				}
				if(checkBounds(i+1, j-1) && tmpCell.getLeftWall().getIsActive() && tmpCell.getBottomWall().getIsActive()){
					tmp = cellAroundCornerSubFunction(i+1, j-1, tmpCell, color);
					color = tmpCell.getCellColor();
					to_ret = to_ret ? to_ret : tmp;
				}
				if(checkBounds(i-1, j+1) && tmpCell.getRightWall().getIsActive() && tmpCell.getTopWall().getIsActive()){
					tmp = cellAroundCornerSubFunction(i-1, j+1, tmpCell, color);
					color = tmpCell.getCellColor();
					to_ret = to_ret ? to_ret : tmp;
				}
				if(checkBounds(i-1, j-1) && tmpCell.getLeftWall().getIsActive() && tmpCell.getTopWall().getIsActive()){
					tmp = cellAroundCornerSubFunction(i-1, j-1, tmpCell, color);
					color = tmpCell.getCellColor();
					to_ret = to_ret ? to_ret : tmp;
				}
			}
		}
		tmp = colorLineSep();
		to_ret = to_ret ? to_ret : tmp;
		tmp = drawLineColorSep();
		to_ret = to_ret ? to_ret : tmp;
		tmp = colorBorderLines();
		to_ret = to_ret ? to_ret : tmp;
		return to_ret;
	}
	
	private static boolean cellAroundCornerSubFunction(int i, int j, Cell tmpCell, int color){
		boolean to_ret = false;
		if(color != 0){
			if(cellLst[i][j].getCellColor() == 0){
				cellLst[i][j].setCellColor(color == 1 ? 2 : 1, true);
				to_ret = true;
			}
		}
		else{
			if(cellLst[i][j].getCellColor() != 0){
				tmpCell.setCellColor(cellLst[i][j].getCellColor() == 1 ? 2 : 1, true);
				to_ret = true;
			}
		}
		return to_ret;
	}
	
	/*Function not working
	 * Have to fix this! ( But the Solver works fine w/o this )
	 */
	/*
	private static boolean oneCellNotColored(){
		int i, j, color;
		Cell tmpCell;
		boolean to_ret = false;
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
						if(checkBounds(i-1, j) && tmpCell.getTopWall().getIsActive() && cellLst[i-1][j].getCellColor() == 0){
							tmpCell.setCellColor(Color1.size()==0 ? 2 : 1, true);
							to_ret = true;
						}
						else if(checkBounds(i+1,j) && tmpCell.getBottomWall().getIsActive() && cellLst[i+1][j].getCellColor() == 0){
							tmpCell.setCellColor(Color1.size()==0 ? 2 : 1, true);
							to_ret = true;
						}
						else if(checkBounds(i,j+1) && tmpCell.getRightWall().getIsActive() && cellLst[i][j+1].getCellColor() == 0){
							tmpCell.setCellColor(Color1.size()==0 ? 2 : 1, true);
							to_ret = true;
						}
						else if(checkBounds(i,j-1) && tmpCell.getLeftWall().getIsActive() && cellLst[i][j-1].getCellColor() == 0){
							tmpCell.setCellColor(Color1.size()==0 ? 2 : 1, true);						
							to_ret = true;
						}
					}
				}
			}
		}
		boolean tmp = colorLineSep();
		to_ret = to_ret ? to_ret : tmp;
		tmp = drawLineColorSep();
		to_ret = to_ret ? to_ret : tmp;
		tmp = colorBorderLines();
		to_ret = to_ret ? to_ret : tmp;
		return to_ret;
	}
	*/
	private static boolean colorZeroAdj(){
		int i, j, color;
		Cell tmpCell;
		boolean to_ret = false, tmp;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				tmpCell = cellLst[i][j];
				color = tmpCell.getCellColor();
				if(tmpCell.getNodeVal() == 0){
					if(color != 0){
						tmp = zeroSubFunction(i+1, j, color);
						to_ret = to_ret ? to_ret : tmp;
						tmp = zeroSubFunction(i-1, j, color);
						to_ret = to_ret ? to_ret : tmp;
						tmp = zeroSubFunction(i, j+1, color);
						to_ret = to_ret ? to_ret : tmp;
						tmp = zeroSubFunction(i, j-1, color);
						to_ret = to_ret ? to_ret : tmp;
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
							to_ret = true;
							tmpCell.setCellColor(1, true);
							for( Cell k : notColored)
								k.setCellColor(1, true);
						}
						else if( Color2.size() > 0){
							to_ret = true;
							tmpCell.setCellColor(2, true);
							for( Cell k : notColored)
								k.setCellColor(2, true);
						}
					}
				}
			}
		}
		tmp = colorLineSep();
		to_ret = to_ret ? to_ret : tmp;
		tmp = drawLineColorSep();
		to_ret = to_ret ? to_ret : tmp;
		tmp = colorBorderLines();
		to_ret = to_ret ? to_ret : tmp;
		return to_ret;
	}
	
	private static boolean zeroSubFunction(int i, int j, int color){
		if(checkBounds(i, j) && cellLst[i][j].getCellColor() == 0){
			cellLst[i][j].setCellColor(color, true);
			return true;
		}
		return false;
	}
	
	private static boolean connectAdjSets(){
		int i, j, color, parent;
		Cell tmpCell;
		boolean to_ret = false, tmp;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				tmpCell = cellLst[i][j];
				color = tmpCell.getCellColor();
				if(color != 0){
					if(( (checkBounds(i+1, j) && cellLst[i+1][j].getCellColor() == color) || ( !checkBounds(i+1, j) && color == 1)) 
							&& ds.findSet(getIndex(i, j)) != ds.findSet( getIndex(i+1,j))){
						ds.union(getIndex(i, j), getIndex(i+1,j));
						to_ret = true;
					}
					if(( (checkBounds(i-1, j) && cellLst[i-1][j].getCellColor() == color) || ( !checkBounds(i-1, j) && color == 1))
							&& ds.findSet(getIndex(i, j)) != ds.findSet( getIndex(i-1,j))){
						ds.union(getIndex(i, j), getIndex(i-1, j));
						to_ret = true;
					}
					if( ((checkBounds(i, j-1) && cellLst[i][j-1].getCellColor() == color) || ( !checkBounds(i, j-1) && color == 1))
							&& ds.findSet(getIndex(i, j)) != ds.findSet( getIndex(i,j-1))){
						ds.union(getIndex(i, j), getIndex(i, j-1));
						to_ret = true;
					}
					if( ((checkBounds(i, j+1) && cellLst[i][j+1].getCellColor() == color) || ( !checkBounds(i, j+1) && color == 1))
							&& ds.findSet(getIndex(i, j)) != ds.findSet( getIndex(i,j+1))){
						ds.union(getIndex(i, j), getIndex(i, j+1));
						to_ret = true;
					}
				}
			}
		}
		
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				tmpCell = cellLst[i][j];
				color = tmpCell.getCellColor();
				parent = ds.findSet(getIndex(i, j));
				if(color == 0){
					if(ds.findSet(getIndex(i+1, j)) == parent && !(checkBounds(i+1, j) && cellLst[i+1][j].getCellColor() == 0)){
						tmpCell.setCellColor(checkBounds(i+1, j) ? cellLst[i+1][j].getCellColor() : 1, true);
						to_ret = true;
					}
					else if(ds.findSet(getIndex(i-1, j)) == parent  && !(checkBounds(i-1, j) && cellLst[i-1][j].getCellColor() == 0)){
						tmpCell.setCellColor(checkBounds(i-1, j) ? cellLst[i-1][j].getCellColor() : 1, true);
						to_ret = true;
					}
					else if(ds.findSet(getIndex(i, j+1)) == parent && !(checkBounds(i, j+1) && cellLst[i][j+1].getCellColor() == 0)){
						tmpCell.setCellColor(checkBounds(i, j+1) ? cellLst[i][j+1].getCellColor() : 1, true);
						to_ret = true;
					}
					else if(ds.findSet(getIndex(i, j-1)) == parent && !(checkBounds(i, j-1) && cellLst[i][j-1].getCellColor() == 0)){
						tmpCell.setCellColor(checkBounds(i, j-1) ? cellLst[i][j-1].getCellColor() : 1, true);
						to_ret = true;
					}
				}
				color = tmpCell.getCellColor();
				parent = ds.findSet(getIndex(i, j));
				if(color != 0){
					if(checkBounds(i+1, j) && ds.findSet(getIndex(i+1, j)) == parent && cellLst[i+1][j].getCellColor() == 0){
						cellLst[i+1][j].setCellColor(color, true);
						to_ret = true;
					}
					if(checkBounds(i-1, j) && ds.findSet(getIndex(i-1, j)) == parent && cellLst[i-1][j].getCellColor() == 0){
						cellLst[i-1][j].setCellColor(color, true);
						to_ret = true;
					}
					if(checkBounds(i, j+1) && ds.findSet(getIndex(i, j+1)) == parent && cellLst[i][j+1].getCellColor() == 0){
						cellLst[i][j+1].setCellColor(color, true);
						to_ret = true;
					}
					if(checkBounds(i, j-1) && ds.findSet(getIndex(i, j-1)) == parent && cellLst[i][j-1].getCellColor() == 0){
						cellLst[i][j-1].setCellColor(color, true);
						to_ret = true;
					}
				}
			}
		}
		
		tmp = colorLineSep();
		to_ret = to_ret ? to_ret : tmp;
		tmp = drawLineColorSep();
		to_ret = to_ret ? to_ret : tmp;
		tmp = colorBorderLines();
		to_ret = to_ret ? to_ret : tmp;
		return to_ret;
	}
	
	private static boolean CheckBoardStateCorrect(){
		int i, j, color, parent;
		Cell tmpCell;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				tmpCell = cellLst[i][j];
				color = tmpCell.getCellColor();
				parent = ds.findSet(getIndex(i, j));
				if(color != 0){
					if(checkBounds(i+1, j)){
						if(tmpCell.getBottomWall().getIsActive() && (cellLst[i+1][j].getCellColor() == color || parent == ds.findSet(getIndex(i+1,j)) ))
							return false;
					}
					else if(color == 1 && tmpCell.getBottomWall().getIsActive() )
						return false;
					if(checkBounds(i-1, j)){
						if(tmpCell.getTopWall().getIsActive() && (cellLst[i-1][j].getCellColor() == color || parent == ds.findSet(getIndex(i-1,j) )))
							return false;
					}
					else if(color == 1 && tmpCell.getTopWall().getIsActive())
						return false;
					if(checkBounds(i, j+1)){
						if(tmpCell.getRightWall().getIsActive() && (cellLst[i][j+1].getCellColor() == color || parent == ds.findSet(getIndex(i,j+1) )))
							return false;
					}
					else if(color == 1 && tmpCell.getRightWall().getIsActive() )
						return false;
					if(checkBounds(i, j-1)){
						if(tmpCell.getLeftWall().getIsActive() && ( cellLst[i][j-1].getCellColor() == color || parent == ds.findSet(getIndex(i,j-1) )))
							return false;
					}
					else if(color == 1 && tmpCell.getLeftWall().getIsActive())
						return false;
				}
				if(tmpCell.getNodeVal() != -1 && tmpCell.getNodeVal() < tmpCell.getActiveWalls())
					return false;
			}
		}
		
		/*
		 * For checking whether any point has morethan 2 lines incident
		 */
		for( i = 0; i <= rowSize; ++i)
			for( j = 0; j <= colSize; ++j)
				posArr[i][j] = 0;
		
		Vector<Wall> tmpWallLst = Grid.getAllWalls(cellLst);
		for( Wall w : tmpWallLst){
			if(w.getIsActive()){
				++posArr[w.getWallStart().getX()][w.getWallStart().getY()];
				++posArr[w.getWallEnd().getX()][w.getWallEnd().getY()];
			}
		}
		
		for( i = 0; i <= rowSize; ++i)
			for( j = 0; j <= colSize; ++j)
				if(posArr[i][j] > 2)
					return false;
		return true;
	}
	
	private static boolean isGameOver(){
		int i, j, insParent = 1, tmp, outParent = 0;
		boolean setFlag = true, setFlag1 = true;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				if(cellLst[i][j].getNodeVal() != -1 && cellLst[i][j].getNodeVal() != cellLst[i][j].getActiveWalls())
					return false;
				if( setFlag && cellLst[i][j].getCellColor() == 2 ){
					insParent = ds.findSet(getIndex(i, j));
					setFlag = false;
				}
				if( setFlag1 && cellLst[i][j].getCellColor() == 1){
					outParent = ds.findSet(getIndex(i, j));
					setFlag1 = false;
				}
				tmp = ds.findSet(getIndex(i, j));
				if(cellLst[i][j].getCellColor() < 1 || (tmp != insParent && tmp != outParent))
					return false;
			}
		}
		for( i = 0; i <= rowSize; ++i)
			for( j = 0; j <= colSize; ++j)
				posArr[i][j] = 0;
		
		Vector<Wall> tmpWallLst = Grid.getAllWalls(cellLst);
		for( Wall w : tmpWallLst)
			if(w.getIsActive()){
				++posArr[w.getWallStart().getX()][w.getWallStart().getY()];
				++posArr[w.getWallEnd().getX()][w.getWallEnd().getY()];
			}
		
		for( i = 0; i <= rowSize; ++i)
			for( j = 0; j <= colSize; ++j)
				if(posArr[i][j] > 2)
					return false;
		return true;
	}
	
	private static boolean backTrack(){
		boolean flag = true, tmpflag;
		while(flag){
			if(!CheckBoardStateCorrect())
				return false;
			flag = colorOneAdj();
			tmpflag = colorTwoAdj();
			flag = flag ? flag : tmpflag;
			tmpflag = colorThreeAdj();
			flag = flag ? flag : tmpflag;
			tmpflag = colorZeroAdj();
			flag = flag ? flag : tmpflag;
			tmpflag = cellAroundCorner();
			flag = flag ? flag : tmpflag;
//			tmpflag = oneCellNotColored();
//			flag = flag ? flag : tmpflag;
			tmpflag = connectAdjSets();
			flag = flag ? flag : tmpflag;
			if(!CheckBoardStateCorrect())
				return false;
			if(isGameOver())
				return true;
		}
		Coordinate emptyCellCoordinate = getFirstNonColored3();
		if(emptyCellCoordinate == null){
			++level;
			if(level%1000 == 0)
				System.out.println(level);
			return false;
		}
		Cell[][] cellcopy = getCellLstCopy(cellLst);
		DisjointSet dscopy = ds.getCopy();
		cellLst[emptyCellCoordinate.getX()][emptyCellCoordinate.getY()].setCellColor(1, true);
		if(backTrack())
			return true;
		ds = dscopy.getCopy();
		cellLst = getCellLstCopy(cellcopy);
		cellLst[emptyCellCoordinate.getX()][emptyCellCoordinate.getY()].setCellColor(2, true);
		return backTrack();
	}
	
	/*
	 * Max Adjacent Cells Coloured
	 */
	/*
	private static Coordinate getFirstNonColored1(){
		int i, j, count = -1, tmp;
		Coordinate pos = null;
		for( i = 0; i < rowSize; ++i)
			for( j = 0; j < colSize; ++j){
				if(cellLst[i][j].getCellColor() == 0){
					tmp = cellLst[i][j].noAdjColored(cellLst) ; 
					if( tmp > count){
						pos = cellLst[i][j].getPosition();
						count = tmp;
					}
				}
			}
		return pos;
	}
	*/
	/*
	 * Naive First Non Coloured Cell Selection
	 */
	/*
	private static Coordinate getFirstNonColored2(){
		int i, j;
		for( i = 0; i < rowSize; ++i)
			for( j = 0; j < colSize; ++j)
				if(cellLst[i][j].getCellColor() == 0)
					return cellLst[i][j].getPosition();
		return null;
	}
	*/
	
	/*
	 * Max Node Val
	 */
	private static Coordinate getFirstNonColored3(){
		int i, j, value = -2;
		Coordinate pos = null;
		for( i = 0; i < rowSize; ++i)
			for( j = 0; j < colSize; ++j){
				if(cellLst[i][j].getCellColor() == 0 && cellLst[i][j].getNodeVal() > value){
					pos = cellLst[i][j].getPosition();
					value = cellLst[i][j].getNodeVal();
				}
			}
		return pos;
	}
	
	/*
	 * Max Active Walls
	 */
	/*
	private static Coordinate getFirstNonColored4(){
		int i, j, value = -1;
		Coordinate pos = null;
		for( i = 0; i < rowSize; ++i)
			for( j = 0; j < colSize; ++j){
				if(cellLst[i][j].getCellColor() == 0 && cellLst[i][j].getActiveWalls() > value){
					pos = cellLst[i][j].getPosition();
					value = cellLst[i][j].getActiveWalls();
				}
			}
		return pos;
	}
	*/
	
	/*
	 * Heuristic FIRST + THIRD
	 */
	/*
	private static Coordinate getFirstNonColored5(){
		int i, j, value = -2, count = -1, tmp;
		Coordinate pos = null;
		for( i = 0; i < rowSize; ++i)
			for( j = 0; j < colSize; ++j){
				if(cellLst[i][j].getCellColor() == 0 && cellLst[i][j].getNodeVal() >= value){
					tmp = cellLst[i][j].noAdjColored(cellLst) ; 
					if(value == cellLst[i][j].getNodeVal()){
						if( tmp > count){
							pos = cellLst[i][j].getPosition();
							value = cellLst[i][j].getNodeVal();
							count = tmp;
						}
					}
					else{
						pos = cellLst[i][j].getPosition();
						value = cellLst[i][j].getNodeVal();
						count = tmp;
					}
				}
			}
		return pos;
	}
	*/
}
