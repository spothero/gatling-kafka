package com.github.mnogu.gatling.kafka.test

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

class ByteArraySimulation extends Simulation {
  val kafkaConf = kafka
    .topic("test")
    .properties(
      Map(
        ProducerConfig.ACKS_CONFIG -> "1",
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG ->
          "org.apache.kafka.common.serialization.ByteArraySerializer",
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG ->
          "org.apache.kafka.common.serialization.ByteArraySerializer"))

  val scn = scenario("Kafka Test")
    .exec(kafka("request").send("foo".getBytes: Array[Byte]))

  setUp(
    scn
      .inject(constantUsersPerSec(10) during(90 seconds)))
    .protocols(kafkaConf)
}
