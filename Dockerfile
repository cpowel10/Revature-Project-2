FROM openjdk:8
EXPOSE 8088
ADD target/project-one.jar P1.jar
ENTRYPOINT ["java","-jar","/P1.jar"]