package fr.cleia.sia.infrastructure.postgresql.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dossier")
@Getter @Setter
public class DossierEntity {
    @Id
    private String id;

    private String intitule;

    @Column(nullable = false)
    private String cote;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fonds_id", nullable = false)
    private FondsEntity fonds;

    @OneToMany(mappedBy = "dossier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PieceEntity> pieces = new ArrayList<>();
}
