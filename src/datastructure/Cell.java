package datastructure;

import backend.MainSolver;

public class Cell implements Comparable<Cell>{
	private Wall topWall, bottomWall, rightWall, leftWall;
	private int nodeVal;
	private Coordinate position;		//Left Top Co-ordinates
	private int cellColor;				// 0 - no color ; 1 - outer part ; 2 - inner part
	private boolean isColored;
	
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
	
	public Cell(int val){
		this.setNodeVal(val);
		this.setCellColor(0);
		this.setIsColored(false);
		this.setPosition(new Coordinate(0, -2));
	}
	
	/*
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
	
	/*
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
	
	@Override
	public int compareTo(Cell o) {
		int i = this.noAdjColored();
		int j = o.noAdjColored();
		if(i!=j)
			return i > j ? 1 : -1;
		return 0;
	}
	
	private int noAdjColored(){
		int count = 0;
		int i = this.getPosition().getX();
		int j = this.getPosition().getY();
		if(!MainSolver.checkBounds(i+1, j) || Grid.cellLst[i+1][j].getCellColor() != 0)
			++count;
		if(!MainSolver.checkBounds(i-1, j) || Grid.cellLst[i-1][j].getCellColor() != 0)
			++count;
		if(!MainSolver.checkBounds(i, j+1) || Grid.cellLst[i][j+1].getCellColor() != 0)
			++count;
		if(!MainSolver.checkBounds(i, j-1) || Grid.cellLst[i][j-1].getCellColor() != 0)
			++count;
		return count;
	}
	
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
