package fr.cleia.sia.domain.vo;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

public class ValueObjectsTest {
    @Test
    void title_trim_et_non_vide() {
        var t = new Title("  Hello  ");
        assertThat(t.value()).isEqualTo("Hello");
    }

    @Test
    void title_vide_interdit() {
        assertThatThrownBy(() -> new Title("   "))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Title(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void title_longueur_max_255() {
        var s255 = "a".repeat(255);
        var t = new Title(s255);
        assertThat(t.value()).isEqualTo(s255);

        var s256 = "a".repeat(256);
        assertThatThrownBy(() -> new Title(s256))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void depth_min_zero() {
        var d0 = new Depth(0);
        assertThat(d0.value()).isEqualTo(0);
        assertThatThrownBy(() -> new Depth(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void depth_increment_plus_minus() {
        var d1 = new Depth(1);
        assertThat(d1.increment().value()).isEqualTo(2);
        assertThat(d1.plus(3).value()).isEqualTo(4);
        assertThat(d1.minus(1).value()).isEqualTo(0);
        assertThatThrownBy(() -> d1.minus(2)) // 1 - 2 = -1 interdit
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nodeId_newId_non_null() {
        var id = NodeId.newId();
        assertThat(id).isNotNull();
        assertThat(id.value()).isNotNull();
        assertThatCode(() -> UUID.fromString(id.toString())).doesNotThrowAnyException();
    }

    @Test
    void nodeId_ne_doit_pas_accepter_null() {
        assertThatThrownBy(() -> new NodeId(null))
                .isInstanceOf(NullPointerException.class);
    }
}
