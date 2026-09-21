-- Datos de ejemplo para pruebas manuales.
-- El hash BCrypt usado corresponde a la contraseña de ejemplo: password.

INSERT INTO usuarios (id, nombre, documento, correo, telefono, direccion, contrasena, rol, estado)
VALUES
    (1, 'Ana Torres', '1001001001', 'ana.torres@example.com', '3001001001', 'Calle 10 # 20-30', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADOPTANTE', TRUE),
    (2, 'Bruno Silva', '1001001002', 'bruno.silva@example.com', '3001001002', 'Carrera 15 # 40-12', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADOPTANTE', TRUE),
    (3, 'Carla Mendoza', '1001001003', 'carla.mendoza@example.com', '3001001003', 'Calle 80 # 12-05', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADOPTANTE', FALSE),
    (4, 'Diego Admin', '9009009001', 'admin@adopet.example.com', '3009009001', 'Sede principal', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMINISTRADOR', TRUE);

INSERT INTO mascotas (id, nombre, especie, raza, sexo, edad, tamano, descripcion, estado_salud, imagen_url, estado)
VALUES
    (1, 'Luna', 'PERRO', 'Labrador', 'HEMBRA', 3, 'GRANDE', 'Perra tranquila y sociable.', 'Salud estable, vacunación al día', 'https://example.com/mascotas/luna.jpg', 'DISPONIBLE'),
    (2, 'Milo', 'GATO', 'Criollo', 'MACHO', 2, 'PEQUENO', 'Gato juguetón y cariñoso.', 'Salud estable, esterilizado', 'https://example.com/mascotas/milo.jpg', 'DISPONIBLE'),
    (3, 'Toby', 'PERRO', 'Beagle', 'MACHO', 5, 'MEDIANO', 'Perro activo que disfruta los paseos.', 'En tratamiento veterinario', 'https://example.com/mascotas/toby.jpg', 'NO_DISPONIBLE'),
    (4, 'Nala', 'GATO', 'Siamés', 'HEMBRA', 4, 'PEQUENO', 'Gata independiente y afectuosa.', 'Salud estable, vacunación al día', 'https://example.com/mascotas/nala.jpg', 'ADOPTADA');

INSERT INTO solicitudes_adopcion (id, fecha, estado, observaciones, fecha_actualizacion, adoptante_id, mascota_id)
VALUES
    (1, DATE '2026-09-01', 'FINALIZADA', 'Adaptación completada satisfactoriamente.', TIMESTAMP '2026-09-15 10:30:00', 1, 4),
    (2, DATE '2026-09-10', 'PENDIENTE', 'La familia tiene experiencia con perros.', NULL, 1, 1),
    (3, DATE '2026-09-11', 'RECHAZADA', 'No cumple las condiciones de vivienda requeridas.', TIMESTAMP '2026-09-13 14:00:00', 2, 3),
    (4, DATE '2026-09-12', 'EN_REVISION', 'Se solicitó información adicional.', TIMESTAMP '2026-09-14 09:15:00', 2, 2);

INSERT INTO seguimientos (id, fecha, estado, observaciones, solicitud_id)
VALUES
    (1, DATE '2026-09-15', 'INICIAL', 'Se confirma la entrega de Nala al adoptante.', 1),
    (2, DATE '2026-09-17', 'VISITA_PROGRAMADA', 'Visita de seguimiento programada.', 1),
    (3, DATE '2026-09-13', 'RECHAZADA', 'Se notificó la decisión al solicitante.', 3),
    (4, DATE '2026-09-14', 'EN_REVISION', 'Pendiente de documentación adicional.', 4);

SELECT setval(pg_get_serial_sequence('usuarios', 'id'), (SELECT MAX(id) FROM usuarios));
SELECT setval(pg_get_serial_sequence('mascotas', 'id'), (SELECT MAX(id) FROM mascotas));
SELECT setval(pg_get_serial_sequence('solicitudes_adopcion', 'id'), (SELECT MAX(id) FROM solicitudes_adopcion));
SELECT setval(pg_get_serial_sequence('seguimientos', 'id'), (SELECT MAX(id) FROM seguimientos));
