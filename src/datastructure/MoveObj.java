package datastructure;

/**
 * This Class implements the ADT to support the recording of the Moves made by the Solver
 * @author sabari
 */
public class MoveObj {
	private boolean isWallMove;
	private Wall wallMove;
	private Cell CellMove;
	
	/**
	 * Constructor for the MoveObj to contain a Wall move
	 * @param a: The Wall modified
	 */
	public MoveObj(Wall a){
		this.isWallMove = true;
		this.wallMove = a.getCopy();
		this.CellMove = null;
	}
	/**
	 * Constructor for the MoveObj to contain a Cell move
	 * @param a: The Cell modified
	 */
	public MoveObj(Cell a){
		this.isWallMove = false;
		this.wallMove = null;
		this.CellMove = a.getCopy();
	}

	/**
	 * Getter Methods
	 */
	public boolean getisWallMove(){
		return this.isWallMove;
	}
	
	public Wall getWallMove(){
		return this.wallMove;
	}
	
	public Cell getCellMove(){
		return this.CellMove;
	}
	
	/**
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
