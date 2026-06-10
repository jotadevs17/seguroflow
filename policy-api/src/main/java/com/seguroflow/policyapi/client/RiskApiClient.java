package com.seguroflow.policyapi.client;

import com.seguroflow.policyapi.client.dto.RiskAnalysisRequestDTO;
import com.seguroflow.policyapi.client.dto.RiskAnalysisResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

@Component
public class RiskApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RiskApiClient.class);

    private final RestClient restClient;

    public RiskApiClient(@Value("${seguroflow.risk-api.base-url}") String riskApiBaseUrl) {
        this.restClient = RestClient.create(riskApiBaseUrl);
    }

    public RiskAnalysisResponseDTO analyze(RiskAnalysisRequestDTO request) {
        try {
            RiskAnalysisResponseDTO response = restClient.post()
                    .uri("/risk-analysis")
                    .body(request)
                    .retrieve()
                    .body(RiskAnalysisResponseDTO.class);

            if (response == null || response.riskLevel() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Risk API retornou resposta invalida");
            }

            return response;
        } catch (RestClientException ex) {
            LOGGER.warn("Risk API indisponivel ou retornou erro", ex);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Risk API indisponivel", ex);
        }
    }
}
