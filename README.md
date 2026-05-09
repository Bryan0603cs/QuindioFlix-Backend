# QuindioFlix Backend

Backend REST desarrollado en **Java 21 + Spring Boot 3.3.5**, listo para trabajar con **Oracle / SQL Developer** y para ser consumido posteriormente desde un frontend en **Angular**.

El proyecto quedó reorganizado con una distribución de carpetas parecida al ejemplo enviado, pero conservando nombres de paquetes en minúscula por buenas prácticas de Java.

## Tecnologías usadas

- Java 21
- Spring Boot Web
- Spring Data JPA
- Spring Validation
- Spring Security + JWT
- Oracle JDBC `ojdbc11`
- Flyway para migraciones SQL
- H2 en modo Oracle para ejecución local rápida
- Springdoc OpenAPI / Swagger
- Lombok
- Actuator

## Estructura principal

```text
src/main/java/co/edu/uniquindio/quindioflix
├── business
│   ├── dto
│   │   ├── command             # DTO de entrada para peticiones REST
│   │   └── response            # DTO de salida para respuestas REST
│   ├── exception               # Excepciones personalizadas y handler global
│   ├── model                   # Enums y conceptos del dominio
│   └── service
│       ├── impl                # Implementaciones de los casos de uso
│       ├── util                # Utilidades de negocio
│       └── *.java              # Interfaces de servicios / casos de uso
├── configuration
│   └── security                # JWT, CORS, filtros y configuración de seguridad
├── controller                  # Controladores REST
└── persistence
    ├── dao                    # Reservado para consultas DAO personalizadas
    ├── entity                 # Entidades JPA mapeadas a Oracle
    ├── mapper                 # Conversión Entity -> DTO
    └── repository             # Repositorios Spring Data JPA
```


## Nota importante sobre IntelliJ y `.class` decompilados

Si IntelliJ muestra el mensaje `Decompiled .class file, bytecode version...`, significa que abriste un archivo compilado dentro de `target/classes` o una dependencia del repositorio Maven. No es la causa del error del backend. Para editar el código debes abrir siempre los archivos `.java` ubicados en:

```text
src/main/java/co/edu/uniquindio/quindioflix
```

La carpeta `target/` se genera automáticamente al compilar y puede eliminarse sin problema con:

```bash
mvn clean
```

## Perfiles de ejecución

El proyecto trae dos perfiles:

### 1. Perfil `dev`

Es el perfil por defecto. Usa H2 en memoria con modo Oracle para poder levantar el backend sin tener Oracle abierto.

```bash
mvn spring-boot:run
```

Swagger:

```text
http://localhost:8080/swagger-ui.html
```

Consola H2:

```text
http://localhost:8080/h2-console
```

Datos de conexión H2:

```text
JDBC URL: jdbc:h2:mem:quindioflix;MODE=Oracle;DATABASE_TO_UPPER=true;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
User: sa
Password: vacío
```

### 2. Perfil `oracle`

Este perfil se usa cuando ya tienes Oracle funcionando.

Primero crea el usuario ejecutando en SQL Developer:

```sql
database/00_crear_usuario_app.sql
```

