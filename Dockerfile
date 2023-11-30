FROM openjdk:17-oracle
EXPOSE 5500
ADD target/card_transfer_service-0.0.1-SNAPSHOT.jar card_transfer_service.jar
ENTRYPOINT ["java","-jar","/card_transfer_service.jar"]