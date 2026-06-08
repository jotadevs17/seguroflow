package com.seguroflow.policyapi.controller;

import com.seguroflow.policyapi.domain.dto.ApoliceDTO;
import com.seguroflow.policyapi.domain.dto.ApoliceResponseDTO;
import com.seguroflow.policyapi.service.ApoliceService;
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
@RequestMapping("/apolices")
public class ApoliceController {

    private final ApoliceService apoliceService;

    public ApoliceController(ApoliceService apoliceService) {
        this.apoliceService = apoliceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApoliceResponseDTO criar(@Valid @RequestBody ApoliceDTO dto) {
        return apoliceService.criar(dto);
    }

    @GetMapping
    public List<ApoliceResponseDTO> listar() {
        return apoliceService.listar();
    }

    @GetMapping("/{id}")
    public ApoliceResponseDTO buscarPorId(@PathVariable Long id) {
        return apoliceService.buscarPorId(id);
    }
}
