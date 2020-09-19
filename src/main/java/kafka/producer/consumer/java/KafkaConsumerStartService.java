package kafka.producer.consumer.java;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import kafka.producer.consumer.configuration.NewConsumerConfiguration;

@Service
public class KafkaConsumerStartService {
	
	private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumerStartService.class);

	@Autowired
	private NewConsumerConfiguration newConsumerConfiguration;
	
	@Value("${kafka.brokers}")
	private String kafkaBrokers;
	
	@Autowired
	private ApplicationContext ctx;
	
	public KafkaConsumerStartService() {}
	
	@PostConstruct
	private void startConsumer() {
		int num = newConsumerConfiguration.getNumThreads();
		
		ExecutorService ex = Executors.newFixedThreadPool(num);
		
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, newConsumerConfiguration.getAutoCommitInterval());
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, newConsumerConfiguration.getKeyDeserializerClass());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, newConsumerConfiguration.getValueDeserializerClass());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, newConsumerConfiguration.getGroupId());
		
		while(num --> 0) {
			ex.submit(ctx.getBean(NewConsumer.class, num, newConsumerConfiguration.getTopic(), props));
		}
		ex.shutdown();
		
		LOG.info("new consumer started");
	}
	
}
