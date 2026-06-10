from app.schemas import RiskAnalysisRequest, RiskAnalysisResponse, RiskLevel


def analyze_risk(request: RiskAnalysisRequest) -> RiskAnalysisResponse:
    claim_ratio = request.claimAmount / request.insuredAmount
    reasons: list[str] = []

    if request.claimAmount > request.insuredAmount * 0.7:
        reasons.append("O valor do sinistro e maior que 70% do valor segurado")
        return RiskAnalysisResponse(riskLevel=RiskLevel.ALTO, reasons=reasons)

    if request.previousClaims >= 3:
        reasons.append("O cliente possui 3 ou mais sinistros anteriores")
        return RiskAnalysisResponse(riskLevel=RiskLevel.ALTO, reasons=reasons)

    if request.daysUntilPolicyExpiration < 30:
        reasons.append("A apolice vence em menos de 30 dias")
        return RiskAnalysisResponse(riskLevel=RiskLevel.MEDIO, reasons=reasons)

    if claim_ratio >= 0.4:
        reasons.append("O valor do sinistro esta entre 40% e 70% do valor segurado")
        return RiskAnalysisResponse(riskLevel=RiskLevel.MEDIO, reasons=reasons)

    reasons.append("Nenhum fator de risco relevante foi identificado")
    return RiskAnalysisResponse(riskLevel=RiskLevel.BAIXO, reasons=reasons)
