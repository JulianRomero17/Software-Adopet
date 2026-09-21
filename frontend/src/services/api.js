import axios from 'axios'

const api = axios.create({ baseURL: '/api' })

export function setApiToken(token) {
  if (token) api.defaults.headers.common.Authorization = `Bearer ${token}`
  else delete api.defaults.headers.common.Authorization
}

export const authApi = {
  register: (payload) => api.post('/auth/registro', payload),
  login: (payload) => api.post('/auth/login', payload),
}

export const petsApi = {
  list: (params) => api.get('/mascotas', { params }),
  create: (payload) => api.post('/mascotas', payload),
  update: (id, payload) => api.put(`/mascotas/${id}`, payload),
  deactivate: (id) => api.patch(`/mascotas/${id}/desactivar`),
  detail: (id) => api.get(`/mascotas/${id}`),
}

export const requestsApi = {
  mine: () => api.get('/solicitudes/mias'),
  pending: () => api.get('/solicitudes/pendientes'),
  create: (mascotaId) => api.post('/solicitudes', { mascotaId }),
  decide: (id, payload) => api.patch(`/solicitudes/${id}/decision`, payload),
  history: () => api.get('/historial'),
}

export default api
