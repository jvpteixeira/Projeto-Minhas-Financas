package com.fatecmm.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fatecmm.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