Luego ejecuta con el perfil Oracle:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=oracle
```

También puedes configurar estas variables:

```properties
ORACLE_URL=jdbc:oracle:thin:@localhost:1521/FREEPDB1
ORACLE_USERNAME=QUINDIOFLIX_APP
ORACLE_PASSWORD=QuindioFlix_2026
```

Si aparece `The Network Adapter could not establish the connection`, no es un error del código: significa que Oracle no está escuchando en `localhost:1521`, el servicio está apagado, el listener no está activo o el nombre del PDB no coincide.

## Migraciones Flyway

Flyway ejecuta automáticamente:

- `V1__create_schema.sql`: tablas, PK, FK, checks, relaciones N:M y reflexivas.
- `V3__indexes.sql`: índices principales para rendimiento.

Los datos de prueba grandes están separados en `src/main/resources/db/seed/V99__seed_data.sql` y se cargan únicamente al activar el perfil `seed`.

## Scripts del curso incluidos

En la carpeta `database/` quedan los scripts separados para sustentar en SQL Developer:

| Archivo | Contenido |
|---|---|
| `00_crear_usuario_app.sql` | Usuario Oracle para el backend |
| `01_modelo_fisico_oracle.sql` | Modelo físico completo |
| `02_datos_prueba.sql` | Datos de prueba |
| `03_consultas_avanzadas.sql` | Parametrizadas, PIVOT, UNPIVOT, ROLLUP, CUBE, GROUPING SETS, vistas materializadas y fragmentación |
| `04_plsql.sql` | Cursores, procedimientos, funciones, excepciones y triggers |
| `05_transacciones_concurrencia.sql` | Transacciones críticas y concurrencia con `SELECT FOR UPDATE` |
| `06_indices_explain.sql` | Índices y base para `EXPLAIN PLAN` |
| `07_usuarios_roles.sql` | Roles, usuarios, privilegios y perfil de recursos |

## Endpoints principales

| Método | Endpoint | Uso |
|---|---|---|
| POST | `/api/auth/register` | Registrar usuario + perfil principal + primer pago |
| POST | `/api/auth/login` | Login con JWT |
| GET | `/api/planes` | Listar planes activos |
| GET | `/api/usuarios/{id}` | Consultar usuario |
| PATCH | `/api/usuarios/{id}/plan` | Cambiar plan validando perfiles máximos |
| POST | `/api/usuarios/{id}/perfiles` | Crear perfil |
| GET | `/api/usuarios/{id}/perfiles` | Listar perfiles del usuario |
| POST | `/api/reproducciones` | Registrar reproducción validando cuenta activa y perfil infantil |
| POST | `/api/calificaciones` | Calificar contenido, exige reproducción mínima del 50% |
| POST | `/api/favoritos` | Agregar favorito |
| DELETE | `/api/favoritos/{perfilId}/{contenidoId}` | Eliminar favorito |
| POST | `/api/pagos` | Registrar pago del usuario autenticado y activar cuenta si es exitoso |
| GET | `/api/categorias` | Listar categorías para filtros del catálogo |
| GET | `/api/generos` | Listar géneros para filtros del catálogo |
| GET | `/api/contenidos` | Listar/buscar catálogo paginado con filtros |
| GET | `/api/contenidos/{id}` | Ver detalle de contenido |
| GET | `/api/contenidos/{id}/temporadas` | Listar temporadas y episodios del contenido |
| GET | `/api/contenidos/{id}/relacionados` | Listar contenido relacionado |
| POST | `/api/contenidos` | Crear contenido del catálogo |
| POST | `/api/reportes-contenido` | Reportar contenido inapropiado |
| PATCH | `/api/reportes-contenido/{id}/resolver` | Resolver reporte con moderador/admin |
| GET | `/api/reportes/analitica/top-contenido-ciudad` | Top contenido por ciudad |
| GET | `/api/reportes/analitica/ingresos-plan` | Ingresos por plan. Usar `anio` desde Angular; también acepta `año` |
| GET | `/api/reportes/analitica/calificacion-genero` | Calificación promedio por categoría para un género |

## Validaciones implementadas en backend

- Email único.
- Plan activo y existente.
- Máximo de perfiles por plan.
- Fechas coherentes en reproducción.
- Cuenta activa para reproducir.
- Perfil infantil restringido a `TP`, `MAS_7`, `MAS_13`.
- Calificación permitida solo si el perfil reprodujo al menos el 50% del contenido.
- No duplicar favoritos ni calificaciones por perfil/contenido.
- Resolver reportes solo con usuario `MODERADOR` o `ADMIN`.
- Activación automática de cuenta cuando un pago queda `EXITOSO`.

## Prueba rápida de registro

```json
{
  "nombre": "Nuevo Usuario",
  "email": "nuevo@mail.com",
  "telefono": "3001112233",
  "fechaNacimiento": "2000-05-10",
  "ciudad": "Armenia",
  "password": "Password123",
  "planId": 1,
  "referidoPorId": null,
  "metodoPagoPrimerPago": "PSE"
}
```

## Mejoras para integración con Angular

Esta versión incluye endpoints adicionales para el frontend, paginación del catálogo, Swagger con botón Authorize, actualización de avance de reproducción, historial de pagos, historial de reproducciones, favoritos por usuario/perfil, reportes de contenido y eliminación transaccional de cuenta.

Revisa también `MEJORAS_BACKEND_FRONTEND.md`.

### Token JWT en Swagger

1. Ejecuta `POST /api/auth/login`.
2. Copia el token.
3. Presiona **Authorize** en Swagger.
4. Pega el token con el formato `Bearer TU_TOKEN`.

### Variables importantes para producción

- `JWT_SECRET`: debe cambiarse antes de desplegar.
- `SPRING_PROFILES_ACTIVE=oracle`: activa la conexión real a Oracle.
- `CORS_ALLOWED_ORIGINS=http://localhost:4200`: URL del frontend Angular.


