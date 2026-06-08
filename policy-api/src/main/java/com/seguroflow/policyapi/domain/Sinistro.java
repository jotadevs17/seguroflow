package com.seguroflow.policyapi.domain;

import com.seguroflow.policyapi.domain.enums.NivelRisco;
import com.seguroflow.policyapi.domain.enums.StatusSinistro;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sinistros")
public class Sinistro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String descricao;

    @NotNull
    @Positive
    @Column(nullable = false)
    private BigDecimal valorEstimado;

    @NotNull
    @Column(nullable = false)
    private LocalDate dataOcorrencia;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSinistro status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelRisco nivelRisco;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "apolice_id", nullable = false)
    private Apolice apolice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Apolice getApolice() {
        return apolice;
    }

    public void setApolice(Apolice apolice) {
        this.apolice = apolice;
    }

}
