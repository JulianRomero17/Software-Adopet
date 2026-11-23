# Persistencia de Adopet en PostgreSQL

Adopet guarda en PostgreSQL las cuentas registradas, mascotas, solicitudes de adopción, seguimientos y favoritos. La sesión de autenticación y la preferencia visual del navegador permanecen en el cliente; no contienen los registros de negocio.

## Conexión y arranque

Configura `DB_URL`, `DB_USERNAME` y `DB_PASSWORD` en el `.env` local, que no debe subirse al repositorio. La URL debe ser JDBC, por ejemplo `jdbc:postgresql://localhost:5432/Adopet`.

Desde la raíz del proyecto, inicia el backend con:

```powershell
mvn spring-boot:run
```

Hibernate crea o actualiza el esquema (`spring.jpa.hibernate.ddl-auto=update`). Luego Spring ejecuta `data.sql`, que carga de forma repetible las 13 mascotas del catálogo. La carga no crea ni modifica cuentas y no elimina registros existentes; los datos del antiguo catálogo solo se actualizan cuando tienen la URL de imagen de ejemplo `https://example.com/mascotas/...`.

Las mascotas incluyen `motivo_desactivacion` para conservar el motivo registrado por administración. El estado puede volver a `DISPONIBLE` al reactivar una mascota; el motivo anterior se conserva como referencia.

El esquema actual se deriva de las entidades JPA. `schema.sql` se conserva sin DDL legado para evitar crear tablas incompatibles.

## Funcionalidad respaldada por la base

- Registro e inicio de sesión de adoptantes contra las cuentas persistidas.
- Consulta, búsqueda, creación, edición y desactivación de mascotas.
- Solicitudes de adopción, incluidas las respuestas al cuestionario, decisiones y seguimientos.
- Favoritos asociados a la cuenta del adoptante.
- Panel administrativo alimentado por mascotas, solicitudes y cuentas almacenadas.

## Crear el primer administrador

El registro público crea únicamente adoptantes. Para habilitar el primer administrador, configura localmente en `.env` `ADMIN_BOOTSTRAP_ENABLED=true`, `ADMIN_NAME`, `ADMIN_DOCUMENT`, `ADMIN_EMAIL` y `ADMIN_PASSWORD` (mínimo 12 caracteres), y reinicia el backend con `mvn spring-boot:run`. El arranque guarda la contraseña con BCrypt y crea la cuenta solo si el correo aún no existe. No reutiliza cuentas existentes; si el correo ya pertenece a un adoptante, el arranque informa el conflicto sin promoverlo.

El bootstrap no sincroniza la contraseña de una cuenta existente automáticamente. Para restablecer explícitamente la contraseña del administrador identificado por `ADMIN_EMAIL`, configura temporalmente `ADMIN_BOOTSTRAP_RESET_PASSWORD=true` junto con `ADMIN_BOOTSTRAP_ENABLED=true` y la nueva `ADMIN_PASSWORD`, e inicia la aplicación una vez. Luego cambia ambas opciones a `false` y reinicia. El restablecimiento solo afecta una cuenta que ya tenga rol `ADMINISTRADOR`.

Después de confirmar que el administrador puede iniciar sesión, deja ambas opciones en `false` en `.env`. No compartas ni subas ese archivo al repositorio; `.env` está excluido por Git.

## Comprobación en PostgreSQL

En pgAdmin o `psql`, selecciona la base configurada en `DB_URL` y ejecuta:

```sql
SELECT current_database(), current_user, version();

SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'public'
  AND table_type = 'BASE TABLE'
ORDER BY table_name;

SELECT especie, count(*) AS cantidad
FROM mascotas
GROUP BY especie
ORDER BY especie;
```

Las tablas de negocio incluyen `usuarios`, `adoptantes`, `administradores`, `mascotas`, `solicitudes_adopcion`, `seguimientos` y `favoritos`. Hibernate puede crear además tablas auxiliares o de secuencia.
