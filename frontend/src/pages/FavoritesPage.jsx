import { Heart, Trash2 } from 'lucide-react'
import { useEffect, useState } from 'react'
import { favoritesApi } from '../services/api'

export default function FavoritesPage() {
  const [favorites, setFavorites] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    let active = true
    favoritesApi.list()
      .then(({ data }) => { if (active) setFavorites(data) })
      .catch(err => { if (active) setError(err.response?.data?.mensaje || 'No pudimos cargar tus favoritos.') })
      .finally(() => { if (active) setLoading(false) })
    return () => { active = false }
  }, [])

  async function remove(pet) {
    try {
      await favoritesApi.remove(pet.id)
      setFavorites(current => current.filter(item => item.id !== pet.id))
    } catch (err) {
      setError(err.response?.data?.mensaje || 'No pudimos actualizar tus favoritos.')
    }
  }

  return <div><header><p className="eyebrow">Tus elecciones</p><h1 className="mt-2 font-display text-4xl font-bold text-ink sm:text-5xl">Mis favoritos.</h1><p className="mt-3 text-[#81786F]">Guarda las mascotas que quieres conocer con más calma.</p></header>{error && <p role="alert" className="mt-6 rounded-xl bg-coral-50 px-4 py-3 text-sm font-semibold text-coral-700">{error}</p>}{loading ? <div className="surface mt-10 h-56 animate-pulse bg-[#F1ECE5]" /> : favorites.length ? <div className="mt-10 grid gap-6 sm:grid-cols-2 xl:grid-cols-3">{favorites.map(pet => <article key={pet.id} className="overflow-hidden rounded-2xl border border-[#EEE7DD] bg-white shadow-soft"><div className="relative aspect-[4/3] bg-sage-100"><img src={pet.imagenUrl} alt={pet.nombre} className="h-full w-full object-cover" /><button type="button" onClick={() => remove(pet)} className="absolute right-4 top-4 grid h-9 w-9 place-items-center rounded-full bg-white/90 text-coral-600 shadow-sm" title="Quitar de favoritos"><Heart size={17} fill="currentColor" /></button></div><div className="flex items-end justify-between gap-3 p-5"><div><h2 className="font-display text-2xl font-bold text-ink">{pet.nombre}</h2><p className="mt-1 text-xs font-semibold uppercase tracking-wider text-[#9C9389]">{pet.especie} · {pet.raza}</p></div><button type="button" onClick={() => remove(pet)} className="grid h-10 w-10 place-items-center rounded-xl bg-coral-50 text-coral-600" title="Eliminar favorito"><Trash2 size={17} /></button></div></article>)}</div> : !error && <div className="surface mt-10 flex flex-col items-center px-6 py-20 text-center"><div className="grid h-14 w-14 place-items-center rounded-2xl bg-coral-50 text-coral-600"><Heart size={26} /></div><h2 className="mt-5 font-display text-2xl font-bold">Aún no tienes favoritos</h2><p className="mt-2 max-w-sm text-sm leading-6 text-[#81786F]">Pulsa el corazón de una mascota en el catálogo para guardarla aquí.</p></div>}</div>
}
