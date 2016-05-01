import javax.swing.*;
import java.awt.*;
import java.util.List;
/* Clase que contiene el juego no serializable imagenes etc */
/* Actua como un envoltorio para el Board que si se envia por red */
/* Se ejecuta en la parte del cliente y engloba al Board (serializable)*/
public class Board extends JPanel implements java.io.Serializable {
	/* Initialize the player and ghosts */
	Player player;
	Ghost ghost1=new Ghost(180, 180);
	Ghost ghost2=new Ghost(200, 180);
	Ghost ghost3=new Ghost(220, 180);
	Ghost ghost4=new Ghost(220, 180);
	/* Timer is used for playing sound effects and animations */
	long timer=System.currentTimeMillis();
	/* Dying is used to count frames in the dying animation.  If it's non-zero,
	pacman is in the process of dying */
	int dying=0;
	/* Score information */
	int currScore;
	/* if the high scores have been cleared, we have to update the top of the screen to reflect that */
	boolean clearHighScores=false;
	int numLives=2;
	/* Game dimensions */
	int gridSize;
	int max;
	/* State flags*/
	boolean stopped;
	boolean titleScreen;
	boolean winScreen=false;
	boolean overScreen=false;
	boolean demo=false;
	int New;
	/* Used to call sound effects */
	GameSounds sounds;
	int lastPelletEatenX=0;
	int lastPelletEatenY=0;
	/* This is the font used for the menus */
	Font font=new Font("Monospaced", Font.BOLD, 12);
	/* Nombre de este Player*/
	String nombre;
	/* La parte serializable de Board */
	// Board bS;
	/* Lista con los amigos conectados*/
	List<Player> l;
	/* Asignara el objeto Player desde la lista solo la primera vez */
	boolean primeraEjecucion=true;
	/* Tablero comun con la informacion de puntuaciones maximas y pellets */
	Tablero tB;
	/* Contendra el servicio servidor para actualizarle la posicion */
	ServicioPac srv;
	/* Si hay un cambio de estado*/
	boolean cambio=false;
	/* Fantasmas de IA */
	boolean fantasmas;
	/* Costructor */
	// public Board(Board boardSerializable) {
	public Board(ServicioPac server, String a, boolean fantas) {
		nombre=a;
		fantasmas=fantas;
		/* Asigno el servidor */
		srv=server;
		titleScreen=true;
		// bS=boarderializable;
		stopped=false;
		max=400;
		gridSize=20;
		New=0;
		sounds=new GameSounds();
	}
	/* For JAR File*/
	Image pacmanImage=Toolkit.getDefaultToolkit().getImage(Pacman.class.getResource("img/pacman.jpg"));
	Image pacmanUpImage=Toolkit.getDefaultToolkit().getImage(Pacman.class.getResource("img/pacmanup.jpg"));
	Image pacmanDownImage=Toolkit.getDefaultToolkit().getImage(Pacman.class.getResource("img/pacmandown.jpg"));
	Image pacmanLeftImage=Toolkit.getDefaultToolkit().getImage(Pacman.class.getResource("img/pacmanleft.jpg"));
	Image pacmanRightImage=Toolkit.getDefaultToolkit().getImage(Pacman.class.getResource("img/pacmanright.jpg"));
	Image ghost10=Toolkit.getDefaultToolkit().getImage(Pacman.class.getResource("img/ghost10.jpg"));
	Image ghost20=Toolkit.getDefaultToolkit().getImage(Pacman.class.getResource("img/ghost20.jpg"));
	Image ghost30=Toolkit.getDefaultToolkit().getImage(Pacman.class.getResource("img/ghost30.jpg"));
	Image ghost40=Toolkit.getDefaultToolkit().getImage(Pacman.class.getResource("img/ghost40.jpg"));
	Image ghost11=Toolkit.getDefaultToolkit().getImage(Pacman.class.getResource("img/ghost11.jpg"));
	Image ghost21=Toolkit.getDefaultToolkit().getImage(Pacman.class.getResource("img/ghost21.jpg"));
	Image ghost31=Toolkit.getDefaultToolkit().getImage(Pacman.class.getResource("img/ghost31.jpg"));
	Image ghost41=Toolkit.getDefaultToolkit().getImage(Pacman.class.getResource("img/ghost41.jpg"));
	Image titleScreenImage=Toolkit.getDefaultToolkit().getImage(Pacman.class.getResource("img/titleScreen.jpg"));
	Image gameOverImage=Toolkit.getDefaultToolkit().getImage(Pacman.class.getResource("img/gameOver.jpg"));
	Image winScreenImage=Toolkit.getDefaultToolkit().getImage(Pacman.class.getResource("img/winScreen.jpg"));
	/* For NOT JAR file*/
	// Image pacmanImage=(Image)Toolkit.getDefaultToolkit().getImage("img/pacman.jpg");
	// Image pacmanUpImage=(Image)Toolkit.getDefaultToolkit().getImage("img/pacmanup.jpg");
	// Image pacmanDownImage=(Image)Toolkit.getDefaultToolkit().getImage("img/pacmandown.jpg");
	// Image pacmanLeftImage=(Image)Toolkit.getDefaultToolkit().getImage("img/pacmanleft.jpg");
	// Image pacmanRightImage=(Image)Toolkit.getDefaultToolkit().getImage("img/pacmanright.jpg");
	// Image ghost10=(Image)Toolkit.getDefaultToolkit().getImage("img/ghost10.jpg");
	// Image ghost20=(Image)Toolkit.getDefaultToolkit().getImage("img/ghost20.jpg");
	// Image ghost30=(Image)Toolkit.getDefaultToolkit().getImage("img/ghost30.jpg");
	// Image ghost40=(Image)Toolkit.getDefaultToolkit().getImage("img/ghost40.jpg");
	// Image ghost11=(Image)Toolkit.getDefaultToolkit().getImage("img/ghost11.jpg");
	// Image ghost21=(Image)Toolkit.getDefaultToolkit().getImage("img/ghost21.jpg");
	// Image ghost31=(Image)Toolkit.getDefaultToolkit().getImage("img/ghost31.jpg");
	// Image ghost41=(Image)Toolkit.getDefaultToolkit().getImage("img/ghost41.jpg");
	// Image titleScreenImage=(Image)Toolkit.getDefaultToolkit().getImage("img/titleScreen.jpg");
	// Image gameOverImage=(Image)Toolkit.getDefaultToolkit().getImage("img/gameOver.jpg");
	// Image winScreenImage=(Image)Toolkit.getDefaultToolkit().getImage("img/winScreen.jpg");
	/* This is the main function that draws one entire frame of the game */
	public void paint(Graphics g)
	{
	/* If we're playing the dying animation, don't update the entire screen.
	Just kill the pacman*/
		if (dying>0)
		{
			/* Stop any pacman eating sounds */
			sounds.nomNomStop();
			/* Draw the pacman */
			g.drawImage(pacmanImage, player.x, player.y, Color.BLACK, null);
			// pacmanImage.paintIcon(this, g, player.x, player.y);
			g.setColor(Color.BLACK);
			/* Kill the pacman */
			if (dying==4)
				g.fillRect(player.x, player.y, 20, 7);
			else if (dying==3)
				g.fillRect(player.x, player.y, 20, 14);
			else if (dying==2)
				g.fillRect(player.x, player.y, 20, 20);
			else if (dying==1)
			{
				g.fillRect(player.x, player.y, 20, 20);
			}
	/* Take .1 seconds on each frame of death, and then take 2 seconds
	for the final frame to allow for the sound effect to end */
			long currTime=System.currentTimeMillis();
			long temp;
			if (dying!=1)
				temp=100;
			else
				temp=100;
			/* If it's time to draw a new death frame... */
			if (currTime-timer>=temp)
			{
				dying--;
				timer=currTime;
				/* If this was the last death frame...*/
				if (dying==0)
				{
					if (numLives==-1)
					{
						/* Demo mode has infinite lives, just give it more lives*/
						if (demo)
							numLives=2;
						else
						{
							/* Game over for player.  If relevant, update high score.  Set gameOver flag*/
							// if (player.currScore>tB.getHighScore())
							// {
							// 	//TODO//updateScore(player.currScore);
							// }
							overScreen=true;
						}
					}
				}
			}
			return;
		}
		/* If this is the title screen, draw the title screen and return */
		if (titleScreen)
		{
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 600, 600);
			g.drawImage(titleScreenImage, 0, 0, Color.BLACK, null);
			/* Stop any pacman eating sounds */
			sounds.nomNomStop();
			New=1;
			return;
		}
		/* If this is the win screen, draw the win screen and return */
		else if (winScreen)
		{
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 600, 600);
			g.drawImage(winScreenImage, 0, 0, Color.BLACK, null);
			New=1;
			/* Stop any pacman eating sounds */
			sounds.nomNomStop();
			return;
		}
		/* If this is the game over screen, draw the game over screen and return */
		else if (overScreen)
		{
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 600, 600);
			g.drawImage(gameOverImage, 0, 0, Color.BLACK, null);
			New=1;
			/* Stop any pacman eating sounds */
			sounds.nomNomStop();
			return;
		}
		/* If need to update the high scores, redraw the top menu bar */
		if (clearHighScores)
		{
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 600, 18);
			g.setColor(Color.YELLOW);
			g.setFont(font);
			clearHighScores=false;
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 600, 20);
			puntuaciones(g);
		}
		/* oops is set to true when pacman has lost a life */
		boolean oops=false;
		/* Game initialization */
		if (New==1)
		{
			reset();
			// player=new Player(200, 300);
			ghost1=new Ghost(180, 180);
			ghost2=new Ghost(200, 180);
			ghost3=new Ghost(220, 180);
			ghost4=new Ghost(220, 180);
			rmiUpdate();
			player.currScore=0;
			drawBoard(g);
			drawPellets(g);
			drawLives(g);
			/* Send the game map to player and all ghosts */
			player.updateState(tB.state);
			/* Don't let the player go in the ghost box*/
			player.state[9][7]=false;
			/* Manda el mapa a los Amigos */
			if(l!=null) {
				for (int i=0; i<l.size(); i++) {
					Player a=(Player)l.get(i);
					a.updateState(tB.state);
					}
			}
			ghost1.updateState(tB.state);
			ghost2.updateState(tB.state);
			ghost3.updateState(tB.state);
			ghost4.updateState(tB.state);
			/* Draw the top menu bar*/
			puntuaciones(g);
			New++;
		}
		/* Second frame of new game */
		else if (New==2)
		{
			New++;
		}
		/* Third frame of new game */
		else if (New==3)
		{
			New++;
			/* Play the newGame sound effect */
			sounds.newGame();
			timer=System.currentTimeMillis();
			return;
		}
		/* Fourth frame of new game */
		else if (New==4)
		{
			/* Stay in this state until the sound effect is over */
			long currTime=System.currentTimeMillis();
			if (currTime-timer>=5000)
			{
				New=0;
			}
			else
				return;
		}
		/* Drawing optimization */
		/* Optimiza a los Amigos */
		if(l!=null) {
			for (int i=0; i<l.size(); i++) {
				Player a=(Player)l.get(i);
				g.copyArea(a.x-20, a.y-20, 80, 80, 0, 0);
				}
		}
		// g.copyArea(player.x-20, player.y-20, 80, 80, 0, 0);
		g.copyArea(ghost1.x-20, ghost1.y-20, 80, 80, 0, 0);
		g.copyArea(ghost2.x-20, ghost2.y-20, 80, 80, 0, 0);
		g.copyArea(ghost3.x-20, ghost3.y-20, 80, 80, 0, 0);
		g.copyArea(ghost4.x-20, ghost4.y-20, 80, 80, 0, 0);
		/* Detect collisions */
		if(fantasmas&&player.getComecoco()) {
		if (player.x==ghost1.x&&Math.abs(player.y-ghost1.y)<10)
				oops=true;
			else if (player.x==ghost2.x&&Math.abs(player.y-ghost2.y)<10)
				oops=true;
			else if (player.x==ghost3.x&&Math.abs(player.y-ghost3.y)<10)
				oops=true;
			else if (player.x==ghost4.x&&Math.abs(player.y-ghost4.y)<10)
				oops=true;
			else if (player.y==ghost1.y&&Math.abs(player.x-ghost1.x)<10)
				oops=true;
			else if (player.y==ghost2.y&&Math.abs(player.x-ghost2.x)<10)
				oops=true;
			else if (player.y==ghost3.y&&Math.abs(player.x-ghost3.x)<10)
				oops=true;
			else if (player.y==ghost4.y&&Math.abs(player.x-ghost4.x)<10)
			oops=true;
		}
		/* Fantasma matador */
		Player k=PlayerFantasma();
		if (k!=null) {
			if (player.getComecoco()&&player.x==k.x&&Math.abs(player.y-k.y)<10) {
				oops=true;
			}
			k=null; // Limpia var
		}
		/* Kill the pacman */
		// if (oops&&!stopped) // TODO
		if (oops)
		{
			/* 4 frames of death*/
			dying=4;
			/* Play death sound effect */
			sounds.death();
			/* Stop any pacman eating sounds */
			sounds.nomNomStop();
			/*Decrement lives, update screen to reflect that.  And set appropriate flags and timers */
			numLives--;
			stopped=true;
			drawLives(g);
			timer=System.currentTimeMillis();
		}
		/* Delete the players and ghosts */
		g.setColor(Color.BLACK);
		g.fillRect(player.lastX, player.lastY, 20, 20);
		// Borra los amigos
		borrandoAmigos(l, g);
		g.fillRect(ghost1.lastX, ghost1.lastY, 20, 20);
		g.fillRect(ghost2.lastX, ghost2.lastY, 20, 20);
		g.fillRect(ghost3.lastX, ghost3.lastY, 20, 20);
		g.fillRect(ghost4.lastX, ghost4.lastY, 20, 20);
		/* Dibuja Scores con cambio de pacman a fantasma */
		if (cambio) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 600, 20);
			puntuaciones(g);
			pintandoAmigos(l, g);
			cambio=false;
		}
		/* Eat pellets */ // Solo si es comecoco
		if (player.getComecoco()&&tB.pellets[player.pelletX][player.pelletY]&&New!=2&&New!=3)
		{
			lastPelletEatenX=player.pelletX;
			lastPelletEatenY=player.pelletY;
			/* Play eating sound */
			sounds.nomNom();
			/* Increment pellets eaten value to track for end game */
			player.pelletsEaten++;
			/* Delete the pellet*/
			tB.pellets[player.pelletX][player.pelletY]=false;
			/* Increment the score */
			player.currScore+=50;
			/* Update the screen to reflect the new score */
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 600, 20);
			puntuaciones(g);
			/* If this was the last pellet */
			if (player.pelletsEaten==173)
			{
				/*Demo mode can't get a high score */
				if (!demo)
				{
					// if (player.currScore>tB.getHighScore())
					// {
					// 	//updateScore(player.currScore);
					// }
					winScreen=true;
				}
				else
				{
					titleScreen=true;
				}
				return;
			}
		}
		/* If we moved to a location without pellets, stop the sounds */
		else if ( (player.pelletX!=lastPelletEatenX||player.pelletY!=lastPelletEatenY )||player.stopped)
		{
			/* Stop any pacman eating sounds */
			sounds.nomNomStop();
		}
		/* Replace pellets that have been run over by ghosts */
		if(fantasmas) {
			if(tB.pellets[ghost1.lastPelletX][ghost1.lastPelletY])
				fillPellet(ghost1.lastPelletX, ghost1.lastPelletY, g);
			if(tB.pellets[ghost2.lastPelletX][ghost2.lastPelletY])
				fillPellet(ghost2.lastPelletX, ghost2.lastPelletY, g);
			if(tB.pellets[ghost3.lastPelletX][ghost3.lastPelletY])
				fillPellet(ghost3.lastPelletX, ghost3.lastPelletY, g);
			if(tB.pellets[ghost4.lastPelletX][ghost4.lastPelletY])
				fillPellet(ghost4.lastPelletX, ghost4.lastPelletY, g);
		}
		/* Remplaza los pellets por donde pasan los amigos fantasmas*/
		for (int i=0; i<l.size(); i++) {
			Player a=(Player)l.get(i);
			// if (!a.getComecoco()&&tB.pellets[a.lastPelletX][a.lastPelletY])
			if (!a.getComecoco()) {
				fillPellet(a.lastPelletX, a.lastPelletY, g);
			}
		}
		/*Draw the ghosts */
		if (ghost1.frameCount<500)
		{
			if(fantasmas) {
				/* Draw first frame of ghosts */
				g.drawImage(ghost20, ghost1.x, ghost1.y, Color.BLACK, null);
				// ghost10.paintIcon(this, g, ghost1.x, ghost1.y);
				g.drawImage(ghost20, ghost2.x, ghost2.y, Color.BLACK, null);
				// ghost20.paintIcon(this, g, ghost2.x, ghost2.y);
				g.drawImage(ghost30, ghost3.x, ghost3.y, Color.BLACK, null);
				// ghost30.paintIcon(this, g, ghost3.x, ghost3.y);
				g.drawImage(ghost40, ghost4.x, ghost4.y, Color.BLACK, null);
				// ghost40.paintIcon(this, g, ghost4.x, ghost4.y);
				}
			ghost1.frameCount++;
		}
		else
		{
			if(fantasmas) {
				/* Draw second frame of ghosts */
				g.drawImage(ghost21, ghost1.x, ghost1.y, Color.BLACK, null);
				// ghost11.paintIcon(this, g, ghost1.x, ghost1.y);
				g.drawImage(ghost21, ghost2.x, ghost2.y, Color.BLACK, null);
				// ghost21.paintIcon(this, g, ghost2.x, ghost2.y);
				g.drawImage(ghost31, ghost3.x, ghost3.y, Color.BLACK, null);
				// ghost31.paintIcon(this, g, ghost3.x, ghost3.y);
				g.drawImage(ghost41, ghost4.x, ghost4.y, Color.BLACK, null);
				// ghost41.paintIcon(this, g, ghost4.x, ghost4.y);
			}
			if (ghost1.frameCount>=1000)
				ghost1.frameCount=0;
			else
				ghost1.frameCount++;
		}
		// Dibuja los Amigos Pacmans
		pintandoAmigos(l, g);
		/* Draw the border around the game in case it was overwritten by ghost movement or something */
		g.setColor(Color.WHITE);
		g.drawRect(19, 19, 382, 382);
		/* Draw the border around the game in case it was overwritten by ghost movement or something */
		g.setColor(Color.WHITE);
		g.drawRect(19, 19, 382, 382);
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
		g.setColor(Color.RED); // Agregado
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
		g.fillRect(120, 40, 60, 20);
		g.fillRect(200, 20, 20, 40);
		g.fillRect(240, 40, 60, 20);
		g.fillRect(320, 40, 60, 20);
		g.fillRect(40, 80, 60, 20);
		g.fillRect(160, 80, 100, 20);
		g.fillRect(200, 80, 20, 60);
		g.fillRect(320, 80, 60, 20);
		g.fillRect(20, 120, 80, 60);
		g.fillRect(320, 120, 80, 60);
		g.fillRect(20, 200, 80, 60);
		g.fillRect(320, 200, 80, 60);
		g.fillRect(160, 160, 40, 20);
		g.fillRect(220, 160, 40, 20);
		g.fillRect(160, 180, 20, 20);
		g.fillRect(160, 200, 100, 20);
		g.fillRect(240, 180, 20, 20);
		g.setColor(Color.BLUE);
		g.fillRect(120, 120, 60, 20);
		g.fillRect(120, 80, 20, 100);
		g.fillRect(280, 80, 20, 100);
		g.fillRect(240, 120, 60, 20);
		g.fillRect(280, 200, 20, 60);
		g.fillRect(120, 200, 20, 60);
		g.fillRect(160, 240, 100, 20);
		g.fillRect(200, 260, 20, 40);
		g.fillRect(120, 280, 60, 20);
		g.fillRect(240, 280, 60, 20);
		g.fillRect(40, 280, 60, 20);
		g.fillRect(80, 280, 20, 60);
		g.fillRect(320, 280, 60, 20);
		g.fillRect(320, 280, 20, 60);
		g.fillRect(20, 320, 40, 20);
		g.fillRect(360, 320, 40, 20);
		g.fillRect(160, 320, 100, 20);
		g.fillRect(200, 320, 20, 60);
		g.fillRect(40, 360, 140, 20);
		g.fillRect(240, 360, 140, 20);
		g.fillRect(280, 320, 20, 40);
		g.fillRect(120, 320, 20, 60);
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
				if (tB.pellets[i-1][j-1])
					g.fillOval(i*20+8, j*20+8, 4, 4);
			}
		}
	}
	/* Draws one individual pellet.  Used to redraw pellets that ghosts have run over */
	public void fillPellet(int x, int y, Graphics g)
	{
		Color a=g.getColor(); // Necesito restaurar el color
		g.setColor(Color.YELLOW);
		g.fillOval(x*20+28, y*20+28, 4, 4);
		g.setColor(a); // Necesito restaurar el color
	}
	/* Devuelve el player de la lista segun el su nombre */
	public Player PlayerPropio(String nombre) throws Exception {
		Player a=null;
		for (int i=0; i<l.size(); i++) {
			Player p=(Player)l.get(i);
			if (p.getNombre().indexOf(nombre)!=-1) {
				// Encontrado el player
				a=p;
			}
		}
		if (a!=null) {
			return a;
		}
		else {
			throw new Exception(nombre+" no encontrado.");
		}
	}
	/* Devuelve el player Fantasma */
	public Player PlayerFantasma() {
		for (int i=0; i<l.size(); i++) {
			Player p=(Player)l.get(i);
			if (!p.getComecoco()) {
				return p;
			}
		}
		return null;
	}
	/* Funcion para actualizar los datos del servidor y del player */
	public void rmiUpdate() {
		// Actualiza los datos del player en servidor
		if (player!=null) {
			try	{
				srv.updatePlayer(player);
			}
			catch (Exception er) {
				System.err.println("Excepcion enviando datos");
				er.printStackTrace();
			}
		}
		// Actualiza los datos del mapa en servidor
		if(tB!=null) {
			try	{
				srv.updateTablero(tB);
			}
			catch (Exception er) {
				System.err.println("Excepcion enviando datos");
				er.printStackTrace();
			}
		}
		// Pide la lista al servidor
		try	{
			setAmigos(srv.listaAmigos());
		}
		catch (Exception er) {
			System.err.println("Excepcion en la lista de amigos conectados");
			er.printStackTrace();
		}
		// Pide el tablero al servidor
		try	{
			setTablero(srv.getTablero());
		}
		catch (Exception er) {
			System.err.println("Excepcion en el tablero");
			er.printStackTrace();
		}
		if(primeraEjecucion) {primeraEjecucion=false; }
		updatePlayerData();
	}

	public void setAmigos(List<Player> a) {
		boolean aumento=false;
		// Si existe l, se comprueba si han aumentado los clientes
		if(l!=null) {aumento=(a.size()!=l.size()); }
		// Se asigna siempre la lueva lista para actualizar los datos
		// De todos los amigos y sus posiciones
		l=a;
		// Si es la primera ejecucion o son distintos se imprime la lista de usuarios
		if (aumento||primeraEjecucion) {
			if(primeraEjecucion) {
				System.out.println("= Conectados =");
				// Tambien asignamos un Player desde la lista al juego
				try {
					player=PlayerPropio(getNombre());
				}
				catch (Exception e) {
					System.err.println("Excepcion en Amigos: ");
					e.printStackTrace();
				}

			}
		}
		imprimeListaAmigos();
	}
	/* Actualiza las pellets y los estados con la nueva informacion */
	public void setTablero(Tablero a) {
		if(primeraEjecucion) {
			this.tB=a;
		}
		else {
			for(int i=0; i<20; i++)
			{
				for(int j=0; j<20; j++)
				{
					tB.pellets[i][j]=a.pellets[i][j];
				}
			}
			tB.updateState(a.state);
			tB.setHighScore(a.getHighScore());
		}
	}
	/* Imprime los amigos conectados */
	public void imprimeListaAmigos() {
		System.out.print("\r");
		for (int i=0; i<l.size(); i++) {
			Player p=(Player)l.get(i);
			System.out.print(p.getNombre()+" "+p.getScore()+"\t ");
		}
	}
	/* Actualiza los datos del player con la nueva informacion*/
	public void updatePlayerData() {
		for (int i=0; i<l.size(); i++) {
			Player p=(Player)l.get(i);
			if(p.getNombre().indexOf(player.getNombre())!=-1) {
				if (player.getComecoco()!=p.getComecoco()) {
					cambio=true; // Actualiza los graficos si hubo cambio
				}
				player.setComecoco(p.getComecoco());
			}
		}
	}
	// Actualiza los graficos para no repetir el texto
	public void puntuaciones(Graphics g) {
		g.setColor(Color.YELLOW);
		g.setFont(font);
		if (demo)
			g.drawString("DEMO MODE PRESS ANY KEY TO START A GAME\t High Score: "+tB.getHighScore(), 20, 10);
		else {
			if(player.getComecoco()) {
				g.drawString("Score: "+(player.currScore)+"\t High Score: "+tB.getHighScore()+" Eres Pacman!", 20, 10);
			}
			else {
				g.drawString("Score: "+(player.currScore)+"\t High Score: "+tB.getHighScore()+" Fantasma", 20, 10);
			}
		}
		rmiUpdate(); // Actualiza con la nueva puntuacion
	}
	public void pintandoAmigos(List<Player> l, Graphics g) {
		// Borra los Amigos
		borrandoAmigos(l, g);
		// Dibuja los Amigos Pacmans
		for (int i=0; i<l.size(); i++) {
			Player a=(Player)l.get(i);
			// Si es nuestro propio player
			if(a.getNombre().indexOf(player.getNombre())!=-1) {
				a=player;
				// Si es comecoco se pinta
				if(a.getComecoco()) {
					/* Draw the pacman */
					if (player.frameCount<5)
					{
						/* Draw mouth closed */
						g.drawImage(pacmanImage, a.x, a.y, Color.BLACK, null);
					}
					else
					{
						/* Draw mouth open in appropriate direction */
						if (player.frameCount>=10)
							player.frameCount=0;
						switch(a.currDirection)
						{
							case 'L':
							g.drawImage(pacmanLeftImage, a.x, a.y, Color.BLACK, null);
							break;
							case 'R':
							g.drawImage(pacmanRightImage, a.x, a.y, Color.BLACK, null);
							break;
							case 'U':
							g.drawImage(pacmanUpImage, a.x, a.y, Color.BLACK, null);
							break;
							case 'D':
							g.drawImage(pacmanDownImage, a.x, a.y, Color.BLACK, null);
							break;
						}
					}
				}
				// Por defecto se pintan fantasmas
				else {
					if(ghost1.frameCount<500) {
						g.drawImage(ghost10, a.x, a.y, Color.BLACK, null);
						// ghost1.frameCount++;
					}
					else {
						// if (ghost1.frameCount>=10)
						// 	ghost1.frameCount=0;
						// else
						// 	ghost1.frameCount++;
						g.drawImage(ghost11, a.x, a.y, Color.BLACK, null);
					}
				}
			}
			else {
				// Si es comecoco se pinta
				if(a.getComecoco()) {
					/* Draw the pacman */
					if (ghost1.frameCount<50)
					{
						/* Draw mouth closed */
						g.drawImage(pacmanImage, a.x, a.y, Color.BLACK, null);
					}
					else
					{
						/* Draw mouth open in appropriate direction */
						switch(a.currDirection)
						{
							case 'L':
							g.drawImage(pacmanLeftImage, a.x, a.y, Color.BLACK, null);
							break;
							case 'R':
							g.drawImage(pacmanRightImage, a.x, a.y, Color.BLACK, null);
							break;
							case 'U':
							g.drawImage(pacmanUpImage, a.x, a.y, Color.BLACK, null);
							break;
							case 'D':
							g.drawImage(pacmanDownImage, a.x, a.y, Color.BLACK, null);
							break;
						}
					}
				}
				// Por defecto se pintan fantasmas
				else {
					if(ghost1.frameCount<50) {
						g.drawImage(ghost10, a.x, a.y, Color.BLACK, null);
						// ghost1.frameCount++;
					}
					else {
						// if (ghost1.frameCount>=10)
						// 	ghost1.frameCount=0;
						// else
						// 	ghost1.frameCount++;
						g.drawImage(ghost11, a.x, a.y, Color.BLACK, null);
					}
				}
			}

		}
		/* Repintado de los Amigos */
		if(l!=null) {
			for (int i=0; i<l.size(); i++) {
				Player a=(Player)l.get(i);
				repaint(a.x-20, a.y-20, 80, 80);
				}
		}
	}
	public void borrandoAmigos(List<Player> l, Graphics g) {
		// Borra los amigos
		for (int i=0; i<l.size(); i++) {
			Player p=(Player)l.get(i);
			g.fillRect(p.lastX, p.lastY, 20, 20);
		}
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
	public void reset() {
		numLives=2;
	}
}
