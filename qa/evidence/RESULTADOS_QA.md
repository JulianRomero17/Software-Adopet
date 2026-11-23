# Ejecución de QA - Adopet

Fecha: 2026-11-22

## Resumen

- Backend: `mvn test`, 36/36 pruebas aprobadas; 0 fallas, 0 errores, 0 omitidas.
- Unidades: 32 pruebas en AdminBootstrap (4), AuthService (8), FavoritoService (4), MascotaService (5), SolicitudAdopcionService (7) y UsuarioService (4).
- Integración: 4/4 aprobadas usando PostgreSQL 18 desechable con Testcontainers.
- Carga/rendimiento: 25 llamadas simultáneas a `GET /api/mascotas/{id}`; 25 respuestas HTTP 200; p95 81 ms; 0 errores. Criterio aplicado: p95 <3000 ms. Medición MockMvc interna al proceso.
- Frontend: `npm run build` aprobado con Vite 8.3.8.
- Dependencias npm: audit posterior a las actualizaciones reportó 0 vulnerabilidades.
- Navegadores: Chrome 157.0.8103.79 y Edge 158.0.4370.33, con vistas escritorio 1440×1000 y móvil 390×844. En móvil: menú visible, enlace de sección operativo, CTA de catálogo conduce al acceso y no existe overflow horizontal.
- Recuperación: PostgreSQL 18 QA; `pg_dump`, borrado del centinela, restauración, reinicio del contenedor y lectura posterior exitosa (1/1).

## Resultado por categoría

| Categoría | Estado | Alcance verificado |
|---|---|---|
| Funcionalidad | Aprobada | Registro/login, catálogo, solicitud, rechazo duplicado, decisión, estado e historial; baja/reactivación. |
| Seguridad | Aprobada en alcance | BCrypt, token, denegación por rol, auditoría npm sin vulnerabilidades reportadas. No se hizo pentest ni escaneo CVE de dependencias Java. |
| Rendimiento | Aprobada | Endpoint de detalle medido: p95 81 ms bajo 25 solicitudes sincronizadas; criterio RNF-02 <3 s. |
| Carga/estrés | Aprobada para 25 concurrentes | 25/25 respuestas correctas, 0 errores. No se continuó hasta saturación; máximo absoluto no inferido. |
| Integración | Aprobada | 4/4 casos con Spring y PostgreSQL 18 aislado. |
| Usabilidad | Aprobada por tareas guiadas | 3 interacciones móviles pasaron en Chrome y Edge. No participaron usuarios externos. |
| Compatibilidad | Aprobada en matriz | Chrome y Edge, escritorio y viewport móvil. No se probaron Safari, Firefox ni dispositivos físicos. |
| Estabilidad/recuperación | Aprobada para PostgreSQL QA | Respaldo restaurado y dato preservado tras reiniciar el contenedor. No representa failover del backend o recuperación de almacenamiento del host. |

El informe IEEE definitivo contiene procedimientos, criterios y limitaciones completas. Las cuatro capturas de renderizado están en esta carpeta. El ejecutor de navegador está en `qa/browser-smoke.mjs`.
