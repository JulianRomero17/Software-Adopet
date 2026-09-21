import { useState } from 'react'
import { ArrowRight, Check, Heart, Menu, PawPrint, Search, ShieldCheck, X } from 'lucide-react'
import { Link } from 'react-router-dom'
import MascotaCard from '../components/MascotaCard'
import logo from '../../images/Logo_Adopet.png'
import heroPets from '../../images/perros_gatos_pagina_principal.png'
import dog from '../../images/perro_1.png'
import cat from '../../images/gato_1.png'

const mascotasDestacadas = [
  { id: 1, nombre: 'Bruno', especie: 'Perro', raza: 'Golden retriever', imagen: dog },
  { id: 2, nombre: 'Michi', especie: 'Gato', raza: 'Criollo', imagen: cat },
  { id: 3, nombre: 'Luna', especie: 'Perra', raza: 'Compañera fiel', imagen: heroPets },
  { id: 4, nombre: 'Simba', especie: 'Gato', raza: 'Curioso y tierno', imagen: cat },
]

const pasos = [
  { icon: Search, numero: '01', titulo: 'Explora', texto: 'Conoce mascotas que buscan una familia y descubre cuál conecta contigo.' },
  { icon: Heart, numero: '02', titulo: 'Solicita', texto: 'Cuéntanos un poco sobre ti y da el primer paso hacia una nueva historia.' },
  { icon: PawPrint, numero: '03', titulo: 'Adopta', texto: 'Acompañamos el encuentro para que el nuevo hogar sea para siempre.' },
]

