# kafka-producer-consumer
Simple kafka producer consumer example

-Produces data in Kafka topic with REST API.

-Consumer consumes data from topic & indexes data in ES index of same name as topic.

-Producer API

POST: http://localhost:8080/producer/test_new #test_new is topic name

BODY: {"search": "test"}

#START APLICATION

Run APP.java as java application


