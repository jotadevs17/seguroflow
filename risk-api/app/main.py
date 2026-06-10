from fastapi import FastAPI

from app.schemas import RiskAnalysisRequest, RiskAnalysisResponse
from app.services.risk_analysis_service import analyze_risk

app = FastAPI()


@app.get("/")
def health_check():
    return {"message": "API de analise de risco em execucao"}


@app.post("/risk-analysis", response_model=RiskAnalysisResponse)
def analyze_risk_endpoint(request: RiskAnalysisRequest):
    return analyze_risk(request)
