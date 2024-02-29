package com.groupeisi.service;


import com.groupeisi.entities.Etudiant;

import java.util.List;

public interface EtudiantService {
    Etudiant creer(Etudiant etudiant);
    List<Etudiant> lire();
    Etudiant modifier(int id, Etudiant etudiant);
    String supprimer(int id);
}
