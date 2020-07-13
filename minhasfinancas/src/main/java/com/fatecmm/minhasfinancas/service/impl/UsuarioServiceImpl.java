package com.fatecmm.minhasfinancas.service.impl;

import java.util.Optional;

import javax.security.sasl.AuthenticationException;

import org.springframework.stereotype.Service;

import com.fatecmm.minhasfinancas.exception.BusinessRuleException;
import com.fatecmm.minhasfinancas.model.entity.Usuario;
import com.fatecmm.minhasfinancas.model.repository.UsuarioRepository;
import com.fatecmm.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	
	private UsuarioRepository repository;
	
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario authenticate(String email, String senha) throws AuthenticationException {
		Optional<Usuario> user = repository.findByEmail(email);
		if(!user.isPresent()) {
			throw new AuthenticationException("User not found");
		}
		
		if(!user.get().getSenha().equals(senha)) {
			throw new AuthenticationException("Invalid Password");
		}
		
		return user.get();
	}

	@Override
	public Usuario saveUser(Usuario usuario) throws BusinessRuleException {
		validateEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validateEmail(String email) throws BusinessRuleException {	
		boolean exist = repository.existsByEmail(email);
		if(exist) {
			throw new BusinessRuleException("There's a record user with this email");
		}
	}
	
}
