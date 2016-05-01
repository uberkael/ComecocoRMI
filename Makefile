###########
# Default #
###########
all: clean compilacion
################
# Compila todo #
################
compilacion:
	javac *.java
############################
# Registo del servidor RMI #
############################
registro:
	# export CLASSPATH=$PWD # Solo es necesario una vez
	rmiregistry 55555
#######################
# Ejecuta el servidor #
#######################
server:
	# Eliminada la necesidad de comando de permisos
	# java -Djava.security.policy=servidor.permisos ServidorPac 55555
	# java ServidorPac 55555 localhost # Eliminado eleccion del puerto
	java ServidorPac
# Ejecuta el servidor en red local #
####################################
slocal:
	# Eliminada la necesidad de comando de permisos
	# java -Djava.security.policy=servidor.permisos ServidorPac 55555
	# java ServidorPac 55555 192.168.1.2 # Eliminado eleccion del puerto
	java ServidorPac 192.168.1.2
#####################
# Ejecuta Cliente 1 #
#####################
cliente:
	# Eliminada la necesidad de comando de permisos
	# java -Djava.security.policy=cliente.permisos ClientePac localhost 55555 Adri
	# java ClientePac localhost 55555 Adri # Eliminado eleccion del puerto
	java ClientePac localhost Adri
#####################
# Ejecuta Cliente  #
#####################
cliente2:
	# Eliminada la necesidad de comando de permisos
	# java -Djava.security.policy=cliente.permisos ClientePac localhost 55555 KaeL
	# java ClientePac localhost 55555 KaeL # Eliminado eleccion del puerto
	java ClientePac localhost KaeL
####################################
# Crea un jar para la demostracion #
####################################
jar:
	# No funciona usar Intellij
	# jar cvfm ComecocosC.jar highScores.txt ClientePac.class BoardS.class Board.class Player.class img/ sounds/
############
# Limpieza #
############
clean:
	rm -fr *.class 2>/dev/null



