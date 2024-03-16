package koke;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import koke.dto.ConferenceDTO;
import koke.init.JwtRequest;

public class ConferenceTest {

	@Disabled
	@Test
	public void createConferenceRight() {
		RestTemplate restTemplate = new RestTemplate();
		String tokenUrl = "http://localhost:8080/authenticate";
		JwtRequest req = new JwtRequest("systemowner", "password");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String accessToken = restTemplate.postForObject(tokenUrl, req, String.class);
		headers.set("Authorization", "Bearer " + accessToken.substring(10, accessToken.length() - 2));
		ConferenceDTO conference = new ConferenceDTO(1, "konfa", "grad", "drzava", new Date(2023, 5, 1),
				new Date(2023, 6, 1), "konfa", "neke teme");
		String createKonfa = "http://localhost:8080/";
		HttpEntity<ConferenceDTO> req1 = new HttpEntity<>(conference, headers);
		ResponseEntity<ConferenceDTO> konfa = restTemplate.postForEntity(createKonfa, req1, ConferenceDTO.class, 20);
		Assertions.assertNotNull(konfa);
	}
	@Disabled
	@Test
	public void wrongCreateConference() {
		RestTemplate restTemplate = new RestTemplate();
		String tokenUrl = "http://localhost:8080/authenticate";
		JwtRequest req = new JwtRequest("systemowner", "password");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String accessToken = restTemplate.postForObject(tokenUrl, req, String.class);
		headers.set("Authorization", "Bearer " + accessToken.substring(10, accessToken.length() - 2));
		ConferenceDTO conference = new ConferenceDTO(1, "konfaKriva", "grad", "drzava", new Date(2023, 7, 1),
				new Date(2023, 6, 1), "konfa", "neke teme");
		String createKonfa = "http://localhost:8080/";
		HttpEntity<ConferenceDTO> req1 = new HttpEntity<>(conference, headers);
		Assertions.assertThrows(HttpClientErrorException.BadRequest.class, () -> {
			restTemplate.postForEntity(createKonfa, req1, ConferenceDTO.class, 20);
		});

	}
}
