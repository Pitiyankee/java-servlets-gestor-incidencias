package es.centro.dwes.incidencias.repositorio;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AplicacionListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        evento.getServletContext().setAttribute(
                IncidenciaRepositorio.CLAVE_CONTEXTO,
                new IncidenciaRepositorio()
        );
        evento.getServletContext().log(
                "Repositorio de incidencias inicializado"
        );
    }

    @Override
    public void contextDestroyed(ServletContextEvent evento) {
        evento.getServletContext().log(
                "Aplicación gestor-incidencias detenida"
        );
    }
}
