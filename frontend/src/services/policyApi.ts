import type { Claim, CreateClaimInput, Policy } from "../types/insurance";

const API_BASE_URL = import.meta.env.VITE_POLICY_API_URL ?? "/api";

interface ApiErrorResponse {
  message?: string;
  fields?: Record<string, string>;
}

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, init);

  if (!response.ok) {
    let apiError: ApiErrorResponse | null = null;

    try {
      apiError = (await response.json()) as ApiErrorResponse;
    } catch {
      apiError = null;
    }

    const fieldMessages = apiError?.fields ? Object.values(apiError.fields).filter(Boolean) : [];
    const detail = fieldMessages[0] ?? apiError?.message;

    throw new Error(detail ?? `Falha ao consultar a policy-api (${response.status})`);
  }

  return response.json() as Promise<T>;
}

export function listPolicies(): Promise<Policy[]> {
  return request<Policy[]>("/apolices");
}

export function listClaims(): Promise<Claim[]> {
  return request<Claim[]>("/sinistros");
}

export function createClaim(input: CreateClaimInput): Promise<Claim> {
  return request<Claim>("/sinistros", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(input)
  });
}
