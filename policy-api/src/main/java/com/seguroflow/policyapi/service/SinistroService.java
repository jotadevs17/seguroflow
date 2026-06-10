package com.seguroflow.policyapi.service;

import com.seguroflow.policyapi.client.RiskApiClient;
import com.seguroflow.policyapi.client.dto.RiskAnalysisRequestDTO;
import com.seguroflow.policyapi.client.dto.RiskAnalysisResponseDTO;
import com.seguroflow.policyapi.domain.Apolice;
import com.seguroflow.policyapi.domain.Sinistro;
import com.seguroflow.policyapi.domain.dto.SinistroDTO;
import com.seguroflow.policyapi.domain.dto.SinistroResponseDTO;
import com.seguroflow.policyapi.domain.enums.NivelRisco;
import com.seguroflow.policyapi.repository.ApoliceRepository;
import com.seguroflow.policyapi.repository.SinistroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class SinistroService {

    private final SinistroRepository sinistroRepository;
    private final ApoliceRepository apoliceRepository;
    private final RiskApiClient riskApiClient;

    public SinistroService(SinistroRepository sinistroRepository, ApoliceRepository apoliceRepository,
                           RiskApiClient riskApiClient) {
        this.sinistroRepository = sinistroRepository;
        this.apoliceRepository = apoliceRepository;
        this.riskApiClient = riskApiClient;
    }

    @Transactional
    public SinistroResponseDTO criar(SinistroDTO dto) {
        Apolice apolice = apoliceRepository.findById(dto.getApoliceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Apolice nao encontrada"));

        if (dto.getDataOcorrencia().isBefore(apolice.getDataInicio())
                || dto.getDataOcorrencia().isAfter(apolice.getDataFim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data do sinistro fora da vigencia da apolice");
        }

        RiskAnalysisResponseDTO riskAnalysis = riskApiClient.analyze(buildRiskAnalysisRequest(dto, apolice));

        Sinistro sinistro = new Sinistro();
        sinistro.setDescricao(dto.getDescricao());
        sinistro.setValorEstimado(dto.getValorEstimado());
        sinistro.setDataOcorrencia(dto.getDataOcorrencia());
        sinistro.setStatus(dto.getStatus());
        sinistro.setNivelRisco(toNivelRisco(riskAnalysis.riskLevel()));
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

    private RiskAnalysisRequestDTO buildRiskAnalysisRequest(SinistroDTO dto, Apolice apolice) {
        long daysUntilPolicyExpiration = Math.max(0, ChronoUnit.DAYS.between(LocalDate.now(), apolice.getDataFim()));
        long previousClaims = sinistroRepository.countByApolice_Id(apolice.getId());

        return new RiskAnalysisRequestDTO(
                dto.getValorEstimado(),
                apolice.getValorSegurado(),
                daysUntilPolicyExpiration,
                previousClaims
        );
    }

    private NivelRisco toNivelRisco(String riskLevel) {
        try {
            return NivelRisco.valueOf(riskLevel);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Risk API retornou nivel de risco invalido: " + riskLevel,
                    ex
            );
        }
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
