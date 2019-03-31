package ms.auth.poc.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IDAuthenticationDelegationService {

    @Value("${authn.server.url.authenticate}")
    private String authenticationUrl;

    public Assertion getAssertion(String token) {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<Assertion> response = template.exchange(authenticationUrl, HttpMethod.POST, entity, Assertion.class);
        return response.getBody();
    }

}
