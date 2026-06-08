package com.seguroflow.policyapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
public class Cliente {

    @Id
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String cpf;

    @NotBlank
    private String email;

    @NotBlank
    private String telefone;

    private List<Apolice> apolices;

}
