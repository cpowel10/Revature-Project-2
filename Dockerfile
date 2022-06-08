FROM openjdk:8
EXPOSE 8088
ADD target/project-two.jar P2.jar
ENTRYPOINT ["java","-jar","/P2.jar"]