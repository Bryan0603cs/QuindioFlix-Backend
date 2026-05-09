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
| GET | `/api/reproducciones?perfilId=1&page=0&size=20` | Historial paginado de reproducción del perfil autenticado |
| POST | `/api/reproducciones` | Registrar reproducción |
| PATCH | `/api/reproducciones/{id}` | Actualizar avance |
| GET | `/api/favoritos/perfil/{perfilId}?page=0&size=20` | Favoritos paginados de un perfil |
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
| POST | `/api/pagos` | Registrar pago exitoso del usuario autenticado |

El backend toma el usuario desde el JWT, calcula internamente el monto desde el plan, registra el pago como `EXITOSO` y aplica el descuento por referido cuando corresponde. El frontend no envía `usuarioId` ni `estadoPago` en el body.

JSON de pago:

```json
{
  "metodoPago": "PSE",
  "referencia": "PAGO-ANGULAR-001"
}
```

### Reportes y analítica

| Método | Ruta | Uso |
|---|---|---|
| POST | `/api/reportes-contenido` | Crear reporte de contenido inapropiado |
| GET | `/api/reportes-contenido?estado=PENDIENTE&page=0&size=20` | Listar reportes paginados, ADMIN o MODERADOR |
| PATCH | `/api/reportes-contenido/{id}/resolver` | Resolver reporte |
| GET | `/api/reportes/analitica/top-contenido-ciudad` | Top por ciudad |
| GET | `/api/reportes/analitica/ingresos-plan` | Ingresos por plan |
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
