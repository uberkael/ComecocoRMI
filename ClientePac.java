import java.rmi.*;
import static java.lang.Thread.sleep;
class ClientePac {
	static public void main (String args[]) {
		// Elimina la necesidad de permisos en linea de comando
		System.setProperty("java.security.policy", "cliente.permisos");
		// Elimina la capacidad de elegir puerto
		String puerto="55555";
		if (args.length<2) {
			System.err.println("Uso: ClientePac <host> <Nombre>");
			return;
		}
		if (System.getSecurityManager()==null)
			System.setSecurityManager(new SecurityManager());
		try {
			ServicioPac srv=(ServicioPac) Naming.lookup("//"+args[0]+":"+puerto+"/Pacman");
			// Crea un tablero local con el nombre del lugador
			Board tablero=new Board(args[1]);
			// Agrega un player a la lista del servidor con el nombre
			srv.crearPlayer(tablero.getNombre());
			// Crea un juego de pacman local con el tablero y el servidor
			new Pacman(srv, tablero);
		}
		catch (RemoteException e) {
			System.err.println("Error de comunicacion: "+e.toString());
		}
		catch (Exception e) {
			System.err.println("Excepcion en ClientePac:");
			e.printStackTrace();
		}
	}
}
/*
javac *.java
java -Djava.security.policy=cliente.permisos ClientePac localhost 54321 Adri
*/
