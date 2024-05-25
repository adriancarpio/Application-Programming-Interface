FROM maven:3.9.6 AS build
RUN mkdir /construir
WORKDIR /construir
COPY . /construir
RUN mvn clean package install

FROM openjdk:17
RUN mkdir /app
WORKDIR /app
COPY --from=build /construir/target/com.cuentas.movimiento-1.0.0.jar /app/com.cuentas.movimiento-1.0.0.jar
ENV DEFAULT_OPTIONS="-Duser.timezone=America/Guayaquil -Djava.net.preferIPv4Stack=true -Djava.security.egd=file:/dev/./urandom"
ENV JAVA_OPTS="-Xms512m -Xmx896m"
ENTRYPOINT java ${DEFAULT_OPTIONS} ${JAVA_OPTS} -jar /app/com.cuentas.movimiento-1.0.0.jar