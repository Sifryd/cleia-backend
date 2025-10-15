package fr.cleia.sia.domain.vo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DepthTest {
    @Test
    void refuse_negatif(){
        assertThatThrownBy(() -> new Depth(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void increment_ok(){
        var d = new Depth(0).increment();
        assertThat(d.value()).isEqualTo(1);
    }

    @Test
    void plus_ok(){
        var d = new Depth(0).plus(3);
        assertThat(d.value()).isEqualTo(3);
    }

    @Test
    void plus_refuse_negatif(){
        assertThatThrownBy(() -> new Depth(0).plus(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
