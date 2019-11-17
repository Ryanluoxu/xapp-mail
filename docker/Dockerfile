FROM openjdk:8-jdk
ARG APPLICATION_VERSION
ENV APPLICATION_VERSION ${APPLICATION_VERSION}
ARG TZ=Asia/Singapore
ENV TZ=${TZ}
COPY lib/ /app/lib
COPY application*.yml /app/
COPY logback-spring.xml /app/logback-spring.xml
COPY application-*.jar /app/application.jar
WORKDIR /app
VOLUME /app/logs
EXPOSE 8080
CMD ["java","-jar","-Xmx256m","/app/application.jar"]