package fr.cleia.sia.domain.vo;

public record Depth(int value) {
    public Depth {
        if(value < 0){
            throw new IllegalArgumentException("Depth must be positive");
        }
    }
    public Depth increment(){
        return new Depth(value + 1);
    }

    public Depth plus(int delta){
        int result = this.value + delta;
        if(result < 0){
            throw new IllegalArgumentException("Depth must be positive");
        }
        return new Depth(result);
    }

    public Depth minus(int delta){
        return plus(-delta);
    }
}
