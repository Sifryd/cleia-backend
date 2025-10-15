package fr.cleia.sia.domain.vo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TitleTest {
    @Test
    void trim_et_conserve_la_valeur(){
        var t = new Title("  Mon titre  ");
        assertThat(t.value().equals("Mon titre"));
    }

    @Test
    void refuse_blank(){
        assertThatThrownBy(()->new Title(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Title value must not be empty");
    }

    @Test
    void refuse_plus_de_255_caractere(){
        assertThatThrownBy(()->new Title("X".repeat(256)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Title value must not exceed 255 characters");
    }
}
