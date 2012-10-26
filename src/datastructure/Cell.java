package datastructure;

import backend.MainSolver;

/**
 * This Class defines the Data Structure to represent the cells in the Game Grid
 */
public class Cell{
	private Wall topWall, bottomWall, rightWall, leftWall;
	private int nodeVal;
	
	//The position of the Left Top Corner of the cell is stored in 'position' 
	private Coordinate position;
	
	/**
	 * Cell colour - 0 if no colour
	 * 			   - 1 if cell belongs to outer part
	 * 			   - 2 if cell belongs to inner part
	 */
	private int cellColor;
	
	//This indicates the whether the current cell is coloured or not
	private boolean isColored;
	
	/**
	 * Constructor to create Cell object given its value and its left top Coordinate
	 * @param val: The value of the node
	 * @param pos: The left top Coordinate of the Cell
	 */
	public Cell(int val, Coordinate pos){
		this.setNodeVal(val);
		this.setPosition(pos);
		this.setCellColor(0);
		this.setIsColored(false);
		this.setRightWall(new Wall(new Coordinate(pos.getX(), pos.getY()+1), false));
		if(pos.getX()!=0)
			this.setTopWall(Grid.cellLst[pos.getX()-1][pos.getY()].getBottomWall());
		else
			this.setTopWall(new Wall(new Coordinate(pos.getX(), pos.getY()), true));
		if(pos.getY()!=0)
			this.setLeftWall(Grid.cellLst[pos.getX()][pos.getY()-1].getRightWall());
		else
			this.setLeftWall(new Wall(new Coordinate(pos.getX(), pos.getY()), false));
		this.setBottomWall(new Wall(new Coordinate(pos.getX()+1, pos.getY()), true));
	}
	
	/**
	 * Constructor for the Cells in which Walls are not important
	 * @param val : The value contained in the cell
	 */
	public Cell(int val){
		this.setNodeVal(val);
		this.setCellColor(0);
		this.setIsColored(false);
		this.setPosition(new Coordinate(0, -2));
	}
	
	/**
	 * Getter Methods
	 */
	public Wall getTopWall(){
		return this.topWall;
	}
	public Wall getBottomWall(){
		return this.bottomWall;
	}
	public Wall getLeftWall(){
		return this.leftWall;
	}
	public Wall getRightWall(){
		return this.rightWall;
	}
	public int getNodeVal(){
		return this.nodeVal;
	}
	public Coordinate getPosition(){
		return this.position;
	}
	public int getCellColor(){
		return this.cellColor;
	}
	public boolean getIsColored(){
		return this.isColored;
	}
	
	/**
	 * Setter Methods
	 */
	public void setNodeVal(int val){
		this.nodeVal = val;
	}
	private void setPosition(Coordinate pos) {
		this.position = pos;		
	}
	public void setBottomWall(Wall wall) {
		this.bottomWall = wall;
	}
	public void setLeftWall(Wall wall) {
		this.leftWall = wall;
	}
	public void setTopWall(Wall wall) {
		this.topWall = wall;
	}
	public void setRightWall(Wall wall) {
		this.rightWall = wall;
	}
	public void setCellColor(int color){
		this.cellColor = color;
	}
	public void setIsColored(boolean b) {
		this.isColored = b;
	}
	public void setCellColor(int color, boolean b){
		this.cellColor = color;
		this.isColored = b;
	}

	/**
	 * The Function returns the Number of active walls of the current Cell
	 * @return Number of active Walls
	 */
	public int getActiveWalls(){
		int count = 0;
		if(this.leftWall.getIsActive())
			++count;
		if(this.rightWall.getIsActive())
			++count;
		if(this.topWall.getIsActive())
			++count;
		if(this.bottomWall.getIsActive())
			++count;
		return count;
	}
	
	/**
	 * The Functions returns the number of Adjacent cells 'coloured' to the current Cell object,
	 * 	for the given Grid representation 
	 * @param cellArr : The cell array in which the current Cell is present
	 * @return : Number of Adjacent Coloured cells
	 */
	public int noAdjColored(Cell[][] cellArr){
		int count = 0;
		int i = this.getPosition().getX();
		int j = this.getPosition().getY();
		if(!MainSolver.checkBounds(i+1, j) || cellArr[i+1][j].getCellColor() != 0)
			++count;
		if(!MainSolver.checkBounds(i-1, j) || cellArr[i-1][j].getCellColor() != 0)
			++count;
		if(!MainSolver.checkBounds(i, j+1) || cellArr[i][j+1].getCellColor() != 0)
			++count;
		if(!MainSolver.checkBounds(i, j-1) || cellArr[i][j-1].getCellColor() != 0)
			++count;
		return count;
	}
	
	/**
	 * Functions returns a copy of the current Cell. 
	 * This recursively copies all its walls.
	 * @return Copy of the current Cell
	 */
	public Cell getCopy(){
		Cell copy = new Cell(this.nodeVal, this.position);
		copy.setCellColor(this.cellColor, this.isColored);
		copy.setBottomWall(this.bottomWall.getCopy());
		copy.setTopWall(this.topWall.getCopy());
		copy.setRightWall(this.rightWall.getCopy());
		copy.setLeftWall(this.leftWall.getCopy());
		return copy;
	}
}
