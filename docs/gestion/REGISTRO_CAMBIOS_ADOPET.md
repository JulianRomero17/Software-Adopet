# Registro de cambios — Adopet

Este archivo reúne los informes de trabajo del proyecto. Se debe actualizar cada vez que una persona complete, corrija o pruebe una tarea definida en [`PLAN_ADOPET.md`](PLAN_ADOPET.md).

No se deben eliminar entradas anteriores. Si una prueba cambia de resultado, se agrega una entrada nueva que explique la corrección.

## Reglas del registro

- Usar únicamente `Julián`, `Juan Esteban` o `Yuber` como responsable.
- Registrar fecha y hora de Colombia.
- No incluir `DB_PASSWORD`, `JWT_SECRET`, credenciales SMTP ni otros secretos.
- Los logs y capturas deben ocultar credenciales antes de adjuntarse.
- Usar los estados del plan: `[x]` hecho y verificado, `[~]` parcial, `[!]` bloqueado y `[ ]` pendiente.
- Una tarea solo se marca como `[x]` cuando tiene evidencia reproducible; separar en entradas distintas lo completado, lo parcial y lo bloqueado.
- Si una tarea queda bloqueada, registrar la causa exacta y lo necesario para desbloquearla.
- Cada entrada debe incluir la rama confirmada con Git y el hash del commit, o indicar `No verificable con git log`. No inferir la rama histórica solo por la rama actual.

---

## Plantilla de informe individual

Copiar esta plantilla debajo de `Entradas cronológicas` por cada avance verificable:

```md
### [AAAA-MM-DD HH:MM] — Responsable: Nombre

- Estado: [x] Hecho y verificado / [~] Parcial / [!] Bloqueado / [ ] Pendiente
- Fecha planificada:
- Fecha real:
- Dependencias:
- Área: backend / frontend / base de datos / seguridad / pruebas / documentación técnica
- Historia o requisito: HU-__ / RF-__
- Tarea o problema:
- Qué se hizo:
- Endpoints o pantallas involucrados:
- Archivos creados o modificados:
- Configuración utilizada, sin secretos:
- Rama: No verificable con git log (registrar solo después de confirmar con `git branch`)
- Commit/hash:
- Cómo se probó:
- Resultado esperado:
- Resultado obtenido:
- Evidencia: captura, video, log, comando o enlace local
- Riesgos, pendientes o reversión necesaria:
- Entrega a la siguiente persona:
```

---

## Plantilla de incidencia o bloqueo

Usar esta plantilla cuando el problema impida continuar:

```md
### [AAAA-MM-DD HH:MM] — Bloqueo reportado por Nombre

- Estado: [!] Bloqueado
- Componente:
- Paso exacto que falla:
- Mensaje de error completo, sin secretos:
- Última prueba que sí funcionó:
- Entorno: sistema operativo, versión de Java, Node y PostgreSQL, puertos sin credenciales
- Evidencia:
- Posible causa:
- Acción necesaria para desbloquear:
- Persona o tarea dependiente afectada:
```

---

## Entradas cronológicas

<!-- Esta es la línea base de commits de JulianRomero17 que Git permite atribuir. El estado de la rama al crear cada commit no queda guardado en el objeto commit; master es la rama actual verificada y contiene estos commits. No se puede inferir más autoría o fecha que la indicada por git log. -->

### [2026-09-21 14:10:32] — Responsable: Julián

