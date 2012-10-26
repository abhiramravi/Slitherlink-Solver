package datastructure;

/**
 * This Class implements the Data Structure to represent the edges of the Cells of the game grid
 */
public class Wall {

	private Coordinate wallStart, wallEnd;
	/**
	 * @val fixed: The property of the wall is fixed. Thats is, if the wall is not active, it will not be active forever and vice versa
	 * @val isActive: The wall is active in the current game grid
	 * @val Horz: True if Wall is Horizontal
	 * 			  False if Wall is Vertical
	 */
	private boolean Horzn, isActive, fixed;
	
	/**
	 * Constructor for the Wall given the start coordinate and type of the Wall
	 * @param wstrt: Start Coordinates of the Wall
	 * @param type:	Horizontal or Vertical
	 */
	public Wall(Coordinate wstrt, boolean type) {
		this.setWallStart(wstrt);
		this.setHorzn(type);
		if(type)
			this.setWallEnd( new Coordinate(wstrt.getX(), wstrt.getY()+1));
		else
			this.setWallEnd( new Coordinate(wstrt.getX()+1, wstrt.getY()));
		this.setIsActive(false);
		this.setFixed(false);
	}

	/**
	 * Setter Methods
	 */
	public void setWallStart(Coordinate wstrt) {
		this.wallStart = wstrt;
	}
	public void setWallEnd(Coordinate wend) {
		this.wallEnd = wend;
	}
	public void setHorzn(boolean type){
		this.Horzn = type;
	}
	public void setIsActive(boolean b) {
		this.isActive = b;
	}
	public void setFixed(boolean b) {
		this.fixed = b;
	}
	public void setFixed(boolean a, boolean b){
		this.fixed = a;
		this.setIsActive(b);
	}

	
	/**
	 * Getter Methods
	 */
	public Coordinate getWallStart(){
		return this.wallStart;
	}
	public Coordinate getWallEnd(){
		return this.wallEnd;
	}
	public boolean getHorzn(){
		return this.Horzn;
	}
	public boolean getIsActive() {
		return this.isActive;
	}
	public boolean getFixed(){
		return this.fixed;
	}
	
	/**
	 * Function to get the copy of current Wall object
	 * @return: A copy of the current wall
	 */
	public Wall getCopy(){
		Wall copy = new Wall(this.wallStart, this.Horzn);
		copy.setFixed(this.fixed, this.isActive);
		return copy;
	}
}
