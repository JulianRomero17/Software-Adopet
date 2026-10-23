import { CheckCircle2, HeartPulse, ShieldCheck, Syringe, X } from 'lucide-react'

export default function PetDetailsModal({ pet, requested, onClose, onRequest }) {
  if (!pet) return null

  const details = [
    ['Raza', pet.raza || 'Sin raza definida'],
    ['Edad', pet.edadTexto || `${pet.edad} años`],
    ['Sexo', pet.sexo === 'HEMBRA' ? 'Hembra' : 'Macho'],
    ['Tamaño', pet.tamano || 'Por conocer'],
  ]

  return <div className="fixed inset-0 z-50 grid place-items-center bg-ink/40 p-4 backdrop-blur-sm" role="dialog" aria-modal="true" aria-labelledby="pet-modal-title" onMouseDown={onClose}>
    <div className="max-h-[90vh] w-full max-w-3xl overflow-y-auto rounded-3xl bg-white shadow-float" onMouseDown={event => event.stopPropagation()}>
      <div className="grid md:grid-cols-[0.9fr_1.1fr]">
        <div className="relative min-h-72 bg-sage-100 md:min-h-full">
          <img src={pet.imagenUrl} alt={pet.nombre} className="h-full min-h-72 w-full object-cover" />
          <button type="button" onClick={onClose} className="absolute right-4 top-4 grid h-10 w-10 place-items-center rounded-full bg-white/90 text-ink shadow-sm hover:bg-white" aria-label="Cerrar detalles"><X size={20} /></button>
        </div>
        <div className="p-6 sm:p-8">
          <div className="flex items-start justify-between gap-4"><div><p className="eyebrow">Conoce su historia</p><h2 id="pet-modal-title" className="mt-2 font-display text-4xl font-bold text-ink">{pet.nombre}</h2><p className="mt-1 text-sm font-semibold uppercase tracking-wider text-[#9C9389]">{pet.especie} · {pet.raza}</p></div><span className="shrink-0 rounded-full bg-sage-50 px-3 py-1 text-xs font-bold text-sage-700">Disponible</span></div>
          <p className="mt-6 text-sm leading-7 text-[#746C63]">{pet.descripcion}</p>
          <div className="mt-6 grid grid-cols-2 gap-3">{details.map(([label, value]) => <div key={label} className="rounded-2xl bg-cream p-3"><p className="text-[11px] font-bold uppercase tracking-wider text-[#9C9389]">{label}</p><p className="mt-1 text-sm font-bold text-ink">{value}</p></div>)}</div>
          <div className="mt-5 grid gap-3 sm:grid-cols-3"><div className="flex items-center gap-2 rounded-xl border border-sage-100 px-3 py-3 text-xs font-bold text-sage-700"><Syringe size={17} /> Vacunado</div><div className="flex items-center gap-2 rounded-xl border border-sage-100 px-3 py-3 text-xs font-bold text-sage-700"><CheckCircle2 size={17} /> Esterilizado</div><div className="flex items-center gap-2 rounded-xl border border-sage-100 px-3 py-3 text-xs font-bold text-sage-700"><HeartPulse size={17} /> Salud estable</div></div>
          <div className="mt-6 flex items-start gap-3 rounded-2xl bg-sage-50 p-4 text-sm leading-6 text-sage-700"><ShieldCheck className="mt-0.5 shrink-0" size={19} /><p>Su información está preparada para que conozcas sus necesidades antes de enviar una solicitud de adopción.</p></div>
          <div className="mt-7 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end"><button type="button" onClick={onClose} className="secondary-button">Cerrar</button><button type="button" onClick={() => onRequest(pet)} disabled={requested} className="primary-button">{requested ? 'Solicitud enviada' : `Quiero solicitar a ${pet.nombre}`}</button></div>
        </div>
      </div>
    </div>
  </div>
}
