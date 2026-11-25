package es.centro.dwes.incidencias.modelo;

public enum Prioridad {
    BAJA("Baja"),
    MEDIA("Media"),
    ALTA("Alta");

    private final String texto;

    Prioridad(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    public static Prioridad desde(String valor) {
        if (valor == null) {
            return null;
        }
        try {
            return valueOf(valor);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
