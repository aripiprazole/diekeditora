FROM openjdk:11 AS build

COPY . /app
WORKDIR /app

RUN /app/gradlew build --no-daemon --no-watch-fs -x test

FROM registry.access.redhat.com/ubi8/ubi-minimal:8.3

ARG JAVA_PACKAGE=java-11-openjdk-headless
ARG RUN_JAVA_VERSION=1.3.8
ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en'

# Install java and the run-java script
# Also set up permissions for user `1001`
RUN microdnf install curl ca-certificates ${JAVA_PACKAGE} \
    && microdnf update \
    && microdnf clean all \
    && mkdir /app \
    && chown 1001 /app \
    && chmod "g+rwX" /app \
    && chown 1001:root /app \
    && curl https://repo1.maven.org/maven2/io/fabric8/run-java-sh/${RUN_JAVA_VERSION}/run-java-sh-${RUN_JAVA_VERSION}-sh.sh -o /app/run-java.sh \
    && chown 1001 /app/run-java.sh \
    && chmod 540 /app/run-java.sh \
    && echo "securerandom.source=file:/dev/urandom" >> /etc/alternatives/jre/lib/security/java.security

# Configure the JAVA_OPTIONS, you can add -XshowSettings:vm to also display the heap size.
ENV JAVA_OPTIONS=""

COPY --from=build --chown=1001 /app/build/libs/*.jar /app

EXPOSE 8080
USER 1001

ENTRYPOINT [ "/app/run-java.sh" ]
