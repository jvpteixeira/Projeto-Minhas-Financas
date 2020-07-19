package com.fatecmm.minhasfinancas.service;

import com.fatecmm.minhasfinancas.exception.BusinessRuleException;
import com.fatecmm.minhasfinancas.model.entity.Lancamento;
import com.fatecmm.minhasfinancas.model.entity.Usuario;
import com.fatecmm.minhasfinancas.model.repository.LancamentoRepository;
import com.fatecmm.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.fatecmm.minhasfinancas.service.impl.LancamentoServiceImpl;
import model.enums.StatusLancamento;
import model.enums.TipoLancamento;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class lancamentoServiceTest {

    @SpyBean
    LancamentoServiceImpl service;

    @MockBean
    LancamentoRepository repository;

    @Test
    public void mustSaveARelease() throws BusinessRuleException {
        //cenario
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        Mockito.doNothing().when(service).validar(lancamentoASalvar);

        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1l);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);

        //execucao
        Lancamento lancamento = service.salvar(lancamentoASalvar);

        //verificacao
        Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
        Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
    }

    @Test
    public void notMustSaveAReleaseWhenThereisValidationError() throws BusinessRuleException {
        //cenario
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        Mockito.doThrow(BusinessRuleException.class).when(service).validar(lancamentoASalvar);

        //execucao e verificação
        Assertions.catchThrowableOfType(() -> service.salvar(lancamentoASalvar), BusinessRuleException.class);
        Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
    }

    @Test
    public void mustUpdateARelease() throws BusinessRuleException {
        //cenario
        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1l);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.doNothing().when(service).validar(lancamentoSalvo);
        Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);

        //execucao
        service.atualizar(lancamentoSalvo);

        //verificacao
        Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
    }

    @Test
    public void mustThrowErrorWhenTryUpdateAUnsavedRelease() throws BusinessRuleException {
        //cenario
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();

        //execucao e verificação
        Assertions.catchThrowableOfType(() -> service.atualizar(lancamentoASalvar), NullPointerException.class);
        Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
    }

    @Test
    public void mustDeleteARelease() {
        //cenario
        Lancamento lancamentoADeletar = LancamentoRepositoryTest.criarLancamento();
        lancamentoADeletar.setId(1l);

        //execucao
        service.deletar(lancamentoADeletar);

        //verificacao
        Mockito.verify(repository).delete(lancamentoADeletar);

    }

    @Test
    public void mustThrowErrorWhenTryDeleteAnUnsavedRelease() {
        Lancamento lancamentoADeletar = LancamentoRepositoryTest.criarLancamento();

        //execucao
        Assertions.catchThrowableOfType(() -> service.deletar(lancamentoADeletar), NullPointerException.class);

        //verificacao
        Mockito.verify(repository, Mockito.never()).delete(lancamentoADeletar);
    }

    @Test
    public void mustFilterReleaser() {
        //cenário
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1l);

        List<Lancamento> lista = Arrays.asList(lancamento);
        Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);

        //execucao
        List<Lancamento> resultado = service.buscar(lancamento);

        //verificacoes
        Assertions.assertThat(resultado)
                .isNotEmpty()
                .hasSize(1)
                .contains(lancamento);
    }

    @Test
    public void mustUpdateTheStatusOfARelease() throws BusinessRuleException {
        //cenário
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1l);
        lancamento.setStatus(StatusLancamento.PENDENTE);
        StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
        Mockito.doReturn(lancamento).when(service).atualizar(lancamento);

        //execucao
        service.atualizarStatus(lancamento, novoStatus);

        //verificacoes
        Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
        Mockito.verify(service).atualizar(lancamento);

    }

    @Test
    public void mustReceiveAReleaseById() {
        //cenário
        Long id = 1l;
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));

        //execucao
        Optional<Lancamento> resultado = service.obterPorId(id);

        //verificacao
        Assertions.assertThat(resultado.isPresent()).isTrue();
    }

    @Test
    public void mustReturnEmptyWhenThereisntARelease() {
        //cenário
        Long id = 1l;
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        //execucao
        Optional<Lancamento> resultado = service.obterPorId(id);

        //verificacao
        Assertions.assertThat(resultado.isPresent()).isFalse();
    }

    @Test
    public void mustThrowErrorsWhenValidateRelease(){
        Lancamento lancamento = new Lancamento();

        Throwable erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Informs a valid description");
        lancamento.setDescricao("Salario");

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Informs a valid month");
        lancamento.setMes(1);

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Informs a valid year");
        lancamento.setAno(2020);

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Informs an user");
        lancamento.setUsuario(new Usuario(1l,"joao","joao@gmail.com","1234"));

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Informs a valid value");
        lancamento.setValor(BigDecimal.valueOf(2000));

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Enter a release type");
        lancamento.setTipo(TipoLancamento.RECEITA);

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isNull();

    }
}