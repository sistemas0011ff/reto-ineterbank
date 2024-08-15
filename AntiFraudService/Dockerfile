# Establece la imagen base para Java
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

ENV KAFKA_TOPIC_TRANSACTION_RESULT=topic-trx-validation-result
ENV SPRING_KAFKA_TOPIC_TRANSACTION=trx-topic

# Copia los archivos de construcción de Gradle
COPY build.gradle settings.gradle /app/

# Copia el código fuente al contenedor
COPY src /app/src

# Copia los scripts de Gradle Wrapper al contenedor
COPY gradlew /app/
COPY gradle /app/gradle

# Compila el proyecto, omitiendo las pruebas
RUN ./gradlew build -x test

# Expone el puerto en el que la aplicación correrá
EXPOSE 8881

# Ejecuta la aplicación
CMD ["java", "-jar", "build/libs/AntiFraudService-0.0.1-SNAPSHOT.jar"]
