package com.fatecmm.minhasfinancas.resource;

import com.fatecmm.minhasfinancas.api.dto.AtualizaStatusDTO;
import com.fatecmm.minhasfinancas.api.dto.LancamentoDTO;
import com.fatecmm.minhasfinancas.exception.BusinessRuleException;
import com.fatecmm.minhasfinancas.model.entity.Lancamento;
import com.fatecmm.minhasfinancas.model.entity.Usuario;
import com.fatecmm.minhasfinancas.service.LancamentoService;
import com.fatecmm.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import model.enums.StatusLancamento;
import model.enums.TipoLancamento;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoResource {
    private final LancamentoService service;
    private  final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity buscar(
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "mes", required = false) Integer mes,
            @RequestParam(value = "ano", required = false) Integer ano,
            @RequestParam("usuario") Long idUsuario
        ) {
        Lancamento lancamentoFiltro = new Lancamento();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setMes(mes);
        lancamentoFiltro.setAno(ano);

        Optional<Usuario> usuario =  usuarioService.obterPorId(idUsuario);
        if(!usuario.isPresent()){
            return ResponseEntity.badRequest().body("User not found for informed id");
        } else {
            lancamentoFiltro.setUsuario(usuario.get());
        }
        List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
        return ResponseEntity.ok(lancamentos);
    }

    @PostMapping
    public ResponseEntity save(@RequestBody LancamentoDTO dto){
        try{
            Lancamento entidade = converter(dto);
            entidade = service.salvar(entidade);
            return ResponseEntity.status(HttpStatus.CREATED).body(entidade);
        } catch(BusinessRuleException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto){
        service.obterPorId(id).map(entity -> {
            try{
                Lancamento lancamento = converter(dto);
                lancamento.setId(entity.getId());
                service.atualizar(lancamento);
                return ResponseEntity.ok(lancamento);
            } catch(BusinessRuleException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() ->
                new ResponseEntity("Release not found in database", HttpStatus.BAD_REQUEST));
        return null;
    }

    @PutMapping("{id}/status")
    public ResponseEntity atualizarStatus(@PathVariable("id") Long id,@RequestBody AtualizaStatusDTO dto){
            return service.obterPorId(id).map(entity -> {
                StatusLancamento statusSelecionado =  StatusLancamento.valueOf(dto.getStatus());
                if(statusSelecionado == null){
                    return ResponseEntity.badRequest().body("Could not update status, send a valid status");
                }
                try {
                    entity.setStatus(statusSelecionado);
                    service.atualizar(entity);
                    return ResponseEntity.ok(entity);
                } catch (BusinessRuleException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            }).orElseGet(() -> new ResponseEntity("Release not found in database", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id){
        service.obterPorId(id).map(entity -> {
            try{
                service.deletar(entity);
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            } catch(Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() ->
                new ResponseEntity("Release not found in database", HttpStatus.BAD_REQUEST));
        return null;
    }

    private Lancamento converter(LancamentoDTO dto) throws BusinessRuleException {
        Lancamento lancamento = new Lancamento();
        lancamento.setId(dto.getId());
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setAno(dto.getAno());
        lancamento.setMes(dto.getMes());
        lancamento.setValor(dto.getValor());
        Usuario usuario = usuarioService
                .obterPorId(dto.getUsuario())
                .orElseThrow(() -> new BusinessRuleException("User not found for informed id"));
        lancamento.setUsuario(usuario);

        if(dto.getTipo() != null){
            lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
        }

        if(dto.getStatus() != null){
            lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
        }

        return lancamento;
    }
}
