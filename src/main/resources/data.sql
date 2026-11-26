--Inserción de Roles base del sistema
INSERT INTO rol (nombre)
SELECT 'Administrador' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM rol WHERE UPPER(nombre) = 'ADMINISTRADOR');

INSERT INTO rol (nombre)
SELECT 'Bibliotecario' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM rol WHERE UPPER(nombre) = 'BIBLIOTECARIO');

INSERT INTO rol (nombre)
SELECT 'Lector' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM rol WHERE UPPER(nombre) = 'LECTOR');

-- Creación del Usuario Administrador por defecto
-- Credenciales: admin@bibliot.com / Admin12345
INSERT INTO usuario (
    nombres,
    apellidos,
    dni,
    email,
    telefono,
    direccion,
    password,
    id_rol,
    fecha_registro,
    activo
)
SELECT
    'Helena',
    'Lopez',
    '60753899',
    'admin@bibliot.com',
    '000000000',
    'Calle Falsa 123, Urbanización Demo, Arequipa',
    '$2a$10$LykiLB0Ihfyxm094btUBQubxaXSpayTHlvbg/6S2HCxCbTNezip7C',
    1,
    NOW(),
    b'1'
FROM rol r
WHERE UPPER(r.nombre) = 'ADMINISTRADOR'
  AND NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'admin@bibliot.com')
    LIMIT 1;
