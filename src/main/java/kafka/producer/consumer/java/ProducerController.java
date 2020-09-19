package kafka.producer.consumer.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {
	
	@Autowired
	private KafkaProducerService producerEx;
	
	@PostMapping("/producer/{topic}")
	public ResponseEntity<String> produce(@PathVariable String topic, @RequestBody String message) {
		try {
			producerEx.produce(topic, message);
			return ResponseEntity.ok("message produced in kafka successfully");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
