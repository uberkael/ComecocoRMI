/* Modificaciones para funcionar en RMI y multiplayer*/
/* Basado en el trabajo de: */
/* Drew Schuster */ /* https://github.com/dtschust/javapacman */
import javax.swing.*;
import java.awt.event.*;
import javax.swing.JApplet;
import java.awt.*;
import java.lang.*;
/* This class contains the entire game... most of the game logic is in the BoardS class but this
creates the gui and captures mouse and keyboard input, as well as controls the game states */
public class Pacman extends JApplet implements MouseListener, KeyListener
{
	/* These timers are used to kill title, game over, and victory screens after a set idle period (5 seconds)*/
	long titleTimer=-1;
	long timer=-1;
	/* Create a new board */
	BoardN b;
	/* This timer is used to do request new frames be drawn*/
	javax.swing.Timer frameTimer;
	/* Contendra el servicio servidor para actualizarle la posicion */
	ServicioPac srv;
	/* This constructor creates the entire game essentially */
	public Pacman(ServicioPac server, BoardN boardNoSerializable)
	{
		/* Asigno el servidor */
		srv=server;
		/* Movido para usar el player de red */
		b=boardNoSerializable;
		/* Asigna la lista de amigos conectados desde el servidor */
		try	{
			b.setAmigos(srv.listaAmigos());
			// System.out.println(srv.posicionPlayer(b.bS.player));
		}
		catch (Exception er) {
			System.err.println("Excepcion en la lista de amigos conectados");
			er.printStackTrace();
		}
		// Parte grafica //
		b.requestFocus();
		/* Create and set up window frame*/
		JFrame f=new JFrame();
		f.setSize(420, 460);
		/* Add the board to the frame */
		f.add(b, BorderLayout.CENTER);
		/*Set listeners for mouse actions and button clicks*/
		b.addMouseListener(this);
		b.addKeyListener(this);
		/* Make frame visible, disable resizing */
		f.setVisible(true);
		f.setResizable(false);
		/* Set the New flag to 1 because this is a new game */
		b.bS.New=1;
		/* Manually call the first frameStep to initialize the game. */
		stepFrame(true);
		/* Create a timer that calls stepFrame every 30 milliseconds */
		frameTimer=new javax.swing.Timer(30, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				stepFrame(false);
			}
		});
		/* Start the timer */
		frameTimer.start();
		b.requestFocus();
	}
	/* This repaint function repaints only the parts of the screen that may have 	changed.
	Namely the area around every player ghost and the menu bars
	*/
	public void repaint()
	{
		if (b.bS.player.teleport)
		{
			b.repaint(b.bS.player.lastX-20, b.bS.player.lastY-20, 80, 80);
			b.bS.player.teleport=false;
		}
		b.repaint(0, 0, 600, 20);
		b.repaint(0, 420, 600, 40);
		/* Repintado de los Amigos */
		if(b.l!=null) {
			for (int i=0; i<b.l.size(); i++) {
				Player a=(Player)b.l.get(i);
				b.repaint(a.x-20, a.y-20, 80, 80);
				}
		}
		b.repaint(b.bS.player.x-20, b.bS.player.y-20, 80, 80);
		b.repaint(b.bS.ghost1.x-20, b.bS.ghost1.y-20, 80, 80);
		b.repaint(b.bS.ghost2.x-20, b.bS.ghost2.y-20, 80, 80);
		b.repaint(b.bS.ghost3.x-20, b.bS.ghost3.y-20, 80, 80);
		b.repaint(b.bS.ghost4.x-20, b.bS.ghost4.y-20, 80, 80);
	}
	/* Steps the screen forward one frame */
	public void stepFrame(boolean New)
	{
		// Envia la posicion actual al servidor, que la imprimirá
		try	{
			srv.updatePlayer(b.bS.player);
			b.setAmigos(srv.listaAmigos());
			// System.out.println(srv.posicionPlayer(b.bS.player));
		}
		catch (Exception er) {
			System.err.println("Excepcion en la posicion o player");
			er.printStackTrace();
		}
		/* If we aren't on a special screen than the timers can be set to-1 to 	disable them */
		if (!b.titleScreen&&!b.winScreen&&!b.overScreen)
		{
			timer=-1;
			titleTimer=-1;
		}
		/* If we are playing the dying animation, keep advancing frames until the 	animation is complete */
		if (b.dying>0)
		{
			b.repaint();
			return;
		}
	/* New can either be specified by the New parameter in stepFrame function call 	or by the state
	of b.New.  Update New accordingly */
		New=New||(b.bS.New!=0) ;
	/* If this is the title screen, make sure to only stay on the title screen for 	5 seconds.
	If after 5 seconds the user hasn't started a game, start up demo mode */
		if (b.titleScreen)
		{
			if (titleTimer==-1)
			{
				titleTimer=System.currentTimeMillis();
			}
			long currTime=System.currentTimeMillis();
			if (currTime-titleTimer>=5000)
			{
				b.titleScreen=false;
				// b.bS.demo=true;
				titleTimer=-1;
			}
			b.repaint();
			return;
		}
	/* If this is the win screen or game over screen, make sure to only stay on 	the screen for 5 seconds.
	If after 5 seconds the user hasn't pressed a key, go to title screen */
		else if (b.winScreen||b.overScreen)
		{
			if (timer==-1)
			{
				timer=System.currentTimeMillis();
			}
			long currTime=System.currentTimeMillis();
			if (currTime-timer>=15000)
			{
				b.winScreen=false;
				b.overScreen=false;
				b.titleScreen=true;
				timer=-1;
			}
			b.repaint();
			return;
		}
	/* If we have a normal game state, move all pieces and update pellet status */
		if (!New)
		{
	/* The pacman player has two functions, demoMove if we're in demo mode and 	move if we're in
	user playable mode.  Call the appropriate one here */
			if (b.bS.demo)
			{
				b.bS.player.demoMove();
			}
			else
			{
				b.bS.player.move();
			}
	/* Also move the ghosts, and update the pellet states */
			/* Repintado de los Amigos */
			if(b.l!=null) {
				for (int i=0; i<b.l.size(); i++) {
					Player a=(Player)b.l.get(i);
					b.repaint(a.x-20, a.y-20, 80, 80);
					}
			}
			b.bS.ghost1.move();
			b.bS.ghost2.move();
			b.bS.ghost3.move();
			b.bS.ghost4.move();
			b.bS.player.updatePellet();
			b.bS.ghost1.updatePellet();
			b.bS.ghost2.updatePellet();
			b.bS.ghost3.updatePellet();
			b.bS.ghost4.updatePellet();
		}
	/* We either have a new game or the user has died, either way we have to reset 	the board */
		if (b.bS.stopped||New)
		{
		/*Temporarily stop advancing frames */
			frameTimer.stop();
		/* If user is dying ... */
			while (b.dying>0)
			{
			/* Play dying animation. */
				stepFrame(false);
			}
		/* Move all game elements back to starting positions and orientations */
			b.bS.player.currDirection='L';
			b.bS.player.direction='L';
			b.bS.player.desiredDirection='L';
			b.bS.player.x=200;
			b.bS.player.y=300;
			b.bS.ghost1.x=180;
			b.bS.ghost1.y=180;
			b.bS.ghost2.x=200;
			b.bS.ghost2.y=180;
			b.bS.ghost3.x=220;
			b.bS.ghost3.y=180;
			b.bS.ghost4.x=220;
			b.bS.ghost4.y=180;
		/* Advance a frame to display main state*/
			b.repaint(0, 0, 600, 600);
		/*Start advancing frames once again*/
			b.bS.stopped=false;
			frameTimer.start();
		}
	/* Otherwise we're in a normal state, advance one frame*/
		else
		{
			repaint();
		}
	}
	/* Handles user key presses*/
	public void keyPressed(KeyEvent e)
	{
		/* Pressing a key in the title screen starts a game */
		if (b.titleScreen)
		{
			b.titleScreen=false;
			return;
		}
		/* Pressing a key in the win screen or game over screen goes to the title 	screen */
		else if (b.winScreen||b.overScreen)
		{
			b.titleScreen=true;
			b.winScreen=false;
			b.overScreen=false;
			return;
		}
		/* Pressing a key during a demo kills the demo mode and starts a new game 	*/
		else if (b.bS.demo)
		{
			b.bS.demo=false;
			/* Stop any pacman eating sounds */
			b.sounds.nomNomStop();
			b.bS.New=1;
			return;
		}
		/* Otherwise, key presses control the player!*/
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_LEFT:
				b.bS.player.desiredDirection='L';
				break;
			case KeyEvent.VK_RIGHT:
				b.bS.player.desiredDirection='R';
				break;
			case KeyEvent.VK_UP:
				b.bS.player.desiredDirection='U';
				break;
			case KeyEvent.VK_DOWN:
				b.bS.player.desiredDirection='D';
				break;
		}
		repaint();
	}
	/* This function detects user clicks on the menu items on the bottom of the 	screen */
	public void mousePressed(MouseEvent e){
		if (b.titleScreen||b.winScreen||b.overScreen)
		{
			/* If we aren't in the game where a menu is showing, ignore clicks */
			return;
		}
		/* Get coordinates of click */
		int x=e.getX();
		int y=e.getY();
		if ( 400<=y&&y<=460)
		{
			if ( 100<=x&&x<=150)
			{
				/* New game has been clicked */
				b.bS.New=1;
			}
			else if (180<=x&&x<=300)
			{
				/* Clear high scores has been clicked */
				// b.clearHighScores();
			}
			else if (350<=x&&x<=420)
			{
				/* Exit has been clicked */
				System.exit(0);
			}
		}
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	/* Main function simply creates a new pacman instance*/
	// public static void main(String [] args)
	// {
	// 	Pacman c=new Pacman(player);
	// }
}
