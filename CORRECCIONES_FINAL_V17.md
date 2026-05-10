# Correcciones finales V17 — Oracle, Angular y administración

Esta versión corrige los últimos puntos detectados antes de conectar con Oracle y preparar el frontend Angular.

## Corregido

- `database/07_usuarios_roles.sql` ahora usa prefijo `QUINDIOFLIX_APP.` en todos los `GRANT` sobre tablas, vistas materializadas, procedimientos y funciones. Esto evita `ORA-00942` cuando se ejecuta como `SYS` o `SYSTEM`.
- Se agregaron endpoints administrativos:
  - `GET /api/usuarios?page=0&size=20&rol=CLIENTE&estado=ACTIVO&planId=1&ciudad=Armenia`
  - `PATCH /api/usuarios/{id}/estado`
  - `PATCH /api/usuarios/{id}/rol`
  - `DELETE /api/perfiles/{id}`
- `UsuarioController` ya no inyecta `PagoService`; los pagos del usuario se consultan mediante `UsuarioService`.
- Los `GET` de consumo de catálogo (`relacionados`, `temporadas`, `episodios`) exigen usuario autenticado con cuenta activa.
- `AuthorizationService` incluye `isCurrentUserActive()` para las reglas de autorización por método.
- `docker-compose.yml` incluye `healthcheck` explícito para el backend y el frontend espera `service_healthy`.
- `CuentaScheduler`, `EmpleadoServiceImpl` y la eliminación de perfiles quedan integrados para evitar errores de compilación vistos en versiones mezcladas.

## Nota importante

Abrir siempre esta carpeta como proyecto raíz. Deben verse en la raíz:

- `pom.xml`
- `src`
- `database`
- `CORRECCIONES_FINAL_V17.md`

Si se compila otra carpeta como `quindioflix_backend_corregido_descarga/quindioflix-backend`, se estará usando una versión anterior mezclada.
