package sensores.track.backend.model.enums;

public enum PerfilUsuario {
    PARCIAL,
    COMPLETO;

    public String getDescricao() {
        return switch (this) {
            case PARCIAL -> "Parcial";
            case COMPLETO -> "Completo";
        };
    }
}
