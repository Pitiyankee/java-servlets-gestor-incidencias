package es.centro.dwes.incidencias.web;

import java.io.PrintWriter;

public final class Html {

    private Html() {
    }

    public static String escapar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    public static void cabecera(PrintWriter out,
                                String titulo,
                                String contexto) {
        out.printf("""
                <!doctype html>
                <html lang="es">
                <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <title>%s</title>
                <link rel="stylesheet" href="%s/css/estilos.css">
                </head>
                <body>
                <header class="cabecera">
                <div class="contenedor barra">
                <a class="marca" href="%s/incidencias">Gestor de incidencias</a>
                <nav>
                <a href="%s/incidencias">Listado</a>
                <a href="%s/incidencias/nueva">Nueva incidencia</a>
                </nav>
                </div>
                </header>
                <main class="contenedor">
                """,
                escapar(titulo),
                contexto,
                contexto,
                contexto,
                contexto
        );
    }

    public static void pie(PrintWriter out) {
        out.println("""
                </main>
                <footer class="pie">Práctica 2 · DWES</footer>
                </body>
                </html>
                """);
    }
}