## Mejoras finales para Angular y Oracle

- El registro de usuario ya no recibe `montoPrimerPago`; el backend calcula el valor desde el plan activo.
- Para Oracle se usa `ORACLE_USERNAME`, no `ORACLE_USER`.
- El endpoint de consumo por usuario recibe fechas ISO, por ejemplo: `2025-01-01T00:00:00`.
- `GET /api/reportes-contenido` y resolución de reportes requieren rol `ADMIN` o `MODERADOR`.
- `DELETE /api/usuarios/{id}` solo puede ejecutarlo el propio usuario o un `ADMIN`.
- `POST /api/pagos` solo permite registrar pagos del usuario autenticado, salvo que sea `ADMIN`.
- Se agregaron `GET /api/categorias`, `GET /api/generos` y `GET /api/contenidos/{id}/temporadas`.

## Correcciones finales V7 para Angular y catálogo

La versión V7 agrega las funcionalidades finales requeridas para que Angular pueda trabajar con catálogo, temporadas, episodios, calificaciones, pagos y referidos sin endpoints faltantes.

Usuarios de prueba:

| Rol | Email | Contraseña |
|---|---|---|
| ADMIN | usuario1@mail.com | Password123 |
| MODERADOR | usuario2@mail.com | Password123 |
| CONTENIDO | usuario3@mail.com | Password123 |
| CLIENTE | usuario4@mail.com | Password123 |

Endpoints agregados o reforzados:

- `PUT /api/contenidos/{id}`
- `DELETE /api/contenidos/{id}`
- `POST /api/contenidos/{id}/relacionados`
- `POST /api/contenidos/{id}/temporadas`
- `PUT /api/contenidos/{id}/temporadas/{temporadaId}`
- `DELETE /api/contenidos/{id}/temporadas/{temporadaId}`
- `GET /api/contenidos/{id}/temporadas/{temporadaId}/episodios`
- `POST /api/contenidos/{id}/temporadas/{temporadaId}/episodios`
- `PUT /api/contenidos/{id}/temporadas/{temporadaId}/episodios/{episodioId}`
- `DELETE /api/contenidos/{id}/temporadas/{temporadaId}/episodios/{episodioId}`
- `PUT /api/calificaciones/{id}`
- `DELETE /api/calificaciones/{id}`
- `GET /api/usuarios/{id}/pagos`
- `GET /api/usuarios/{id}/referidos`
- `GET /api/usuarios/{id}/referente`

Los endpoints de escritura del catálogo requieren rol `ADMIN` o `CONTENIDO`.

Para instrucciones de Angular, revisar:

```text
README_INTEGRACION_ANGULAR.md
```
