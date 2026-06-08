package com.seguroflow.policyapi.domain.dto;

import com.seguroflow.policyapi.domain.enums.TipoSeguro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ApoliceResponseDTO {

    private final Long id;
    private final String numero;
    private final TipoSeguro tipoSeguro;
    private final BigDecimal valorSegurado;
    private final LocalDate dataInicio;
    private final LocalDate dataFim;
    private final Long clienteId;
    private final List<Long> sinistroIds;

    public ApoliceResponseDTO(Long id, String numero, TipoSeguro tipoSeguro, BigDecimal valorSegurado,
                              LocalDate dataInicio, LocalDate dataFim, Long clienteId, List<Long> sinistroIds) {
        this.id = id;
        this.numero = numero;
        this.tipoSeguro = tipoSeguro;
        this.valorSegurado = valorSegurado;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.clienteId = clienteId;
        this.sinistroIds = sinistroIds;
    }

    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public TipoSeguro getTipoSeguro() {
        return tipoSeguro;
    }

    public BigDecimal getValorSegurado() {
        return valorSegurado;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public List<Long> getSinistroIds() {
        return sinistroIds;
    }
}
