package es.centro.dwes.incidencias.repositorio;

import es.centro.dwes.incidencias.modelo.Estado;
import es.centro.dwes.incidencias.modelo.Incidencia;
import es.centro.dwes.incidencias.modelo.Prioridad;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class IncidenciaRepositorio {

    public static final String CLAVE_CONTEXTO = "repositorioIncidencias";

    private final ConcurrentMap<Long, Incidencia> datos =
            new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(0);

    public Incidencia crear(String titulo,
                            String nombre,
                            String email,
                            String categoria,
                            Prioridad prioridad,
                            String descripcion,
                            List<String> etiquetas) {
        long id = secuencia.incrementAndGet();
        Incidencia incidencia = new Incidencia(
                id,
                titulo,
                nombre,
                email,
                categoria,
                prioridad,
                descripcion,
                etiquetas
        );
        datos.put(id, incidencia);
        return incidencia;
    }

    public Optional<Incidencia> buscar(long id) {
        return Optional.ofNullable(datos.get(id));
    }

    public List<Incidencia> listar(String texto, Estado estado) {
        String filtro = texto == null
                ? ""
                : texto.trim().toLowerCase(Locale.ROOT);

        return datos.values().stream()
                .filter(i -> estado == null || i.getEstado() == estado)
                .filter(i -> filtro.isBlank()
                        || i.getTitulo().toLowerCase(Locale.ROOT).contains(filtro)
                        || i.getNombre().toLowerCase(Locale.ROOT).contains(filtro))
                .sorted(Comparator.comparingLong(Incidencia::getId).reversed())
                .toList();
    }

    public boolean actualizarEstado(long id, Estado estado) {
        Incidencia incidencia = datos.get(id);
        if (incidencia == null) {
            return false;
        }
        incidencia.setEstado(estado);
        return true;
    }
}
