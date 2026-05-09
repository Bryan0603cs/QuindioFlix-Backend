# Correcciones finales V12 — consolidación sin romper lógica anterior

Esta versión parte de la V11 y corrige los hallazgos nuevos sin eliminar la funcionalidad previa del backend.

## Críticos corregidos

1. **Flyway sin versiones duplicadas**
   - Se eliminó el archivo fantasma `V2__indexes.sql`.
   - Se separó el seed masivo del directorio de migraciones de producción.
   - Migraciones base activas: `V1__create_schema.sql` y `V3__indexes.sql`.
   - Seed opcional: `db/seed/V99__seed_data.sql` con perfil `seed`.

2. **Bloqueo explícito de cuentas en login**
   - `AppUserDetailsService` ahora lanza:
     - `DisabledException` para cuentas `INACTIVO`.
     - `LockedException` para cuentas `SUSPENDIDO`.
   - Esto garantiza que el login quede bloqueado aunque cambie el comportamiento interno del `DaoAuthenticationProvider`.

3. **Analítica de calificación por género implementada**
   - Se agregó:
     - `GET /api/reportes/analitica/calificacion-genero?genero=Acción`
   - Responde promedio de calificaciones por categoría para el género seleccionado.
   - Cubre el requisito de consultas avanzadas del proyecto.

## Pendientes medios corregidos

4. **PATCH de estado y rol ahora usan body JSON**

`PATCH /api/usuarios/{id}/estado`

```json
{
  "estadoCuenta": "SUSPENDIDO"
}
```

`PATCH /api/usuarios/{id}/rol`

```json
{
  "rol": "CONTENIDO"
}
```

5. **Empleados paginados para Angular**

```http
GET /api/empleados?page=0&size=20
```

6. **Eliminación individual de perfiles**

```http
DELETE /api/perfiles/{id}
```

- Borra reproducciones, calificaciones y favoritos del perfil.
- No permite eliminar el último perfil de una cuenta.

7. **Parámetro de año compatible**

`GET /api/reportes/analitica/ingresos-plan` acepta ambos formatos:

```http
/api/reportes/analitica/ingresos-plan?mes=5&anio=2026
/api/reportes/analitica/ingresos-plan?mes=5&año=2026
```

Se recomienda usar `anio` desde Angular para evitar problemas de encoding.

## Ejecución recomendada

### Desarrollo local H2

```bash
mvn clean spring-boot:run
```

### Oracle sin datos de prueba

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=oracle
```

### Oracle con datos de prueba

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=oracle,seed
```
