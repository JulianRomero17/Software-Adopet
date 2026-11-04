-- Carga idempotente del catálogo de la aplicación. No crea ni modifica usuarios.
MERGE INTO mascotas AS actual
USING (VALUES
    ('Bruno', 'PERRO', 'Mestizo', 'MACHO', 0, '2 meses', 'PEQUENO', 'Cachorro cariñoso. Requiere valoración veterinaria y esquema de vacunación.', 'Pendiente de valoración veterinaria', 'Pendiente de completar', 'No aún por edad', '/images/PerrosAdopcion/PerroA1.jpg', 'DISPONIBLE'),
    ('Luna', 'PERRO', 'Maltés mestizo', 'HEMBRA', 2, '2 años', 'PEQUENO', 'Pequeña, atenta y sociable. Ideal para una familia comprometida.', 'Pendiente de confirmar', 'Pendiente de confirmar', 'Pendiente de confirmar', '/images/PerrosAdopcion/PerroA2.jpg', 'DISPONIBLE'),
    ('Toby', 'PERRO', 'Mestizo de pelo rizado', 'MACHO', 0, '3 meses', 'PEQUENO', 'Cachorro juguetón que necesita cuidados, paciencia y socialización.', 'Pendiente de valoración veterinaria', 'Pendiente de completar', 'No aún por edad', '/images/PerrosAdopcion/PerroA3.jpg', 'DISPONIBLE'),
    ('Rocky', 'PERRO', 'Mestizo tipo pitbull', 'MACHO', 4, '4 años', 'GRANDE', 'Perro adulto, fuerte y noble. Requiere una familia responsable.', 'Pendiente de valoración veterinaria', 'Pendiente de confirmar', 'Pendiente de confirmar', '/images/PerrosAdopcion/PerroA4.jpg', 'DISPONIBLE'),
    ('Maya', 'PERRO', 'Pomerania mestiza', 'HEMBRA', 0, '4 meses', 'PEQUENO', 'Cachorra pequeña, curiosa y llena de energía.', 'Pendiente de valoración veterinaria', 'Pendiente de completar', 'No aún por edad', '/images/PerrosAdopcion/PerroA5.jpg', 'DISPONIBLE'),
    ('Max', 'PERRO', 'Mestizo tipo terrier', 'MACHO', 6, '6 años', 'MEDIANO', 'Adulto tranquilo y cariñoso con las personas.', 'Pendiente de valoración veterinaria', 'Pendiente de confirmar', 'Pendiente de confirmar', '/images/PerrosAdopcion/PerroA6.jpg', 'DISPONIBLE'),
    ('Kira', 'PERRO', 'Mestiza', 'HEMBRA', 0, '4 meses', 'PEQUENO', 'Cachorra dulce y compañera. Necesita continuar su calendario veterinario.', 'Pendiente de valoración veterinaria', 'Pendiente de completar', 'No aún por edad', '/images/PerrosAdopcion/PerroA7.jpg', 'DISPONIBLE'),
    ('Michi', 'GATO', 'Doméstico de pelo corto', 'MACHO', 2, '2 años', 'PEQUENO', 'Gato curioso, tierno y amante de las caricias.', 'Pendiente de valoración veterinaria', 'Pendiente de confirmar', 'Pendiente de confirmar', '/images/GatosAdopcion/GatoA1.jpg', 'DISPONIBLE'),
    ('Simba', 'GATO', 'Doméstico de pelo corto', 'MACHO', 0, '4 meses', 'PEQUENO', 'Gatito curioso y juguetón que necesita un hogar seguro.', 'Pendiente de valoración veterinaria', 'Pendiente de completar', 'No aún por edad', '/images/GatosAdopcion/GatoA2.jpg', 'DISPONIBLE'),
    ('Nala', 'GATO', 'Doméstica de pelo corto', 'HEMBRA', 0, '2 meses', 'PEQUENO', 'Gatita bebé que requiere cuidados y seguimiento veterinario.', 'Pendiente de valoración veterinaria', 'Pendiente de completar', 'No aún por edad', '/images/GatosAdopcion/GatoA3.jpg', 'DISPONIBLE'),
    ('Oliver', 'GATO', 'Doméstico de pelo corto', 'MACHO', 0, '3 meses', 'PEQUENO', 'Gatito juguetón y muy curioso.', 'Pendiente de valoración veterinaria', 'Pendiente de completar', 'No aún por edad', '/images/GatosAdopcion/GatoA4.jpeg', 'DISPONIBLE'),
    ('Mila', 'GATO', 'Doméstica atigrada', 'HEMBRA', 5, '5 años', 'PEQUENO', 'Tranquila, elegante y afectuosa.', 'Pendiente de valoración veterinaria', 'Pendiente de confirmar', 'Pendiente de confirmar', '/images/GatosAdopcion/GatoA5.jpg', 'DISPONIBLE'),
    ('Tom', 'GATO', 'Mestizo tipo siamés', 'MACHO', 0, '3 meses', 'PEQUENO', 'Gatito amable que busca una familia paciente.', 'Pendiente de valoración veterinaria', 'Pendiente de completar', 'No aún por edad', '/images/GatosAdopcion/GatoA6.jpg', 'DISPONIBLE')
) AS catalogo(nombre, especie, raza, sexo, edad, edad_texto, tamano, descripcion, estado_salud, vacunacion, esterilizacion, imagen_url, estado)
ON lower(actual.nombre) = lower(catalogo.nombre)
   AND upper(actual.especie) = upper(catalogo.especie)
WHEN MATCHED AND actual.imagen_url LIKE 'https://example.com/mascotas/%' THEN
    UPDATE SET raza = catalogo.raza,
               sexo = catalogo.sexo,
               edad = catalogo.edad,
               edad_texto = catalogo.edad_texto,
               tamano = catalogo.tamano,
               descripcion = catalogo.descripcion,
               estado_salud = catalogo.estado_salud,
               vacunacion = catalogo.vacunacion,
               esterilizacion = catalogo.esterilizacion,
               imagen_url = catalogo.imagen_url
WHEN NOT MATCHED THEN
    INSERT (nombre, especie, raza, sexo, edad, edad_texto, tamano, descripcion, estado_salud, vacunacion, esterilizacion, imagen_url, estado)
    VALUES (catalogo.nombre, catalogo.especie, catalogo.raza, catalogo.sexo, catalogo.edad, catalogo.edad_texto, catalogo.tamano, catalogo.descripcion, catalogo.estado_salud, catalogo.vacunacion, catalogo.esterilizacion, catalogo.imagen_url, catalogo.estado);
