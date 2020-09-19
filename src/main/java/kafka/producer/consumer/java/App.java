package kafka.producer.consumer.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import kafka.producer.consumer.configuration.NewConsumerConfiguration;
import kafka.producer.consumer.configuration.ProducerConfiguration;

@SpringBootApplication
@EnableConfigurationProperties({NewConsumerConfiguration.class, ProducerConfiguration.class})
public class App 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
    }
}