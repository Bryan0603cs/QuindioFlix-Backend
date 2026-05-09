# Estructura reorganizada del backend QuindioFlix

La distribución de paquetes quedó ajustada para que sea parecida al proyecto de referencia enviado, manteniendo nombres de paquetes en minúscula por convención de Java.

```text
src/main/java/co/edu/uniquindio/quindioflix
├── business
│   ├── dto
│   │   ├── command
│   │   └── response
│   ├── exception
│   ├── model
│   └── service
│       ├── impl        # Implementaciones de servicios
│       └── util        # Utilidades de negocio
├── configuration
│   └── security
├── controller
└── persistence
    ├── dao
    ├── entity
    ├── mapper
    └── repository
```

## Equivalencia con la versión anterior

| Antes | Ahora |
|---|---|
| `application/dto` | `business/dto` |
| `application/service` | `business/service` |
| `domain/exception` | `business/exception` |
| `domain/model` | `business/model` |
| `infrastructure/security` | `configuration/security` |
| `infrastructure/persistence/entity` | `persistence/entity` |
| `infrastructure/persistence/repository` | `persistence/repository` |
| `presentation/controller` | `controller` |
| `presentation/error` | `business/exception` |

## Nota sobre ejecución

El error `The Network Adapter could not establish the connection` aparece cuando el perfil `oracle` está activo pero Oracle no está escuchando en `localhost:1521` o el PDB configurado no existe. Para probar el backend sin depender de Oracle, se dejó el perfil `dev` por defecto con H2 en modo Oracle.


## Regla de trabajo en IntelliJ

El código fuente editable está en `src/main/java`. Si IntelliJ muestra `Decompiled .class file`, se está visualizando una clase compilada de `target/classes` o una dependencia externa. Esa vista no debe editarse. Para evitar confusiones, puede ejecutarse `mvn clean` y volver a abrir las clases desde la carpeta `src`.
