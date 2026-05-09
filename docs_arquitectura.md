# Decisiones de arquitectura del backend

El backend se estructuró con separación por capas para facilitar mantenimiento, pruebas y conexión posterior con Angular:

- `domain`: conceptos puros del negocio, enumeraciones y excepciones propias.
- `application`: casos de uso, DTOs, validaciones de negocio y coordinación transaccional.
- `infrastructure`: persistencia JPA/Oracle, repositorios, seguridad JWT y configuración técnica.
- `presentation`: controladores REST y manejo global de errores.

## Reglas de negocio implementadas

1. El email del usuario es único.
2. Un usuario solo puede seleccionar un plan activo.
3. Al registrarse se crea usuario, perfil principal y primer pago dentro de una transacción.
4. No se permite bajar a un plan con menos perfiles que los que ya tiene el usuario.
5. Los perfiles infantiles solo acceden a contenido `TP`, `MAS_7` y `MAS_13`.
6. Solo las cuentas activas pueden registrar reproducciones.
7. Una reproducción no puede terminar antes de iniciar.
8. Un perfil solo puede calificar si reprodujo al menos el 50% del contenido.
9. Un perfil no puede calificar dos veces el mismo contenido.
10. Un perfil no puede duplicar el mismo favorito.
11. Solo usuarios `MODERADOR` o `ADMIN` pueden resolver reportes.
12. Un pago exitoso activa la cuenta y actualiza la fecha del último pago.

## Enfoque Oracle

El modelo físico incluye relaciones N:M (`CONTENIDO_GENERO`), relaciones reflexivas (`USUARIOS.ID_REFERIDO_POR`, `EMPLEADOS.ID_SUPERVISOR`, `CONTENIDO_RELACIONADO`) y tablas de operación para reproducciones, pagos, calificaciones, favoritos y reportes. Los scripts del curso se dejaron separados en la carpeta `database/` para que puedan ejecutarse y sustentarse desde SQL Developer.

## Endpoints principales para Angular

### Autenticación
- `POST /api/auth/login`: autentica usuario y devuelve JWT.
- `POST /api/auth/register`: registra usuario, crea perfil principal y primer pago calculado desde el plan.

### Usuario y perfiles
- `GET /api/usuarios/me`: devuelve el usuario autenticado desde el JWT.
- `GET /api/usuarios/{id}`: consulta usuario por id, protegido por propietario o ADMIN.
- `PATCH /api/usuarios/{id}/plan`: cambia plan, protegido por propietario o ADMIN.
- `DELETE /api/usuarios/{id}`: elimina la cuenta de forma transaccional, protegido por propietario o ADMIN.
- `GET /api/usuarios/{id}/perfiles`: lista perfiles del usuario.
- `POST /api/usuarios/{id}/perfiles`: crea perfil validando máximo por plan.
- `PUT /api/perfiles/{id}`: actualiza nombre y avatar del perfil, protegido por propietario o ADMIN.

### Catálogo
- `GET /api/planes` y `GET /api/planes/{id}`: planes disponibles.
- `GET /api/categorias`: categorías para filtros.
- `GET /api/generos`: géneros para filtros.
- `GET /api/contenidos`: catálogo paginado con filtros `titulo`, `categoriaId`, `generoId`, `page`, `size`, `sort`.
- `GET /api/contenidos/{id}`: detalle de contenido.
- `GET /api/contenidos/{id}/relacionados`: contenido relacionado.
- `GET /api/contenidos/{contenidoId}/temporadas`: temporadas con episodios para series y podcasts.

### Consumo
- `GET /api/reproducciones?perfilId=X`: historial del perfil, protegido por propietario o ADMIN.
- `POST /api/reproducciones`: crea reproducción, protegido por propietario del perfil o ADMIN.
- `PATCH /api/reproducciones/{id}`: actualiza avance, protegido por propietario de la reproducción o ADMIN.
- `GET /api/calificaciones?contenidoId=X`: calificaciones públicas de un contenido.
- `POST /api/calificaciones`: crea calificación, protegido por propietario del perfil o ADMIN.
- `GET /api/favoritos/perfil/{perfilId}`: favoritos del perfil, protegido por propietario o ADMIN.
- `POST /api/favoritos`: agrega favorito, protegido por propietario del perfil o ADMIN.
- `DELETE /api/favoritos/{perfilId}/{contenidoId}`: elimina favorito, protegido por propietario del perfil o ADMIN.

