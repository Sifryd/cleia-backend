package fr.cleia.sia.domain.ports;

import fr.cleia.sia.domain.description.models.Piece;
import fr.cleia.sia.domain.vo.NodeId;

import java.util.Optional;
import java.util.stream.Stream;

public interface DepotDePiece {
    Optional<Piece> findById(NodeId identifiant);
    void sauvegarderPiece(Piece piece);
    void supprimerPiece(NodeId identifiant);
    boolean existe(NodeId identifiant);
    Stream<Piece> enfantsDe(NodeId parentId);
}
