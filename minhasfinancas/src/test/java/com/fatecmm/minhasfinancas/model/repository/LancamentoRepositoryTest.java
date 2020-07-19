package com.fatecmm.minhasfinancas.model.repository;


import com.fatecmm.minhasfinancas.model.entity.Lancamento;
import model.enums.StatusLancamento;
import model.enums.TipoLancamento;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {
    @Autowired
    LancamentoRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void mustSaveARelease(){
        Lancamento lancamento = Lancamento.builder()
                                        .ano(2019)
                                        .mes(1)
                                        .descricao("qualquer lanc")
                                        .valor(BigDecimal.valueOf(10))
                                        .tipo(TipoLancamento.RECEITA)
                                        .status(StatusLancamento.PENDENTE)
                                        .dataCadastro(LocalDate.now())
                                        .build();

        lancamento = repository.save(lancamento);
        assertThat(lancamento.getId()).isNotNull();
    }

    @Test
    public void mustUpdateARelease(){
        Lancamento lancamento = criarEPersistirLancamento();

        lancamento.setAno(2018);
        lancamento.setDescricao("Teste atualizar");
        lancamento.setStatus(StatusLancamento.CANCELADO);
        repository.save(lancamento);

        Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class,lancamento.getId());

        assertThat(lancamentoAtualizado.getAno()).isEqualTo(2018);
        assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Teste atualizar");
        assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
    }

    @Test
    public void mustDeleteARelease(){
        Lancamento lancamento = criarEPersistirLancamento();

        lancamento =  entityManager.find(Lancamento.class,lancamento.getId());

        repository.delete(lancamento);

        Lancamento lancamentoInexistente = entityManager.find(Lancamento.class,lancamento.getId());
        assertThat(lancamentoInexistente).isNull();
    }

    @Test
    public void mustSearchAReleaseById(){
        Lancamento lancamento = criarEPersistirLancamento();

        Optional<Lancamento> lancamentoEncontrado =  repository.findById(lancamento.getId());
        assertThat(lancamentoEncontrado.isPresent()).isTrue();

    }

    public static Lancamento criarLancamento(){
        return Lancamento.builder()
                .ano(2019)
                .mes(1)
                .descricao("qualquer lanc")
                .valor(BigDecimal.valueOf(10))
                .tipo(TipoLancamento.RECEITA)
                .status(StatusLancamento.PENDENTE)
                .dataCadastro(LocalDate.now())
                .build();
    }

    private Lancamento criarEPersistirLancamento(){
        Lancamento lancamento = criarLancamento();
        entityManager.persist(lancamento);
        return lancamento;
    }
}
