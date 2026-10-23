export default function Toast({ message, type = 'success', onClose }) {
  if (!message) return null
  return <div className={`fixed bottom-5 right-5 z-50 max-w-sm rounded-xl border px-4 py-3 text-sm font-semibold shadow-float ${type === 'error' ? 'border-coral-200 bg-coral-50 text-coral-700' : 'border-sage-200 bg-sage-50 text-sage-700'}`} role="status"><div className="flex items-center gap-4"><span>{message}</span><button onClick={onClose} className="text-lg leading-none opacity-60" aria-label="Cerrar">×</button></div></div>
}
