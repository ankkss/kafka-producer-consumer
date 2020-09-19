package kafka.producer.consumer.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "new.consumer")
public class NewConsumerConfiguration {
	
	private String keyDeserializerClass;
	private String valueDeserializerClass;
	private String topic;
	private String groupId;
	private int autoCommitInterval;
	private int numThreads;
	
	public String getKeyDeserializerClass() {
		return keyDeserializerClass;
	}
	public void setKeyDeserializerClass(String keyDeserializerClass) {
		this.keyDeserializerClass = keyDeserializerClass;
	}
	public String getValueDeserializerClass() {
		return valueDeserializerClass;
	}
	public void setValueDeserializerClass(String valueDeserializerClass) {
		this.valueDeserializerClass = valueDeserializerClass;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public int getAutoCommitInterval() {
		return autoCommitInterval;
	}
	public void setAutoCommitInterval(int autoCommitInterval) {
		this.autoCommitInterval = autoCommitInterval;
	}
	public int getNumThreads() {
		return numThreads;
	}
	public void setNumThreads(int numThreads) {
		this.numThreads = numThreads;
	}
}
