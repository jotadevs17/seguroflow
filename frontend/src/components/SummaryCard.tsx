interface SummaryCardProps {
  label: string;
  value: number;
  tone?: "neutral" | "low" | "medium" | "high";
}

export function SummaryCard({ label, value, tone = "neutral" }: SummaryCardProps) {
  return (
    <article className={`summary-card summary-card--${tone}`}>
      <span>{label}</span>
      <strong>{value}</strong>
    </article>
  );
}
