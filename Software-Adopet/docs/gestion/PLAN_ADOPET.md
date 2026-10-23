# Plan de trabajo — Adopet: sistema de gestión de adopción de mascotas

## Explicación general

Adopet es una aplicación web que centraliza el proceso de adopción de mascotas: publicación de mascotas, solicitudes de adopción, seguimiento e historial. Tiene dos tipos de usuario: **adoptante** y **administrador**. El detalle de requisitos está en [Adopet_EspecificacionRequisitos_FIS (2).pdf](../../Adopet_EspecificacionRequisitos_FIS%20%282%29.pdf) (RF-01 a RF-20, HU-01 a HU-18 y Gantt de la Figura 14).

El sistema usa arquitectura por capas con MVC: backend en Spring Boot (`presentation / business / data / security`) y frontend en React + Vite. La base de datos es PostgreSQL.

La estrategia de este plan es en dos fases: primero se termina **toda la interfaz y todas las funcionalidades** trabajando con datos provisionales, y al final se **conecta la base de datos** y se desactiva el modo provisional.

```text
Frontend React (Vite, puerto 5173)
       │ HTTP /api (proxy de Vite)
       ▼
Backend Spring Boot (puerto 8080)  ── JWT, roles, servicios
       │ JPA / JDBC
       ▼
PostgreSQL (adopet_db)
```

Cada avance, prueba, bloqueo y commit debe registrarse en [`REGISTRO_CAMBIOS_ADOPET.md`](REGISTRO_CAMBIOS_ADOPET.md). La cobertura y el estado de requisitos se mantienen en [`TRAZABILIDAD_ADOPET.md`](TRAZABILIDAD_ADOPET.md). Una tarea no se marca como terminada en este plan hasta que tenga su informe y evidencia.

**Alcance de este plan:** solo programación (no documentación académica).

**Fechas:** el Gantt de referencia define 8 semanas (4 Sprints de 2 semanas). Las fechas planificadas y reales permanecen en este plan para que el equipo las defina, registre y actualice como parte del control del proyecto. El equipo decide su propio ritmo y cuándo ejecutar cada tarea; las semanas de referencia no son plazos impuestos.

---

## Convenciones

- `[x]` hecho y verificado: existe evidencia en código, Git o reportes de pruebas, según corresponda.
- `[~]` parcial: indicar qué parte está hecha y qué falta.
- `[!]` bloqueado: indicar la causa verificable y qué se necesita para continuar.
- `[ ]` pendiente.
- No atribuir todo el trabajo del repositorio a una sola persona: usar la evidencia por commit en Git y el registro cronológico.
- Cada tarea debe llevar fecha planificada y fecha real; usar `Por definir` hasta que el equipo confirme el calendario. Registrar también las dependencias.

---

## Objetivo no negociable

Demostrar el flujo completo de adopción con datos reales en la base de datos:

```text
Adoptante se registra e inicia sesión
    → consulta y filtra el catálogo
        → solicita una mascota disponible
            → Administrador revisa y aprueba o rechaza
                → la mascota pasa a ADOPTADA
                    → se registra seguimiento y se notifica al adoptante
                        → el adoptante ve el estado y el historial
```

El proyecto no se considera terminado si el frontend sigue en `PROVISIONAL_MODE = true` o si el flujo solo funciona con datos locales.

---

## Alcance P0 de la entrega

