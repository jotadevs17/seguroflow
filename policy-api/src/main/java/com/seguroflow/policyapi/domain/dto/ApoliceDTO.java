package com.seguroflow.policyapi.domain.dto;

import com.seguroflow.policyapi.domain.enums.TipoSeguro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ApoliceDTO {

    @NotBlank
    private String numero;

    @NotNull
    private TipoSeguro tipoSeguro;

    @NotNull
    @Positive
    private BigDecimal valorSegurado;

    @NotNull
    private LocalDate dataInicio;

    @NotNull
    private LocalDate dataFim;

    @NotNull
    private Long clienteId;

    public ApoliceDTO() {
    }

    public ApoliceDTO(String numero, TipoSeguro tipoSeguro, BigDecimal valorSegurado, LocalDate dataInicio,
                      LocalDate dataFim, Long clienteId) {
        this.numero = numero;
        this.tipoSeguro = tipoSeguro;
        this.valorSegurado = valorSegurado;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.clienteId = clienteId;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public TipoSeguro getTipoSeguro() {
        return tipoSeguro;
    }

    public void setTipoSeguro(TipoSeguro tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }

    public BigDecimal getValorSegurado() {
        return valorSegurado;
    }

    public void setValorSegurado(BigDecimal valorSegurado) {
        this.valorSegurado = valorSegurado;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}
