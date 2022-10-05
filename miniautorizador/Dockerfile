# select image maven and jdk
FROM maven:3.8.6-amazoncorretto-17

# copy the source tree and the pom.xml to our new container
COPY ./ ./

# package our application code
RUN mvn clean package

# set the startup command to execute the jar
CMD ["java", "-jar", "target/prova-vr-2.1.jar"]