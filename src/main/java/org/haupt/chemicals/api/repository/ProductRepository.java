package org.haupt.chemicals.api.repository;

import org.haupt.chemicals.api.model.Diagnose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Diagnose, Long> {
    @Query("SELECT p FROM Diagnose p WHERE p.titel LIke %?1%")
    public List<Diagnose> findByTitel(String titel);

    @Query("SELECT p FROM Diagnose p WHERE p.id = ?1")
    public Diagnose findById(long titel);
}
