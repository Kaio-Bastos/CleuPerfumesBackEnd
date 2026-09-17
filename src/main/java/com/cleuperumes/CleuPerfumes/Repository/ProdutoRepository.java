package com.cleuperumes.CleuPerfumes.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cleuperumes.CleuPerfumes.Model.Entity.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    // Spring gera automaticamente o CRUD completo aqui (save, findAll, findById, delete)
}