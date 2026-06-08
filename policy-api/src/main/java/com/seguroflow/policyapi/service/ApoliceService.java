package com.seguroflow.policyapi.service;

import com.seguroflow.policyapi.domain.Apolice;
import com.seguroflow.policyapi.domain.Cliente;
import com.seguroflow.policyapi.domain.Sinistro;
import com.seguroflow.policyapi.domain.dto.ApoliceDTO;
import com.seguroflow.policyapi.domain.dto.ApoliceResponseDTO;
import com.seguroflow.policyapi.repository.ApoliceRepository;
import com.seguroflow.policyapi.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ApoliceService {

    private final ApoliceRepository apoliceRepository;
    private final ClienteRepository clienteRepository;

    public ApoliceService(ApoliceRepository apoliceRepository, ClienteRepository clienteRepository) {
        this.apoliceRepository = apoliceRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public ApoliceResponseDTO criar(ApoliceDTO dto) {
        if (apoliceRepository.existsByNumero(dto.getNumero())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Numero de apolice ja cadastrado");
        }

        if (dto.getDataFim().isBefore(dto.getDataInicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data fim nao pode ser anterior a data inicio");
        }

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente nao encontrado"));

        Apolice apolice = new Apolice();
        apolice.setNumero(dto.getNumero());
        apolice.setTipoSeguro(dto.getTipoSeguro());
        apolice.setValorSegurado(dto.getValorSegurado());
        apolice.setDataInicio(dto.getDataInicio());
        apolice.setDataFim(dto.getDataFim());
        apolice.setCliente(cliente);

        return toResponse(apoliceRepository.save(apolice));
    }

    @Transactional(readOnly = true)
    public List<ApoliceResponseDTO> listar() {
        return apoliceRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ApoliceResponseDTO buscarPorId(Long id) {
        Apolice apolice = apoliceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Apolice nao encontrada"));

        return toResponse(apolice);
    }

    private ApoliceResponseDTO toResponse(Apolice apolice) {
        List<Long> sinistroIds = apolice.getSinistros()
                .stream()
                .map(Sinistro::getId)
                .toList();

        return new ApoliceResponseDTO(
                apolice.getId(),
                apolice.getNumero(),
                apolice.getTipoSeguro(),
                apolice.getValorSegurado(),
                apolice.getDataInicio(),
                apolice.getDataFim(),
                apolice.getCliente().getId(),
                sinistroIds
        );
    }
}
