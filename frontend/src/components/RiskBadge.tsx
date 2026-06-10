import type { RiskLevel } from "../types/insurance";

interface RiskBadgeProps {
  level: RiskLevel;
}

const labels: Record<RiskLevel, string> = {
  BAIXO: "Baixo",
  MEDIO: "Medio",
  ALTO: "Alto"
};

export function RiskBadge({ level }: RiskBadgeProps) {
  return <span className={`risk-badge risk-badge--${level.toLowerCase()}`}>{labels[level]}</span>;
}