- Estado: [~] Parcial
- Fecha planificada: No verificable con git log.
- Fecha real: 2026-09-21 14:10:32 (fecha/hora del commit).
- Dependencias: No verificable con git log.
- Área: backend / seguridad / base de datos / pruebas
- Historia o requisito: HU-01, HU-02, HU-07, HU-10, HU-11, HU-12, HU-13, HU-14 (parcial), HU-15, HU-16, HU-17 (parcial), HU-18 (parcial); RF-01, RF-02, RF-04 a RF-06, RF-10 a RF-20
- Tarea o problema: Línea base del backend Adopet y scripts de base de datos.
- Qué se hizo: Git registra la creación de la aplicación Spring Boot y sus capas, autenticación JWT/roles, servicios, controladores, DTOs, configuración y pruebas. El código actual confirma los endpoints base de autenticación, mascotas, solicitudes, usuarios, historial y lectura de seguimiento. No están completos el perfil, POST de seguimiento, Finalizada ni el envío real de recuperación.
- Endpoints o pantallas involucrados: `/api/auth/*`, `/api/mascotas`, `/api/solicitudes/*`, `/api/historial`, `/api/usuarios/*`
- Archivos creados o modificados: `pom.xml`; `src/main/java/com/adopet/AdopetApplication.java`; `src/main/java/com/adopet/business/services/AuthService.java`, `MascotaService.java`, `SolicitudAdopcionService.java`, `SeguimientoService.java`, `UsuarioService.java`, `NotificacionService.java`; `src/main/java/com/adopet/presentation/controllers/AuthController.java`, `MascotaController.java`, `SolicitudAdopcionController.java`, `SeguimientoController.java`, `UsuarioController.java`; `src/main/resources/application.properties`, `schema.sql`, `data.sql`. Inventario completo: `git show --name-only 15ae25576494a7011162f010bfced506e5fe17c8`.
- Configuración utilizada, sin secretos: `application.properties` declara PostgreSQL en localhost:5432/adopet_db, `ddl-auto=update` y `spring.sql.init.mode=always`; no es evidencia de conexión probada.
- Rama: `master` es la rama actual y contiene este commit; rama histórica al crear el commit: No verificable con git log.
- Commit/hash: `15ae25576494a7011162f010bfced506e5fe17c8`
- Cómo se probó: no se atribuye una ejecución de pruebas a este commit sin log de ejecución fechado. Los reportes de la línea base actual se anotan separadamente en la evidencia de QA.
- Resultado esperado: backend por capas con endpoints y base de datos preparados.
- Resultado obtenido: estructura y código base presentes; funcionalidades parciales listadas arriba y conexión real a PostgreSQL pendiente.
- Evidencia: `git show --stat 15ae25576494a7011162f010bfced506e5fe17c8`; archivos del commit; estado actual de `application.properties`.
- Riesgos, pendientes o reversión necesaria: perfil, cierre Finalizada, POST de seguimiento, recuperación por correo, validación del modo de inicialización y conexión PostgreSQL pendientes.
- Entrega a la siguiente persona: No verificable con git log.

### [2026-09-21 14:10:32] — Responsable: Julián

- Estado: [~] Parcial
- Fecha planificada: No verificable con git log.
- Fecha real: 2026-09-21 14:10:32 (fecha/hora del commit).
- Dependencias: No verificable con git log.
- Área: frontend
- Historia o requisito: HU-01, HU-02, HU-04, HU-06, HU-08, HU-10, HU-12, HU-13
- Tarea o problema: Interfaz inicial React y consumo de servicios.
- Qué se hizo: Git registra la aplicación inicial, rutas/pantallas, componentes y cliente API. El código actual conserva `PROVISIONAL_MODE = true`; la pantalla llamada "Mis solicitudes" consume `/historial`, no `/solicitudes/mias`.
- Endpoints o pantallas involucrados: registro, login, catálogo, detalle/modal, panel admin, historial, navegación y `/api`.
- Archivos creados o modificados: `frontend/package.json`, `frontend/src/App.jsx`, `frontend/src/components/AppShell.jsx`, `PetCard.jsx`, `ProtectedRoute.jsx`, `StatusPill.jsx`, `Toast.jsx`, `frontend/src/context/AuthContext.jsx`, `frontend/src/pages/AdminDashboardPage.jsx`, `CatalogPage.jsx`, `HistoryPage.jsx`, `LoginPage.jsx`, `RegisterPage.jsx`, `frontend/src/services/api.js`, `frontend/vite.config.js`. Inventario completo: `git show --name-only 15ae25576494a7011162f010bfced506e5fe17c8`.
- Configuración utilizada, sin secretos: React/Vite; el proxy `/api` está declarado en `frontend/vite.config.js`.
- Rama: `master` es la rama actual y contiene este commit; rama histórica al crear el commit: No verificable con git log.
- Commit/hash: `15ae25576494a7011162f010bfced506e5fe17c8`
- Cómo se probó: creación de archivos visible en Git; no hay evidencia fechada de prueba de interfaz para ese commit.
- Resultado esperado: esqueleto de interfaz y navegación para los flujos principales.
- Resultado obtenido: vistas base presentes; integración completa con API y flujo de solicitudes no verificados y con pendientes identificados.
- Evidencia: `git show --stat 15ae25576494a7011162f010bfced506e5fe17c8`; código actual de `frontend/src/config.js`, `frontend/src/pages/HistoryPage.jsx` y `frontend/src/services/api.js`.
- Riesgos, pendientes o reversión necesaria: modo provisional activo, sesión no persistente y "Mis solicitudes" mal conectada al historial.
- Entrega a la siguiente persona: tareas UI e integración pendientes asignadas en el plan.

