FROM eclipse-temurin:17.0.5_8-jre-focal as builder
WORKDIR extracted
ADD ./target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17.0.5_8-jre-focal
WORKDIR application
COPY --from=builder extracted/dependencies/ ./
COPY --from=builder extracted/spring-boot-loader/ ./
COPY --from=builder extracted/snapshot-dependencies/ ./
COPY --from=builder extracted/application/ ./

EXPOSE 9192

#use it before springboot 3.2
#ENTRYPOINT ["java" , "org.springframework.boot.loader.JarLauncher"]
#use it for after springboot 3.2

ENTRYPOINT ["java" , "org.springframework.boot.loader.launch.JarLauncher"]