# Define la imagen base
FROM eclipse-temurin:21

# Instalar alguna dependencia que necesite previamente la app
RUN mkdir /opt/app

# Copiar los archivos del proyecto (source dest)
COPY target/api-0.0.1-SNAPSHOT.jar /opt/app/java_app.jar

# RUN chmod +rx /opt/app/java_app.jar

# Exponer el puerto de la app
EXPOSE 8080
# EXPOSE 3306
# EXPOSE 5432

# Iniciar la app
CMD ["java", "-jar", "/opt/app/java_app.jar"]

# tambien se puede inicar la app usando ENTRYPOINT
# ENTRYPOINT ["java", "-jar", "java-app.jar"]