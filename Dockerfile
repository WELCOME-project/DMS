FROM tomcat:9.0-jdk11

COPY ./dms-service/target/dms-service-0.1.0-SNAPSHOT.war /usr/local/tomcat/webapps/dms-service.war

EXPOSE 8080
