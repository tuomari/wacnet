FROM clojure:openjdk-8-lein-slim-buster AS lein_toolchain

WORKDIR /opt/wacnet

COPY project.clj project.clj
RUN lein deps

COPY env env
COPY resources resources
COPY src src
COPY test test
COPY externs.js externs.js

RUN lein uberjar


FROM openjdk:8-jre-slim

WORKDIR /opt

COPY --from=lein_toolchain /opt/wacnet/target/wacnet-*-standalone.jar  /opt/wacnet-standalone.jar

EXPOSE 47800/tcp

CMD [ "java", "-jar", "/opt/wacnet-standalone.jar" ]
