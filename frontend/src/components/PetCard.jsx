import { Heart, MapPin } from 'lucide-react'
import StatusPill from './StatusPill'
import logo from '../../images/Logo_Adopet.png'

export default function PetCard({ pet, onRequest, requested }) {
  return (
    <article className="group overflow-hidden rounded-2xl border border-[#EEE7DD] bg-white shadow-soft transition duration-300 hover:-translate-y-1 hover:shadow-float">
      <div className="relative aspect-[4/3] overflow-hidden bg-sage-100">
        {pet.imagenUrl ? <img src={pet.imagenUrl} alt={pet.nombre} className="h-full w-full object-cover transition duration-500 group-hover:scale-105" /> : <div className="flex h-full items-center justify-center bg-sage-50"><img src={logo} alt="Adopet" className="h-24 w-24 object-contain opacity-50" /></div>}
        <div className="absolute left-4 top-4"><StatusPill status={pet.estado} /></div>
        <button className="absolute right-4 top-4 grid h-9 w-9 place-items-center rounded-full bg-white/90 text-coral-500 shadow-sm transition hover:bg-white" title="Guardar mascota"><Heart size={17} /></button>
      </div>
      <div className="p-5">
        <div className="flex items-start justify-between gap-3">
          <div><h3 className="font-display text-2xl font-bold text-ink">{pet.nombre}</h3><p className="mt-1 text-xs font-semibold uppercase tracking-wider text-[#9C9389]">{pet.especie} · {pet.raza || 'Sin raza'}</p></div>
          <span className="rounded-lg bg-cream px-2.5 py-1 text-xs font-bold text-[#80776D]">{pet.edad} años</span>
        </div>
        <div className="mt-4 flex items-center gap-2 text-sm text-[#777069]"><MapPin size={15} className="text-coral-500" /> {pet.tamano || 'Tamaño por conocer'} <span className="text-[#D5CEC4]">•</span> {pet.sexo}</div>
        <p className="mt-3 line-clamp-2 text-sm leading-6 text-[#777069]">{pet.descripcion || 'Una nueva historia puede comenzar contigo.'}</p>
        {onRequest && <button className="primary-button mt-5 w-full" onClick={() => onRequest(pet)} disabled={requested}>{requested ? 'Solicitud enviada' : 'Quiero conocerle'}</button>}
      </div>
    </article>
  )
}
