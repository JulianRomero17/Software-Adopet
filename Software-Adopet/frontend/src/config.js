// Modo temporal mientras se termina de conectar la base de datos.
// Cambiar a false cuando el backend y PostgreSQL estén listos.
export const PROVISIONAL_MODE = true

export const DEMO_ACCOUNT = {
  correo: 'demo@adopet.local',
  contrasena: 'adopet2026',
  rol: 'ADOPTANTE',
  nombre: 'Daniela Gómez',
  documento: '1000000001',
  telefono: '300 555 0101',
  direccion: 'Cra. 15 # 20-30, Bogotá',
}

export const DEMO_ADMIN_ACCOUNT = {
  correo: 'admin@adopet.example.com',
  contrasena: 'password',
  rol: 'ADMINISTRADOR',
}
