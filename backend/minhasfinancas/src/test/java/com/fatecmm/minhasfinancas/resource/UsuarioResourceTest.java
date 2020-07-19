package com.fatecmm.minhasfinancas.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatecmm.minhasfinancas.api.dto.UsuarioDTO;
import com.fatecmm.minhasfinancas.exception.BusinessRuleException;
import com.fatecmm.minhasfinancas.model.entity.Usuario;
import com.fatecmm.minhasfinancas.service.LancamentoService;
import com.fatecmm.minhasfinancas.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.print.attribute.standard.Media;
import javax.security.sasl.AuthenticationException;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioResource.class) //subir contexto REST
@AutoConfigureMockMvc
public class UsuarioResourceTest {

    static final String API = "/api/usuarios";
    static final MediaType JSON = MediaType.APPLICATION_JSON;
    @Autowired
    MockMvc mvc; //mockar todas as chamadas

    @MockBean
    UsuarioService usuarioService;

    @MockBean
    LancamentoService lancamentoService;

    @Test
    public void mustAuthenticateAnUser() throws Exception {
        //cenario
        UsuarioDTO dto = UsuarioDTO.builder().email(EMAIL).senha(SENHA).build();
        Usuario usuario = Usuario.builder().id(1l).email(EMAIL).senha(SENHA).build();

        Mockito.when(usuarioService.authenticate(EMAIL, SENHA)).thenReturn(usuario);

        String json = new ObjectMapper().writeValueAsString(dto);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API.concat("/autenticar"))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));
    }

    @Test
    public void mustReturnBadRequestWhenReceiveAuthenticationError() throws Exception {
        //cenario
        UsuarioDTO dto = UsuarioDTO.builder().email(EMAIL).senha(SENHA).build();

        Mockito.when(usuarioService.authenticate(EMAIL, SENHA)).thenThrow(AuthenticationException.class);

        String json = new ObjectMapper().writeValueAsString(dto);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API.concat("/autenticar"))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void mustCreateANewUser() throws Exception {
        //cenario
        UsuarioDTO dto = UsuarioDTO.builder().email(EMAIL).senha(SENHA).build();
        Usuario usuario = Usuario.builder().id(1l).email(EMAIL).senha(SENHA).build();

        Mockito.when(usuarioService.saveUser(Mockito.any(Usuario.class))).thenReturn(usuario);

        String json = new ObjectMapper().writeValueAsString(dto);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API)
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));
    }

    @Test
    public void mustReturnBadRequestWhenTryCreateAnInvalidUser() throws Exception {
        //cenario
        UsuarioDTO dto = UsuarioDTO.builder().email(EMAIL).senha(SENHA).build();

        Mockito.when(usuarioService.saveUser(Mockito.any(Usuario.class))).thenThrow(BusinessRuleException.class);

        String json = new ObjectMapper().writeValueAsString(dto);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API)
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    String EMAIL = "joao@gmail.com";
    String SENHA = "123";
}
