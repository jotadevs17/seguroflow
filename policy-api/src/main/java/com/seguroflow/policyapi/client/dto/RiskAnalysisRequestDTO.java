package com.seguroflow.policyapi.client.dto;

import java.math.BigDecimal;

public record RiskAnalysisRequestDTO(
        BigDecimal claimAmount,
        BigDecimal insuredAmount,
        long daysUntilPolicyExpiration,
        long previousClaims
) {
}
