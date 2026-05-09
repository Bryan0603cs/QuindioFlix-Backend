-- ============================================================================
-- 06_indices_explain.sql
-- Núcleo 4: índices y análisis con EXPLAIN PLAN.
-- El objetivo es evidenciar ANTES y DESPUÉS del índice.
-- ============================================================================

-- ============================================================================
-- 4.1 Índice compuesto para historial de reproducciones por perfil y fecha
-- ============================================================================

BEGIN
  EXECUTE IMMEDIATE 'DROP INDEX IDX_REP_PERFIL_FECHA';
EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE != -1418 THEN
      NULL;
    END IF;
END;
/

-- BEFORE: sin índice específico, el plan debería tender a TABLE ACCESS FULL
EXPLAIN PLAN FOR
SELECT p.ID_PERFIL, p.NOMBRE, c.TITULO, r.FECHA_HORA_INICIO, r.PORCENTAJE_AVANCE
FROM REPRODUCCIONES r
JOIN PERFILES p ON p.ID_PERFIL = r.ID_PERFIL
JOIN CONTENIDO c ON c.ID_CONTENIDO = r.ID_CONTENIDO
WHERE r.ID_PERFIL = 10
  AND r.FECHA_HORA_INICIO BETWEEN TO_TIMESTAMP('2024-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS')
                              AND TO_TIMESTAMP('2026-12-31 23:59:59','YYYY-MM-DD HH24:MI:SS')
ORDER BY r.FECHA_HORA_INICIO DESC;

SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY);

CREATE INDEX IDX_REP_PERFIL_FECHA ON REPRODUCCIONES(ID_PERFIL, FECHA_HORA_INICIO);

-- AFTER: con índice, el plan debería usar INDEX RANGE SCAN sobre IDX_REP_PERFIL_FECHA
EXPLAIN PLAN FOR
SELECT p.ID_PERFIL, p.NOMBRE, c.TITULO, r.FECHA_HORA_INICIO, r.PORCENTAJE_AVANCE
FROM REPRODUCCIONES r
JOIN PERFILES p ON p.ID_PERFIL = r.ID_PERFIL
JOIN CONTENIDO c ON c.ID_CONTENIDO = r.ID_CONTENIDO
WHERE r.ID_PERFIL = 10
  AND r.FECHA_HORA_INICIO BETWEEN TO_TIMESTAMP('2024-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS')
                              AND TO_TIMESTAMP('2026-12-31 23:59:59','YYYY-MM-DD HH24:MI:SS')
ORDER BY r.FECHA_HORA_INICIO DESC;

SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY);

-- ============================================================================
-- 4.2 Índice funcional para autenticación/búsqueda por correo
-- ============================================================================

BEGIN
  EXECUTE IMMEDIATE 'DROP INDEX IDX_USUARIOS_EMAIL_LOWER';
EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE != -1418 THEN
      NULL;
    END IF;
END;
/

-- BEFORE: búsqueda case-insensitive sin índice funcional
EXPLAIN PLAN FOR
SELECT *
FROM USUARIOS
WHERE LOWER(EMAIL) = LOWER('usuario1@mail.com');

SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY);

CREATE INDEX IDX_USUARIOS_EMAIL_LOWER ON USUARIOS(LOWER(EMAIL));

-- AFTER: búsqueda case-insensitive usando IDX_USUARIOS_EMAIL_LOWER
EXPLAIN PLAN FOR
SELECT *
FROM USUARIOS
WHERE LOWER(EMAIL) = LOWER('usuario1@mail.com');

SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY);

-- ============================================================================
-- 4.3 Otros índices recomendados
-- ============================================================================

CREATE INDEX IDX_CONT_CAT_ANIO ON CONTENIDO(ID_CATEGORIA, ANIO_LANZAMIENTO);
CREATE INDEX IDX_PAGO_USUARIO_FECHA ON PAGOS(ID_USUARIO, FECHA_PAGO);
CREATE INDEX IDX_CONT_TITULO_UPPER ON CONTENIDO(UPPER(TITULO));

SELECT INDEX_NAME, TABLE_NAME
FROM USER_INDEXES
WHERE INDEX_NAME IN (
  'IDX_REP_PERFIL_FECHA',
  'IDX_USUARIOS_EMAIL_LOWER',
  'IDX_CONT_CAT_ANIO',
  'IDX_PAGO_USUARIO_FECHA',
  'IDX_CONT_TITULO_UPPER'
)
ORDER BY TABLE_NAME, INDEX_NAME;
