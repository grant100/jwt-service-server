#!/bin/bash
./gradlew clean
./gradlew build
reset
docker build --tag=service-node .
docker tag auth-server gsowards/service-node:latest
docker push gsowards/service-node:latest
#docker run -p 443:8080 gsowards/auth-server:latest
