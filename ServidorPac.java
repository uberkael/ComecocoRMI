import java.rmi.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import javax.swing.JApplet;
import java.awt.*;
import java.util.List;
import java.lang.*;
import java.io.PrintWriter;
class ServidorPac extends JApplet
{
	static public void main (String args[]) {
		// Si el cliente es un jar no tendrá cliente.permisos
		// Hack para crear uno si no existe
		PrintWriter out;
		try
		{
			out=new PrintWriter("servidor.permisos");
			out.println("grant { permission java.security.AllPermission; };");
			out.close();
		}
		catch(Exception e) {
			System.out.println("Error escribiendo archivo");
		}
		// Elimina la necesidad de permisos en linea de comando
		System.setProperty("java.security.policy", "servidor.permisos");
		// Elimina la capacidad de elegir puerto
		String puerto="55555";
		if (args.length>=2) {
			System.err.println("Uso: ServidorPac [<Ip de Salida>]");
			return;
		}
		if (args.length==1) {
			System.setProperty("java.rmi.server.hostname", args[0]);
		}
		if (System.getSecurityManager()==null) {
			System.setSecurityManager(new RMISecurityManager());
		}
		try {
			ServicioPacImpl srv=new ServicioPacImpl();
			Naming.rebind("rmi://localhost:"+puerto+"/Pacman", srv);
		}
		catch (RemoteException e) {
			System.err.println("Error de comunicacion: "+e.toString());
			System.exit(1);
		}
		catch (Exception e) {
			System.err.println("Excepcion en ServidorPac:");
			e.printStackTrace();
			System.exit(1);
		}
	}
}
/*
rm *.class
javac *.java
export CLASSPATH=$PWD
rmiregistry 54321
java -Djava.security.policy=servidor.permisos ServidorPac 54321
*/
