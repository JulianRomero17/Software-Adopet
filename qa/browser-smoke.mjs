// Smoke visual y de navegación para la portada Adopet usando Chrome DevTools Protocol.
// Requiere un navegador Chromium en loopback (9222 Edge / 9223 Chrome) y Vite en 5173.
const port = process.argv[2] || '9222';
const viewportMode = process.argv[3] || 'mobile';
const viewport = viewportMode === 'mobile'
  ? { width: 390, height: 844, deviceScaleFactor: 1, mobile: true }
  : { width: 1440, height: 1000, deviceScaleFactor: 1, mobile: false };

const targets = await (await fetch(`http://127.0.0.1:${port}/json`)).json();
const target = targets.find((item) => item.type === 'page' && item.url.startsWith('http://127.0.0.1:5173'));
if (!target) throw new Error('No se encontró la pestaña local de Adopet');

const socket = new WebSocket(target.webSocketDebuggerUrl);
await new Promise((resolve, reject) => {
  socket.addEventListener('open', resolve, { once: true });
  socket.addEventListener('error', reject, { once: true });
});

let nextId = 0;
const pending = new Map();
socket.addEventListener('message', ({ data }) => {
  const message = JSON.parse(data);
  if (!message.id || !pending.has(message.id)) return;
  const { resolve, reject } = pending.get(message.id);
  pending.delete(message.id);
  if (message.error) reject(new Error(message.error.message));
  else resolve(message.result);
});

const call = (method, params = {}) => new Promise((resolve, reject) => {
  const id = ++nextId;
  pending.set(id, { resolve, reject });
  socket.send(JSON.stringify({ id, method, params }));
});

await call('Emulation.setDeviceMetricsOverride', viewport);
await call('Page.navigate', { url: 'http://127.0.0.1:5173/' });
await new Promise((resolve) => setTimeout(resolve, 1500));

const metrics = await call('Runtime.evaluate', {
  expression: `(() => ({
    viewport: [innerWidth, innerHeight],
    document: [document.documentElement.clientWidth, document.documentElement.scrollWidth],
    body: [document.body.clientWidth, document.body.scrollWidth],
    horizontalOverflow: document.documentElement.scrollWidth > document.documentElement.clientWidth
  }))()`,
  returnByValue: true,
});
const browserName = port === '9223' ? 'chrome' : 'edge';
const fs = await import('node:fs/promises');
await fs.mkdir('qa/evidence', { recursive: true });
const screenshot = await call('Page.captureScreenshot', { format: 'png', captureBeyondViewport: false });
await fs.writeFile(`qa/evidence/${browserName}-${viewportMode}.png`, Buffer.from(screenshot.data, 'base64'));

let uiFlow = null;
if (viewportMode === 'mobile') {
  const flow = await call('Runtime.evaluate', {
    expression: `(async () => {
      const menuButton = document.querySelector('button[title*="menú"]');
      menuButton?.click();
      await new Promise((resolve) => setTimeout(resolve, 100));
      const mobileMenuOpened = [...document.querySelectorAll('a[href="#como-funciona"]')]
        .some((link) => link.getBoundingClientRect().width > 0);
      const sectionLink = [...document.querySelectorAll('a[href="#como-funciona"]')]
        .find((link) => link.getBoundingClientRect().width > 0);
      sectionLink?.click();
      await new Promise((resolve) => setTimeout(resolve, 100));
      const anchorNavigationWorked = location.hash === '#como-funciona';
      document.querySelector('a[href="/catalogo"]')?.click();
      await new Promise((resolve) => setTimeout(resolve, 300));
      return {
        mobileMenuOpened,
        anchorNavigationWorked,
        catalogRequiresLogin: location.pathname === '/login',
        loginFormVisible: Boolean(document.querySelector('form input[type="email"]')),
      };
    })()`,
    awaitPromise: true,
    returnByValue: true,
  });
  uiFlow = flow.result.value;
}

const measuredMetrics = metrics.result.value;
if (measuredMetrics.horizontalOverflow) throw new Error(`Hay desbordamiento horizontal: ${JSON.stringify(measuredMetrics)}`);
if (uiFlow && Object.values(uiFlow).some((result) => result !== true)) {
  throw new Error(`Falló una tarea de interfaz: ${JSON.stringify(uiFlow)}`);
}
console.log(JSON.stringify({ browserName, viewportMode, metrics: measuredMetrics, uiFlow }));
socket.close();
