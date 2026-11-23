# Evidencia reproducible de QA

## Backend

Con Docker Desktop iniciado, ejecutar desde la raíz del proyecto:

```powershell
mvn test
```

`AdopetIntegrationTest` inicia PostgreSQL 18 desechable con Testcontainers. Incluye el flujo de adopción, baja/reactivación, denegaciones por rol y 25 solicitudes simultáneas. Los tiempos se toman alrededor de solicitudes MockMvc dentro del proceso; no equivalen a una medición de red en producción.

## Frontend y dependencias

Desde `frontend/`:

```powershell
npm audit
npm run build
npm run dev -- --host 127.0.0.1
```

## Navegadores

Iniciar Edge o Chrome en modo headless con depuración limitada a loopback y un perfil temporal. Ejemplo para Edge:

```powershell
Start-Process 'C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe' -WindowStyle Hidden -ArgumentList @('--headless=new','--disable-gpu','--no-sandbox','--disable-features=VizDisplayCompositor','--use-gl=angle','--use-angle=swiftshader','--remote-debugging-port=9222','--remote-allow-origins=*','--user-data-dir=tmp\qa\edge-profile','http://127.0.0.1:5173')
node qa/browser-smoke.mjs 9222 desktop
node qa/browser-smoke.mjs 9222 mobile
```

Para Chrome, usar el ejecutable de Chrome, el puerto `9223` y ejecutar el script con `9223`. El smoke test verifica viewport sin desbordamiento horizontal, menú móvil, navegación al ancla y redirección del catálogo protegido al formulario de acceso. Genera capturas en `qa/evidence/`.

Las capturas actuales corresponden a Chrome 157.0.8103.79 y Edge 158.0.4370.33; escritorio 1440×1000 y móvil 390×844.
