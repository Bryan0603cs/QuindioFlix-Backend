-- ============================================================================
-- 03_consultas_avanzadas.sql
-- QuindioFlix - Consultas avanzadas, PIVOT, UNPIVOT, ROLLUP/CUBE,
-- vistas materializadas y fragmentación física de REPRODUCCIONES.
-- ============================================================================

-- ============================================================================
-- 3.1.1.a Top 10 de contenido más reproducido por ciudad
-- ============================================================================
DEFINE ciudad = 'Armenia';

SELECT *
FROM (
    SELECT c.ID_CONTENIDO,
           c.TITULO,
           u.CIUDAD,
           COUNT(*) AS TOTAL_REPRODUCCIONES
    FROM REPRODUCCIONES r
    JOIN PERFILES p ON r.ID_PERFIL = p.ID_PERFIL
    JOIN USUARIOS u ON p.ID_USUARIO = u.ID_USUARIO
    JOIN CONTENIDO c ON r.ID_CONTENIDO = c.ID_CONTENIDO
    WHERE UPPER(u.CIUDAD) = UPPER('&ciudad')
    GROUP BY c.ID_CONTENIDO, c.TITULO, u.CIUDAD
    ORDER BY TOTAL_REPRODUCCIONES DESC
)
WHERE ROWNUM <= 10;

-- ============================================================================
-- 3.1.1.b Ingresos por plan en mes/año
-- ============================================================================
DEFINE mes = 5;
DEFINE anio = 2026;

SELECT pl.NOMBRE AS PLAN,
       EXTRACT(MONTH FROM pg.FECHA_PAGO) AS MES,
       EXTRACT(YEAR FROM pg.FECHA_PAGO) AS ANIO,
       SUM(pg.MONTO) AS INGRESOS
FROM PAGOS pg
JOIN USUARIOS u ON pg.ID_USUARIO = u.ID_USUARIO
JOIN PLANES pl ON u.ID_PLAN = pl.ID_PLAN
WHERE pg.ESTADO = 'EXITOSO'
  AND EXTRACT(MONTH FROM pg.FECHA_PAGO) = &mes
  AND EXTRACT(YEAR FROM pg.FECHA_PAGO) = &anio
GROUP BY pl.NOMBRE, EXTRACT(MONTH FROM pg.FECHA_PAGO), EXTRACT(YEAR FROM pg.FECHA_PAGO)
ORDER BY INGRESOS DESC;

-- ============================================================================
-- 3.1.1.c Calificación promedio por categoría para un género
-- ============================================================================
DEFINE genero = 'Accion';

SELECT ca.NOMBRE AS CATEGORIA,
       g.NOMBRE AS GENERO,
       ROUND(AVG(cal.ESTRELLAS), 2) AS PROMEDIO_ESTRELLAS,
       COUNT(cal.ID_CALIFICACION) AS TOTAL_CALIFICACIONES
FROM CALIFICACIONES cal
JOIN CONTENIDO c ON cal.ID_CONTENIDO = c.ID_CONTENIDO
JOIN CATEGORIAS ca ON c.ID_CATEGORIA = ca.ID_CATEGORIA
JOIN CONTENIDO_GENERO cg ON c.ID_CONTENIDO = cg.ID_CONTENIDO
JOIN GENEROS g ON cg.ID_GENERO = g.ID_GENERO
WHERE UPPER(g.NOMBRE) = UPPER('&genero')
GROUP BY ca.NOMBRE, g.NOMBRE
ORDER BY PROMEDIO_ESTRELLAS DESC;

-- ============================================================================
-- 3.1.2 PIVOT: usuarios activos por ciudad y plan
-- ============================================================================
SELECT *
FROM (
    SELECT u.CIUDAD, p.NOMBRE AS PLAN
    FROM USUARIOS u
    JOIN PLANES p ON u.ID_PLAN = p.ID_PLAN
    WHERE u.ESTADO_CUENTA = 'ACTIVO'
)
PIVOT (
    COUNT(PLAN)
    FOR PLAN IN (
        'BASICO' AS BASICO,
        'ESTANDAR' AS ESTANDAR,
        'PREMIUM' AS PREMIUM
    )
)
ORDER BY CIUDAD;

