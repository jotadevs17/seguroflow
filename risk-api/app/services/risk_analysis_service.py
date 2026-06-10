from app.schemas import RiskAnalysisRequest, RiskAnalysisResponse, RiskLevel


def analyze_risk(request: RiskAnalysisRequest) -> RiskAnalysisResponse:
    claim_ratio = request.claimAmount / request.insuredAmount
    reasons: list[str] = []

    if request.claimAmount > request.insuredAmount * 0.7:
        reasons.append("Claim amount is greater than 70% of the insured amount")
        return RiskAnalysisResponse(riskLevel=RiskLevel.HIGH, reasons=reasons)

    if request.previousClaims >= 3:
        reasons.append("Customer has 3 or more previous claims")
        return RiskAnalysisResponse(riskLevel=RiskLevel.HIGH, reasons=reasons)

    if request.daysUntilPolicyExpiration < 30:
        reasons.append("Policy expires in less than 30 days")
        return RiskAnalysisResponse(riskLevel=RiskLevel.MEDIUM, reasons=reasons)

    if claim_ratio >= 0.4:
        reasons.append("Claim amount is between 40% and 70% of the insured amount")
        return RiskAnalysisResponse(riskLevel=RiskLevel.MEDIUM, reasons=reasons)

    reasons.append("No relevant risk factor was identified")
    return RiskAnalysisResponse(riskLevel=RiskLevel.LOW, reasons=reasons)