### [2026-09-21 14:36:10] — Responsable: Julián

- Estado: [x] Hecho y verificado
- Fecha planificada: No verificable con git log.
- Fecha real: 2026-09-21 14:36:10 (fecha/hora del commit).
- Dependencias: No verificable con git log.
- Área: frontend
- Historia o requisito: HU-04, HU-06
- Tarea o problema: Página principal inicial.
- Qué se hizo: se añadió la página `Home` y cambios relacionados de presentación.
- Endpoints o pantallas involucrados: landing y componente de tarjeta de mascota.
- Archivos creados o modificados: `frontend/src/App.jsx`, `frontend/src/components/MascotaCard.jsx`, `frontend/src/pages/Home.jsx`.
- Configuración utilizada, sin secretos: React/Vite.
- Rama: `master` es la rama actual y contiene este commit; rama histórica al crear el commit: No verificable con git log.
- Commit/hash: `87cdda67c41375784c39897ab99ce416b82ff77a`
- Cómo se probó: verificación del cambio registrado por Git y de los archivos presentes.
- Resultado esperado: página principal disponible en la aplicación.
- Resultado obtenido: implementación de `Home` presente; prueba visual en navegador para ese commit: No verificable con git log.
- Evidencia: `git show --stat 87cdda67c41375784c39897ab99ce416b82ff77a`.
- Riesgos, pendientes o reversión necesaria: destacados y catálogo deben conectarse a datos reales.
- Entrega a la siguiente persona: integración de la landing con el catálogo real pendiente.

### [2026-09-28 10:36:10] — Responsable: Julián

- Estado: [x] Hecho y verificado
- Fecha planificada: No verificable con git log.
- Fecha real: 2026-09-28 10:36:10 (fecha/hora del commit).
- Dependencias: No verificable con git log.
- Área: frontend
- Historia o requisito: HU-02
- Tarea o problema: completar el punto de entrada de la aplicación frontend.
- Qué se hizo: Git registra la ampliación de `main.jsx`.
- Endpoints o pantallas involucrados: inicialización del frontend.
- Archivos creados o modificados: `frontend/src/main.jsx`.
- Configuración utilizada, sin secretos: React/Vite.
- Rama: `master` es la rama actual y contiene este commit; rama histórica al crear el commit: No verificable con git log.
- Commit/hash: `205da6cf8b6f48015879ec7e72afc9f30a840b76`
- Cómo se probó: verificación del cambio registrado por Git y del archivo presente.
- Resultado esperado: punto de entrada que monta la aplicación.
- Resultado obtenido: cambio de `main.jsx` registrado; validación de ejecución en navegador para ese commit: No verificable con git log.
- Evidencia: `git show --stat 205da6cf8b6f48015879ec7e72afc9f30a840b76`.
- Riesgos, pendientes o reversión necesaria: validar inicio mediante build y prueba de ejecución.
- Entrega a la siguiente persona: No verificable con git log.

### [2026-09-28 12:07:01] — Responsable: Julián

