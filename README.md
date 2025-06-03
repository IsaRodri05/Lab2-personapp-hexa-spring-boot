# personapp-hexa-spring-boot
Plantilla Laboratorio Arquitectura Limpia

Instalar MariaDB en puerto 3307
Instalar MongoDB en puerto 27017

Ejecutar los scripts en las dbs

el adaptador rest corre en el puerto 3000
el swagger en http://localhost:3000/swagger-ui.html

Son dos adaptadores de entrada, 2 SpringApplication diferentes

Deben configurar el lombok en sus IDEs

Pueden hacer Fork a este repo, no editar este repositorio

## Ejecutar
### Rest
Para poder correr el codigo es necesario primero clonar y compilar el paquete java
```
$ git clone https://github.com/IsaRodri05/Lab2-personapp-hexa-spring-boot
$ cd Lab2-personapp-hexa-spring-boot
$ mvn clean package
$ mvn clean install
```
Luego debemos compilar el compose y ejecutarlo
```
$ docker compose build
$ docker compose up
```

### CLI
Para correr el cli asegurate que los contenedores docker estén corriendo
```
$ cd Lab2-personapp-hexa-spring-boot/cli-input-adapter/target
$ java -jar cli-input-adapter-0.0.1-SNAPSHOT.jar
```
