package gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

public class SlitherLinkBoard extends JFrame
{
	public static int gridRows;
	public static int gridCols;
	public static int[][] input;
	public static void main(String[] args) throws Exception
    {
		FR.init(System.in);
		getInput();
        final SlitherLinkBoard slb = new SlitherLinkBoard(gridRows, gridCols);             
    }
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
	private Container c;
	private JPanel pnlMain;
	private JPanel[][] pnlCells;
	private Border b = BorderFactory.createLineBorder(Color.BLACK);
	public SlitherLinkBoard(int m, int n) throws Exception
    {
          c = getContentPane();
          setBounds(400, 100, 470, 525);
          setBackground(new Color(204, 204, 204));
          setDefaultCloseOperation(EXIT_ON_CLOSE);
          setTitle("SlitherLink Solver");
          setResizable(true);          
          c.setLayout(null);             
        
          pnlMain = new JPanel(new GridLayout(m, n));
          pnlCells = new JPanel[m][n];
          pnlCells[0][0].setBorder(b);
          pnlMain.setBounds(3, 3, 500, 500);
          pnlMain.setBackground(new Color(255, 255, 255)); 
          c.add(pnlMain);
          
          show();
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