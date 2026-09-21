import { ArrowUpRight, Heart } from 'lucide-react'
import { Link } from 'react-router-dom'

export default function MascotaCard({ mascota }) {
  return (
    <article className="group overflow-hidden rounded-2xl border border-[#EEE7DD] bg-white shadow-soft transition duration-300 hover:-translate-y-1 hover:shadow-float">
      <div className="relative flex aspect-[4/3] items-end justify-center overflow-hidden bg-sage-50 p-4">
        <img src={mascota.imagen} alt={mascota.nombre} className="h-full w-full object-contain transition duration-500 group-hover:scale-105" />
        <div className="absolute left-4 top-4 rounded-full bg-white/90 px-3 py-1 text-[11px] font-bold uppercase tracking-wider text-sage-700 shadow-sm">Disponible</div>
        <button type="button" className="absolute right-4 top-4 grid h-9 w-9 place-items-center rounded-full bg-white/90 text-coral-500 shadow-sm transition hover:bg-white" title={`Guardar a ${mascota.nombre}`}><Heart size={17} /></button>
      </div>
      <div className="flex items-end justify-between gap-3 p-5">
        <div><h3 className="font-display text-2xl font-bold text-ink">{mascota.nombre}</h3><p className="mt-1 text-xs font-semibold uppercase tracking-wider text-[#9C9389]">{mascota.especie} · {mascota.raza}</p></div>
        <Link to="/catalogo" className="grid h-10 w-10 shrink-0 place-items-center rounded-xl bg-coral-50 text-coral-600 transition hover:bg-coral-100" title={`Ver detalle de ${mascota.nombre}`}><ArrowUpRight size={19} /></Link>
      </div>
    </article>
  )
}
