package com.cleuperumes.CleuPerfumes.Model.Entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Lote {
    private int quantidade;
    private String validade;

    public Lote(){}

    public int getQuantidade() {return quantidade;}
    public void setQuantidade(int quantidade){this.quantidade = quantidade;}

    public String getValidade(){return validade;}
    public void setValidade(String validade){this.validade = validade;}
}