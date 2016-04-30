import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
class ServicioPacImpl extends UnicastRemoteObject implements ServicioPac {
	// Contiene una lista de players
	List<Player> l;
	// Constructor
	ServicioPacImpl() throws RemoteException {
		l=new LinkedList<Player>();
	}
	/* Añade un jugador a la lista y la devuelve */
	public void crearPlayer(String nombre) throws RemoteException {
		int x=200;
		int y=300;
		// Comprueba si el jugador ya existe (lanza exepcion)
		for (int i=0; i<l.size(); i++) {
			Player p=(Player)l.get(i);
			if (p.getNombre().indexOf(nombre)!=-1) {
				throw new RemoteException("El nombre ya existe.");
			}
		}
		// Si no lo agrega a la lista y lo devuelve al cliente
		Player c=new Player(x, y, nombre);
		l.add(c);
		System.out.println("Player agregado: "+c.getNombre());
	}
	/* Devuelve la posicion del jugador*/
	public String posicionPlayer(Player jugador) throws RemoteException {
		String posi="Posicion de "+jugador.getNombre()+"X:"+jugador.getX()+"\tY: "+jugador.getY()+" Puntuacion: "+jugador.getScore();
		// System.out.println(posi); // Lo imprime en el servidor tambien
		return posi;
	}
	/* Actualiza la posicion del jugador remoto en la lista*/
	public void updatePlayer(Player jugador) throws Exception {
		boolean encontrado=false;
		for (int i=0; i<l.size(); i++) {
			Player p=l.get(i);
			if (p.getNombre().indexOf(jugador.getNombre())!=-1) {
				// Encontrado el player
				encontrado=true;
				// El servidor busca si tiene mayor puntuacion que nadie y lo hace comecoco
				jugador.setComecoco(promocionaComecoco(jugador));
				this.l.set(i, jugador); // Se sobreescribe el jugador de la lista
			}
		}
		if (encontrado) {
			posicionPlayer(jugador);
			return;
		}
		else {
			throw new Exception(jugador.getNombre()+" no encontrado.");
		}
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
	/* Promociona al que tenga mayor de todo el servidor puntuacion a comecocos */
	public boolean promocionaComecoco(Player jugador) throws Exception {
		int max=0;
		for (int i=0; i<l.size(); i++) {
			Player p=(Player)l.get(i);
			if (p.getScore()>=max) {
				// Encontrado el player con mayor puntuacion
				max=p.getScore();
			}
		}
		// El fantasma es el que más puntuacion tiene
		if(jugador.getScore()>=max) { return false; }
		else { return true; }
	}
	/* Actualiza la posicion del jugador remoto en la lista*/
	public List<Player> listaAmigos() throws Exception {
		return l;
	}
	/* Crea una instancia de la partida con un jugador*/
	public BoardS creajuego(String nombre) throws RemoteException {
		BoardS c=new BoardS(nombre);
		return c;
	}
}
