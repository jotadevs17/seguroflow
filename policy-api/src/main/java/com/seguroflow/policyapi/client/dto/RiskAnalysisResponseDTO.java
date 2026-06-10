package com.seguroflow.policyapi.client.dto;

import java.util.List;

public record RiskAnalysisResponseDTO(
        String riskLevel,
        List<String> reasons
) {
}