-- ============================================================================
-- 3.1.2 PIVOT: reproducciones por categoría y dispositivo
-- ============================================================================
SELECT *
FROM (
    SELECT ca.NOMBRE AS CATEGORIA,
           r.DISPOSITIVO
    FROM REPRODUCCIONES r
    JOIN CONTENIDO c ON r.ID_CONTENIDO = c.ID_CONTENIDO
    JOIN CATEGORIAS ca ON c.ID_CATEGORIA = ca.ID_CATEGORIA
)
PIVOT (
    COUNT(DISPOSITIVO)
    FOR DISPOSITIVO IN (
        'CELULAR' AS CELULAR,
        'TABLET' AS TABLET,
        'TV' AS TV,
        'COMPUTADOR' AS COMPUTADOR
    )
)
ORDER BY CATEGORIA;

-- ============================================================================
-- 3.1.3 UNPIVOT: normalizar resumen de usuarios por plan
-- ============================================================================
WITH RESUMEN AS (
    SELECT *
    FROM (
        SELECT u.CIUDAD, p.NOMBRE AS PLAN
        FROM USUARIOS u
        JOIN PLANES p ON u.ID_PLAN = p.ID_PLAN
        WHERE u.ESTADO_CUENTA = 'ACTIVO'
    )
    PIVOT (
        COUNT(PLAN)
        FOR PLAN IN (
            'BASICO' AS BASICO,
            'ESTANDAR' AS ESTANDAR,
            'PREMIUM' AS PREMIUM
        )
    )
)
SELECT CIUDAD, PLAN, CANTIDAD
FROM RESUMEN
UNPIVOT (
    CANTIDAD FOR PLAN IN (
        BASICO AS 'BASICO',
        ESTANDAR AS 'ESTANDAR',
        PREMIUM AS 'PREMIUM'
    )
)
ORDER BY CIUDAD, PLAN;

-- ============================================================================
-- 3.1.3 ROLLUP: ingresos por ciudad y plan
-- ============================================================================
SELECT
    CASE WHEN GROUPING(u.CIUDAD) = 1 THEN 'TOTAL GENERAL' ELSE u.CIUDAD END AS CIUDAD,
    CASE WHEN GROUPING(p.NOMBRE) = 1 THEN 'TOTAL CIUDAD' ELSE p.NOMBRE END AS PLAN,
    SUM(pg.MONTO) AS INGRESOS
FROM PAGOS pg
JOIN USUARIOS u ON pg.ID_USUARIO = u.ID_USUARIO
JOIN PLANES p ON u.ID_PLAN = p.ID_PLAN
WHERE pg.ESTADO = 'EXITOSO'
GROUP BY ROLLUP(u.CIUDAD, p.NOMBRE)
ORDER BY u.CIUDAD, p.NOMBRE;

-- ============================================================================
-- 3.1.3 CUBE: reproducciones por categoría y dispositivo
-- ============================================================================
SELECT
    CASE WHEN GROUPING(ca.NOMBRE) = 1 THEN 'TODAS LAS CATEGORIAS' ELSE ca.NOMBRE END AS CATEGORIA,
    CASE WHEN GROUPING(r.DISPOSITIVO) = 1 THEN 'TODOS LOS DISPOSITIVOS' ELSE r.DISPOSITIVO END AS DISPOSITIVO,
    COUNT(*) AS TOTAL_REPRODUCCIONES
FROM REPRODUCCIONES r
JOIN CONTENIDO c ON r.ID_CONTENIDO = c.ID_CONTENIDO
JOIN CATEGORIAS ca ON c.ID_CATEGORIA = ca.ID_CATEGORIA
GROUP BY CUBE(ca.NOMBRE, r.DISPOSITIVO)
ORDER BY CATEGORIA, DISPOSITIVO;

