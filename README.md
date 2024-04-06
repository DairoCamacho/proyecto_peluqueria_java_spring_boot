
# Sistema de Información para el Agendamiento de Citas (SIAC)

El objetivo de la aplicación es permitir a los clientes de la Peluquería Alejandra realizar de citas para ser atendidos, seleccionando el servicio deseado, así como la fecha y hora de la cita.


## Repositorio de la API

https://github.com/DairoCamacho/api-springboot-hairsalon.git



## Despliegue

Para desplegar este proyecto ejecute el comando:

```bash
  ./mvnw clean package -DskipTest && java -jar api-0.0.1-SNAPSHOT.jar
```


## Variables de entorno

Para ejecutar este proyecto, deberá agregar las siguientes variables de entorno a su archivo .env

`DATABASE_UR`

`DATABASE_USERNAME`

`DATABASE_PASSWORD`

`JWT_SECRET`


## Requisitos previos

Para este proyecto requiere de los siguientes componentes:

```bash
  [jdk version 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
  [Apache Maven](https://maven.apache.org/download.cgi)
  [MySQL (Ejecutandose previamente)](https://www.apachefriends.org/download.html)
```
    