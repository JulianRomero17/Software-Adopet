import { ClipboardList, LayoutDashboard, LogOut, Search, ShieldCheck } from 'lucide-react'
import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import logo from '../../images/Logo_Adopet.png'

export default function AppShell() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const admin = user?.rol === 'ADMINISTRADOR'
  const navClass = ({ isActive }) => `flex items-center gap-3 rounded-xl px-3.5 py-2.5 text-sm font-semibold transition ${isActive ? 'bg-coral-100 text-coral-700' : 'text-[#746C63] hover:bg-cream hover:text-ink'}`

  function signOut() { logout(); navigate('/login') }

  return <div className="min-h-screen bg-cream">
    <aside className="fixed inset-y-0 left-0 z-20 hidden w-64 border-r border-[#EEE7DD] bg-white px-5 py-6 lg:block">
      <div className="flex items-center gap-0 px-2"><img src={logo} alt="Adopet" className="relative -top-1 -mr-3 h-20 w-20 object-contain" /><div className="relative top-0.5"><div className="font-display text-2xl font-bold leading-none text-ink">adopet<span className="text-coral-500">.</span></div><div className="mt-1 text-[10px] font-bold uppercase tracking-widest text-[#9B9289]">un hogar empieza aquí</div></div></div>
      <div className="mt-12"><p className="eyebrow px-3">{admin ? 'Gestión' : 'Descubre'}</p><nav className="mt-3 space-y-1">
        {admin ? <><NavLink to="/admin" className={navClass}><LayoutDashboard size={18} /> Panel general</NavLink><NavLink to="/catalogo" className={navClass}><Search size={18} /> Catálogo</NavLink></> : <><NavLink to="/catalogo" className={navClass}><Search size={18} /> Explorar mascotas</NavLink><NavLink to="/historial" className={navClass}><ClipboardList size={18} /> Mis solicitudes</NavLink></>}
      </nav></div>
      <div className="absolute bottom-6 left-5 right-5 border-t border-[#EEE7DD] pt-5"><div className="mb-4 flex items-center gap-3 px-2"><div className="grid h-9 w-9 place-items-center rounded-full bg-sage-100 font-display font-bold text-sage-700">{user?.correo?.[0]?.toUpperCase()}</div><div className="min-w-0"><p className="truncate text-sm font-bold text-ink">{user?.correo}</p><p className="text-xs text-[#9B9289]">{admin ? 'Administrador' : 'Adoptante'}</p></div></div><button onClick={signOut} className="flex w-full items-center gap-3 rounded-xl px-3.5 py-2.5 text-sm font-semibold text-[#8A8178] hover:bg-coral-50 hover:text-coral-700"><LogOut size={18} /> Cerrar sesión</button></div>
    </aside>
    <main className="lg:pl-64"><div className="mx-auto min-h-screen max-w-[1440px] px-5 py-5 sm:px-8 lg:px-12 lg:py-9"><div className="mb-7 flex items-center justify-between lg:hidden"><div className="flex items-center gap-2 font-display text-xl font-bold">adopet<span className="text-coral-500">.</span></div><button onClick={signOut} className="rounded-xl p-2 text-[#776F66]" title="Cerrar sesión"><LogOut size={19} /></button></div><Outlet /></div></main>
  </div>
}
