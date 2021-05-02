Sistema de Achivos
==================

Esta es el código base para el ejercicio de File System (Sistema de Archivos). Está diseñado para:

* Java 8. :warning: Si bien el proyecto no lo limita explícitamente, el comando `mvn verify` no funcionará con versiones mas modernas de Java.
* JUnit 5. :warning: La versión 5 de JUnit es la más nueva del framework y presenta algunas diferencias respecto a la versión "clásica" (JUnit 4). Para mayores detalles, ver:
  *  [Apunte de herramientas](https://docs.google.com/document/d/1VYBey56M0UU6C0689hAClAvF9ILE6E7nKIuOqrRJnWQ/edit#heading=h.dnwhvummp994)
  *  [Entrada de Blog (en inglés)](https://www.baeldung.com/junit-5-migration)
  *  [Entrada de Blog (en español)](https://www.paradigmadigital.com/dev/nos-espera-junit-5/)
* Maven 3.3 o superior

# El enunciado

Un equipo de desarrollo acaba de implementar un novedoso y revolucionario sistema de archivos: un conjunto de objetos
que nos permiten abrir y cerrar archivos (binarios), y leerlos o escribirlos secuencialmente. Sin embargo, no se esmeraron mucho en que la interfaz entrante al sistema sea fácil de usar:

```java
public interface LowLevelFileSystem {
  int openFile(String path);

  void closeFile(int fd);
  int syncReadFile(int fd, byte[] bufferBytes, int bufferStart, int bufferEnd);
  void syncWriteFile(int fd, byte[] bufferBytes, int bufferStart, int bufferEnd);
  void asyncReadFile(int fd, byte[] buffer, int bufferStart, int bufferEnd,
      Consumer<Integer> callback);
}
```
(ver archivo `fs/LowLevelFileSystem.java`)

Como ven esta interfaz entrante (representada por una __interface__ Java) cumple lo solicitado, pero presenta un bajo nivel de abstraccion y no es por tanto fácil de usar.


Entonces nos solicitaron que diseñemos una mejor interfaz entrante a este sistema, que pueda usar un programador (una API, application programming interface) que sea de mayor nivel de abstracción y más simple de usar. Esta estará conformada por uno o más componentes (interfaces, objetos, clases, etc) que tendremos que diseñar.

Ademas, nos pidieron que implementemos esta intefaz de forma que todas las operaciones que se hagan contra esta API terminen ejecutando las operaciones de la interfaz de bajo nivel del sistema de archivos.

Sin embargo, los requerimientos son un poco abiertos. Nos han señalado las siguientes cosas:
* Este API debe ser "fácil" de usar para un programador: debería ser clara, aprovechar el paradigma de objetos, ocultar detalles que no nos aportan y presentar buenas abstracciones.
* No es necesario que exponga una única __interface__ tipo fachada.
* Debe usar a la interfaz entrante original que nos dieron, pero no modificarla.
* Debe ser robusta, presentando un buen manejo de errores
* Esta API debe permitr como mínimo:
  * abrir un archivo para lecto escritura
  * cerrar un archivo
  * escribir sincrónicamente un bloque de n bytes de archivo
  * leer sincrónicamente un bloque de n bytes de un archivo
  * leer asincrónicamente un bloque de n bytes de un archivo
* Esta API que diseñaremos será básicamente un adaptador, es decir, no agregará funcionalidad al sistema de archivos original, sino que tan solo expondrá una mejor interfaz entrante. Es decir, ahora aquella interfaz entrante original será para nosotres la interfaz saliente del pequeño sistema adaptador que vamos a diseñar.

```
                 +---+    +-----------------+
Componentes ---> |API|--> |File System Posta|
que usan         +---+    +-----------------+
al file system     ^
                   |
                   +--- nosotres diseñaremos esto

```
Entonces, tenemos como tarea "embellecer" al sistema de archivos. Y como segunda tarea, dar un ejemplo de uso del API para los siguiente casos:
  * Tenemos que leer de un archivo 3 campos: el primero de 4 bytes (C0), el segundo de 1 byte (C1), el tercero de 5 bytes (C2). Y escribir en otro archivo C0, un bloque 0x0, 0x10, 0x0, y luego C1 y C2.
  * Tenemos que leer un archivo completo, y escribirlo en otro archivo, en bloques de igual tamaño parametrizable.

Los ejemplos tambien tenemos que plantearlos nosotres, en forma de tests.

Finalmente, como nuestro cliente es bastante quisquilloso quiere ver formas alternativas de solucionar las lecturas sincrónicas y asincrónicas, para compararlas y ver cuales le gustan más.

## Notas

  * no nos interesa lidiar con problemas de concurrencia. Asumimos que ya los resuelve el sistema de archivos.
  * si no saben por donde empezar, hay un test y unas clases que les pueden servir de puntapié para ambas soluciones

## Bonus


  * Nos gustaría que la interfaz nos expusiera al menos una forma de aplicarle a cada bloque una transformación, o de filtrar facilmente bloque
  * Nos gustaría que funcionara para archivos muy grandes, potencialmente infinitos.
  * Nos gustaría que este API eventualmente pudiera tener diferentes implementaciones que hablen contra otros sistemas de archivos con otras interfaces de bajo nivel.


# Ejecutar tests

```
mvn test
```

# Validar el proyecto de forma exahustiva

```
mvn clean verify
```

Este comando hará lo siguiente:

 1. Ejecutará los tests
 2. Validará las convenciones de formato mediante checkstyle
 3. Detectará la presencia de (ciertos) code smells
 4. Validará la cobertura del proyecto

# Entrega del proyecto

Para entregar el proyecto, crear un tag llamado `entrega-final`. Es importante que antes de realizarlo se corra la validación
explicada en el punto anterior. Se recomienda hacerlo de la siguiente forma:

```
mvn clean verify && git tag entrega-final && git push origin HEAD --tags
```

# Configuración del IDE (IntelliJ)

 1. Tabular con dos espacios: ![Screenshot_2021-04-09_18-23-26](https://user-images.githubusercontent.com/677436/114242543-73e1fe00-9961-11eb-9a61-7e34be9fb8de.png)
 2. Instalar y configurar Checkstyle:
    1. Instalar el plugin https://plugins.jetbrains.com/plugin/1065-checkstyle-idea:
    2. Configurarlo activando los Checks de Google y la versión de Checkstyle `== 8.35`: ![Screenshot_2021-04-09_18-16-13](https://user-images.githubusercontent.com/677436/114242548-75132b00-9961-11eb-972e-28e6e1412979.png)
 3. Usar fin de linea unix
    1. En **Settings/Preferences**, ir a a **Editor | Code Style**.
    2. En la lista **Line separator**, seleccionar `Unix and OS X (\n)`.
 ![Screenshot 2021-04-10 03-49-00](https://user-images.githubusercontent.com/11875266/114260872-c6490c00-99ad-11eb-838f-022acc1903f4.png)
