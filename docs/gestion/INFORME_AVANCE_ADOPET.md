# Informe de avance — Adopet

**Fecha:** 27 de octubre de 2026  
**Propósito:** resumen para presentar el estado del proyecto y los avances recientes.  
**Alcance de este avance:** funcionalidades que pueden desarrollarse y verificarse localmente, sin conectar PostgreSQL.

## Resumen ejecutivo

Adopet es una aplicación web para gestionar adopciones de mascotas. Cuenta con un frontend en React y Vite y un backend en Spring Boot organizado por capas. El backend ya tiene una base funcional para autenticación, roles, mascotas, solicitudes, historial y consulta de seguimiento.

En este avance se completaron mejoras del registro y de la sesión, y se amplió la gestión administrativa del inventario de mascotas. También se mejoraron los filtros del catálogo y la información de la ficha. El frontend compila para producción y pasaron 20 pruebas unitarias del backend.

**La integración final con PostgreSQL todavía no se ha realizado.** La aplicación conserva el modo provisional, por lo que las pruebas de build y de servicios no demuestran que los datos persistan o que el flujo completo funcione conectado a la base de datos.

## Avances recientes

### Registro y sesión

- Se añadieron validaciones en el formulario de registro: formato de correo, documento numérico de 6 a 15 dígitos, contraseña de al menos 8 caracteres y confirmación de contraseña.
- La sesión se conserva al recargar la página y no se guarda la contraseña en el almacenamiento local.
- Si el token vence —por su fecha de expiración o por una respuesta HTTP 401 del API— se limpia la sesión y se muestra un aviso para volver a iniciar sesión.

### Gestión de mascotas

- El inventario administrativo puede listar mascotas en distintos estados; el catálogo del adoptante mantiene la restricción a mascotas disponibles.
- Se puede abrir el formulario con los datos actuales para editar una mascota.
- La desactivación solicita confirmación y utiliza la operación correspondiente tanto en modo provisional como en modo API.
- El formulario muestra una vista previa de la imagen y el estado inicial. También requiere campos clave como el nombre y el estado de salud y valida la edad y la URL de imagen.

### Catálogo y ficha

- Se añadieron listas desplegables para filtrar por especie y tamaño.
- El botón para limpiar filtros ahora ejecuta la búsqueda con los filtros vacíos.
- La ficha de la mascota presenta los datos disponibles de salud, vacunación y esterilización; si un dato no existe, lo indica en vez de mostrar una afirmación fija.

### Pruebas y documentación

- Se añadió una prueba unitaria de `MascotaService` para verificar el listado completo usado por el inventario.
- Se actualizaron el plan de trabajo y el registro de cambios con las tareas y evidencias de este avance.

## Verificación realizada

| Verificación | Resultado |
|---|---|
| Build de producción del frontend (`npm run build`, ejecutado desde `frontend/`) | Correcto |
| Pruebas unitarias de `AuthService` | 8 aprobadas |
| Pruebas unitarias de `SolicitudAdopcionService` | 7 aprobadas |
| Pruebas unitarias de `UsuarioService` | 4 aprobadas |
| Pruebas unitarias de `MascotaService` | 1 aprobada |
| Total de pruebas unitarias ejecutadas en estas suites | 20 aprobadas |
| Revisión de formato del diff (`git diff --check`) | Sin errores |
| Prueba de integración con Testcontainers/PostgreSQL | No ejecutada en este avance |

Las pruebas unitarias ejercitan servicios con dependencias simuladas; no sustituyen una prueba de integración ni validan la conexión con PostgreSQL.

## Qué queda pendiente

- Conectar el backend a PostgreSQL y verificar la creación/carga del esquema y los datos de ejemplo.
- Probar registro, login, catálogo, filtros, edición de mascotas y solicitudes contra la API conectada.
- Desactivar el modo provisional solo cuando la integración esté comprobada.
- Completar el perfil del adoptante y las pantallas de recuperación de contraseña; el envío de correo y SMTP también deben configurarse y probarse.
- Completar las operaciones e interfaces pendientes de seguimiento, cierre de solicitudes, historial administrativo y gestión de usuarios.
- Ampliar las pruebas unitarias de mascotas y ejecutar la prueba integrada. La prueba existente con Testcontainers requiere un entorno Docker válido.

## Explicación breve para presentar

> “En este avance trabajamos en funcionalidades que no requerían todavía la conexión a PostgreSQL. Completamos validaciones del registro y mejoramos la sesión para que se conserve al recargar y se cierre correctamente si vence el token. También terminamos la gestión básica del inventario administrativo, incluyendo edición, desactivación con confirmación y vista previa de imágenes. Mejoramos los filtros y la información de la ficha de cada mascota. Verificamos que el frontend genere su build de producción y que pasen 20 pruebas unitarias del backend. La conexión con PostgreSQL y la validación del flujo completo quedan como siguiente etapa; por eso todavía no presentamos la aplicación como integrada ni como entrega final.”

## Evidencia y seguimiento

- [Plan de trabajo Adopet](./PLAN_ADOPET.md)
- [Registro de cambios Adopet](./REGISTRO_CAMBIOS_ADOPET.md)
- Código de prueba: `src/test/java/com/adopet/business/services/MascotaServiceTest.java`
- Estado al redactar: cambios locales sin commit; no se atribuye individualmente todo el avance a una sola persona.
