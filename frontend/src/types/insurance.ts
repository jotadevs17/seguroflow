export type RiskLevel = "BAIXO" | "MEDIO" | "ALTO";

export type ClaimStatus = "ABERTO" | "EM_ANALISE" | "APROVADO" | "NEGADO";

export type InsuranceType = "AUTO" | "RESIDENCIAL" | "VIDA";

export interface Customer {
  id: number;
  nome: string;
  cpf: string;
  email: string;
  telefone: string;
  apoliceIds: number[];
}

export interface Policy {
  id: number;
  numero: string;
  tipoSeguro: InsuranceType;
  valorSegurado: number;
  dataInicio: string;
  dataFim: string;
  clienteId: number;
  sinistroIds: number[];
}

export interface Claim {
  id: number;
  descricao: string;
  valorEstimado: number;
  dataOcorrencia: string;
  status: ClaimStatus;
  nivelRisco: RiskLevel;
  apoliceId: number;
}

export interface CreateClaimInput {
  descricao: string;
  valorEstimado: number;
  dataOcorrencia: string;
  status: ClaimStatus;
  apoliceId: number;
}
