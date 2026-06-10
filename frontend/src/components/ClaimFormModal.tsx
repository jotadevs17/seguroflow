import type { FormEvent } from "react";
import { useEffect, useMemo, useState } from "react";
import { createClaim, listPolicies } from "../services/policyApi";
import type { Claim, CreateClaimInput, Policy } from "../types/insurance";

interface ClaimFormModalProps {
  onClose: () => void;
  onCreated: (claim: Claim) => void;
}

interface ClaimFormState {
  descricao: string;
  valorEstimado: string;
  dataOcorrencia: string;
  apoliceId: string;
}

const initialForm: ClaimFormState = {
  descricao: "",
  valorEstimado: "",
  dataOcorrencia: "",
  apoliceId: ""
};

const currencyFormatter = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL"
});

export function ClaimFormModal({ onClose, onCreated }: ClaimFormModalProps) {
  const [form, setForm] = useState<ClaimFormState>(initialForm);
  const [policies, setPolicies] = useState<Policy[]>([]);
  const [isLoadingPolicies, setIsLoadingPolicies] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let isMounted = true;

    async function loadPolicies() {
      try {
        setIsLoadingPolicies(true);
        setError(null);
        const data = await listPolicies();

        if (isMounted) {
          setPolicies(data);
          setForm((current) => ({ ...current, apoliceId: current.apoliceId || String(data[0]?.id ?? "") }));
        }
      } catch (caughtError) {
        if (isMounted) {
          setError(caughtError instanceof Error ? caughtError.message : "Nao foi possivel carregar as apolices.");
        }
      } finally {
        if (isMounted) {
          setIsLoadingPolicies(false);
        }
      }
    }

    void loadPolicies();

    return () => {
      isMounted = false;
    };
  }, []);

  const selectedPolicy = useMemo(
    () => policies.find((policy) => String(policy.id) === form.apoliceId),
    [form.apoliceId, policies]
  );

  const isFormComplete =
    form.apoliceId !== "" &&
    form.descricao.trim() !== "" &&
    Number(form.valorEstimado) > 0 &&
    form.dataOcorrencia !== "";
  const canSubmit = !isLoadingPolicies && !isSubmitting && policies.length > 0 && isFormComplete;

  function validateForm(): CreateClaimInput | null {
    const descricao = form.descricao.trim();
    const valorEstimado = Number(form.valorEstimado);
    const apoliceId = Number(form.apoliceId);

    if (!apoliceId) {
      setError("Selecione uma apolice para abrir o sinistro.");
      return null;
    }

    if (!descricao) {
      setError("Informe a descricao do sinistro.");
      return null;
    }

    if (!Number.isFinite(valorEstimado) || valorEstimado <= 0) {
      setError("Informe um valor estimado maior que zero.");
      return null;
    }

    if (!form.dataOcorrencia) {
      setError("Informe a data da ocorrencia.");
      return null;
    }

    return {
      descricao,
      valorEstimado,
      dataOcorrencia: form.dataOcorrencia,
      status: "ABERTO",
      apoliceId
    };
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError(null);

    const payload = validateForm();

    if (!payload) {
      return;
    }

    try {
      setIsSubmitting(true);
      const createdClaim = await createClaim(payload);
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
            Apolice
            <select
              value={form.apoliceId}
              onChange={(event) => setForm((current) => ({ ...current, apoliceId: event.target.value }))}
              required
              disabled={isLoadingPolicies || isSubmitting || policies.length === 0}
            >
              <option value="">{isLoadingPolicies ? "Carregando apolices..." : "Selecione uma apolice"}</option>
              {policies.map((policy) => (
                <option key={policy.id} value={policy.id}>
                  {policy.numero} - {policy.tipoSeguro} - {currencyFormatter.format(Number(policy.valorSegurado))}
                </option>
              ))}
            </select>
          </label>

          {selectedPolicy && (
            <div className="policy-summary">
              <span>Vigencia</span>
              <strong>
                {selectedPolicy.dataInicio} ate {selectedPolicy.dataFim}
              </strong>
            </div>
          )}

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
                value={form.valorEstimado}
                onChange={(event) =>
                  setForm((current) => ({ ...current, valorEstimado: event.target.value }))
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

          {error && <div className="form-error">{error}</div>}

          <div className="claim-modal__actions">
            <button type="button" className="secondary-action" onClick={onClose} disabled={isSubmitting}>
              Cancelar
            </button>
            <button type="submit" className="primary-action primary-action--orange" disabled={!canSubmit}>
              {isSubmitting ? "Salvando..." : "Salvar sinistro"}
            </button>
          </div>
        </form>
      </section>
    </div>
  );
}
