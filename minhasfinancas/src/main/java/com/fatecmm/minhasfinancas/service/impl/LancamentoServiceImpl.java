package com.fatecmm.minhasfinancas.service.impl;

import com.fatecmm.minhasfinancas.exception.BusinessRuleException;
import com.fatecmm.minhasfinancas.model.entity.Lancamento;
import com.fatecmm.minhasfinancas.model.repository.LancamentoRepository;
import com.fatecmm.minhasfinancas.service.LancamentoService;
import model.enums.StatusLancamento;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class LancamentoServiceImpl implements LancamentoService {


    private LancamentoRepository repository;

    public LancamentoServiceImpl(LancamentoRepository repository){
        this.repository = repository;
    }

    @Override
    @Transactional
    public Lancamento salvar(Lancamento lancamento) throws BusinessRuleException {
        validar(lancamento);
        return repository.save(lancamento);
    }

    @Override
    @Transactional
    public Lancamento atualizar(Lancamento lancamento) throws BusinessRuleException {
        Objects.requireNonNull(lancamento.getId());
        validar(lancamento);
        return repository.save(lancamento);
    }

    @Override
    @Transactional
    public void deletar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        repository.delete(lancamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
        Example example = Example.of(lancamentoFiltro, ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return repository.findAll(example);
    }

    @Override
    public void atualizarStatus(Lancamento lancamento, StatusLancamento status) throws BusinessRuleException {
        lancamento.setStatus(status);
        lancamento.setStatus(StatusLancamento.PENDENTE);
        atualizar(lancamento);
    }

    @Override
    public void validar(Lancamento lancamento) throws BusinessRuleException {
        if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")){
            throw new BusinessRuleException("Informs a valid description");
        }

        if(lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12){
            throw new BusinessRuleException("Informs a valid month");
        }

        if(lancamento.getAno() == null || lancamento.getAno().toString().length() != 4){
            throw new BusinessRuleException("Informs a valid year");
        }

        if(lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null){
            throw new BusinessRuleException("Informs an user");
        }

        if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1){
            throw new BusinessRuleException("Informs a valid value");
        }

        if(lancamento.getTipo() == null){
            throw new BusinessRuleException("Enter a release type");
        }
    }
}
