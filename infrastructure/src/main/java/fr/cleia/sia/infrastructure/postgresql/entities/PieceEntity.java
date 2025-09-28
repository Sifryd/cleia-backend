package fr.cleia.sia.infrastructure.postgresql.entities;

import jakarta.persistence.*;
import lombok.*;

// PieceEntity.java
@Entity
@Table(name = "piece")
@Getter @Setter
public class PieceEntity {
    @Id
    private String id;

    private String intitule;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dossier_id", nullable = false)
    private DossierEntity dossier;
}