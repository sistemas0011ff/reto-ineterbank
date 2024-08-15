# Establece la imagen base para Java
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Define la variable de entorno para el tópico de Kafka
ENV SPRING_KAFKA_TOPIC_TRANSACTION=trx-topic

# Copia los archivos de construcción de Gradle
COPY build.gradle settings.gradle /app/

# Copia el código fuente al contenedor
COPY src /app/src

# Copia los scripts de Gradle Wrapper al contenedor
COPY gradlew /app/
COPY gradle /app/gradle

ENV SPRING_PROFILES_ACTIVE=test

# Compila el proyecto, omitiendo las pruebas
RUN ./gradlew build -x test
#RUN ./gradlew build

# Expone el puerto en el que la aplicación correrá
EXPOSE 8880

# Ejecuta la aplicación
CMD ["java", "-jar", "build/libs/TransactionService-0.0.1-SNAPSHOT.jar"]
