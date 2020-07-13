package com.fatecmm.minhasfinancas.service;

import com.fatecmm.minhasfinancas.model.entity.Usuario;
import net.bytebuddy.implementation.bytecode.Throw;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.fatecmm.minhasfinancas.exception.BusinessRuleException;
import com.fatecmm.minhasfinancas.model.repository.UsuarioRepository;
import com.fatecmm.minhasfinancas.service.impl.UsuarioServiceImpl;

import javax.security.sasl.AuthenticationException;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE) //Para não sobrescrever as config de teste
public class UsuarioServiceTest {

	@SpyBean
	UsuarioServiceImpl service;

	@MockBean //vai criar uma instância mokada do repository
	UsuarioRepository repository;


	@Test(expected = Test.None.class)
	public void mustSaveAnUser() throws BusinessRuleException {
		//cenario
		Mockito.doNothing().when(service).validateEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
				.id(1l)
				.nome("nome")
				.email("email@gmail.com")
				.senha("senha")
				.build();

		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

		//acao
		Usuario usuariosalvo = service.saveUser(new Usuario());

		//verificacao
		Assertions.assertThat(usuariosalvo).isNotNull();
		Assertions.assertThat(usuariosalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuariosalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuariosalvo.getEmail()).isEqualTo("email@gmail.com");
		Assertions.assertThat(usuariosalvo.getSenha()).isEqualTo("senha");
	}

	@Test(expected = BusinessRuleException.class)
	public void notMustSaveAnUserWithRegisteredEmail() throws BusinessRuleException {
		//cenario
		String email = "email@gmail.com";
		Usuario usuario = Usuario.builder()
				.email(email)
				.build();

		Mockito.doThrow(BusinessRuleException.class).when(service).validateEmail(email);

		//ação
		service.saveUser(usuario);

		//verificao
		Mockito.verify(repository, Mockito.never()).save(usuario);

	}


	@Test(expected = Test.None.class)
	public void mustAuthenticateAnUserWithSuccess() throws BusinessRuleException, AuthenticationException {
		//cenário
		Usuario usuario = USER;
		Mockito.when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));

		//acao
		Usuario result = service.authenticate(EMAIL,SENHA);

		//verificacao
		Assertions.assertThat(result).isNotNull();
	}

	@Test()
	public void mustThrowErrorWhenNotFoundAnRegisteredUserWithInformedEmail() throws AuthenticationException {
		//cenario
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

		//acao
		Throwable exception = Assertions.catchThrowable(() -> service.authenticate("email@gmail.com", "senha"));
		Assertions.assertThat(exception)
				.isInstanceOf(AuthenticationException.class)
				.hasMessage("User not found");
	}
	@Test()
	public void mustThrowErrorWhenIncorrectPassword() throws AuthenticationException {
		//cenario
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

		//acao
		Throwable exception = Assertions.catchThrowable(() -> service.authenticate("email@gmail.com","senha123"));
		Assertions.assertThat(exception)
				.isInstanceOf(AuthenticationException.class)
				.hasMessage("Invalid Password");
	}
	@Test(expected = Test.None.class)
	public void mustValidateEmail() throws BusinessRuleException {
		//cenário
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//acao
		service.validateEmail("email@gmail.com");
	}
	
	@Test(expected = BusinessRuleException.class)
	public void mustReturnAErrorWhenThereIsARecordedEmail() throws BusinessRuleException {
		//cenário
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

		//acao
		service.validateEmail("email@gmail.com");
		
	}	

	//Constants
	String EMAIL = "email@email.com";
	String SENHA = "senha";
	Usuario USER = Usuario.builder().email(EMAIL).senha(SENHA).id(1l).build();
}
