package koke;

import java.util.Date;

import org.json.JSONException;
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
import koke.dto.LoginDTO;
import koke.dto.UserAccountDTO;
import koke.init.JwtRequest;
import koke.rest.UserAccountRole;

public class LoginRegisterTest {

	@Disabled
	@Test
	public void loginTest() throws JSONException {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8080/users/login";
		// nepostojeci user
		Assertions.assertThrows(HttpClientErrorException.Unauthorized.class, () -> {
			LoginDTO a = new LoginDTO("nepostojeci", "password");
			restTemplate.postForEntity(url, a, UserAccountDTO.class);
		});
		// postojeci user
		LoginDTO authorisedLogin = new LoginDTO("systemowner", "password");
		ResponseEntity<UserAccountDTO> account = restTemplate.postForEntity(url, authorisedLogin, UserAccountDTO.class);
		Assertions.assertNotNull(account);
		// user kojem je istekla konferencija
		Assertions.assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			LoginDTO a = new LoginDTO("participant3", "pass123456");
			restTemplate.postForEntity(url, a, UserAccountDTO.class);
		});
	}

	@Disabled
	@Test
	public void registerTest() {
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

		UserAccountDTO acc = new UserAccountDTO(1, "Mato matic", "0987654321", "mato@matic.hr", "Cro", "infinum",
				"kbkjd", "matic4", "12345678", "main admin", UserAccountRole.MAINADMIN,
				(long) konfa.getBody().getIdConference());
		headers.setContentType(MediaType.APPLICATION_JSON);
		accessToken = restTemplate.postForObject(tokenUrl, req, String.class);
		headers.set("Authorization", "Bearer " + accessToken.substring(10, accessToken.length() - 2));
		HttpEntity<UserAccountDTO> req2 = new HttpEntity<>(acc, headers);
		String url = "http://localhost:8080/users/createMainAdmin/" + konfa.getBody().getIdConference();
		ResponseEntity<UserAccountDTO> a = restTemplate.postForEntity(url, req2, UserAccountDTO.class,
				Long.valueOf(konfa.getBody().getIdConference()));
		Assertions.assertNotNull(a);

	}
	@Disabled
	@Test
	public void wrongRegisterTest() {
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

		UserAccountDTO acc = new UserAccountDTO(1, "Mato matic", "0987651", "mato", "Cro", "infinum", "kbkjd", "mat",
				"1234", "main admin", UserAccountRole.MAINADMIN, (long) konfa.getBody().getIdConference());
		headers.setContentType(MediaType.APPLICATION_JSON);
		accessToken = restTemplate.postForObject(tokenUrl, req, String.class);
		headers.set("Authorization", "Bearer " + accessToken.substring(10, accessToken.length() - 2));
		HttpEntity<UserAccountDTO> req2 = new HttpEntity<>(acc, headers);
		String url = "http://localhost:8080/users/createMainAdmin/" + konfa.getBody().getIdConference();
		Assertions.assertThrows(HttpClientErrorException.BadRequest.class, () -> {
			restTemplate.postForEntity(url, req2, UserAccountDTO.class,
					Long.valueOf(konfa.getBody().getIdConference()));
		});

	}

}
