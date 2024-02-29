package com.groupeisi.repository;

import com.groupeisi.entities.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Integer> {




    Etudiant findByTelephone(String telephone);
}
