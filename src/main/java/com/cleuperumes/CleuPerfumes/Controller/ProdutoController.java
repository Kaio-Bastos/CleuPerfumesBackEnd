package com.cleuperumes.CleuPerfumes.Controller;

import com.cleuperumes.CleuPerfumes.Model.Entity.Produto;
import com.cleuperumes.CleuPerfumes.Repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*") // Permite requisições do seu front React
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    @PostMapping
    public Produto salvar(@RequestBody Produto produto) {
        return produtoRepository.save(produto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return produtoRepository.findById(id)
                .map(produto -> {
                    produtoRepository.delete(produto);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto produtoDetalhes) {
        return produtoRepository.findById(id)
                .map(produto -> {
                    produto.setNome(produtoDetalhes.getNome());
                    produto.setCodigoBarras(produtoDetalhes.getCodigoBarras());
                    produto.setFotoUrl(produtoDetalhes.getFotoUrl());
                    produto.setValorLiquido(produtoDetalhes.getValorLiquido());

                    produto.setLotes(produtoDetalhes.getLotes());
                    
                    Produto atualizado = produtoRepository.save(produto);
                    return ResponseEntity.ok(atualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
