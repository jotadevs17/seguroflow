package com.seguroflow.policyapi.repository;

import com.seguroflow.policyapi.domain.Apolice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApoliceRepository extends JpaRepository<Apolice, Long> {

    boolean existsByNumero(String numero);
}
