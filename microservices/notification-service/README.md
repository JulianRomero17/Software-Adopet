# Adopet Notification Service

Microservicio Spring Boot independiente para enviar por SMTP los correos de cambio de estado de solicitudes.

## Contrato

- `GET /health`: estado básico del proceso (sin credenciales).
- `POST /internal/notifications/status-change`: recibe JSON con `solicitudId`, `destinatario`, `estado` y `fechaCambio`. Requiere `X-Internal-Api-Key`.
- Responde `200` con `status=sent`, `400` ante datos inválidos, `401` ante clave interna inválida y `5xx` si el proveedor SMTP falla.

## Ejecución local

Configura SMTP y la misma clave interna en las variables del proceso. En PowerShell:

```powershell
$env:INTERNAL_API_KEY = "un-secreto-compartido-largo"
$env:SMTP_HOST = "smtp.example.com"
$env:SMTP_PORT = "587"
$env:SMTP_USERNAME = "cuenta@example.com"
$env:SMTP_PASSWORD = "contraseña-de-aplicación"
mvn spring-boot:run
```

Desde la raíz del repositorio, copia la configuración del `.env.example` al `.env`, define `NOTIFICATION_SERVICE_API_KEY` con un secreto privado y ejecuta:

```powershell
docker compose -f docker-compose.notifications.yml up --build
```

El backend principal debe tener `NOTIFICATION_SERVICE_URL=http://localhost:8081` y `NOTIFICATION_SERVICE_API_KEY` con el mismo valor. Si ambos corren dentro de Docker, usa el nombre DNS `http://notification-service:8081` y configura el backend en la misma red.

## Seguridad y alcance

La clave compartida autentica llamadas internas; almacena secretos reales solo en `.env` o en un gestor de secretos. En despliegue público, limita el acceso de red al endpoint interno y usa TLS. El endpoint de salud no verifica SMTP. La llamada desde el monolito es síncrona y de mejor esfuerzo: si SMTP o el microservicio no están disponibles, el cambio de solicitud queda guardado y el error se registra en los logs; no hay reintentos ni cola persistente en esta primera extracción.
