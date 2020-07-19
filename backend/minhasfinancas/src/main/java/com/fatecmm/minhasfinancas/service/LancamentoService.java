package com.fatecmm.minhasfinancas.service;

import com.fatecmm.minhasfinancas.exception.BusinessRuleException;
import com.fatecmm.minhasfinancas.model.entity.Lancamento;
import model.enums.StatusLancamento;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface LancamentoService {

    Lancamento salvar(Lancamento lancamento) throws BusinessRuleException;

    Lancamento atualizar(Lancamento lancamento) throws BusinessRuleException;

    void deletar(Lancamento lancamento);

    List<Lancamento> buscar(Lancamento lancamentoFiltro);

    void atualizarStatus(Lancamento lancamento, StatusLancamento status) throws BusinessRuleException;

    void validar(Lancamento lancamento) throws BusinessRuleException;

    Optional<Lancamento> obterPorId(Long id);

    BigDecimal obterSaldoPorUsuario(Long id);

}