### Pagos y reportes
- `GET /api/pagos?usuarioId=X`: historial de pagos, protegido por propietario o ADMIN.
- `POST /api/pagos`: registra renovación, calcula monto desde el plan y aplica descuento solo si el pago es EXITOSO.
- `POST /api/reportes-contenido`: crea reporte, protegido por el usuario reportante.
- `GET /api/reportes-contenido`: listado para ADMIN o MODERADOR.
- `PATCH /api/reportes-contenido/{id}/resolver`: resolución por ADMIN o MODERADOR autorizado.

### Analítica
- `GET /api/reportes/analitica/top-contenido-ciudad`
- `GET /api/reportes/analitica/ingresos-plan`
- `GET /api/reportes/analitica/calificacion-genero`
- `GET /api/reportes/analitica/consumo-usuario?usuarioId=X&desde=2025-01-01T00:00:00&hasta=2025-12-31T23:59:59`

Estos endpoints quedan preparados para consumo desde Angular usando el header:

```http
Authorization: Bearer <token>
```

## API final para integración con Angular

### Seguridad

El backend usa JWT. El frontend debe enviar:

```http
Authorization: Bearer <token>
```

Los roles funcionales de la aplicación son:

- `CLIENTE`: consumo, favoritos, calificaciones, pagos y gestión de su cuenta.
- `MODERADOR`: revisión de reportes de contenido.
- `CONTENIDO`: gestión del catálogo, temporadas, episodios, géneros y relaciones entre contenidos.
- `ADMIN`: acceso completo.

### Catálogo

- `GET /api/contenidos`: catálogo paginado con filtros por título, categoría y género.
- `GET /api/contenidos/{id}`: detalle de contenido.
- `POST /api/contenidos`: crear contenido, solo `ADMIN` o `CONTENIDO`.
- `PUT /api/contenidos/{id}`: actualizar contenido, solo `ADMIN` o `CONTENIDO`.
- `DELETE /api/contenidos/{id}`: eliminar contenido, solo `ADMIN` o `CONTENIDO`.
- `GET /api/contenidos/{id}/relacionados`: contenido relacionado.
- `POST /api/contenidos/{id}/relacionados`: crear relación secuela, precuela, remake, spin-off, etc.

### Temporadas y episodios

- `GET /api/contenidos/{id}/temporadas`: temporadas con episodios agrupados.
- `POST /api/contenidos/{id}/temporadas`: crear temporada.
- `PUT /api/contenidos/{id}/temporadas/{temporadaId}`: actualizar temporada.
- `DELETE /api/contenidos/{id}/temporadas/{temporadaId}`: eliminar temporada.
- `GET /api/contenidos/{id}/temporadas/{temporadaId}/episodios`: listar episodios de una temporada.
- `POST /api/contenidos/{id}/temporadas/{temporadaId}/episodios`: crear episodio.
- `PUT /api/contenidos/{id}/temporadas/{temporadaId}/episodios/{episodioId}`: actualizar episodio.
- `DELETE /api/contenidos/{id}/temporadas/{temporadaId}/episodios/{episodioId}`: eliminar episodio.

### Calificaciones

- `GET /api/calificaciones?contenidoId=X`: listar calificaciones de un contenido.
- `POST /api/calificaciones`: crear calificación, validando reproducción mínima del 50%.
- `PUT /api/calificaciones/{id}`: actualizar calificación propia.
- `DELETE /api/calificaciones/{id}`: eliminar calificación propia.

### Pagos y referidos

- `GET /api/pagos?usuarioId=X&page=0&size=10`: historial paginado.
- `GET /api/usuarios/{id}/pagos?page=0&size=10`: ruta REST consistente para historial de pagos.
- `POST /api/pagos`: registrar intento de pago.
- `GET /api/usuarios/{id}/referidos`: usuarios referidos activos.
- `GET /api/usuarios/{id}/referente`: usuario referente.

El monto del pago se calcula desde el plan. El descuento por referido solo se aplica cuando el pago es `EXITOSO`.


## Seed de datos

Las migraciones base no cargan datos masivos en producción. Para cargar datos de demostración en Oracle se activa el perfil `seed`: `mvn spring-boot:run -Dspring-boot.run.profiles=oracle,seed`.
