const styles = {
  DISPONIBLE: 'bg-sage-100 text-sage-700',
  NO_DISPONIBLE: 'bg-[#F2ECE5] text-[#827568]',
  ADOPTADA: 'bg-coral-100 text-coral-700',
  PENDIENTE: 'bg-[#FFF2D8] text-[#9A6A12]',
  EN_REVISION: 'bg-[#E4EFF2] text-[#3F6E7A]',
  APROBADA: 'bg-sage-100 text-sage-700',
  RECHAZADA: 'bg-coral-100 text-coral-700',
  FINALIZADA: 'bg-[#EDE8F4] text-[#66527E]',
}

export default function StatusPill({ status }) {
  return <span className={`inline-flex rounded-full px-3 py-1 text-[11px] font-bold ${styles[status] || 'bg-[#F2ECE5] text-[#827568]'}`}>{status?.replace('_', ' ')}</span>
}
