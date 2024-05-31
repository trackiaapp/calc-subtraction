package trackia.app.example.calc.subtraction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import lombok.AllArgsConstructor;
import trackia.app.configuration.TrackiaConfiguration;
import trackia.app.util.RestTemplateJournal;

@ComponentScan(basePackages = {"trackia.app", "trackia.app.example.calc.addition"})
@Configuration
@AllArgsConstructor
public class RestTemplateConfig {
	final TrackiaConfiguration config;

    @Bean
	public RestTemplateJournal restTemplate() {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setReadTimeout(3000);
		RestTemplateJournal restTemplate = new RestTemplateJournal(config);
		restTemplate.setRequestFactory(requestFactory);
		
		return restTemplate;
	}
}
