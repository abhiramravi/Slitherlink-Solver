package gui;

import datastructure.Cell;
import datastructure.Wall;

public class MoveObj {
	private boolean isWallMove;
	private Wall wallMove;
	private Cell CellMove;
	
	/*
	 * Getter Methods
	 */
	
	public MoveObj(Wall a){
		this.isWallMove = true;
		this.wallMove = a.getCopy();
		this.CellMove = null;
	}
	
	public MoveObj(Cell a){
		this.isWallMove = false;
		this.wallMove = null;
		this.CellMove = a.getCopy();
	}
	
	public boolean getisWallMove(){
		return this.isWallMove;
	}
	
	public Wall getWallMove(){
		return this.wallMove;
	}
	
	public Cell getCellMove(){
		return this.CellMove;
	}
	
	/*
	 * Setter Methods
	 */
	public void setisWallMove(boolean b){
		this.isWallMove = b;
	}
	
	public void setWallMove(Wall b){
		this.wallMove = b.getCopy();
	}
	
	public void setCellMove(Cell b){
		this.CellMove = b.getCopy();
	}
}
