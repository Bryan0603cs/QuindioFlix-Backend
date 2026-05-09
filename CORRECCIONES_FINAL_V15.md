# Correcciones finales V15 — Entregables BD II, seguridad infantil y estabilidad

Esta versión se generó sobre el ZIP entregado por el usuario y conserva la lógica anterior del backend, corrigiendo los pendientes detectados para cumplir mejor el proyecto académico y la integración Angular.

## Correcciones críticas de scripts Oracle

- `src/main/resources/db/migration` queda limpio solo con:
  - `V1__create_schema.sql`
  - `V3__indexes.sql`
- Se eliminaron `V2__indexes.sql` y `V2__seed_data.sql` de migraciones para evitar conflictos Flyway y no cargar datos de prueba en producción.
- `database/02_datos_prueba.sql` fue reemplazado por el seed completo equivalente a `db/seed/V99__seed_data.sql`, con datos suficientes para los mínimos del PDF.
- `database/01_modelo_fisico_oracle.sql` fue sincronizado con `V1__create_schema.sql`.
- Se agregó `CONSTRAINT UK_USUARIO_EMAIL UNIQUE (EMAIL)` a `USUARIOS`.
- `V3__indexes.sql` usa `IDX_USUARIOS_EMAIL_LOWER` para búsquedas case-insensitive sin duplicar la restricción UNIQUE.

## Consultas avanzadas

- `database/03_consultas_avanzadas.sql` ahora incluye:
  - consultas parametrizadas
  - PIVOT
  - UNPIVOT
  - ROLLUP
  - CUBE
  - GROUPING SETS
  - `MV_CONTENIDO_POPULAR`
  - `MV_INGRESOS_MENSUALES`
  - `BUILD IMMEDIATE`
  - `REFRESH COMPLETE ON DEMAND`
  - verificación con `USER_MVIEWS`
  - fragmentación por rango de `REPRODUCCIONES` con `CREATE TABLESPACE`, `DATAFILE` y `PARTITION BY RANGE`.

## PL/SQL

- `SP_REGISTRAR_USUARIO` ahora inserta `FECHA_VENCIMIENTO`.
- `TRG_PAGOS_ACTIVAR_USUARIO` ahora actualiza `FECHA_VENCIMIENTO`.
- Se agregó bloque anónimo de prueba para `SP_REPORTE_CONSUMO` con `SYS_REFCURSOR`.
- `ROL_ADMIN` recibe `GRANT EXECUTE` sobre procedimientos y funciones.

## Índices y EXPLAIN PLAN

- `database/06_indices_explain.sql` ahora tiene estructura BEFORE/AFTER:
  - DROP INDEX
  - EXPLAIN PLAN antes
  - CREATE INDEX
  - EXPLAIN PLAN después
- Incluye análisis para reproducciones por perfil/fecha y búsqueda por email.

## Backend Spring Boot

- `AuthServiceImpl`, `PerfilServiceImpl`, `PlanServiceImpl`, `FavoritoServiceImpl` y `GeneroServiceImpl` tienen `@Slf4j`.
- El login exitoso queda registrado con usuario, email y rol.
- `ReproduccionServiceImpl` valida perfiles infantiles y actualiza popularidad.
- `CalificacionServiceImpl` impide que perfiles infantiles califiquen contenido no permitido.
- `TemporadaServiceImpl` impide crear temporadas para películas, documentales o música.
- `ReproduccionController.listarPorPerfil` ahora es paginado.
- `FavoritoController.listarPorPerfil` ahora es paginado.
- Se agregó `POST /api/admin/contenidos/actualizar-popularidad`.
- `SecurityConfig` expone el header `Authorization` para Angular.

## Angular / Docker

- `README_INTEGRACION_ANGULAR.md` documenta cómo compilar Angular y copiar el `dist` a `frontend/dist`.
- Se conserva el uso de Docker y el healthcheck anterior.

## Notas

- Para H2 local: `mvn clean spring-boot:run`
- Para Oracle sin datos: `mvn spring-boot:run -Dspring-boot.run.profiles=oracle`
- Para Oracle con datos: `mvn spring-boot:run -Dspring-boot.run.profiles=oracle,seed`
