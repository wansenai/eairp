FROM openjdk:21
MAINTAINER WanSen AI<team@wansenai.com>
COPY api/target/wansenerp-v2.jar application/application.jar
WORKDIR /application
EXPOSE 8088
CMD ["java", "-jar", "application.jar"]
