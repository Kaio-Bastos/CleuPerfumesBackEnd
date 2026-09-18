package com.cleuperumes.CleuPerfumes.Controller;

import com.cleuperumes.CleuPerfumes.Model.Entity.Compra;
import com.cleuperumes.CleuPerfumes.Model.Service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "*")
public class ComprasController {

    @Autowired
    private CompraService comService;

    @GetMapping
    public List<Compra> listar() {
        List<Compra> Compras = comService.listarTodos();
        return Compras;
    }

    @PostMapping
    public ResponseEntity<Compra> criar(@RequestBody Compra Compra) {
        Compra novoCompra = comService.criarCompra(Compra);
        return ResponseEntity.ok(novoCompra);
    }

    @PatchMapping("/{id}/status-entrega")
    public ResponseEntity<Compra> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String novoStatus = payload.get("statusEntrega");
        Compra CompraAtualizado = comService.atualizarStatusEntrega(id, novoStatus);
        return ResponseEntity.ok(CompraAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        comService.deletarCompra(id);
        return ResponseEntity.noContent().build();
    }
}