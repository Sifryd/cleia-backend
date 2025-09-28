package fr.cleia.sia.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreerArborescenceCommandeDTO (
        @NotBlank String identifiantFond,
        @NotBlank String intituleFonds,
        @NotNull List<DossierDTO> dossiers
)
{
    public record DossierDTO(
            @NotBlank String identifiant,
            @NotBlank String intitule,
            @NotBlank String cote,
            @NotNull List<PieceDTO> pieces
            ){}

    public record PieceDTO(
            @NotBlank String identifiant,
            @NotBlank String intitule
    ){}
}
