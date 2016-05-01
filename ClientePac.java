import java.io.PrintWriter;
import java.rmi.*;
class ClientePac {
	static public void main (String args[]) {
		// Si el cliente es un jar no tendrá cliente.permisos
		// Hack para crear uno si no existe
		PrintWriter out;
		try
		{
			out=new PrintWriter("cliente.permisos");
			out.println("grant { permission java.security.AllPermission; };");
			out.close();
		}
		catch(Exception e) {
			System.out.println("Error escribiendo archivo");
		}
		// Elimina la necesidad de permisos en linea de comando
		System.setProperty("java.security.policy", "cliente.permisos");
		// Elimina la capacidad de elegir puerto
		String puerto="55555";
		// Fantasmas de IA
		boolean fantasmas=false;
		if (args.length<2) {
			System.err.println("Uso: ClientePac <host> <Nombre> [Fantasmas]");
			return;
		}
		if (args.length==3) {fantasmas=true;}
		if (System.getSecurityManager()==null)
			System.setSecurityManager(new SecurityManager());
		try {
			ServicioPac srv=(ServicioPac) Naming.lookup("//"+args[0]+":"+puerto+"/Pacman");
			// Crea un tablero local con el nombre del lugador y el servidor
			Board tablero=new Board(srv, args[1], fantasmas);
			// Agrega un player a la lista del servidor con el nombre
			srv.crearPlayer(tablero.getNombre());
			// Crea un juego de pacman local con el tablero
			new Pacman(tablero);
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
