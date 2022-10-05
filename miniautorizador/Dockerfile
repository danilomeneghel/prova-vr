# select image maven and openjdk
FROM sgurumukhi/maven-openjdk-17-slim:3.6.3

# copy the source tree and the pom.xml to our new container
COPY ./ ./

# package our application code
RUN mvn clean package

# set the startup command to execute the jar
CMD ["java", "-jar", "target/prova-vr-2.1.jar"]