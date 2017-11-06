VERSION=0.0.3

all: build push

build:
	docker build . -t spothero/gatling-kafka:${VERSION}

push:
	docker push spothero/gatling-kafka:${VERSION}

run:
	docker run spothero/gatling-kafka:${VERSION} -s com.github.spothero.gatling.kafka.test.KafkaSimulation
