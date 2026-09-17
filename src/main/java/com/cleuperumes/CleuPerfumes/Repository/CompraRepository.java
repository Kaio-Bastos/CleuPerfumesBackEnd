package com.cleuperumes.CleuPerfumes.Repository;

import com.cleuperumes.CleuPerfumes.Model.Entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
}