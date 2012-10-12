package gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class SlitherLinkBoard extends JFrame
{
	/* The variables representing the input */
	public static int gridRows;
	public static int gridCols;
	public static int[][] input;
	
	
	/* The main function */
	public static void main(String[] args) throws Exception
    {
		FR.init(System.in);
		getInput();
        final SlitherLinkBoard slb = new SlitherLinkBoard(gridRows, gridCols);
    }
	
	/* Function to get the input and set the static vars */
	public static void getInput() throws IOException
	{
		gridRows = FR.nextInt();
		gridCols = FR.nextInt();
		input = new int[gridRows][gridCols];
		for(int i = 0; i < gridRows; i++)
			for(int j = 0; j < gridCols; j++)
			{
				input[i][j] = FR.nextInt();
			}
	}
	
	/* Variables that define the GUI */
	private Container c;
	private JPanel pnlMain;
	private JPanel[][] pnlCells;
	private Border defaultBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
	
	/* Modify this function to get the border width correctly */
	private Border getActiveBorder()
	{
		return BorderFactory.createMatteBorder(2, 0, 2, 0, Color.BLACK);
	}
	
	/* The Main constructor */
	public SlitherLinkBoard(int m, int n) throws Exception
    {
          c = getContentPane();
          setBounds(400, 100, 1000, 1000);
          setBackground(new Color(204, 204, 204));
          setDefaultCloseOperation(EXIT_ON_CLOSE);
          setTitle("SlitherLink Solver");
          setResizable(true);          
          c.setLayout(null);             
        
          pnlMain = new JPanel(new GridLayout(m, n));
          pnlCells = new JPanel[m][n];
          
          pnlMain.setBounds(3, 3, 500, 500);
          pnlMain.setBackground(new Color(255, 255, 255)); 
          c.add(pnlMain);
          
          drawBoard();
          //---------
          pnlCells[2][2].setBorder(getActiveBorder());
          //--------
          show();
    }
	
	/* Drawing the default board on the screen. */
	 private void drawBoard()
     {
           for (int y = 0; y < gridRows; y++)
                 for (int x = 0; x < gridCols; x++)
                 {
                       pnlCells[y][x] = new JPanel(new BorderLayout());
                       pnlMain.add(pnlCells[y][x]);
                       pnlCells[y][x].setBackground(new Color(255, 255, 255));
                       pnlCells[y][x].setBorder(defaultBorder);
                 }
     }
}

class FR
{
	static BufferedReader reader;
	static StringTokenizer tokenizer;

	/** call this method to initialize reader for InputStream */
	static void init(InputStream input)
	{
		reader = new BufferedReader(new InputStreamReader(input));
		tokenizer = new StringTokenizer("");
	}

	/** get next word */
	static String next() throws IOException
	{
		while (!tokenizer.hasMoreTokens())
		{
			// TODO add check for eof if necessary
			tokenizer = new StringTokenizer(reader.readLine());
		}
		return tokenizer.nextToken();
	}

	static int nextInt() throws IOException
	{
		return Integer.parseInt(next());
	}

	static double nextDouble() throws IOException
	{
		return Double.parseDouble(next());
	}
}