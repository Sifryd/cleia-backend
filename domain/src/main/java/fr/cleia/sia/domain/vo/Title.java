package fr.cleia.sia.domain.vo;

import java.util.Objects;

public record Title (String value){
    public Title {
        Objects.requireNonNull(value, "Title value must not be null");
        var t = value.strip();
        if(t.isEmpty()){
            throw new IllegalArgumentException("Title value must not be empty");
        }
        if (t.length() > 255) {
            throw new IllegalArgumentException("Title value must not exceed 255 characters");
        }
        value = t;
    }

    public static Title of(String raw){
        return new Title(raw);
    }

    @Override
    public String toString() {
        return value;
    }
}
