import axios from 'axios'
import { normalizePet } from '../data/petImages'

const api = axios.create({ baseURL: '/api' })

function normalizePetResponse(response) {
  return { ...response, data: normalizePet(response.data) }
}

function normalizePetListResponse(response) {
  return { ...response, data: response.data.map(normalizePet) }
}

api.interceptors.response.use(
  response => response,
  error => {
    const url = error.config?.url || ''
    const isAuthenticationRequest = url.includes('/auth/login') || url.includes('/auth/registro')
    if (error.response?.status === 401 && !isAuthenticationRequest && api.defaults.headers.common.Authorization) {
      window.dispatchEvent(new Event('adopet:session-expired'))
    }
    return Promise.reject(error)
  },
)

export function setApiToken(token) {
  if (token) api.defaults.headers.common.Authorization = `Bearer ${token}`
  else delete api.defaults.headers.common.Authorization
}

export const authApi = {
  register: (payload) => api.post('/auth/registro', payload),
  login: (payload) => api.post('/auth/login', payload),
}

export const petsApi = {
  list: (params) => api.get('/mascotas', { params }).then(normalizePetListResponse),
  create: (payload) => api.post('/mascotas', payload).then(normalizePetResponse),
  update: (id, payload) => api.put(`/mascotas/${id}`, payload).then(normalizePetResponse),
  deactivate: (id) => api.patch(`/mascotas/${id}/desactivar`),
  detail: (id) => api.get(`/mascotas/${id}`).then(normalizePetResponse),
}

export const favoritesApi = {
  list: () => api.get('/favoritos').then(normalizePetListResponse),
  add: (petId) => api.put(`/favoritos/${petId}`),
  remove: (petId) => api.delete(`/favoritos/${petId}`),
}

export const requestsApi = {
  mine: () => api.get('/solicitudes/mias'),
  pending: () => api.get('/solicitudes/pendientes'),
  create: (payload) => api.post('/solicitudes', payload),
  decide: (id, payload) => api.patch(`/solicitudes/${id}/decision`, payload),
  history: () => api.get('/historial'),
}

export const usersApi = {
  detail: (id) => api.get(`/usuarios/${id}`),
}

export default api
