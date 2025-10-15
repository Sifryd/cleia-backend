package fr.cleia.sia.domain.vo;

import java.util.Objects;
import java.util.UUID;

public record NodeId (UUID value) {
    public NodeId {
        Objects.requireNonNull(value, "NodeId value must not be null");
    }
    public static NodeId newId(){
        return new NodeId(UUID.randomUUID());
    }
    @Override
    public String toString() {
        return value.toString();
    }
}
