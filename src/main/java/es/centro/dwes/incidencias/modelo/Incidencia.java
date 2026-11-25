package es.centro.dwes.incidencias.modelo;

import java.time.LocalDateTime;
import java.util.List;

public class Incidencia {

    private final long id;
    private final String titulo;
    private final String nombre;
    private final String email;
    private final String categoria;
    private final Prioridad prioridad;
    private final String descripcion;
    private final List<String> etiquetas;
    private final LocalDateTime fechaCreacion;
    private volatile Estado estado;

    public Incidencia(long id,
                      String titulo,
                      String nombre,
                      String email,
                      String categoria,
                      Prioridad prioridad,
                      String descripcion,
                      List<String> etiquetas) {
        this.id = id;
        this.titulo = titulo;
        this.nombre = nombre;
        this.email = email;
        this.categoria = categoria;
        this.prioridad = prioridad;
        this.descripcion = descripcion;
        this.etiquetas = List.copyOf(etiquetas);
        this.fechaCreacion = LocalDateTime.now();
        this.estado = Estado.ABIERTA;
    }

    public long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getCategoria() {
        return categoria;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
