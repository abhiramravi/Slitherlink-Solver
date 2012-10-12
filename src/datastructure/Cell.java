package datastructure;

public class Cell {
	private Wall topWall, bottomWall, rightWall, leftWall;
	private int nodeVal;
	private Coordinate position;			//Left Top Co-ordinates
	
	public Cell(int val, Coordinate pos){
		this.setNodeVal(val);
		this.setPosition(pos);
		this.setRightWall(new Wall(new Coordinate(pos.getX(), pos.getY()), false));
		this.setTopWall(new Wall(new Coordinate(pos.getX(), pos.getY()), true));
		this.setLeftWall(new Wall(new Coordinate(pos.getX(), pos.getY()+1), false));
		this.setBottomWall(new Wall(new Coordinate(pos.getX()+1, pos.getY()), true));
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
}