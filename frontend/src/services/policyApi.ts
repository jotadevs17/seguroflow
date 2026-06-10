import type { Claim, CreateClaimInput } from "../types/insurance";

const API_BASE_URL = import.meta.env.VITE_POLICY_API_URL ?? "/api";

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, init);

  if (!response.ok) {
    throw new Error(`Falha ao consultar a policy-api (${response.status})`);
  }

  return response.json() as Promise<T>;
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
