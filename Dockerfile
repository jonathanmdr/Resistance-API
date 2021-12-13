FROM maven:3.8.1-openjdk-11 AS builder

LABEL maintainer="Jonathan Henrique Medeiros"

COPY . /build

RUN cd /build && \
    mvn dependency:go-offline && \
    mvn clean package

RUN mkdir /app && \
    mv /build/target/resistance-1.0.0.jar /app/resistance.jar

FROM azul/zulu-openjdk:11.0.10

RUN apt-get update && \
    rm -rf /var/lib/apt/lists/*

RUN mkdir /app

COPY --from=builder /app/* /app

WORKDIR /app

ENTRYPOINT ["java", "-jar", "resistance.jar"]
