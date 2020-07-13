package com.fatecmm.minhasfinancas.resource;

import com.fatecmm.minhasfinancas.api.dto.UsuarioDTO;
import com.fatecmm.minhasfinancas.exception.BusinessRuleException;
import com.fatecmm.minhasfinancas.model.entity.Usuario;
import com.fatecmm.minhasfinancas.service.UsuarioService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {
    private UsuarioService service;

    public UsuarioResource(UsuarioService service){
        this.service = service;
    }

    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody UsuarioDTO dto){
        try{
            Usuario usuarioAutenticado = service.authenticate(dto.getEmail(),dto.getSenha());
            return ResponseEntity.ok(usuarioAutenticado);
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity save(@RequestBody  UsuarioDTO dto){
        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha()).build();
        try{
            Usuario usuarioSalvo = service.saveUser(usuario);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        }catch(BusinessRuleException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
