# Correcciones finales V11

Esta versión corrige los pendientes detectados después de la V10.

## Críticos corregidos

1. `SecurityConfig` ya no expone `GET /api/contenidos/**` públicamente.
   - Todo consumo de catálogo, temporadas, episodios, relacionados y calificaciones requiere token.
   - Los controladores de contenido agregan `@PreAuthorize("@authorizationService.isCurrentUserActive()")` en endpoints de lectura.

2. `application-dev.yml` ya no ejecuta Flyway contra H2.
   - `spring.flyway.enabled: false`
   - `ddl-auto: create-drop`
   - `data-dev.sql` carga datos mínimos compatibles con H2.

3. `POST /api/pagos` ya no recibe `usuarioId` ni `estadoPago` desde el body.
   - El usuario se toma del JWT con `@AuthenticationPrincipal`.
   - El backend define el pago como `EXITOSO` y calcula monto/descuento internamente.

## Pendientes medios corregidos

- El seed de prueba se separó de las migraciones de producción.
  - Producción: `classpath:db/migration`
  - Seed opcional: activar perfil `seed` para usar `classpath:db/seed`.
- `docker-compose.yml` declara `healthcheck` explícito para backend.
- `ContenidoRepository.buscarCatalogo` ya no usa `CONCAT` anidado.
- Se agregó `GET /api/empleados` para formularios de Angular.
- Se agregó manejo JSON para parámetros faltantes y cuentas bloqueadas.
- `.env.example` documenta todas las variables usadas por Docker y Spring.
- `README_INTEGRACION_ANGULAR.md` incluye las instrucciones actualizadas.

## Ejecutar localmente

```bash
mvn clean spring-boot:run
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

## Ejecutar con Oracle y seed opcional

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=oracle,seed
```
