# Correcciones finales V7 — QuindioFlix Backend

Esta versión corrige los pendientes detectados para funcionalidad real con Angular y para cumplir mejor el CRUD de catálogo solicitado en el proyecto.

## Cambios críticos

- Se agregó `RolUsuario.CONTENIDO`.
- Se actualizó el `CHECK` SQL de `USUARIOS.ROL` para aceptar `CONTENIDO`.
- El usuario semilla `usuario3@mail.com` queda como rol `CONTENIDO`.
- `POST /api/contenidos`, `PUT /api/contenidos/{id}`, `DELETE /api/contenidos/{id}` requieren `ADMIN` o `CONTENIDO`.
- Se agregó CRUD de contenido.
- Se agregó creación de contenido relacionado: `POST /api/contenidos/{id}/relacionados`.
- Se agregó CRUD de temporadas para gestores de catálogo.
- Se agregó CRUD de episodios y el endpoint requerido: `GET /api/contenidos/{id}/temporadas/{temporadaId}/episodios`.
- Se agregó actualización y eliminación de calificaciones propias.
- Se agregó manejo de `AccessDeniedException` con respuesta `403` para Angular.

## Cambios importantes para frontend

- `GET /api/usuarios/{id}/pagos` devuelve pagos paginados.
- `GET /api/pagos?usuarioId=X` también devuelve pagos paginados.
- `GET /api/usuarios/{id}/referidos` lista referidos activos.
- `GET /api/usuarios/{id}/referente` indica quién refirió al usuario.
- Se agregó `README_INTEGRACION_ANGULAR.md` con rutas, roles de prueba y flujo Docker.

## Seguridad

- Los endpoints de escritura del catálogo ahora están protegidos por rol.
- Las calificaciones solo se pueden actualizar o eliminar por el propietario del perfil o ADMIN.
- Las reproducciones, favoritos y pagos conservan validación de propietario desde JWT.

## Ejecución recomendada

```bash
mvn clean spring-boot:run
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

Credenciales:

```text
ADMIN: usuario1@mail.com / Password123
MODERADOR: usuario2@mail.com / Password123
CONTENIDO: usuario3@mail.com / Password123
CLIENTE: usuario4@mail.com / Password123
```
