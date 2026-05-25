>[!caution]
> ## ¡Porfavor, lea antes de realizar cualquier acción!

<p align="center"><img  width="350" height="150" alt="image" src="https://github.com/user-attachments/assets/e60825bf-683d-45c3-98bc-0d1cea262896"/></p>

# Welcome!!

Si quiere probar la aplicacion en local debe de tener en cuenta un par de consideraciones:
1. ### [Base de datos](#1-base-de-datos).
2. ### [Aplicacion Java](#2-aplicacion-java).
---
## 1. Base de datos
1. Para tener la base de datos preparada, tiene que crear una base de datos en **pgAdmin** llamada "PadelProject". No hace falta crear las tablas ya que el servidor las crea automaticamente.
2. Asegurese de que la URL de la base de datos del JDBC este bien y apunte al servicio de pgAdmin con su puerto.
3. Asegurese de que las credenciales de la base de datos esten bien en el archivo **`application-postgres.properties`** de esta ruta: **`src\main\resources\application-postgres.properties`**. Linea 10 a Linea 18.
4. Si el servidor no ha creado las tablas automaticamente, tiene a su disposicion un archivo SQL para generar las tablas en esta ruta **`src\main\resources\SQL\SQLPadelProject.sql`** y quite los guiones de los comentarios para dejar las clausulas solo.

## 2. Aplicacion Java
### Este proyecto a ser formado por 2 aplicaciones diferentes (Un servidor y una aplicacion grafica) que se han repartido en dos ramas.
Para probar la aplicacion se descargan los dos proyectos de las dos ramas indicadas abajo y se ejecuta el servidor primero ejecutando esta clase `PracticingspringApplication.java` que es la clase principal del servidor.
Para la aplicacion grafica se debe de ejecutar la aplicacion `Login.java`.

Ramas:
- Servidor: [Aqui](https://github.com/Fredrick0K/PadelProject/tree/server)
- App grafica: [Aqui](https://github.com/Fredrick0K/PadelProject/tree/ui-app)

---
> [!warning] 
> ### Asegurese de tener los proyectos separados.


