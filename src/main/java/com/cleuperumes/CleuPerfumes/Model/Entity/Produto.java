package com.cleuperumes.CleuPerfumes.Model.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Comparator;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_barras", unique = true, length = 50)
    private String codigoBarras;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(name = "foto_url", columnDefinition = "VARCHAR")
    private String fotoUrl;

    @Column(name = "valor_liquido", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorLiquido;

    @Convert(converter = LoteConverter.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "lotes", columnDefinition = "jsonb", nullable = false)
    private List<Lote> lotes;

    // Construtor Vazio (Exigido pelo JPA)
    public Produto() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public BigDecimal getValorLiquido() { return valorLiquido; }
    public void setValorLiquido(BigDecimal valorLiquido) { this.valorLiquido = valorLiquido; }

    public List<Lote> getLotes() { return lotes;}
    public void setLotes(List<Lote> lotes) { this.lotes = lotes; }

    public int getQuantidade(){
    if (lotes == null) return 0;
    int total = 0;
    for(Lote lote : lotes){ 
        total += lote.getQuantidade(); 
    }    
    return total;
}

    public void darBaixaEstoque(int quantidadeVendida) {
    if (lotes == null || lotes.isEmpty()) {
        throw new RuntimeException("Não há lotes disponíveis para baixa.");
    }

    // 1. Ordena os lotes por data de validade (da mais próxima para a mais distante)
    lotes.sort(Comparator.comparing(Lote::getValidade));

    // 2. Percorre os lotes descontando a quantidade vendida
    for (int i = 0; i < lotes.size(); i++) {
        if (quantidadeVendida <= 0) break; // Se já abateu tudo, encerra

        Lote loteAtual = lotes.get(i);

        if (loteAtual.getQuantidade() <= quantidadeVendida) {
            // Se o lote tem menos ou igual à quantidade vendida, consumimos ele todo
            quantidadeVendida -= loteAtual.getQuantidade();
            lotes.remove(i); // Remove o lote esgotado da lista
            i--; // Recua o index por causa da remoção
        } else {
            // Se o lote tem mais do que o necessário, apenas subtraímos o restante
            loteAtual.setQuantidade(loteAtual.getQuantidade() - quantidadeVendida);
            quantidadeVendida = 0;
        }
    }

    if (quantidadeVendida > 0) {
        throw new RuntimeException("Estoque insuficiente para atender a quantidade desejada.");
    }
}
}