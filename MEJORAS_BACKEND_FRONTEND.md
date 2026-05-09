# Mejoras finales para backend + Angular + Oracle

Esta versión corrige los puntos pendientes detectados para dejar el backend más seguro y listo para integrarse con Angular.

## Correcciones críticas

- `RegistrarUsuarioCommand` ya no expone `montoPrimerPago`. El primer pago se calcula internamente con `plan.getPrecioMensual()` y el descuento por referido.
- `RegistrarPagoCommand` tampoco recibe `descuentoAplicado`; el backend calcula el descuento por referido activo.
- `USUARIOS.FECHA_VENCIMIENTO` quedó en el DDL principal y en los datos semilla.
- Se eliminó la migración duplicada `V4__add_fecha_vencimiento.sql` para evitar error por columna duplicada en instalaciones limpias.
- `AnaliticaController` usa `@DateTimeFormat(iso = DATE_TIME)` en los parámetros `desde` y `hasta`.
- `DELETE /api/usuarios/{id}` quedó protegido: solo puede ejecutarlo el propio usuario o un `ADMIN`.
- `POST /api/pagos` y `GET /api/pagos?usuarioId=X` quedaron protegidos por usuario autenticado o `ADMIN`.
- `GET /api/reportes-contenido` y `PATCH /api/reportes-contenido/{id}/resolver` requieren `ADMIN` o `MODERADOR`.
- `POST /api/reportes-contenido` valida que el usuario autenticado coincida con `usuarioReportaId`, salvo que sea `ADMIN`.

## Endpoints nuevos para Angular

- `GET /api/categorias`
- `GET /api/generos`
- `GET /api/contenidos/{id}/temporadas`
- `GET /api/contenidos/{id}/relacionados`
- `GET /api/favoritos/perfil/{perfilId}`
- `DELETE /api/favoritos/{perfilId}/{contenidoId}`
- `GET /api/reportes/analitica/consumo-usuario?usuarioId=1&desde=2025-01-01T00:00:00&hasta=2025-12-31T23:59:59`

## Docker y despliegue local

- Se agregó `Dockerfile` del backend.
- `docker-compose.yml` ahora incluye Oracle y backend.
- `.env.example` usa `ORACLE_USERNAME`, igual que `application-oracle.yml`.
- CORS incluye Angular local y escenarios Docker:
  - `http://localhost:4200`
  - `http://127.0.0.1:4200`
  - `http://localhost:80`
  - `http://frontend:4200`
  - `http://frontend:80`

## Arquitectura

- `UsuarioController` ya no inyecta `FavoritoService`; delega favoritos de usuario a `UsuarioService`.
- `ContenidoServiceImpl` usa `MapperService.contenidoRelacionado()`.
- Se agregaron servicios dedicados para `Categoria`, `Genero` y `Temporada`.
- `CuentaScheduler` quedó activo solo en perfil `oracle`.
