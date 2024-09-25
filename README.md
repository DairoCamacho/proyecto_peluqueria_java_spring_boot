
# Sistema de Información para el Agendamiento de Citas (SIAC)

El objetivo de la aplicación es permitir a los clientes de la Peluquería Alejandra realizar de citas para ser atendidos, seleccionando el servicio deseado, así como la fecha y hora de la cita.


## Repositorio de la API

https://github.com/DairoCamacho/api-springboot-hairsalon.git



## Despliegue

Para desplegar este proyecto ejecute el comando:

```bash
   mvn clean -DskipTests=true install -P prod
```
```bash
   docker compose up
```


## Variables de entorno

El proyecto usa las siguientes variables de entorno:

`DATABASE_URL`

`DATABASE_USERNAME`

`DATABASE_PASSWORD`

`JWT_SECRET`


## Requisitos previos

Para este proyecto requiere el uso de los siguientes componentes:
  
  [jdk version 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

  [Maven](https://maven.apache.org/download.cgi)
  
  [Postgresql (Ejecutandose previamente)](https://www.postgresql.org/download)

Sin embargo, también puede ser desplegado haciendo uso [Docker Desktop](https://www.docker.com/products/docker-desktop/)

