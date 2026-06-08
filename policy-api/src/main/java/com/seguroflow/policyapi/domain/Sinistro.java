package com.seguroflow.policyapi.domain;

import com.seguroflow.policyapi.domain.enums.NivelRisco;
import com.seguroflow.policyapi.domain.enums.StatusSinistro;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Sinistro {

    @Id
    private Long id;

    @NotBlank
    private String descricao;

    private BigDecimal valorEstimado;

    private LocalDate dataOcorrencia;

    private StatusSinistro status;
    
    private NivelRisco nivelRisco;

    private Apolice apolice;

}
