import type { FormEvent } from "react";
import { useState } from "react";
import { createClaim } from "../services/policyApi";
import type { Claim, ClaimStatus, CreateClaimInput } from "../types/insurance";

interface ClaimFormModalProps {
  onClose: () => void;
  onCreated: (claim: Claim) => void;
}

const initialForm: CreateClaimInput = {
  descricao: "",
  valorEstimado: 0,
  dataOcorrencia: "",
  status: "ABERTO",
  apoliceId: 0
};

const statusOptions: Array<{ value: ClaimStatus; label: string }> = [
  { value: "ABERTO", label: "Aberto" },
  { value: "EM_ANALISE", label: "Em analise" },
  { value: "APROVADO", label: "Aprovado" },
  { value: "NEGADO", label: "Negado" }
];

export function ClaimFormModal({ onClose, onCreated }: ClaimFormModalProps) {
  const [form, setForm] = useState<CreateClaimInput>(initialForm);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    try {
      setIsSubmitting(true);
      setError(null);
      const createdClaim = await createClaim(form);
      onCreated(createdClaim);
      onClose();
    } catch (caughtError) {
      setError(caughtError instanceof Error ? caughtError.message : "Nao foi possivel cadastrar o sinistro.");
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <div className="modal-backdrop" role="presentation">
      <section className="claim-modal" role="dialog" aria-modal="true" aria-labelledby="claim-modal-title">
        <div className="claim-modal__header">
          <div>
            <p className="eyebrow">Novo registro</p>
            <h2 id="claim-modal-title">Cadastrar sinistro</h2>
          </div>
          <button type="button" className="ghost-action" onClick={onClose} disabled={isSubmitting}>
            Fechar
          </button>
        </div>

        <form className="claim-form" onSubmit={handleSubmit}>
          <label>
            Descricao
            <textarea
              value={form.descricao}
              onChange={(event) => setForm((current) => ({ ...current, descricao: event.target.value }))}
              required
              rows={3}
              placeholder="Ex.: Colisao traseira em vistoria inicial"
            />
          </label>

          <div className="form-grid">
            <label>
              Valor estimado
              <input
                type="number"
                min="0.01"
                step="0.01"
                value={form.valorEstimado || ""}
                onChange={(event) =>
                  setForm((current) => ({ ...current, valorEstimado: Number(event.target.value) }))
                }
                required
              />
            </label>

            <label>
              Data da ocorrencia
              <input
                type="date"
                value={form.dataOcorrencia}
                onChange={(event) => setForm((current) => ({ ...current, dataOcorrencia: event.target.value }))}
                required
              />
            </label>
          </div>

          <div className="form-grid">
            <label>
              Status
              <select
                value={form.status}
                onChange={(event) =>
                  setForm((current) => ({ ...current, status: event.target.value as ClaimStatus }))
                }
              >
                {statusOptions.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
            </label>

            <label>
              ID da apolice
              <input
                type="number"
                min="1"
                step="1"
                value={form.apoliceId || ""}
                onChange={(event) => setForm((current) => ({ ...current, apoliceId: Number(event.target.value) }))}
                required
              />
            </label>
          </div>

          {error && <div className="form-error">{error}</div>}

          <div className="claim-modal__actions">
            <button type="button" className="secondary-action" onClick={onClose} disabled={isSubmitting}>
              Cancelar
            </button>
            <button type="submit" className="primary-action primary-action--orange" disabled={isSubmitting}>
              {isSubmitting ? "Salvando..." : "Salvar sinistro"}
            </button>
          </div>
        </form>
      </section>
    </div>
  );
}
