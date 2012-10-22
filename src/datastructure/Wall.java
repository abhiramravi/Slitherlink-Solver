package datastructure;

public class Wall {

	private Coordinate wallStart, wallEnd;
	/* 
	 Fixed - the property of the wall is fixed. If it is not present, it will not be present forever. and the other way 
	 */
	private boolean Horzn, isActive, fixed;
	
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

	/*
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

	
	/*
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
	
	public Wall getCopy(){
		Wall copy = new Wall(this.wallStart, this.Horzn);
		copy.setFixed(this.fixed, this.isActive);
		return copy;
	}
}
