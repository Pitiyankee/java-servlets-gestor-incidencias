package es.centro.dwes.incidencias.web;

import es.centro.dwes.incidencias.modelo.Incidencia;
import es.centro.dwes.incidencias.modelo.Prioridad;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/incidencias/crear")
public class CrearIncidenciaServlet extends ServletBase {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        FormularioDatos datos = FormularioDatos.desde(request);
        Map<String, String> errores = datos.validar();

        if (!errores.isEmpty()) {
            request.setAttribute("datosFormulario", datos);
            request.setAttribute("erroresFormulario", errores);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            RequestDispatcher dispatcher =
                    request.getRequestDispatcher(
                            "/incidencias/nueva"
                    );
            dispatcher.forward(request, response);
            return;
        }

        Incidencia incidencia = repositorio().crear(
                datos.titulo(),
                datos.nombre(),
                datos.email(),
                datos.categoria(),
                Prioridad.desde(datos.prioridad()),
                datos.descripcion(),
                datos.etiquetas()
        );

        String destino = request.getContextPath()
                + "/incidencias/ver?id="
                + incidencia.getId()
                + "&creada=1";

        response.sendRedirect(
                response.encodeRedirectURL(destino)
        );
    }
}
