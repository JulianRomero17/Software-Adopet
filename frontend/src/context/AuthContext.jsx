import { createContext, useContext, useEffect, useMemo, useState } from 'react'
import { authApi, setApiToken } from '../services/api'

const AuthContext = createContext(null)
const SESSION_STORAGE_KEY = 'adopet_session'

function decodeToken(token) {
  try {
    return JSON.parse(atob(token.split('.')[1]))
  } catch {
    return {}
  }
}

function readStoredSession() {
  try {
    const session = JSON.parse(localStorage.getItem(SESSION_STORAGE_KEY) || 'null')
    if (!session?.token) return { session: null, expired: false }

    const { exp } = decodeToken(session.token)
    if (session.token === 'provisional-session' || typeof exp !== 'number' || exp * 1000 <= Date.now()) {
      localStorage.removeItem(SESSION_STORAGE_KEY)
      return { session: null, expired: true }
    }

    setApiToken(session.token)
    return { session, expired: false }
  } catch {
    localStorage.removeItem(SESSION_STORAGE_KEY)
    return { session: null, expired: true }
  }
}

export function AuthProvider({ children }) {
  const [initialSession] = useState(readStoredSession)
  const [session, setSession] = useState(initialSession.session)
  const [sessionExpired, setSessionExpired] = useState(initialSession.expired)
  const user = session ? Object.fromEntries(Object.entries(session).filter(([key]) => key !== 'token' && key !== 'contrasena')) : null

  useEffect(() => {
    if (!session) {
      localStorage.removeItem(SESSION_STORAGE_KEY)
      return
    }
    const { contrasena, ...safeSession } = session
    localStorage.setItem(SESSION_STORAGE_KEY, JSON.stringify(safeSession))
  }, [session])

  useEffect(() => {
    function expireSession() {
      setApiToken(null)
      setSession(null)
      setSessionExpired(true)
    }
    window.addEventListener('adopet:session-expired', expireSession)
    return () => window.removeEventListener('adopet:session-expired', expireSession)
  }, [])

  async function login(credentials, expectedRole) {
    setSessionExpired(false)
    const { data } = await authApi.login(credentials)
    const claims = decodeToken(data.token)
    if (data.rol !== expectedRole) {
      const error = new Error('El tipo de acceso no coincide con el rol de la cuenta')
      error.response = { data: { mensaje: 'Esta cuenta no tiene permiso para el apartado seleccionado.' } }
      throw error
    }
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
    setSessionExpired(false)
  }

  const value = useMemo(() => ({ session, user, login, register, logout, sessionExpired }), [session, user, sessionExpired])
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) throw new Error('useAuth debe usarse dentro de AuthProvider')
  return context
}
