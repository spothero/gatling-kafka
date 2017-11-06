package com.github.spothero.gatling.kafka.test

import io.gatling.core.Predef._
import org.apache.kafka.clients.producer.ProducerConfig
import scala.concurrent.duration._

import com.github.mnogu.gatling.kafka.Predef._

// Extend Scala Random with a random byte generator for this test
implicit class ExtendedRandom(ran: scala.util.Random) {
  def nextByteArray(size: Int) = {
    val arr = new Array[Byte](size)
    ran.nextBytes(arr)
    arr
  }
}

class KafkaSimulation extends Simulation {
  // Fetch config
  val host = sys.env("KAFKA_HOST")
  val port = sys.env("KAFKA_PORT")
  val topicName = sys.env("KAFKA_TOPIC")
  val users = sys.env("USERS")
  val duration = sys.env("TEST_DURATION_SECS")
  val messageSizeBytes = sys.env("MESSAGE_SIZE_BYTES")

  val kafkaConf = kafka
    // Kafka topic name
    .topic(topicName)
    // Kafka producer configs
    .properties(
      Map(
        ProducerConfig.ACKS_CONFIG -> "1",
        // list of Kafka broker hostname and port pairs
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG -> s"$host:$port",

        // in most cases, StringSerializer or ByteArraySerializer
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG ->
          "org.apache.kafka.common.serialization.ByteArraySerializer",
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG ->
          "org.apache.kafka.common.serialization.ByteArraySerializer"))

  val scn = scenario("Kafka Test")
    .exec(
      kafka("request")
        // message to send
        .send(scala.util.Random.nextByteArray(messageSizeBytes): Array[Byte]))

  setUp(
    scn
      .inject(constantUsersPerSec(users) during(duration seconds)))
    .protocols(kafkaConf)
}
