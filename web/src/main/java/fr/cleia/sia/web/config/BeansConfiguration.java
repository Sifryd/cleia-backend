package fr.cleia.sia.web.config;

import fr.cleia.sia.domain.ports.DepotDeFonds;
import fr.cleia.sia.application.usecase.ConsulterArborescence;
import fr.cleia.sia.application.usecase.ConsulterArborescenceService;
import fr.cleia.sia.application.usecase.CreerArborescence;
import fr.cleia.sia.application.usecase.CreerArborescenceService;
import fr.cleia.sia.domain.description.rules.PolitiqueDeDescription;
import fr.cleia.sia.infrastructure.memory.DepotDeFondsMemoire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfiguration
{
    @Bean
    PolitiqueDeDescription politiqueDeDescription() {
        return new PolitiqueDeDescription();
    }

    @Bean
    DepotDeFonds depotDeFonds() {
        return new  DepotDeFondsMemoire();
    }

    @Bean
    CreerArborescence creerArborescence (DepotDeFonds depotDeFonds, PolitiqueDeDescription politique) {
        return new CreerArborescenceService(depotDeFonds, politique);
    }

    @Bean
    ConsulterArborescence consulterArborescence (DepotDeFonds depotDeFonds) {
        return new ConsulterArborescenceService(depotDeFonds);
    }
}
