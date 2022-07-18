package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

public class TransferService {
    private final String baseUrl = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    public void setCurrentUser(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }

    AuthenticatedUser currentUser;

    public boolean transferMoney(int receiverId, BigDecimal transferAmount) {
        boolean isTransferSuccessful = false;
        Transfer transferObject = new Transfer(receiverId, transferAmount);
        HttpEntity<Transfer> entity = prepareEntity(transferObject);
        try {
            restTemplate.exchange(baseUrl + "transfer", HttpMethod.PUT, entity, Boolean.class);
            isTransferSuccessful = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return isTransferSuccessful;
    }

    public Transfer[] viewTransfers() {
        Transfer[] transfers = null;
        HttpEntity entity = prepareEntity();
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "transfer/history", HttpMethod.GET, entity, Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }


    public Transfer transferDetails(int transferId) { //need to pass a chosen account # to pass into transactionDetails
        Transfer transfers = new Transfer();
        HttpEntity entity = prepareEntity();
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(baseUrl + "transfer/" + transferId + "/details", HttpMethod.GET, entity, Transfer.class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    private HttpEntity prepareEntity() {
        return new HttpEntity(prepareHeaders());
    }

    private <TBody> HttpEntity<TBody> prepareEntity(TBody body) {
     // <Tbody> placeholder - tell me what type goes in httpEntity
        return new HttpEntity(body, prepareHeaders());
    }

    private HttpHeaders prepareHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return headers;
    }
}
