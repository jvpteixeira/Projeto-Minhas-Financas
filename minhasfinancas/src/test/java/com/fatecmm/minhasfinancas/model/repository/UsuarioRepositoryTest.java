package com.fatecmm.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fatecmm.minhasfinancas.model.entity.Usuario;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager; //conceito Spring Data JPA
	
	@Test
	public void mustVerifyTheExistenceOfAnEmail() {
		//cenário
		entityManager.persist(givenWeHaveAnUser());
		
		//ação
		boolean result = repository.existsByEmail("usuario@email.com");
		
		
		//verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void mustReturnFalseWhenThereIsNotAUserRecordedWithEmail() {
		//cenário
		
		//ação
		boolean result = repository.existsByEmail("usuarioDiferente@email.com");
		
		
		//verificação
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void mustPersistAnUser() {
		//cenário
		Usuario user = givenWeHaveAnUser();
		
		//ação
		Usuario usuarioSalvo = repository.save(user);
		
		//verificação
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
		
	}
	
	@Test
	public void mustFindUserByEmail() {
		//cenário
		givenWeHaveAnRecordedUser();
				
		//when
		Optional<Usuario> user = whenFindByEmail();
		
		//then
		Assertions.assertThat(user.isPresent()).isTrue();
	}
	
	@Test
	public void mustReturnAnEmptyUserWhenNotFound() {
		//cenário
				
		//when
		Optional<Usuario> user = whenFindByEmail();
		
		//then
		Assertions.assertThat(user.isPresent()).isFalse();
	}
	
	
	
	private Optional<Usuario> whenFindByEmail() {
		return repository.findByEmail(EMAIL);
	}

	private void givenWeHaveAnRecordedUser() {
		entityManager.persist(USER);		
	}

	private Usuario givenWeHaveAnUser() {
		return USER;
	}

	//constants
	String EMAIL = "usuario@email.com";
	Usuario USER = Usuario.builder().nome("usuario").email(EMAIL).build();
}
