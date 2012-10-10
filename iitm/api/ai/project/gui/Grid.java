package iitm.api.ai.project.gui;

public class Grid {
	
	private int noRows;
	private int noColumns;
	private boolean isSolved;
	public Cell[][] cellLst;
	
	public Grid(int rows, int cols){
		this.setRows(rows);
		this.setColumn(cols);
		this.cellLst = new Cell[this.noRows][this.noColumns];
		this.isSolved = false;
	}
	
	/*
	 * Setter Methods
	 */
	public void setRows(int rows){
		this.noRows = rows;
	}
	public void setColumn(int cols){
		this.noColumns = cols;
	}
	public void setisSolved(boolean solved){
		this.isSolved = solved;
	}
	
	/*
	 * Getter Methods
	 */
	public int getRows(){
		return this.noRows;
	}
	public int getColumns(){
		return this.noColumns;
	}
	public boolean getisSolvedr(){
		return this.isSolved;
	}
}