-- ============================================================================
-- 3.1.3 GROUPING SETS: consumo por categoría y ciudad
-- ============================================================================
SELECT ca.NOMBRE AS CATEGORIA,
       u.CIUDAD,
       COUNT(*) AS TOTAL_REPRODUCCIONES
FROM REPRODUCCIONES r
JOIN PERFILES p ON r.ID_PERFIL = p.ID_PERFIL
JOIN USUARIOS u ON p.ID_USUARIO = u.ID_USUARIO
JOIN CONTENIDO c ON r.ID_CONTENIDO = c.ID_CONTENIDO
JOIN CATEGORIAS ca ON c.ID_CATEGORIA = ca.ID_CATEGORIA
GROUP BY GROUPING SETS (
    (ca.NOMBRE),
    (u.CIUDAD),
    (ca.NOMBRE, u.CIUDAD)
)
ORDER BY ca.NOMBRE, u.CIUDAD;

-- ============================================================================
-- 3.1.4 Vistas materializadas requeridas
-- ============================================================================

BEGIN
    EXECUTE IMMEDIATE 'DROP MATERIALIZED VIEW MV_CONTENIDO_POPULAR';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -12003 AND SQLCODE != -942 THEN
            RAISE;
        END IF;
END;
/

CREATE MATERIALIZED VIEW MV_CONTENIDO_POPULAR
BUILD IMMEDIATE
REFRESH COMPLETE ON DEMAND
AS
SELECT c.ID_CONTENIDO,
       c.TITULO,
       ca.NOMBRE AS CATEGORIA,
       COUNT(r.ID_REPRODUCCION) AS TOTAL_REPRODUCCIONES,
       ROUND(AVG(cal.ESTRELLAS), 2) AS CALIFICACION_PROMEDIO,
       SUM(CASE WHEN r.PORCENTAJE_AVANCE >= 90 THEN 1 ELSE 0 END) AS REPRODUCCIONES_COMPLETAS
FROM CONTENIDO c
JOIN CATEGORIAS ca ON c.ID_CATEGORIA = ca.ID_CATEGORIA
LEFT JOIN REPRODUCCIONES r ON c.ID_CONTENIDO = r.ID_CONTENIDO
LEFT JOIN CALIFICACIONES cal ON c.ID_CONTENIDO = cal.ID_CONTENIDO
GROUP BY c.ID_CONTENIDO, c.TITULO, ca.NOMBRE;

BEGIN
    EXECUTE IMMEDIATE 'DROP MATERIALIZED VIEW MV_INGRESOS_MENSUALES';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -12003 AND SQLCODE != -942 THEN
            RAISE;
        END IF;
END;
/

CREATE MATERIALIZED VIEW MV_INGRESOS_MENSUALES
BUILD IMMEDIATE
REFRESH COMPLETE ON DEMAND
AS
SELECT EXTRACT(YEAR FROM pg.FECHA_PAGO) AS ANIO,
       EXTRACT(MONTH FROM pg.FECHA_PAGO) AS MES,
       u.CIUDAD,
       pl.NOMBRE AS PLAN,
       SUM(pg.MONTO) AS INGRESOS,
       COUNT(pg.ID_PAGO) AS TOTAL_PAGOS
FROM PAGOS pg
JOIN USUARIOS u ON pg.ID_USUARIO = u.ID_USUARIO
JOIN PLANES pl ON u.ID_PLAN = pl.ID_PLAN
WHERE pg.ESTADO = 'EXITOSO'
GROUP BY EXTRACT(YEAR FROM pg.FECHA_PAGO),
         EXTRACT(MONTH FROM pg.FECHA_PAGO),
         u.CIUDAD,
         pl.NOMBRE;

-- Verificación de vistas materializadas
SELECT MVIEW_NAME, BUILD_MODE, REFRESH_METHOD, REFRESH_MODE
FROM USER_MVIEWS
WHERE MVIEW_NAME IN ('MV_CONTENIDO_POPULAR', 'MV_INGRESOS_MENSUALES')
ORDER BY MVIEW_NAME;

