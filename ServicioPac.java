import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
interface ServicioPac extends Remote {
	public void crearPlayer(String nombre) throws RemoteException;
	public String posicionPlayer(Player jugador) throws RemoteException;
	public void updatePlayer(Player jugador) throws Exception;
	public Player cualPlayer(String nombre) throws Exception;
	public void promocionaComecocos() throws Exception;
	public List<Player> listaAmigos() throws Exception;
}

