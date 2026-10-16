import { useEffect, useState } from 'react'
import { CheckCircle2, ClipboardList, Clock3, PawPrint, XCircle } from 'lucide-react'
import StatusPill from '../components/StatusPill'
import { petsApi, requestsApi } from '../services/api'
import { PROVISIONAL_MODE } from '../config'
import { readHistory } from '../services/localStore'

const decisionMessages = {
  APROBADA: {
    title: '¡Tu solicitud fue aprobada!',
    description: 'El equipo aprobó que inicies el proceso para adoptar a esta mascota.',
    Icon: CheckCircle2,
    styles: 'border-[#EEE7DD] bg-sage-50 text-sage-700',
  },
  RECHAZADA: {
    title: 'Tu solicitud no fue aprobada',
    description: 'El equipo revisó tu solicitud y decidió no continuar con esta adopción.',
    Icon: XCircle,
    styles: 'border-[#EEE7DD] bg-coral-50 text-coral-700',
  },
}

function formatDate(value) {
  if (!value) return 'Fecha no disponible'
  const date = /^\d{4}-\d{2}-\d{2}$/.test(value)
    ? new Date(`${value}T12:00:00`)
    : new Date(value)
  return Number.isNaN(date.getTime()) ? value : date.toLocaleDateString('es', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  })
}

export default function HistoryPage() {
  const [history, setHistory] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [petDetailsError, setPetDetailsError] = useState(false)

  useEffect(() => {
    let active = true

    async function loadHistory() {
      if (PROVISIONAL_MODE) {
        setHistory(readHistory())
        setLoading(false)
        return
      }

      try {
        const { data } = await requestsApi.history()
        const results = await Promise.allSettled(data.map(async request => {
          if (!request.mascotaId) return request
          const { data: mascota } = await petsApi.detail(request.mascotaId)
          return { ...request, mascota }
        }))
        if (!active) return

        setPetDetailsError(results.some(result => result.status === 'rejected'))
        setHistory(results.map((result, index) => {
          if (result.status === 'fulfilled') return result.value
          return data[index]
        }))
      } catch (requestError) {
        if (active) setError(requestError.response?.data?.mensaje || 'No pudimos cargar tu historial. Inténtalo de nuevo más tarde.')
      } finally {
        if (active) setLoading(false)
      }
    }

    loadHistory()
    return () => { active = false }
  }, [])

  return <div>
    <header>
      <p className="eyebrow">Tu proceso</p>
      <h1 className="mt-2 font-display text-4xl font-bold text-ink">Mis solicitudes.</h1>
      <p className="mt-3 text-[#81786F]">Consulta el resultado y los detalles de cada proceso de adopción.</p>
    </header>

    {error && <div role="alert" className="mt-8 rounded-2xl bg-coral-50 p-5 text-sm font-semibold text-coral-700">{error}</div>}
    {petDetailsError && <div role="status" className="mt-6 rounded-2xl bg-[#FFF2D8] p-4 text-sm text-[#805B19]">No fue posible cargar los datos de una mascota. El estado de tu solicitud sí está disponible.</div>}

    {loading ? <div className="surface mt-10 h-56 animate-pulse bg-[#F1ECE5]" /> : history.length ? <div className="mt-8 space-y-5">
      {history.map(item => {
        const decision = decisionMessages[item.estado]
        const Icon = decision?.Icon || (item.estado === 'FINALIZADA' ? ClipboardList : Clock3)
        const pet = item.mascota
        const petName = pet?.nombre || item.mascotaNombre || `Mascota #${item.mascotaId}`

        return <article key={item.id} className="surface overflow-hidden">
          <div className="flex flex-col gap-5 p-5 sm:flex-row sm:items-center sm:p-6">
            {pet?.imagenUrl ? <img src={pet.imagenUrl} alt={petName} className="h-24 w-24 shrink-0 rounded-2xl object-cover" /> : <div className="grid h-16 w-16 shrink-0 place-items-center rounded-2xl bg-sage-100 text-sage-700"><PawPrint size={27} /></div>}
            <div className="min-w-0 flex-1">
              <p className="eyebrow">Mascota que elegiste</p>
              <h2 className="mt-1 font-display text-2xl font-bold text-ink">{petName}</h2>
              <p className="mt-1 text-sm text-[#81786F]">
                Solicitud #{item.id} · Enviada el {formatDate(item.fecha)}
                {item.fechaActualizacion && item.fechaActualizacion !== item.fecha ? ` · Actualizada el ${formatDate(item.fechaActualizacion)}` : ''}
              </p>
            </div>
            <StatusPill status={item.estado} />
          </div>

          <div className={`mx-5 mb-5 flex gap-3 rounded-2xl border p-4 sm:mx-6 ${decision?.styles || 'border-[#F0E2C5] bg-[#FFFAEE] text-[#805B19]'}`}>
            <Icon className="mt-0.5 shrink-0" size={21} />
            <div>
              <h3 className="font-bold">{decision?.title || (item.estado === 'FINALIZADA' ? 'Este proceso ha finalizado' : 'Tu solicitud sigue en revisión')}</h3>
              <p className="mt-1 text-sm leading-5">
                {decision?.description || (item.estado === 'FINALIZADA'
                  ? 'Consulta las observaciones del equipo para conocer más sobre el cierre de tu solicitud.'
                  : 'El administrador todavía no ha tomado una decisión. Te mostraremos el resultado aquí.')}
              </p>
            </div>
          </div>

          <div className="border-t border-[#F1ECE5] px-5 py-4 sm:px-6">
            <p className="text-xs font-bold uppercase tracking-wider text-[#9A9187]">Observaciones del equipo</p>
            <p className="mt-1 text-sm leading-6 text-[#746C63]">{item.observaciones || 'El equipo no agregó observaciones.'}</p>
          </div>
        </article>
      })}
    </div> : !error && <div className="surface mt-10 flex flex-col items-center px-6 py-20 text-center">
      <div className="grid h-14 w-14 place-items-center rounded-2xl bg-sage-100 text-sage-700"><ClipboardList size={26} /></div>
      <h2 className="mt-5 font-display text-2xl font-bold">Tu historial está esperando</h2>
      <p className="mt-2 max-w-sm text-sm leading-6 text-[#81786F]">Cuando envíes una solicitud, podrás consultar aquí el estado y la respuesta del equipo.</p>
    </div>}
  </div>
}
