package es.centro.dwes.incidencias.web;

import es.centro.dwes.incidencias.modelo.Prioridad;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public record FormularioDatos(
        String titulo,
        String nombre,
        String email,
        String categoria,
        String prioridad,
        String descripcion,
        List<String> etiquetas) {

    private static final Pattern EMAIL = Pattern.compile(
            "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"
    );

    private static final Set<String> CATEGORIAS = Set.of(
            "Hardware", "Software", "Red", "Cuenta", "Otro"
    );

    private static final Set<String> ETIQUETAS = Set.of(
            "Urgente", "Bloqueante", "Recurrente", "Equipo compartido"
    );

    public static FormularioDatos vacio() {
        return new FormularioDatos(
                "", "", "", "", "MEDIA", "", List.of()
        );
    }

    public static FormularioDatos desde(HttpServletRequest request) {
        String[] valores = request.getParameterValues("etiquetas");
        List<String> etiquetas = valores == null
                ? List.of()
                : Arrays.stream(valores)
                        .map(String::trim)
                        .toList();

        return new FormularioDatos(
                limpiar(request.getParameter("titulo")),
                limpiar(request.getParameter("nombre")),
                limpiar(request.getParameter("email")),
                limpiar(request.getParameter("categoria")),
                limpiar(request.getParameter("prioridad")),
                limpiar(request.getParameter("descripcion")),
                etiquetas
        );
    }

    public Map<String, String> validar() {
        Map<String, String> errores = new LinkedHashMap<>();

        if (titulo.length() < 5 || titulo.length() > 80) {
            errores.put(
                    "titulo",
                    "El título debe tener entre 5 y 80 caracteres."
            );
        }

        if (nombre.length() < 3 || nombre.length() > 60) {
            errores.put(
                    "nombre",
                    "El nombre debe tener entre 3 y 60 caracteres."
            );
        }

        if (!EMAIL.matcher(email).matches() || email.length() > 120) {
            errores.put(
                    "email",
                    "Introduce una dirección de correo válida de hasta 120 caracteres."
            );
        }

        if (!CATEGORIAS.contains(categoria)) {
            errores.put(
                    "categoria",
                    "Selecciona una categoría válida."
            );
        }

        if (Prioridad.desde(prioridad) == null) {
            errores.put(
                    "prioridad",
                    "Selecciona una prioridad válida."
            );
        }

        if (descripcion.length() < 20 || descripcion.length() > 600) {
            errores.put(
                    "descripcion",
                    "La descripción debe tener entre 20 y 600 caracteres."
            );
        }

        List<String> noPermitidas = new ArrayList<>(etiquetas);
        noPermitidas.removeAll(ETIQUETAS);
        if (!noPermitidas.isEmpty() || etiquetas.size() > 3) {
            errores.put(
                    "etiquetas",
                    "Selecciona como máximo tres etiquetas válidas."
            );
        }

        return errores;
    }

    public boolean tieneEtiqueta(String etiqueta) {
        return etiquetas.contains(etiqueta);
    }

    private static String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