BEGIN
    DBMS_MVIEW.REFRESH('MV_CONTENIDO_POPULAR', 'C');
    DBMS_MVIEW.REFRESH('MV_INGRESOS_MENSUALES', 'C');
END;
/

SELECT * FROM MV_CONTENIDO_POPULAR FETCH FIRST 10 ROWS ONLY;
SELECT * FROM MV_INGRESOS_MENSUALES FETCH FIRST 10 ROWS ONLY;

-- ============================================================================
-- 3.1.5 Fragmentación física de REPRODUCCIONES por rango de fechas
-- ============================================================================
-- Justificación:
-- REPRODUCCIONES es la tabla transaccional de mayor crecimiento. Fragmentarla por
-- fecha permite consultar historial por periodos, simplificar mantenimiento y ubicar
-- datos históricos en tablespaces/datafiles separados.
--
-- Nota de ejecución:
-- CREATE TABLESPACE requiere privilegios DBA y una ruta válida para DATAFILE.
-- Ajuste la ruta de los datafiles según su instalación Oracle antes de ejecutar.

CREATE TABLESPACE TS_REP_2024
DATAFILE 'ts_rep_2024.dbf' SIZE 50M AUTOEXTEND ON NEXT 10M MAXSIZE 500M;

CREATE TABLESPACE TS_REP_2025
DATAFILE 'ts_rep_2025.dbf' SIZE 50M AUTOEXTEND ON NEXT 10M MAXSIZE 500M;

CREATE TABLESPACE TS_REP_FUTURO
DATAFILE 'ts_rep_futuro.dbf' SIZE 50M AUTOEXTEND ON NEXT 10M MAXSIZE 500M;

-- Ejemplo de tabla fragmentada. En una migración real se debe crear antes de insertar datos
-- o migrar con DBMS_REDEFINITION para no perder información.
CREATE TABLE REPRODUCCIONES_PARTICIONADA (
  ID_REPRODUCCION NUMBER(19,0) NOT NULL,
  ID_PERFIL NUMBER(19,0) NOT NULL,
  ID_CONTENIDO NUMBER(19,0) NOT NULL,
  ID_EPISODIO NUMBER(19,0),
  FECHA_HORA_INICIO TIMESTAMP NOT NULL,
  FECHA_HORA_FIN TIMESTAMP,
  DISPOSITIVO VARCHAR2(20) NOT NULL,
  PORCENTAJE_AVANCE NUMBER(5,2) DEFAULT 0 NOT NULL,
  CONSTRAINT PK_REP_PART PRIMARY KEY (ID_REPRODUCCION, FECHA_HORA_INICIO),
  CONSTRAINT CK_REP_PART_DISP CHECK (DISPOSITIVO IN ('CELULAR','TABLET','TV','COMPUTADOR')),
  CONSTRAINT CK_REP_PART_AVANCE CHECK (PORCENTAJE_AVANCE BETWEEN 0 AND 100)
)
PARTITION BY RANGE (FECHA_HORA_INICIO) (
  PARTITION REP_2024 VALUES LESS THAN (TIMESTAMP '2025-01-01 00:00:00') TABLESPACE TS_REP_2024,
  PARTITION REP_2025 VALUES LESS THAN (TIMESTAMP '2026-01-01 00:00:00') TABLESPACE TS_REP_2025,
  PARTITION REP_FUTURO VALUES LESS THAN (MAXVALUE) TABLESPACE TS_REP_FUTURO
);

-- Verificación de fragmentación
SELECT TABLE_NAME, PARTITION_NAME, TABLESPACE_NAME, HIGH_VALUE
FROM USER_TAB_PARTITIONS
WHERE TABLE_NAME = 'REPRODUCCIONES_PARTICIONADA'
ORDER BY PARTITION_POSITION;
