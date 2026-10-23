import { createContext, useContext, useMemo, useState } from 'react'
import { authApi, setApiToken } from '../services/api'
import { DEMO_ACCOUNT, DEMO_ADMIN_ACCOUNT, PROVISIONAL_MODE } from '../config'
import { findLocalUser, registerLocalUser } from '../services/localStore'

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
  const user = session ? Object.fromEntries(Object.entries(session).filter(([key]) => key !== 'token' && key !== 'contrasena')) : null

  async function login(credentials, expectedRole) {
    if (PROVISIONAL_MODE) {
      const account = credentials.correo === DEMO_ADMIN_ACCOUNT.correo
        ? DEMO_ADMIN_ACCOUNT
        : findLocalUser(credentials.correo)

      if (!account || account.contrasena !== credentials.contrasena || account.rol !== expectedRole) {
        const error = new Error('Credenciales provisionales inválidas')
        error.response = { data: { mensaje: 'Verifica el correo, la contraseña y el tipo de acceso seleccionado.' } }
        throw error
      }

      const next = { ...account, token: 'provisional-session' }
      setSession(next)
      return next
    }

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
    if (PROVISIONAL_MODE) return registerLocalUser(payload)
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
