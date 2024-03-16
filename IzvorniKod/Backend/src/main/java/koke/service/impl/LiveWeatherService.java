package koke.service.impl;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;

import koke.domain.Conference;
import koke.dto.CurrentWeatherDTO;
import koke.service.ConferenceService;

@Service
public class LiveWeatherService {
	
	@Autowired 
	ConferenceService conferenceService;

	private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q={city},{country}&APPID={key}&units=metric";

	private String apiKey = "a485c482bf22711886587378ffc960a1";

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	public LiveWeatherService(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
		this.restTemplate = restTemplateBuilder.build();
		this.objectMapper = objectMapper;
	}

	public CurrentWeatherDTO getCurrentWeather(Long idConference) {
		Conference conference = conferenceService.getByConferenceId(idConference);
		String city = conference.getCity();
		String country = conference.getCountry();
		URI url = new UriTemplate(WEATHER_URL).expand(city, country, apiKey);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		return convert(response);
	}

	private CurrentWeatherDTO convert(ResponseEntity<String> response) {
		try {
			JsonNode root = objectMapper.readTree(response.getBody());
			return new CurrentWeatherDTO(root.path("weather").get(0).path("main").asText(),
					BigDecimal.valueOf(root.path("main").path("temp").asDouble()),
					BigDecimal.valueOf(root.path("main").path("feels_like").asDouble()),
					BigDecimal.valueOf(root.path("wind").path("speed").asDouble()));
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error parsing JSON", e);
		}
	}
}
