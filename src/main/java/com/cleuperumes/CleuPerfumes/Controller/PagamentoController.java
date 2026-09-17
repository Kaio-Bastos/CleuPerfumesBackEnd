package com.cleuperumes.CleuPerfumes.Controller;

import com.cleuperumes.CleuPerfumes.Model.Entity.Pagamento;
import com.cleuperumes.CleuPerfumes.Model.Service.PagamentoService;
import com.cleuperumes.CleuPerfumes.Repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagamentos")
@CrossOrigin(origins = "*")
public class PagamentoController {

    @Autowired
    public PagamentoService pagamentoService;

    @Autowired 
    public PagamentoRepository pagamentoRepository;
    
    @GetMapping
    public ResponseEntity<List<Pagamento>> listar() {
        List<Pagamento> Pagamentos = pagamentoService.listarTodos();
        return ResponseEntity.ok(Pagamentos);
    }

    
    @PostMapping
    public ResponseEntity<Pagamento> criar(@RequestBody Pagamento Pagamento) {
        Pagamento novoPagamento = pagamentoService.criarPagamento(Pagamento);
        return ResponseEntity.ok(novoPagamento);
    }

    // 👈 Endpoint para dar baixa no pagamento da parcela (mudar status para 'Pago')
    @PatchMapping("/{id}/dar-baixa")
    public ResponseEntity<Pagamento> darBaixaPagamento(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        Pagamento pagamento = pagamentoService.buscarPorId(id);
        pagamento.setStatus("Pago");
        pagamento.setDataPagamento(LocalDateTime.now());
        Pagamento atualizado = pagamentoService.criarPagamento(pagamento);
        return ResponseEntity.ok(atualizado);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Pagamento> atualizarStatusPagamento(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        Pagamento pagamento = pagamentoService.buscarPorId(id);
        
        String novoStatus = payload.get("status");
        if (novoStatus != null) {
            pagamento.setStatus(novoStatus);
        }
        
        Pagamento atualizado = pagamentoService.salvar(pagamento);
        return ResponseEntity.ok(atualizado);
    }
}
