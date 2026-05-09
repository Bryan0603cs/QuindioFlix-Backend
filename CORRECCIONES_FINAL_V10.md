# Correcciones finales V10 — Seguridad, Angular y producción

Esta versión corrige los pendientes detectados después de la V9 y deja el backend más seguro para integración con Angular y ejecución con Oracle/Docker.

## Seguridad y autenticación

- `SecurityConfig` ya no expone `GET /api/contenidos/**` de forma abierta.
- Solo quedan públicos:
  - `GET /api/contenidos`
  - `GET /api/contenidos/{id}`
  - `GET /api/planes`
  - `GET /api/categorias`
  - `GET /api/generos`
- Temporadas, episodios, relacionados, calificaciones, favoritos y reproducciones requieren usuario autenticado y cuenta activa.
- `AppUserDetailsService` bloquea login de usuarios `INACTIVO` o `SUSPENDIDO`.
- `AuthenticatedUser` ahora incluye `EstadoCuenta` y ya no devuelve todos los flags de seguridad en `true`.
- El JWT ahora incluye `estadoCuenta`.
- `AuthorizationService` valida contra base de datos que la cuenta siga activa antes de permitir operaciones de usuario/perfil/reproducción/calificación.

## Administración Angular

- `GET /api/usuarios` ahora permite filtros:
  - `rol`
  - `estado`
  - `planId`
  - `ciudad`
  - `page`, `size`, `sort`
- `PATCH /api/usuarios/{id}/estado` permite suspender/reactivar cuentas a ADMIN/MODERADOR.
- `PATCH /api/usuarios/{id}/rol` permite al ADMIN cambiar roles.
- Se agregó `GET /api/empleados` para que Angular pueda cargar el selector de empleado responsable al crear/editar contenido.

## Reportes de contenido

- `POST /api/reportes-contenido` ya no recibe `usuarioReportaId` en el body. Lo toma del JWT.
- `PATCH /api/reportes-contenido/{id}/resolver` ya no recibe `moderadorId` en el body. Lo toma del JWT.
- `GET /api/reportes-contenido` ahora es paginado y permite filtrar por estado.

## Pagos

- `RegistrarPagoCommand` ya no recibe `estadoPago` desde el cliente.
- `POST /api/pagos` registra pagos exitosos desde la API pública.
- El monto se calcula internamente desde el plan del usuario.
- El descuento por referido se calcula internamente.
- El historial de pagos permanece paginado.

## Manejo de errores

- `GlobalExceptionHandler` ahora maneja:
  - `MissingServletRequestParameterException`
  - `DisabledException`
  - `LockedException`
  - `AccessDeniedException`
- Angular recibe siempre respuestas JSON en errores comunes de validación, permisos y parámetros faltantes.

## Scheduler y Docker

- `CuentaScheduler` corre en dev y oracle.
- Se agregó endpoint manual:
  - `POST /api/admin/cuentas/desactivar-vencidas`
- `Dockerfile` tiene `HEALTHCHECK` contra `/actuator/health`.
- `docker-compose.yml` espera que el backend esté saludable antes de iniciar el frontend.
- `CORS_ALLOWED_ORIGINS` queda parametrizado desde `.env.example` / variables de entorno.

## JSON actualizados

### Crear reporte

```json
{
  "contenidoId": 3,
  "descripcion": "Contenido no apto para este perfil"
}
```

### Resolver reporte

```json
{
  "estado": "RESUELTO",
  "comentarioResolucion": "Reporte revisado por moderación"
}
```

### Registrar pago

```json
{
  "usuarioId": 1,
  "metodoPago": "PSE",
  "referencia": "PAGO-PRUEBA-001"
}
```

