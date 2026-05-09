# Correcciones finales V9 — seguridad, scheduler, Angular y Oracle

## Correcciones críticas aplicadas

1. **SecurityConfig ya no expone todo `/api/contenidos/**` como público.**
   - Solo quedan públicos `GET /api/contenidos`, `GET /api/contenidos/{id}` y `GET /api/contenidos/{id}/relacionados`.
   - La lectura de temporadas y episodios requiere token.
   - `GET /api/contenidos/{id}/temporadas` y `GET /api/contenidos/{id}/temporadas/{temporadaId}/episodios` requieren usuario activo mediante `@authorizationService.isActiveUser()`.

2. **CuentaScheduler ya no está limitado al perfil `oracle`.**
   - Ahora también corre en `dev` con H2.
   - Se puede probar localmente cambiando `CUENTAS_VENCIDAS_CRON`.

3. **Reproducciones validan cuenta activa en el servicio.**
   - Registrar reproducción, listar historial y actualizar avance validan que la cuenta del perfil esté `ACTIVO`.

4. **Endpoints administrativos agregados para Angular.**
   - `GET /api/usuarios?page=0&size=20`
   - `PATCH /api/usuarios/{id}/estado`
   - `PATCH /api/usuarios/{id}/rol`

5. **CORS configurable desde Docker/.env.**
   - `docker-compose.yml` ahora lee `CORS_ALLOWED_ORIGINS` desde variable de entorno.

6. **OpenAPI mejorado con ejemplos en DTO principales.**
   - Login, registro, pagos, reproducciones y creación de contenido tienen ejemplos en Swagger.

7. **`año` eliminado de Java/API.**
   - Se usa `año` y `añoLanzamiento`.
   - `AnaliticaController` usa `@RequestParam("año")`.
   - `AnaliticaServiceImpl` usa parámetros posicionales en SQL nativo para evitar errores con `ciudad`, `mes` y `año`.

8. **Perfil individual eliminable.**
   - `DELETE /api/perfiles/{id}` borra calificaciones, favoritos y reproducciones del perfil.
   - No permite eliminar el último perfil de una cuenta.

9. **Índice explícito de email.**
   - `IDX_USUARIOS_EMAIL` quedó como índice único explícito en migración y scripts Oracle.

10. **Fragmentación Oracle documentada y activa en script avanzado.**
    - `database/03_consultas_avanzadas.sql` incluye tablespaces, particiones RANGE y verificación con `USER_TAB_PARTITIONS`.

## Archivos importantes modificados

- `SecurityConfig.java`
- `AuthorizationService.java`
- `CuentaScheduler.java`
- `ReproduccionServiceImpl.java`
- `UsuarioController.java`
- `UsuarioService.java`
- `UsuarioServiceImpl.java`
- `PerfilController.java`
- `PerfilService.java`
- `PerfilServiceImpl.java`
- `AnaliticaController.java`
- `AnaliticaService.java`
- `AnaliticaServiceImpl.java`
- `docker-compose.yml`
- `.env.example`
- `application.yml`
- `database/03_consultas_avanzadas.sql`
- `database/06_indices_explain.sql`
- `src/main/resources/db/migration/V1__create_schema.sql`
- `src/main/resources/db/migration/V3__indexes.sql`
