package com.cleuperumes.CleuPerfumes.Model.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "cliente", nullable = false)
    private String cliente;

    @Column(name = "data_compra", nullable = false)
    private LocalDateTime dataCompra;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;
    
    @Column(name = "parcelas", nullable = true)
    private BigDecimal parcelas;

    @Column(name = "status_entrega")
    private String statusEntrega = "Pendente";

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("pedido") // 👈 Impede o loop infinito nos itens
    private List<ItemPedido> itens = new ArrayList<>();

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("compra") // 👈 Impede o loop infinito nos pagamentos
    private List<Pagamento> pagamentos = new ArrayList<>();

    public Compra() {}

    @PrePersist
    public void prePersist() {
        this.dataCompra = LocalDateTime.now();
        if (this.statusEntrega == null) {
            this.statusEntrega = "Pendente";
        }
    }

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
        item.setPedido(this);
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public LocalDateTime getDataCompra() { return dataCompra; }
    public void setDataCompra(LocalDateTime dataCompra) { this.dataCompra = dataCompra; }

    public String getStatusEntrega() { return statusEntrega; }
    public void setStatusEntrega(String statusEntrega) { this.statusEntrega = statusEntrega; }

    public BigDecimal getParcelas() { return parcelas; }
    public void setParcelas(BigDecimal Parcelas) { this.parcelas = Parcelas; }

    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }

    public List<Pagamento> getPagamentos() { return pagamentos; }
    public void setPagamentos(List<Pagamento> pagamentos) { this.pagamentos = pagamentos; }
}