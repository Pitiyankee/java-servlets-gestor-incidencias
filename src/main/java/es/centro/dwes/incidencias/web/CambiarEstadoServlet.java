package es.centro.dwes.incidencias.web;

import es.centro.dwes.incidencias.modelo.Estado;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/incidencias/estado")
public class CambiarEstadoServlet extends ServletBase {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        Long id = DetalleIncidenciaServlet.leerId(
                request.getParameter("id")
        );
        Estado estado = Estado.desde(
                request.getParameter("estado")
        );

        if (id == null || estado == null) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Identificador o estado no válido"
            );
            return;
        }

        if (!repositorio().actualizarEstado(id, estado)) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "No existe la incidencia solicitada"
            );
            return;
        }

        String destino = request.getContextPath()
                + "/incidencias/ver?id="
                + id
                + "&actualizada=1";

        response.sendRedirect(
                response.encodeRedirectURL(destino)
        );
    }
}
