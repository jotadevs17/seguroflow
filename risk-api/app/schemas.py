from enum import Enum

from pydantic import BaseModel, Field


class RiskLevel(str, Enum):
    BAIXO = "BAIXO"
    MEDIO = "MEDIO"
    ALTO = "ALTO"


class RiskAnalysisRequest(BaseModel):
    claimAmount: float = Field(gt=0)
    insuredAmount: float = Field(gt=0)
    daysUntilPolicyExpiration: int = Field(ge=0)
    previousClaims: int = Field(ge=0)


class RiskAnalysisResponse(BaseModel):
    riskLevel: RiskLevel
    reasons: list[str]
