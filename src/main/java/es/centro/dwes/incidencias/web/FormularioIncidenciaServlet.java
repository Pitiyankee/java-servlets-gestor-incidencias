package es.centro.dwes.incidencias.web;

import es.centro.dwes.incidencias.modelo.Prioridad;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/incidencias/nueva")
public class FormularioIncidenciaServlet extends ServletBase {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        mostrar(
                request,
                response,
                FormularioDatos.vacio(),
                Map.of()
        );
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        Object datos = request.getAttribute("datosFormulario");
        Object errores = request.getAttribute("erroresFormulario");

        if (!(datos instanceof FormularioDatos formulario)
                || !(errores instanceof Map<?, ?> mapa)) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "No se han recibido datos de validación"
            );
            return;
        }

        @SuppressWarnings("unchecked")
        Map<String, String> erroresTipados =
                (Map<String, String>) mapa;

        mostrar(request, response, formulario, erroresTipados);
    }

    private void mostrar(HttpServletRequest request,
                         HttpServletResponse response,
                         FormularioDatos datos,
                         Map<String, String> errores)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String ctx = request.getContextPath();

        Html.cabecera(out, "Nueva incidencia", ctx);

        out.println("""
                <h1>Registrar una incidencia</h1>
                <p>Los campos marcados con * son obligatorios.</p>
                """);

        if (!errores.isEmpty()) {
            out.println("""
                    <div class="mensaje error" role="alert">
                    <strong>Revisa el formulario:</strong>
                    <ul>
                    """);
            errores.values().forEach(error ->
                    out.printf(
                            "<li>%s</li>%n",
                            Html.escapar(error)
                    )
            );
            out.println("</ul></div>");
        }

        out.printf(
                "<form class=\"formulario\" method=\"post\" "
                        + "action=\"%s/incidencias/crear\">%n",
                ctx
        );

        campo(out, "titulo", "Título *", datos.titulo(),
                errores, "text", "5", "80");
        campo(out, "nombre", "Nombre y apellidos *", datos.nombre(),
                errores, "text", "3", "60");
        campo(out, "email", "Correo electrónico *", datos.email(),
                errores, "email", "5", "120");

        out.println("""
                <label for="categoria">Categoría *</label>
                <select id="categoria" name="categoria" required>
                <option value="">Selecciona...</option>
                """);
        for (String categoria : new String[]{
                "Hardware", "Software", "Red", "Cuenta", "Otro"
        }) {
            out.printf(
                    "<option value=\"%s\"%s>%s</option>%n",
                    categoria,
                    categoria.equals(datos.categoria())
                            ? " selected"
                            : "",
                    categoria
            );
        }
        out.println("</select>");
        out.println(error(errores, "categoria"));

        out.println("<fieldset><legend>Prioridad *</legend>");
        for (Prioridad prioridad : Prioridad.values()) {
            out.printf("""
                    <label class="en-linea">
                    <input type="radio" name="prioridad" value="%s"%s>
                    %s
                    </label>
                    """,
                    prioridad.name(),
                    prioridad.name().equals(datos.prioridad())
                            ? " checked"
                            : "",
                    prioridad.getTexto()
            );
        }
        out.println(error(errores, "prioridad"));
        out.println("</fieldset>");

        out.printf("""
                <label for="descripcion">
                Descripción * (20-600 caracteres)
                </label>
                <textarea id="descripcion"
                name="descripcion"
                minlength="20"
                maxlength="600"
                required>%s</textarea>
                %s
                """,
                Html.escapar(datos.descripcion()),
                error(errores, "descripcion")
        );

        out.println("""
                <fieldset>
                <legend>Etiquetas (máximo 3)</legend>
                """);
        for (String etiqueta : new String[]{
                "Urgente", "Bloqueante", "Recurrente", "Equipo compartido"
        }) {
            out.printf("""
                    <label class="en-linea">
                    <input type="checkbox"
                    name="etiquetas"
                    value="%s"%s>
                    %s
                    </label>
                    """,
                    etiqueta,
                    datos.tieneEtiqueta(etiqueta)
                            ? " checked"
                            : "",
                    etiqueta
            );
        }
        out.println(error(errores, "etiquetas"));
        out.println("</fieldset>");

        out.printf("""
                <div class="acciones">
                <button type="submit">Registrar incidencia</button>
                <a class="boton secundario" href="%s/incidencias">
                Cancelar
                </a>
                </div>
                </form>
                """,
                ctx
        );

        Html.pie(out);
    }

    private static void campo(PrintWriter out,
                              String nombre,
                              String etiqueta,
                              String valor,
                              Map<String, String> errores,
                              String tipo,
                              String minimo,
                              String maximo) {
        out.printf("""
                <label for="%s">%s</label>
                <input id="%s"
                name="%s"
                type="%s"
                value="%s"
                minlength="%s"
                maxlength="%s"
                required>
                %s
                """,
                nombre,
                etiqueta,
                nombre,
                nombre,
                tipo,
                Html.escapar(valor),
                minimo,
                maximo,
                error(errores, nombre)
        );
    }

    private static String error(Map<String, String> errores,
                                String campo) {
        String mensaje = errores.get(campo);
        return mensaje == null
                ? ""
                : "<p class=\"error-campo\">"
                + Html.escapar(mensaje)
                + "</p>";
    }
}
