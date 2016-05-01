/* Tablero comun con la informacion de puntuaciones maximas y pellets */
class Tablero extends Mover implements java.io.Serializable
{
	/* Puntuacion maxima de un jugador */
	int highScore;
	/*Contains the game map, passed to player and ghosts */
	boolean[][] state;
	/* Contains the state of all pellets*/
	boolean[][] pellets;
	/* Constructor */
	public Tablero() {
		highScore=0;
		reset();
		updateMap(40, 40, 60, 20);
		updateMap(120, 40, 60, 20);
		updateMap(200, 20, 20, 40);
		updateMap(240, 40, 60, 20);
		updateMap(320, 40, 60, 20);
		updateMap(40, 80, 60, 20);
		updateMap(160, 80, 100, 20);
		updateMap(200, 80, 20, 60);
		updateMap(320, 80, 60, 20);
		updateMap(20, 120, 80, 60);
		updateMap(320, 120, 80, 60);
		updateMap(20, 200, 80, 60);
		updateMap(320, 200, 80, 60);
		updateMap(160, 160, 40, 20);
		updateMap(220, 160, 40, 20);
		updateMap(160, 180, 20, 20);
		updateMap(160, 200, 100, 20);
		updateMap(240, 180, 20, 20);
		updateMap(120, 120, 60, 20);
		updateMap(120, 80, 20, 100);
		updateMap(280, 80, 20, 100);
		updateMap(240, 120, 60, 20);
		updateMap(280, 200, 20, 60);
		updateMap(120, 200, 20, 60);
		updateMap(160, 240, 100, 20);
		updateMap(200, 260, 20, 40);
		updateMap(120, 280, 60, 20);
		updateMap(240, 280, 60, 20);
		updateMap(40, 280, 60, 20);
		updateMap(80, 280, 20, 60);
		updateMap(320, 280, 60, 20);
		updateMap(320, 280, 20, 60);
		updateMap(20, 320, 40, 20);
		updateMap(360, 320, 40, 20);
		updateMap(160, 320, 100, 20);
		updateMap(200, 320, 20, 60);
		updateMap(40, 360, 140, 20);
		updateMap(240, 360, 140, 20);
		updateMap(280, 320, 20, 60);
		updateMap(120, 320, 20, 60);
		// printState(state);
	}
	/* Reset occurs on a new game*/
	public void reset()
	{
		state=new boolean[20][20];
		pellets=new boolean[20][20];
		/* Clear state and pellets arrays */
		for(int i=0; i<20; i++)
		{
			for(int j=0; j<20; j++)
			{
				state[i][j]=true;
				pellets[i][j]=true;
			}
		}
		/* Handle the weird spots with no pellets*/
		for(int i=5; i<14; i++)
		{
			for(int j=5; j<12; j++)
			{
				pellets[i][j]=false;
			}
		}
		pellets[9][7]=false;
		pellets[8][8]=false;
		pellets[9][8]=false;
		pellets[10][8]=false;
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
	/* Actualiza los pellets */
	public void updatePellets(boolean[][] pella)
	{
		for(int i=0; i<20; i++)
		{
			for(int j=0; j<20; j++)
			{
				this.pellets[i][j]=pella[i][j];
			}
		}
	}
	/* Updates the state information */
	public void updateState(boolean[][] stato)
	{
		for(int i=0; i<20; i++)
		{
			for(int j=0; j<20; j++)
			{
				this.state[i][j]=stato[i][j];
			}
		}
	}
	/* Imprime una matriz con el estado del mapa para debug*/
	public void printState(boolean[][] state)
	{
		System.out.println("Mapa:");
		int num;
		for(int i=0; i<20; i++)
		{
			for(int j=0; j<20; j++)
			{
				num=(this.state[i][j]) ? 1 : 0;
				System.out.print(num+" ");
			}
			System.out.print("\n");
		}
	}
	public int getHighScore() {
		return this.highScore;
	}
	public void setHighScore(int aux) {
		this.highScore=aux;
	}
}
