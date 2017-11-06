FROM denvazh/gatling:2.3.0

COPY target/scala-2.12/gatling-kafka-assembly-0.1.2-SNAPSHOT.jar /opt/gatling/lib
COPY src/test/scala/com/github/spothero/gatling/kafka/test /opt/gatling/user-files/simulations

ENV KAFKA_HOST localhost
ENV KAFKA_PORT 9090
ENV KAFKA_TOPIC loadtest
ENV USERS 10
ENV TEST_DURATION_SECS 90
ENV MESSAGE_SIZE_BYTES 1000000

ENTRYPOINT ["gatling.sh"]
