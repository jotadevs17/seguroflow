import type { Claim } from "../types/insurance";
import { RiskBadge } from "./RiskBadge";

interface ClaimTableProps {
  claims: Claim[];
}

const statusLabels: Record<Claim["status"], string> = {
  ABERTO: "Aberto",
  EM_ANALISE: "Em analise",
  APROVADO: "Aprovado",
  NEGADO: "Negado"
};

const currencyFormatter = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL"
});

const dateFormatter = new Intl.DateTimeFormat("pt-BR", {
  timeZone: "UTC"
});

export function ClaimTable({ claims }: ClaimTableProps) {
  return (
    <div className="table-shell" aria-label="Lista de sinistros">
      <table>
        <thead>
          <tr>
            <th>Sinistro</th>
            <th>Apolice</th>
            <th>Ocorrencia</th>
            <th>Valor estimado</th>
            <th>Status</th>
            <th>Risco</th>
          </tr>
        </thead>
        <tbody>
          {claims.map((claim) => (
            <tr key={claim.id}>
              <td>
                <strong>#{claim.id}</strong>
                <span>{claim.descricao}</span>
              </td>
              <td>{claim.apoliceId}</td>
              <td>{dateFormatter.format(new Date(`${claim.dataOcorrencia}T00:00:00Z`))}</td>
              <td>{currencyFormatter.format(Number(claim.valorEstimado))}</td>
              <td>{statusLabels[claim.status]}</td>
              <td>
                <RiskBadge level={claim.nivelRisco} />
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
