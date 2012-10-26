package backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import datastructure.Cell;
import datastructure.Coordinate;
import datastructure.DisjointSet;
import datastructure.Grid;
import datastructure.MoveObj;
import datastructure.Wall;

/**
 * This class implements the Basic Solver of the SlitherLink given the Variables initialised by the Grid Class.
 * The idea of 'Parity' is used to efficiently colour the Cells and hence Walls.
 */

public class MainSolver {
	/**
	 * Local Copy of the Game Grid variables
	 */
	private static int rowSize, colSize;
	private static Cell[][] cellLst;
	private static DisjointSet ds;
	private static ArrayList<MoveObj> allMoveLst;
	
	/**
	 * @val outerCell: All the outer cells form one single clique hence represented by one common Cell.
	 * @val level: The number of leaves visited by the Back Tracking Solver ( Not necessarily the number of the back Tracks done by the Heuristic )
	 */
	private static Cell outerCell = new Cell(-1);
	private static int level;
	
	
	private static int[][] posArr;
	private static int MaxAllowedMoves = (int)Math.pow(10, 5);
	
	public static void basicSolver(){
		
		/**
		 * Initialisation of the static variables
		 */
		rowSize = Grid.getRows();
		colSize = Grid.getColumns();
		ds = Grid.ds;
		cellLst = Grid.cellLst;
		allMoveLst = Grid.allMoveLst;
		
		outerCell.setCellColor(1, true);
		level = 0;
		
		posArr = new int[rowSize+1][colSize+1];
		
		/**
		 * Bases Case Solving Functions
		 * These functions colour only the Cell Walls
		 * O(rowSize*colsSize) for each function call
		 */
		HandleZeroCell();
		HandleAdjZeroThree();
		HandleDiaZeroThree();
		HandleAdjThree();
		HandleDiaThree();
		
		/**
		 * Solver Functions to Handle values present in the corner Cells
		 * O(1) for each function call
		 */
		handleCorner(cellLst[0][0]);
		handleCorner(cellLst[0][colSize-1]);
		handleCorner(cellLst[rowSize-1][0]);
		handleCorner(cellLst[rowSize-1][colSize-1]);
		
		/**
		 * Bases Case Solvers to colour the Cells using the Walls coloured as separators
		 * O(rowSize*colsSize) for each function call
		 */
		colorBorderCells();
		colorZeroCells();
		
		/**
		 * Temporary variables used for solving the Bases Cases iteratively
		 */
		int n = 0, i, j;
		boolean isCorrect = true, isSolved = false, flag = true, tmpflag;
		
		/**
		 * Iteratively solving the Bases Cases until all the cases fail to make at-least one successful move
		 */
		while(flag && n <= MaxAllowedMoves){
			
			flag = colorOneAdj();
			
			tmpflag = colorTwoAdj();
			flag = flag ? flag : tmpflag;
			
			tmpflag = colorThreeAdj();
			flag = flag ? flag : tmpflag;
			
			tmpflag = colorZeroAdj();
			flag = flag ? flag : tmpflag;
			
			tmpflag = cellAroundCorner();
			flag = flag ? flag : tmpflag;
			
			tmpflag = connectAdjSets();
			flag = flag ? flag : tmpflag;
			
			if(!CheckBoardStateCorrect()){
				System.out.println("SOMETHING IS WRONG WITH THE CONSTRUCTION AND BASE CASE " + n);
				isCorrect = false;
				break;
			}
			
			if(isGameOver()){
				System.out.println("GAME OVER after " + n + " BASE CASE steps");
				Grid.cellLst = getCellLstCopy(cellLst);
				isSolved = true;
				break;
			}
			
			++n;
			
			/**
			 * Printing counter variable
			 */
			if(n%500 == 0)
				System.out.println(n);
		}
		
		/**
		 * If the Game is not solved by the Bases Case Iteration, Back Tracking is used
		 */
		if(!isSolved && isCorrect){
			
			System.out.println("BASE CASE DONE IN " + n + " STEPS");
			
			/**
			 * Debugging print statements
			 */
			for( i = 0; i < rowSize; ++i, System.out.println())
				for(  j = 0; j < colSize; ++j)
					System.out.print(ds.findSet(getIndex(i, j)) + " ");
			
			System.out.println("Calling BackTrack");
			
			if(backTrack()){
				System.out.println("Solution Found");
				System.out.println("BACK TRACK DONE AFTER VISITING THE LEAVES "+ level + " TIMES");
				System.out.println("TOTAL NUMBER NUMBER OF MOVES MADE : "+ allMoveLst.size());
				Grid.isSolved = true;
				Grid.cellLst = getCellLstCopy(cellLst);
			}
			else{
				System.out.println("SOMETHING IS WRONG WITH THE INPUT OR HEURISTIC");
				Grid.isSolved = false;
			}
			
			/**
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
	
	/**
	 * Function Checks for Nodes with value 0 in the Game Grid
	 * This Sets all the four Walls of '0' Cell to 'Fixed' and 'Not Active'
	 */
	private static void HandleZeroCell(){
		Cell tmpCell;
		int i, j;
		
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				tmpCell = cellLst[i][j];
				if(tmpCell.getNodeVal() == 0){
					if(!tmpCell.getLeftWall().getFixed()){
						tmpCell.getLeftWall().setFixed(true, false);
						ds.union( getIndex(i, j), getIndex(i+1, j));
					}
					if(!tmpCell.getRightWall().getFixed()){
						tmpCell.getRightWall().setFixed(true, false);
						ds.union( getIndex(i, j), getIndex(i-1, j));
					}
					if(!tmpCell.getBottomWall().getFixed()){
						tmpCell.getBottomWall().setFixed(true, false);
						ds.union( getIndex(i, j), getIndex(i, j+1));
					}
					if(!tmpCell.getTopWall().getFixed()){
						tmpCell.getTopWall().setFixed(true, false);
						ds.union( getIndex(i, j), getIndex(i, j-1));
					}
				}
			}
		}
	}
	
