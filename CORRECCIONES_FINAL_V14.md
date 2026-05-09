# Correcciones finales V14

Esta versión corrige los pendientes reportados sin eliminar la lógica funcional anterior del backend.

## Correcciones críticas

- Se eliminó definitivamente `src/main/resources/db/migration/V2__indexes.sql`.
- Se retiró `src/main/resources/db/migration/V2__seed_data.sql` del flujo principal de Flyway para que producción no cargue datos semilla.
- El seed completo quedó solo como `src/main/resources/db/seed/V99__seed_data.sql` y se activa con el perfil `seed`.
- `USUARIOS.EMAIL` ahora tiene restricción `CONSTRAINT UK_USUARIO_EMAIL UNIQUE (EMAIL)` desde el modelo físico inicial.
- Se evitó el choque entre la restricción UNIQUE y el índice de email: `V3__indexes.sql` usa `IDX_USUARIOS_EMAIL_LOWER` para búsquedas case-insensitive.
- Se agregó endpoint administrativo `POST /api/admin/contenidos/actualizar-popularidad`.
- La popularidad se recalcula también al registrar o actualizar reproducciones.

## Correcciones PL/SQL / Oracle

- `SP_REGISTRAR_USUARIO` ahora inserta `FECHA_VENCIMIENTO`.
- `TRG_PAGOS_ACTIVAR_USUARIO` ahora actualiza `FECHA_VENCIMIENTO` cuando entra un pago exitoso.
- `07_usuarios_roles.sql` otorga `GRANT EXECUTE` a `ROL_ADMIN` sobre procedimientos y funciones.
- `06_indices_explain.sql` quedó dividido en BEFORE y AFTER para evidenciar el cambio del plan de ejecución.

## Seguridad e integración Angular

- `AuthorizationService` ya no consulta `USUARIOS` en cada request solo para validar estado; usa el estado incluido en el JWT.
- `/api/reportes/analitica/ingresos-plan` queda restringido a `ADMIN`.
- Los otros reportes analíticos siguen disponibles para `ADMIN` y `MODERADOR`.
- CORS ahora expone el header `Authorization` para clientes Angular.

## Cómo ejecutar

Dev H2:

```bash
mvn clean spring-boot:run
```

Oracle sin datos semilla:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=oracle
```

Oracle con datos semilla:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=oracle,seed
```
