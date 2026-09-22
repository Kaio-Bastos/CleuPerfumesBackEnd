package com.cleuperumes.CleuPerfumes.Model.Entity;

import java.time.LocalDate;
import jakarta.persistence.Embeddable;

@Embeddable
public class Lote {
    private int quantidade;
    private LocalDate validade;

    public Lote(){}

    public int getQuantidade() {return quantidade;}
    public void setQuantidade(int quantidade){this.quantidade = quantidade;}

    public LocalDate getValidade(){return validade;}
    public void setValidade(LocalDate validade){this.validade = validade;}
}