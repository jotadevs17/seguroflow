package com.seguroflow.policyapi.domain.dto;

import com.seguroflow.policyapi.domain.enums.NivelRisco;
import com.seguroflow.policyapi.domain.enums.StatusSinistro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SinistroDTO {

    @NotBlank
    private String descricao;

    @NotNull
    @Positive
    private BigDecimal valorEstimado;

    @NotNull
    private LocalDate dataOcorrencia;

    @NotNull
    private StatusSinistro status;

    @NotNull
    private NivelRisco nivelRisco;

    @NotNull
    private Long apoliceId;

    public SinistroDTO() {
    }

    public SinistroDTO(String descricao, BigDecimal valorEstimado, LocalDate dataOcorrencia,
                       StatusSinistro status, NivelRisco nivelRisco, Long apoliceId) {
        this.descricao = descricao;
        this.valorEstimado = valorEstimado;
        this.dataOcorrencia = dataOcorrencia;
        this.status = status;
        this.nivelRisco = nivelRisco;
        this.apoliceId = apoliceId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValorEstimado() {
        return valorEstimado;
    }

    public void setValorEstimado(BigDecimal valorEstimado) {
        this.valorEstimado = valorEstimado;
    }

    public LocalDate getDataOcorrencia() {
        return dataOcorrencia;
    }

    public void setDataOcorrencia(LocalDate dataOcorrencia) {
        this.dataOcorrencia = dataOcorrencia;
    }

    public StatusSinistro getStatus() {
        return status;
    }

    public void setStatus(StatusSinistro status) {
        this.status = status;
    }

    public NivelRisco getNivelRisco() {
        return nivelRisco;
    }

    public void setNivelRisco(NivelRisco nivelRisco) {
        this.nivelRisco = nivelRisco;
    }

    public Long getApoliceId() {
        return apoliceId;
    }

    public void setApoliceId(Long apoliceId) {
        this.apoliceId = apoliceId;
    }
}
