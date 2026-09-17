package com.cleuperumes.CleuPerfumes.Model.Service;

import com.cleuperumes.CleuPerfumes.Model.Entity.ItemPedido;
import com.cleuperumes.CleuPerfumes.Model.Entity.Compra;
import com.cleuperumes.CleuPerfumes.Model.Entity.Produto;
import com.cleuperumes.CleuPerfumes.Repository.CompraRepository;
import com.cleuperumes.CleuPerfumes.Repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

@Service
public class CompraService {

    @Autowired
    private CompraRepository CompraRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Compra> listarTodos() {
        return CompraRepository.findAll();
    }

    @Transactional
    public Compra criarCompra(Compra CompraRecebido) {
        // Valida se o cliente veio preenchido e existe no banco
        if (CompraRecebido.getCliente() == null || CompraRecebido.getCliente() == null) {
            throw new RuntimeException("Nome do cliente é obrigatório");
        }

        CompraRecebido.setCliente(CompraRecebido.getCliente());

        // Processa e abate o estoque de cada item
        List<ItemPedido> itensTemporarios = CompraRecebido.getItens();
        CompraRecebido.setItens(new ArrayList<>()); // Limpa para preencher com o método helper

        for (ItemPedido item : itensTemporarios) {
            Produto produto = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            if (produto.getQuantidade() < item.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            produto.setQuantidade(produto.getQuantidade() - item.getQuantidade());
            produtoRepository.save(produto);

            ItemPedido novoItem = new ItemPedido(
                    CompraRecebido,
                    produto,
                    item.getQuantidade(),
                    produto.getValorLiquido());
            CompraRecebido.adicionarItem(novoItem);
        }

        return CompraRepository.save(CompraRecebido);
    }

    @Transactional
    public Compra atualizarStatusEntrega(Long id, String novoStatus) {
        Compra Compra = CompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra não encontrado"));

        Compra.setStatusEntrega(novoStatus);
        return CompraRepository.save(Compra);
    }

    
}