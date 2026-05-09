# Correcciones finales V6

Esta versión consolida las correcciones críticas para dejar el backend listo para pruebas con Swagger, Angular, Docker y Oracle/H2.

## Corregido

- Se eliminó definitivamente la migración duplicada `V4__add_fecha_vencimiento.sql`.
- `FECHA_VENCIMIENTO` queda creada solo en `V1__create_schema.sql`.
- `PagoServiceImpl` aplica descuento por referido únicamente cuando `estadoPago == EXITOSO`.
- Los pagos fallidos, pendientes o reembolsados no aplican descuento ni activan cuenta.
- `AuthorizationService` ya no consulta la tabla `USUARIOS` por cada autorización. Lee el usuario autenticado desde el principal construido con el JWT.
- Se agregó `AuthenticatedUser` con `usuarioId`, email y rol.
- `JwtAuthenticationFilter` reconstruye el usuario autenticado desde claims del JWT.
- Se agregó `GET /api/usuarios/me` para Angular.
- Se protegió el acceso a reproducciones por propiedad del perfil.
- Se protegió la creación de calificaciones y favoritos por propiedad del perfil.
- Se protegió la actualización del avance de reproducción por propiedad de la reproducción.
- Se agregó `PUT /api/perfiles/{id}` para editar nombre y avatar.
- Se movió la consulta de temporadas a `TemporadaController` para evitar mezcla de responsabilidades en `ContenidoController`.
- Se agregó configuración de paginación para `Pageable`.
- Se reemplazó `exception.printStackTrace()` por `log.error(...)`.
- Se agregó `@Max(2030)` para `anioLanzamiento`.
- Se movió la eliminación transaccional de cuenta desde `EntityManager` en el servicio hacia métodos de repositorio con `@Modifying`.
- Se simplificó `Dockerfile` usando imagen Maven oficial.
- Se agregó servicio `frontend` opcional en `docker-compose.yml` con profile `frontend`.
- Se agregó `nginx.conf` y `frontend/dist/index.html` placeholder para integración con Angular.
- Se documentaron endpoints principales para Angular en `docs_arquitectura.md`.

## Orden recomendado de ejecución

```bash
mvn clean spring-boot:run
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

Login de prueba:

```json
{
  "email": "usuario1@mail.com",
  "password": "Password123"
}
```
