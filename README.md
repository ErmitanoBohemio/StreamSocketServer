# StreamSocketServer

Establece un servidor simple por medio de Stream Sockets

Paso 1: Crear ServerSocket, se especifica el numero de puerto TCP (entre 0 y 65535) y el numero maximo de clientes que pueden esperar por la conexion con el servidor.

PAso 2: Esperar por conexion, a partir un un objeto Socket; el servidor espera indefinidamente (o blocks) por un intento de un cliente de conectarse por medio del metodo accept.

Paso 3: El Socket permite al servidor comunicarse con el cliente enviando bytes por medio de OutputStream y recibiendo bytes por medio de InputStream; por medio de los metodos getOutputStream y getInputStream, a menudo es util enviar o recibir valores de tipo primitivo (int, double, etc) o objetos Serializables (String y otros tipos serializables) más que enviar bytes.

Paso 4: Realización del procesamiento, fase rn la cual el servidor y el cliente se comunican vía los objetos OutputStream e InputStream.

Paso 5: Cerrar Conexión, cuando la tranmisión se completa el servidor cierra la conexión invocando al metodo close tanto en los streams como en el socket.