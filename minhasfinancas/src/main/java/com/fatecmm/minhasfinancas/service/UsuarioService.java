package com.fatecmm.minhasfinancas.service;

import javax.security.sasl.AuthenticationException;

import com.fatecmm.minhasfinancas.exception.BusinessRuleException;
import com.fatecmm.minhasfinancas.model.entity.Usuario;
import org.springframework.stereotype.Service;

@Service
public interface UsuarioService {
	
	Usuario authenticate(String email, String senha) throws AuthenticationException;
	
	Usuario saveUser(Usuario usuario) throws BusinessRuleException;
	
	void validateEmail(String email) throws BusinessRuleException ; 
}
