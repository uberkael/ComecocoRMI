import javax.swing.*;
import java.awt.*;
import java.util.List;

/* Clase que contiene el juego no serializable imagenes etc */
/* Actua como un envoltorio para el BoardS que si se envia por red */
/* Se ejecuta en la parte del cliente y engloba al BoardS (serializable)*/
public class BoardN extends JPanel implements java.io.Serializable {
	/* La parte serializable de Board */
	BoardS bS;
	/* Used to call sound effects */
	GameSounds sounds;

	/* Banderas de estado*/
	boolean titleScreen;
	boolean winScreen=false;
	boolean overScreen=false;
	/* Dying is used to count frames in the dying animation.  If it's non-zero,
	pacman is in the process of dying */
	int dying=0;
	/* Lista con los amigos conectados*/
	List<Player> l;
	/* Asignara el objeto Player desde la lista solo la primera vez */
	boolean primeraEjecucion=true;
	/* Costructor */
	public BoardN(BoardS boardSerializable) {
		titleScreen=true;
		bS=boardSerializable;
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
			g.drawImage(pacmanImage, bS.player.x, bS.player.y, Color.BLACK, null);
			// pacmanImage.paintIcon(this, g, player.x, player.y);
			g.setColor(Color.BLACK);
			/* Kill the pacman */
			if (dying==4)
				g.fillRect(bS.player.x, bS.player.y, 20, 7);
			else if (dying==3)
				g.fillRect(bS.player.x, bS.player.y, 20, 14);
			else if (dying==2)
				g.fillRect(bS.player.x, bS.player.y, 20, 20);
			else if (dying==1)
			{
				g.fillRect(bS.player.x, bS.player.y, 20, 20);
			}
	/* Take .1 seconds on each frame of death, and then take 2 seconds
	for the final frame to allow for the sound effect to end */
			long currTime=System.currentTimeMillis();
			long temp;
			if (dying!=1)
				temp=100;
			else
				temp=2000;
			/* If it's time to draw a new death frame... */
			if (currTime-bS.timer>=temp)
			{
				dying--;
				bS.timer=currTime;
				/* If this was the last death frame...*/
				if (dying==0)
				{
					if (bS.numLives==-1)
					{
						/* Demo mode has infinite lives, just give it more lives*/
						if (bS.demo)
							bS.numLives=2;
						else
						{
							/* Game over for player.  If relevant, update high score.  Set gameOver flag*/
							if (bS.player.currScore>bS.highScore)
							{
								bS.updateScore(bS.player.currScore);
							}
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
			bS.New=1;
			return;
		}
		/* If this is the win screen, draw the win screen and return */
		else if (winScreen)
		{
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 600, 600);
			g.drawImage(winScreenImage, 0, 0, Color.BLACK, null);
			bS.New=1;
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
			bS.New=1;
			/* Stop any pacman eating sounds */
			sounds.nomNomStop();
			return;
		}
		/* If need to update the high scores, redraw the top menu bar */
		if (bS.clearHighScores)
		{
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 600, 18);
			g.setColor(Color.YELLOW);
			g.setFont(bS.font);
			bS.clearHighScores=false;
			if (bS.demo)
				g.drawString("DEMO MODE PRESS ANY KEY TO START A GAME\t High Score: "+bS.highScore, 20, 10);
			else
				g.drawString("Score: "+(bS.player.currScore)+"\t High Score: "+bS.highScore, 20, 10);
		}
		/* oops is set to true when pacman has lost a life */
		boolean oops=false;
		/* Game initialization */
		if (bS.New==1)
		{
			bS.reset();
			// player=new Player(200, 300);
			bS.ghost1=new Ghost(180, 180);
			bS.ghost2=new Ghost(200, 180);
			bS.ghost3=new Ghost(220, 180);
			bS.ghost4=new Ghost(220, 180);
			bS.player.currScore=0;
			bS.drawBoard(g);
			bS.drawPellets(g);
			bS.drawLives(g);
			/* Send the game map to player and all ghosts */
			bS.player.updateState(bS.state);
			/* Don't let the player go in the ghost box*/
			bS.player.state[9][7]=false;
			//			/* Manda el mapa a los Amigos */
			//if(l!=null) {
			//	for (int i=0; i<l.size(); i++) {
			//		Player a=(Player)l.get(i);
			//		a.updateState(bS.state);
			//		}
			//}
			/* Manda el mapa a los Amigos */
			if(l!=null) {
				for (int i=0; i<l.size(); i++) {
					Player a=(Player)l.get(i);
					a.updateState(bS.state);
					}
			}
			bS.ghost1.updateState(bS.state);
			bS.ghost2.updateState(bS.state);
			bS.ghost3.updateState(bS.state);
			bS.ghost4.updateState(bS.state);
			/* Draw the top menu bar*/
			g.setColor(Color.YELLOW);
			g.setFont(bS.font);
			if (bS.demo)
				g.drawString("DEMO MODE PRESS ANY KEY TO START A GAME\t High Score: "+bS.highScore, 20, 10);
			else
				g.drawString("Score: "+(bS.player.currScore)+"\t High Score: "+bS.highScore, 20, 10);
			bS.New++;
		}
		/* Second frame of new game */
		else if (bS.New==2)
		{
			bS.New++;
		}
		/* Third frame of new game */
		else if (bS.New==3)
		{
			bS.New++;
			/* Play the newGame sound effect */
			sounds.newGame();
			bS.timer=System.currentTimeMillis();
			return;
		}
		/* Fourth frame of new game */
		else if (bS.New==4)
		{
			/* Stay in this state until the sound effect is over */
			long currTime=System.currentTimeMillis();
			if (currTime-bS.timer>=5000)
			{
				bS.New=0;
			}
			else
				return;
		}
		/* Drawing optimization */
		/* Optimiza a los Amigos */
		//if(l!=null) {
		//	for (int i=0; i<l.size(); i++) {
		//		Player a=(Player)l.get(i);
		//		g.copyArea(a.x-20, a.y-20, 80, 80, 0, 0);
		//		}
		//}
		g.copyArea(bS.player.x-20, bS.player.y-20, 80, 80, 0, 0);
		g.copyArea(bS.ghost1.x-20, bS.ghost1.y-20, 80, 80, 0, 0);
		g.copyArea(bS.ghost2.x-20, bS.ghost2.y-20, 80, 80, 0, 0);
		g.copyArea(bS.ghost3.x-20, bS.ghost3.y-20, 80, 80, 0, 0);
		g.copyArea(bS.ghost4.x-20, bS.ghost4.y-20, 80, 80, 0, 0);
		/* Detect collisions */
		if (bS.player.x==bS.ghost1.x&&Math.abs(bS.player.y-bS.ghost1.y)<10)
			oops=true;
		else if (bS.player.x==bS.ghost2.x&&Math.abs(bS.player.y-bS.ghost2.y)<10)
			oops=true;
		else if (bS.player.x==bS.ghost3.x&&Math.abs(bS.player.y-bS.ghost3.y)<10)
			oops=true;
		else if (bS.player.x==bS.ghost4.x&&Math.abs(bS.player.y-bS.ghost4.y)<10)
			oops=true;
		else if (bS.player.y==bS.ghost1.y&&Math.abs(bS.player.x-bS.ghost1.x)<10)
			oops=true;
		else if (bS.player.y==bS.ghost2.y&&Math.abs(bS.player.x-bS.ghost2.x)<10)
			oops=true;
		else if (bS.player.y==bS.ghost3.y&&Math.abs(bS.player.x-bS.ghost3.x)<10)
			oops=true;
		else if (bS.player.y==bS.ghost4.y&&Math.abs(bS.player.x-bS.ghost4.x)<10)
			oops=true;
		/* Kill the pacman */
		if (oops&&!bS.stopped)
		{
			/* 4 frames of death*/
			dying=4;
			/* Play death sound effect */
			sounds.death();
			/* Stop any pacman eating sounds */
			sounds.nomNomStop();
			/*Decrement lives, update screen to reflect that.  And set appropriate flags and timers */
			bS.numLives--;
			bS.stopped=true;
			bS.drawLives(g);
			bS.timer=System.currentTimeMillis();
		}
		/* Delete the players and ghosts */
		g.setColor(Color.BLACK);
		g.fillRect(bS.player.lastX, bS.player.lastY, 20, 20);
		// Borra los amigos
		for (int i=0; i<l.size(); i++) {
			Player p=(Player)l.get(i);
			g.fillRect(p.lastX, p.lastY, 20, 20);
		}
		g.fillRect(bS.ghost1.lastX, bS.ghost1.lastY, 20, 20);
		g.fillRect(bS.ghost2.lastX, bS.ghost2.lastY, 20, 20);
		g.fillRect(bS.ghost3.lastX, bS.ghost3.lastY, 20, 20);
		g.fillRect(bS.ghost4.lastX, bS.ghost4.lastY, 20, 20);
		/* Eat pellets */
		if ( bS.pellets[bS.player.pelletX][bS.player.pelletY]&&bS.New!=2&&bS.New!=3)
		{
			bS.lastPelletEatenX=bS.player.pelletX;
			bS.lastPelletEatenY=bS.player.pelletY;
			/* Play eating sound */
			sounds.nomNom();
			/* Increment pellets eaten value to track for end game */
			bS.player.pelletsEaten++;
			/* Delete the pellet*/
			bS.pellets[bS.player.pelletX][bS.player.pelletY]=false;
			/* Increment the score */
			bS.player.currScore+=50;
			/* Update the screen to reflect the new score */
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 600, 20);
			g.setColor(Color.YELLOW);
			g.setFont(bS.font);
			if (bS.demo)
				g.drawString("DEMO MODE PRESS ANY KEY TO START A GAME\t High Score: "+bS.highScore, 20, 10);
			else
				g.drawString("Score: "+(bS.player.currScore)+"\t High Score: "+bS.highScore, 20, 10);
			/* If this was the last pellet */
			if (bS.player.pelletsEaten==173)
			{
				/*Demo mode can't get a high score */
				if (!bS.demo)
				{
					if (bS.player.currScore>bS.highScore)
					{
						bS.updateScore(bS.player.currScore);
					}
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
		else if ( (bS.player.pelletX!=bS.lastPelletEatenX||bS.player.pelletY!=bS.lastPelletEatenY )||bS.player.stopped)
		{
			/* Stop any pacman eating sounds */
			sounds.nomNomStop();
		}
		/* Replace pellets that have been run over by ghosts */
		if ( bS.pellets[bS.ghost1.lastPelletX][bS.ghost1.lastPelletY])
			bS.fillPellet(bS.ghost1.lastPelletX, bS.ghost1.lastPelletY, g);
		if ( bS.pellets[bS.ghost2.lastPelletX][bS.ghost2.lastPelletY])
			bS.fillPellet(bS.ghost2.lastPelletX, bS.ghost2.lastPelletY, g);
		if ( bS.pellets[bS.ghost3.lastPelletX][bS.ghost3.lastPelletY])
			bS.fillPellet(bS.ghost3.lastPelletX, bS.ghost3.lastPelletY, g);
		if ( bS.pellets[bS.ghost4.lastPelletX][bS.ghost4.lastPelletY])
			bS.fillPellet(bS.ghost4.lastPelletX, bS.ghost4.lastPelletY, g);
		/* Remplaza los pellets por donde pasan los amigos*/
		//if(l!=null) {
		//	for (int i=0; i<l.size(); i++) {
		//		Player a=(Player)l.get(i);
		//		// TÒDO evitar que lo haga con pacman
		//		if ( bS.pellets[a.lastPelletX][a.lastPelletY])
		//			bS.fillPellet(a.lastPelletX, a.lastPelletY, g);
		//		}
		//}
		/*Draw the ghosts */
		if (bS.ghost1.frameCount<5)
		{
			/* Draw first frame of ghosts */
			g.drawImage(ghost20, bS.ghost1.x, bS.ghost1.y, Color.BLACK, null);
			// ghost10.paintIcon(this, g, bS.ghost1.x, bS.ghost1.y);
			g.drawImage(ghost20, bS.ghost2.x, bS.ghost2.y, Color.BLACK, null);
			// ghost20.paintIcon(this, g, bS.ghost2.x, bS.ghost2.y);
			g.drawImage(ghost30, bS.ghost3.x, bS.ghost3.y, Color.BLACK, null);
			// ghost30.paintIcon(this, g, bS.ghost3.x, bS.ghost3.y);
			g.drawImage(ghost40, bS.ghost4.x, bS.ghost4.y, Color.BLACK, null);
			// ghost40.paintIcon(this, g, bS.ghost4.x, bS.ghost4.y);
			bS.ghost1.frameCount++;
		}
		else
		{
			/* Draw second frame of ghosts */
			g.drawImage(ghost21, bS.ghost1.x, bS.ghost1.y, Color.BLACK, null);
			// ghost11.paintIcon(this, g, bS.ghost1.x, bS.ghost1.y);
			g.drawImage(ghost21, bS.ghost2.x, bS.ghost2.y, Color.BLACK, null);
			// ghost21.paintIcon(this, g, bS.ghost2.x, bS.ghost2.y);
			g.drawImage(ghost31, bS.ghost3.x, bS.ghost3.y, Color.BLACK, null);
			// ghost31.paintIcon(this, g, bS.ghost3.x, bS.ghost3.y);
			g.drawImage(ghost41, bS.ghost4.x, bS.ghost4.y, Color.BLACK, null);
			// ghost41.paintIcon(this, g, bS.ghost4.x, bS.ghost4.y);
			if (bS.ghost1.frameCount>=10)
				bS.ghost1.frameCount=0;
			else
				bS.ghost1.frameCount++;
		}
		// Dibuja los Amigos Pacmans
		for (int i=0; i<l.size(); i++) {
			Player a=(Player)l.get(i);
				// Si es nuestro propio player
			if(a.getNombre()==bS.getNombre()) {
					a=bS.player; // Sobreescribimos para usar el nuestro
				}
				// Si es comecoco se pinta
				if(a.getComecoco()) {
					/* Draw the pacman */
					if (bS.player.frameCount<5)
					{
						/* Draw mouth closed */
						g.drawImage(pacmanImage, bS.player.x, bS.player.y, Color.BLACK, null);
					}
					else
					{
						/* Draw mouth open in appropriate direction */
						if (bS.player.frameCount>=10)
							bS.player.frameCount=0;

						switch(bS.player.currDirection)
						{
							case 'L':
							g.drawImage(pacmanLeftImage, bS.player.x, bS.player.y, Color.BLACK, null);
							break;
							case 'R':
							g.drawImage(pacmanRightImage, bS.player.x, bS.player.y, Color.BLACK, null);
							break;
							case 'U':
							g.drawImage(pacmanUpImage, bS.player.x, bS.player.y, Color.BLACK, null);
							break;
							case 'D':
							g.drawImage(pacmanDownImage, bS.player.x, bS.player.y, Color.BLACK, null);
							break;
						}
					}
				}
				// Por defecto se pintan fantasmas
				else {
					if(a.frameCount<5) {
						g.drawImage(ghost10, a.x, a.y, Color.BLACK, null);
					}
					else {
						g.drawImage(ghost11, a.x, a.y, Color.BLACK, null);
					}
				}
			}

		/* Draw the border around the game in case it was overwritten by ghost movement or something */
		g.setColor(Color.WHITE);
		g.drawRect(19, 19, 382, 382);
		/* Draw the border around the game in case it was overwritten by ghost movement or something */
		g.setColor(Color.WHITE);
		g.drawRect(19, 19, 382, 382);
		}

	/* Devuelve el player de la lista segun el su nombre*/
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
	public void setAmigos(List<Player> a) {
		boolean aumento=false;
		// Si existe l, se comprueba si han aumentado los clientes
		if(l!=null) {aumento=(a.size()!=l.size()); }
		// Se asigna siempre la lueva lista para actualizar los datos
		// De todos los amigos y sus posiciones
		l=a;
		// Si son distintos se imprime la lista de usuarios
		if (aumento) {
			imprimeListaAmigos();
		}
		// Si es la primera ejecucion se asigna un Player fijo al BoardS
		if(primeraEjecucion) {
			primeraEjecucion=false;
			try {
				// Tambien asignamos un Player fijo al juego
				bS.setPlayer(PlayerPropio(bS.getNombre()));
			}
			catch (Exception e) {
				System.err.println("Excepcion en Amigos: ");
				e.printStackTrace();
			}
		}
		updateComecocoStatus();
		super.repaint();
	}
	public void imprimeListaAmigos() {
		for (int i=0; i<l.size(); i++) {
			Player p=(Player)l.get(i);
			System.out.println("= Conectados =");
			System.out.println(p.getNombre());
		}
	}
	public void updateComecocoStatus() {
		for (int i=0; i<l.size(); i++) {
			Player p=(Player)l.get(i);
			if(p.getNombre()==bS.player.getNombre()) {
				bS.player.setComecoco(p.getComecoco());
			}
		}
	}
}