- Estado: [~] Parcial
- Fecha planificada: No verificable con git log.
- Fecha real: 2026-09-28 12:07:01 (fecha/hora del commit).
- Dependencias: No verificable con git log.
- Área: frontend
- Historia o requisito: HU-02, HU-04, HU-05, HU-06, HU-07, HU-08, HU-10, HU-12, HU-13, HU-15
- Tarea o problema: ampliación del catálogo, detalle, favoritos y modo provisional.
- Qué se hizo: Git registra el modo provisional, cuenta demo, catálogo, detalles y almacenamiento local. El modo provisional sigue activo y no acredita el flujo conectado a API real.
- Endpoints o pantallas involucrados: catálogo, ficha/modal, favoritos, historial local, login y registro.
- Archivos creados o modificados: `frontend/src/App.jsx`, `frontend/src/components/AppShell.jsx`, `MascotaCard.jsx`, `PetCard.jsx`, `PetDetailsModal.jsx`, `frontend/src/config.js`, `frontend/src/context/AuthContext.jsx`, `frontend/src/pages/CatalogPage.jsx`, `FavoritesPage.jsx`, `HistoryPage.jsx`, `Home.jsx`, `LoginPage.jsx`, `RegisterPage.jsx`, `frontend/src/services/localStore.js`.
- Configuración utilizada, sin secretos: `PROVISIONAL_MODE = true`; catálogo demo y `localStorage`.
- Rama: `master` es la rama actual y contiene este commit; rama histórica al crear el commit: No verificable con git log.
- Commit/hash: `71629736c78ae038c7f097171240a3ff3703dc49`
- Cómo se probó: verificación del cambio registrado por Git y del código actual; no hay evidencia de prueba completa contra API.
- Resultado esperado: experiencia de catálogo y favoritos en modo provisional.
- Resultado obtenido: componentes y almacenamiento local presentes; integración real pendiente.
- Evidencia: `git show --stat 71629736c78ae038c7f097171240a3ff3703dc49`; `frontend/src/config.js`.
- Riesgos, pendientes o reversión necesaria: desactivar modo provisional tras implementar y probar las vistas pendientes.
- Entrega a la siguiente persona: conectar vistas a la API y sustituir datos locales.

### [2026-09-28 12:14:29] — Responsable: Julián

- Estado: [x] Hecho y verificado
- Fecha planificada: No verificable con git log.
- Fecha real: 2026-09-28 12:14:29 (fecha/hora del commit).
- Dependencias: No verificable con git log.
- Área: frontend
- Historia o requisito: P1 — presentación de landing/favoritos
- Tarea o problema: integrar icono/corazón en la página principal.
- Qué se hizo: Git registra ajustes de estilos relacionados con el corazón/favoritos en la landing.
- Endpoints o pantallas involucrados: estilos de la página principal.
- Archivos creados o modificados: `frontend/src/index.css`.
- Configuración utilizada, sin secretos: CSS del frontend.
- Rama: `master` es la rama actual y contiene este commit; rama histórica al crear el commit: No verificable con git log.
- Commit/hash: `d43ad982e53cf099b42ecd2109283ad32c5f5b57`
- Cómo se probó: verificación del diff registrado por Git.
- Resultado esperado: estilo visual para el corazón/favoritos en landing.
- Resultado obtenido: cambios CSS presentes; comprobación visual de ese commit: No verificable con git log.
- Evidencia: `git show --stat d43ad982e53cf099b42ecd2109283ad32c5f5b57`.
- Riesgos, pendientes o reversión necesaria: ninguna pendiente de código registrada para este cambio visual; confirmar visualmente en revisión de UI.
- Entrega a la siguiente persona: No verificable con git log.

---

## Seguimiento por persona

Esta sección resume el avance. El detalle y la evidencia deben permanecer en las entradas cronológicas.

### Julián — Servicios, acceso a datos, endpoints y base de datos

- Estado general: [~] Avance parcial registrado en Git; el estado de trabajo actual no se puede inferir solo del historial.
- [x] Autenticación, roles y JWT.
- [x] CRUD, búsqueda y filtros de mascotas.
- [~] Solicitudes y decisión: endpoints y reglas base presentes; falta estado Finalizada y confirmar el momento del cambio a ADOPTADA.
- [~] Servicio de seguimiento y consulta con control de acceso: falta `POST` para registrar cambios.
- [~] Notificación de cambio de estado: servicio y llamada presentes; envío SMTP no probado y recuperación por correo incompleta.
- [x] `schema.sql` y `data.sql`.
- [ ] Endpoints de perfil (`/api/usuarios/me`).
- [ ] Endpoint `POST` de seguimiento para el administrador.
- [ ] Cierre de solicitud como Finalizada.
- [!] Envío real del correo de recuperación: `TODO` en `AuthService`; mecanismo SMTP no configurado/probado.
- [ ] Política de contraseña al restablecer.
- [ ] SMTP real configurado y probado.
- [ ] PostgreSQL instalado y `adopet_db` creada.
- [ ] Variables de entorno y `.env.example` compartidos.
- [ ] Tokens de recuperación guardados en BD.
- [ ] Registrar la configuración presente (`ddl-auto=update` junto con `spring.sql.init.mode=always`) y decidir si es definitiva.
- [ ] Probar PostgreSQL con evidencia: los scripts/configuración existentes no demuestran una conexión exitosa.
- [ ] README técnico y revisión de CORS.
- Último commit/hash: `d43ad982e53cf099b42ecd2109283ad32c5f5b57` (último commit local verificable de JulianRomero17, frontend).
- Evidencia principal: entradas cronológicas del 2026-09-21 y 2026-09-28; `git log` y `git show`.
- Pendiente principal: endpoints faltantes y conexión a la base de datos

