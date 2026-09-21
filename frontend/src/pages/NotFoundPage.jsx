import { Link } from 'react-router-dom'

export default function NotFoundPage() {
  return <div className="grid min-h-screen place-items-center bg-cream p-6 text-center"><div><p className="eyebrow">404</p><h1 className="mt-3 font-display text-5xl font-bold">Esta huella se perdió.</h1><p className="mt-4 text-[#81786F]">La página que buscas no está aquí.</p><Link to="/" className="primary-button mt-7">Volver a Adopet</Link></div></div>
}
