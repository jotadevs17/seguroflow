package com.seguroflow.policyapi.domain.dto;

import com.seguroflow.policyapi.domain.enums.NivelRisco;
import com.seguroflow.policyapi.domain.enums.StatusSinistro;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SinistroResponseDTO {

    private final Long id;
    private final String descricao;
    private final BigDecimal valorEstimado;
    private final LocalDate dataOcorrencia;
    private final StatusSinistro status;
    private final NivelRisco nivelRisco;
    private final Long apoliceId;

    public SinistroResponseDTO(Long id, String descricao, BigDecimal valorEstimado, LocalDate dataOcorrencia,
                               StatusSinistro status, NivelRisco nivelRisco, Long apoliceId) {
        this.id = id;
        this.descricao = descricao;
        this.valorEstimado = valorEstimado;
        this.dataOcorrencia = dataOcorrencia;
        this.status = status;
        this.nivelRisco = nivelRisco;
        this.apoliceId = apoliceId;
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValorEstimado() {
        return valorEstimado;
    }

    public LocalDate getDataOcorrencia() {
        return dataOcorrencia;
    }

    public StatusSinistro getStatus() {
        return status;
    }

    public NivelRisco getNivelRisco() {
        return nivelRisco;
    }

    public Long getApoliceId() {
        return apoliceId;
    }
}
