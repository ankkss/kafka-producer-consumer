package kafka.producer.consumer.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "producer")
public class ProducerConfiguration {

	private String keySerializerClass;
	private String valueSerializerClass;
	
	public String getKeySerializerClass() {
		return keySerializerClass;
	}
	public void setKeySerializerClass(String keySerializerClass) {
		this.keySerializerClass = keySerializerClass;
	}
	public String getValueSerializerClass() {
		return valueSerializerClass;
	}
	public void setValueSerializerClass(String valueSerializerClass) {
		this.valueSerializerClass = valueSerializerClass;
	}
}
