package es.centro.dwes.incidencias.web;

import es.centro.dwes.incidencias.modelo.Estado;
import es.centro.dwes.incidencias.modelo.Incidencia;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;

@WebServlet("/incidencias/ver")
public class DetalleIncidenciaServlet extends ServletBase {

    private static final DateTimeFormatter FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        Long id = leerId(request.getParameter("id"));

        if (id == null) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "El identificador debe ser un entero positivo"
            );
            return;
        }

        Incidencia incidencia =
                repositorio().buscar(id).orElse(null);

        if (incidencia == null) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "No existe la incidencia solicitada"
            );
            return;
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String ctx = request.getContextPath();

        Html.cabecera(out, "Incidencia " + id, ctx);

        if ("1".equals(request.getParameter("creada"))) {
            out.println("""
                    <p class="mensaje exito">
                    Incidencia creada correctamente.
                    </p>
                    """);
        }

        if ("1".equals(request.getParameter("actualizada"))) {
            out.println("""
                    <p class="mensaje exito">
                    Estado actualizado correctamente.
                    </p>
                    """);
        }

        out.printf("""
                <article class="detalle">
                <h1>#%d · %s</h1>
                <dl>
                <dt>Solicitante</dt><dd>%s</dd>
                <dt>Correo</dt><dd>%s</dd>
                <dt>Categoría</dt><dd>%s</dd>
                <dt>Prioridad</dt><dd>%s</dd>
                <dt>Estado</dt><dd>%s</dd>
                <dt>Creación</dt><dd>%s</dd>
                </dl>
                <h2>Descripción</h2>
                <p class="texto-largo">%s</p>
                <h2>Etiquetas</h2>
                <ul>
                """,
                incidencia.getId(),
                Html.escapar(incidencia.getTitulo()),
                Html.escapar(incidencia.getNombre()),
                Html.escapar(incidencia.getEmail()),
                Html.escapar(incidencia.getCategoria()),
                incidencia.getPrioridad().getTexto(),
                incidencia.getEstado().getTexto(),
                incidencia.getFechaCreacion().format(FECHA),
                Html.escapar(incidencia.getDescripcion())
        );

        if (incidencia.getEtiquetas().isEmpty()) {
            out.println("<li>Sin etiquetas</li>");
        } else {
            incidencia.getEtiquetas().forEach(etiqueta ->
                    out.printf(
                            "<li>%s</li>%n",
                            Html.escapar(etiqueta)
                    )
            );
        }

        out.printf("""
                </ul>
                </article>
                <section>
                <h2>Cambiar el estado</h2>
                <form method="post" action="%s/incidencias/estado">
                <input type="hidden" name="id" value="%d">
                <label for="estado">Nuevo estado</label>
                <select id="estado" name="estado">
                """,
                ctx,
                incidencia.getId()
        );

        for (Estado estado : Estado.values()) {
            out.printf(
                    "<option value=\"%s\"%s>%s</option>%n",
                    estado.name(),
                    estado == incidencia.getEstado()
                            ? " selected"
                            : "",
                    estado.getTexto()
            );
        }

        out.println("""
                </select>
                <button type="submit">Guardar estado</button>
                </form>
                </section>
                """);

        Html.pie(out);
    }

    static Long leerId(String valor) {
        try {
            long id = Long.parseLong(valor);
            return id > 0 ? id : null;
        } catch (NumberFormatException | NullPointerException ex) {
            return null;
        }
    }
}