	/**
	 * Function Checks for the adjacent Cells with values 0 and 3
	 * This Sets all the edges of the '3' Cell other than the common edge to 'Fixed' and 'Active'.
	 */
	private static void HandleAdjZeroThree(){
		Cell tmpCell;
		int i, j;
		
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				if(cellLst[i][j].getNodeVal() == 0){
					if(checkBounds(i+1,j) && cellLst[i+1][j].getNodeVal() == 3){
						tmpCell = cellLst[i+1][j];
						if(!tmpCell.getBottomWall().getFixed()){
							tmpCell.getBottomWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(tmpCell.getBottomWall()));
						}
						if(!tmpCell.getLeftWall().getFixed()){
							tmpCell.getLeftWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(tmpCell.getLeftWall()));
						}
						if(!tmpCell.getRightWall().getFixed()){
							tmpCell.getRightWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(tmpCell.getRightWall()));
						}
						if(checkBounds(i, j-1) && !cellLst[i][j-1].getBottomWall().getFixed()){
							cellLst[i][j-1].getBottomWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(cellLst[i][j-1].getBottomWall()));
						}
						if(checkBounds(i, j+1) && !cellLst[i][j+1].getBottomWall().getFixed()){
							cellLst[i][j+1].getBottomWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(cellLst[i][j+1].getBottomWall()));
						}
					}
					if(checkBounds(i-1, j) && cellLst[i-1][j].getNodeVal() == 3){
						tmpCell = cellLst[i-1][j];
						if(!tmpCell.getTopWall().getFixed()){
							tmpCell.getTopWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(tmpCell.getTopWall()));
						}
						if(!tmpCell.getLeftWall().getFixed()){
							tmpCell.getLeftWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(tmpCell.getLeftWall()));
						}
						if(!tmpCell.getRightWall().getFixed()){
							tmpCell.getRightWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(tmpCell.getRightWall()));
						}
						if(checkBounds(i, j-1) && !cellLst[i][j-1].getTopWall().getFixed()){
							cellLst[i][j-1].getTopWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(cellLst[i][j-1].getTopWall()));
						}
						if(checkBounds(i, j+1) && !cellLst[i][j+1].getTopWall().getFixed()){
							cellLst[i][j+1].getTopWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(cellLst[i][j+1].getTopWall()));
						}
					}
					if(checkBounds(i, j-1) && cellLst[i][j-1].getNodeVal() == 3){
						tmpCell = cellLst[i][j-1];
						if(!tmpCell.getTopWall().getFixed()){
							tmpCell.getTopWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(tmpCell.getTopWall()));
						}
						if(!tmpCell.getLeftWall().getFixed()){
							tmpCell.getLeftWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(tmpCell.getLeftWall()));
						}
						if(!tmpCell.getBottomWall().getFixed()){
							tmpCell.getBottomWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(tmpCell.getBottomWall()));
						}
						if(checkBounds(i+1, j) && !cellLst[i+1][j].getLeftWall().getFixed()){
							cellLst[i+1][j].getLeftWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(cellLst[i+1][j].getLeftWall()));
						}
						if(checkBounds(i-1, j) && !cellLst[i-1][j].getLeftWall().getFixed()){
							cellLst[i-1][j].getLeftWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(cellLst[i-1][j].getLeftWall()));
						}
					}
					if(checkBounds(i, j+1) && cellLst[i][j+1].getNodeVal() == 3){
						tmpCell = cellLst[i][j+1];
						if(!tmpCell.getTopWall().getFixed()){
							tmpCell.getTopWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(tmpCell.getTopWall()));
						}
						if(!tmpCell.getRightWall().getFixed()){
							tmpCell.getRightWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(tmpCell.getRightWall()));
						}
						if(!tmpCell.getBottomWall().getFixed()){
							tmpCell.getBottomWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(tmpCell.getBottomWall()));
						}
						if(checkBounds(i+1, j) && !cellLst[i+1][j].getRightWall().getFixed()){
							cellLst[i+1][j].getRightWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(cellLst[i+1][j].getRightWall()));
						}
						if(checkBounds(i-1, j) && !cellLst[i-1][j].getRightWall().getFixed()){
							cellLst[i-1][j].getRightWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(cellLst[i-1][j].getRightWall()));
						}
					}			
				}
			}
		}
	}
	
	/**
	 * Function Checks for Diagonal Nodes with values 0 and 3
	 * Setting the two edges incident at the common corner ( of 0 and 3 ) to 'Fixed' and 'Active' 
	 */
	private static void HandleDiaZeroThree(){
		Cell tmpCell;
		int i, j;
		
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				if(cellLst[i][j].getNodeVal() == 0){
					if(checkBounds(i+1, j+1) && cellLst[i+1][j+1].getNodeVal() == 3){
						tmpCell = cellLst[i+1][j+1];
						if(!tmpCell.getLeftWall().getFixed()){
							tmpCell.getLeftWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(tmpCell.getLeftWall()));
						}
						if(!tmpCell.getTopWall().getFixed()){
							tmpCell.getTopWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(tmpCell.getTopWall()));
						}
					}
					if(checkBounds(i-1, j-1) && cellLst[i-1][j-1].getNodeVal() == 3){
						tmpCell = cellLst[i-1][j-1];
						if(!tmpCell.getBottomWall().getFixed()){
							tmpCell.getBottomWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(tmpCell.getBottomWall()));
						}
						if(!tmpCell.getRightWall().getFixed()){
							tmpCell.getRightWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(tmpCell.getRightWall()));
						}
					}
					if(checkBounds(i+1, j-1) && cellLst[i+1][j-1].getNodeVal() == 3){
						tmpCell = cellLst[i+1][j-1];
						if(!tmpCell.getRightWall().getFixed()){
							tmpCell.getRightWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(tmpCell.getRightWall()));
						}
						if(!tmpCell.getTopWall().getFixed()){
							tmpCell.getTopWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(tmpCell.getTopWall()));
						}
					}
					if(checkBounds(i-1, j+1) && cellLst[i-1][j+1].getNodeVal() == 3){
						tmpCell = cellLst[i-1][j+1];
						if(!tmpCell.getLeftWall().getFixed()){
							tmpCell.getLeftWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(tmpCell.getLeftWall()));
						}
						if(!tmpCell.getBottomWall().getFixed()){
							tmpCell.getBottomWall().setFixed(true, true);
							allMoveLst.add(new MoveObj(tmpCell.getBottomWall()));
						}
					}
				}
			}
		}
	}
	
	/**
	 * Function Checks for Adjacent Cells with value 3
	 * This sets the common edge and the two parallel edges to common edge, to 'Fixed' and 'Active' 
	 */
	private static void HandleAdjThree(){
		int i, j;
		
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				if(cellLst[i][j].getNodeVal() == 3){
					if(checkBounds(i, j+1) && cellLst[i][j+1].getNodeVal() == 3){
						if(!cellLst[i][j].getLeftWall().getFixed()){
							cellLst[i][j].getLeftWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i][j].getLeftWall()));
						}
						if(!cellLst[i][j].getRightWall().getFixed()){
							cellLst[i][j].getRightWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i][j].getRightWall()));
						}
						if(!cellLst[i][j+1].getRightWall().getFixed()){
							cellLst[i][j+1].getRightWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i][j+1].getRightWall()));
						}
					}
					if(checkBounds(i, j-1) && cellLst[i][j-1].getNodeVal() == 3){
						if(!cellLst[i][j].getLeftWall().getFixed()){
							cellLst[i][j].getLeftWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i][j].getLeftWall()));
						}
						if(!cellLst[i][j].getRightWall().getFixed()){
							cellLst[i][j].getRightWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i][j].getRightWall()));
						}
						if(!cellLst[i][j-1].getLeftWall().getFixed()){
							cellLst[i][j-1].getLeftWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i][j-1].getLeftWall()));
						}
					}
					if(checkBounds(i+1, j) && cellLst[i+1][j].getNodeVal() == 3){
						if(!cellLst[i][j].getTopWall().getFixed()){
							cellLst[i][j].getTopWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i][j].getTopWall()));
						}
						if(!cellLst[i][j].getBottomWall().getFixed()){
							cellLst[i][j].getBottomWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i][j].getBottomWall()));
						}
						if(!cellLst[i+1][j].getBottomWall().getFixed()){
							cellLst[i+1][j].getBottomWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i+1][j].getBottomWall()));
						}
					}
					if(checkBounds(i-1, j) && cellLst[i-1][j].getNodeVal() == 3){
						if(!cellLst[i][j].getBottomWall().getFixed()){
							cellLst[i][j].getBottomWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i][j].getBottomWall()));
						}
						if(!cellLst[i][j].getTopWall().getFixed()){
							cellLst[i][j].getTopWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i][j].getTopWall()));
						}
						if(!cellLst[i-1][j].getTopWall().getFixed()){
							cellLst[i-1][j].getTopWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i-1][j].getTopWall()));
						}
					}
				}
			}
		}
		
	}
	
	/**
	 * Function Checks for Diagonal Nodes with value 3
	 * This sets the outer edges of each Cell to 'Fixed' and 'Active'
	 */
	private static void HandleDiaThree(){
		int i, j;
		
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				if(cellLst[i][j].getNodeVal() == 3){
					if(checkBounds(i+1, j+1) && cellLst[i+1][j+1].getNodeVal() == 3){
						if(!cellLst[i+1][j+1].getBottomWall().getFixed()){
							cellLst[i+1][j+1].getBottomWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i+1][j+1].getBottomWall()));
						}
						if(!cellLst[i+1][j+1].getRightWall().getFixed()){
							cellLst[i+1][j+1].getRightWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i+1][j+1].getRightWall()));
						}
						if(!cellLst[i][j].getTopWall().getFixed()){
							cellLst[i][j].getTopWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i][j].getTopWall()));
						}
						if(!cellLst[i][j].getLeftWall().getFixed()){
							cellLst[i][j].getLeftWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i][j].getLeftWall()));
						}
					}
					if(checkBounds(i-1, j-1) && cellLst[i-1][j-1].getNodeVal() == 3){
						if(!cellLst[i-1][j-1].getTopWall().getFixed()){
							cellLst[i-1][j-1].getTopWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i-1][j-1].getTopWall()));
						}
						if(!cellLst[i-1][j-1].getLeftWall().getFixed()){
							cellLst[i-1][j-1].getLeftWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i-1][j-1].getLeftWall()));
						}
						if(!cellLst[i][j].getBottomWall().getFixed()){
							cellLst[i][j].getBottomWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i][j].getBottomWall()));
						}
						if(!cellLst[i][j].getRightWall().getFixed()){
							cellLst[i][j].getRightWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i][j].getRightWall()));
						}
					}
					if(checkBounds(i+1, j-1) && cellLst[i+1][j-1].getNodeVal() == 3){
						if(!cellLst[i+1][j-1].getBottomWall().getFixed()){
							cellLst[i+1][j-1].getBottomWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i+1][j-1].getBottomWall()));
						}
						if(!cellLst[i+1][j-1].getLeftWall().getFixed()){
							cellLst[i+1][j-1].getLeftWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i+1][j-1].getLeftWall()));
						}
						if(!cellLst[i][j].getTopWall().getFixed()){
							cellLst[i][j].getTopWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i][j].getTopWall()));
						}
						if(!cellLst[i][j].getRightWall().getFixed()){
							cellLst[i][j].getRightWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i][j].getRightWall()));
						}
					}
					if(checkBounds(i-1, j+1) && cellLst[i-1][j+1].getNodeVal() == 3){
						if(!cellLst[i-1][j+1].getTopWall().getFixed()){
							cellLst[i-1][j+1].getTopWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i-1][j+1].getTopWall()));
						}
						if(!cellLst[i-1][j+1].getRightWall().getFixed()){
							cellLst[i-1][j+1].getRightWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i-1][j+1].getRightWall()));
						}
						if(!cellLst[i][j].getBottomWall().getFixed()){
							cellLst[i][j].getBottomWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i][j].getBottomWall()));
						}
						if(!cellLst[i][j].getLeftWall().getFixed()){
							cellLst[i][j].getLeftWall().setFixed(true, true);
							allMoveLst.add( new MoveObj(cellLst[i][j].getLeftWall()));
						}
					}
				}
			}
		}
	}
	
	/**
	 * Function to handle the Corner cells with value other than -1
	 * @param tmpCell: The corner Cell which must be solved for
	 */
	public static void handleCorner(Cell tmpCell){
		int x = tmpCell.getPosition().getX();
		int y = tmpCell.getPosition().getY();
		switch(tmpCell.getNodeVal()){
			case 1:{
				ds.union(0, getIndex(x, y));
				if(x == 0 && y == 0){
					if(!tmpCell.getTopWall().getFixed()){
						tmpCell.getTopWall().setFixed(true, false);
						allMoveLst.add( new MoveObj( tmpCell.getTopWall()));
					}
					if(!tmpCell.getLeftWall().getFixed()){
						tmpCell.getLeftWall().setFixed(true, false);
						allMoveLst.add( new MoveObj( tmpCell.getLeftWall()));
					}
				}
				if(x == rowSize-1 && y == 0){
					if(!tmpCell.getBottomWall().getFixed()){
						tmpCell.getBottomWall().setFixed(true, false);
						allMoveLst.add( new MoveObj( tmpCell.getBottomWall()));
					}
					if(!tmpCell.getLeftWall().getFixed()){
						tmpCell.getLeftWall().setFixed(true, false);
						allMoveLst.add( new MoveObj( tmpCell.getLeftWall()));
					}
				}
				if(x == rowSize-1 && y == colSize-1){
					if(!tmpCell.getBottomWall().getFixed()){
						tmpCell.getBottomWall().setFixed(true, false);
						allMoveLst.add( new MoveObj( tmpCell.getBottomWall()));
					}
					if(!tmpCell.getRightWall().getFixed()){
						tmpCell.getRightWall().setFixed(true, false);
						allMoveLst.add( new MoveObj( tmpCell.getRightWall()));
					}
				}
				if(x == 0 && y == colSize-1){
					if(!tmpCell.getTopWall().getFixed()){
						tmpCell.getTopWall().setFixed(true, false);
						allMoveLst.add( new MoveObj( tmpCell.getTopWall()));
					}
					if(!tmpCell.getRightWall().getFixed()){
						tmpCell.getRightWall().setFixed(true, false);
						allMoveLst.add( new MoveObj( tmpCell.getRightWall()));
					}
				}
			}
			break;
			case 3:{
				if(x == 0 && y == 0){
					if(!tmpCell.getTopWall().getFixed()){
						tmpCell.getTopWall().setFixed(true, true);
						allMoveLst.add( new MoveObj( tmpCell.getTopWall()));
					}
					if(!tmpCell.getLeftWall().getFixed()){
						tmpCell.getLeftWall().setFixed(true, true);
						allMoveLst.add( new MoveObj( tmpCell.getLeftWall()));
					}
				}
				if(x == rowSize-1 && y == 0){
					if(!tmpCell.getBottomWall().getFixed()){
						tmpCell.getBottomWall().setFixed(true, true);
						allMoveLst.add( new MoveObj( tmpCell.getBottomWall()));
					}
					if(!tmpCell.getLeftWall().getFixed()){
						tmpCell.getLeftWall().setFixed(true, true);
						allMoveLst.add( new MoveObj( tmpCell.getLeftWall()));
					}
				}
				if(x == rowSize-1 && y == colSize-1){
					if(!tmpCell.getBottomWall().getFixed()){
						tmpCell.getBottomWall().setFixed(true, true);
						allMoveLst.add( new MoveObj( tmpCell.getBottomWall()));
					}
					if(!tmpCell.getRightWall().getFixed()){
						tmpCell.getRightWall().setFixed(true, true);
						allMoveLst.add( new MoveObj( tmpCell.getRightWall()));
					}
				}
				if(x == 0 && y == colSize-1){
					if(!tmpCell.getTopWall().getFixed()){
						tmpCell.getTopWall().setFixed(true, true);
						allMoveLst.add( new MoveObj( tmpCell.getTopWall()));
					}
					if(!tmpCell.getRightWall().getFixed()){
						tmpCell.getRightWall().setFixed(true, true);
						allMoveLst.add( new MoveObj( tmpCell.getRightWall()));
					}
				}
			}
			break;
			case 2:{
				if(x == 0 && y == 0){
					if(!cellLst[1][0].getLeftWall().getFixed()){
						cellLst[1][0].getLeftWall().setFixed(true, true);
						allMoveLst.add( new MoveObj( cellLst[1][0].getLeftWall()));
					}
					if(!cellLst[0][1].getTopWall().getFixed()){
						cellLst[0][1].getTopWall().setFixed(true, true);
						allMoveLst.add( new MoveObj( cellLst[0][1].getTopWall()));
					}
				}
				if(x == rowSize-1 && y == 0){
					if(!cellLst[x-1][0].getLeftWall().getFixed()){
						cellLst[x-1][0].getLeftWall().setFixed(true, true);
						allMoveLst.add( new MoveObj( cellLst[x-1][0].getLeftWall()));
					}
					if(!cellLst[x][1].getBottomWall().getFixed()){
						cellLst[x][1].getBottomWall().setFixed(true, true);
						allMoveLst.add( new MoveObj( cellLst[x][1].getBottomWall()));
					}
				}
				if(x == rowSize-1 && y == colSize-1){
					if(!cellLst[x][y-1].getBottomWall().getFixed()){
						cellLst[x][y-1].getBottomWall().setFixed(true, true);
						allMoveLst.add( new MoveObj( cellLst[x][y-1].getBottomWall()));
					}
					if(!cellLst[x-1][y].getRightWall().getFixed()){
						cellLst[x-1][y].getRightWall().setFixed(true, true);
						allMoveLst.add( new MoveObj( cellLst[x-1][y].getRightWall()));
					}
				}
				if(x == 0 && y == colSize-1){
					if(!cellLst[x][y-1].getTopWall().getFixed()){
						cellLst[x][y-1].getTopWall().setFixed(true, true);
						allMoveLst.add( new MoveObj( cellLst[x][y-1].getTopWall()));
					}
					if(!cellLst[x+1][y].getRightWall().getFixed()){
						cellLst[x+1][y].getRightWall().setFixed(true, true);
						allMoveLst.add( new MoveObj( cellLst[x+1][y].getRightWall()));
					}
				}
			}
			
		}
	}
	
	/**
	 * Function colours the border Cells with colour '2' if the its border Wall is Active
	 */
	private static void colorBorderCells(){
		int i;
		for( i = 0 ; i < colSize; ++i){
			if(cellLst[0][i].getTopWall().getIsActive() && cellLst[0][i].getCellColor() == 0){
				cellLst[0][i].setCellColor(2, true);
				allMoveLst.add( new MoveObj( cellLst[0][i]));
			}
			if(cellLst[rowSize-1][i].getBottomWall().getIsActive() && cellLst[rowSize-1][i].getCellColor() == 0){
				cellLst[rowSize-1][i].setCellColor(2, true);
				allMoveLst.add( new MoveObj( cellLst[rowSize-1][i]));
			}
		}
		for( i = 0; i < rowSize; ++i){
			if(cellLst[i][0].getLeftWall().getIsActive() && cellLst[i][0].getCellColor() == 0){
				cellLst[i][0].setCellColor(2, true);
				allMoveLst.add( new MoveObj( cellLst[i][0]));
			}
			if(cellLst[i][colSize-1].getRightWall().getIsActive() && cellLst[i][colSize-1].getCellColor() == 0){
				cellLst[i][colSize-1].setCellColor(2, true);
				allMoveLst.add( new MoveObj( cellLst[i][colSize-1]));
			}
		}
	}
	
	/**
	 * Function sets the Border Wall 'Active' if the border cells is coloured with '2'
	 * @return: True if at-least one Wall is set 'Active'
	 * 			False otherwise
	 */
	private static boolean colorBorderLines(){
		int i;
		boolean to_ret = false;
		for( i = 0 ; i < colSize; ++i){
			if(cellLst[0][i].getCellColor() > 1 && !cellLst[0][i].getTopWall().getFixed()){
				cellLst[0][i].getTopWall().setFixed(true, true);
				allMoveLst.add( new MoveObj( cellLst[0][i].getTopWall()));
				to_ret = true;
			}
			if(cellLst[rowSize-1][i].getCellColor() > 1 && !cellLst[rowSize-1][i].getBottomWall().getFixed()){
				cellLst[rowSize-1][i].getBottomWall().setFixed(true, true);
				allMoveLst.add( new MoveObj( cellLst[rowSize-1][i].getBottomWall()));
				to_ret = true;
			}
		}
		for( i = 0; i < rowSize; ++i){
			if(cellLst[i][0].getCellColor() > 1 && !cellLst[i][0].getLeftWall().getFixed()){
				cellLst[i][0].getLeftWall().setFixed(true, true);
				allMoveLst.add( new MoveObj( cellLst[i][0].getLeftWall()));
				to_ret = true;
			}
			if(cellLst[i][colSize-1].getCellColor() > 1 && !cellLst[i][colSize-1].getRightWall().getFixed()){
				cellLst[i][colSize-1].getRightWall().setFixed(true, true);
				allMoveLst.add( new MoveObj( cellLst[i][colSize-1].getRightWall()));
				to_ret = true;
			}
		}
		return to_ret;
	}
	
	/**
	 * Add complimentary colours to the Cells whose common edge is 'Active'
	 * @return: True if at-least one Cell is coloured
	 * 			False otherwise
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
						allMoveLst.add( new MoveObj(cellLst[i-1][j]));
						to_ret = true;
					}
					if(checkBounds(i+1, j) && tmpCell.getBottomWall().getIsActive() && cellLst[i+1][j].getCellColor() == 0 && ds.findSet(getIndex(i, j)) != ds.findSet(getIndex(i+1, j))){
						cellLst[i+1][j].setCellColor(color, true);
						allMoveLst.add( new MoveObj(cellLst[i+1][j]));
						to_ret = true;
					}
					if(checkBounds(i, j+1) && tmpCell.getRightWall().getIsActive() && cellLst[i][j+1].getCellColor() == 0 && ds.findSet(getIndex(i, j)) != ds.findSet(getIndex(i, j+1))){
						cellLst[i][j+1].setCellColor(color, true);
						allMoveLst.add( new MoveObj(cellLst[i][j+1]));
						to_ret = true;
					}
					if(checkBounds(i, j-1) && tmpCell.getLeftWall().getIsActive() && cellLst[i][j-1].getCellColor() == 0 && ds.findSet(getIndex(i, j)) != ds.findSet(getIndex(i, j-1))){
						cellLst[i][j-1].setCellColor(color, true);
						allMoveLst.add( new MoveObj(cellLst[i][j-1]));
						to_ret = true;
					}
				}
			}
		}
		return to_ret;
	}
	
	/**
	 * Function set the common edge between Cells with complimentary colours to 'Active' 
	 * @return: True if at-least one Wall is set 'Active'
	 * 			False otherwise
	 */
	private static boolean drawLineColorSep(){
		int i, j, color;
		Cell tmpCell;
		boolean to_ret = false, tmp;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j ){
				tmpCell = cellLst[i][j];
				color = tmpCell.getCellColor();
				if( color != 0){
					if(checkBounds(i-1, j) && cellLst[i-1][j].getCellColor() != 0 && !tmpCell.getTopWall().getFixed() ){
						tmp = cellLst[i-1][j].getCellColor() != color;
						if(tmp){
							tmpCell.getTopWall().setFixed(true, tmp);
							allMoveLst.add( new MoveObj(tmpCell.getTopWall()));
							to_ret = true;
						}
					}
					if(checkBounds(i+1, j) && cellLst[i+1][j].getCellColor() != 0 && !tmpCell.getBottomWall().getFixed()){
						tmp = cellLst[i+1][j].getCellColor() != color;
						if(tmp){
							tmpCell.getBottomWall().setFixed(true, tmp);
							allMoveLst.add( new MoveObj(tmpCell.getBottomWall()));
							to_ret = true;
						}
					}
					if(checkBounds(i, j-1) && cellLst[i][j-1].getCellColor() != 0 && !tmpCell.getLeftWall().getFixed()){
						tmp = cellLst[i][j-1].getCellColor() != color;
						if(tmp){
							tmpCell.getLeftWall().setFixed(true, tmp);
							allMoveLst.add( new MoveObj(tmpCell.getLeftWall()));
							to_ret = true;
						}
					}
					if(checkBounds(i, j+1) && cellLst[i][j+1].getCellColor() != 0 && !tmpCell.getRightWall().getFixed()){
						tmp = cellLst[i][j+1].getCellColor() != color;
						if(tmp){
							tmpCell.getRightWall().setFixed(true, tmp);
							allMoveLst.add( new MoveObj(tmpCell.getRightWall()));
							to_ret = true;
						}
					}
				}
			}
		}
		return to_ret;
	}
	
	/**
	 * Function colours the Cells with value 0 and its Neighbours, with colour '1'
	 */
	private static void colorZeroCells(){
		int i;
		for( i = 0; i < colSize; ++i){
			if(cellLst[0][i].getNodeVal() == 0 && cellLst[0][i].getCellColor() == 0){
				cellLst[0][i].setCellColor(1, true);
				allMoveLst.add( new MoveObj(cellLst[0][i]));
				if(cellLst[1][i].getCellColor() == 0){
					cellLst[1][i].setCellColor(1, true);
					allMoveLst.add( new MoveObj(cellLst[1][i]));
				}	
				if(checkBounds(0, i+1) && cellLst[0][i+1].getCellColor() == 0){
					cellLst[0][i+1].setCellColor(1, true);
					allMoveLst.add( new MoveObj(cellLst[0][i+1]));
				}
				if(checkBounds(0, i-1) && cellLst[0][i-1].getCellColor() == 0){
					cellLst[0][i-1].setCellColor(1, true);
					allMoveLst.add( new MoveObj(cellLst[0][i-1]));
				}
			}
			if(cellLst[rowSize-1][i].getNodeVal() == 0 && cellLst[rowSize-1][i].getCellColor() == 0){
				cellLst[rowSize-1][i].setCellColor(1, true);
				allMoveLst.add( new MoveObj(cellLst[rowSize-1][i]));
				if(cellLst[rowSize-2][i].getCellColor() == 0){
					cellLst[rowSize-2][i].setCellColor(1, true);
					allMoveLst.add( new MoveObj(cellLst[rowSize-2][i]));
				}
				if(checkBounds(rowSize-1, i-1) && cellLst[rowSize-1][i-1].getCellColor() == 0){
					cellLst[rowSize-1][i-1].setCellColor(1, true);
					allMoveLst.add( new MoveObj(cellLst[rowSize-1][i-1]));
				}
				if(checkBounds(rowSize-1, i+1) && cellLst[rowSize-1][i+1].getCellColor() == 0){
					cellLst[rowSize-1][i+1].setCellColor(1, true);
					allMoveLst.add( new MoveObj(cellLst[rowSize-1][i+1]));
				}
			}
		}
		
		for( i = 1; i < rowSize-1; ++i){
			if(cellLst[i][0].getNodeVal() == 0 && cellLst[i][0].getCellColor() == 0){
				cellLst[i][0].setCellColor(1, true);
				allMoveLst.add( new MoveObj(cellLst[i][0]));
				if(cellLst[i][1].getCellColor() == 0){
					cellLst[i][1].setCellColor(1, true);
					allMoveLst.add( new MoveObj(cellLst[i][1]));
				}
				if(checkBounds(i+1, 0) && cellLst[i+1][0].getCellColor() == 0){
					cellLst[i+1][0].setCellColor(1);
					allMoveLst.add( new MoveObj(cellLst[i+1][0]));
				}
				if(checkBounds(i-1, 0) && cellLst[i-1][0].getCellColor() == 0){
					cellLst[i-1][0].setCellColor(1);
					allMoveLst.add( new MoveObj(cellLst[i-1][0]));
				}
			}
			if(cellLst[i][colSize-1].getNodeVal() == 0 && cellLst[i][colSize-1].getCellColor() == 0){
				cellLst[i][colSize-1].setCellColor(1, true);
				allMoveLst.add( new MoveObj(cellLst[i][colSize-1]));
				if(cellLst[i][colSize-2].getCellColor() == 0){
					cellLst[i][colSize-2].setCellColor(1, true);
					allMoveLst.add( new MoveObj(cellLst[i][colSize-2]));
				}
				if(checkBounds(i-1, colSize-1) && cellLst[i-1][colSize-1].getCellColor() == 0){
					cellLst[i-1][colSize-1].setCellColor(1);
					allMoveLst.add( new MoveObj(cellLst[i-1][colSize-1]));
				}
				if(checkBounds(i+1, colSize-1) && cellLst[i+1][colSize-1].getCellColor() == 0){
					cellLst[i+1][colSize-1].setCellColor(1);
					allMoveLst.add( new MoveObj(cellLst[i+1][colSize-1]));
				}
			}
		}
		colorLineSep();
		drawLineColorSep();
		colorBorderLines();
	}
	
	/**
	 * Function Handle the Cases where Adjacent cells of Node value 2 having same/different colours ( != 0 )
	 * Idea : Cells with value 2 should have two neighbours with same colour and the rest with different colour 
	 * @return: True if at-least one Wall is set 'Active' or a Cell is coloured
	 * 			False otherwise
	 */
	private static boolean colorTwoAdj(){
		int i, j, color;
		Cell tmpCell;
		boolean to_ret = false, tmp;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				List<Cell> notColored = new ArrayList<Cell>();
				List<Cell> Color1 = new ArrayList<Cell>();
				List<Cell> Color2 = new ArrayList<Cell>();
				tmpCell = cellLst[i][j];
				color = tmpCell.getCellColor();
				if(tmpCell.getNodeVal() == 2 && tmpCell.noAdjColored(cellLst) != 0){
					oneTwoThreeSubFunction(i-1, j, Color1, Color2, notColored, tmpCell);
					oneTwoThreeSubFunction(i+1, j, Color1, Color2, notColored, tmpCell);
					oneTwoThreeSubFunction(i, j-1, Color1, Color2, notColored, tmpCell);
					oneTwoThreeSubFunction(i, j+1, Color1, Color2, notColored, tmpCell);
					if(Color1.size() == 2){
						for( Cell k : notColored){
							k.setCellColor(2, true);
							allMoveLst.add( new MoveObj(k));
							to_ret = true;
						}
					}
					else if(Color2.size() == 2){
						for( Cell k : notColored){
							k.setCellColor(1, true);
							allMoveLst.add( new MoveObj(k));
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
		tmp = colorLineSep();
		to_ret = to_ret ? to_ret : tmp;
		tmp = drawLineColorSep();
		to_ret = to_ret ? to_ret : tmp;
		tmp = colorBorderLines();
		to_ret = to_ret ? to_ret : tmp;
		return to_ret;
	}
	
	/**
	 * Function Handles the Case where a Cell with value 0 has at-least one coloured neighbour
	 * Idea : Cells with value 1 can have only one Neighbour with opposite colour
	 * @return: True if at-least one Wall is set 'Active' or a Cell is coloured 
	 * 			False otherwise
	 */
	private static boolean colorOneAdj(){
		int i, j, color;
		boolean to_ret = false, tmp;
		Cell tmpCell;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				tmpCell = cellLst[i][j];
				color = tmpCell.getCellColor();
				if(tmpCell.getNodeVal() == 1 && tmpCell.noAdjColored(cellLst) != 0){
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
							allMoveLst.add( new MoveObj(tmpCell));
							to_ret = true;
						}
						else if(Color2.size() > 1){
							tmpCell.setCellColor(2, true);
							allMoveLst.add( new MoveObj(tmpCell));
							to_ret = true;
						}
						color = tmpCell.getCellColor();
					}
					if(color == 1){
						if(Color2.size() > 0){
							for( Cell k : notColored){
								k.setCellColor(1, true);
								allMoveLst.add( new MoveObj(k));
								to_ret = true;
							}
						}	
						else if(Color1.size() == 3){
							for( Cell k : notColored){
								k.setCellColor(2, true);
								allMoveLst.add( new MoveObj(k));
								to_ret = true;
							}
						}
					}
					else if(color == 2){
						if(Color1.size() > 0){
							for( Cell k : notColored){
								k.setCellColor(2, true);
								allMoveLst.add( new MoveObj(k));
								to_ret = true;
							}
						}
						else if(Color2.size() == 3){
							for( Cell k : notColored){
								k.setCellColor(1, true);
								allMoveLst.add( new MoveObj(k));
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
		
		tmp = colorLineSep();
		to_ret = to_ret ? to_ret : tmp;
		tmp = drawLineColorSep();
		to_ret = to_ret ? to_ret : tmp;
		tmp = colorBorderLines();
		to_ret = to_ret ? to_ret : tmp;
		return to_ret;
	}
	
	/**
	 * Function Handles the Case where a Cell with value 3 has at-least one coloured neighbour
	 * Idea : Cells with value 3 can have only one neighbour with same colour 
	 * @return: True if at-least one Wall is set 'Active' or a Cell is coloured 
	 * 			False otherwise
	 */
	private static boolean colorThreeAdj(){
		int i, j, color;
		Cell tmpCell;
		boolean to_ret = false, tmp;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				tmpCell = cellLst[i][j];
				color = tmpCell.getCellColor();
				if(tmpCell.getNodeVal() == 3 && tmpCell.noAdjColored(cellLst) != 0){
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
							allMoveLst.add( new MoveObj(tmpCell));
							to_ret = true;
						}
						else if(Color2.size() > 1){
							tmpCell.setCellColor(1, true);
							allMoveLst.add( new MoveObj(tmpCell));
							to_ret = true;
						}
						color = tmpCell.getCellColor();
					}				
					if(color == 1){
						if(Color1.size() > 0){
							for( Cell k : notColored){
								k.setCellColor(2, true);
								allMoveLst.add( new MoveObj(k));
								to_ret = true;
							}
						}
						else if(Color2.size() == 3)
							for( Cell k : notColored){
								k.setCellColor(1, true);
								allMoveLst.add( new MoveObj(k));
								to_ret = true;
							}
					}
					else if(color == 2){
						if(Color2.size() > 0){
							for( Cell k : notColored){
								k.setCellColor(1, true);
								allMoveLst.add( new MoveObj(k));
								to_ret = true;
							}
						}
						else if(Color1.size() == 3)
							for( Cell k : notColored){
								k.setCellColor(2, true);
								allMoveLst.add( new MoveObj(k));
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
		tmp = colorLineSep();
		to_ret = to_ret ? to_ret : tmp;
		tmp = drawLineColorSep();
		to_ret = to_ret ? to_ret : tmp;
		tmp = colorBorderLines();
		to_ret = to_ret ? to_ret : tmp;
		return to_ret;
	}
	
	/**
	 * Helper Function to group the Cells adjacent to the given cell 'tmpCell' with their colour
	 * @param i : The X Coordinate of the Cell under consideration
	 * @param j : The Y Coordinate of the Cell under consideration
	 * @param Color1 : The List into which Adjacent cells with colour '1' is to be filled
	 * @param Color2 : The List into which Adjacent cells with colour '2' is to be filled
	 * @param notColored : The List into which un-coloured Adjacent cells is to be filled
	 * @param tmpCell : The given Cell
	 */
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
	
	/**
	 * Function to handle the Cases as described in the below grid diagram
	 * 
	 * 									0								  1/2
	 * 						   -------				(or)		 -------
	 * 							 1/2 |							   0   |
	 * 								 |								   |
	 * 	Idea : The Cells Meeting at the corner, should have have opposite colours if the corner Walls are Active or vice versa
	 * @return 	True if at-least one Wall is set 'Active' or a Cell is coloured 
	 * 			False otherwise
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
	
	/**
	 * Helper Function to colour the Cells around the corner with opposite colour
	 * @param i : The X Coordinate of the Cell under consideration
	 * @param j : The Y Coordinate of the Cell under consideration
	 * @param tmpCell : The given Cell
	 * @param color : The colour the given Cell
	 * @return 	True if at-least one Cell is coloured 
	 * 			False otherwise
	 */
	private static boolean cellAroundCornerSubFunction(int i, int j, Cell tmpCell, int color){
		boolean to_ret = false;
		if(color != 0){
			if(cellLst[i][j].getCellColor() == 0){
				cellLst[i][j].setCellColor(color == 1 ? 2 : 1, true);
				allMoveLst.add( new MoveObj(cellLst[i][j]));
				to_ret = true;
			}
		}
		else{
			if(cellLst[i][j].getCellColor() != 0){
				tmpCell.setCellColor(cellLst[i][j].getCellColor() == 1 ? 2 : 1, true);
				allMoveLst.add( new MoveObj(tmpCell));
				to_ret = true;
			}
		}
		return to_ret;
	}
	
	/**
	 * Function to colour the Cell with value 0 and its neighbours given at-least one of them is coloured
	 * @return 	True if at-least one Cell is coloured 
	 * 			False otherwise
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
							allMoveLst.add( new MoveObj(tmpCell));
							for( Cell k : notColored){
								k.setCellColor(1, true);
								allMoveLst.add( new MoveObj(k));
							}
						}
						else if( Color2.size() > 0){
							to_ret = true;
							tmpCell.setCellColor(2, true);
							allMoveLst.add( new MoveObj(tmpCell));
							for( Cell k : notColored){
								k.setCellColor(2, true);
								allMoveLst.add( new MoveObj(k));
							}
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
	
	/**
	 * Helper Function to Colour a given cell with the given colour under the Boundary conditions
	 * @param i : The X Coordinate of the Cell under consideration
	 * @param j : The Y Coordinate of the Cell under consideration
	 * @param color : The colour with which the Cell must be coloured
	 * @return 	True if the Cell is coloured 
	 * 		 	False otherwise
	 */
	private static boolean zeroSubFunction(int i, int j, int color){
		if(checkBounds(i, j) && cellLst[i][j].getCellColor() == 0){
			cellLst[i][j].setCellColor(color, true);
			allMoveLst.add( new MoveObj(cellLst[i][j]));
			return true;
		}
		return false;
	}
	
	/**
	 * Function Colours two Adjacent Cells belonging to same Disjoint Set with same colour or
	 * Performs Union() of two Adjacent Cells if they have same colour
	 * @return 	True if at-least one Cell is coloured 
	 * 		 	False otherwise
	 */
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
						allMoveLst.add( new MoveObj(tmpCell));
						to_ret = true;
					}
					else if(ds.findSet(getIndex(i-1, j)) == parent  && !(checkBounds(i-1, j) && cellLst[i-1][j].getCellColor() == 0)){
						tmpCell.setCellColor(checkBounds(i-1, j) ? cellLst[i-1][j].getCellColor() : 1, true);
						allMoveLst.add( new MoveObj(tmpCell));
						to_ret = true;
					}
					else if(ds.findSet(getIndex(i, j+1)) == parent && !(checkBounds(i, j+1) && cellLst[i][j+1].getCellColor() == 0)){
						tmpCell.setCellColor(checkBounds(i, j+1) ? cellLst[i][j+1].getCellColor() : 1, true);
						allMoveLst.add( new MoveObj(tmpCell));
						to_ret = true;
					}
					else if(ds.findSet(getIndex(i, j-1)) == parent && !(checkBounds(i, j-1) && cellLst[i][j-1].getCellColor() == 0)){
						tmpCell.setCellColor(checkBounds(i, j-1) ? cellLst[i][j-1].getCellColor() : 1, true);
						allMoveLst.add( new MoveObj(tmpCell));
						to_ret = true;
					}
				}
				color = tmpCell.getCellColor();
				parent = ds.findSet(getIndex(i, j));
				if(color != 0){
					if(checkBounds(i+1, j) && ds.findSet(getIndex(i+1, j)) == parent && cellLst[i+1][j].getCellColor() == 0){
						cellLst[i+1][j].setCellColor(color, true);
						allMoveLst.add( new MoveObj(cellLst[i+1][j]));
						to_ret = true;
					}
					if(checkBounds(i-1, j) && ds.findSet(getIndex(i-1, j)) == parent && cellLst[i-1][j].getCellColor() == 0){
						cellLst[i-1][j].setCellColor(color, true);
						allMoveLst.add( new MoveObj(cellLst[i-1][j]));
						to_ret = true;
					}
					if(checkBounds(i, j+1) && ds.findSet(getIndex(i, j+1)) == parent && cellLst[i][j+1].getCellColor() == 0){
						cellLst[i][j+1].setCellColor(color, true);
						allMoveLst.add( new MoveObj(cellLst[i][j+1]));
						to_ret = true;
					}
					if(checkBounds(i, j-1) && ds.findSet(getIndex(i, j-1)) == parent && cellLst[i][j-1].getCellColor() == 0){
						cellLst[i][j-1].setCellColor(color, true);
						allMoveLst.add( new MoveObj(cellLst[i][j-1]));
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
	
	/**
	 * Function to check the Correctness of the Game Grid at any given point
	 * Checks for the following cases,
	 * 		Adjacent cells belonging to same Disjoint Set but having different colour
	 * 		Adjacent Cells belonging to different Disjoint Sets but having same colour
	 * 		Cells with all four Walls 'Active'
	 * 		A Coordinate with more than 2 'Active' incident Walls
	 * 		Cells with more 'Active' Walls than its Node value
	 * @return  True if the Cells colouring and Wall 'Active' is consistent
	 * 		 	False otherwise
	 */
	private static boolean CheckBoardStateCorrect(){
		int i, j, color, parent;
		Cell tmpCell;
		for( i = 0; i < rowSize; ++i){
			for( j = 0; j < colSize; ++j){
				tmpCell = cellLst[i][j];
				if(tmpCell.getActiveWalls() == 4)
					return false;
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
	
	/**
	 * Function checks whether the current is a Goal State
	 * Calls CheckBoardStateCorrect() to check correctness
	 * @return  True if the Game is solved 
	 * 		 	False otherwise
	 */
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
		return true;
	}
	
	/**
	 * Function implements the Back Tracking part using a Heuristic
	 * @return	True if Back tracking result in correct solution
	 * 			False otherwise
	 */
	private static boolean backTrack(){
		
		boolean flag = true, tmpflag;
		int color = 1, count = 0;
		
		while(flag && count <= MaxAllowedMoves){
			
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
			
			tmpflag = connectAdjSets();
			flag = flag ? flag : tmpflag;
			
			if(!CheckBoardStateCorrect())
				return false;
			
			if(isGameOver())
				return true;
			
			++count;
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
		
		if(level%2 == 0)
			color = 2;
		
		cellLst[emptyCellCoordinate.getX()][emptyCellCoordinate.getY()].setCellColor(color, true);
		allMoveLst.add( new MoveObj(cellLst[emptyCellCoordinate.getX()][emptyCellCoordinate.getY()]));
		if(backTrack())
			return true;
		
		addDifference(cellcopy);
		
		ds = dscopy.getCopy();
		cellLst = getCellLstCopy(cellcopy);
		
		color = color == 1 ? 2 : 1;
		cellLst[emptyCellCoordinate.getX()][emptyCellCoordinate.getY()].setCellColor(color, true);
		allMoveLst.add( new MoveObj(cellLst[emptyCellCoordinate.getX()][emptyCellCoordinate.getY()]));
		return backTrack();
	}
	
	/**
	 * Heuristic : Max Adjacent Cells Coloured
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
	
	
	/**
	 *  Heuristic : Naive First Non Coloured Cell Selection
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
	
	/**
	 *  Heuristic : Max Node Value
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
	
	/**
	 *  Heuristic : Max Active Walls
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
	
	/**
	 *  Heuristic :  FIRST + THIRD
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
	
	/**
	 * Function to undo the changes made by the Back Tracking if it did not result in correct solution
	 * @param cellcopy : A copy of Cell array before back tracking is invoked at every level 
	 */
	private static void addDifference(Cell[][] cellcopy) {
		Vector<Wall> oldLst =  Grid.getAllWalls(cellcopy);
		Vector<Wall> newLst =  Grid.getAllWalls(cellLst);
		int i, j;
		for(i = 0; i < oldLst.size(); ++i)
			if(oldLst.get(i).getIsActive() != newLst.get(i).getIsActive())
				allMoveLst.add( new MoveObj(oldLst.get(i)));
		
		for( i = 0; i < rowSize; ++i)
			for( j = 0; j < colSize; ++j)
				if(cellcopy[i][j].getCellColor() != cellLst[i][j].getCellColor())
					allMoveLst.add( new MoveObj( cellcopy[i][j]));
	}
	
	/**
	 * Helper functions for the Basic Solver Function
	 */
	
	/**
	 * Function to check whether the given 'x' coordinate and 'y' coordinate are within the Game Grid
	 * @param x: X coordinate
	 * @param y: Y coordinate
	 * @return: True if it is within Bounds
	 * 			False if it is out of Bounds
	 */
	public static boolean checkBounds(int x, int y){
		return ( x < 0 || y < 0 || x >= rowSize || y >= colSize) ? false :  true;
	}
	
	/**
	 * Function to get the Index in the Disjoint Set array, for the given Cell coordinates
	 * @param x: X coordinate
	 * @param y: Y coordinate
	 * @return: The Disjoint Set array index
	 */
	public static int getIndex(int x, int y){
		return checkBounds(x,y) ? ( x*rowSize + y + 2 ) : 0;
	}
	
	/**
	 * Function to get a copy of the given Cell array ( Recursively copies its walls ) 
	 * @param cellArr: The cell array which has to be copied
	 * @return: A copy of the given Cell array
	 */
	private static Cell[][] getCellLstCopy(Cell[][] cellArr){
		int i, j;
		Cell[][] copy = new Cell[rowSize][colSize];
		for( i = 0; i < rowSize; ++i)
			for( j = 0; j < colSize; ++j)
				copy[i][j] = cellArr[i][j].getCopy();
		return copy;
	}
	
}
