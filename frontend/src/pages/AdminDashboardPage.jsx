import { useEffect, useState } from 'react'
import { Check, ClipboardList, Mail, MapPin, PawPrint, Phone, Plus, Search, ShieldCheck, UserRound, X } from 'lucide-react'
import { petsApi, requestsApi, usersApi } from '../services/api'
import { PROVISIONAL_MODE } from '../config'
import provisionalPets from '../data/provisionalPets'
import { decideLocalRequest, readPets, readRequests, savePets } from '../services/localStore'
import StatusPill from '../components/StatusPill'
import Toast from '../components/Toast'
import logo from '../../images/Logo_Adopet.png'

const EMPTY_PET = {
  nombre: '',
  especie: 'PERRO',
  raza: '',
  sexo: 'MACHO',
  edad: 1,
  tamano: 'MEDIANO',
  descripcion: '',
  estadoSalud: 'Salud estable',
  imagenUrl: '',
}

export default function AdminDashboardPage() {
  const [pending, setPending] = useState([])
  const [pets, setPets] = useState([])
  const [toast, setToast] = useState('')
  const [decision, setDecision] = useState(null)
  const [note, setNote] = useState('')
  const [showForm, setShowForm] = useState(false)
  const [reviewingId, setReviewingId] = useState(null)
  const [form, setForm] = useState(EMPTY_PET)

  async function load() {
    try {
      if (PROVISIONAL_MODE) {
        const localPets = readPets(provisionalPets)
        setPets(localPets)
        setPending(readRequests().filter(request => ['PENDIENTE', 'EN_REVISION'].includes(request.estado)))
        return
      }

      const [requests, petsResponse] = await Promise.all([requestsApi.pending(), petsApi.list()])
      setPending(requests.data)
      setPets(petsResponse.data)
    } catch (err) {
      setToast(err.response?.data?.mensaje || err.message || 'No pudimos cargar el panel.')
    }
  }

  useEffect(() => { load() }, [])

  async function decide(status) {
    try {
      if (PROVISIONAL_MODE) decideLocalRequest(decision.id, status, note)
      else await requestsApi.decide(decision.id, { decision: status, observaciones: note })
      setToast(status === 'APROBADA' ? 'Solicitud aprobada y mascota adoptada.' : 'Solicitud rechazada.')
      setDecision(null)
      setNote('')
      await load()
    } catch (err) {
      setToast(err.response?.data?.mensaje || err.message || 'No pudimos actualizar la solicitud.')
    }
  }

  async function reviewRequest(request) {
    setReviewingId(request.id)
    try {
      if (PROVISIONAL_MODE) {
        const applicant = request.adoptante
        const pet = request.mascota || pets.find(item => item.id === request.mascotaId)
        setDecision({ ...request, adoptante: applicant, mascota: pet })
      } else {
        const [petResponse, applicantResponse] = await Promise.all([
          petsApi.detail(request.mascotaId),
          usersApi.detail(request.adoptanteId),
        ])
        setDecision({ ...request, mascota: petResponse.data, adoptante: applicantResponse.data })
      }
      setNote('')
    } catch (err) {
      setToast(err.response?.data?.mensaje || err.message || 'No pudimos cargar los datos completos de la solicitud.')
    } finally {
      setReviewingId(null)
    }
  }

  async function createPet(event) {
    event.preventDefault()
    const payload = { ...form, edad: Number(form.edad), estado: 'DISPONIBLE', edadTexto: `${form.edad} ${Number(form.edad) === 1 ? 'año' : 'años'}` }
    try {
      if (PROVISIONAL_MODE) {
        const currentPets = readPets(provisionalPets)
        const id = Math.max(0, ...currentPets.map(pet => Number(pet.id) || 0)) + 1
        savePets([{ ...payload, id }, ...currentPets])
      } else {
        await petsApi.create(payload)
      }
      setToast('Mascota registrada como disponible.')
      setForm(EMPTY_PET)
      setShowForm(false)
      await load()
    } catch (err) {
      setToast(err.response?.data?.mensaje || err.message || 'No pudimos registrar la mascota.')
    }
  }

  function toggleAvailability(pet) {
    const estado = pet.estado === 'DISPONIBLE' ? 'NO_DISPONIBLE' : 'DISPONIBLE'
    savePets(readPets(provisionalPets).map(item => item.id === pet.id ? { ...item, estado } : item))
    setPets(readPets(provisionalPets))
    setToast(estado === 'DISPONIBLE' ? `${pet.nombre} vuelve al catálogo.` : `${pet.nombre} ya no aparece en el catálogo.`)
  }

  const availableCount = pets.filter(pet => pet.estado === 'DISPONIBLE').length

  return <div>
    <header className="flex flex-col justify-between gap-6 md:flex-row md:items-end">
      <div><p className="eyebrow">Panel de gestión</p><h1 className="mt-2 font-display text-4xl font-bold text-ink">Hola, equipo Adopet.</h1><p className="mt-3 text-[#81786F]">Administra las mascotas y acompaña cada solicitud de adopción.</p></div>
      <button onClick={() => setShowForm(!showForm)} className="primary-button"><Plus size={18} /> Registrar mascota</button>
    </header>

    <div className="mt-10 grid gap-4 sm:grid-cols-3">
      <div className="surface border-l-4 border-l-coral-500 p-5"><p className="text-sm font-semibold text-[#81786F]">En revisión</p><p className="mt-2 font-display text-4xl font-bold text-ink">{pending.length}</p><p className="mt-1 text-xs text-[#A1988D]">solicitudes por atender</p></div>
      <div className="surface border-l-4 border-l-sage-500 p-5"><p className="text-sm font-semibold text-[#81786F]">Disponibles</p><p className="mt-2 font-display text-4xl font-bold text-ink">{availableCount}</p><p className="mt-1 text-xs text-[#A1988D]">mascotas en catálogo</p></div>
      <div className="surface border-l-4 border-l-sunshine p-5"><p className="text-sm font-semibold text-[#81786F]">Cuidado primero</p><div className="mt-3 flex items-center gap-2 text-sm font-bold text-sage-700"><img src={logo} alt="Adopet" className="h-6 w-6 object-contain" /> Cada caso es único</div></div>
    </div>

    {showForm && <form onSubmit={createPet} className="surface mt-6 p-6">
      <div className="flex items-center justify-between"><div><p className="eyebrow">Catálogo</p><h2 className="mt-1 font-display text-2xl font-bold">Registrar mascota</h2></div><button type="button" onClick={() => setShowForm(false)} className="rounded-lg p-2 text-[#8A8178]" aria-label="Cerrar formulario"><X size={18} /></button></div>
      <div className="mt-5 grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        <label><span className="mb-2 block text-xs font-bold uppercase tracking-wider text-[#81786F]">Nombre</span><input className="field" required value={form.nombre} onChange={event => setForm({ ...form, nombre: event.target.value })} /></label>
        <label><span className="mb-2 block text-xs font-bold uppercase tracking-wider text-[#81786F]">Especie</span><select className="field" value={form.especie} onChange={event => setForm({ ...form, especie: event.target.value })}><option value="PERRO">Perro</option><option value="GATO">Gato</option><option value="OTRO">Otro</option></select></label>
        <label><span className="mb-2 block text-xs font-bold uppercase tracking-wider text-[#81786F]">Raza</span><input className="field" value={form.raza} onChange={event => setForm({ ...form, raza: event.target.value })} placeholder="Sin raza definida" /></label>
        <label><span className="mb-2 block text-xs font-bold uppercase tracking-wider text-[#81786F]">Sexo</span><select className="field" value={form.sexo} onChange={event => setForm({ ...form, sexo: event.target.value })}><option value="MACHO">Macho</option><option value="HEMBRA">Hembra</option></select></label>
        <label><span className="mb-2 block text-xs font-bold uppercase tracking-wider text-[#81786F]">Edad (años)</span><input className="field" type="number" min="0" required value={form.edad} onChange={event => setForm({ ...form, edad: event.target.value })} /></label>
        <label><span className="mb-2 block text-xs font-bold uppercase tracking-wider text-[#81786F]">Tamaño</span><select className="field" value={form.tamano} onChange={event => setForm({ ...form, tamano: event.target.value })}><option value="PEQUENO">Pequeño</option><option value="MEDIANO">Mediano</option><option value="GRANDE">Grande</option></select></label>
        <label><span className="mb-2 block text-xs font-bold uppercase tracking-wider text-[#81786F]">Estado de salud</span><input className="field" value={form.estadoSalud} onChange={event => setForm({ ...form, estadoSalud: event.target.value })} /></label>
        <label><span className="mb-2 block text-xs font-bold uppercase tracking-wider text-[#81786F]">URL de imagen</span><input className="field" type="url" value={form.imagenUrl} onChange={event => setForm({ ...form, imagenUrl: event.target.value })} placeholder="https://..." /></label>
        <label className="sm:col-span-2 lg:col-span-3"><span className="mb-2 block text-xs font-bold uppercase tracking-wider text-[#81786F]">Descripción</span><textarea className="field min-h-24" value={form.descripcion} onChange={event => setForm({ ...form, descripcion: event.target.value })} /></label>
      </div>
      <button className="primary-button mt-5"><Plus size={17} /> Guardar mascota</button>
    </form>}

    <section className="surface mt-8 overflow-hidden">
      <div className="flex items-center justify-between border-b border-[#EEE7DD] px-6 py-5"><div><h2 className="font-display text-2xl font-bold">Solicitudes por revisar</h2><p className="mt-1 text-sm text-[#9A9187]">Revisa cada historia antes de decidir.</p></div><ClipboardList className="text-coral-500" /></div>
      {pending.length ? <div className="divide-y divide-[#F1ECE5]">{pending.map(item => <div key={item.id} className="flex flex-col gap-4 px-6 py-5 md:flex-row md:items-center md:justify-between"><div><div className="flex items-center gap-3"><span className="font-bold text-ink">Solicitud #{item.id}</span><StatusPill status={item.estado} /></div><p className="mt-2 text-sm text-[#81786F]">{item.adoptante?.nombre || item.adoptanteCorreo || `Adoptante #${item.adoptanteId}`} · {item.mascota?.nombre || item.mascotaNombre || `Mascota #${item.mascotaId}`} · {item.fecha}</p></div><button onClick={() => reviewRequest(item)} disabled={reviewingId === item.id} className="secondary-button self-start md:self-auto"><Search size={16} /> {reviewingId === item.id ? 'Cargando...' : 'Ver candidato y mascota'}</button></div>)}</div> : <div className="px-6 py-14 text-center text-sm text-[#81786F]">No hay solicitudes pendientes por ahora.</div>}
    </section>

    <section className="surface mt-8 overflow-hidden">
      <div className="flex items-center justify-between border-b border-[#EEE7DD] px-6 py-5"><div><h2 className="font-display text-2xl font-bold">Mascotas registradas</h2><p className="mt-1 text-sm text-[#9A9187]">Controla qué mascotas aparecen en el catálogo.</p></div><PawPrint className="text-sage-600" /></div>
      <div className="divide-y divide-[#F1ECE5]">{pets.map(pet => <div key={pet.id} className="flex flex-col gap-4 px-6 py-4 sm:flex-row sm:items-center sm:justify-between"><div className="flex items-center gap-4"><div className="grid h-12 w-12 shrink-0 place-items-center overflow-hidden rounded-xl bg-sage-50">{pet.imagenUrl ? <img src={pet.imagenUrl} alt="" className="h-full w-full object-cover" /> : <PawPrint size={20} className="text-sage-600" />}</div><div><p className="font-bold text-ink">{pet.nombre}</p><p className="mt-1 text-xs text-[#81786F]">{pet.especie} · {pet.raza || 'Sin raza'} · {pet.edadTexto || `${pet.edad} años`}</p></div></div><div className="flex items-center gap-3"><StatusPill status={pet.estado} />{PROVISIONAL_MODE && pet.estado !== 'ADOPTADA' && <button type="button" onClick={() => toggleAvailability(pet)} className="text-xs font-bold text-sage-700 hover:text-sage-900">{pet.estado === 'DISPONIBLE' ? 'Ocultar' : 'Publicar'}</button>}</div></div>)}</div>
    </section>

    {decision && <div className="fixed inset-0 z-40 grid place-items-center overflow-y-auto bg-ink/50 p-4 backdrop-blur-sm" role="dialog" aria-modal="true" aria-labelledby="request-title"><div className="surface my-auto max-h-[94vh] w-full max-w-5xl overflow-y-auto p-5 sm:p-8">
      <div className="flex items-start justify-between gap-4"><div><p className="eyebrow">Evaluación del candidato</p><h2 id="request-title" className="mt-1 font-display text-3xl font-bold">Solicitud #{decision.id}</h2><p className="mt-2 text-sm text-[#81786F]">Revisa la información del adoptante y de la mascota antes de tomar una decisión.</p></div><button onClick={() => setDecision(null)} className="rounded-lg p-2 text-[#8A8178]" aria-label="Cerrar detalles de solicitud"><X size={19} /></button></div>
      <div className="mt-6 grid gap-6 lg:grid-cols-2">
        <section className="overflow-hidden rounded-2xl border border-[#EEE7DD] bg-cream/50">
          {decision.mascota?.imagenUrl ? <img src={decision.mascota.imagenUrl} alt={decision.mascota.nombre} className="h-56 w-full object-cover" /> : <div className="grid h-40 place-items-center bg-sage-50"><PawPrint size={36} className="text-sage-500" /></div>}
          <div className="p-5"><div className="flex items-start justify-between gap-3"><div><p className="eyebrow">Mascota solicitada</p><h3 className="mt-1 font-display text-2xl font-bold text-ink">{decision.mascota?.nombre || decision.mascotaNombre || `Mascota #${decision.mascotaId}`}</h3></div>{decision.mascota?.estado && <StatusPill status={decision.mascota.estado} />}</div>
            <p className="mt-3 text-sm leading-6 text-[#746C63]">{decision.mascota?.descripcion || 'Sin descripción registrada.'}</p>
            <div className="mt-4 grid grid-cols-2 gap-3">
              <InfoItem label="Especie" value={decision.mascota?.especie} />
              <InfoItem label="Raza" value={decision.mascota?.raza} />
              <InfoItem label="Sexo" value={decision.mascota?.sexo} />
              <InfoItem label="Edad" value={decision.mascota?.edadTexto || (decision.mascota?.edad != null ? `${decision.mascota.edad} años` : '')} />
              <InfoItem label="Tamaño" value={decision.mascota?.tamano} />
              <InfoItem label="Salud" value={decision.mascota?.estadoSalud} />
              <InfoItem label="Vacunación" value={decision.mascota?.vacunacion} />
              <InfoItem label="Esterilización" value={decision.mascota?.esterilizacion} />
            </div>
          </div>
        </section>
        <section className="rounded-2xl border border-[#EEE7DD] bg-white p-5">
          <div className="flex items-center gap-3"><div className="grid h-11 w-11 place-items-center rounded-full bg-sage-100 text-sage-700"><UserRound size={21} /></div><div><p className="eyebrow">Datos del solicitante</p><h3 className="font-display text-xl font-bold text-ink">{decision.adoptante?.nombre || 'Nombre no registrado'}</h3></div></div>
          <div className="mt-5 space-y-3">
            <InfoLine icon={ShieldCheck} label="Documento" value={decision.adoptante?.documento} />
            <InfoLine icon={Mail} label="Correo" value={decision.adoptante?.correo || decision.adoptanteCorreo} />
            <InfoLine icon={Phone} label="Teléfono" value={decision.adoptante?.telefono} />
            <InfoLine icon={MapPin} label="Dirección" value={decision.adoptante?.direccion} />
            {decision.adoptante?.activo !== undefined && <InfoLine icon={UserRound} label="Estado de cuenta" value={decision.adoptante.activo ? 'Activa' : 'Inactiva'} />}
          </div>
          <div className="mt-6 border-t border-[#EEE7DD] pt-5"><h4 className="font-display text-lg font-bold text-ink">Información para evaluar la adopción</h4>{Object.keys(decision.informacionAdopcion || {}).length ? <div className="mt-3 space-y-3">{[['tipoVivienda', 'Tipo de vivienda'], ['permisoMascotas', 'Permiso para mascotas'], ['experiencia', 'Experiencia cuidando mascotas'], ['tiempoDisponible', 'Tiempo para su cuidado'], ['motivacion', 'Motivación para adoptar']].map(([key, label]) => <InfoBlock key={key} label={label} value={decision.informacionAdopcion[key]} />)}</div> : <p className="mt-2 text-sm leading-6 text-[#81786F]">Esta solicitud se creó antes del formulario de evaluación; muestra los datos de contacto disponibles.</p>}</div>
        </section>
      </div>
      <label className="mt-6 block"><span className="mb-2 block text-sm font-bold text-ink">Observaciones de la decisión</span><textarea className="field min-h-24" placeholder="Registra el motivo de aprobación o rechazo para el historial del adoptante." value={note} onChange={event => setNote(event.target.value)} /></label>
      <div className="mt-5 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end"><button onClick={() => setDecision(null)} className="secondary-button">Volver a solicitudes</button><button onClick={() => decide('RECHAZADA')} className="rounded-xl border border-coral-200 px-5 py-3 text-sm font-bold text-coral-700 hover:bg-coral-50"><X size={16} className="mr-1 inline" /> Rechazar candidato</button><button onClick={() => decide('APROBADA')} className="primary-button"><Check size={16} /> Aprobar adopción</button></div>
    </div></div>}
    <Toast message={toast} type={toast.includes('No pudimos') || toast.includes('No encontramos') ? 'error' : 'success'} onClose={() => setToast('')} />
  </div>
}

function InfoItem({ label, value }) {
  return <div className="rounded-xl bg-white p-3"><p className="text-[10px] font-bold uppercase tracking-wider text-[#9A9187]">{label}</p><p className="mt-1 text-sm font-semibold text-ink">{value || 'No registrado'}</p></div>
}

function InfoLine({ icon: Icon, label, value }) {
  return <div className="flex items-start gap-3 text-sm"><Icon size={17} className="mt-0.5 shrink-0 text-sage-700" /><div><span className="font-semibold text-[#81786F]">{label}: </span><span className="text-ink">{value || 'No registrado'}</span></div></div>
}

function InfoBlock({ label, value }) {
  return <div className="rounded-xl bg-cream p-3"><p className="text-[10px] font-bold uppercase tracking-wider text-[#9A9187]">{label}</p><p className="mt-1 whitespace-pre-wrap text-sm leading-5 text-ink">{value || 'No registrado'}</p></div>
}
