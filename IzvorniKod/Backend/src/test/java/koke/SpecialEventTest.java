package koke;

import koke.dto.DataGroupDTO;
import koke.dto.IncreaseDTO;
import koke.dto.SpecialEventDTO;
import koke.init.JwtRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

public class SpecialEventTest {

    @Disabled
    @Test
    public void createSpecialEventTest() {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "http://localhost:8080/authenticate";
        JwtRequest req = new JwtRequest("main1", "12345678");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String accessToken = restTemplate.postForObject(tokenUrl, req, String.class);
        headers.set("Authorization", "Bearer " + accessToken.substring(10, accessToken.length() - 2));
        SpecialEventDTO specialEventDTO = new SpecialEventDTO(100, 15, "Vrsta specijalnog događaja", "Opis");
        String url = "http://localhost:8080/specialEvents/add/1";
        HttpEntity<SpecialEventDTO> req1 = new HttpEntity<>(specialEventDTO, headers);
        ResponseEntity<SpecialEventDTO> responseEntity = restTemplate.postForEntity(url, req1, SpecialEventDTO.class, Long.valueOf(1));
        Assertions.assertNotNull(responseEntity);
        SpecialEventDTO specialEvent = responseEntity.getBody();
        Assertions.assertTrue(Objects.equals(specialEvent.getCapacity(), 15));
        Assertions.assertTrue(Objects.equals(specialEvent.getType(), "Vrsta specijalnog događaja"));
        Assertions.assertTrue(Objects.equals(specialEvent.getMessage(), "Opis"));
    }

    @Disabled
    @Test
    public void createSpecialEventUnauthorizedTest() {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "http://localhost:8080/authenticate";
        JwtRequest req = new JwtRequest("oper1", "12345678");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String accessToken = restTemplate.postForObject(tokenUrl, req, String.class);
        headers.set("Authorization", "Bearer " + accessToken.substring(10, accessToken.length() - 2));
        SpecialEventDTO specialEventDTO = new SpecialEventDTO(100, 15, "Vrsta specijalnog događaja", "Opis");
        String url = "http://localhost:8080/specialEvents/add/1";
        HttpEntity<SpecialEventDTO> req1 = new HttpEntity<>(specialEventDTO, headers);
        Assertions.assertThrows(HttpClientErrorException.Forbidden.class, () -> {
            restTemplate.postForEntity(url, req1, SpecialEventDTO.class,
                    Long.valueOf(1));
        });
    }

    @Disabled
    @Test
    public void attendSpecialEventTest() {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "http://localhost:8080/authenticate";
        JwtRequest req = new JwtRequest("part1", "12345678");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String accessToken = restTemplate.postForObject(tokenUrl, req, String.class);
        headers.set("Authorization", "Bearer " + accessToken.substring(10, accessToken.length() - 2));
        String url = "http://localhost:8080/specialEvents/attend/32/10";
        HttpEntity<SpecialEventDTO> req1 = new HttpEntity<>(null, headers);
        ResponseEntity<SpecialEventDTO> responseEntity = restTemplate.postForEntity(url, req1, SpecialEventDTO.class, Long.valueOf(7), Long.valueOf(10));
        Assertions.assertNotNull(responseEntity);
        SpecialEventDTO specialEvent = responseEntity.getBody();
        Assertions.assertTrue(Objects.equals(specialEvent.getCapacity(), 1));
        Assertions.assertTrue(Objects.equals(specialEvent.getType(), "vrsta"));
        Assertions.assertTrue(Objects.equals(specialEvent.getMessage(), "opis"));
    }

    @Disabled
    @Test
    public void applyToSpecialEventTest() {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "http://localhost:8080/authenticate";
        JwtRequest req = new JwtRequest("part1", "12345678");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String accessToken = restTemplate.postForObject(tokenUrl, req, String.class);
        headers.set("Authorization", "Bearer " + accessToken.substring(10, accessToken.length() - 2));
        String url = "http://localhost:8080/specialEvents/apply/32/10";
        HttpEntity<SpecialEventDTO> req1 = new HttpEntity<>(null, headers);
        ResponseEntity<SpecialEventDTO> responseEntity = restTemplate.postForEntity(url, req1, SpecialEventDTO.class, Long.valueOf(7), Long.valueOf(10));
        Assertions.assertNotNull(responseEntity);
        SpecialEventDTO specialEvent = responseEntity.getBody();
        Assertions.assertTrue(Objects.equals(specialEvent.getCapacity(), 1));
        Assertions.assertTrue(Objects.equals(specialEvent.getType(), "vrsta"));
        Assertions.assertTrue(Objects.equals(specialEvent.getMessage(), "opis"));
    }

    @Disabled
    @Test
    public void increaseCapacitySpecialEventTest() {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "http://localhost:8080/authenticate";
        JwtRequest req = new JwtRequest("main1", "12345678");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String accessToken = restTemplate.postForObject(tokenUrl, req, String.class);
        headers.set("Authorization", "Bearer " + accessToken.substring(10, accessToken.length() - 2));
        String url = "http://localhost:8080/specialEvents/increasingCapacity/32";
        IncreaseDTO increaseDTO = new IncreaseDTO(5);
        HttpEntity<IncreaseDTO> req1 = new HttpEntity<>(increaseDTO, headers);
        ResponseEntity<SpecialEventDTO> responseEntity = restTemplate.postForEntity(url, req1, SpecialEventDTO.class, Long.valueOf(32));
        Assertions.assertNotNull(responseEntity);
        SpecialEventDTO specialEvent = responseEntity.getBody();
        Assertions.assertTrue(Objects.equals(specialEvent.getCapacity(), 6));
        Assertions.assertTrue(Objects.equals(specialEvent.getType(), "vrsta"));
        Assertions.assertTrue(Objects.equals(specialEvent.getMessage(), "opis"));
    }
}