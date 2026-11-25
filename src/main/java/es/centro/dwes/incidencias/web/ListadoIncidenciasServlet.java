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
import java.util.List;

@WebServlet("/incidencias")
public class ListadoIncidenciasServlet extends ServletBase {

    private static final DateTimeFormatter FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        String texto = request.getParameter("q");
        String estadoParametro = request.getParameter("estado");
        Estado estado = Estado.desde(estadoParametro);

        if (estadoParametro != null
                && !estadoParametro.isBlank()
                && estado == null) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Filtro de estado no válido"
            );
            return;
        }

        List<Incidencia> incidencias =
                repositorio().listar(texto, estado);

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String ctx = request.getContextPath();

        Html.cabecera(out, "Listado de incidencias", ctx);

        out.println("<h1>Incidencias registradas</h1>");

        // Ampliación B: los contadores se calculan en Java sobre todas
        // las incidencias del repositorio, sin tener en cuenta los filtros.
        List<Incidencia> todas = repositorio().listar(null, null);
        int abiertas = 0;
        int enProceso = 0;
        int resueltas = 0;
        for (Incidencia i : todas) {
            switch (i.getEstado()) {
                case ABIERTA -> abiertas++;
                case EN_PROCESO -> enProceso++;
                case RESUELTA -> resueltas++;
            }
        }
        out.printf("""
                <section class="contadores">
                <div><strong>%d</strong><span>Total</span></div>
                <div><strong>%d</strong><span>Abiertas</span></div>
                <div><strong>%d</strong><span>En proceso</span></div>
                <div><strong>%d</strong><span>Resueltas</span></div>
                </section>
                """,
                todas.size(),
                abiertas,
                enProceso,
                resueltas
        );

        if ("1".equals(request.getParameter("creada"))) {
            out.println("""
                    <p class="mensaje exito">
                    La incidencia se ha registrado correctamente.
                    </p>
                    """);
        }

        out.printf("""
                <form class="filtros"
                method="get"
                action="%s/incidencias">
                <label>
                Texto
                <input name="q"
                value="%s"
                placeholder="Título o solicitante">
                </label>
                <label>
                Estado
                <select name="estado">
                <option value="">Todos</option>
                """,
                ctx,
                Html.escapar(texto)
        );

        for (Estado opcion : Estado.values()) {
            String selected = opcion == estado
                    ? " selected"
                    : "";
            out.printf(
                    "<option value=\"%s\"%s>%s</option>%n",
                    opcion.name(),
                    selected,
                    opcion.getTexto()
            );
        }

        out.printf("""
                </select>
                </label>
                <button type="submit">Filtrar</button>
                <a class="boton secundario" href="%s/incidencias">
                Limpiar
                </a>
                </form>
                """,
                ctx
        );

        if (incidencias.isEmpty()) {
            out.println("""
                    <p class="mensaje">
                    No hay incidencias que coincidan con el filtro.
                    </p>
                    """);
        } else {
            out.println("""
                    <div class="tabla-responsive">
                    <table>
                    <thead>
                    <tr>
                    <th>ID</th>
                    <th>Título</th>
                    <th>Categoría</th>
                    <th>Prioridad</th>
                    <th>Estado</th>
                    <th>Fecha</th>
                    <th>Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    """);

            for (Incidencia i : incidencias) {
                out.printf("""
                        <tr>
                        <td>%d</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>
                        <a href="%s/incidencias/ver?id=%d">Ver</a>
                        </td>
                        </tr>
                        """,
                        i.getId(),
                        Html.escapar(i.getTitulo()),
                        Html.escapar(i.getCategoria()),
                        i.getPrioridad().getTexto(),
                        i.getEstado().getTexto(),
                        i.getFechaCreacion().format(FECHA),
                        ctx,
                        i.getId()
                );
            }

            out.println("</tbody></table></div>");
        }

        Html.pie(out);
    }
}
