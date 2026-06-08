package com.seguroflow.policyapi.domain.dto;

import java.util.List;

public class ClienteResponseDTO {

    private final Long id;
    private final String nome;
    private final String cpf;
    private final String email;
    private final String telefone;
    private final List<Long> apoliceIds;

    public ClienteResponseDTO(Long id, String nome, String cpf, String email, String telefone,
                              List<Long> apoliceIds) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.apoliceIds = apoliceIds;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public List<Long> getApoliceIds() {
        return apoliceIds;
    }
}
