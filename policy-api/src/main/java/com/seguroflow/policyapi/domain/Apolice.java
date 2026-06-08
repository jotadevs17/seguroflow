package com.seguroflow.policyapi.domain;

import com.seguroflow.policyapi.domain.enums.TipoSeguro;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Apolice {

    @Id
    private Long id;

    @NotBlank
    private String numero;

    private TipoSeguro tipoSeguro;

    private BigDecimal valorSegurado;


    private LocalDate dataInicio;
    private LocalDate dataFim;

    private Cliente cliente;

    private List<Sinistro> sinistros;

}
