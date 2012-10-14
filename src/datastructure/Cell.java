package datastructure;

import java.util.ArrayList;
import java.util.List;

import backend.MainSolver;

public class Cell implements Comparable<Cell>{
	private Wall topWall, bottomWall, rightWall, leftWall;
	private int nodeVal;
	private Coordinate position;		//Left Top Co-ordinates
	private int cellColor;				// 0 - no color ; 1 - outer part ; 2 - inner part
	//public int noCellOppColor;
	private boolean isColored;
	
	public Cell(int val, Coordinate pos){
		this.setNodeVal(val);
		this.setPosition(pos);
		this.setCellColor(0);
		this.setIsColored(false);
		//this.noCellOppColor = 0;
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
	private void setBottomWall(Wall wall) {
		this.bottomWall = wall;
	}
	private void setLeftWall(Wall wall) {
		this.leftWall = wall;
	}
	private void setTopWall(Wall wall) {
		this.topWall = wall;
	}
	private void setRightWall(Wall wall) {
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
	
}
