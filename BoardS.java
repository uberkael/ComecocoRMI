import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;
/* Modificaciones */
/* Contiene la parte serializable de el juego (no imagenes) */
/* Basado en el trabajo de: */
/* Drew Schuster */ /* https://github.com/dtschust/javapacman */
/* Agregado serializable */
public class BoardS extends JPanel implements java.io.Serializable {
	/* Initialize the player and ghosts */
	// Player player=new Player(200, 300);
	Player player;
	Ghost ghost1=new Ghost(180, 180);
	Ghost ghost2=new Ghost(200, 180);
	Ghost ghost3=new Ghost(220, 180);
	Ghost ghost4=new Ghost(220, 180);
	/* Timer is used for playing sound effects and animations */
	long timer=System.currentTimeMillis();
	/* Score information */
	int highScore;
	/* if the high scores have been cleared, we have to update the top of the screen to reflect that */
	boolean clearHighScores=false;
	int numLives=2;
	/*Contains the game map, passed to player and ghosts */
	boolean[][] state;
	/* Contains the state of all pellets*/
	boolean[][] pellets;
	/* Game dimensions */
	int gridSize;
	int max;
	/* State flags*/
	boolean stopped;
	boolean demo=false;
	int New;
	int lastPelletEatenX=0;
	int lastPelletEatenY=0;
	/* This is the font used for the menus */
	Font font=new Font("Monospaced", Font.BOLD, 12);
	/* Nombre de este Player*/
	String nombre;
	// Constructor
	public BoardS(String a)
	{
		nombre=a;
		initHighScores();
		stopped=false;
		max=400;
		gridSize=20;
		New=0;
	}
	/* Reads the high scores file and saves it */
	public void initHighScores() {
		File file=new File("highScores.txt");
		Scanner sc;
		try {
			sc=new Scanner(file);
			highScore=sc.nextInt();
			sc.close();
		} catch (Exception e) {
		}
	}
	/* Writes the new high score to a file and sets flag to update it on screen */
	public void updateScore(int score)
	{
		PrintWriter out;
		try
		{
			out=new PrintWriter("highScores.txt");
			out.println(score);
			out.close();
		}
		catch(Exception e)
		{
		}
		highScore=score;
		clearHighScores=true;
	}
	/* Reset occurs on a new game*/
	public void reset()
	{
		numLives=2;
		state = new boolean[20][20];
		pellets = new boolean[20][20];
		/* Clear state and pellets arrays */
		for(int i=0;i<20;i++)
		{
			for(int j=0;j<20;j++)
			{
				state[i][j]=true;
				pellets[i][j]=true;
			}
		}
		/* Handle the weird spots with no pellets*/
		for(int i = 5;i<14;i++)
		{
			for(int j = 5;j<12;j++)
			{
				pellets[i][j]=false;
			}
		}
		pellets[9][7] = false;
		pellets[8][8] = false;
		pellets[9][8] = false;
		pellets[10][8] = false;
	}
	/*
	Function is called during drawing of the map.
	Whenever the a portion of the map is covered up with a barrier,
	the map and pellets arrays are updated accordingly to note
	that those are invalid locations to travel or put pellets
	*/
	public void updateMap(int x, int y, int width, int height)
	{
		for (int i=x/gridSize; i<x/gridSize+width/gridSize; i++)
		{
			for (int j=y/gridSize; j<y/gridSize+height/gridSize; j++)
			{
				state[i-1][j-1]=false;
				pellets[i-1][j-1]=false;
			}
		}
	}
	/* Draws the appropriate number of lives on the bottom left of the screen.
	Also draws the menu */
	public void drawLives(Graphics g)
	{
		g.setColor(Color.BLACK);
		/*Clear the bottom bar*/
		g.fillRect(0, max+5, 600, gridSize);
		g.setColor(Color.YELLOW);
		for(int i=0; i<numLives; i++)
		{
			/*Draw each life */
			g.fillOval(gridSize*(i+1), max+5, gridSize, gridSize);
		}
		/* Draw the menu items */
		g.setColor(Color.YELLOW);
		g.setFont(font);
		g.drawString("Reset", 100, max+5+gridSize);
		g.drawString("Clear High Scores", 180, max+5+gridSize);
		g.drawString("Exit", 350, max+5+gridSize);
	}
	/*  This function draws the board.  The pacman board is really complicated and can only feasibly be done
	manually.  Whenever I draw a wall, I call updateMap to invalidate those coordinates.  This way the pacman
	and ghosts know that they can't traverse this area */
	public void drawBoard(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 600, 600);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 420, 420);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 20, 600);
		g.fillRect(0, 0, 600, 20);
		g.setColor(Color.WHITE);
		g.drawRect(19, 19, 382, 382);
		g.setColor(Color.BLUE);
		g.fillRect(40, 40, 60, 20);
		updateMap(40, 40, 60, 20);
		g.fillRect(120, 40, 60, 20);
		updateMap(120, 40, 60, 20);
		g.fillRect(200, 20, 20, 40);
		updateMap(200, 20, 20, 40);
		g.fillRect(240, 40, 60, 20);
		updateMap(240, 40, 60, 20);
		g.fillRect(320, 40, 60, 20);
		updateMap(320, 40, 60, 20);
		g.fillRect(40, 80, 60, 20);
		updateMap(40, 80, 60, 20);
		g.fillRect(160, 80, 100, 20);
		updateMap(160, 80, 100, 20);
		g.fillRect(200, 80, 20, 60);
		updateMap(200, 80, 20, 60);
		g.fillRect(320, 80, 60, 20);
		updateMap(320, 80, 60, 20);
		g.fillRect(20, 120, 80, 60);
		updateMap(20, 120, 80, 60);
		g.fillRect(320, 120, 80, 60);
		updateMap(320, 120, 80, 60);
		g.fillRect(20, 200, 80, 60);
		updateMap(20, 200, 80, 60);
		g.fillRect(320, 200, 80, 60);
		updateMap(320, 200, 80, 60);
		g.fillRect(160, 160, 40, 20);
		updateMap(160, 160, 40, 20);
		g.fillRect(220, 160, 40, 20);
		updateMap(220, 160, 40, 20);
		g.fillRect(160, 180, 20, 20);
		updateMap(160, 180, 20, 20);
		g.fillRect(160, 200, 100, 20);
		updateMap(160, 200, 100, 20);
		g.fillRect(240, 180, 20, 20);
		updateMap(240, 180, 20, 20);
		g.setColor(Color.BLUE);
		g.fillRect(120, 120, 60, 20);
		updateMap(120, 120, 60, 20);
		g.fillRect(120, 80, 20, 100);
		updateMap(120, 80, 20, 100);
		g.fillRect(280, 80, 20, 100);
		updateMap(280, 80, 20, 100);
		g.fillRect(240, 120, 60, 20);
		updateMap(240, 120, 60, 20);
		g.fillRect(280, 200, 20, 60);
		updateMap(280, 200, 20, 60);
		g.fillRect(120, 200, 20, 60);
		updateMap(120, 200, 20, 60);
		g.fillRect(160, 240, 100, 20);
		updateMap(160, 240, 100, 20);
		g.fillRect(200, 260, 20, 40);
		updateMap(200, 260, 20, 40);
		g.fillRect(120, 280, 60, 20);
		updateMap(120, 280, 60, 20);
		g.fillRect(240, 280, 60, 20);
		updateMap(240, 280, 60, 20);
		g.fillRect(40, 280, 60, 20);
		updateMap(40, 280, 60, 20);
		g.fillRect(80, 280, 20, 60);
		updateMap(80, 280, 20, 60);
		g.fillRect(320, 280, 60, 20);
		updateMap(320, 280, 60, 20);
		g.fillRect(320, 280, 20, 60);
		updateMap(320, 280, 20, 60);
		g.fillRect(20, 320, 40, 20);
		updateMap(20, 320, 40, 20);
		g.fillRect(360, 320, 40, 20);
		updateMap(360, 320, 40, 20);
		g.fillRect(160, 320, 100, 20);
		updateMap(160, 320, 100, 20);
		g.fillRect(200, 320, 20, 60);
		updateMap(200, 320, 20, 60);
		g.fillRect(40, 360, 140, 20);
		updateMap(40, 360, 140, 20);
		g.fillRect(240, 360, 140, 20);
		updateMap(240, 360, 140, 20);
		g.fillRect(280, 320, 20, 40);
		updateMap(280, 320, 20, 60);
		g.fillRect(120, 320, 20, 60);
		updateMap(120, 320, 20, 60);
		drawLives(g);
	}
	/* Draws the pellets on the screen */
	public void drawPellets(Graphics g)
	{
		g.setColor(Color.YELLOW);
		for (int i=1; i<20; i++)
		{
			for (int j=1; j<20; j++)
			{
				if ( pellets[i-1][j-1])
					g.fillOval(i*20+8, j*20+8, 4, 4);
			}
		}
	}
	/* Draws one individual pellet.  Used to redraw pellets that ghosts have run over */
	public void fillPellet(int x, int y, Graphics g)
	{
		g.setColor(Color.YELLOW);
		g.fillOval(x*20+28, y*20+28, 4, 4);
	}
	public String getNombre() {
		return this.nombre;
	}
	public void setPlayer (Player a) {
		this.player=a;
	}
	public Player getPlayer () {
		return this.player;
	}
}
