package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import backend.MainSolver;
import datastructure.Cell;
import datastructure.Coordinate;
import datastructure.Grid;
import datastructure.MoveObj;
import datastructure.Wall;

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
		final SlitherLinkBoard slb = new SlitherLinkBoard();
	}

	/* Function to get the input and set the static vars */
	public static void getInput() throws IOException
	{
		gridRows = FR.nextInt();
		gridCols = FR.nextInt();
		input = new int[gridRows][gridCols];
		for (int i = 0; i < gridRows; i++)
			for (int j = 0; j < gridCols; j++)
			{
				input[i][j] = FR.nextInt();
			}
	}

	/* Variables that define the GUI */
	private Container c;
	private JPanel pnlMain;
	private JPanel[][] pnlCells;
	private Border defaultBorder = BorderFactory
			.createLineBorder(Color.LIGHT_GRAY);
	private JTextField[][] j;

	/* Modify this function to get the border width correctly */
	private Border getActiveBorder(int[] a)
	{
		return BorderFactory.createMatteBorder(a[0], a[1], a[2], a[3],
				Color.BLACK);
	}

	private Border getDeActiveBorder(int[] a)
	{
		return BorderFactory.createMatteBorder(a[0], a[1], a[2], a[3],
				Color.LIGHT_GRAY);
	}

	/* The Main constructor */
	public SlitherLinkBoard() throws Exception
	{
		c = getContentPane();
		setBounds(400, 100, 570, 570);
		setBackground(new Color(204, 204, 204));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("SlitherLink Solver");
		setResizable(true);
		c.setLayout(null);

		drawMenu();

		show();
	}

	private void colorCells()
	{
		for (int i = 0; i < gridRows; i++)
		{
			for (int k = 0; k < gridCols; k++)
			{
				if (Grid.cellLst[i][k].getCellColor() == 1)
				{
					j[i][k].setBackground(Color.ORANGE);
				}
				if (Grid.cellLst[i][k].getCellColor() == 2)
				{
					j[i][k].setBackground(Color.YELLOW);
				}
			}
		}
	}

	/* Drawing the default board on the screen. */
	private void drawBoard()
	{
		j = new JTextField[gridRows][gridCols];
		for (int y = 0; y < gridRows; y++)
			for (int x = 0; x < gridCols; x++)
			{
				pnlCells[y][x] = new JPanel(new BorderLayout());
				/* Creating text fields for input */
				j[y][x] = new JTextField();
				j[y][x].setFont(new Font("Arial", Font.BOLD, 20));
				j[y][x].setHorizontalAlignment(JLabel.CENTER);
				pnlCells[y][x].add(j[y][x], BorderLayout.CENTER);
				pnlMain.add(pnlCells[y][x]);
				// j[y][x].setBackground(Color.ORANGE);
				pnlCells[y][x].setBackground(new Color(255, 255, 255));
				pnlCells[y][x].setBorder(defaultBorder);
			}
	}

	private int stroke = 3;
	private int[][][] colors;

	private void printWalls()
	{
		colors = new int[gridRows][gridCols][4];
		Vector<Wall> wList = Grid.getAllWalls();
		for (Wall w : wList)
		{
			/*
			 * System.out.println(w.getWallStart().getX() + " " +
			 * w.getWallStart().getY() + " " + w.getIsActive() + " horiz = " +
			 * w.getHorzn());
			 */
			if (w.getIsActive())
			{
				Coordinate c = w.getWallStart();
				System.out.println("X = " + c.getX() + "; Y = " + c.getY());
				if (w.getHorzn())
				{
					if (c.getX() < gridRows)
					{
						colors[c.getX()][c.getY()][0] = stroke;
						pnlCells[c.getX()][c.getY()]
								.setBorder(getActiveBorder(colors[c.getX()][c
										.getY()]));
					} else
					{
						colors[c.getX() - 1][c.getY()][2] = stroke;
						pnlCells[c.getX() - 1][c.getY()]
								.setBorder(getActiveBorder(colors[c.getX() - 1][c
										.getY()]));
					}
				} else
				{
					if (c.getY() < gridCols)
					{
						colors[c.getX()][c.getY()][1] = stroke;
						pnlCells[c.getX()][c.getY()]
								.setBorder(getActiveBorder(colors[c.getX()][c
										.getY()]));
					} else
					{
						colors[c.getX()][c.getY() - 1][3] = stroke;
						pnlCells[c.getX()][c.getY() - 1]
								.setBorder(getActiveBorder(colors[c.getX()][c
										.getY() - 1]));
					}
				}
			}
		}
	}

	Solver solver = new Solver();

	class Solver implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			// TODO
			input = new int[gridRows][gridCols];
			for (int i = 0; i < gridRows; i++)
			{
				for (int k = 0; k < gridCols; k++)
				{
					if (!j[i][k].getText().equals(""))
					{
						input[i][k] = Integer
								.parseInt(j[i][k].getText().trim());
					} else
					{
						input[i][k] = -1;
					}

				}
			}
			Grid.updateCellList(gridRows, gridCols, input);

			for (int i = 0; i < gridRows; i++)
			{
				for (int k = 0; k < gridCols; k++)
				{
					System.out.print(input[i][k] + " ");
				}
				System.out.println();
			}
			/* The Main part of the computation */

			// MainSolver.basicSolver(); printWalls(); colorCells();

			/* The Main part of the computation ends */

			colors = new int[gridRows][gridCols][4];
			Thread t = new Thread(r);
			t.start();

		}

	}

	int sleeper = 200;
	Runnable r = new Runnable()
	{

		@Override
		public void run()
		{
			MainSolver.basicSolver();
			for (MoveObj m : Grid.allMoveLst)
			{
				simulateMove(m);
				try
				{
					Thread.sleep(sleeper);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	};

	public void simulateMove(MoveObj m)
	{
		if (m.getisWallMove())
		{
			wallMove(m);
		} else
		{
			cellColorMove(m);
		}
	}

	public void cellColorMove(MoveObj m)
	{
		Cell c = m.getCellMove();
		int i = c.getPosition().getX();
		int k = c.getPosition().getY();
		if (c.getCellColor() == 1)
		{
			j[i][k].setBackground(Color.ORANGE);
		}
		if (c.getCellColor() == 2)
		{
			j[i][k].setBackground(Color.YELLOW);
		}
		if (c.getCellColor() != 1 && c.getCellColor() != 2)
		{
			j[i][k].setBackground(Color.LIGHT_GRAY);
		}

	}

	public void wallMove(MoveObj m)
	{
		Wall w = m.getWallMove();
		if (w.getIsActive())
		{
			Coordinate c = w.getWallStart();
			System.out
					.println("Activate X = " + c.getX() + "; Y = " + c.getY());
			if (w.getHorzn())
			{
				if (c.getX() < gridRows)
				{
					colors[c.getX()][c.getY()][0] = stroke;
					pnlCells[c.getX()][c.getY()]
							.setBorder(getActiveBorder(colors[c.getX()][c
									.getY()]));
				} else
				{
					colors[c.getX() - 1][c.getY()][2] = stroke;
					pnlCells[c.getX() - 1][c.getY()]
							.setBorder(getActiveBorder(colors[c.getX() - 1][c
									.getY()]));
				}
			} else
			{
				if (c.getY() < gridCols)
				{
					colors[c.getX()][c.getY()][1] = stroke;
					pnlCells[c.getX()][c.getY()]
							.setBorder(getActiveBorder(colors[c.getX()][c
									.getY()]));
				} else
				{
					colors[c.getX()][c.getY() - 1][3] = stroke;
					pnlCells[c.getX()][c.getY() - 1]
							.setBorder(getActiveBorder(colors[c.getX()][c
									.getY() - 1]));
				}
			}
		} else
		{
			Coordinate c = w.getWallStart();
			System.out.println("Deactivate X = " + c.getX() + "; Y = "
					+ c.getY());
			if (w.getHorzn())
			{
				if (c.getX() < gridRows)
				{
					colors[c.getX()][c.getY()][0] = 0;
					pnlCells[c.getX()][c.getY()]
							.setBorder(getActiveBorder(colors[c.getX()][c
									.getY()]));
				} else
				{
					colors[c.getX() - 1][c.getY()][2] = 0;
					pnlCells[c.getX() - 1][c.getY()]
							.setBorder(getActiveBorder(colors[c.getX() - 1][c
									.getY()]));
				}
			} else
			{
				if (c.getY() < gridCols)
				{
					colors[c.getX()][c.getY()][1] = 0;
					pnlCells[c.getX()][c.getY()]
							.setBorder(getActiveBorder(colors[c.getX()][c
									.getY()]));
				} else
				{
					colors[c.getX()][c.getY() - 1][3] = 0;
					pnlCells[c.getX()][c.getY() - 1]
							.setBorder(getActiveBorder(colors[c.getX()][c
									.getY() - 1]));
				}
			}
		}
	}

	class newgame implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			String input1 = JOptionPane.showInputDialog(null,
					"Enter the dimensions of the grid - row<singlespace>col");

			try
			{
				String[] dims = input1.split(" ");
				/* Getting the new grid coordinates */
				int x = Integer.parseInt(dims[0]);
				int y = Integer.parseInt(dims[1]);
				if (x < 1 || y < 1)
					JOptionPane.showMessageDialog(null, "Invalid Dimensions");
				gridRows = x;
				gridCols = y;
				/* Creating the new GUI */
				c.removeAll();
				c = getContentPane();
				setBounds(400, 100, 570, 570);
				setBackground(new Color(204, 204, 204));
				setDefaultCloseOperation(EXIT_ON_CLOSE);
				setTitle("SlitherLink Solver");
				setResizable(true);
				c.setLayout(null);
				newgame ng = new newgame();

				pnlMain = new JPanel(new GridLayout(x, y));
				pnlCells = new JPanel[x][y];

				pnlMain.setBounds(3, 3, 500, 500);
				pnlMain.setBackground(new Color(255, 255, 255));
				c.add(pnlMain);

				drawMenu();
				drawBoard();
				// printWalls();
				// colorCells();

				show();

			} catch (Exception e)
			{
				System.out.println("Invalid dimensions");
			}

		}

	}

	public void drawMenu()
	{
		JMenuBar menuBar;
		JMenu menu, submenu;
		JMenuItem menuItem;
		JRadioButtonMenuItem rbMenuItem;
		JCheckBoxMenuItem cbMenuItem;
		newgame ng = new newgame();
		// Create the menu bar.
		menuBar = new JMenuBar();

		// Build the first menu.
		menu = new JMenu("File");
		menuBar.add(menu);

		// a group of JMenuItems
		menuItem = new JMenuItem("New Game", KeyEvent.VK_T);
		menuItem.addActionListener(ng);
		menu.add(menuItem);

		menuItem = new JMenuItem("Solve!", KeyEvent.VK_T);
		menuItem.addActionListener(solver);
		menu.add(menuItem);

		menuItem = new JMenuItem("Delay", KeyEvent.VK_T);
		menuItem.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				String a = JOptionPane
						.showInputDialog("Enter delay(ms) for move display");
				try
				{
					sleeper = Integer.parseInt(a);
				} catch (Exception e)
				{
					System.out.print("Failed to convert to int\n");
				}
			}
		});
		menu.add(menuItem);

		menuItem = new JMenuItem("Exit", KeyEvent.VK_T);
		menuItem.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				System.exit(1);
			}
		});
		menu.add(menuItem);

		// putting about in the menu bar.
		menu = new JMenu("About");
		menuBar.add(menu);

		menuItem = new JMenuItem("(C)Copyright 2012", KeyEvent.VK_T);
		menu.add(menuItem);

		menuItem = new JMenuItem("Abhiram Ravi", KeyEvent.VK_T);
		menuItem.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					URI site = new URI("http://www.cse.iitm.ac.in/~abhiram");
					java.awt.Desktop.getDesktop().browse(site);
				} catch (IOException e)
				{
					e.printStackTrace();
				} catch (URISyntaxException e)
				{
					e.printStackTrace();
				}

			}
		});
		menu.add(menuItem);

		menuItem = new JMenuItem("Sabari Naran", KeyEvent.VK_T);
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					URI site = new URI("http://www.facebook.com/npsabari");
					java.awt.Desktop.getDesktop().browse(site);
				} catch (IOException e)
				{
					e.printStackTrace();
				} catch (URISyntaxException e)
				{
					e.printStackTrace();
				}

			}
		});
		
		setJMenuBar(menuBar);
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