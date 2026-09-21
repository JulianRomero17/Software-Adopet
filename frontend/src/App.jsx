import { Navigate, Route, Routes } from 'react-router-dom'
import { useAuth } from './context/AuthContext'
import ProtectedRoute from './components/ProtectedRoute'
import AppShell from './components/AppShell'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import CatalogPage from './pages/CatalogPage'
import AdminDashboardPage from './pages/AdminDashboardPage'
import HistoryPage from './pages/HistoryPage'
import NotFoundPage from './pages/NotFoundPage'

export default function App() {
  const { user } = useAuth()
  const home = user?.rol === 'ADMINISTRADOR' ? '/admin' : '/catalogo'

  return (
    <Routes>
      <Route path="/" element={<Navigate to={user ? home : '/login'} replace />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/registro" element={<RegisterPage />} />
      <Route element={<ProtectedRoute />}>
        <Route element={<AppShell />}>
          <Route path="/catalogo" element={<CatalogPage />} />
          <Route path="/historial" element={<HistoryPage />} />
          <Route path="/admin" element={<ProtectedRoute roles={['ADMINISTRADOR']} />}>
            <Route index element={<AdminDashboardPage />} />
          </Route>
        </Route>
      </Route>
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  )
}
