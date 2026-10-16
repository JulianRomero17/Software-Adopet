import { DEMO_ACCOUNT } from '../config'

const FAVORITES_KEY = 'adopet_favoritos'
const HISTORY_KEY = 'adopet_historial'
const PETS_KEY = 'adopet_mascotas'
const REQUESTS_KEY = 'adopet_solicitudes'
const USERS_KEY = 'adopet_usuarios'

function readList(key) {
  try {
    const value = JSON.parse(localStorage.getItem(key) || '[]')
    return Array.isArray(value) ? value : []
  } catch {
    return []
  }
}

function saveList(key, value) {
  localStorage.setItem(key, JSON.stringify(value))
}

export function readFavorites() {
  return readList(FAVORITES_KEY)
}

export function saveFavorites(favorites) {
  saveList(FAVORITES_KEY, favorites)
}

export function readHistory() {
  return readList(HISTORY_KEY)
}

export function saveHistory(history) {
  saveList(HISTORY_KEY, history)
}

export function readPets(initialPets = []) {
  const stored = localStorage.getItem(PETS_KEY)
  if (stored !== null) return readList(PETS_KEY)
  saveList(PETS_KEY, initialPets)
  return initialPets
}

export function savePets(pets) {
  saveList(PETS_KEY, pets)
}

export function readRequests() {
  const requests = readList(REQUESTS_KEY)
  const requestIds = new Set(requests.map(request => request.id))
  const legacyPending = readHistory().filter(item =>
    ['PENDIENTE', 'EN_REVISION'].includes(item.estado) && !requestIds.has(item.id)
  ).map(item => ({ ...item, adoptanteCorreo: item.adoptanteCorreo || DEMO_ACCOUNT.correo }))

  const allRequests = legacyPending.length ? [...legacyPending, ...requests] : requests
  const pets = readList(PETS_KEY)
  const enriched = allRequests.map(request => {
    const applicant = findLocalUser(request.adoptanteCorreo)
    const pet = pets.find(item => item.id === request.mascotaId)
    return {
      ...request,
      adoptante: request.adoptante || (applicant ? {
        nombre: applicant.nombre,
        documento: applicant.documento,
        correo: applicant.correo,
        telefono: applicant.telefono,
        direccion: applicant.direccion,
      } : { correo: request.adoptanteCorreo, nombre: 'Información del adoptante no disponible' }),
      mascota: request.mascota || pet,
      informacionAdopcion: request.informacionAdopcion || {},
    }
  })

  const needsEnrichment = legacyPending.length || allRequests.some(request =>
    !request.adoptante || !request.mascota || !request.informacionAdopcion
  )
  if (needsEnrichment) {
    saveRequests(enriched)
    return enriched
  }
  return allRequests
}

export function saveRequests(requests) {
  saveList(REQUESTS_KEY, requests)
}

export function findLocalUser(correo) {
  if (correo === DEMO_ACCOUNT.correo) return DEMO_ACCOUNT
  return readList(USERS_KEY).find(user => user.correo === correo) || null
}

export function registerLocalUser(user) {
  const users = readList(USERS_KEY)
  if (users.some(existing => existing.correo.toLowerCase() === user.correo.toLowerCase())) {
    throw new Error('Ya existe una cuenta con este correo electrónico.')
  }
  if (users.some(existing => existing.documento === user.documento)) {
    throw new Error('Ya existe una cuenta con este documento.')
  }
  const nextUser = { ...user, rol: 'ADOPTANTE' }
  saveList(USERS_KEY, [...users, nextUser])
  return nextUser
}

export function createLocalRequest(pet, applicant, application) {
  const requests = readRequests()
  if (requests.some(item => item.mascotaId === pet.id && item.adoptanteCorreo === applicant.correo && ['PENDIENTE', 'EN_REVISION', 'APROBADA'].includes(item.estado))) {
    throw new Error('Ya tienes una solicitud registrada para esta mascota.')
  }

  const request = {
    id: Date.now(),
    adoptanteCorreo: applicant.correo,
    adoptante: {
      nombre: applicant.nombre,
      documento: applicant.documento,
      correo: applicant.correo,
      telefono: applicant.telefono,
      direccion: applicant.direccion,
    },
    mascota: { ...pet },
    informacionAdopcion: { ...application },
    mascotaId: pet.id,
    mascotaNombre: pet.nombre,
    fecha: new Date().toISOString().slice(0, 10),
    estado: 'PENDIENTE',
    observaciones: '',
  }
  saveRequests([request, ...requests])
  saveHistory([request, ...readHistory()])
  return request
}

export function decideLocalRequest(id, decision, observaciones) {
  const requests = readRequests()
  const request = requests.find(item => item.id === id)
  if (!request) throw new Error('No encontramos la solicitud seleccionada.')

  const updatedRequest = {
    ...request,
    estado: decision,
    observaciones,
    fechaActualizacion: new Date().toISOString().slice(0, 10),
  }
  saveRequests(requests.map(item => item.id === id ? updatedRequest : item))

  const history = readHistory()
  saveHistory(history.map(item => item.id === id ? updatedRequest : item))

  if (decision === 'APROBADA') {
    savePets(readPets().map(pet =>
      pet.id === request.mascotaId ? { ...pet, estado: 'ADOPTADA' } : pet
    ))
  }

  return updatedRequest
}
