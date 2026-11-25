# Gestor de incidencias

Aplicación web con Servlets (Jakarta EE) para registrar y seguir incidencias: alta validada, listado con filtros, detalle y cambio de estado. Los datos viven en memoria.

## Requisitos
- Java 17
- Maven
- Tomcat 11

## Construcción
mvn clean package

## Despliegue
Copiar target/gestor-incidencias.war a webapps de Tomcat.

## Acceso
http://localhost:8080/gestor-incidencias/incidencias

## Funciones
- Alta validada
- Listado
- Filtros
- Detalle
- Cambio de estado

## Limitaciones
Los datos se almacenan en memoria y se pierden al reiniciar.
