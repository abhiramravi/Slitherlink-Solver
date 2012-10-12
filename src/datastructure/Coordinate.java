package datastructure;

public class Coordinate {

	private int xpos, ypos;
	
	public Coordinate(int x, int y){
		this.setX(x);
		this.setY(y);
	}
	
	/*
	 * Getter Methods
	 */
	public int getX() {
		return this.xpos;
	}
	public int getY(){
		return this.ypos;
	}
	
	/*
	 * Setter Methods
	 */
	public void setX(int x){
		this.xpos = x;
	}
	public void setY(int y){
		this.ypos = y;
	}
}
