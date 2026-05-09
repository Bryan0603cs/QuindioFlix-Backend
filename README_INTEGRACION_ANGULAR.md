# Integración Angular ↔ QuindioFlix Backend

## Base URL local

```text
http://localhost:8080
```

## Autenticación

1. Consumir `POST /api/auth/login`.
2. Guardar el campo `token` de la respuesta.
3. Enviar el token en cada petición protegida:

```http
Authorization: Bearer <token>
```

## Usuarios de prueba

Todos usan la contraseña:

```text
Password123
```

| Rol | Email |
|---|---|
| ADMIN | usuario1@mail.com |
| MODERADOR | usuario2@mail.com |
| CONTENIDO | usuario3@mail.com |
| CLIENTE | usuario4@mail.com |

## Endpoints principales para Angular

### Auth

| Método | Ruta | Uso |
|---|---|---|
| POST | `/api/auth/login` | Iniciar sesión |
| POST | `/api/auth/register` | Registrar usuario con primer pago calculado internamente |

### Usuario

| Método | Ruta | Uso |
|---|---|---|
| GET | `/api/usuarios/me` | Usuario autenticado |
| GET | `/api/usuarios/{id}` | Detalle de usuario |
| GET | `/api/usuarios/{id}/perfiles` | Perfiles del usuario |
| GET | `/api/usuarios/{id}/favoritos` | Favoritos agregados de todos los perfiles |
| GET | `/api/usuarios/{id}/pagos?page=0&size=10` | Historial paginado de pagos |
| GET | `/api/usuarios/{id}/referidos` | Usuarios referidos activos |
| GET | `/api/usuarios/{id}/referente` | Usuario que lo refirió, si existe |
| PATCH | `/api/usuarios/{id}/plan` | Cambiar plan |
| DELETE | `/api/usuarios/{id}` | Eliminar cuenta propia o como ADMIN |

### Perfiles

| Método | Ruta | Uso |
|---|---|---|
| POST | `/api/usuarios/{id}/perfiles` | Crear perfil |
| PUT | `/api/perfiles/{id}` | Editar nombre/avatar del perfil |
| DELETE | `/api/perfiles/{id}` | Eliminar perfil, excepto si es el último de la cuenta |

### Catálogo

| Método | Ruta | Uso |
|---|---|---|
| GET | `/api/categorias` | Filtros de categoría |
| GET | `/api/generos` | Filtros de género |
| GET | `/api/contenidos?page=0&size=20&titulo=&categoriaId=&generoId=` | Catálogo paginado |
| GET | `/api/contenidos/{id}` | Detalle de contenido |
| GET | `/api/contenidos/{id}/relacionados` | Secuelas, precuelas, spin-offs, etc. |
| GET | `/api/contenidos/{id}/temporadas` | Temporadas con episodios agrupados |
| GET | `/api/contenidos/{id}/temporadas/{temporadaId}/episodios` | Episodios de una temporada |

### Gestión de catálogo: ADMIN o CONTENIDO

| Método | Ruta | Uso |
|---|---|---|
| POST | `/api/contenidos` | Crear contenido |
| PUT | `/api/contenidos/{id}` | Actualizar contenido |
| DELETE | `/api/contenidos/{id}` | Eliminar contenido y sus datos dependientes |
| POST | `/api/contenidos/{id}/relacionados` | Crear relación entre contenidos |
| POST | `/api/contenidos/{id}/temporadas` | Crear temporada |
| PUT | `/api/contenidos/{id}/temporadas/{temporadaId}` | Actualizar temporada |
| DELETE | `/api/contenidos/{id}/temporadas/{temporadaId}` | Eliminar temporada |
| POST | `/api/contenidos/{id}/temporadas/{temporadaId}/episodios` | Crear episodio |
| PUT | `/api/contenidos/{id}/temporadas/{temporadaId}/episodios/{episodioId}` | Actualizar episodio |
| DELETE | `/api/contenidos/{id}/temporadas/{temporadaId}/episodios/{episodioId}` | Eliminar episodio |

### Consumo

