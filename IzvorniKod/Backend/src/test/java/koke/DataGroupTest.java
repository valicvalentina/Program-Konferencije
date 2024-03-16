package koke;

import koke.dto.DataGroupDTO;
import koke.dto.UserAccountDTO;
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

public class DataGroupTest {
    @Disabled
    @Test
    public void createDataGroupTest() {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "http://localhost:8080/authenticate";
        JwtRequest req = new JwtRequest("main1", "12345678");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String accessToken = restTemplate.postForObject(tokenUrl, req, String.class);
        headers.set("Authorization", "Bearer " + accessToken.substring(10, accessToken.length() - 2));
        DataGroupDTO dataGroupDTO = new DataGroupDTO(100, "Nova grupa podataka", "Podatci o grupi podataka");
        String url = "http://localhost:8080/dataGroup/add/1";
        HttpEntity<DataGroupDTO> req1 = new HttpEntity<>(dataGroupDTO, headers);
        ResponseEntity<DataGroupDTO> responseEntity = restTemplate.postForEntity(url, req1, DataGroupDTO.class, Long.valueOf(1));
        Assertions.assertNotNull(responseEntity);
        DataGroupDTO dataGroup = responseEntity.getBody();
        Assertions.assertTrue(Objects.equals(dataGroup.getGroupName(), "Nova grupa podataka"));
        Assertions.assertTrue(Objects.equals(dataGroup.getData(), "Podatci o grupi podataka"
        ));
    }

    @Disabled
    @Test
    public void createDataGroupUnauthorizedTest() {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "http://localhost:8080/authenticate";
        JwtRequest req = new JwtRequest("oper1", "12345678");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String accessToken = restTemplate.postForObject(tokenUrl, req, String.class);
        headers.set("Authorization", "Bearer " + accessToken.substring(10, accessToken.length() - 2));
        DataGroupDTO dataGroupDTO = new DataGroupDTO(100, "Jos jedna grupa podataka", "Podatci o grupi podataka");
        String url = "http://localhost:8080/dataGroup/add/1";
        HttpEntity<DataGroupDTO> req1 = new HttpEntity<>(dataGroupDTO, headers);
        Assertions.assertThrows(HttpClientErrorException.Forbidden.class, () -> {
            restTemplate.postForEntity(url, req1, DataGroupDTO.class,
                    Long.valueOf(1));
        });
    }
}
