import { useEffect, useMemo, useState } from "react";
import { ClaimFormModal } from "../components/ClaimFormModal";
import { ClaimTable } from "../components/ClaimTable";
import { RiskBadge } from "../components/RiskBadge";
import { SummaryCard } from "../components/SummaryCard";
import { listClaims } from "../services/policyApi";
import type { Claim, RiskLevel } from "../types/insurance";

const emptyRiskCounts: Record<RiskLevel, number> = {
  BAIXO: 0,
  MEDIO: 0,
  ALTO: 0
};

export function ClaimsDashboardPage() {
  const [claims, setClaims] = useState<Claim[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [successClaim, setSuccessClaim] = useState<Claim | null>(null);
  const [isFormOpen, setIsFormOpen] = useState(false);

  useEffect(() => {
    let isMounted = true;

    async function loadClaims() {
      try {
        setIsLoading(true);
        setError(null);
        const data = await listClaims();

        if (isMounted) {
          setClaims([...data].sort((first, second) => second.id - first.id));
        }
      } catch (caughtError) {
        if (isMounted) {
          setError(caughtError instanceof Error ? caughtError.message : "Nao foi possivel carregar os sinistros.");
        }
      } finally {
        if (isMounted) {
          setIsLoading(false);
        }
      }
    }

    void loadClaims();

    return () => {
      isMounted = false;
    };
  }, []);

  const riskCounts = useMemo(
    () =>
      claims.reduce<Record<RiskLevel, number>>(
        (totals, claim) => ({
          ...totals,
          [claim.nivelRisco]: totals[claim.nivelRisco] + 1
        }),
        { ...emptyRiskCounts }
      ),
    [claims]
  );

  return (
    <main className="dashboard">
      <section className="hero-panel" aria-labelledby="dashboard-title">
        <div className="hero-panel__content">
          <p className="eyebrow">SeguroFlow</p>
          <h1 id="dashboard-title">Painel operacional de sinistros</h1>
          <p>
            Plataforma desenvolvida visando reforçar os estudos para vaga de Desenvolvedor Trainee na Confitec. Desenvolvido por João Pedro Ferreira
          </p>
          <button
            type="button"
            className="primary-action"
            onClick={() => {
              setSuccessClaim(null);
              setIsFormOpen(true);
            }}
          >
            Novo sinistro
          </button>
        </div>

        <div className="hero-panel__visual" aria-hidden="true">
          <div className="radial-card radial-card--main">
            <span>Total</span>
            <strong>{claims.length}</strong>
            <small>sinistros</small>
          </div>
          <div className="radial-card radial-card--low">Baixo</div>
          <div className="radial-card radial-card--medium">Medio</div>
          <div className="radial-card radial-card--high">Alto</div>
        </div>
      </section>

      <section className="content-band" aria-label="Indicadores e listagem">
        <div className="summary-grid">
          <SummaryCard label="Total de sinistros" value={claims.length} />
          <SummaryCard label="Risco baixo" value={riskCounts.BAIXO} tone="low" />
          <SummaryCard label="Risco medio" value={riskCounts.MEDIO} tone="medium" />
          <SummaryCard label="Risco alto" value={riskCounts.ALTO} tone="high" />
        </div>

        <div className="claims-panel">
          <div className="claims-panel__header">
            <div>
              <p className="eyebrow">Monitoramento</p>
              <h2>Sinistros recentes</h2>
            </div>
            <span>{claims.length} registros</span>
          </div>

          {successClaim && (
            <div className="success-message" role="status">
              <span>Sinistro #{successClaim.id} criado</span>
              <strong>Risco calculado pela API:</strong>
              <RiskBadge level={successClaim.nivelRisco} />
            </div>
          )}

          {isLoading && <div className="state-message">Carregando sinistros...</div>}

          {!isLoading && error && (
            <div className="state-message state-message--error">
              <strong>API indisponivel</strong>
              <span>{error}</span>
            </div>
          )}

          {!isLoading && !error && claims.length === 0 && (
            <div className="state-message">
              <strong>Nenhum sinistro cadastrado</strong>
              <span>Quando a policy-api retornar registros, eles aparecem nesta lista.</span>
            </div>
          )}

          {!isLoading && !error && claims.length > 0 && <ClaimTable claims={claims} />}
        </div>
      </section>

      {isFormOpen && (
        <ClaimFormModal
          onClose={() => setIsFormOpen(false)}
          onCreated={(claim) => {
            setClaims((currentClaims) => [claim, ...currentClaims.filter((currentClaim) => currentClaim.id !== claim.id)]);
            setSuccessClaim(claim);
            setError(null);
          }}
        />
      )}
    </main>
  );
}
