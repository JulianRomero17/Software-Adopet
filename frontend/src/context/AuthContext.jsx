import { createContext, useContext, useMemo, useState } from 'react'
import { authApi, setApiToken } from '../services/api'

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
