package es.centro.dwes.incidencias.modelo;

public enum Estado {
    ABIERTA("Abierta"),
    EN_PROCESO("En proceso"),
    RESUELTA("Resuelta");

    private final String texto;

    Estado(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    public static Estado desde(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return valueOf(valor);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