### Juan Esteban — Interfaz y consumo de controladores

- Estado general: [ ] Sin evidencia de commits atribuibles a Juan Esteban en el `git log` disponible; esto no permite inferir trabajo fuera de Git.
- [ ] Validaciones de formulario en el registro.
- [ ] Sesión persistente y manejo de token expirado.
- [ ] Página "Mi perfil".
- [ ] Páginas de recuperación y restablecimiento de contraseña.
- [ ] Inventario admin con editar y desactivar mascotas.
- [ ] Catálogo con datos reales, carga, lista vacía y paginación.
- [ ] Filtros con listas desplegables y botón "Limpiar".
- [ ] Ficha con estado de salud real y URL propia.
- [ ] Historial para el administrador.
- [ ] Página de gestión de usuarios.
- [ ] Diseño de la línea de tiempo de seguimiento.
- [ ] Revisión en celular y navegadores modernos.
- [ ] `PROVISIONAL_MODE = false` y retiro de datos de ejemplo.
- [ ] Build final del frontend.
- Último commit/hash: No verificable con git log.
- Evidencia principal: tareas pendientes del plan; la interfaz existente en Git no se atribuye a Juan Esteban.
- Pendiente principal: todo el bloque de interfaz pendiente

### Yuber — Solicitudes, seguimiento, integración y pruebas

- Estado general: [ ] Sin evidencia de commits atribuibles a Yuber en el `git log` disponible; esto no permite inferir trabajo fuera de Git.
- [ ] Confirmación y mensajes claros al solicitar.
- [ ] "Mis solicitudes" con `/solicitudes/mias`, nombre y foto de la mascota, y filtro por estado.
- [ ] Detalle y filtros de solicitudes en el panel admin.
- [ ] Estado "En revisión" desde la interfaz.
- [ ] Interfaz para marcar Finalizada.
- [ ] Seguimiento en panel admin y en "Mis solicitudes".
- [ ] Notificaciones para "En revisión" y "Finalizada".
- [ ] Pruebas de `MascotaService`, `SeguimientoService` y `NotificacionService`.
- [!] `AdopetIntegrationTest` bloqueada por falta de entorno Docker válido para Testcontainers.
- [ ] Pruebas de roles.
- [ ] Prueba de tiempo de respuesta (RNF-02).
- [ ] Verificación del proxy y pruebas contra la BD real.
- [ ] Limpieza del repositorio para la entrega.
- [ ] Demostración grabada de respaldo y envío final.
- Último commit/hash: No verificable con git log.
- Evidencia principal: reportes Surefire y pendientes del plan.
- Pendiente principal: solicitudes y seguimiento en la interfaz, pruebas adicionales, integración con BD y desbloqueo de Testcontainers

---

## Registro de sesiones de integración

| Fecha | Hora inicial | Hora final | Responsable | Objetivo | Resultado | Evidencia |
|---|---|---|---|---|---|---|
| Pendiente | Pendiente | Pendiente | Julián | Conexión de PostgreSQL y variables de entorno | Pendiente | Pendiente |
| Pendiente | Pendiente | Pendiente | Yuber, apoyo Juan Esteban | Frontend sin modo provisional contra la API real | Pendiente | Pendiente |
| Pendiente | Pendiente | Pendiente | Equipo completo | Recorrido completo y ensayo de la demostración | Pendiente | Pendiente |

---

## Matriz de pruebas de integración

