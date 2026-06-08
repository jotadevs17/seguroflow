package com.seguroflow.policyapi.controller;

import com.seguroflow.policyapi.domain.dto.SinistroDTO;
import com.seguroflow.policyapi.domain.dto.SinistroResponseDTO;
import com.seguroflow.policyapi.service.SinistroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sinistros")
public class SinistroController {

    private final SinistroService sinistroService;

    public SinistroController(SinistroService sinistroService) {
        this.sinistroService = sinistroService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SinistroResponseDTO criar(@Valid @RequestBody SinistroDTO dto) {
        return sinistroService.criar(dto);
    }

    @GetMapping
    public List<SinistroResponseDTO> listar() {
        return sinistroService.listar();
    }

    @GetMapping("/{id}")
    public SinistroResponseDTO buscarPorId(@PathVariable Long id) {
        return sinistroService.buscarPorId(id);
    }
}
