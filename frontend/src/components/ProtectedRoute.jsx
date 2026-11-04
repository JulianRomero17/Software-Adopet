import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function ProtectedRoute({ roles }) {
  const { user, sessionExpired } = useAuth()
  const location = useLocation()
  if (!user) return <Navigate to="/login" state={{ from: location, sessionExpired }} replace />
  if (roles && !roles.includes(user.rol)) return <Navigate to="/catalogo" replace />
  return <Outlet />
}