| Prueba | Responsable | Estado | Resultado y evidencia |
|---|---|---|---|
| Backend inicia y conecta con PostgreSQL | Julián | [ ] | Pendiente; `application.properties` contiene configuración, pero no prueba que la conexión haya ocurrido |
| `schema.sql` y `data.sql` cargan sin errores | Julián | [ ] | Pendiente |
| Registro de adoptante nuevo | Yuber | [ ] | Pendiente |
| Login de adoptante y de administrador | Yuber | [ ] | Pendiente |
| Sesión se mantiene al recargar | Juan Esteban | [ ] | Pendiente |
| Recuperar y restablecer contraseña con correo | Julián y Juan Esteban | [!] | Bloqueada: envío real de recuperación conserva un `TODO`; SMTP y flujo de correo no están probados |
| Editar perfil | Juan Esteban | [ ] | Pendiente |
| Catálogo muestra solo mascotas disponibles | Juan Esteban | [ ] | Pendiente |
| Filtros por especie, raza, sexo, edad y tamaño | Yuber | [ ] | Pendiente |
| Admin registra, edita y desactiva una mascota | Juan Esteban | [ ] | Pendiente |
| Solicitud de mascota disponible se registra como Pendiente | Yuber | [ ] | Pendiente |
| Solicitud de mascota no disponible es rechazada | Yuber | [ ] | Pendiente |
| Solicitud duplicada es rechazada | Yuber | [ ] | Pendiente |
| Adoptante solo ve sus solicitudes | Yuber | [ ] | Pendiente |
| Admin pasa solicitud a En revisión y agrega observación | Yuber | [ ] | Pendiente |
| Aprobar cambia la mascota a ADOPTADA | Yuber | [!] | El código actual lo hace al aprobar, pero RF-16/RN-06 es ambiguo y falta confirmación del Product Owner antes de fijar el criterio |
| Rechazar mantiene la mascota disponible | Yuber | [ ] | Pendiente |
| Adoptante recibe correo con estado y fecha | Julián | [!] | Notificación invocada por el servicio; falta configurar/probar SMTP real |
| Seguimiento visible para admin y adoptante | Yuber | [ ] | Pendiente |
| Adoptante no puede ver el seguimiento de otro | Yuber | [ ] | Pendiente |
| Historial para adoptante y administrador | Juan Esteban | [ ] | Pendiente |
| Admin activa y desactiva usuarios | Juan Esteban | [ ] | Pendiente |
| Adoptante no accede a rutas de administrador | Yuber | [ ] | Pendiente |
| Consultas comunes responden en menos de 3 s | Yuber | [ ] | Pendiente |
| Pruebas unitarias del backend | Julián (reportes existentes); Yuber (faltantes) | [~] | `mvn test` (2026-10-11): 19 aprobadas — `AuthServiceTest` 8, `SolicitudAdopcionServiceTest` 7, `UsuarioServiceTest` 4. Faltan `MascotaService`, `SeguimientoService` y `NotificacionService`. Reportes en `target/surefire-reports/` |
| Prueba de integración (`AdopetIntegrationTest`) | Yuber | [!] | `mvn test` (2026-10-11) reporta 1 error de 20 pruebas: Testcontainers no encuentra un entorno Docker válido. Evidencia en `target/surefire-reports/TEST-com.adopet.AdopetIntegrationTest.xml` |
| Build del frontend | Juan Esteban | [ ] | Pendiente |
| Demostración grabada de respaldo | Yuber | [ ] | Pendiente |

---

## Revisión consolidada de la entrega

Completar esta sección después del ensayo final.

- Estado: [ ] Pendiente / [ ] Aprobada / [ ] Rechazada
- Fecha y hora de revisión:
- Rama integrada:
- Commit final:
- Alcance P0 completado:
- Funcionalidades P1 incluidas:
- Pruebas ejecutadas:
- Resultado de pruebas:
- Resultado de build:
- Flujo completo verificado con PostgreSQL:
- Evidencia final:
- Riesgos conocidos aceptados:
- Pendientes para la siguiente entrega:
- Responsable del envío final:
- Hora de envío:

### Checklist de aprobación

- [ ] El frontend funciona sin modo provisional contra el backend real.
- [ ] El backend usa PostgreSQL con los datos de ejemplo cargados.
- [ ] El flujo registro → solicitud → decisión → seguimiento → historial funciona.
- [ ] Los roles restringen correctamente el acceso.
- [ ] Hay notificaciones al adoptante.
- [ ] No hay credenciales ni secretos en Git.
- [ ] Los errores se manejan sin detener el sistema.
- [ ] Pruebas y build no presentan errores nuevos.
- [ ] Los tres informes tienen evidencia y commit/hash.
- [ ] Existe video de respaldo de la demostración.
- [ ] El plan y este registro reflejan el estado real de la entrega.
