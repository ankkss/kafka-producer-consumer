package kafka.producer.consumer.java;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ElasticHighClient {

	private RestHighLevelClient client = null;

	private static final Logger LOG = LoggerFactory.getLogger(ElasticHighClient.class);

	@Value("#{'${elastic.host}'.split(',')}")
	private List<String> hosts;

	@Value("${elastic.port}")
	private int port;

	private String user;

	private String password;

	public ElasticHighClient() {
	}

	@PostConstruct
	private void initialize() {
		List<HttpHost> httpsHosts = hosts.stream().map(s -> new HttpHost(s, port)).collect(Collectors.toList());

		RestClientBuilder builder = RestClient.builder(httpsHosts.toArray(new HttpHost[] {}));

		// If x-pack security enabled
		if (user != null && password != null) {
			final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("user", "password"));

			builder.setHttpClientConfigCallback(
					httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
		}
		client = new RestHighLevelClient(builder);
	}

	public int executeBulk(BulkRequest bulkRequest) throws IOException {
		try {
			if (bulkRequest.numberOfActions() == 0)
				return 0;

			BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);

			int failed = 0;
			if (response.hasFailures()) {
				for (BulkItemResponse item : response.getItems()) {
					if (item.isFailed()) {
						++failed;
					}
				}
			}
			return failed;
		} catch (Exception e) {
			LOG.error("Error while executing bulk request ", e);
			return bulkRequest.numberOfActions();
		}
	}

}