| Método | Ruta | Uso |
|---|---|---|
| GET | `/api/reproducciones?perfilId=1` | Historial de reproducción del perfil autenticado |
| POST | `/api/reproducciones` | Registrar reproducción |
| PATCH | `/api/reproducciones/{id}` | Actualizar avance |
| GET | `/api/favoritos/perfil/{perfilId}` | Favoritos de un perfil |
| POST | `/api/favoritos` | Agregar favorito |
| DELETE | `/api/favoritos/{perfilId}/{contenidoId}` | Quitar favorito |
| GET | `/api/calificaciones?contenidoId=1` | Calificaciones de un contenido |
| POST | `/api/calificaciones` | Crear calificación |
| PUT | `/api/calificaciones/{id}` | Actualizar calificación propia |
| DELETE | `/api/calificaciones/{id}` | Eliminar calificación propia |

### Pagos

| Método | Ruta | Uso |
|---|---|---|
| GET | `/api/pagos?usuarioId=1&page=0&size=10` | Historial paginado |
| POST | `/api/pagos` | Registrar pago del usuario autenticado |

El backend calcula el monto desde el plan, aplica el descuento por referido internamente y registra el pago como `EXITOSO`. El frontend no debe enviar `usuarioId` ni `estadoPago` en el body.

### Reportes y analítica

| Método | Ruta | Uso |
|---|---|---|
| POST | `/api/reportes-contenido` | Crear reporte de contenido inapropiado |
| GET | `/api/reportes-contenido` | Listar reportes, ADMIN o MODERADOR |
| PATCH | `/api/reportes-contenido/{id}/resolver` | Resolver reporte |
| GET | `/api/reportes/analitica/top-contenido-ciudad` | Top por ciudad |
| GET | `/api/reportes/analitica/ingresos-plan?mes=5&anio=2026` | Ingresos por plan. También acepta `año`, pero se recomienda `anio` en Angular |
| GET | `/api/reportes/analitica/calificacion-genero` | Calificación por género |
| GET | `/api/reportes/analitica/consumo-usuario` | Consumo por usuario entre fechas ISO |

## Paginación

Los endpoints paginados aceptan:

```text
?page=0&size=20&sort=titulo,asc
```

## Docker con Angular

El `docker-compose.yml` incluye un servicio opcional `frontend` con Nginx. Para usarlo:

1. Compilar Angular:

```bash
ng build --configuration production
```

2. Copiar el contenido generado de `dist/<nombre-app>/browser` o `dist/<nombre-app>` a:

```text
backend/frontend/dist
```

3. Ejecutar:

```bash
docker compose --profile frontend up --build
```

Angular quedará servido en:

```text
http://localhost
```

El proxy Nginx redirige `/api/` hacia el contenedor `backend:8080`.


## Correcciones V11 importantes para Angular

- Todos los endpoints de `/api/contenidos/**` requieren token y cuenta ACTIVA.
- En perfil `dev`, Flyway está desactivado y se usa H2 con `data-dev.sql` para pruebas locales.
- `POST /api/pagos` ya no recibe `usuarioId` ni `estadoPago`; el usuario se toma del JWT y el estado siempre lo define el backend.
- `POST /api/reportes-contenido` ya no recibe `usuarioReportaId`; se toma del JWT.
- `PATCH /api/reportes-contenido/{id}/resolver` ya no recibe `moderadorId`; se toma del JWT.
- Para cargar el selector de empleados en Angular usar `GET /api/empleados?page=0&size=20`.
- Para levantar frontend con Docker, primero compilar Angular y copiar el resultado en `frontend/dist`.


## Administración de usuarios

Cambiar estado de cuenta:

```http
PATCH /api/usuarios/{id}/estado
```

```json
{
  "estadoCuenta": "SUSPENDIDO"
}
```

Cambiar rol:

```http
PATCH /api/usuarios/{id}/rol
```

```json
{
  "rol": "CONTENIDO"
}
```

## Oracle y datos de prueba

Para evitar contaminar producción, el seed grande está separado de las migraciones base.

- Oracle sin seed:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=oracle
```

- Oracle con seed de demostración:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=oracle,seed
```
