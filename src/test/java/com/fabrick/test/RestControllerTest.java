package com.fabrick.test;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fabrick.bean.Account;
import com.fabrick.bean.Address;
import com.fabrick.bean.Bonifico;
import com.fabrick.bean.Creditor;
import com.fabrick.bean.LegalPersonBeneficiary;
import com.fabrick.bean.NaturalPersonBeneficiary;
import com.fabrick.bean.TaxRelief;

@SpringBootTest
public class RestControllerTest {

    private final TestRestTemplate restTemplate;

    public RestControllerTest(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Test
    public void testRestController() {
        String url = "http://localhost:8080/bonifico";

        Bonifico bonifico = bonificoBuilder();

        ResponseEntity<String> response = restTemplate.postForEntity(url, bonifico, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    private Bonifico bonificoBuilder() {
    	
	    Bonifico bonifico = new Bonifico();
	    	Creditor creditor = new Creditor();
			creditor.setName("John Doe");
		
				Account account = new Account();
				account.setAccountCode("IT23A0336844430152923804660");
		
			creditor.setAccount(account);
		
			Address address = new Address();
		
			creditor.setAddress(address);
			
		bonifico.setCreditor(creditor);
		bonifico.setDescription("Payment invoice 75/2017");
		bonifico.setAmount(new Double(800));
		bonifico.setCurrency("EUR");
		
	    	TaxRelief taxRelief = new TaxRelief();
	    	taxRelief.setIsCondoUpgrade(true);
	    	taxRelief.setCreditorFiscalCode("56258745832");
	    	taxRelief.setBeneficiaryType("NATURAL_PERSON");
	    	
		    	NaturalPersonBeneficiary naturalPersonBeneficiary = new NaturalPersonBeneficiary();
		    	naturalPersonBeneficiary.setFiscalCode1("MRLFNC81L04A859L");
		    	
		    	taxRelief.setNaturalPersonBeneficiary(naturalPersonBeneficiary);
		
		    	LegalPersonBeneficiary legalPersonBeneficiary = new LegalPersonBeneficiary();
		    	legalPersonBeneficiary.setFiscalCode("MRLFNC81L04A859L");
		    	
		    	taxRelief.setLegalPersonBeneficiary(legalPersonBeneficiary);
		    	
		bonifico.setTaxRelief(taxRelief);
		
		return bonifico;
    }
}