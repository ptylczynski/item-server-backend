FROM openjdk:11
RUN java -version
ADD target /
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "item-server.jar"]

