import { createContext, useContext, useMemo, useState } from 'react'
import { authApi, setApiToken } from '../services/api'
import { DEMO_ACCOUNT, PROVISIONAL_MODE } from '../config'

const AuthContext = createContext(null)

function decodeToken(token) {
  try {
    return JSON.parse(atob(token.split('.')[1]))
  } catch {
    return {}
  }
}

export function AuthProvider({ children }) {
  const [session, setSession] = useState(null)
  const user = session ? { correo: session.correo, rol: session.rol } : null

  async function login(credentials) {
    if (PROVISIONAL_MODE) {
      if (credentials.correo !== DEMO_ACCOUNT.correo || credentials.contrasena !== DEMO_ACCOUNT.contrasena) {
        const error = new Error('Credenciales provisionales inválidas')
        error.response = { data: { mensaje: 'Usa demo@adopet.local y adopet2026 para ingresar.' } }
        throw error
      }

      const next = { token: 'provisional-session', correo: DEMO_ACCOUNT.correo, rol: DEMO_ACCOUNT.rol }
      setSession(next)
      return next
    }

    const { data } = await authApi.login(credentials)
    const claims = decodeToken(data.token)
    const next = { token: data.token, correo: claims.sub || credentials.correo, rol: data.rol }
    setApiToken(next.token)
    setSession(next)
    return next
  }

  async function register(payload) {
    return authApi.register(payload)
  }

  function logout() {
    setApiToken(null)
    setSession(null)
  }

  const value = useMemo(() => ({ session, user, login, register, logout }), [session, user])
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) throw new Error('useAuth debe usarse dentro de AuthProvider')
  return context
}
