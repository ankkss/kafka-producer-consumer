package kafka.producer.consumer.java;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kafka.producer.consumer.configuration.ProducerConfiguration;

@Service
public class KafkaProducerService {
	
	private KafkaProducer<Long, String> producer;
	
	@Autowired
	private ProducerConfiguration producerConfiguration;
	
	@Value("${kafka.brokers}")
	private String kafkaBrokers;
	
	private final static Logger log = LoggerFactory.getLogger(KafkaProducerService.class);

	public KafkaProducerService() {}
	
	@PostConstruct
	private void intialize() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producerConfiguration.getKeySerializerClass());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, producerConfiguration.getValueSerializerClass());

		producer = new KafkaProducer<>(props);
	}
	
	public void produce(String topic, String message) throws Exception {
		try {
			RecordMetadata rmd = producer.send(new ProducerRecord<>(topic, message)).get(1000, TimeUnit.MILLISECONDS);
			log.info("message produced to kafka topic-{} partition-{} offset-{}", rmd.topic(), rmd.partition(), rmd.offset());
		} catch (Exception e) {
			throw e;
		}
	}
}
