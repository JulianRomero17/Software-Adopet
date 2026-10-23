# Trazabilidad de requisitos — Adopet

Este documento relaciona cada requisito funcional (RF), regla de negocio (RN) y requisito no funcional (RNF) de [Adopet_EspecificacionRequisitos_FIS (2).pdf](../../Adopet_EspecificacionRequisitos_FIS%20%282%29.pdf) con su historia, responsable, estado y evidencia disponible.

Los estados usan la leyenda de [`PLAN_ADOPET.md`](PLAN_ADOPET.md#convenciones): `[x]` hecho y verificado; `[~]` parcial (indicar faltante); `[!]` bloqueado (indicar causa); `[ ]` pendiente. La presencia de código o de una configuración no demuestra por sí sola que se haya validado el sistema conectado a PostgreSQL. Las referencias a commits apuntan a cambios atribuidos por `git log` a `JulianRomero17`; la rama actual es `master`, pero la rama histórica de creación de cada commit no es verificable con Git.

## Requerimientos funcionales

| ID | Descripción corta | HU relacionada | Responsable | Estado | Evidencia o archivo/commit | Justificación si se excluye o queda fuera |
|---|---|---|---|---|---|---|
| RF-01 | Crear cuenta de adoptante con datos personales, contacto y contraseña | HU-01 | Julián / Yuber | [~] | `AuthController.java`, `AuthService.java`, `RegisterPage.jsx`; `15ae25576494a7011162f010bfced506e5fe17c8`; pruebas `AuthServiceTest` (8) | En alcance; falta probar con PostgreSQL real. |
| RF-02 | Autenticar adoptantes y administradores con credenciales válidas | HU-02 | Julián / Juan Esteban / Yuber | [~] | `AuthController.java`, `JwtUtil.java`, `SecurityConfig.java`, `AuthContext.jsx`; `15ae25576494a7011162f010bfced506e5fe17c8`; `PROVISIONAL_MODE = true` | En alcance; login backend presente, pero la interfaz opera en modo provisional y sesión persistente pendiente. |
| RF-03 | Consultar y actualizar perfil del adoptante | HU-03 | Julián / Juan Esteban | [ ] | `PLAN_ADOPET.md`, sección HU-03; endpoints y página aún pendientes | En alcance; no se excluye. |
| RF-04 | Administrador registra mascotas con sus datos y disponibilidad | HU-10 | Julián / Juan Esteban | [~] | `MascotaController.java`, `MascotaService.java`, `MascotaRequestDTO.java`, `AdminDashboardPage.jsx`; `15ae25576494a7011162f010bfced506e5fe17c8` | Endpoint y formulario existen; integración real e inventario pendiente. |
| RF-05 | Administrador modifica datos registrados de una mascota | HU-11 | Julián / Juan Esteban | [~] | `MascotaController.java`, `MascotaService.java`, `frontend/src/services/api.js`; código y commit inicial; UI de edición pendiente | En alcance; API implementada, interfaz admin pendiente. |
| RF-06 | Desactivar publicación sin borrar historial | HU-11 | Julián / Juan Esteban | [~] | `MascotaController.java`, `MascotaService.java`, `frontend/src/services/api.js`; código y commit inicial; botones admin pendientes | En alcance; falta completar y probar operación desde la interfaz. |
| RF-07 | Adoptante consulta mascotas disponibles | HU-04 | Julián / Juan Esteban | [~] | `MascotaController.java`, `CatalogPage.jsx`, `frontend/src/config.js`; commit inicial y `71629736c78ae038c7f097171240a3ff3703dc49` | API disponible; catálogo sigue en modo provisional y falta consumo real verificado. |
| RF-08 | Filtrar por especie, raza, sexo, edad, tamaño y disponibilidad | HU-05 | Julián / Juan Esteban / Yuber | [~] | `MascotaService.java`, `CatalogPage.jsx`; `15ae25576494a7011162f010bfced506e5fe17c8`; prueba de filtros contra API pendiente | En alcance; lógica y controles existen parcialmente, falta validación integrada y completar controles. |
| RF-09 | Mostrar detalle de una mascota seleccionada | HU-06 | Julián / Juan Esteban | [~] | `MascotaController.java`, `PetDetailsModal.jsx`; `71629736c78ae038c7f097171240a3ff3703dc49` | Modal presente; URL propia, estado de salud real y conexión a datos reales pendientes. |
| RF-10 | Registrar solicitud asociada a mascota disponible | HU-07 | Julián / Yuber | [~] | `SolicitudAdopcionController.java`, `SolicitudAdopcionService.java`, `CatalogPage.jsx`; commit inicial; `SolicitudAdopcionServiceTest` (7) | Backend existe; envío desde interfaz se omite en modo provisional. |
| RF-11 | Impedir solicitudes para mascotas no disponibles | HU-07 | Julián / Yuber | [x] | `SolicitudAdopcionService.java`; `SolicitudAdopcionServiceTest` (7); `15ae25576494a7011162f010bfced506e5fe17c8` | En alcance; probado por pruebas unitarias existentes; falta recorrido integrado. |
| RF-12 | Adoptante consulta sus solicitudes y estados | HU-08 | Julián / Juan Esteban / Yuber | [~] | `GET /api/solicitudes/mias`; `HistoryPage.jsx` llama `/historial`; `frontend/src/services/api.js`; commit inicial | En alcance; página no consume todavía el endpoint correcto de solicitudes. |
| RF-13 | Administrador consulta solicitudes recibidas | HU-12 | Julián / Yuber / Juan Esteban | [~] | `SolicitudAdopcionController.java` (`/pendientes`), `AdminDashboardPage.jsx`; commit inicial | Endpoint presente; panel requiere probarse con API real y completar detalle/filtros. |
| RF-14 | Administrador cambia estados Pendiente, En revisión, Aprobada, Rechazada y Finalizada | HU-13 | Julián / Yuber | [~] | `SolicitudAdopcionController.java`, `SolicitudAdopcionService.java`, `EstadoSolicitud.java`; commit inicial | Aprobada/Rechazada implementadas; En revisión desde interfaz y Finalizada pendientes. |
| RF-15 | Registrar y consultar cambios relevantes del proceso | HU-09, HU-14 | Julián / Yuber / Juan Esteban | [~] | `SeguimientoService.java`, `SeguimientoController.java`, `SolicitudAdopcionController.java`; commit inicial | Lectura y servicio existen; endpoint POST y vistas de línea de tiempo pendientes. |
| RF-16 | Cambiar mascota a Adoptada al finalizar adopción; interpretación acordada: al aprobar | HU-13, HU-15 | Julián (Product Owner) / Yuber | [~] | `SolicitudAdopcionService.decidirSolicitud`; Julián confirmó la interpretación el 2026-10-11 en `PLAN_ADOPET.md` | Interpretación confirmada; el flujo completo que verifica aprobación y estado sigue pendiente de prueba integrada. |
| RF-17 | Conservar historial de solicitudes y adopciones | HU-08, HU-15 | Julián / Juan Esteban | [~] | `SolicitudAdopcionService.java`, `UsuarioController.java`, `HistoryPage.jsx`; historial backend presente, interfaz admin y flujo Finalizada pendientes | En alcance; historial disponible parcialmente; falta acordar y probar transición final. |
| RF-18 | Administrador consulta y gestiona cuentas de adoptantes | HU-16 | Julián / Juan Esteban | [~] | `UsuarioController.java`, `UsuarioService.java`; endpoints base en commit inicial; página de usuarios pendiente | Backend parcial; interfaz admin no implementada. |
| RF-19 | Restablecer contraseña con mecanismo de verificación | HU-17 | Julián / Juan Esteban | [!] | `AuthService.java`, `AuthController.java`; `AuthService` conserva `TODO` para envío de correo | Endpoints presentes, pero recuperación completa bloqueada hasta integrar y probar correo/verificación; tokens en memoria. |
| RF-20 | Notificar al adoptante cambios relevantes de solicitud | HU-18 | Julián / Yuber | [~] | `NotificacionService.java`, llamada desde `SolicitudAdopcionService.java`; SMTP y estados adicionales pendientes | Servicio y llamada presentes; entrega real y cobertura de todos los cambios sin verificar. |

## Reglas de negocio

| ID | Descripción corta | HU relacionada | Responsable | Estado | Evidencia o archivo/commit | Justificación si se excluye o queda fuera |
|---|---|---|---|---|---|---|
| RN-01 | Mascota disponible solo con estado Disponible | HU-04, HU-07 | Julián / Juan Esteban | [x] | `MascotaService.java`, `EstadoMascota.java`; commit inicial | En alcance; implementación visible en backend. |
| RN-02 | Solo usuarios autenticados (adoptante/administrador) operan | HU-02 | Julián / Yuber | [~] | `SecurityConfig.java`, `JwtAuthenticationFilter.java`; commit inicial; pruebas de roles pendientes | Reglas presentes en código; recorrido de autorización pendiente. |
| RN-03 | No solicitar mascota Adoptada o no disponible | HU-07 | Julián / Yuber | [x] | `SolicitudAdopcionService.java`; `SolicitudAdopcionServiceTest` (7); commit inicial | En alcance; validación y rechazo de duplicados cubiertos por pruebas unitarias. |
| RN-04 | Asociar solicitud con adoptante y mascota existentes | HU-07 | Julián / Yuber | [x] | `SolicitudAdopcionService.java`, `schema.sql`; commit inicial | En alcance; entidades y relación visibles; persistencia real aún pendiente. |
| RN-05 | Solo administrador aprueba o rechaza solicitudes | HU-13 | Julián / Yuber | [~] | `SolicitudAdopcionController.java` (`@PreAuthorize`); commit inicial; prueba de roles pendiente | Restricción presente; falta demostrarla con prueba de acceso. |
| RN-06 | Cambiar mascota a Adoptada; interpretación acordada: al aprobar | HU-13, HU-15 | Julián (Product Owner) / Yuber | [~] | `SolicitudAdopcionService.decidirSolicitud`; Julián confirmó la interpretación el 2026-10-11 en `PLAN_ADOPET.md` | Interpretación confirmada; falta prueba integrada del flujo conforme a la decisión. |
| RN-07 | Conservar procesos finalizados como historial | HU-15 | Julián / Juan Esteban | [~] | `SolicitudAdopcionService.listarHistorial`; `HistoryPage.jsx`; commit inicial | El flujo y estado Finalizada no están cerrados; interfaz de historial admin pendiente. |
| RN-08 | Cada actor accede solo a funciones permitidas por rol | HU-02, HU-13, HU-16 | Julián / Yuber | [~] | `SecurityConfig.java`, `@PreAuthorize` en controladores; commit inicial; pruebas de roles pendientes | Restricciones de backend presentes; validación integrada pendiente. |

## Requerimientos no funcionales

| ID | Descripción corta | HU relacionada | Responsable | Estado | Evidencia o archivo/commit | Justificación si se excluye o queda fuera |
|---|---|---|---|---|---|---|
| RNF-01 | Interfaz clara, consistente e intuitiva | HU-01 a HU-18 | Juan Esteban / Equipo | [~] | `frontend/src/index.css`, páginas y componentes; commits `87cdda67c41375784c39897ab99ce416b82ff77a`, `71629736c78ae038c7f097171240a3ff3703dc49`, `d43ad982e53cf099b42ecd2109283ad32c5f5b57`; evaluación pendiente | En alcance; falta lista de evaluación de usabilidad, prueba con usuarios y registro de resultados. |
| RNF-02 | Consultas comunes responden en menos de 3 segundos | HU-04, HU-05, HU-08, HU-12, HU-15, HU-16 | Yuber | [ ] | Tarea QA de rendimiento en `PLAN_ADOPET.md`; sin mediciones registradas | En alcance; falta definir carga/entorno y adjuntar mediciones reproducibles. |
| RNF-03 | Contraseñas almacenadas con hash seguro, nunca texto plano | HU-01, HU-02, HU-17 | Julián | [x] | `AuthService.java` usa BCrypt; `15ae25576494a7011162f010bfced506e5fe17c8` | En alcance; implementación confirmada en código. |
| RNF-04 | Restringir funciones por rol | HU-02, HU-13, HU-16 | Julián / Yuber | [~] | `SecurityConfig.java`, filtro JWT y `@PreAuthorize`; commit inicial | Implementación presente; pruebas de roles pendientes. |
| RNF-05 | Integridad referencial de adoptantes, mascotas y solicitudes | HU-07, HU-10, HU-14 | Julián | [~] | `schema.sql`, modelos JPA; commit inicial; validación con BD pendiente | Relaciones declaradas; falta probarlas en PostgreSQL real. |
| RNF-06 | Disponibilidad durante los periodos de uso definidos | Todas | Julián / Equipo | [ ] | Tarea de disponibilidad agregada a QA en `PLAN_ADOPET.md`; periodos no definidos | En alcance; equipo debe definir periodos y evidenciar prueba de arranque, recuperación y monitoreo. |
| RNF-07 | Separación de responsabilidades que facilite cambios | Todas | Equipo completo | [~] | Paquetes `presentation`, `business`, `data`, `security`; commit inicial; revisión mantenible pendiente | Arquitectura por capas existe; falta revisión cruzada documentada. |
| RNF-08 | Agregar módulos sin rediseño completo | Todas | Julián / Equipo | [~] | Estructura por capas del backend y `frontend/src`; commit inicial; prueba de extensión pendiente | No se excluye; falta documentar módulos y validar una ampliación representativa. |
| RNF-09 | Funcionar en navegadores modernos | HU-01 a HU-18 | Juan Esteban | [ ] | Tarea de compatibilidad en QA en `PLAN_ADOPET.md`; sin matriz de navegadores | En alcance; falta ejecutar y registrar la matriz de pruebas. |
| RNF-10 | Permitir copias periódicas de la información | HU-15, HU-16 | Julián | [ ] | Tarea de respaldo en fase de BD en `PLAN_ADOPET.md`; sin evidencia de respaldos | En alcance; definir frecuencia, procedimiento de restauración y prueba. |

## Alcance excluido según la especificación

No son requisitos de esta entrega: pagos, transporte, servicios veterinarios, integraciones gubernamentales, aplicación móvil e integración con redes sociales (sección III-C de la especificación). Estas exclusiones no eliminan ninguno de los RF, RN o RNF trazados en las tablas.

## Mantenimiento de la trazabilidad

- Actualizar el estado aquí y en la tarea correspondiente del plan en la misma revisión.
- Para `[x]`, enlazar evidencia reproducible (código, hash Git, reporte o prueba).
- La interpretación de RF-16/RN-06 fue confirmada por el Product Owner el 2026-10-11; si vuelve a cambiar, actualizar conjuntamente esta tabla, [Decisiones de interpretación](PLAN_ADOPET.md#decisiones-de-interpretacion) y los criterios de aceptación relacionados.
