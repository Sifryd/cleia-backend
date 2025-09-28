package fr.cleia.sia.infrastructure.postgresql.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// FondsEntity.java
@Entity
@Table(name = "fonds")
@Getter @Setter
public class FondsEntity {
    @Id
    private String id;

    private String intitule;

    @OneToMany(mappedBy = "fonds", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DossierEntity> dossiers = new ArrayList<>();
}
