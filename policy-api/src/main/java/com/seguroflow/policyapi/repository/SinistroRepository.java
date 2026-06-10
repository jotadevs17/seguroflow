package com.seguroflow.policyapi.repository;

import com.seguroflow.policyapi.domain.Sinistro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SinistroRepository extends JpaRepository<Sinistro, Long> {

    long countByApolice_Id(Long apoliceId);
}
