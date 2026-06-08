package com.seguroflow.policyapi.service;

import com.seguroflow.policyapi.domain.Apolice;
import com.seguroflow.policyapi.domain.Sinistro;
import com.seguroflow.policyapi.domain.dto.SinistroDTO;
import com.seguroflow.policyapi.domain.dto.SinistroResponseDTO;
import com.seguroflow.policyapi.repository.ApoliceRepository;
import com.seguroflow.policyapi.repository.SinistroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SinistroService {

    private final SinistroRepository sinistroRepository;
    private final ApoliceRepository apoliceRepository;

    public SinistroService(SinistroRepository sinistroRepository, ApoliceRepository apoliceRepository) {
        this.sinistroRepository = sinistroRepository;
        this.apoliceRepository = apoliceRepository;
    }

    @Transactional
    public SinistroResponseDTO criar(SinistroDTO dto) {
        Apolice apolice = apoliceRepository.findById(dto.getApoliceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Apolice nao encontrada"));

        Sinistro sinistro = new Sinistro();
        sinistro.setDescricao(dto.getDescricao());
        sinistro.setValorEstimado(dto.getValorEstimado());
        sinistro.setDataOcorrencia(dto.getDataOcorrencia());
        sinistro.setStatus(dto.getStatus());
        sinistro.setNivelRisco(dto.getNivelRisco());
        sinistro.setApolice(apolice);

        return toResponse(sinistroRepository.save(sinistro));
    }

    @Transactional(readOnly = true)
    public List<SinistroResponseDTO> listar() {
        return sinistroRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SinistroResponseDTO buscarPorId(Long id) {
        Sinistro sinistro = sinistroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sinistro nao encontrado"));

        return toResponse(sinistro);
    }

    private SinistroResponseDTO toResponse(Sinistro sinistro) {
        return new SinistroResponseDTO(
                sinistro.getId(),
                sinistro.getDescricao(),
                sinistro.getValorEstimado(),
                sinistro.getDataOcorrencia(),
                sinistro.getStatus(),
                sinistro.getNivelRisco(),
                sinistro.getApolice().getId()
        );
    }
}
