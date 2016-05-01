import java.io.File;
import java.io.PrintWriter;
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
class ServicioPacImpl extends UnicastRemoteObject implements ServicioPac {
	// Contiene una lista de players
	List<Player> l;
	// Tablero comun con los datos y puntuaciones
	Tablero tB;
	// Constructor
	ServicioPacImpl() throws RemoteException {
		tB=new Tablero();
		l=new LinkedList<Player>();
	}
	/* Añade un jugador a la lista y la devuelve */
	public void crearPlayer(String nombre) throws RemoteException {
		// Comprueba si el jugador ya existe
		for (int i=0; i<l.size(); i++) {
			Player p=(Player)l.get(i);
			if (p.getNombre().indexOf(nombre)!=-1) {
				throw new RemoteException("El nombre ya existe.");
			}
		}
		// Impide la colision al crearse con el fantasma
		int x=200;
		int y=300;
		// Si no lo agrega a la lista y lo devuelve al cliente
		Player c=new Player(x, y, nombre);
		l.add(c);
		System.out.println("Lista de Clientes:");
		try	{imprimeListaAmigos();} catch (Exception e) {}
		// System.out.println("Player agregado: "+c.getNombre());
	}
	/* Devuelve la posicion del jugador*/
	public String posicionPlayer(Player jugador) throws RemoteException {
		String posi="Posicion de"+jugador.getNombre()+"\tX:"+jugador.getX()+"\tY: "+jugador.getY()+"\tPuntuacion: "+jugador.getScore();
		// System.out.println(posi); // Lo imprime en el servidor tambien
		return posi;
	}
	/* Actualiza la posicion del jugador remoto en la lista*/
	public void updatePlayer(Player jugador) throws Exception {
		boolean encontrado=false;
		for (int i=0; i<l.size(); i++) {
			Player p=l.get(i);
			// El servidor actualiza quien es el Comecoco con mas putuacion
			if (p.getNombre().indexOf(jugador.getNombre())!=-1) {
				encontrado=true; // Encontrado el player
				this.l.set(i, jugador); // Se sobreescribe el jugador de la lista
			}
		}
		if (encontrado) {
			// posicionPlayer(jugador);
			updateScore(jugador);
			promocionaComecocos(); // Actualiza toda la lista con el unico fantasma
			return;
		}
		else {
			throw new Exception(jugador.getNombre()+" no encontrado.");
		}
	}
	public void updateTablero(Tablero a) throws Exception {
		// Actualiza sin sustituir el objeto
		tB.updateState(a.state);
		tB.updatePellets(a.pellets);
	}
	/* Devuelve el player segun el su nombre*/
	public Player cualPlayer(String nombre) throws Exception {
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
	/* Promociona al que tenga mayor de todo el servidor puntuacion a Fantasma */
	public void promocionaComecocos() throws Exception {
		// Haya el maximo
		int max=tB.getHighScore();
		int aux;
		int unico=0; // Uno solo de los comecocos sera fantasma (pseudoaleatorio)
		for (int i=0; i<l.size(); i++) {
			Player p=(Player)l.get(i);
			aux=p.getScore();
			if (aux>=max) {
				unico=i;
			}
		}
		// El fantasma es el que más puntuacion tiene (hasta 1000 no habra ninguno)
		// Aumentara en series de 1000
		for (int i=0; i<l.size(); i++) {
			Player p=(Player)l.get(i);
			if (i==unico&&max>950&&max%1000==0) {
				// Encontrado el player con mayor puntuacion
				p.setComecoco(false);
			}
			else {
				p.setComecoco(true);
			}
		}
	}
	/* Actualiza la posicion del jugador remoto en la lista*/
	public List<Player> listaAmigos() throws Exception {
		return l;
	}
	/* Imprime los amigos conectados */
	public void imprimeListaAmigos() throws Exception {
		System.out.print("\r");
		for (int i=0; i<l.size(); i++) {
			Player p=(Player)l.get(i);
			System.out.print(p.getNombre()+" "+p.getScore()+"\t ");
		}
	}
	/* Actualiza la posicion del jugador remoto en la lista*/
	public Tablero getTablero() throws Exception {
		return tB;
	}
	/* Actualiza la mayor puntuacion */
	public void updateScore(Player aspirante) throws Exception {
		int score=aspirante.getScore();
		if(tB.getHighScore()<score) {
			// System.out.println("Nueva Mayor puntuacion de "+aspirante.getNombre()+" "+score);
			tB.setHighScore(score);
			imprimeListaAmigos();
			PrintWriter out;
			try
			{
				out=new PrintWriter("highScores.txt");
				out.println(score);
				out.close();
			}
			catch(Exception e) {
			}
		}
		//highScore=score;
		//TODO //clearHighScores=true;
	}
}
