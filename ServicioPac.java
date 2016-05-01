import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
interface ServicioPac extends Remote {
	void crearPlayer(String nombre) throws RemoteException;
	String posicionPlayer(Player jugador) throws RemoteException;
	void updatePlayer(Player jugador) throws Exception;
	public void promocionaComecocos() throws Exception;
	List<Player> listaAmigos() throws Exception;
	BoardS creajuego(String nombre) throws RemoteException;
}