export default function Home() {
  const [menuOpen, setMenuOpen] = useState(false)

  return <div className="min-h-screen overflow-hidden bg-cream text-ink">
    <nav className="fixed inset-x-0 top-0 z-50 border-b border-[#EEE7DD]/80 bg-cream/90 backdrop-blur-md">
      <div className="mx-auto flex h-[76px] max-w-7xl items-center justify-between px-5 sm:px-8 lg:px-12">
        <Link to="/" className="flex items-center gap-0" onClick={() => setMenuOpen(false)}><img src={logo} alt="Adopet" className="relative -top-1 -mr-2 h-14 w-14 object-contain" /><span className="font-display text-2xl font-bold leading-none">adopet<span className="text-coral-500">.</span></span></Link>
        <div className="hidden items-center gap-8 md:flex"><a href="#como-funciona" className="text-sm font-semibold text-[#746C63] transition hover:text-coral-600">Cómo funciona</a><a href="#destacadas" className="text-sm font-semibold text-[#746C63] transition hover:text-coral-600">Mascotas</a><Link to="/login" className="text-sm font-semibold text-[#746C63] transition hover:text-coral-600">Iniciar sesión</Link><Link to="/registro" className="primary-button px-4 py-2.5">Registrarse <ArrowRight size={15} /></Link></div>
        <button type="button" className="rounded-xl p-2 text-ink md:hidden" onClick={() => setMenuOpen(!menuOpen)} title="Abrir menú">{menuOpen ? <X size={23} /> : <Menu size={23} />}</button>
      </div>
      {menuOpen && <div className="border-t border-[#EEE7DD] bg-cream px-5 py-5 md:hidden"><div className="mx-auto flex max-w-7xl flex-col gap-2"><a href="#como-funciona" onClick={() => setMenuOpen(false)} className="rounded-xl px-3 py-3 text-sm font-semibold text-[#746C63] hover:bg-white">Cómo funciona</a><a href="#destacadas" onClick={() => setMenuOpen(false)} className="rounded-xl px-3 py-3 text-sm font-semibold text-[#746C63] hover:bg-white">Mascotas</a><Link to="/login" onClick={() => setMenuOpen(false)} className="rounded-xl px-3 py-3 text-sm font-semibold text-[#746C63] hover:bg-white">Iniciar sesión</Link><Link to="/registro" onClick={() => setMenuOpen(false)} className="primary-button mt-2">Registrarse <ArrowRight size={15} /></Link></div></div>}
    </nav>

    <main>
      <section className="relative isolate min-h-[720px] bg-gradient-to-br from-cream via-[#FFF6EF] to-coral-100/80 pt-[76px]">
        <div className="absolute -right-40 top-32 -z-10 h-[520px] w-[520px] rounded-full border-[54px] border-white/50" />
        <div className="mx-auto grid min-h-[644px] max-w-7xl items-center gap-10 px-5 py-16 sm:px-8 lg:grid-cols-[0.84fr_1.16fr] lg:px-12 lg:py-20">
          <div className="relative z-10 max-w-xl"><p className="eyebrow">Una nueva historia empieza aquí</p><h1 className="mt-5 font-display text-5xl font-bold leading-[0.98] text-ink sm:text-7xl">Encuentra a tu <span className="text-coral-500">nuevo mejor amigo.</span></h1><p className="mt-7 max-w-md text-lg leading-8 text-[#746C63]">Adopta con el corazón y con la información que necesitas. Hay una compañía esperando conocerte.</p><div className="mt-9 flex flex-wrap gap-3"><Link to="/catalogo" className="primary-button px-6 py-4">Ver mascotas disponibles <ArrowRight size={18} /></Link><a href="#como-funciona" className="secondary-button px-5 py-4">Cómo funciona</a></div><div className="mt-9 flex items-center gap-3 text-sm font-semibold text-[#81786F]"><div className="flex -space-x-2"><span className="grid h-8 w-8 place-items-center rounded-full border-2 border-cream bg-sage-300 text-sage-700"><Heart size={14} /></span><span className="grid h-8 w-8 place-items-center rounded-full border-2 border-cream bg-sunshine text-[#8D681D]"><PawPrint size={14} /></span><span className="grid h-8 w-8 place-items-center rounded-full border-2 border-cream bg-coral-300 text-coral-700"><Check size={14} /></span></div> Miles de historias esperan un hogar</div></div>
          <div className="relative flex min-h-[350px] items-end justify-center lg:min-h-[540px]"><div className="absolute bottom-6 h-64 w-[90%] rounded-[48%] bg-coral-300/35 blur-2xl" /><img src={heroPets} alt="Perros y gatos esperando un hogar" className="relative z-10 max-h-[520px] w-full object-contain drop-shadow-[0_25px_20px_rgba(88,63,47,0.16)]" /><div className="absolute bottom-4 right-3 z-20 hidden rounded-2xl border border-white/70 bg-white/85 p-4 shadow-soft sm:block"><ShieldCheck className="text-sage-700" size={23} /><p className="mt-2 text-xs font-bold text-ink">Adopción responsable</p><p className="mt-1 text-[11px] text-[#81786F]">Siempre acompañada</p></div></div>
        </div>
      </section>

      <section id="como-funciona" className="bg-white px-5 py-20 sm:px-8 lg:px-12"><div className="mx-auto max-w-7xl"><div className="max-w-xl"><p className="eyebrow">Tan sencillo como abrir el corazón</p><h2 className="mt-3 font-display text-4xl font-bold sm:text-5xl">Un camino acompañado.</h2><p className="mt-4 text-[#81786F]">Te ayudamos a pasar de la primera mirada al encuentro que cambia dos vidas.</p></div><div className="mt-10 grid gap-5 md:grid-cols-3">{pasos.map(({ icon: Icon, numero, titulo, texto }) => <article key={titulo} className="group rounded-2xl border border-[#EEE7DD] bg-cream p-7 shadow-soft transition hover:-translate-y-1 hover:shadow-float"><div className="flex items-start justify-between"><div className="grid h-14 w-14 place-items-center rounded-2xl bg-white text-coral-500 shadow-sm transition group-hover:bg-coral-500 group-hover:text-white"><Icon size={25} /></div><span className="font-display text-3xl font-bold text-coral-300">{numero}</span></div><h3 className="mt-8 font-display text-2xl font-bold">{titulo}</h3><p className="mt-3 text-sm leading-7 text-[#81786F]">{texto}</p></article>)}</div></div></section>

      <section id="destacadas" className="bg-cream px-5 py-20 sm:px-8 lg:px-12"><div className="mx-auto max-w-7xl"><div className="flex flex-col justify-between gap-5 sm:flex-row sm:items-end"><div><p className="eyebrow">Conoce algunas historias</p><h2 className="mt-3 font-display text-4xl font-bold sm:text-5xl">Mascotas destacadas.</h2></div><Link to="/catalogo" className="inline-flex items-center gap-2 text-sm font-bold text-coral-600 hover:text-coral-700">Ver todo el catálogo <ArrowRight size={17} /></Link></div><div className="mt-10 grid gap-6 sm:grid-cols-2 xl:grid-cols-4">{mascotasDestacadas.map(mascota => <MascotaCard key={mascota.id} mascota={mascota} />)}</div></div></section>

      <section className="px-5 py-20 sm:px-8 lg:px-12"><div className="mx-auto grid max-w-7xl items-center gap-10 rounded-[32px] bg-sage-700 px-7 py-12 text-white sm:px-12 lg:grid-cols-[1fr_1.3fr] lg:py-14"><div><p className="text-xs font-bold uppercase tracking-[0.2em] text-sage-300">El impacto de elegir adoptar</p><h2 className="mt-4 font-display text-4xl font-bold leading-tight sm:text-5xl">Un hogar cambia más de una vida.</h2><p className="mt-5 max-w-md leading-7 text-white/75">Cada adopción responsable abre espacio para seguir cuidando, recuperando y conectando.</p></div><div className="grid gap-4 sm:grid-cols-3"><div className="rounded-2xl bg-white/10 p-5"><Heart className="text-coral-300" size={24} /><p className="mt-6 font-display text-4xl font-bold">1:1</p><p className="mt-1 text-sm text-white/70">encuentro con propósito</p></div><div className="rounded-2xl bg-white/10 p-5"><ShieldCheck className="text-sunshine" size={24} /><p className="mt-6 font-display text-4xl font-bold">100%</p><p className="mt-1 text-sm text-white/70">acompañamiento humano</p></div><div className="rounded-2xl bg-white/10 p-5"><PawPrint className="text-sage-300" size={24} /><p className="mt-6 font-display text-4xl font-bold">∞</p><p className="mt-1 text-sm text-white/70">historias por comenzar</p></div></div></div></section>
    </main>

    <footer className="border-t border-[#EEE7DD] bg-white px-5 py-12 sm:px-8 lg:px-12"><div className="mx-auto grid max-w-7xl gap-10 sm:grid-cols-2 lg:grid-cols-[1.5fr_1fr_1fr]"><div><Link to="/" className="flex items-center gap-0"><img src={logo} alt="Adopet" className="relative -top-1 -mr-2 h-12 w-12 object-contain" /><span className="font-display text-2xl font-bold">adopet<span className="text-coral-500">.</span></span></Link><p className="mt-4 max-w-xs text-sm leading-6 text-[#81786F]">Adopciones más humanas, historias que empiezan con cuidado.</p></div><div><h3 className="font-display text-lg font-bold">Enlaces rápidos</h3><div className="mt-4 flex flex-col gap-3 text-sm text-[#81786F]"><Link to="/catalogo" className="hover:text-coral-600">Catálogo</Link><a href="#como-funciona" className="hover:text-coral-600">Cómo funciona</a><Link to="/login" className="hover:text-coral-600">Iniciar sesión</Link></div></div><div><h3 className="font-display text-lg font-bold">Proyecto académico</h3><p className="mt-4 text-sm leading-6 text-[#81786F]">Fundamentos de Ingeniería de Software<br />Universidad Distrital<br />Equipo Adopet</p></div></div><div className="mx-auto mt-10 max-w-7xl border-t border-[#EEE7DD] pt-5 text-xs text-[#A1988D]">© 2026 Adopet · Construido con cuidado para encontrar hogares.</div></footer>
  </div>
}
