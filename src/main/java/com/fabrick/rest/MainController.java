package com.fabrick.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fabrick.bean.Bonifico;
import com.fabrick.utils.RestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class MainController {

	private static final Logger logger = LogManager.getLogger(MainController.class);
	
    @Value("${api.baseUrl}")
    private String baseUrl;

    @Value("${api.authSchema}")
    private String authSchema;

    @Value("${api.apiKey}")
    private String apiKey;
    
    @Value("${api.accountId}")
    private String accountId;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    @GetMapping("/saldo")
    public ResponseEntity<String> getSaldo() {
    	
    	String apiUrl = baseUrl + "/api/gbs/banking/v4.0/accounts";

    	logger.info("Calling saldo endpoint: " + apiUrl);
    	
        HttpHeaders headers = createHeaders();
        
        ResponseEntity<String> response = sendGETRequest(apiUrl, headers);

        if (response.getStatusCode() == HttpStatus.OK) {
        	logger.info("Service saldo status code: " + HttpStatus.OK);
        	
            return ResponseEntity.ok(response.getBody());
        } else {
        	logger.info("Service saldo: " + response.getStatusCode());
        	
            return ResponseEntity.status(response.getStatusCode()).body("Errore durante la lettura del saldo.");
        }
        
    }

    @GetMapping("/transazioni")
    public ResponseEntity<String> getTransazioni(@RequestParam("accountId") String accountId, @RequestParam(value = "fromAccountingDate", required = false) String fromAccountingDate,@RequestParam(value = "toAccountingDate", required = false) String toAccountingDate) throws URISyntaxException {
    	
    	String apiUrl = baseUrl + "/api/gbs/banking/v4.0/accounts/" + accountId + "/transactions";
    	
    	logger.info("Calling transazioni endpoint: " + apiUrl);
    	
    	HttpHeaders headers = createHeaders();
    	
    	Map<String, String> params = new HashMap<>();
        params.put("fromAccountingDate", fromAccountingDate);
        params.put("toAccountingDate", toAccountingDate);
        
		URI uri = RestUtils.createURIWithParameters(apiUrl, params);
		        
    	ResponseEntity<String> response = sendGETRequest(uri.toString(), headers);

        if (response.getStatusCode() == HttpStatus.OK) {
        	logger.info("Service transazioni status code: " + HttpStatus.OK);
        	
            return ResponseEntity.ok(response.getBody());
        } else {
        	logger.info("Service transazioni: " + response.getStatusCode());
        	
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la lettura delle transazioni.");
        }
    }

    @PostMapping("/bonifico")
    public ResponseEntity<String> effettuaBonifico(@RequestBody Bonifico bonifico) {
    	
    	String apiUrl = baseUrl + "/api/gbs/banking/v4.0/accounts/" + accountId + "/payments/money-transfers";
    	
    	logger.info("Calling bonifico endpoint: " + apiUrl);
    	
        ObjectMapper objectMapper = new ObjectMapper();
        
        String bonificoJSON = "";
        
        HttpHeaders headers = createHeaders();
        
        try {
        	bonificoJSON = objectMapper.writeValueAsString(bonifico);
		} catch (JsonProcessingException e) {			
			e.printStackTrace();
		}              
        
        ResponseEntity<String> response = sendPOSTRequest(apiUrl, headers, bonificoJSON);

        if (response.getStatusCode() == HttpStatus.OK) {
        	logger.info("Service bonifico status code: " + HttpStatus.OK);
        	
            return ResponseEntity.ok(response.getBody());
        } else {
        	logger.info("Service bonifico: " + response.getStatusCode());
        	
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'invio del bonifico.");
        }
        
    }

    private ResponseEntity<String> sendGETRequest(String endpoint, HttpHeaders headers) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.GET, entity, String.class);
        return response;
    }
       
    private ResponseEntity<String> sendPOSTRequest(String endpoint, HttpHeaders headers, String requestBody) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.POST, entity, String.class);
        return response;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Auth-Schema", authSchema);
        headers.set("Api-Key", apiKey);
        headers.set("X-Time-Zone", "Europe/Rome");
        return headers;
    }
}