- [x] Registro e inicio de sesión con JWT y roles (backend; evidencia de código y commit).
- [x] CRUD de mascotas, búsqueda y filtros (backend; evidencia de código y commit).
- [x] Solicitudes con validación de disponibilidad y duplicados (backend; evidencia de código y commit).
- [x] Decisión del administrador con cambio de mascota a ADOPTADA al aprobar (backend; evidencia de código y decisión confirmada en [Decisiones de interpretación](#decisiones-de-interpretacion)).
- [~] Interfaz: landing, login, registro, catálogo, filtros, ficha, favoritos, página de solicitudes y panel admin básico. En modo provisional ya permite acceso separado para administrador, gestión local de solicitudes y visualización del resultado por el adoptante. `HistoryPage` consume `/historial` en modo API, enriquece cada solicitud con los datos de la mascota; la validación integral con API y PostgreSQL sigue pendiente.
- [ ] Perfil del adoptante (HU-03).
- [!] Recuperación de contraseña completa con correo (HU-17): endpoints parciales, envío de correo con `TODO` y SMTP sin validar.
- [ ] Gestión completa de mascotas desde el panel admin: listar, editar y desactivar (HU-10, HU-11).
- [~] Seguimiento de solicitudes para administrador y adoptante (HU-09, HU-14): servicio y lectura presentes; alta de eventos e interfaces pendientes.
- [~] Notificaciones al adoptante (HU-18): servicio y llamada presentes; SMTP real y cobertura de cambios pendientes.
- [~] Historial de adopciones para ambos roles (HU-15): endpoint backend presente y el adoptante puede consultar estado y observaciones en modo provisional; vista admin y estado Finalizada pendientes.
- [ ] Gestión de usuarios para el administrador (HU-16).
- [ ] Base de datos PostgreSQL conectada y `PROVISIONAL_MODE = false`.
- [ ] Pruebas, evidencia y entrega final.

## Alcance P1 si P0 queda estable

- [ ] Favoritos guardados en el backend.
- [ ] Notificaciones dentro del sistema (campana de avisos).
- [ ] Página de detalle de mascota con URL propia.
- [ ] Paginación en catálogo y listados.

## Fuera de alcance

Pagos, transporte, servicios veterinarios, integraciones gubernamentales, aplicación móvil e integración con redes sociales (sección III-C del documento de requisitos).

---

## Integrantes

| Integrante | Rol Scrum | Enfoque |
|---|---|---|
| Julián Darío Romero Buitrago | Product Owner / Developer | Servicios, acceso a datos, endpoints, base de datos |
| Juan Esteban Moreno Durán | Scrum Master / Developer | Interfaz y consumo de controladores |
| Yuber Alejandro Bohorquez Roa | Developer | Solicitudes, seguimiento, integración y pruebas |

---

## Estado inicial verificado

- [~] Backend con autenticación, mascotas, solicitudes, consulta de seguimiento y usuarios verificables en el código; faltan perfil, endpoint POST de seguimiento y cierre como Finalizada.
- [~] Frontend funciona con modo provisional habilitado (`PROVISIONAL_MODE = true`); contiene cuenta demo, catálogo de ejemplo y almacenamiento local. No demuestra el flujo conectado a la API real.
- [x] Existen `schema.sql`, `data.sql` y configuración PostgreSQL en `application.properties`; [ ] conexión de PostgreSQL y ejecución con datos reales pendientes de evidencia.
- [x] `mvn test` (2026-10-11): 19 pruebas unitarias aprobadas (Auth 8, Solicitudes 7, Usuarios 4).
- [!] `mvn test` termina con error en la prueba de integración (1 error de 20 pruebas totales): Testcontainers no encuentra un entorno Docker válido.
- [!] `AdopetIntegrationTest`: bloqueada porque Testcontainers no encuentra un entorno Docker válido; requiere Docker disponible o un perfil de integración alternativo.
- [!] Recuperación de contraseña: los endpoints están presentes, pero el envío real de correo conserva un `TODO`; SMTP y flujo completo pendientes.

---

## Sprint 1 — Base del sistema y gestión inicial (Semanas 1–2)

### HU-01 — Registro de adoptante (Julián / Yuber)
- [x] `POST /api/auth/registro` con correo único y contraseña BCrypt (Julián)
- [x] Pantalla de registro conectada a la API (Julián)
- [ ] Validaciones de formulario en el cliente: correo, documento, contraseña y confirmación (Juan Esteban)
- [ ] Probar el registro contra la BD real (Julián, fase final)

### HU-02 — Inicio de sesión y roles (Juan Esteban / Yuber)
- [x] `POST /api/auth/login` con JWT y rol (Julián)
- [x] `SecurityConfig`, `@PreAuthorize` y filtro JWT (Julián)
- [~] Login, `AuthContext` y `ProtectedRoute` por rol (Julián): el login ofrece acceso separado para adoptante y administrador, credenciales demo y redirección del administrador a `/admin`; el modo provisional permanece activo, y la sesión no se restaura al recargar.
- [ ] Persistir la sesión al recargar la página (Juan Esteban)
- [ ] Manejar token expirado: volver al login con aviso (Juan Esteban)
- [ ] Probar login real con adoptante y administrador (Julián, fase final)

### HU-03 — Gestión de perfil (Juan Esteban)
- [ ] `GET /api/usuarios/me` y `PUT /api/usuarios/me` con su servicio (Julián)
- [ ] Página "Mi perfil" para consultar y editar datos (Juan Esteban)
- [ ] Enlace "Mi perfil" en el menú lateral (Juan Esteban)

### HU-17 — Recuperación de contraseña (Julián)
- [~] `POST /api/auth/recuperar-contrasena` y `/restablecer-contrasena` con token de 15 minutos (Julián): endpoints presentes; tokens en memoria, falta persistencia en BD.
- [!] Envío real del correo: `enviarCorreoRecuperacion` tiene un `TODO`; falta configurar y probar el mecanismo de verificación (Julián).
- [ ] Política de contraseña al restablecer (criterio de aceptación HU-17) (Julián)
- [ ] Páginas "¿Olvidaste tu contraseña?" y "Restablecer contraseña" con enlace desde el login (Juan Esteban)
- [ ] Guardar tokens en BD en lugar de memoria (Julián, fase final)

### HU-10 — Registro e inventario de mascotas (Julián / Juan Esteban)
- [x] `POST /api/mascotas` con validaciones (Julián)
- [~] Formulario "Registrar mascota" en el panel admin (Julián): el formulario existe; su funcionamiento integrado con la API debe confirmarse al retirar el modo provisional.
- [ ] Inventario de mascotas en el panel admin con estado y acciones (Juan Esteban)
- [ ] Mejorar formulario: previsualización de imagen, estado inicial y errores por campo (Juan Esteban)

---

## Sprint 2 — Búsqueda y exploración de mascotas (Semanas 3–4)

### HU-04 — Catálogo de mascotas disponibles (Juan Esteban)
- [x] `GET /api/mascotas` solo disponibles para el adoptante (Julián)
- [~] Catálogo con tarjetas y landing con mascotas destacadas (Julián): existe interfaz, pero el modo provisional sigue activo y la fuente de datos reales debe probarse.
- [ ] Reemplazar mascotas de ejemplo por datos del backend, incluidas las destacadas del Home (Juan Esteban)
- [ ] Estados de carga, lista vacía y paginación (Juan Esteban)

### HU-05 — Búsqueda y filtros avanzados (Julián / Juan Esteban)
- [x] Filtros con `Specification` por especie, raza, sexo, edad y tamaño (Julián)
- [~] Barra de filtros en el catálogo (Julián): está implementada; falta validar su integración contra la API real.
- [ ] Botón "Limpiar filtros" y listas desplegables para especie y tamaño (Juan Esteban)
- [ ] Probar filtros contra la API real (Yuber)

### HU-06 — Ficha detallada de mascota (Juan Esteban)
- [x] `GET /api/mascotas/{id}` (Julián)
- [~] Ventana de detalle con datos y botón de solicitud (Julián): está en la interfaz provisional; integración con datos reales pendiente.
- [ ] Mostrar el estado de salud real (hoy las etiquetas son fijas) (Juan Esteban)
- [ ] Página de detalle con URL propia `/mascotas/:id` (Juan Esteban)

### HU-11 — Actualizar y desactivar mascotas (Julián)
- [x] `PUT /api/mascotas/{id}` y `PATCH /api/mascotas/{id}/desactivar` (Julián)
- [x] Funciones `update` y `deactivate` en `services/api.js` (Julián)
- [ ] Botones "Editar" y "Desactivar" en el inventario, con confirmación (Juan Esteban)
- [ ] Formulario de edición precargado (Juan Esteban)

---

## Sprint 3 — Flujo de solicitudes y adopción (Semanas 5–6)

### HU-07 — Creación y validación de solicitudes (Yuber / Julián)
- [x] `POST /api/solicitudes` con validación de disponibilidad y duplicados (Julián)
- [~] Botón "Quiero solicitar" desde la ficha (Julián): visible en la interfaz; el envío real se omite mientras `PROVISIONAL_MODE` está activo.
- [ ] Confirmación antes de enviar y mensajes claros ante mascota no disponible o solicitud duplicada (Yuber)
- [ ] Deshabilitar el botón según datos del backend, no solo estado local (Yuber)

### HU-08 — Consulta de solicitudes por el adoptante (Yuber / Juan Esteban)
- [x] `GET /api/solicitudes/mias` y `GET /api/historial` (Julián)
- [~] Página "Mis solicitudes": en modo provisional muestra claramente solicitud pendiente, aprobada o rechazada, mascota, fecha y observaciones. En modo API consulta `requestsApi.history()` (`/historial`) y obtiene el detalle de cada mascota; falta probar ambos flujos con backend y verificar aislamiento por adoptante (Juan Esteban / Yuber).
- [~] La pantalla muestra nombre y foto cuando están disponibles en modo local o al cargar el detalle de la mascota desde la API; falta verificar la respuesta real y sus campos en una integración completa (Yuber / Juan Esteban).
- [ ] Filtro por estado (Yuber)

### HU-12 — Panel de revisión de solicitudes (Yuber / Juan Esteban)
- [x] `GET /api/solicitudes/pendientes` (Julián)
- [~] Contadores y listado en el panel admin (Julián): existe el panel; falta comprobarlo con datos de la API y ambos roles.
- [~] Detalle de solicitud con datos del adoptante y de la mascota: disponible en modo provisional y mediante llamadas de detalle a la API; las respuestas del cuestionario de adopción solo se conservan localmente y falta validar la integración real (Yuber).
- [ ] Filtros por estado, fecha o adoptante (Yuber)

### HU-13 — Aprobación o rechazo (Yuber / Julián)
- [~] `PATCH /api/solicitudes/{id}/decision`: aprobar/rechazar implementados; el endpoint llama al servicio de notificación, pero SMTP y el flujo completo no están verificados. El cambio a ADOPTADA al aprobar sigue la decisión confirmada RF-16/RN-06 (Julián).
- [~] Diálogo de decisión con observaciones: el administrador puede aprobar o rechazar y guardar observaciones en modo provisional; el consumo del endpoint existe, pero falta probar el flujo completo contra la API real.
- [ ] Marcar una solicitud como "En revisión" desde la interfaz (Yuber)
- [ ] Marcar una aprobada como "Finalizada": servicio (Julián) e interfaz (Yuber)

---

## Sprint 4 — Seguimiento, notificaciones, historial y cierre (Semanas 7–8)

### HU-14 — Gestión de seguimiento, administrador (Julián / Yuber)
- [x] Modelo, repositorio y servicio de seguimiento (Julián)
- [x] `GET /api/solicitudes/{id}/seguimiento` (Julián)
- [ ] `POST /api/solicitudes/{id}/seguimiento` para agregar estado y observaciones (Julián). Planificada: Por definir; real: Por definir; depende del servicio de seguimiento HU-14.
- [ ] Línea de tiempo con formulario en el detalle de la solicitud del panel admin (Yuber). Planificada: Por definir; real: Por definir; depende del endpoint POST de HU-14.

### HU-09 — Seguimiento para el adoptante (Juan Esteban / Yuber)
- [x] `GET /api/solicitudes/{id}/seguimiento/mio` con control de acceso (Julián)
- [ ] `seguimientoApi` en `services/api.js` (Yuber). Planificada: Por definir; real: Por definir; depende del endpoint POST/GET de HU-14.
- [ ] Línea de tiempo dentro de "Mis solicitudes"; diseño por Juan Esteban (Yuber). Planificada: Por definir; real: Por definir; depende de `seguimientoApi` y del consumo de `/solicitudes/mias`.

### HU-18 — Notificaciones automáticas (Julián / Yuber)
- [~] `NotificacionService` con envío de correo invocado al decidir una solicitud (Julián): servicio y llamada están en código; la entrega de correo no se ha verificado y SMTP real está pendiente.
- [ ] Configurar SMTP real y probar el envío; el correo debe incluir estado y fecha del cambio (Julián)
- [ ] Notificar también "En revisión" y "Finalizada" (Yuber)
- [ ] (P1) Notificaciones dentro del sistema: endpoint (Julián) y campana (Juan Esteban)

### HU-15 — Historial de adopciones (Juan Esteban)
- [x] `GET /api/historial` según rol (Julián)
- [ ] Página de historial para el administrador con enlace en el menú (Juan Esteban). Planificada: Por definir; real: Por definir; depende de confirmar estados finales y del flujo de HU-13.
- [ ] Reemplazar el historial local de `localStore.js` por datos del backend (Juan Esteban)

### HU-16 — Gestión de cuentas de usuarios (Juan Esteban)
- [x] `GET /api/usuarios`, `GET /api/usuarios/{id}` y `PATCH /api/usuarios/{id}/estado` (Julián)
- [ ] Página "Usuarios": listado de adoptantes, búsqueda, activar y desactivar (Juan Esteban)
- [ ] Ruta `/admin/usuarios` y enlace en el menú lateral (Juan Esteban)

### QA — Pruebas integrales y cierre (Yuber / Equipo completo)
- [x] 19 pruebas unitarias aprobadas de `AuthService` (8), `SolicitudAdopcionService` (7) y `UsuarioService` (4), según reportes (Julián).
- [ ] Pruebas de `MascotaService` (filtros, actualizar, desactivar) (Yuber)
- [ ] Pruebas de `SeguimientoService` y `NotificacionService` (Yuber)
- [!] `AdopetIntegrationTest` bloqueada: Testcontainers no encuentra Docker; habilitar Docker o acordar perfil alternativo (Yuber).
- [ ] Pruebas de roles: adoptante sin acceso a rutas de administrador y viceversa (Yuber)
- [ ] Recorrido completo del flujo con ambos roles (Equipo completo)
- [ ] Pruebas de tiempo de respuesta menor a 3 s en consultas comunes, RNF-02 (Yuber)
- [ ] Revisión en celular y navegadores modernos, RNF-09 (Juan Esteban)
- [ ] RNF-01: realizar revisión de usabilidad con lista de criterios (claridad, consistencia, facilidad de navegación) y registrar hallazgos y correcciones (Juan Esteban / Equipo).
- [ ] RNF-06: acordar periodos de disponibilidad y verificar arranque, recuperación ante caída y monitoreo durante esos periodos (Julián / Equipo).
- [ ] RNF-07: revisar separación por capas, convenciones y documentación para cambios/mantenimiento; registrar revisión cruzada (Equipo completo).
- [ ] RNF-08: documentar módulos/extensiones previstos y validar que una extensión seleccionada pueda añadirse sin rediseño global (Julián / Equipo).

---

## Extras ya construidos (fuera del Gantt)

- [x] Favoritos en `localStorage` (Julián; alcance provisional/local verificado en código).
- [x] Diseño visual: logo, paleta, `AppShell`, `Toast`, `StatusPill`, página 404 (Julián)
- [x] Modo provisional con cuentas demo separadas para adoptante y administrador, catálogo y persistencia local de mascotas y solicitudes (Julián; verificado en código; reemplazo por API sigue pendiente).
- [x] Selector de tema claro/oscuro persistente (Julián; verificado en código).
- [~] Revisión local de solicitudes: muestra información disponible del adoptante, mascota y respuestas del formulario; permite registrar decisión y observaciones. La información del cuestionario y la persistencia local no sustituyen el contrato ni las pruebas con la API y PostgreSQL.
- [~] Resultado de la solicitud para el adoptante: la pantalla muestra estado, mascota, fecha y observaciones en modo provisional; falta validación con datos reales y confirmar el aislamiento de datos por adoptante.
- [x] `schema.sql` y `data.sql` para PostgreSQL (Julián)
- [ ] (P1) Favoritos en el backend: tabla y endpoints (Julián), interfaz (Juan Esteban)

---

## Fase final — Conexión de la base de datos

Se ejecuta cuando todas las vistas y funcionalidades estén completas con datos provisionales.

- [ ] Instalar PostgreSQL y crear `adopet_db` (Julián)
- [ ] Definir `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `JWT_EXPIRATION_MS` y `SMTP_*`, y compartir un `.env.example` sin secretos (Julián)
- [ ] Registrar la configuración actual (`spring.jpa.hibernate.ddl-auto=update` y `spring.sql.init.mode=always`) y validar si es la opción final y no entra en conflicto con `schema.sql` (Julián).
- [ ] Probar la conexión PostgreSQL y la carga de `schema.sql`/`data.sql`, adjuntando logs reproducibles. Que existan configuración y scripts no demuestra una conexión real exitosa (Julián).
- [ ] Cargar `data.sql` y verificar los datos de ejemplo (Julián)
- [ ] Poner `PROVISIONAL_MODE = false` y retirar datos de ejemplo, cuenta demo y uso de `localStore.js` (Juan Esteban)
- [ ] Verificar el proxy `/api → http://localhost:8080` (Yuber)
- [ ] Probar cada módulo contra la BD real: autenticación, mascotas, solicitudes, seguimiento, usuarios (Yuber con apoyo del equipo)
- [ ] Verificar integridad referencial (RNF-05) y respaldo de la BD (RNF-10) (Julián)

---

## Cierre y entrega final

- [ ] README técnico con pasos para correr backend, frontend y base de datos (Julián)
- [ ] Revisar CORS y variables de entorno para ejecución en otro computador (Julián)
- [ ] Generar el build final del frontend (`npm run build`); `dist/` actual está desactualizado (Juan Esteban)
- [ ] Limpiar el repositorio: quitar `target/`, `node_modules/` y código provisional del paquete entregado (Yuber)
- [ ] Revisión cruzada: cada cambio revisado por otro integrante (Definition of Done) (Equipo completo)
- [ ] Confirmar que no hay errores críticos conocidos (Equipo completo)
- [ ] Ensayo completo de la demostración desde cero (Equipo completo)
- [ ] Guardar una demostración grabada de respaldo (Yuber)
- [ ] Congelar cambios funcionales después del ensayo, salvo errores bloqueantes (Equipo completo)
- [ ] Empaquetado y envío final (Yuber)

---

## Cronograma por Sprint

Las fechas planificadas y reales de cada Sprint y tarea se gestionan en las tablas de esta sección. El equipo debe reemplazar `Por definir` al acordar una fecha planificada y actualizar la fecha real al iniciar o completar el trabajo. Las semanas de referencia no fijan el ritmo de ejecución. Las dependencias se anotan en la tarea y en la tabla siguiente cuando condicionan el inicio.

| Sprint | Semanas | Inicio planificado | Fin planificado | Inicio real | Fin real | Historias | Entregable | Dependencias |
|---|---|---|---|---|---|---|---|---|
| 1 | 1–2 | Por definir | Por definir | Por definir | Por definir | HU-01, HU-02, HU-03, HU-17, HU-10 | Usuarios, autenticación, roles, recuperación y mascotas básicas | Inicio confirmado por el equipo |
| 2 | 3–4 | Por definir | Por definir | Por definir | Por definir | HU-04, HU-05, HU-06, HU-11 | Catálogo, filtros, detalle y edición de mascotas | Sprint 1 |
| 3 | 5–6 | Por definir | Por definir | Por definir | Por definir | HU-07, HU-08, HU-12, HU-13 | Solicitudes, revisión y decisión | Sprint 1 y Sprint 2 |
| 4 | 7–8 | Por definir | Por definir | Por definir | Por definir | HU-14, HU-09, HU-18, HU-15, HU-16, QA | Seguimiento, notificaciones, historial, usuarios y cierre | HU-13; HU-09 y pantalla admin dependen del POST de HU-14 |
| Final | — | Por definir | Por definir | Por definir | Por definir | Fase BD + cierre | Base de datos conectada y entrega | Funcionalidades P0 y entorno PostgreSQL disponible |

| Tarea / HU | Responsable | Estado | Fecha planificada | Fecha real | Dependencias |
|---|---|---|---|---|---|
| HU-01 Registro | Julián / Yuber | [~] | Por definir | Por definir | Inicio confirmado; API y PostgreSQL para prueba final |
| HU-02 Inicio de sesión y roles | Julián / Juan Esteban / Yuber | [~] | Por definir | Por definir | HU-01 y API disponible |
| HU-03 Perfil | Julián / Juan Esteban | [ ] | Por definir | Por definir | HU-02 |
| HU-17 Recuperación | Julián / Juan Esteban | [!] | Por definir | Por definir | SMTP y persistencia de token |
| HU-10 Registro e inventario | Julián / Juan Esteban | [~] | Por definir | Por definir | API de mascotas |
| HU-04 Catálogo | Juan Esteban / Julián | [~] | Por definir | Por definir | HU-10 y datos API |
| HU-05 Búsqueda y filtros | Julián / Juan Esteban / Yuber | [~] | Por definir | Por definir | HU-04 y API |
| HU-06 Detalle | Julián / Juan Esteban | [~] | Por definir | Por definir | HU-04 y API |
| HU-11 Actualizar/desactivar | Julián / Juan Esteban | [~] | Por definir | Por definir | HU-10 y API |
| HU-07 Solicitudes | Julián / Yuber | [~] | Por definir | Por definir | HU-04, usuario autenticado y API |
| HU-08 Consulta de solicitudes | Juan Esteban / Yuber / Julián | [~] | Por definir | Por definir | HU-07 y `/solicitudes/mias` |
| HU-12 Revisión administrativa | Julián / Yuber / Juan Esteban | [~] | Por definir | Por definir | HU-07 y rol administrador |
| HU-13 Decisión | Julián / Yuber | [~] | Por definir | Por definir | HU-12 |
| POST seguimiento HU-14 | Julián | [ ] | Por definir | Por definir | HU-13 y persistencia de solicitudes |
| Seguimiento admin HU-14 | Yuber | [ ] | Por definir | Por definir | POST seguimiento HU-14 |
| Seguimiento adoptante HU-09 | Juan Esteban / Yuber | [ ] | Por definir | Por definir | GET/POST seguimiento HU-14 y `/solicitudes/mias` HU-08 |
| HU-18 Notificaciones | Julián / Yuber | [~] | Por definir | Por definir | Estados HU-13/HU-14, SMTP real |
| HU-15 Historial | Juan Esteban / Julián | [~] | Por definir | Por definir | HU-13 y HU-14 |
| HU-16 Gestión de usuarios | Julián / Juan Esteban | [~] | Por definir | Por definir | HU-02 y rol administrador |
| QA y requisitos RNF | Equipo completo | [~] | Por definir | Por definir | Flujos P0 integrados y entorno de prueba |
| Conexión PostgreSQL y cierre | Julián / Juan Esteban / Yuber | [ ] | Por definir | Por definir | P0 implementado; entorno PostgreSQL disponible; pruebas integrales |

<!-- Las fechas se conservan aquí como herramienta de planificación y seguimiento del equipo. El equipo define las fechas y el ritmo que le resulten adecuados; no tiene que iniciar cada Sprint exactamente cada 14 días. Registrar las fechas reales conforme avance el trabajo. -->

<a id="decisiones-de-interpretacion"></a>
## Decisiones de interpretación

- [x] **RF-16 / RN-06 — momento de cambiar la mascota a `ADOPTADA`:** la especificación textual lo liga a la finalización de la adopción, mientras el diagrama de flujo lo sitúa después de la aprobación. La implementación actual de `SolicitudAdopcionService.decidirSolicitud` cambia la mascota a `ADOPTADA` al aprobar. El Product Owner, Julián, confirmó el 2026-10-11 que la interpretación del código es la acordada. Los criterios de aceptación de HU-13 y HU-15 se redactan según esta decisión: la mascota pasa a `ADOPTADA` al aprobar. La prueba integrada del flujo completo sigue pendiente.

---

## Reglas de Git

- [x] Git confirma que la rama actual y única rama local es `master`.
- [x] Flujo adoptado para este repositorio: `master` es la rama de integración; no se usará `entrega-adopet` porque no existe en Git. Los cambios nuevos se trabajarán en ramas individuales, se revisarán con su evidencia y luego se integrarán en `master`.
- [ ] Crear las ramas individuales desde `master` (los nombres siguientes son propuestas; no existen todavía):

```text
adopet/julian-backend-bd
adopet/juanesteban-interfaz
adopet/yuber-solicitudes-pruebas
```

- [ ] No subir `DB_PASSWORD`, `JWT_SECRET`, credenciales SMTP ni archivos `.env`.
- [ ] No usar `git add .` si existen cambios de otra persona en el directorio.
- [ ] Cada commit representa una unidad comprobable.
- [ ] Integrar en `master` solo cambios revisados que tengan evidencia y pasos de prueba.
- [ ] No hacer cambios grandes después del ensayo final.

---

## Criterios de aceptación antes de entregar

- [ ] El adoptante puede registrarse, iniciar sesión, editar su perfil y recuperar su contraseña.
- [ ] El catálogo muestra solo mascotas disponibles y los filtros funcionan con datos reales.
- [ ] No se puede solicitar una mascota no disponible ni duplicar una solicitud (RN-03).
- [ ] Solo el administrador aprueba o rechaza solicitudes (RN-05).
- [ ] Conforme a [Decisiones de interpretación](#decisiones-de-interpretacion), al aprobar una solicitud la mascota cambia a `ADOPTADA`; pendiente probar el flujo completo integrado.
- [ ] El adoptante solo ve sus propias solicitudes y su seguimiento.
- [ ] El adoptante recibe notificación con estado y fecha ante cada cambio.
- [ ] Los procesos finalizados se conservan en el historial (RN-07); la finalización deberá distinguirse de la aprobación conforme a la decisión pendiente.
- [ ] Cada rol solo accede a lo permitido (RN-08, RNF-04).
- [ ] Las contraseñas se guardan con hash (RNF-03).
- [ ] `PROVISIONAL_MODE = false` y todo funciona con PostgreSQL.
- [ ] No hay credenciales en Git.
- [ ] Pruebas, lint y build no presentan errores nuevos.
- [ ] Existe evidencia grabada de respaldo.
- [ ] La documentación técnica permite repetir la prueba en otro computador.

---

## Guion corto de demostración

1. Mostrar PostgreSQL con datos y el backend ejecutándose.
2. Abrir Adopet y registrar un adoptante nuevo.
3. Iniciar sesión y explorar el catálogo con filtros.
4. Abrir la ficha de una mascota y enviar la solicitud.
5. Mostrar que no se puede solicitar de nuevo la misma mascota.
6. Iniciar sesión como administrador y revisar la solicitud.
7. Marcarla "En revisión", agregar una observación y luego aprobarla.
8. Mostrar que la mascota pasó a ADOPTADA y que llegó la notificación.
9. Volver como adoptante y ver el estado y el seguimiento.
10. Mostrar el historial y la gestión de usuarios del administrador.

---

## Evidencia obligatoria por persona

Antes de marcar una tarea con `[x]`, cada responsable entrega:

1. Qué implementó y por qué.
2. Archivos creados o modificados.
3. Pasos exactos de prueba.
4. Resultado esperado y resultado obtenido.
5. Captura, log o video cuando corresponda.
6. Hash del commit.
7. Riesgos o pendientes conocidos.
