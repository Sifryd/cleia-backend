package fr.cleia.sia.infrastructure.postgresql.mapper;

import fr.cleia.sia.infrastructure.postgresql.entities.DossierEntity;
import fr.cleia.sia.infrastructure.postgresql.entities.FondsEntity;
import fr.cleia.sia.infrastructure.postgresql.entities.PieceEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FondsMapper {

    // ===== Domaine -> JPA =====

    @Mapping(target = "id",        expression = "java(source.identifiant())")
    @Mapping(target = "intitule",  expression = "java(source.intitule())")
    @Mapping(target = "dossiers",  source = "dossiers")
    FondsEntity toEntity(fr.cleia.sia.domain.description.models.Fonds source);

    @Mapping(target = "id",        expression = "java(source.identifiant())")
    @Mapping(target = "intitule",  expression = "java(source.intitule())")
    @Mapping(target = "cote",      expression = "java(source.cote())")
    @Mapping(target = "pieces",    source = "pieces")
    DossierEntity toEntity(fr.cleia.sia.domain.description.models.Dossier source);

    @Mapping(target = "id",        expression = "java(source.identifiant())")
    @Mapping(target = "intitule",  expression = "java(source.intitule())")
    @Mapping(target = "dossier",   ignore = true) // on le fixe dans @AfterMapping
    PieceEntity toEntity(fr.cleia.sia.domain.description.models.Piece source);

    // Recâbler les back-references après mapping
    @AfterMapping
    default void wireParents(@MappingTarget FondsEntity target) {
        if (target.getDossiers() != null) {
            for (DossierEntity d : target.getDossiers()) {
                d.setFonds(target);
                if (d.getPieces() != null) {
                    for (PieceEntity p : d.getPieces()) {
                        p.setDossier(d);
                    }
                }
            }
        }
    }
}