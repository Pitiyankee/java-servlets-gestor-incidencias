package es.centro.dwes.incidencias.web;

import es.centro.dwes.incidencias.repositorio.IncidenciaRepositorio;
import jakarta.servlet.http.HttpServlet;

public abstract class ServletBase extends HttpServlet {

    protected IncidenciaRepositorio repositorio() {
        Object valor = getServletContext().getAttribute(
                IncidenciaRepositorio.CLAVE_CONTEXTO
        );
        if (valor instanceof IncidenciaRepositorio repositorio) {
            return repositorio;
        }
        throw new IllegalStateException(
                "El repositorio no está inicializado"
        );
    }
}
