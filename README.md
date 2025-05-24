# ByteChat-G8

ByteChat-G8 es una aplicación de chat en tiempo real construida con Java, MongoDB Atlas y HTML/CSS/JavaScript. Permite a cualquier usuario ingresar con un apodo, enviar mensajes de texto o imágenes, ver mensajes anteriores, y visualizar la lista de usuarios actualmente conectados.

## 🌎 Tecnologías utilizadas

* Java 17
* MongoDB Atlas (con TTL Index)
* Maven
* HTTP Server embebido (com.sun.net.httpserver)
* HTML5, CSS3 (modo oscuro), JavaScript (DOM + fetch)
* Bootstrap 5 para diseño visual moderno
* Render.com para despliegue del backend
* GitHub para alojamiento del código fuente

---

## 📅 Características principales

* Enviar mensajes con texto e imágenes (via URL)
* Registro automático de hora de envío (servidor)
* TTL Index: los mensajes se eliminan automáticamente luego de 24 horas
* Ingreso al chat por nickname (sin login)
* Lista de usuarios conectados en tiempo real
* Usuarios se eliminan automáticamente tras 60s de inactividad

---

## 📂 Estructura del proyecto

```
mensajeria-nosql/
├── pom.xml
├── src/
│   └── main/java/com/bytechat/
│       ├── Main.java
│       ├── Mensaje.java
│       ├── MensajeHandler.java
│       ├── UsuarioHandler.java
│       └── MongoHelper.java
├── docs/
│   ├── index.html
│   ├── style.css
│   └── script.js
```

---

## 🚀 Despliegue en Render

### 1. Subí tu proyecto a GitHub

Asegurate de incluir todos los archivos fuente, `pom.xml`, `/docs` y `/src/main/java`.

### 2. Crear servicio en Render

1. Iniciá sesión en [Render.com](https://render.com)
2. Seleccioná "New Web Service"
3. Conectá tu repositorio de GitHub
4. Configurá los siguientes datos:

| Campo         | Valor                                       |
| ------------- | ------------------------------------------- |
| Environment   | Java                                        |
| Build Command | `mvn clean package`                         |
| Start Command | `java -cp target/classes com.bytechat.Main` |
| Port          | `8080`                                      |
| Region        | Lo más cercano (LatAm o US)                 |
| Name          | `bytechat-g8` o el que prefieras            |

### 3. Crear índice TTL en MongoDB Atlas

#### Para mensajes (24h):

```js
db.mensajes.createIndex({ fechaCreacion: 1 }, { expireAfterSeconds: 86400 })
```

#### Para usuarios conectados (60s):

```js
db.usuarios.createIndex({ fechaConexion: 1 }, { expireAfterSeconds: 60 })
```

---

## 📱 Interacción del cliente

* Al cargar la página, aparece un modal que solicita el apodo del usuario
* Cada 30 segundos, se renueva la conexión del usuario al backend
* Cada 5 segundos se actualiza la lista de conectados
* Cada 2 segundos se refresca la lista de mensajes
* Se pueden enviar mensajes con texto, y opcionalmente con una URL de imagen

---

## ✅ Prueba local

### Ejecutar desde IntelliJ o terminal

```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.bytechat.Main"
```

Accedé desde tu navegador a:

```
http://localhost:8080/
```

---

## 👀 Resultado esperado

* Interfaz moderna con Bootstrap y modo oscuro
* Nicknames visibles en tiempo real
* Imágenes mostradas directamente debajo del mensaje si se incluye URL
* Expiración automática de mensajes y usuarios con TTL de MongoDB

---

## 🌐 Crédito

Este proyecto fue desarrollado como parte de un trabajo práctico grupal para la materia **Bases de Datos II** - UTN.

Desarrollado por: **Carlos Orozco**
