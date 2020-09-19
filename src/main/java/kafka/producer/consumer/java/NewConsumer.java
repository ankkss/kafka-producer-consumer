package kafka.producer.consumer.java;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class NewConsumer implements Runnable {

	private int threadIndex;

	private static final Logger LOG = LoggerFactory.getLogger(NewConsumer.class);

	private KafkaConsumer<Long, String> consumer;

	@Autowired
	private ElasticHighClient elasticHighClient;

	public NewConsumer() {
	}

	public NewConsumer(int threadIndex, String topic, Properties props) {
		this.threadIndex = threadIndex;

		initialize(topic, props);
	}

	private void initialize(String topic, Properties props) {
		consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList(topic.split(",")));
	}

	@Override
	public void run() {
		while (true) {
			try {
				ConsumerRecords<Long, String> records = consumer.poll(Duration.ofMillis(1000));

				BulkRequest req = null;
				if (!records.isEmpty()) {
					req = new BulkRequest();
				}
				for (ConsumerRecord<Long, String> record : records) {
					IndexRequest request = new IndexRequest(record.topic());
					request.source(new JSONObject(record.value()).toMap());
					req.add(request);
					LOG.info("consumer message-{}", record.value());
				}

				if (req != null) {
					int failed = elasticHighClient.executeBulk(req);
					if (failed > 0) {
						LOG.warn("failed no of req {} in ES", failed);
					} else {
						LOG.info("executed bulk request sucessfully");
					}
				}
			} catch (Exception e) {
				LOG.error("while running consumer", e);
			}
		}
	}
}
