# Correcciones finales V16

Se aplicaron las correcciones solicitadas sin cambiar la lógica funcional existente:

- Se eliminó el seed de `db/migration` para evitar conflictos o carga de datos en producción.
- El seed completo quedó como script académico en `database/02_datos_prueba.sql` y como seed opcional en `src/main/resources/db/seed/V99__seed_data.sql`.
- `database/01_modelo_fisico_oracle.sql` quedó sincronizado con `V1__create_schema.sql`.
- `USUARIOS.EMAIL` tiene restricción nombrada `UK_USUARIO_EMAIL`.
- `database/03_consultas_avanzadas.sql` incluye vistas materializadas y fragmentación ejecutable con tablespaces.
- `database/06_indices_explain.sql` muestra EXPLAIN PLAN antes y después de crear índices.
- `SP_REGISTRAR_USUARIO` y `TRG_PAGOS_ACTIVAR_USUARIO` actualizan `FECHA_VENCIMIENTO`.
- `ROL_ADMIN` tiene permisos EXECUTE sobre procedimientos y funciones.
- La popularidad se recalcula al crear/actualizar reproducciones y existe endpoint administrativo para recalcular catálogo completo.
- Reproducciones y favoritos ahora son paginados.
- Temporadas solo se pueden crear para contenidos tipo SERIE o PODCAST.
- Calificaciones validan restricciones de perfil infantil.
- Servicios principales tienen `@Slf4j` y logs de operación.
- CORS expone el header `Authorization`.
- El reporte financiero `ingresos-plan` queda restringido a ADMIN.
- Se agregaron anotaciones `@Operation` en endpoints principales de Swagger.
